import logging
import websocket
import uvicorn

from fastapi import FastAPI, APIRouter
from fastapi.middleware.cors import CORSMiddleware

from routers.application_router import application_router
from routers.classroom_router import classroom_router
from routers.test_router import test_router
from routers.user_router import user_router
from routers.building_router import building_router
from routers.status_router import status_router

from storage.storage_init import init_db

from services.email_service import EmailService
from websockets_for_notifications.notification_websocket import notifications_websocket

#from websockets_for_notifications.notification_websocket import notifications_websocket

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

router = APIRouter(prefix="/api/v1")

router.include_router(user_router)
router.include_router(test_router)
router.include_router(classroom_router)
router.include_router(application_router)
router.include_router(building_router)
router.include_router(status_router)


app = FastAPI()
app.include_router(router)
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)
app.mount("/notifications", notifications_websocket)

if __name__ == "__main__":
    email_service = EmailService()
    email_service.test_send()

    init_db()
    uvicorn.run(app, host="0.0.0.0", port=8000)
