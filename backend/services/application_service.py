import logging
import uuid

from sqlalchemy.orm import Session
from uuid import UUID

from sqlalchemy import or_, and_
from sqlalchemy.sql import text
from sqlalchemy.orm import aliased


from typing import List

from models.dto.application_create_dto import ApplicationCreateDTO
from models.dto.available_classrooms_dto import AvailableClassroomsShowingDTO
from models.dto.application_showing_with_status_dto import ApplicationShowingWithStatusDTO
from models.dto.formatted_application_with_status_dto import PairWithStatus, ApplicationInfDTO
from models.dto.formatted_available_classrooms_dto import ClassroomForPair, ClassroomForPairWithTrack
from models.enum.applicationstatuses import ApplicationStatuses
from models.enum.userroles import UserRoles
from models.tables.application import Application
from models.tables.classroom import Classroom
from models.tables.confirm_status import ConfirmStatus
from models.tables.role import Role
from models.tables.transfering_application import TransferingApplication
from models.tables.user import User

from datetime import datetime, timedelta, date

from models.tables.timeslot import Timeslot


class ApplicationService:
    def __init__(self) -> None:
        logging.basicConfig(level=logging.INFO)
        self.logger = logging.getLogger(__name__)

    async def create_application(
            self,
            user_id,
            application_dto: ApplicationCreateDTO,
            group_id,
            db: Session
    ):
        try:
            new_application = Application(
                user_id=user_id,
                classroom_id=application_dto.classroom_id,
                application_status_id=ApplicationStatuses.Not_processed.value,
                application_date=datetime.utcnow() + timedelta(hours=7),
                name=application_dto.name,
                application_group_id=group_id,
                description=application_dto.description,
                class_date=application_dto.class_date,
                time_table_id=application_dto.time_table_id
            )

            db.add(new_application)
            db.commit()

        except Exception as e:
            self.logger.error(f"(async def create_application) Error: {e}")
            raise

    async def check_application_existence(
            self,
            db: Session,
            application_id: str,
            user_id: str
    ) -> bool:
        try:
            application = db.query(Application).filter(and_(Application.id == application_id,
                                                            Application.user_id == user_id)).first()

            if application:
                self.logger.info(f"(Checking Application existence) Got application with ID: {application.id}")
                return True
            else:
                self.logger.warning(f"(Checking Application existence) No same application found: {application_id}")
                return False

        except Exception as e:
            self.logger.error(f"(Checking user Application existence) Error: {e}")
            raise

    async def time_table_id_validate(self, db: Session, time_table_id: int):
        try:
            classroom = db.query(Timeslot).filter(Timeslot.id == time_table_id).first()

            if classroom:
                self.logger.info(f"(time_table_id_validate) time with id {time_table_id} exists")
                return False
            else:
                self.logger.warning(f"(time_table_id_validate) time with id {time_table_id} not found")
                return True

        except Exception as e:
            self.logger.error(f"(time_table_id_validate) Error: {e}")
            raise

    async def check_priority(
            self,
            db: Session,
            classroom_id: uuid,
            class_date: date,
            time_table_id: int,
            user_id: uuid
    ) -> int:
        try:
            teacher_at_that_time = db \
                .query(Application, User) \
                .join(User, Application.user_id == User.id) \
                .filter(
                    Application.classroom_id == classroom_id,
                    Application.class_date == class_date,
                    Application.time_table_id == time_table_id,
                    or_(User.role_id == UserRoles.Teacher.value, User.id == user_id)
                ) \
                .all()

            for application, user in teacher_at_that_time:
                if application.user_id == user_id:
                    self.logger.warning(f"(Check_priority) Пользователь ({user_id}) уже заняли эту аудиторию")
                    return 0
                elif application.user_id != user_id:
                    self.logger.warning(f"(Check_priority) Преподаватель не занимал эту аудиторию")
                    return 1

            self.logger.info(f"(Check_priority) Преподаватель не занимал эту аудиторию")
            return 2

        except Exception as e:
            self.logger.error(f"(Check_priority) Error: {e}")
            raise

    async def delete_all_students(
            self,
            db: Session,
            classroom_id: uuid,
            class_date: date,
            time_table_id: int,
            user_id: uuid
    ) -> bool:
        try:
            student_at_that_time = db \
                .query(Application, User) \
                .join(User, Application.user_id == User.id) \
                .filter(
                    Application.classroom_id == classroom_id,
                    Application.class_date == class_date,
                    Application.time_table_id == time_table_id,
                    or_(User.role_id == UserRoles.Student.value, User.id == user_id)
                ) \
                .all()

            for application, user in student_at_that_time:
                if application.user_id == user_id:
                    self.logger.warning(f"(Delete all students) Пользователь ({user_id}) уже заняли эту аудиторию")
                    return True

                application.application_status_id = ApplicationStatuses.Rejected.value

            db.commit()
            return False

        except Exception as e:
            self.logger.error(f"(Delete all students) Error: {e}")
            raise

    async def check_correct_statuses(self, db: Session, statuses: List[int]) -> bool:
        try:
            filter_condition = (
                or_(ConfirmStatus.id == status for status in statuses)
            )
            count = db.query(ConfirmStatus).filter(filter_condition).count()

            if count == len(statuses):
                self.logger.info(f"(Check correct statuses) statuses with id {statuses} exist")
                return False
            else:
                self.logger.warning(f"(Check correct statuses) statuses with id {statuses} not found")
                return True

        except Exception as e:
            self.logger.error(f"(Check correct statuses) Error: {e}")
            raise

    async def track_keys(self, db: Session) -> List[ClassroomForPairWithTrack]:
        keys = db.query(Classroom, Application) \
            .outerjoin(Application, Classroom.id == Application.classroom_id) \
            .all()

        keys_dto_dict = {}

        for key in keys:
            classroom_id = key[0].id
            user_id = key[1].user_id if key[1] and key[1].application_status_id == 3 else None

            if classroom_id in keys_dto_dict:
                if user_id:
                    keys_dto_dict[classroom_id] = ClassroomForPairWithTrack(
                        classroom_id=classroom_id,
                        buildings=key[0].building,
                        class_number=key[0].number,
                        user_id=user_id,
                        name=db.query(User).filter((User.id == user_id) & User.is_verified).first().full_name
                    )
            else:
                keys_dto_dict[classroom_id] = ClassroomForPairWithTrack(
                    classroom_id=classroom_id,
                    buildings=key[0].building,
                    class_number=key[0].number,
                    user_id=user_id
                )

        return list(keys_dto_dict.values())


    @staticmethod
    async def show_available_classrooms(
                                        db: Session,
                                        available_classrooms_showing_dto: AvailableClassroomsShowingDTO
                                        ):
        cartesian_product_with_filters = (
            'SELECT * FROM classrooms, timeslots '
            'WHERE '
            'classrooms.building = :building '
            'AND classrooms.number IN :class_numbers '
            'AND NOT EXISTS '
            '(SELECT * FROM applications '
            'WHERE applications.class_date = :date_value '
            'AND applications.application_status_id IN (:status_confirmed, :status_key_received) '
            'AND applications.classroom_id = classrooms.id '
            'AND applications.time_table_id = timeslots.id)'
        )
        query_result = db.execute(text(cartesian_product_with_filters), {
            'building': available_classrooms_showing_dto.building,
            'class_numbers': tuple(available_classrooms_showing_dto.classrooms),
            'date_value': available_classrooms_showing_dto.date,
            'status_confirmed': ApplicationStatuses.Confirmed.value,
            'status_key_received': ApplicationStatuses.Key_received.value
        }).fetchall()


        available_classrooms = ApplicationService.fill_timetable_data(query_result)

        return available_classrooms

    @staticmethod
    def fill_timetable_data(result_classrooms):
        formatted_timetable = {}

        for element in result_classrooms:
            pair = ClassroomForPair(
                classroom_id=element[0],
                buildings=element.building,
                class_number=element.number
            )
            pair_number = element.id

            if pair_number in formatted_timetable:
                formatted_timetable[pair_number].append(pair)
            else:
                formatted_timetable[pair_number] = [pair]

        return formatted_timetable

    async def show_concrete_application(self, application_id: UUID, db: Session) -> ApplicationInfDTO:
        application = db.query(Application, Classroom).join(Classroom, Classroom.id == Application.classroom_id).filter(Application.id == application_id).first()
        application_dto = ApplicationInfDTO(
            application_id=application[0].id,
            classroom_id=application[0].classroom_id,
            user_id=application[0].user_id,
            status=application[0].application_status_id,
            name=application[0].name,
            description=application[0].description,
            application_date=application[0].application_date.date(),
            class_date=application[0].class_date,
            time_table_id=application[0].time_table_id,
            building=application[1].building,
            class_number=application[1].number
        )
        return application_dto

    async def show_notifications(self, user_id: UUID, db: Session) -> list[ApplicationInfDTO]:
        applications = db \
            .query(Application, TransferingApplication, Classroom) \
            .join(TransferingApplication, and_(TransferingApplication.user_recipient_id == user_id, Application.user_id == TransferingApplication.user_sender_id)) \
            .join(Classroom, Classroom.id == Application.classroom_id) \
            .all()

        applications_dto = []
        for application, transfering_application, classroom in applications:
            application_dto = ApplicationInfDTO(
                application_id=application.id,
                classroom_id=application.classroom_id,
                user_id=application.user_id,
                status=application.application_status_id,
                name=application.name,
                description=application.description,
                application_date=application.application_date.date(),
                class_date=application.class_date,
                time_table_id=application.time_table_id,
                building=classroom.building,
                class_number=classroom.number
            )

            applications_dto.append(application_dto)

        return applications_dto


    @staticmethod
    async def show_my_applications(
            db: Session,
            application_showing_with_status_dto: ApplicationShowingWithStatusDTO
    ):
        subquery_statused = db \
            .query(Application) \
            .filter(Application.class_date == application_showing_with_status_dto.date) \
            .filter(or_(Application.application_status_id == status.value
                        for status in application_showing_with_status_dto.statuses)) \
            .subquery()

        query = db \
            .query(Classroom, subquery_statused)

        query = query.join(subquery_statused, subquery_statused.c.classroom_id == Classroom.id)

        if application_showing_with_status_dto.user_id is not None:
            query = query.filter(subquery_statused.c.user_id == application_showing_with_status_dto.user_id)

        result_applications = query.all()
        formatted_timetable = ApplicationService.fill_timetable_data_with_status(result_applications)

        return formatted_timetable

    @staticmethod
    async def show_applications_with_status(
            db: Session,
            application_showing_with_status_dto: ApplicationShowingWithStatusDTO
    ):
        subquery_statused = db \
            .query(Application) \
            .filter(Application.class_date == application_showing_with_status_dto.date) \
            .filter(or_(Application.application_status_id == status.value
                        for status in application_showing_with_status_dto.statuses)) \
            .subquery()

        query = db \
            .query(Classroom, subquery_statused) \
            .filter(Classroom.building == application_showing_with_status_dto.building) \
            .filter(Classroom.number.in_(application_showing_with_status_dto.classrooms))

        query = query.join(subquery_statused, subquery_statused.c.classroom_id == Classroom.id)

        if application_showing_with_status_dto.user_id is not None:
            query = query.filter(subquery_statused.c.user_id == application_showing_with_status_dto.user_id)

        result_applications = query.all()
        formatted_timetable = ApplicationService.fill_timetable_data_with_status(result_applications)

        return formatted_timetable

    @staticmethod
    def fill_timetable_data_with_status(result_applications):
        formatted_timetable = {}
        for classroom in result_applications:
            pair = PairWithStatus(
                application_id=classroom[1],
                classroom_id=classroom[3],
                status=classroom.application_status_id,
                name=classroom.name,
                description=classroom.description,
                buildings=classroom[0].building,
                class_number=classroom[0].number
            )
            pair_number = classroom.time_table_id

            if pair_number in formatted_timetable:
                formatted_timetable[pair_number].append(pair)
            else:
                formatted_timetable[pair_number] = [pair]

        return formatted_timetable

    async def change_application_status(self, db: Session, application_id: str, user: User, new_status: int):
        try:
            app = await self.get_application_by_id(db, application_id)

            if new_status is ApplicationStatuses.Confirmed.value:
                applications = db.query(Application).filter(Application.application_group_id == app.application_group_id).all()
            else:
                applications = [app]

            for application in applications:
                if user.role_id != UserRoles.Dean_office_employee.value:
                    raise PermissionError

                if new_status <= application.application_status_id or new_status > len(ApplicationStatuses):
                    raise KeyError

                application.application_status_id = new_status
                db.commit()

                if (new_status is ApplicationStatuses.Confirmed.value or
                        new_status is ApplicationStatuses.Key_received.value):
                    other_applications = db.query(Application).filter(
                        (Application.class_date == application.class_date) &
                        (Application.time_table_id == application.time_table_id) &
                        (Application.classroom_id == application.classroom_id)
                    ).all()

                    for other_application in other_applications:
                        if other_application.id != application.id:
                            other_application.application_status_id = ApplicationStatuses.Rejected.value

                    db.commit()

                self.logger.info(f"(Change application status) Application status with id {application_id} updated "
                                 f"successfully")
            return application_id
        except PermissionError:
            self.logger.warning(f"(Change application status) User with id {user.id} has no permission for this "
                                f"application")
            raise
        except KeyError:
            self.logger.warning(f"(Change application status) Incorrect status")
            raise
        except Exception as e:
            self.logger.error(f"(Change application status)  Error: {e}")
            raise

    async def get_application_by_id(self, db: Session, application_id: str):
        try:
            application = db.query(Application).filter(Application.id == application_id).first()

            if not application:
                raise FileNotFoundError

            return application

        except FileNotFoundError:
            self.logger.warning(f"(Get application by id) Application with id {application_id} not found")
            raise
        except Exception as e:
            self.logger.error(f"(Get application by id)  Error: {e}")
            raise
