from pydantic import BaseModel


class EnumDTO(BaseModel):
    id: int
    name: str

