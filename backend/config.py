from fastapi.security import OAuth2PasswordBearer


oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/api/v1/test/login/")

MIN_PASSWORD_LENGTH = 6
