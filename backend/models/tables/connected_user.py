import uuid

from sqlalchemy import Column, String, UUID, Integer, Boolean
from storage.db_config import Base


class ConnectedUser(Base):
    __tablename__ = "connected_users"

    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4, unique=True, nullable=False)
    websocket_id = Column(String, index=True)

