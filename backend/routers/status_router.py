import logging

from fastapi import HTTPException, Depends, APIRouter
from requests import Session

import config
from models.dto.classroom_dto import ClassroomDTO
from models.dto.classrooms_dto import ClassroomsDTO
from models.dto.error_dto import ErrorDTO
from models.dto.statuses_dto import StatusesDTO

from services.classroom_service import ClassroomService
from services.status_service import StatusService
from storage.db_config import get_db

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

status_router = APIRouter(prefix="/status")


@status_router.get(
    "/all",
    tags=[config.SWAGGER_GROUPS["status"]],
    response_model=StatusesDTO,
    responses={
        200: {
            "model": StatusesDTO
        },
        500: {
            "model": ErrorDTO
        }
    }
)
async def get_statuses(db: Session = Depends(get_db),
                         status_service: StatusService = Depends(StatusService)
                         ):
    try:
        statuses = await status_service.get_all_statuses(db)
        logger.info(f"(Get all statuses) Successful get statuses, len: {len(statuses)}")

        return StatusesDTO(statuses=statuses)
    except Exception as e:
        logger.error(f"(Get all classrooms)  Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")
