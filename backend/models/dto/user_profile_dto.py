from pydantic import BaseModel, EmailStr
from uuid import UUID


class UserProfileDTO(BaseModel):
    full_name: str
    email: EmailStr
    role: str

class UserProfileWithIdDTO(BaseModel):
    id: UUID
    full_name: str
    email: EmailStr
    role: str
