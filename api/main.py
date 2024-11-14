from flask import Flask, jsonify, request, render_template
from marshmallow import ValidationError

from api.model.user import RequestUserSchema
from api.repositories.mysql import db_session, init_db
from api.repositories.user import UserRepository
from api.services.user import UserService

app = Flask(__name__)

userRepository = UserRepository(db_session)
userService = UserService(userRepository)

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
        card_id = request.form.get("card_id")
        vehicle_plate = request.form.get("vehicle_plate")
        vehicle_image = request.files.get("vehicle_image")
    except Exception as e:
        return jsonify({"message": str(e)}), 500


@app.teardown_appcontext
def shutdown_session(exception=None):
    db_session.remove()


# if __name__ == "__main__":
#     app.run(host="0.0.0.0", port=8080)
