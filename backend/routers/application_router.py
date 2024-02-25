import logging
from uuid import UUID

import jwt

from fastapi import APIRouter, HTTPException, Depends, Query, WebSocket
from sqlalchemy.orm import Session
from datetime import timedelta, date

from models.dto.application_create_dto import ApplicationCreateDTO
from models.dto.available_classrooms_dto import AvailableClassroomsShowingDTO
from models.dto.application_showing_with_status_dto import ApplicationShowingWithStatusDTO
from models.dto.error_dto import ErrorDTO
from models.dto.formatted_available_classrooms_dto import FormattedTimetable, Day
from models.dto.formatted_application_with_status_dto import FormattedTimetableWithStatus, DayWithStatus
from models.dto.message_dto import MessageDTO
from models.enum.userroles import UserRoles
from models.tables.classroom import Classroom
from models.tables.user import User
from storage.db_config import get_db

from services.classroom_service import ClassroomService
from services.user_service import UserService
from services.auth_service import AuthService
from services.application_service import ApplicationService
from services.entity_verifier_service import EntityVerifierService

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
        entity_verifier_service: EntityVerifierService = Depends(EntityVerifierService)
):
    try:
        if await auth_service.check_revoked(db, access_token):
            logger.warning(f"(Create application) Token is revoked: {access_token}")
            raise HTTPException(status_code=403, detail="Token revoked")

        token_data = auth_service.get_data_from_access_token(access_token)
        user = await user_service.get_user_by_id(db, (await token_data)["sub"])

        """""
        if await classroom_service.check_correct_classroom(db, str(application_create_dto.classroom_id)):
            raise HTTPException(status_code=404, detail="Classroom not found")
        """""
        if await entity_verifier_service.check_existence(
                db,
                Classroom,
                Classroom.id == application_create_dto.classroom_id,
                f"(Check Classroom existence) Classroom with id {application_create_dto.classroom_id} exists",
                f"(Check Classroom existence) Classroom with id {application_create_dto.classroom_id} not found",
                classroom_id=application_create_dto.classroom_id
        ):
            raise HTTPException(status_code=404, detail="building not found")

        if await application_service.time_table_id_validate(db, application_create_dto.time_table_id):
            raise HTTPException(status_code=404, detail="Time not found")

        role = user.role_id
        if role == UserRoles.Student.value:
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


