import logging
import uuid

from sqlalchemy.orm import Session
from sqlalchemy import or_
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
                application_status_id = Application_statuses.Not_processed.value,
                application_date = datetime.now(UTC) + timedelta(hours=7),
                name = application_dto.name,
                description = application_dto.description,
                class_date = application_dto.class_date,
                time_table_id = application_dto.time_table_id
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
            self.logger.error(f"(check_priority) Error: {e}")
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
            self.logger.warning(f"GGGGGGGGGGGGGGGGGGGGGG {User_roles.Student.value}")
            for application, user in student_at_that_time:
                if application.user_id == user_id:
                    self.logger.warning(f"вы уже заняли эту аудиторию")
                    return True
                self.logger.warning(f"GGGGGGGGGGGGGGGGGGGGGG {application.user_id}")
                application.application_status_id = Application_statuses.Rejected.value

            db.commit()
            return False

        except Exception as e:
            self.logger.error(f"(delete_all_students) Error: {e}")
            raise