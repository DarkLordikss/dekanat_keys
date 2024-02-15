"""""
class FormattedTimetable(BaseModel):
    classroom_id: UUID
    name: str
    description: str
    buildings: int
    class_number: int
"""""
from typing import Dict, Optional, List
from uuid import UUID
from pydantic import BaseModel

class Pair(BaseModel):
    classroom_id: UUID
    name: str
    description: str
    buildings: int
    class_number: int

class FormattedTimetable(BaseModel):
    timetable: Dict[int, Optional[List[Pair]]]