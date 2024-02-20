from typing import Dict, Optional, List
from uuid import UUID
from pydantic import BaseModel

from datetime import date


class Pair(BaseModel):
    classroom_id: UUID
    name: str
    description: str
    buildings: int
    class_number: int


class Day(BaseModel):
    timetable: Dict[int, Optional[List[Pair]]]
    date: Optional[date]


class FormattedTimetable(BaseModel):
    schedule: list[Day]
