from pydantic import BaseModel, EmailStr, constr

import config


class UserLoginDTO(BaseModel):
    email: EmailStr
    password: constr(min_length=config.MIN_PASSWORD_LENGTH)
