import logging

from fastapi import APIRouter, Depends, HTTPException

from sqlalchemy.orm import Session

import config
from models.dto.building_classrooms_dto import BuildingClassroomsDTO
from models.dto.building_dto import BuildingDTO
from models.dto.error_dto import ErrorDTO
from services.building_service import BuildingService
from storage.db_config import get_db

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

building_router = APIRouter(prefix="/building")


@building_router.get(
    "/get-all-buildings",
    tags=[config.SWAGGER_GROUPS["building"]],
    response_model=BuildingDTO,
    responses={
        200: {
            "model": BuildingDTO

        },
        500: {
            "model": ErrorDTO
        }
    }
)
async def get_buildings(db: Session = Depends(get_db),
                        building_service: BuildingService = Depends(BuildingService)
                        ):
    try:
        buildings = await building_service.get_all_buildings(db)

        logger.info(f"(Get all buildings) Successful: {buildings}")

        return BuildingDTO(buildings=buildings)
    except Exception as e:
        logger.error(f"(Get all buildings) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")


@building_router.get(
    "/get-classrooms-from-building",
    tags=[config.SWAGGER_GROUPS["building"]],
    response_model=BuildingClassroomsDTO,
    responses={
        200: {
            "model": BuildingClassroomsDTO

        },
        404: {
            "model": ErrorDTO
        },
        500: {
            "model": ErrorDTO
        }
    }
)
async def get_building_classrooms(building: int,
                                  db: Session = Depends(get_db),
                                  building_service: BuildingService = Depends(BuildingService)
                                  ):
    try:
        if building not in await building_service.get_all_buildings(db):
            logger.warning(f"(Get building classrooms) Building not found: {building}")
            raise HTTPException(status_code=404, detail="Building not found")

        classrooms = await building_service.get_all_classrooms_from_building(db, building)

        logger.info(f"(Get building classrooms) Successful: {classrooms}")

        return BuildingClassroomsDTO(classrooms=classrooms)
    except HTTPException:
        raise
    except Exception as e:
        logger.error(f"(Get building classrooms) Error: {e}")
        raise HTTPException(status_code=500, detail="Internal server error")
