import logging

from fastapi import APIRouter, Depends, HTTPException

from sqlalchemy.orm import Session

from models.dto.building_dto import BuildingDTO
from models.dto.error_dto import ErrorDTO
from services.building_service import BuildingService
from storage.db_config import get_db

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

building_router = APIRouter(prefix="/building")


@building_router.get(
    "/",
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