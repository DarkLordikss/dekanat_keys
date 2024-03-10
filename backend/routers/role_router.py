import logging

from fastapi import HTTPException, Depends, APIRouter
from requests import Session

import config
from models.dto.classroom_dto import ClassroomDTO
from models.dto.classrooms_dto import ClassroomsDTO
from models.dto.error_dto import ErrorDTO
from models.dto.enums_dto import EnumsDTO

from services.classroom_service import ClassroomService
from services.role_service import RoleService
from services.status_service import StatusService
from storage.db_config import get_db

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

role_router = APIRouter(prefix="/role")


@role_router.get(
    "/all",
    tags=[config.SWAGGER_GROUPS["role"]],
    response_model=EnumsDTO,
    responses={
        200: {
            "model": EnumsDTO
        },
        500: {
            "model": ErrorDTO
        }
    }
)
async def get_roles(db: Session = Depends(get_db),
                         role_service: RoleService = Depends(RoleService)
                         ):
    try:
        statuses = await role_service.get_all_roles(db)
        logger.info(f"(Get all statuses) Successful get statuses, len: {len(statuses)}")

        return EnumsDTO(statuses=statuses)
    except Exception as e:
        logger.error(f"(Get all classrooms)  Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")
