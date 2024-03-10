import logging

from sqlalchemy.orm import Session

from models.dto.enum_dto import EnumDTO
from models.dto.enums_dto import EnumsDTO
from models.tables.classroom import Classroom

from typing import List
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import or_, and_

from models.tables.confirm_status import ConfirmStatus
from models.tables.role import Role

Base = declarative_base()


class RoleService:
    def __init__(self):
        logging.basicConfig(level=logging.INFO)
        self.logger = logging.getLogger(__name__)


    async def get_all_roles(self, db: Session) -> EnumsDTO:
        try:
            roles = list(db.query(Role).all())

            if not roles or len(roles) != 0:
                self.logger.info(f"(Get all roles) Got roles, len: {len(roles)}")
            else:
                self.logger.warning(f"(Get all roles) No roles found")

            roles_dto = [
                EnumDTO(
                    id=role.id,
                    name=role.name
                ) for role in roles
            ]

            return roles_dto

        except Exception as e:
            self.logger.error(f"(Get all classroom) Error: {e}")
            raise
