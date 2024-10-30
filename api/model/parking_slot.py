from enum import Enum
from typing import Optional
from uuid import UUID

from marshmallow import Schema, fields, post_load


class ParkingSlotStatus(Enum):
    available = "available"
    occupied = "occupied"
    reserved = "reserved"
    out_of_service = "out_of_service"


class ParkingSlot(object):
    def __init__(
        self,
        identifier: UUID,
        slot_number: str,
        status: ParkingSlotStatus,
        vehicle_id: Optional[UUID] = None,
    ):
        self.identifier = identifier
        self.slot_number = slot_number
        self.status = status
        self.vehicle_id = vehicle_id


class ParkingSlotSchema(Schema):
    identifier = fields.UUID(dump_only=True)
    slot_number = fields.Str(required=True)
    status = fields.Str(required=True)
    vehicle_id = fields.UUID(allow_none=True)

    @post_load
    def make_parking_slot(self, data, **kwargs):
        data["status"] = ParkingSlotStatus(data["status"])
        return ParkingSlot(**data)
