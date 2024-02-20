from pydantic import BaseModel

from models.dto.classroom_dto import ClassroomDTO


class ClassroomsDTO(BaseModel):
    classrooms: list[ClassroomDTO]
