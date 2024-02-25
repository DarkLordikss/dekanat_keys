import uuid

from sqlalchemy import Column, String, UUID, Integer, Boolean
from storage.db_config import Base


class User(Base):
    __tablename__ = "users"

    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4, unique=True, nullable=False)
    role_id = Column(Integer, nullable=False)
    email = Column(String, nullable=False)
    full_name = Column(String, nullable=False)
    password = Column(String, nullable=False)
    is_verified = Column(Boolean, nullable=True, default=False)
    secret_key = Column(String, nullable=True)
