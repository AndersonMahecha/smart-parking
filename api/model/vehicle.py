from datetime import datetime
from enum import Enum
from typing import Optional
from uuid import UUID


from marshmallow import Schema, fields, post_load


class VehicleType(Enum):
    car = "car"
    motorcycle = "motorcycle"
    unknown = "unknown"


class Vehicle(object):
    def __init__(
        self,
        identifier: UUID,
        license_plate: Optional[str],
        short_code: Optional[str],
        vehicle_type: VehicleType,
        entry_date: datetime,
        parking_slot_id: Optional[UUID],
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
    short_code = fields.Str(allow_none=True)
    type = fields.Str(required=True)
    entry_date = fields.DateTime(required=True)
    parking_slot_id = fields.UUID(allow_none=True)

    @post_load
    def make_vehicle(self, data, **kwargs):
        data["type"] = VehicleType(data["type"])
        return Vehicle(**data)
