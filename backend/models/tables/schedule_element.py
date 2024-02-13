import uuid

from sqlalchemy import Column, UUID, Integer, Date
from storage.db_config import Base


class ScheduleElement(Base):
    __tablename__ = "schedule_elements"

    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4, unique=True, nullable=False)
    date = Column(Date, nullable=False)
    timetable_id = Column(Integer, nullable=False)
