#!/bin/sh

export FLASK_APP=./main.py
export DB_USER=root
export DB_PASSWORD=password
export DB_HOST="127.0.0.1:33060"
export DB_NAME=smart-parking

pipenv run flask --debug run -h 0.0.0.0 --port 8080
