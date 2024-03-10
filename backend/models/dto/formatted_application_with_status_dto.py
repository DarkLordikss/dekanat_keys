from typing import Dict, Optional, List
from uuid import UUID
from pydantic import BaseModel
from datetime import date

from models.enum.applicationstatuses import ApplicationStatuses


class PairWithStatus(BaseModel):
    application_id: UUID
    classroom_id: UUID
    status: ApplicationStatuses
    name: str
    description: str
    buildings: int
    class_number: int


class DayWithStatus(BaseModel):
    timetable: Dict[int, Optional[List[PairWithStatus]]]
    date: Optional[date]


class FormattedTimetableWithStatus(BaseModel):
    schedule: list[DayWithStatus]
