from pydantic import BaseModel, EmailStr


class UserProfileDTO(BaseModel):
    full_name: str
    email: EmailStr
    role: str
