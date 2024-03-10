from pydantic import BaseModel

from models.dto.enum_dto import EnumDTO


class EnumsDTO(BaseModel):
    statuses: list[EnumDTO]
