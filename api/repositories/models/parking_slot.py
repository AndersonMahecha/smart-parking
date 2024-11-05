import uuid

from sqlalchemy import Column, String, ForeignKey

from api.repositories.mysql import Base


class ParkingSlotRepositoryModel(Base):
    __tablename__ = "parking_slots"

    identifier = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()))
    slot_number = Column(String(10), unique=True, nullable=False)
    status = Column(String(10), nullable=False)
    vehicle_id = Column(String(36), ForeignKey("vehicles.identifier"), nullable=True)

    def __init__(self, identifier, slot_number, status, vehicle_id):
        super().__init__()
        self.identifier = identifier
        self.slot_number = slot_number
        self.status = status
        self.vehicle_id = vehicle_id

    def __repr__(self):
        return f"<ParkingSlot {self.identifier}>"
