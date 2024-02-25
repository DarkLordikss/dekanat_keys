from pydantic import BaseModel, EmailStr, constr

import config


class UserRegDTO(BaseModel):
    email: EmailStr
    full_name: str
    password: constr(min_length=config.MIN_PASSWORD_LENGTH)
