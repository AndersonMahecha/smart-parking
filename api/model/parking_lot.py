import uuid
from enum import Enum

from marshmallow import Schema, fields, EXCLUDE, validate, post_dump, post_load


class ParkingSlotStatus(Enum):
    available = "available"
    occupied = "occupied"
    reserved = "reserved"
    out_of_service = "out_of_service"


class ParkingSlot(object):
    def __init__(
        self,
        identifier=None,
        number=None,
        status=None,
        vehicle=None,
    ):
        if identifier is None:
            identifier = str(uuid.uuid4())

        self.identifier = identifier
        self.number = number
        self.status = status
        self.vehicle = vehicle

    def __repr__(self):
        return f"<ParkingSlot {self.identifier}>"

    def __str__(self):
        return f"ParkingSlot {self.identifier}"


class ParkingSlotSchema(Schema):
    class Meta:
        unknown = EXCLUDE

    id = fields.Str(dump_only=True, attribute="identifier")
    number = fields.Str(required=True)
    status = fields.Str(
        required=True,
        validate=validate.OneOf([status.value for status in ParkingSlotStatus]),
    )
    vehicle = fields.Str(required=False)

    @post_load
    def make_parking_slot(self, data, **kwargs):
        return ParkingSlot(**data)

    @post_dump(pass_original=True)
    def update_status(self, data, original_data: ParkingSlot, **kwargs):
        data["status"] = original_data.status.value
        return data