@application_router.get(
    "/show_available_classrooms/",
    response_model=FormattedTimetable,
    responses={
        200: {
            "model": FormattedTimetable
        },
        404: {
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
async def show_available_classrooms(
        building: int,
        start_date: date,
        end_date: date = None,
        classrooms: list[int] = Query(),
        db: Session = Depends(get_db),
        application_service: ApplicationService = Depends(ApplicationService),
        classroom_service: ClassroomService = Depends(ClassroomService),
        entity_verifier_service: EntityVerifierService = Depends(EntityVerifierService),
):
    try:
        if not end_date:
            end_date = start_date

        timetables = []
        schedule = []
        current_date = start_date

        if await entity_verifier_service.check_correct_dates(start_date, end_date):
            raise HTTPException(status_code=400, detail="invalid date")

        if await entity_verifier_service.check_existence(
                db,
                Classroom,
                Classroom.building == building,
                f"(Check building existence) building with number {building} exists",
                f"(Check building existence) building with number {building} not found",
                building_number=building
        ):
            raise HTTPException(status_code=404, detail="building not found")

        if await classroom_service.check_correct_classrooms(db, classrooms, building):
            raise HTTPException(status_code=404, detail="Classrooms not found")

        while current_date <= end_date:
            available_classrooms_showing_dto = AvailableClassroomsShowingDTO(
                building=building,
                classrooms=classrooms,
                date=current_date
            )

            timetables.append(application_service.show_available_classrooms(db, available_classrooms_showing_dto))

            daily_applications = Day(
                date=current_date,
                timetable={}
            )

            schedule.append(daily_applications)
            current_date += timedelta(days=1)

        for index in range(len(schedule)):
            schedule[index].timetable = await timetables[index]

        return FormattedTimetable(schedule=schedule)

    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"(Application) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")


@application_router.get(
    "/show_with_status/",
    response_model=FormattedTimetableWithStatus,
    responses={
        200: {
            "model": FormattedTimetableWithStatus
        },
        404: {
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
async def show_applications_with_status(
        building: int,
        start_date: date,
        end_date: date = None,
        classrooms: list[int] = Query(),
        statuses: list[int] = Query(),
        user_id: UUID = None,
        db: Session = Depends(get_db),
        application_service: ApplicationService = Depends(ApplicationService),
        classroom_service: ClassroomService = Depends(ClassroomService),
        entity_verifier_service: EntityVerifierService = Depends(EntityVerifierService),
):
    try:
        if not end_date:
            end_date = start_date

        timetables = []
        schedule = []
        current_date = start_date

        if await application_service.check_correct_statuses(db, statuses):
            raise HTTPException(status_code=404, detail="statuses not found")

        if await entity_verifier_service.check_correct_dates(start_date, end_date):
            raise HTTPException(status_code=400, detail="invalid date")

        if await entity_verifier_service.check_existence(
                db,
                Classroom,
                Classroom.building == building,
                f"(Check building existence) building with number {building} exists",
                f"(Check building existence) building with number {building} not found",
                building_number=building
        ):
            raise HTTPException(status_code=404, detail="building not found")

        if await classroom_service.check_correct_classrooms(db, classrooms, building):
            raise HTTPException(status_code=404, detail="Classrooms not found")

        if await entity_verifier_service.check_existence(
                db,
                User,
                User.id == user_id,
                f"(Check user existence) user with id {user_id} exists",
                f"(Check user existence) user with id {user_id} not found",
                user_id_func=user_id
        ) and user_id is not None:
            raise HTTPException(status_code=404, detail="user not found")

        while current_date <= end_date:
            application_showing_with_status_dto = ApplicationShowingWithStatusDTO(
                building=building,
                classrooms=classrooms,
                statuses=statuses,
                user_id=user_id,
                date=current_date
            )

            timetables.append(
                application_service.show_applications_with_status(db, application_showing_with_status_dto
                                                                  ))

            daily_applications = DayWithStatus(
                date=current_date,
                timetable={}
            )

            schedule.append(daily_applications)
            current_date += timedelta(days=1)

        for index in range(len(schedule)):
            schedule[index].timetable = await timetables[index]

        return FormattedTimetableWithStatus(schedule=schedule)

    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"(Application) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")


@application_router.post(
    "/change_deal_status/{application_id}",
    response_model=MessageDTO,
    responses={
        200: {
            "model": MessageDTO
        },
        400: {
            "model": ErrorDTO
        },
        401: {
            "model": ErrorDTO
        },
        403: {
            "model": ErrorDTO
        },
        404: {
            "model": ErrorDTO
        },
        500: {
            "model": ErrorDTO
        }
    }
)
async def change_application_status(
        application_id: str,
        status_id: int,
        access_token: str = Depends(config.oauth2_scheme),
        db: Session = Depends(get_db),
        user_service: UserService = Depends(UserService),
        application_service: ApplicationService = Depends(ApplicationService),
        auth_service: AuthService = Depends(AuthService),
):
    try:
        if await auth_service.check_revoked(db, access_token):
            logger.warning(f"(Change deal status) Token is revoked: {access_token}")
            raise HTTPException(status_code=403, detail="Token revoked")

        token_data = auth_service.get_data_from_access_token(access_token)
        user = await user_service.get_user_by_id(db, (await token_data)["sub"])

        await application_service.change_application_status(db, application_id, user, status_id)

        return MessageDTO(message="Status changed")
    except jwt.PyJWTError as e:
        logger.warning(f"(Change deal status) Bad token: {e}")
        raise HTTPException(status_code=403, detail="Bad token")
    except HTTPException:
        raise
    except FileNotFoundError:
        logger.warning(f"(Change deal status) Application {application_id} not found")
        raise HTTPException(status_code=404, detail="Application not found")
    except PermissionError:
        logger.warning(f"(Change deal status) User {user.id} has no permission to change status of application")
        raise HTTPException(status_code=403, detail="No permission")
    except KeyError:
        logger.warning(f"(Change deal status) Wrong new status for application {application_id}")
        raise HTTPException(status_code=400, detail="Wrong status")
    except Exception as e:
        logger.error(f"(Change deal status) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")


@application_router.post(
    "/transfer_key/",
    response_model=FormattedTimetableWithStatus,
    responses={
        200: {
            "model": FormattedTimetableWithStatus
        },
        404: {
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
async def transfer_key(
        application_id: UUID,
        user_recipient_id: UUID,
        access_token: str = Depends(config.oauth2_scheme),
        db: Session = Depends(get_db),
        user_service: UserService = Depends(UserService),
        auth_service: AuthService = Depends(AuthService),
        application_service: ApplicationService = Depends(ApplicationService),
        classroom_service: ClassroomService = Depends(ClassroomService),
        entity_verifier_service: EntityVerifierService = Depends(EntityVerifierService),
):
    try:
        if await auth_service.check_revoked(db, access_token):
            logger.warning(f"(Change deal status) Token is revoked: {access_token}")
            raise HTTPException(status_code=403, detail="Token revoked")

        token_data = auth_service.get_data_from_access_token(access_token)
        user_owner = await user_service.get_user_by_id(db, (await token_data)["sub"])


    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"(Application) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")


@application_router.websocket(
    "/confirm_transfering_key/",
)
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    while True:
        data = await websocket.receive_text()
        await websocket.send_text(f"Message text was: {data}")


clients = []


@application_router.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    clients.append(websocket)

    try:
        while True:
            # Получаем сообщение от клиента
            data = await websocket.receive_text()
            print(f"Received message: {data}")

            # Рассылаем сообщение всем подключенным клиентам
            for client in clients:
                await client.send_text(data)
    except Exception as e:
        print(f"WebSocket connection error: {e}")
        clients.remove(websocket)


@application_router.post("/send_notification/")
async def send_notification(message: str):
    # Рассылка уведомления всем клиентам
    for client in clients:
        await client.send_text(message)
    return {"message": "Notification sent successfully"}

@application_router.post("/send_notification/")
async def send_notification(message: str):
    # Рассылка уведомления всем клиентам
    for client in clients:
        await client.send_text(message)
    return {"message": "Notification sent successfully"}
