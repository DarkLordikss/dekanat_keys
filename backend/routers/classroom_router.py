import logging

from fastapi import HTTPException, Depends, APIRouter
from requests import Session

import config
from models.dto.classroom_dto import ClassroomDTO
from models.dto.classrooms_dto import ClassroomsDTO
from models.dto.error_dto import ErrorDTO

from services.classroom_service import ClassroomService
from storage.db_config import get_db

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

classroom_router = APIRouter(prefix="/classroom")


@classroom_router.get(
    "/all",
    tags=[config.SWAGGER_GROUPS["classroom"]],
    response_model=ClassroomsDTO,
    responses={
        200: {
            "model": ClassroomsDTO
        },
        500: {
            "model": ErrorDTO
        }
    }
)
async def get_classrooms(db: Session = Depends(get_db),
                         classroom_service: ClassroomService = Depends(ClassroomService)
                         ):
    try:
        classrooms = await classroom_service.get_all_classrooms(db)
        classrooms_dto = [
            ClassroomDTO(
                id=str(classroom.id),
                building=str(classroom.building),
                number=str(classroom.number),
                address=classroom.address
            ) for classroom in classrooms
        ]
        logger.info(f"(Get all classrooms) Successful get classrooms, len: {len(classrooms)}")

        return ClassroomsDTO(classrooms=classrooms_dto)
    except Exception as e:
        logger.error(f"(Get all classrooms)  Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")
