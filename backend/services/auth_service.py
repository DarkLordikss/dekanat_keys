import logging
import os
import jwt

from sqlalchemy.orm import Session

from datetime import datetime, timedelta

from models.tables.crl import CRL


class AuthService:
    def __init__(self):
        logging.basicConfig(level=logging.INFO)
        self.logger = logging.getLogger(__name__)

        self.TOKEN_LIFETIME = int(os.environ["ACCESS_TOKEN_EXPIRE_MINUTES"])
        self.SECRET_KEY = str(os.environ["SECRET_KEY"])
        self.ALGORITHM = str(os.environ["ACCESS_TOKEN_ALGORITHM"])

    async def create_access_token(self, data: dict) -> str:
        try:
            to_encode = data.copy()
            expires_delta = timedelta(minutes=self.TOKEN_LIFETIME)
            expire = datetime.utcnow() + expires_delta
            to_encode.update({"exp": expire})
            encoded_jwt = jwt.encode(to_encode, self.SECRET_KEY, algorithm=self.ALGORITHM)

            self.logger.info(f"(Create access token) Successful created access token with payload: {data}")

            return encoded_jwt
        except Exception as e:
            self.logger.error(f"(Create access token) Error creating access token: {e}")
            raise

    async def get_data_from_access_token(self, token: str) -> dict:
        try:
            payload = jwt.decode(token, self.SECRET_KEY, algorithms=[self.ALGORITHM])

            self.logger.info(f"(Get data from token) Successful get data: {payload}")
            return payload
        except jwt.PyJWTError as e:
            self.logger.warning(f"(Get data from token) Bad auth token: {e}")
            raise
        except Exception as e:
            self.logger.error(f"(Get data from token) Error auth token: {e}")
            raise

    async def revoke_access_token(self, db: Session, token: str) -> None:
        try:
            crl_entry = CRL(token=token)
            db.add(crl_entry)
            db.commit()
            self.logger.info(f"(Revoke access token) Token was revoked now: {token}")
        except Exception as e:
            self.logger.error(f"(Revoke access token) Error revoking token: {e}")
            raise

    async def check_revoked(self, db: Session, token: str) -> bool:
        try:
            if db.query(CRL).filter(CRL.token == token).first():
                self.logger.warning(f"(Check revoked access token) Token revoked: {token}")
                return True
            else:
                self.logger.info(f"(Check revoked access token) Token not revoked: {token}")
                return False
        except Exception as e:
            self.logger.error(f"(Check revoked access token) Error revoking token: {e}")
            raise
