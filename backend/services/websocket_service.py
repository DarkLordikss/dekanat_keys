import logging
import uuid

from sqlalchemy.orm import Session
from sqlalchemy import and_


from typing import List

from models.tables.application import Application
from models.tables.connected_user import ConnectedUser
from models.tables.transfering_application import TransferingApplication
#from websockets_for_notifications.notification_websocket import client_sockets


class WebsocketService:
    async def change_application_owner(
            self,
            db: Session,
            user_owner_id: str,
            user_recipient_id: str,
            application_id: str,
            answer: bool
    ):
        application = db \
            .query(Application) \
            .filter(and_(Application.id == application_id)) \
            .first()

        if answer:
            application.user_id = user_recipient_id

        db \
            .query(TransferingApplication) \
            .filter(and_(
                TransferingApplication.application_id == application_id,
                TransferingApplication.user_sender_id == user_owner_id,
                TransferingApplication.user_recipient_id == user_recipient_id)
            ) \
            .delete()

        db.commit()

        """""
        websocket_id = user_recipient_id.websocket_id
        websocket = [conn for conn in client_sockets if id(conn) == websocket_id][0]

        message = "Передача была одобрена" if answer else "Вам отклонили передачу"
        await websocket.send_text(message)
        """""