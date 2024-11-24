import random
import string
from datetime import datetime

from api.model.exceptions import DomainError
from api.model.vehicle import Vehicle as VehicleModel
from api.repositories.card import CardRepository
from api.repositories.parking_slot import ParkingSlotRepository
from api.repositories.vehicle import VehicleRepository


class ParkingService:
    def __init__(
        self,
        vehicle_repository: VehicleRepository,
        parking_slots_repository: ParkingSlotRepository,
        cards_repository: CardRepository,
    ):
        self.vehicle_repository = vehicle_repository
        self.parking_slots_repository = parking_slots_repository
        self.cards_repository = cards_repository

    def register_vehicle_entry(
        self, vehicle: VehicleModel, card_id: str
    ) -> VehicleModel:
        card = None
        if card_id is not None:
            card = self.cards_repository.get_by_id(card_id)
            if card is None:
                raise DomainError("Card not found")

        found_vehicle = self.vehicle_repository.get_vehicle_by_license_plate(
            vehicle.license_plate
        )

        if found_vehicle is not None:
            raise DomainError("Vehicle already registered")

        while True:
            short_code = generate_short_code()
            found_vehicle = self.vehicle_repository.get_vehicle_by_short_code(
                short_code
            )
            if found_vehicle is None:
                break

        transient_vehicle = VehicleModel(
            license_plate=vehicle.license_plate,
            short_code=short_code,
            vehicle_type=vehicle.vehicle_type,
            entry_date=vehicle.entry_date,
        )

        if card is not None:
            try:
                self.cards_repository.update_card(card_id, short_code)
            except DomainError as e:
                if e.message == "Card already has a short code":
                    raise DomainError(
                        "Card is already associated with a vehicle")
            except Exception as e:
                raise e

        vehicle = self.vehicle_repository.create_vehicle(transient_vehicle)

        return vehicle

    def register_vehicle_exit(self, license_plate, card_id: str) -> VehicleModel:

        card = None
        if card_id is not None:
            card = self.cards_repository.get_by_id(card_id)
            if card is None:
                raise DomainError("Card not found")
            # TODO get vehicle by card

        found_vehicle = self.vehicle_repository.get_vehicle_by_license_plate(
            license_plate
        )

        if found_vehicle is None:
            raise DomainError("Vehicle not found")

        if found_vehicle.payment_date is None:
            raise DomainError("Vehicle has not paid")

        if card is not None:
            try:
                self.cards_repository.update_card(card_id, None)
            except Exception as e:
                raise e

        if found_vehicle.has_exited:
            raise DomainError("Vehicle has already left the parking")

        found_vehicle.has_exited = True

        self.vehicle_repository.update_vehicle(found_vehicle)
        return found_vehicle

    def slots_count(self):
        parking_slots = self.parking_slots_repository.get_all_parking_slots()
        return len(parking_slots)

    def vehicles_count(self):
        vehicles = self.vehicle_repository.get_all_vehicles()

        return len([vehicle for vehicle in vehicles if vehicle.has_exited is False])

    def get_total_cost(self, short_code, card_id, license_plate):
        vehicle = None
        if card_id is not None:
            card = self.cards_repository.get_by_id(card_id)
            if card is None:
                raise DomainError("Card not found")
            short_code = card.short_code
        if short_code is not None:
            vehicle = self.vehicle_repository.get_vehicle_by_short_code(
                short_code)

        if license_plate is not None:
            vehicle = self.vehicle_repository.get_vehicle_by_license_plate(
                license_plate
            )

        if vehicle is None:
            raise DomainError("Vehicle not found")

        if vehicle.payment_date is not None:
            raise DomainError("Vehicle has already paid")

        # calculate total time
        total_time = datetime.now() - vehicle.entry_date
        total_time_in_minutes = total_time.total_seconds() / 60

        if total_time_in_minutes < 0:
            total_time_in_minutes = 1

        cost_per_minute = 90
        if vehicle.vehicle_type == "motorcycle":
            cost_per_minute = 50

        total_cost = max(total_time_in_minutes * cost_per_minute, 50)
        return vehicle, datetime.now(), total_time_in_minutes, cost_per_minute, total_cost

    def pay_parking(self, vehicle_id):
        vehicle = self.vehicle_repository.get_vehicle_by_id(vehicle_id)
        if vehicle is None:
            raise DomainError("Vehicle not found")

        vehicle.payment_date = datetime.now()
        self.vehicle_repository.update_vehicle(vehicle)
        return vehicle


def generate_short_code():
    return "".join(random.choices(string.ascii_uppercase + string.digits, k=6))
