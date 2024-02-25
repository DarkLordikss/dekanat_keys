import uuid

from sqlalchemy import Column, String, UUID, Integer, DateTime, Date
from storage.db_config import Base


class TransferingApplication(Base):
    __tablename__ = "transfering_applications"

    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4, unique=True, nullable=False)
    application_id = Column(UUID(as_uuid=True), nullable=False)
    user_recipient_id = Column(UUID(as_uuid=True), nullable=False)
    user_owner_id = Column(UUID(as_uuid=True), nullable=False)

