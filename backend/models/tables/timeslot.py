from sqlalchemy import Column, Integer, Time
from storage.db_config import Base


class Timeslot(Base):
    __tablename__ = "timeslots"

    id = Column(Integer, primary_key=True, unique=True, nullable=False)
    start_time = Column(Time, nullable=False)
    end_time = Column(Time, nullable=False)
