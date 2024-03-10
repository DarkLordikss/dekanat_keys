from pydantic import BaseModel


class StatusDTO(BaseModel):
    id: int
    name: str

