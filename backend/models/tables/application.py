import uuid

from sqlalchemy import Column, String, UUID, Integer, DateTime, Date
from storage.db_config import Base


class Application(Base):
    __tablename__ = "applications"

    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4, unique=True, nullable=False)
    user_id = Column(UUID(as_uuid=True), nullable=False)
    classroom_id = Column(UUID(as_uuid=True), nullable=False)
    #schedule_element_id = Column(UUID(as_uuid=True), nullable=False)
    application_status_id = Column(Integer, nullable=False)
    application_date = Column(DateTime, nullable=False)
    name = Column(String, nullable=False)
    description = Column(String, nullable=True)
    class_date = Column(Date, nullable=False)
    time_table_id = Column(Integer, nullable=False)
