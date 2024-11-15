import asyncio
import random
import websockets
import json


async def handler(websocket):

    # create periodic task:
    asyncio.create_task(send(websocket))

    while True:
        try:
            message = await websocket.recv()
            print(message)

        # client disconnected?
        except websockets.ConnectionClosedOK:
            break


async def send(websocket):
    print("new client connected")
    while True:
        data = [
            {"name": "Random Int 1", "number": random.randint(0, 1000)},
            {"name": "Random Int 2", "number": random.randint(1001, 2000)},
            {"name": "Random Int 3", "number": random.randint(2001, 3000)},
        ]

        try:
            await websocket.send(json.dumps(data))

        # client disconnected?
        except websockets.ConnectionClosedOK:
            break

        await asyncio.sleep(0.5)
    print("client disconnected")


async def main():
    async with websockets.serve(handler, "localhost", 3500):
        await asyncio.Future()  # run forever


if __name__ == "__main__":
    asyncio.run(main())
