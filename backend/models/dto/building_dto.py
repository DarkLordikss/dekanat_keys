from pydantic import BaseModel


class BuildingDTO(BaseModel):
    buildings: list[int]
