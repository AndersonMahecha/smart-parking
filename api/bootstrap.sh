#!/bin/sh

export FLASK_APP=./main.py

: "${DB_USER:=root}"
: "${DB_PASSWORD:=password}"
: "${DB_HOST:="127.0.0.1:33060"}"
: "${DB_NAME:=smart-parking}"

export DB_USER
export DB_PASSWORD
export DB_HOST
export DB_NAME

flask --debug run -h 0.0.0.0 --port 8080
