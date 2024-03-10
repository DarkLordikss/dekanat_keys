import logging

from fastapi import HTTPException, Depends, APIRouter, Query

import jwt
from uuid import UUID

import config
from models.dto.message_dto import MessageDTO
from models.enum.userroles import UserRoles
from services.email_service import EmailService
from storage.db_config import get_db
from sqlalchemy.orm import Session

from models.dto.user_login_dto import UserLoginDTO
from models.dto.user_access_token_dto import UserAccessTokenDTO

from services.user_service import UserService
from services.auth_service import AuthService
from models.dto.user_profile_dto import UserProfileDTO
from models.dto.error_dto import ErrorDTO
from models.dto.user_reg_dto import UserRegDTO

from config import oauth2_scheme

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

user_router = APIRouter(prefix="/user")


@user_router.post(
    "/login/",
    tags=[config.SWAGGER_GROUPS["user"]],
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


@user_router.post(
    "/register/",
    tags=[config.SWAGGER_GROUPS["user"]],
    response_model=MessageDTO,
    responses={
        200: {
            "model": MessageDTO

        },
        400: {
            "model": ErrorDTO
        },
        500: {
            "model": ErrorDTO
        }
    }
)
async def register(user_reg_dto: UserRegDTO,
                   db: Session = Depends(get_db),
                   user_service: UserService = Depends(UserService),
                   email_service: EmailService = Depends(EmailService)
                   ):
    try:
        user = await user_service.get_user_by_email(db, user_reg_dto.email)

        if user:
            logger.warning(f"(Reg) User already registered: {user_reg_dto.email}")
            raise HTTPException(status_code=400, detail="User already exists")

        user = await user_service.create_user(db, user_reg_dto.email, user_reg_dto.password, user_reg_dto.full_name)

        email_service.send_link(user.secret_key, user.email)

        logger.info(f"(Reg) User successful registered: {user.id}")
        return MessageDTO(message=str(user.id))
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"(Reg) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")


@user_router.get(
    "/verify/{key}",
    tags=[config.SWAGGER_GROUPS["user"]],
    response_model=MessageDTO,
    responses={
        200: {
            "model": MessageDTO

        },
        404: {
            "model": ErrorDTO
        },
        500: {
            "model": ErrorDTO
        }
    }
)
async def verify(key: str,
                 db: Session = Depends(get_db),
                 user_service: UserService = Depends(UserService),
                 ):
    try:
        user = await user_service.get_user_by_secret_key(db, key)

        if not user or user.is_verified or await user_service.get_user_by_email(db, user.email):
            logger.warning(f"(Verify) User not exists or verified: {key}")
            raise HTTPException(status_code=404, detail="Not found")

        await user_service.verify_user(db, key)

        logger.info(f"(Verify) User verified registered: {user.id}")

        return MessageDTO(message='Account verified!')
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"(Verify) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")


@user_router.get(
    "/",
    tags=[config.SWAGGER_GROUPS["user"]],
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
            id=user.id,
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
    tags=[config.SWAGGER_GROUPS["user"]],
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


@user_router.get(
    "/users",
    tags=[config.SWAGGER_GROUPS["user"]],
    response_model=list[UserProfileDTO],
    responses={
        200: {
            "model": list[UserProfileDTO]

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
async def get_users(
                    roles: list[int] = Query(),
                    access_token: str = Depends(oauth2_scheme),
                    db: Session = Depends(get_db),
                    user_service: UserService = Depends(UserService),
                    auth_service: AuthService = Depends(AuthService)
                    ):
    try:
        if await auth_service.check_revoked(db, access_token):
            logger.warning(f"(Get user profile) Token is revoked: {access_token}")
            raise HTTPException(status_code=403, detail="Token revoked")

        users = await user_service.get_users(db, roles)

        logger.info(f"(Get users profiles) Successful get profiles with roles: {roles}")

        users_dto = []

        for user in users:
            users_dto.append(
                UserProfileDTO(
                    id=user.id,
                    email=user.email,
                    full_name=user.full_name,
                    role=(await user_service.get_role_by_id(db, user.role_id)).name
                )
            )

        return users_dto
    except jwt.PyJWTError as e:
        logger.warning(f"(Get users profiles) Bad token: {e}")
        raise HTTPException(status_code=403, detail="Bad token")
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"(Get user profile) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")


@user_router.post(
    "/change_role/",
    tags=[config.SWAGGER_GROUPS["user"]],
    response_model=MessageDTO,
    responses={
        200: {
            "model": MessageDTO

        },
        400: {
            "model": ErrorDTO
        },
        500: {
            "model": ErrorDTO
        }
    }
)
async def change_role(
                    another_user_id: str,
                    wished_role_id : int,
                    access_token: str = Depends(oauth2_scheme),
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

        if user.role_id != UserRoles.Dean_office_employee.value:
            logger.warning(f"(Change role) You can't do this with role_id: {user.role_id}")
            raise HTTPException(status_code=403, detail="You're not dean's office")

        if wished_role_id not in range(1, 3):
            logger.warning(f"(Change role) Bad role: {user.role_id}")
            raise HTTPException(status_code=400, detail="Bad role")

        if wished_role_id == UserRoles.Dean_office_employee.value:
            logger.warning(f"(Change role) You can't make dean's office: {user.role_id}")
            raise HTTPException(status_code=403, detail="3 is dean's role")

        message = await user_service.change_user_role(db, wished_role_id, another_user_id)

        return MessageDTO(message=message)
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"(Reg) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")
