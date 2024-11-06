from api.model.vehicle import Vehicle as VehicleModel
from api.repositories.models.vehicle import Vehicle as VehicleEntity


class VehicleRepository:
    def __init__(self, db_session):
        self.db_session = db_session

    def create_vehicle(self, vehicle: VehicleModel):
        entity = VehicleEntity(
            identifier=vehicle.identifier,
            license_plate=vehicle.license_plate,
            short_code=vehicle.short_code,
            vehicle_type=vehicle.vehicle_type.value,
            entry_date=vehicle.entry_date,
            parking_slot_id=vehicle.parking_slot_id,
        )
        self.db_session.add(entity)
        self.db_session.commit()

    def get_vehicle_by_license_plate(self, license_plate: str) -> VehicleModel | None:
        vehicle_schema = (
            self.db_session.query(VehicleEntity)
            .filter_by(license_plate=license_plate)
            .first()
        )

        if vehicle_schema is None:
            return None

        return VehicleModel(
            identifier=vehicle_schema.identifier,
            license_plate=vehicle_schema.license_plate,
            short_code=vehicle_schema.short_code,
            vehicle_type=vehicle_schema.vehicle_type,
            entry_date=vehicle_schema.entry_date,
            parking_slot_id=vehicle_schema.parking_slot_id,
        )

    def get_vehicle_by_short_code(self, short_code: str) -> VehicleModel | None:
        vehicle_schema = (
            self.db_session.query(VehicleEntity)
            .filter_by(short_code=short_code)
            .first()
        )

        if vehicle_schema is None:
            return None

        return VehicleModel(
            identifier=vehicle_schema.identifier,
            license_plate=vehicle_schema.license_plate,
            short_code=vehicle_schema.short_code,
            vehicle_type=vehicle_schema.vehicle_type,
            entry_date=vehicle_schema.entry_date,
            parking_slot_id=vehicle_schema.parking_slot_id,
        )

    # joins vehicle and parking slot tables to get the vehicle by parking slot number
    def get_vehicle_by_parking_slot_number(
        self, parking_slot_number: str
    ) -> VehicleModel | None:
        vehicle_schema = (
            self.db_session.query(VehicleEntity)
            .join(VehicleEntity.parking_slot_id)
            .filter_by(number=parking_slot_number)
            .first()
        )

        if vehicle_schema is None:
            return None

        return VehicleModel(
            identifier=vehicle_schema.identifier,
            license_plate=vehicle_schema.license_plate,
            short_code=vehicle_schema.short_code,
            vehicle_type=vehicle_schema.vehicle_type,
            entry_date=vehicle_schema.entry_date,
            parking_slot_id=vehicle_schema.parking_slot_id,
        )

    def get_all_vehicles(self) -> list[VehicleModel]:
        vehicle_schema = self.db_session.query(VehicleEntity).all()
        return [
            VehicleModel(
                identifier=vehicle.identifier,
                license_plate=vehicle.license_plate,
                short_code=vehicle.short_code,
                vehicle_type=vehicle.vehicle_type,
                entry_date=vehicle.entry_date,
                parking_slot_id=vehicle.parking_slot_id,
            )
            for vehicle in vehicle_schema
        ]