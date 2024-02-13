from sqlalchemy import Column, String, Integer
from storage.db_config import Base


class Role(Base):
    __tablename__ = "roles"

    id = Column(Integer, primary_key=True, unique=True, nullable=False)
    name = Column(String, nullable=False)

