import asyncio
import os
import random
import string
import sys

sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))

from flask import Flask, jsonify, request, render_template
from flask_cors import CORS
from marshmallow import ValidationError

from api.model.exceptions import DomainError
from api.model.user import RequestUserSchema
from api.model.vehicle import VehicleSchema
from api.repositories.card import CardRepository
from api.repositories.mysql import db_session, init_db
from api.repositories.parking_slot import ParkingSlotRepository
from api.repositories.user import UserRepository
from api.repositories.vehicle import VehicleRepository
from api.services.parking_service import ParkingService
from api.services.user import UserService

loop = asyncio.get_event_loop()
app = Flask(__name__)
CORS(app)  # Habilitar CORS para todas las rutas

userRepository = UserRepository(db_session)
vehicleRepository = VehicleRepository(db_session)
parkingSlotsRepository = ParkingSlotRepository(db_session)
cardRepository = CardRepository(db_session)

userService = UserService(userRepository)
parkingService = ParkingService(
    vehicle_repository=vehicleRepository,
    parking_slots_repository=parkingSlotsRepository,
    cards_repository=cardRepository,
)

init_db()

@app.route('/')
def index():
    return render_template('index.html')


@app.route('/ingreso')
def ingreso():
    return render_template('ingreso.html')


@app.route('/salida')
def salida():
    return render_template('salida.html')


@app.route('/pago')
def pago():
    return render_template('pago.html')


@app.route('/visualizacion')
def visualizacion():
    return render_template('visualizacion.html')


@app.route("/api/v1/health", methods=["GET"])
def health():
    return jsonify({"status": "ok"}), 200


@app.route("/api/v1/users", methods=["POST"])
def create_user():
    try:
        user = RequestUserSchema().load(request.json)
    except ValidationError as e:
        return jsonify({"message": str(e)}), 400
    except DomainError as e:
        return jsonify({"message": str(e)}), 400
    except KeyError as e:
        return jsonify({"message": f"Missing required field: {e}"}), 400

    userService.create_user(user)
    serialized_user = RequestUserSchema().dump(user)

    return jsonify(serialized_user), 201


@app.route("/api/v1/users/authenticate", methods=["POST"])
def authenticate_user():
    try:
        username = request.json["username"]
        password = request.json["password"]
    except KeyError as e:
        return jsonify({"message": f"Missing required field: {e}"}), 400

    user = userService.authenticate_user(username, password)

    if user is None:
        return jsonify({"message": "Invalid username or password"}), 401

    serialized_user = RequestUserSchema().dump(user)

    return jsonify(serialized_user), 200


from websockets.asyncio.client import connect


async def notify_clients(data):
    async with connect("ws://localhost:3500") as websocket:
        await websocket.send("broadcast")
        message = await websocket.recv()
        if message != "notified":
            print("error on notification")


@app.route("/api/v1/entries", methods=["POST"])
def register_entry():
    try:
        vehicle_request = VehicleSchema().load(request.json)
        vehicle_image = request.files.get("vehicle_image")
        card_id = request.args.get("card_id")
    except ValidationError as e:
        return jsonify({"message": str(e)}), 400
    except DomainError as e:
        return jsonify({"message": str(e)}), 400
    except KeyError as e:
        return jsonify({"message": f"Missing required field: {e}"}), 400

    try:
        vehicle = parkingService.register_vehicle_entry(
            vehicle_request, card_id)
    except DomainError as e:
        return jsonify({"message": str(e)}), 400

    serialized_vehicle = VehicleSchema().dump(vehicle)

    loop.run_until_complete(notify_clients("entry"))
    return jsonify(serialized_vehicle), 201


@app.route("/api/v1/exits", methods=["POST"])
def register_exit():
    try:
        vehicle_request = VehicleSchema().load(request.json)
        vehicle_image = request.files.get("vehicle_image")
        card_id = request.args.get("card_id")
    except ValidationError as e:
        return jsonify({"message": str(e)}), 400
    except DomainError as e:
        return jsonify({"message": str(e)}), 400
    except KeyError as e:
        return jsonify({"message": f"Missing required field: {e}"}), 400

    try:
        vehicle = parkingService.register_vehicle_exit(vehicle_request, card_id)
    except DomainError as e:
        return jsonify({"message": str(e)}), 400

    serialized_vehicle = VehicleSchema().dump(vehicle)

    loop.run_until_complete(notify_clients("exit"))
    return jsonify(serialized_vehicle), 201


@app.route("/api/v1/system/status", methods=["GET"])
def get_system_status():
    return jsonify({"vehicles": 120, "parking_slots": 10}), 200


@app.teardown_appcontext
def shutdown_session(exception=None):
    db_session.remove()


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080)


def random_plate():
    return "".join(random.choices(string.ascii_uppercase + string.digits, k=6))
