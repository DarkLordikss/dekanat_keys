from typing import Dict, Optional, List
from uuid import UUID
from pydantic import BaseModel

from datetime import date


class Classroom_for_pair(BaseModel):
    classroom_id: UUID
    buildings: int
    class_number: int


class Day(BaseModel):
    timetable: Dict[int, Optional[List[Classroom_for_pair]]]
    date: Optional[date]


class FormattedTimetable(BaseModel):
    schedule: list[Day]
