import logging

from storage.db_config import SessionLocal, Base, engine

from models.tables.user import User
from models.tables.classroom import Classroom
from models.tables.confirm_status import ConfirmStatus
from models.tables.role import Role
from models.tables.schedule_element import ScheduleElement
from models.tables.timeslot import Timeslot
from models.tables.application import Application
from models.tables.crl import CRL

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
