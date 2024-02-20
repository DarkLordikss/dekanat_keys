from pydantic import BaseModel


class ClassroomDTO(BaseModel):
    id: str
    building: str
    number: str
    address: str

