from pydantic import BaseModel


class ErrorDTO(BaseModel):
    detail: str
