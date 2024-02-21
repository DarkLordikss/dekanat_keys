import logging
import uuid

from sqlalchemy.orm import Session
from sqlalchemy import or_, and_
from typing import List

from models.dto.application_create_dto import ApplicationCreateDTO
from models.dto.application_showing_dto import ApplicationShowingDTO
from models.dto.application_showing_with_status_dto import ApplicationShowingWithStatusDTO
from models.dto.formatted_application_dto import Pair
from models.enum.applicationstatuses import ApplicationStatuses
from models.enum.userroles import UserRoles
from models.tables.application import Application
from models.tables.classroom import Classroom
from models.tables.confirm_status import ConfirmStatus
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
            db: Session
    ):
        try:
            new_application = Application(
                user_id=user_id,
                classroom_id=application_dto.classroom_id,
                application_status_id=ApplicationStatuses.Not_processed.value,
                application_date=datetime.utcnow() + timedelta(hours=7),
                name=application_dto.name,
                description=application_dto.description,
                class_date=application_dto.class_date,
                time_table_id=application_dto.time_table_id
            )

            db.add(new_application)
            db.commit()

        except Exception as e:
            self.logger.error(f"(async def create_application) Error: {e}")
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

    @staticmethod
    async def show_applications(
            db: Session,
            application_showing_dto: ApplicationShowingDTO
    ):
        subquery_scheduled = db \
            .query(Application) \
            .filter(Application.class_date == application_showing_dto.date) \
            .filter(or_(
                Application.application_status_id == ApplicationStatuses.Confirmed.value,
                Application.application_status_id == ApplicationStatuses.Key_received.value
            )) \
            .subquery()

        query = db \
            .query(Classroom, subquery_scheduled) \
            .filter(Classroom.building == application_showing_dto.building) \
            .filter(Classroom.number.in_(application_showing_dto.classrooms))

        if application_showing_dto.scheduled:
            query = query.join(subquery_scheduled, subquery_scheduled.c.classroom_id == Classroom.id)
        else:
            query = query.join(subquery_scheduled, subquery_scheduled.c.classroom_id != Classroom.id)

        if application_showing_dto.user_id is not None:
            query = query.filter(subquery_scheduled.c.user_id == application_showing_dto.user_id)

        result_applications = query.all()
        formatted_timetable = ApplicationService.fill_timetable_data(result_applications)

        return formatted_timetable

    @staticmethod
    async def show_applications_with_status(
            db: Session,
            application_showing_with_status_dto: ApplicationShowingWithStatusDTO
    ):
        subquery_statused = db \
            .query(Application) \
            .filter(Application.class_date == application_showing_with_status_dto.date) \
            .filter(or_(Application.application_status_id == status.value for status in application_showing_with_status_dto.statuses)) \
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
            pair = Pair(
                classroom_id=classroom.id,
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

    @staticmethod
    def fill_timetable_data(result_applications):
        formatted_timetable = {}

        for classroom in result_applications:
            pair = Pair(
                classroom_id=classroom.id,
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
            application = db.query(Application).filter(Application.id == application_id).first()

            if not application:
                raise FileNotFoundError

            if user.role_id != UserRoles.Dean_office_employee.value:
                raise PermissionError

            if new_status <= application.application_status_id or new_status > len(ApplicationStatuses):
                raise KeyError

            application.application_status_id = new_status
            db.commit()

            self.logger.info(f"(Change application status) Application status with id {application_id} updated "
                             f"successfully")
            return application_id
        except FileNotFoundError:
            self.logger.warning(f"(Change application status) Application with id {application_id} not found")
            raise
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
