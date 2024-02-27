import logging

from sqlalchemy.orm import Session

from models.dto.classroom_dto import ClassroomDTO
from models.tables.classroom import Classroom


class BuildingService:
    def __init__(self):
        logging.basicConfig(level=logging.INFO)
        self.logger = logging.getLogger(__name__)

    async def get_all_buildings(self, db: Session) -> list:
        try:
            buildings = db.query(Classroom.building).distinct().all()

            unique_buildings = [building[0] for building in buildings]

            self.logger.info(f"(Get all buildings) Success: {unique_buildings}")
            return sorted(unique_buildings)
        except Exception as e:
            self.logger.error(f"(Get all buildings) Error: {e}")
            raise

    async def get_all_classrooms_from_building(self, db: Session, building: int) -> list:
        try:
            classrooms = db.query(Classroom).filter(Classroom.building == building).all()

            classrooms_dtos = [
                ClassroomDTO(
                    id=str(classroom.id),
                    building=str(classroom.building),
                    number=str(classroom.number),
                    address=str(classroom.address)
                ) for classroom in classrooms
            ]

            self.logger.info(f"(Get building classrooms) Success: {classrooms_dtos}")
            return sorted(classrooms_dtos, key=lambda x: x.number)
        except Exception as e:
            self.logger.error(f"(Get building classrooms) Error: {e}")
            raise
