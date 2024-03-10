import logging
import random
import string

from sqlalchemy.orm import Session

from models.enum.applicationstatuses import ApplicationStatuses
from models.tables.user import User
from models.tables.role import Role

from models.enum.userroles import UserRoles


class UserService:
    def __init__(self):
        logging.basicConfig(level=logging.INFO)
        self.logger = logging.getLogger(__name__)

    async def get_user_by_email(self, db: Session, email: str) -> User:
        try:
            user = db.query(User).filter((User.email == email) & User.is_verified).first()

            if user:
                self.logger.info(f"(Email user getting) Got user with ID: {user.id}")
            else:
                self.logger.warning(f"(Email user getting) No same user found: {email}")

            return user
        except Exception as e:
            self.logger.error(f"(Email user getting) Error: {e}")
            raise

    async def create_user(self, db: Session, email: str, password: str, full_name: str) -> User:
        try:
            user = User(
                role_id=UserRoles.Student.value,
                email=email,
                password=password,
                full_name=full_name,
                secret_key=''.join(''.join(random.choices(string.ascii_letters + string.digits, k=33)))
            )
            db.add(user)
            db.commit()

            self.logger.error(f"(Creating user) Success: {user}")

            return user
        except Exception as e:
            self.logger.error(f"(Creating user) Error: {e}")
            raise

    async def get_user_by_id(self, db: Session, _id: str) -> User:
        try:
            user = db.query(User).filter((User.id == _id) & User.is_verified).first()

            if user:
                self.logger.info(f"(User id getting) Got user with ID: {user.id}")
            else:
                self.logger.warning(f"(User id getting) No same user found: {_id}")

            return user
        except Exception as e:
            self.logger.error(f"(User id getting) Error: {e}")
            raise

    async def get_users(self, db: Session, roles: list[ApplicationStatuses]) -> list[User]:
        try:
            users = db \
                .query(User) \
                .filter(User.role_id.in_([role.value for role in roles])) \
                .all()

            return users
        except Exception as e:
            self.logger.error(f"(User id getting) Error: {e}")
            raise

    async def get_user_by_secret_key(self, db: Session, _key: str) -> User:
        try:
            user = db.query(User).filter(User.secret_key == _key).first()

            if user:
                self.logger.info(f"(User key getting) Got user with ID: {user.id}")
            else:
                self.logger.warning(f"(User key getting) No same user found: {_key}")

            return user
        except Exception as e:
            self.logger.error(f"(User key getting) Error: {e}")
            raise

    async def verify_user(self, db: Session, _key: str) -> User:
        try:
            user = db.query(User).filter(User.secret_key == _key).first()
            user.is_verified = True

            db.commit()

            self.logger.info(f"(User verify) Success: {_key}")
            return user
        except Exception as e:
            self.logger.error(f"(User verify) Error: {e}")
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

            if user.password == password and user.is_verified:
                self.logger.info(f"(Password verify) Success: {email}")

                return True
            else:
                self.logger.warning(f"(Password verify) Failure: {email}")

                return False
        except Exception as e:
            self.logger.error(f"(Password verify) Error: {e}")
            raise

    async def check_user_existence(self, db: Session, user_id: str) -> bool:
        try:
            user = db.query(User).filter((User.id == user_id) & User.is_verified).first()

            if user:
                self.logger.info(f"(Checking user existence) Got user with ID: {user.id}")
                return True
            else:
                self.logger.warning(f"(Checking user existence) No same user found: {user_id}")
                return False

        except Exception as e:
            self.logger.error(f"(Checking user existence) Error: {e}")
            raise
