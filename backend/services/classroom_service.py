import logging
from uuid import UUID

from sqlalchemy.orm import Session

from models.tables.classroom import Classroom


class ClassroomService:
    def __init__(self):
        logging.basicConfig(level=logging.INFO)
        self.logger = logging.getLogger(__name__)

    async def get_all_classrooms(self, db: Session) -> list:
        try:
            classrooms = db.query(Classroom).all()

            if not classrooms or len(classrooms) != 0:
                self.logger.info(f"(Get all classroom) Got classrooms, len: {len(classrooms)}")
            else:
                self.logger.warning(f"(Get all classroom) No classrooms found")

            return list(classrooms)
        
        except Exception as e:
            self.logger.error(f"(Get all classroom) Error: {e}")
            raise


    async def check_correct_classroom(self, db: Session, classroom_id: str) -> bool:
        try:
            classroom = db.query(Classroom).filter(Classroom.id == classroom_id).first()
            
            if classroom:
                self.logger.info(f"(Check classroom existence) Classroom with id {classroom_id} exists")
                return False
            else:
                self.logger.warning(f"(Check classroom existence) Classroom with id {classroom_id} not found")
                return True
            
        except Exception as e:
            self.logger.error(f"(Check classroom existence) Error: {e}")
            raise

