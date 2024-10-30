import uuid
from datetime import datetime
from enum import Enum

from marshmallow import (
    Schema,
    fields,
    post_load,
    validate,
    EXCLUDE,
    post_dump,
)

from api.utils.passwords import hash_password


class Role(Enum):
    admin = "admin"
    cashier = "cashier"
    client = "client"
    operator = "operator"


class User(object):
    def __init__(
        self,
        identifier=None,
        first_name="",
        last_name="",
        username="",
        password="",
        role=Role,
    ):
        if role != "client" and (username == "" or password == ""):
            raise ValueError(
                "Username and password are required for admin, cashier and operator roles."
            )

        self.identifier = identifier
        self.first_name = first_name
        self.last_name = last_name
        self.username = username

        if identifier is None:
            self.password = hash_password(password)
        else:
            self.password = password

        self.role = role
        self.created_at = datetime.now()

    def __repr__(self):
        return f"<User {self.identifier}>"

    def to_schema(self):
        return RequestUserSchema().dump(self)


class RequestUserSchema(Schema):
    class Meta:
        unknown = EXCLUDE

    id = fields.Str(dump_only=True, attribute="identifier")
    first_name = fields.Str(required=True)
    last_name = fields.Str(required=True)
    username = fields.Str(required=False)
    password = fields.Str(required=False, load_only=True)
    role = fields.Str(
        required=True,
        validate=validate.OneOf(["admin", "cashier", "client", "operator"]),
    )
    created_at = fields.DateTime()

    @post_load
    def make_user(self, data, **kwargs):
        data["role"] = Role(data["role"])
        return User(**data)

    @post_dump(pass_original=True)
    def add_identifier(self, data, original_data: User, **kwargs):
        data["role"] = original_data.role.value
        return data
