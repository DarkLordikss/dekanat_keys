from fastapi import FastAPI, APIRouter, WebSocket
import logging

import config

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

notifications_websocket = WebSocket(prefix="/notifications")

clients = []


@notifications_websocket.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    clients.append(websocket)

    try:
        while True:
            # Получаем сообщение от клиента
            data = await websocket.receive_text()
            print(f"Received message: {data}")

            # Рассылаем сообщение всем подключенным клиентам
            for client in clients:
                await client.send_text(data)
    except Exception as e:
        print(f"WebSocket connection error: {e}")
        clients.remove(websocket)


