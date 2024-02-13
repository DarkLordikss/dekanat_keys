import logging

from sqlalchemy.orm import Session

from models.tables.user import User
from models.tables.role import Role


class UserService:
    def __init__(self):
        logging.basicConfig(level=logging.INFO)
        self.logger = logging.getLogger(__name__)

    async def get_user_by_email(self, db: Session, email: str) -> User:
        try:
            user = db.query(User).filter(User.email == email).first()

            if user:
                self.logger.info(f"(Email user getting) Got user with ID: {user.id}")
            else:
                self.logger.warning(f"(Email user getting) No same user found: {email}")

            return user
        except Exception as e:
            self.logger.error(f"(Email user getting) Error: {e}")
            raise

    async def get_user_by_id(self, db: Session, _id: str) -> User:
        try:
            user = db.query(User).filter(User.id == _id).first()

            if user:
                self.logger.info(f"(User id getting) Got user with ID: {user.id}")
            else:
                self.logger.warning(f"(User id getting) No same user found: {_id}")

            return user
        except Exception as e:
            self.logger.error(f"(User id getting) Error: {e}")
            raise

    async def get_role_by_id(self, db: Session, _id: str) -> Role:
        try:
            role = db.query(Role).filter(Role.id == _id).first()

            self.logger.info(f"(Role id getting) Got role with ID: {role.id}")

            return role
        except Exception as e:
            self.logger.error(f"(Role id getting) Error: {e}")
            raise

    async def verify_password(self, db: Session, email: str, password: str) -> bool:
        try:
            user = await self.get_user_by_email(db, email)

            if not user:
                self.logger.info(f"(Password verify) No same user found: {email}")

                return False

            if user.password == password:
                self.logger.info(f"(Password verify) Success: {email}")

                return True
            else:
                self.logger.warning(f"(Password verify) Failure: {email}")

                return False
        except Exception as e:
            self.logger.error(f"(Password verify) Error: {e}")
            raise
