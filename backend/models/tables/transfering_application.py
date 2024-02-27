import uuid

from sqlalchemy import Column, String, UUID, Integer, DateTime, Date
from storage.db_config import Base


class TransferingApplication(Base):
    __tablename__ = "transfering_applications"

    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4, unique=True, nullable=False)
    application_id = Column(UUID(as_uuid=True), nullable=False)
    user_recipient_id = Column(UUID(as_uuid=True), nullable=False)
    user_sender_id = Column(UUID(as_uuid=True), nullable=False)

    def __json__(self):
        return {
            "application_id": str(self.application_id),
            "user_recipient_id": str(self.user_recipient_id),
            "user_sender_id": str(self.user_sender_id)
        }
