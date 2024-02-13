import logging
from typing import Annotated

from fastapi import HTTPException, Depends, APIRouter
from fastapi.security import OAuth2PasswordRequestForm
from requests import Session

from backend.models.dto.error_dto import ErrorDTO
from backend.models.dto.user_access_token_dto import UserAccessTokenDTO

from backend.services.auth_service import AuthService
from backend.services.user_service import UserService
from backend.storage.db_config import get_db

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

test_router = APIRouter(prefix="/test")


@test_router.post(
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
async def test_login(form_data: Annotated[OAuth2PasswordRequestForm, Depends()],
                     db: Session = Depends(get_db),
                     user_service: UserService = Depends(UserService),
                     auth_service: AuthService = Depends(AuthService)
                     ):
    try:
        if not await user_service.verify_password(db, form_data.username, form_data.password):
            logger.warning(f"(Test login) Failed login for user with email: {form_data.username}")
            raise HTTPException(status_code=400, detail="Invalid credentials")

        user = await user_service.get_user_by_email(db, form_data.username)

        access_token = await auth_service.create_access_token(
            data={"sub": str(user.id)}
        )

        logger.info(f"(Test login) Login successful for user with ID: {user.id}")
        return UserAccessTokenDTO(access_token=access_token)
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"(Test login) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")
