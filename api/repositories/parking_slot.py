from api.repositories.models.parking_slot import ParkingSlotEntity

from api.model.parking_slot import ParkingSlot as ParkingSlotModel


class ParkingSlotRepository:
    def __init__(self, db_session):
        self.db_session = db_session

    def get_all_parking_slots(self) -> [ParkingSlotModel]:
        slots = self.db_session.query(ParkingSlotEntity).all()

        return [
            ParkingSlotModel(
                identifier=slot.identifier,
                slot_number=slot.slot_number,
                status=slot.status,
            )
            for slot in slots
        ]
