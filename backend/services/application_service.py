import logging
import uuid

from sqlalchemy.orm import Session

from models.dto.application_dto import ApplicationDTO
from models.enum.application_statuses import Application_statuses
from models.enum.user_roles import User_roles
from models.tables.application import Application
from models.tables.user import User

from datetime import datetime, timedelta, date, UTC


from models.tables.timeslot import Timeslot

class ApplicationService:
    def __init__(self) -> None:
        logging.basicConfig(level=logging.INFO)
        self.logger = logging.getLogger(__name__)

    async def create_application(
            self,
            user_id,
            application_dto: ApplicationDTO,
            db: Session
        ):
        try:
            new_application = Application (
                user_id = user_id,
                classroom_id = application_dto.classroom_id,
                application_status_id = Application_statuses.Not_processed,
                application_date = datetime.now(UTC) + timedelta(hours=7),
                name = application_dto.name,
                description = application_dto.description,
                class_date = application_dto.class_date,
                time_table_id = application_dto.time_table_id
            )

            application_entry = Application(new_application)
            db.add(application_entry)
            db.commit()

        except Exception as e:
            self.logger.error(f"(Check classroom existence) Error: {e}")
            raise


    async def time_table_id_validate(self, db: Session, time_table_id: int):
        try:
            classroom = db.query(Timeslot).filter(Timeslot.id == time_table_id).first()

            if classroom:
                self.logger.info(f"(Check time existence) time with id {time_table_id} exists")
                return True
            else:
                self.logger.warning(f"(Check time existence) time with id {time_table_id} not found")
                return False
            
        except Exception as e:
            self.logger.error(f"(Check classroom existence) Error: {e}")
            raise


    async def check_priority(
            self,
            db: Session,
            classroom_id: uuid,
            class_date: date, 
            time_table_id: int
            ) -> bool:
        try:
            teacher_at_that_time = db \
                .query(Application, User) \
                .join(User, Application.user_id == User.id) \
                .filter(
                        Application.classroom_id == classroom_id
                        and Application.class_date == class_date
                        and Application.time_table_id == time_table_id
                        and User.role_id == User_roles.Teacher
                    ) \
                .first()

            if teacher_at_that_time:
                self.logger.warning(f"Преподаватель уже занял эту аудиторию")
                return True
            else:
                self.logger.info(f"Преподаватель не занимал эту аудиторию")
                return False

            
        except Exception as e:
            self.logger.error(f"(Check classroom existence) Error: {e}")
            raise


    async def delete_all_students(
            self,
            db: Session,
            classroom_id: uuid,
            class_date: date, 
            time_table_id: int
            ) -> None:
        try:
            student_at_that_time = db \
                .query(Application, User) \
                .join(User, Application.user_id == User.id) \
                .filter(
                        Application.classroom_id == classroom_id
                        and Application.class_date == class_date
                        and Application.time_table_id == time_table_id
                        and User.role_id == User_roles.Student
                    ) \
                .all()
            
            for application in student_at_that_time:
                application.application_status_id = Application_statuses.Rejected

            db.commit()

        except Exception as e:
            self.logger.error(f"(Check classroom existence) Error: {e}")
            raise