import logging

import jwt

from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.orm import Session
from datetime import timedelta

from models.dto.application_create_dto import ApplicationCreateDTO
from models.dto.application_showing_dto import ApplicationShowingDTO
from models.dto.error_dto import ErrorDTO
from models.dto.formatted_application_dto import FormattedTimetable
from models.dto.message_dto import MessageDTO
from models.enum.user_roles import User_roles
from storage.db_config import get_db

from services.classroom_service import ClassroomService
from services.user_service import UserService
from services.auth_service import AuthService
from services.application_service import ApplicationService

import config

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

application_router = APIRouter(prefix="/applications")


@application_router.post(
    "/create/",
    response_model=MessageDTO,
    responses={
        200: {
            "model": MessageDTO
        },
        404: {
            "model": ErrorDTO
        },
        403: {
            "model": ErrorDTO
        },
        400: {
            "model": ErrorDTO
        },
        500: {
            "model": ErrorDTO
        }
    }
)
async def create_application(
        application_create_dto: ApplicationCreateDTO,
        access_token: str = Depends(config.oauth2_scheme),
        db: Session = Depends(get_db),
        user_service: UserService = Depends(UserService),
        application_service: ApplicationService = Depends(ApplicationService),
        auth_service: AuthService = Depends(AuthService),
        classroom_service: ClassroomService = Depends(ClassroomService)
):
    try:
        if await auth_service.check_revoked(db, access_token):
            logger.warning(f"(Create application) Token is revoked: {access_token}")
            raise HTTPException(status_code=403, detail="Token revoked")

        token_data = auth_service.get_data_from_access_token(access_token)
        user = await user_service.get_user_by_id(db, (await token_data)["sub"])

        if await classroom_service.check_correct_classroom(db, str(application_create_dto.classroom_id)):
            raise HTTPException(status_code=404, detail="Classroom not found")

        if await application_service.time_table_id_validate(db, application_create_dto.time_table_id):
            raise HTTPException(status_code=404, detail="Time not found")

        role = user.role_id
        if role == User_roles.Student.value:
            if application_create_dto.dublicates > 1:
                raise HTTPException(status_code=400, detail="you can't do a lot of duplicates")

            result = await application_service.check_priority(
                db,
                application_create_dto.classroom_id,
                application_create_dto.class_date,
                application_create_dto.time_table_id,
                user.id)

            if result == 0:
                raise HTTPException(status_code=403, detail="you has already occupied this classroom")
            elif result == 1:
                raise HTTPException(status_code=403, detail="teacher has already occupied this classroom")
        else:
            if (await application_service.delete_all_students(
                    db,
                    application_create_dto.classroom_id,
                    application_create_dto.class_date,
                    application_create_dto.time_table_id,
                    user.id
            )):
                raise HTTPException(status_code=403, detail="You has already occupied this classroom")

        for i in range(application_create_dto.dublicates):
            copy_application_dto = application_create_dto.copy()
            copy_application_dto.class_date = (application_create_dto.class_date +
                                               timedelta(days=config.ONE_WEEK) * i)

            await application_service.create_application(user.id, copy_application_dto, db)

        return MessageDTO(message="Application added!")

    except jwt.PyJWTError as e:
        logger.warning(f"(Create application) Bad token: {e}")
        raise HTTPException(status_code=403, detail="Bad token")
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"(Application) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")



@application_router.post(
    "/show/",
    response_model=FormattedTimetable,
    responses={
        200: {
            "model": FormattedTimetable
        },
        404: {
            "model": ErrorDTO
        },
        403: {
            "model": ErrorDTO
        },
        400: {
            "model": ErrorDTO
        },
        500: {
            "model": ErrorDTO
        }
    }
)
async def show_applications(
        application_showing_dto: ApplicationShowingDTO,
        access_token: str = Depends(config.oauth2_scheme),
        db: Session = Depends(get_db),
        user_service: UserService = Depends(UserService),
        application_service: ApplicationService = Depends(ApplicationService),
        auth_service: AuthService = Depends(AuthService),
        classroom_service: ClassroomService = Depends(ClassroomService)
):
    try:
        if await auth_service.check_revoked(db, access_token):
            logger.warning(f"(Create application) Token is revoked: {access_token}")
            raise HTTPException(status_code=403, detail="Token revoked")

        token_data = auth_service.get_data_from_access_token(access_token)
        user = await user_service.get_user_by_id(db, (await token_data)["sub"])
        logger.warning(f"SUPER    {application_showing_dto.date}   GUT")
        x = await application_service.show_applications(db, application_showing_dto)
        logger.warning(f"SUPER GUT")
        return FormattedTimetable(timetable=x)

    except jwt.PyJWTError as e:
        logger.warning(f"(Create application) Bad token: {e}")
        raise HTTPException(status_code=403, detail="Bad token")
    except Exception as e:
        logger.error(f"(Application) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")