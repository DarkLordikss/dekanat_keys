import logging

from sqlalchemy.orm import Session

from models.dto.status_dto import StatusDTO
from models.dto.statuses_dto import StatusesDTO
from models.tables.classroom import Classroom

from typing import List
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import or_, and_

from models.tables.confirm_status import ConfirmStatus

Base = declarative_base()


class StatusService:
    def __init__(self):
        logging.basicConfig(level=logging.INFO)
        self.logger = logging.getLogger(__name__)


    async def get_all_statuses(self, db: Session) -> StatusesDTO:
        try:
            statuses = list(db.query(ConfirmStatus).all())

            if not statuses or len(statuses) != 0:
                self.logger.info(f"(Get all statuses) Got statuses, len: {len(statuses)}")
            else:
                self.logger.warning(f"(Get all statuses) No statuses found")

            statuses_dto = [
                StatusDTO(
                    id=status.id,
                    name=status.name
                ) for status in statuses
            ]

            return statuses_dto

        except Exception as e:
            self.logger.error(f"(Get all classroom) Error: {e}")
            raise
