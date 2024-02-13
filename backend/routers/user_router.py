import logging

from fastapi import HTTPException, Depends, APIRouter

import jwt

from backend.models.dto.message_dto import MessageDTO
from backend.storage.db_config import get_db
from sqlalchemy.orm import Session

from backend.models.dto.user_login_dto import UserLoginDTO
from backend.models.dto.user_access_token_dto import UserAccessTokenDTO

from backend.services.user_service import UserService
from backend.services.auth_service import AuthService
from backend.models.dto.user_profile_dto import UserProfileDTO
from backend.models.dto.error_dto import ErrorDTO

from backend.config import oauth2_scheme

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

user_router = APIRouter(prefix="/user")


@user_router.post(
    "/login/",
    response_model=UserAccessTokenDTO,
    responses={
        200: {
            "model": UserAccessTokenDTO

        },
        400: {
            "model": ErrorDTO
        },
        500: {
            "model": ErrorDTO
        }
    }
)
async def login(user_login_dto: UserLoginDTO,
                db: Session = Depends(get_db),
                user_service: UserService = Depends(UserService),
                auth_service: AuthService = Depends(AuthService)
                ):
    try:
        if not await user_service.verify_password(db, user_login_dto.email, user_login_dto.password):
            logger.warning(f"(Login) Failed login for user with email: {user_login_dto.email}")
            raise HTTPException(status_code=400, detail="Invalid credentials")

        user = await user_service.get_user_by_email(db, user_login_dto.email)

        access_token = await auth_service.create_access_token(
            data={"sub": str(user.id)}
        )

        logger.info(f"(Login) Login successful for user with ID: {user.id}")
        return UserAccessTokenDTO(access_token=access_token)
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"(Login) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")


@user_router.get(
    "/",
    response_model=UserProfileDTO,
    responses={
        200: {
            "model": UserProfileDTO

        },
        401: {
            "model": ErrorDTO
        },
        403: {
            "model": ErrorDTO
        },
        500: {
            "model": ErrorDTO
        }
    }
)
async def get_profile(access_token: str = Depends(oauth2_scheme),
                      db: Session = Depends(get_db),
                      user_service: UserService = Depends(UserService),
                      auth_service: AuthService = Depends(AuthService)
                      ):
    try:
        if await auth_service.check_revoked(db, access_token):
            logger.warning(f"(Get user profile) Token is revoked: {access_token}")
            raise HTTPException(status_code=403, detail="Token revoked")

        token_data = auth_service.get_data_from_access_token(access_token)

        user = await user_service.get_user_by_id(db, (await token_data)["sub"])

        logger.info(f"(Get user profile) Successful get profile with id: {user.id}")

        return UserProfileDTO(
            email=user.email,
            full_name=user.full_name,
            role=(await user_service.get_role_by_id(db, user.role_id)).name
        )
    except jwt.PyJWTError as e:
        logger.warning(f"(Get user profile) Bad token: {e}")
        raise HTTPException(status_code=403, detail="Bad token")
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"(Get user profile) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")


@user_router.post(
    "/logout/",
    response_model=MessageDTO,
    responses={
        200: {
            "model": MessageDTO

        },
        401: {
            "model": ErrorDTO
        },
        403: {
            "model": ErrorDTO
        },
        500: {
            "model": ErrorDTO
        }
    }
)
async def logout(access_token: str = Depends(oauth2_scheme),
                 db: Session = Depends(get_db),
                 auth_service: AuthService = Depends(AuthService)
                 ):
    try:
        if await auth_service.check_revoked(db, access_token):
            logger.warning(f"(Logout) Token is revoked: {access_token}")
            raise HTTPException(status_code=403, detail="Token revoked")

        await auth_service.revoke_access_token(db, access_token)

        logger.info(f"(Logout) Token was revoked now: {access_token}")

        return MessageDTO(message="Token was successfully revoked")
    except jwt.PyJWTError as e:
        logger.warning(f"(Logout) Bad token: {e}")
        raise HTTPException(status_code=403, detail="Bad token")
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"(Login) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")
