from pydantic import BaseModel

from models.dto.status_dto import StatusDTO


class StatusesDTO(BaseModel):
    statuses: list[StatusDTO]
