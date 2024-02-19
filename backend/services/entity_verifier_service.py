import logging

from sqlalchemy.orm import Session

from typing import Type
from sqlalchemy.ext.declarative import declarative_base
from datetime import date

Base = declarative_base()


class EntityVerifierService:
    def __init__(self):
        logging.basicConfig(level=logging.INFO)
        self.logger = logging.getLogger(__name__)

    async def check_existence(self, db: Session, model: Type[Base], filter_condition: any, log_info: str,
                              log_warning: str, **kwargs) -> bool:
        try:
            item = db.query(model).filter(filter_condition).first()

            if item:
                self.logger.info(log_info)
                return False
            else:
                self.logger.warning(log_warning)
                return True

        except Exception as e:
            self.logger.error(f"Error: {e}")
            raise

    @staticmethod
    async def check_correct_dates(start_date: date, end_date: date):
        if end_date >= start_date or end_date is None:
            return False
        else:
            return True
