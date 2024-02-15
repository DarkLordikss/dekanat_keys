import logging
import uuid

from sqlalchemy.orm import Session
from sqlalchemy import or_
from typing import Dict, List, Union, Optional
from collections import defaultdict
from uuid import UUID


from models.dto.application_create_dto import ApplicationCreateDTO
from models.dto.application_showing_dto import ApplicationShowingDTO
from models.dto.formatted_application_dto import FormattedTimetable, Pair
from models.enum.application_statuses import Application_statuses
from models.enum.user_roles import User_roles
from models.tables.application import Application
from models.tables.classroom import Classroom
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
                application_status_id=Application_statuses.Not_processed.value,
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
                    or_(User.role_id == User_roles.Teacher.value, User.id == user_id)
                ) \
                .all()

            for application, user in teacher_at_that_time:
                if application.user_id == user_id:
                    self.logger.warning(f"вы уже заняли эту аудиторию")
                    return 0
                elif application.user_id != user_id:
                    self.logger.warning(f"Преподаватель не занимал эту аудиторию")
                    return 1

            self.logger.info(f"Преподаватель не занимал эту аудиторию")
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
                    or_(User.role_id == User_roles.Student.value, User.id == user_id)
                ) \
                .all()

            for application, user in student_at_that_time:
                if application.user_id == user_id:
                    self.logger.warning(f"Вы уже заняли эту аудиторию")
                    return True

                application.application_status_id = Application_statuses.Rejected.value

            db.commit()
            return False

        except Exception as e:
            self.logger.error(f"(delete_all_students) Error: {e}")
            raise


    async  def show_applications(
            self,
            db: Session,
            application_showing_dto: ApplicationShowingDTO
    ):
        subquery_scheduled = db \
            .query(Application) \
            .filter(Application.class_date == application_showing_dto.date) \
            .filter(or_(
                Application.application_status_id == Application_statuses.Confirmed.value,
                Application.application_status_id == Application_statuses.Key_received.value
            )) \
            .subquery()

        test = db.query(Application).filter(Application.class_date == application_showing_dto.date)
        result = db.query(test.exists()).scalar()

        # Проверяем результат
        if result:
            self.logger.info(f"Подзапрос нашел что-то.{application_showing_dto.date}")
        else:
            self.logger.info(f"Подзапрос ничего не нашел.{application_showing_dto.date}")

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
        formatted_timetable : FormattedTimetable = {}

        for classroom in result_applications:
            class_number = classroom.time_table_id
            self.logger.info(f"ЗАПРОС ВЕРЕН МНЕ classroom: {classroom}")
            self.logger.info(f"ЗАПРОС ВЕРЕН МНЕ classroom.building: {classroom[0].building}")
            self.logger.info(f"ЗАПРОС ВЕРЕН МНЕ classroom.number: {classroom[0].number}")
            ##self.logger.info(f"ЗАПРОС {subquery_scheduled.name.scalar()}")
            pair = Pair(
                classroom_id=classroom.id,
                name=classroom.name,
                description=classroom.description,
                buildings=classroom[0].building,
                class_number=classroom[0].number
            )
            self.logger.info(f"Я РУССКИЙ")
            class_number = classroom.time_table_id

            if class_number in formatted_timetable:
                formatted_timetable[class_number].append(pair)
            else:
                formatted_timetable[class_number] = [pair]

        return formatted_timetable




    def fill_timetable_data(result_applications, formatted_timetable: FormattedTimetable):
        for classroom, subquery_scheduled in result_applications:
            pair = Pair(**{
                "classroom_id": classroom.id,
                "name": subquery_scheduled.c.name,
                "description": subquery_scheduled.c.description,
                "buildings": classroom.building,
                "class_number": classroom.number
            })
            class_number = subquery_scheduled.c.time_table_id

        if class_number in formatted_timetable.timetable:
            formatted_timetable.timetable[class_number].append(pair)
        else:
            formatted_timetable.timetable[class_number] = [pair]
