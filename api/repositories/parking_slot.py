from api.repositories.models.parking_slot import ParkingSlotRepositoryModel


class ParkingSlotRepository:
    def __init__(self, db_session):
        self.db_session = db_session

    def get_all_parking_slots(self):
        return self.db_session.query(ParkingSlotRepositoryModel).all()
