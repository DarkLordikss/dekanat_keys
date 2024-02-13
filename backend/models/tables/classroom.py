import uuid

from sqlalchemy import Column, UUID, Integer, String
from storage.db_config import Base


class Classroom(Base):
    __tablename__ = "classrooms"

    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4, unique=True, nullable=False)
    building = Column(Integer, nullable=False)
    number = Column(Integer, nullable=False)
    address = Column(String, nullable=False)
