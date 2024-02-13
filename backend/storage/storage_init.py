import logging

from backend.storage.db_config import SessionLocal, Base, engine

from backend.models.tables.user import User
from backend.models.tables.classroom import Classroom
from backend.models.tables.confirm_status import ConfirmStatus
from backend.models.tables.role import Role
from backend.models.tables.schedule_element import ScheduleElement
from backend.models.tables.timeslot import Timeslot
from backend.models.tables.application import Application
from backend.models.tables.crl import CRL

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


def init_db():
    db = SessionLocal()

    try:
        Base.metadata.create_all(bind=engine, tables=[User.__table__,
                                                      Application.__table__,
                                                      Classroom.__table__,
                                                      ConfirmStatus.__table__,
                                                      Role.__table__,
                                                      ScheduleElement.__table__,
                                                      Timeslot.__table__,
                                                      CRL.__table__,
                                                      ]
                                 )
        db.commit()
        logger.info("(Init database) Database initialized successfully.")
    except Exception as e:
        db.rollback()
        logger.fatal(f"(Init database) initializing database: {e}")
        raise
    finally:
        db.close()
