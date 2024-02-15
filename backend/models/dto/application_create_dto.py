from pydantic import BaseModel, constr, field_validator
from uuid import UUID
from datetime import date, timedelta

import config


class ApplicationCreateDTO(BaseModel):
    classroom_id: UUID
    name: constr(min_length=config.MIN_LENGTH_RECORDINGS)
    description: str
    class_date: date
    time_table_id: int
    dublicates: int = 1

    @classmethod
    @field_validator('class_date')
    def validate_class_date(cls, v):
        if v < (date.today() + timedelta(days=1, hours=config.ONE_WEEK)):
            raise ValueError('Дата вашего занятия должна быть не позже одного дня отправки заявления')
        return v

    @classmethod
    @field_validator('duplicates')
    def validate_duplicates(cls, v):
        if 1 > v > 18:
            raise ValueError('число дубликатов должно быть не менее одного')
        return v
