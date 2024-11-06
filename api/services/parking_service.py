import random
import string

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

    def register_vehicle_entry(self, card_id: str, vehicle_plate: str) -> None:
        card = None
        if card_id is not None:
            card = self.cards_repository.get_by_id(card_id)
            if card is None:
                raise Exception("Card not found")

        vehicle = self.vehicle_repository.get_vehicle_by_license_plate(vehicle_plate)

        if vehicle is not None:
            raise Exception("Vehicle already registered")

        while True:
            short_code = generate_short_code()
            vehicle = self.vehicle_repository.get_vehicle_by_short_code(short_code)
            if vehicle is None:
                break

        if card is not None:
            self.cards_repository.update_card(card_id, short_code)


def generate_short_code():
    return "".join(random.choices(string.ascii_uppercase + string.digits, k=6))
