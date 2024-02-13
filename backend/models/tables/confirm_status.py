from sqlalchemy import Column, String, Integer
from storage.db_config import Base


class ConfirmStatus(Base):
    __tablename__ = "confirm_statuses"

    id = Column(Integer, primary_key=True, unique=True, nullable=False)
    name = Column(String, nullable=False)
