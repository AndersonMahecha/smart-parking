from api.repositories.models.parking_slot import ParkingSlotEntity

from api.model.parking_slot import ParkingSlot as ParkingSlotModel


class ParkingSlotRepository:
    def __init__(self, db_session):
        self.db_session = db_session

    def get_parking_slot(self, parking_slot_id) -> ParkingSlotModel:
        slot = self.db_session.query(ParkingSlotEntity).filter(
            ParkingSlotEntity.identifier == parking_slot_id
        ).one()

        return ParkingSlotModel(
            identifier=slot.identifier,
            slot_number=slot.slot_number,
            status=slot.status,
            vehicle_id=slot.vehicle_id,
        )

    def get_all_parking_slots(self) -> [ParkingSlotModel]:
        slots = self.db_session.query(ParkingSlotEntity).all()

        return [
            ParkingSlotModel(
                identifier=slot.identifier,
                slot_number=slot.slot_number,
                status=slot.status,
                vehicle_id=slot.vehicle_id,
            )
            for slot in slots
        ]

    def update_parking_slot(self, parking_slot_id, vehicle_identifier=None):
        # Buscar el slot por su identificador
        if vehicle_identifier is None:
            rows_updated = self.db_session.query(ParkingSlotEntity).filter(
                ParkingSlotEntity.identifier == parking_slot_id
            ).update(
                {
                    ParkingSlotEntity.vehicle_id: None,
                    ParkingSlotEntity.status: "available",
                }
            )
        else:
            rows_updated = self.db_session.query(ParkingSlotEntity).filter(
                ParkingSlotEntity.identifier == parking_slot_id
            ).update(
                {
                    ParkingSlotEntity.vehicle_id: vehicle_identifier,
                    ParkingSlotEntity.status: "occupied",
                }
            )
        # Confirmar los cambios en la base de datos
        self.db_session.commit()
        return parking_slot_id
