from typing import Dict, Optional, List
from uuid import UUID
from pydantic import BaseModel

from datetime import date


class ClassroomForPair(BaseModel):
    classroom_id: UUID
    buildings: int
    class_number: int


class ClassroomForPairWithTrack(BaseModel):
    classroom_id: UUID
    buildings: int
    class_number: int
    user_id: Optional[UUID] = None


class Day(BaseModel):
    timetable: Dict[int, Optional[List[ClassroomForPair]]]
    date: Optional[date]


class FormattedTimetable(BaseModel):
    schedule: list[Day]
