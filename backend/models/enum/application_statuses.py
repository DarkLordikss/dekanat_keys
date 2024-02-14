from enum import Enum

class Application_statuses(Enum):
    Not_processed = 1
    Confirmed = 2
    Key_received = 3
    Key_submitted = 4
    Rejected = 5
    Invalid = 6