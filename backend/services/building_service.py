import logging

from sqlalchemy.orm import Session

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
