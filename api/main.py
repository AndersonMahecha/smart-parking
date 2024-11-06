from flask import Flask, jsonify, request
from marshmallow import ValidationError

from api.model.user import RequestUserSchema
from api.repositories.card import CardRepository
from api.repositories.mysql import db_session, init_db
from api.repositories.parking_slot import ParkingSlotRepository
from api.repositories.user import UserRepository
from api.repositories.vehicle import VehicleRepository
from api.services.parking_service import ParkingService
from api.services.user import UserService

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


@app.route("/api/v1/health", methods=["GET"])
def health():
    return jsonify({"status": "ok"}), 200


@app.route("/api/v1/users", methods=["POST"])
def create_user():
    try:
        user = RequestUserSchema().load(request.json)
    except ValidationError as e:
        return jsonify({"message": str(e)}), 400
    except Exception as e:
        return jsonify({"message": str(e)}), 500

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
    except Exception as e:
        return jsonify({"message": str(e)}), 500

    user = userService.authenticate_user(username, password)

    if user is None:
        return jsonify({"message": "Invalid username or password"}), 401

    serialized_user = RequestUserSchema().dump(user)

    return jsonify(serialized_user), 200


@app.route("/api/v1/entries", methods=["POST"])
def register_entry():
    try:
        card_id = request.args.get("card_id")
        vehicle_plate = request.args.get("vehicle_plate")
        vehicle_image = request.files.get("vehicle_image")
    except Exception as e:
        return jsonify({"message": str(e)}), 500

    return jsonify({"message": "Entry registered successfully"}), 201


@app.teardown_appcontext
def shutdown_session(exception=None):
    db_session.remove()


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080)
