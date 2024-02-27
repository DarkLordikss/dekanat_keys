from pydantic import BaseModel

from models.dto.classroom_dto import ClassroomDTO


class BuildingClassroomsDTO(BaseModel):
    classrooms: list[ClassroomDTO]
