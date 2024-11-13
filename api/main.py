import random
import string

import socketio
from flask import Flask, jsonify, request
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
from flask_cors import CORS, cross_origin
from flask_socketio import SocketIO

app = Flask(__name__)

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

clients = []


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


def notify_clients():
    for client in clients:
        socketio.emit("update", room=client)


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
        vehicle = parkingService.register_vehicle_entry(vehicle_request, card_id)
    except DomainError as e:
        return jsonify({"message": str(e)}), 400

    serialized_vehicle = VehicleSchema().dump(vehicle)

    notify_clients()
    return jsonify(serialized_vehicle), 201


@app.route("/api/v1/system/status", methods=["GET"])
def get_system_status():
    return jsonify({"vehicles": 120, "parking_slots": 10}), 200


@app.teardown_appcontext
def shutdown_session(exception=None):
    db_session.remove()


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080, allow_unsafe_werkzeug=True)


def random_plate():
    return "".join(random.choices(string.ascii_uppercase + string.digits, k=6))
