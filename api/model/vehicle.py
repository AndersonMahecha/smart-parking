from datetime import datetime
from enum import Enum
from typing import Optional
from uuid import UUID


from marshmallow import Schema, fields, post_load
from marshmallow.validate import OneOf


class VehicleType(Enum):
    car = "car"
    motorcycle = "motorcycle"
    unknown = "unknown"


class Vehicle(object):
    def __init__(
        self,
        identifier: Optional[UUID] = None,
        license_plate: Optional[str] = None,
        short_code: Optional[str] = None,
        vehicle_type: Optional[VehicleType] = None,
        entry_date: Optional[datetime] = None,
        parking_slot_id: Optional[UUID] = None,
    ):
        self.identifier = identifier
        self.license_plate = license_plate
        self.short_code = short_code
        self.vehicle_type = vehicle_type
        self.entry_date = entry_date
        self.parking_slot_id = parking_slot_id


class VehicleSchema(Schema):
    identifier = fields.UUID(dump_only=True)
    license_plate = fields.Str(allow_none=True)
    short_code = fields.Str(dump_only=True)
    vehicle_type = fields.Str(required=True, validate=OneOf(["car", "motorcycle"]))
    entry_date = fields.DateTime(dump_only=True)
    parking_slot_id = fields.UUID(dump_only=True)

    @post_load
    def make_vehicle(self, data, **kwargs):
        data["vehicle_type"] = VehicleType(data["vehicle_type"])
        return Vehicle(**data)
