import random
import string

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
        )

        if card is not None:
            try:
                self.cards_repository.update_card(card_id, short_code)
            except DomainError as e:
                if e.message == "Card already has a short code":
                    raise DomainError("Card is already associated with a vehicle")
            except Exception as e:
                raise e

        vehicle = self.vehicle_repository.create_vehicle(transient_vehicle)

        return vehicle


def generate_short_code():
    return "".join(random.choices(string.ascii_uppercase + string.digits, k=6))
