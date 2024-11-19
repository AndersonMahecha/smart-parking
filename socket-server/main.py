import asyncio
import random
import string
from typing import List

import websockets


class Parking(object):
    def __init__(self):
        self.cards_id = []
        self.current_vehicles_count = 0
        self.max_vehicles = 6
        self.is_occupied_slots = [False for _ in range(self.max_vehicles)]

    def add_vehicle(self, card) -> bool:
        if self.current_vehicles_count + 1 > self.max_vehicles:
            return False
        if card in self.cards_id:
            return False
        self.cards_id.append(card)
        self.current_vehicles_count += 1
        return True


def random_id():
    return "".join(random.choices(string.ascii_uppercase + string.digits, k=10))


class Client(object):
    def __init__(self, client_id, websocket):
        self.client_type = "device"
        self.client_id = client_id
        self.websocket = websocket
        self.events = asyncio.Queue()


clients: List[Client] = []


async def notify_client(client):
    response = build_barrier_status_response(False)
    client.events.put_nowait(response)
    while True:
        event = await client.events.get()
        try:
            await client.websocket.send(event)
            client.events.task_done()
        except websockets.exceptions.ConnectionClosedError:
            print(f"client disconnected")
            clients.remove(client)
            return


_MESSAGE_TYPE_STATUS = 0
_MESSAGE_TYPE_RESPONSE = 2
_MESSAGE_TYPE_ENTRY = 4
_MESSAGE_TYPE_EXIT = 5

import json


def build_browser_message():

    return json.dumps(
        {
            "max_vehicles": parking.max_vehicles,
            "current_vehicles_count": parking.current_vehicles_count,
            "is_occupied_slots": parking.is_occupied_slots,
            "cards_id": parking.cards_id,
        }
    )


def notify_browsers():
    for client in clients:
        if client.client_type == "browser":
            message = build_browser_message()
            client.events.put_nowait(message)


def process_message(message: bytes) -> str | None:
    message_type = message[0]
    device_type = message[1]

    if message_type == _MESSAGE_TYPE_ENTRY:
        card_id = message[3:13].decode("utf-8")
        status = message[2]
        ok = parking.add_vehicle(card_id)
        response = build_barrier_status_response(ok)
        notify_browsers()
        return response
    if message_type == _MESSAGE_TYPE_STATUS:
        for idx, status in enumerate(parking.is_occupied_slots):
            occupied = status == 0
            parking.is_occupied_slots[idx] = occupied
        notify_browsers()
        return None


def build_barrier_status_response(ok):
    response = bytearray()
    response.append(_MESSAGE_TYPE_RESPONSE)
    response.append(1 if ok else 0)
    response.append(parking.max_vehicles)
    response.append(parking.current_vehicles_count)
    response.append(len(parking.cards_id))
    for card_id in parking.cards_id:
        for c in card_id:
            response.append(ord(c))
    return response.decode("utf-8")


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
            if message == "browser":
                client.client_type = "browser"
                print(f"Client {client_id} is a browser")
                client.events.put_nowait(build_browser_message())
                continue
            if message == "Connected":
                print(message)
                continue

            response = process_message(message)
            if response is not None:
                client.events.put_nowait(response)

            print(f"Client {client_id} received message: {message}")
            print(f"Client {client_id} response: {response}")

        # client disconnected?
        except websockets.ConnectionClosedOK:
            print(f"Client {client_id} disconnected")
            clients.remove(client)
            return


async def websocket_handler():
    print("running websocket handler")
    async with websockets.serve(handler, port=3500):
        await asyncio.Future()


parking = Parking()
if __name__ == "__main__":
    asyncio.run(websocket_handler())
