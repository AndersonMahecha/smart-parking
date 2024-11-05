import uuid

from sqlalchemy import Column, String, ForeignKey, TIMESTAMP

from api.repositories.mysql import Base


class Vehicle(Base):
    __tablename__ = "vehicles"

    identifier = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()))
    license_plate = Column(String(10), unique=True, nullable=True)
    short_code = Column(String(6), unique=True, nullable=True)
    vehicle_type = Column(String(10), nullable=False)
    entry_date = Column(TIMESTAMP, nullable=False)
    parking_slot_id = Column(
        String(36), ForeignKey("parking_slots.identifier"), nullable=True
    )

    def __init__(
        self,
        identifier,
        license_plate,
        short_code,
        vehicle_type,
        entry_date,
        parking_slot_id,
    ):
        super().__init__()
        self.identifier = identifier
        self.license_plate = license_plate
        self.short_code = short_code
        self.vehicle_type = vehicle_type
        self.entry_date = entry_date
        self.parking_slot_id = parking_slot_id

    def __repr__(self):
        return f"<Vehicle {self.identifier}>"
