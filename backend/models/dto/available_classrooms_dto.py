from pydantic import BaseModel, field_validator
from uuid import UUID
from datetime import date

from typing import List, Optional


class AvailableClassroomsShowingDTO(BaseModel):
    building: int
    classrooms: List[int]
    date: date

    @field_validator('date')
    def validate_duplicates(cls, v):
        if v is None:
            v = date.today()
        return v
