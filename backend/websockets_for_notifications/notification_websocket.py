import json

from fastapi import FastAPI, APIRouter, WebSocket, HTTPException, Depends, Query, WebSocketDisconnect
import logging
from typing import List
from sqlalchemy.orm import Session

from models.tables.connected_user import ConnectedUser
from models.tables.transfering_application import TransferingApplication
from services.websocket_service import WebsocketService
from storage.db_config import get_db

import config
client_sockets = {}

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

notifications_websocket = FastAPI(prefix="/notifications")

"""""
clients = []

@notifications_websocket.post("/send_notification/")
async def send_notification(message: str):
    # Рассылка уведомления всем клиентам
    for client in clients:
        await client.send_text(message)
    return {"message": "Notification sent successfully"}
    """""
"""
@notifications_websocket.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    client_sockets.append(websocket)

    try:
        while True:
            # Получаем сообщение от клиента
            data = await websocket.receive_text()
            print(f"Received message: {data}")

            # Рассылаем сообщение всем подключенным клиентам
            for client in client_sockets:
                await client.send_text(data)
    except Exception as e:
        print(f"WebSocket connection error: {e}")
        client_sockets.remove(websocket)
"""
@notifications_websocket.websocket("/ws/{user_id}")
async def websocket_endpoint(
        websocket: WebSocket,
        user_id: str,
        db: Session = Depends(get_db),
        websocket_service: WebsocketService = Depends(WebsocketService)
):
    await websocket.accept()
    logger.info(f"PISYA POPA KAKA")
    client_sockets[user_id] = websocket
    try:


        while True:
            print("GGGGGGGGGGGGGGGGGAAAAAAAAAAAAAAAAAAAAGGGGGGGG")
            data = await websocket.receive_text()
            #data = json.loads(data_json)
            print("GGGGGGGGGGGGGGGGGGGGGGGGG", data)

            # user_recipient_id = data["recipient_id"]
            # application_id = data["application_id"]
            # answer = data["answer"]
            application_id, user_sender_id, answer_str = data.split(":")
            answer = bool(answer_str)
            print("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAa   ", application_id, user_sender_id, answer)
            logger.info(f"SEND_ID   {user_sender_id}")
            logger.info(f"USER_ID   {user_id}")

            if data : await websocket_service.change_application_owner(db, user_sender_id, user_id, application_id, answer)
    except WebSocketDisconnect:
        client_sockets.remove(websocket)


