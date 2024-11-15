from api.model.vehicle import Vehicle as VehicleModel


class HistoricalVehiclesRepository:
    def __init__(self, db_session):
        self.db_session = db_session

    def create_historical_vehicle(self, vehicle: VehicleModel):
        identifier = vehicle.identifier if vehicle.identifier is not None else None
        parking_slot_id = (
            vehicle.parking_slot_id if vehicle.parking_slot_id is not None else None
        )
        entry_date = vehicle.entry_date if vehicle.entry_date is not None else None
        entity = VehicleEntity(
            identifier=identifier,
            license_plate=vehicle.license_plate,
            short_code=vehicle.short_code,
            vehicle_type=vehicle.vehicle_type.value,
            entry_date=entry_date,
            parking_slot_id=parking_slot_id,
        )
        self.db_session.add(entity)
        self.db_session.commit()
        return entity
