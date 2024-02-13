import uuid

from sqlalchemy import Column, String, UUID
from backend.storage.db_config import Base


class CRL(Base):
    __tablename__ = "crl"

    id = Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4, unique=True, nullable=False)
    token = Column(String, index=True, nullable=False)
