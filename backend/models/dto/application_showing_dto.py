from pydantic import BaseModel, field_validator
from uuid import UUID
from datetime import date

from typing import List, Optional


class ApplicationShowingDTO(BaseModel):
    building: int
    classrooms: List[int]
    scheduled: bool
    user_id: Optional[UUID] = None
    date: date

    @field_validator('date')
    def validate_duplicates(cls, v):
        if v is None:
            v = date.today()
        return v
