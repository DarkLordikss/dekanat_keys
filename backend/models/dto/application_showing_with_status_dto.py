from pydantic import BaseModel, field_validator
from uuid import UUID
from datetime import date

from typing import List, Optional

from models.enum.applicationstatuses import ApplicationStatuses


class ApplicationShowingWithStatusDTO(BaseModel):
    building: int
    classrooms: List[int]
    statuses: list[ApplicationStatuses]
    user_id: Optional[UUID] = None
    date: date

    @field_validator('date')
    def validate_duplicates(cls, v):
        if v is None:
            v = date.today()
        return v
