from pydantic import BaseModel


class UserAccessTokenDTO(BaseModel):
    access_token: str
    token_type: str = "bearer"
