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
