import uuid
from datetime import datetime

from sqlalchemy import Column, String, ForeignKey, TIMESTAMP, Boolean

from api.repositories.mysql import Base


class Vehicle(Base):
    __tablename__ = "vehicles"

    identifier = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()))
    license_plate = Column(String(10), unique=True, nullable=True, index=True)
    short_code = Column(String(6), unique=True, nullable=True, index=True)
    vehicle_type = Column(String(10), nullable=False)
    entry_date = Column(TIMESTAMP, nullable=False)
    parking_slot_id = Column(
        String(36), ForeignKey("parking_slots.identifier"), nullable=True
    )
    payment_date = Column(TIMESTAMP, nullable=True)
    has_exited = Column(Boolean, nullable=False, default=False)

    def __init__(
        self,
        identifier,
        license_plate,
        short_code,
        vehicle_type,
        entry_date,
        parking_slot_id,
        payment_date=None,
        has_exited=None,
    ):
        super().__init__()
        self.identifier = identifier
        self.license_plate = license_plate
        self.short_code = short_code
        self.vehicle_type = vehicle_type
        if entry_date is None:
            entry_date = datetime.now()
        self.entry_date = entry_date
        self.parking_slot_id = parking_slot_id
        self.payment_date = payment_date
        self.has_exited = has_exited

    def __repr__(self):
        return f"<Vehicle {self.identifier}>"
