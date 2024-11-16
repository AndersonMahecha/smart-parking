import asyncio
import random
import string

import websockets
import json
from typing import List

def random_id():
    return "".join(random.choices(string.ascii_uppercase + string.digits, k=10))

class Client(object):
    def __init__(self, client_id, websocket):
        self.client_id = client_id
        self.websocket = websocket
        self.events = asyncio.Queue()

clients: List[Client] = []

async def notify_client(client):
    while True:
        event = await client.events.get()
        try:
            await client.websocket.send(event)
            client.events.task_done()
        except websockets.exceptions.ConnectionClosedError:
            print(f"client disconnected")
            clients.remove(client)
            return


def notify_clients():
    pass


async def handler(websocket):
    client_id = random_id()
    print(f"Client connected: {client_id}")
    client = Client(client_id, websocket)
    clients.append(client)

    asyncio.create_task(notify_client(client))

    while True:
        try:
            message = await websocket.recv()
            if message == "broadcast":
                for c in clients:
                    if c is not client:
                        c.events.put_nowait(message)
                client.events.put_nowait("notified")
                continue
            print(f"Client {client_id} received message: {message}")

        # client disconnected?
        except websockets.ConnectionClosedOK:
            print(f"Client {client_id} disconnected")
            clients.remove(client)
            return


async def websocket_handler():
    print("running websocket handler")
    async with websockets.serve(handler, port=3500):
        await asyncio.Future()


if __name__ == "__main__":
    asyncio.run(websocket_handler())

