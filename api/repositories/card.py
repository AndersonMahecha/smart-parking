from api.repositories.models.card import CardRepositoryModel
from sqlalchemy.orm import Session


class CardRepository:
    def __init__(self, session: Session):
        self.session = session

    def get_short_code_by_card_id(self, card_id: str) -> str | None:
        card = (
            self.session.query(CardRepositoryModel).filter_by(card_id=card_id).first()
        )

        if card is None:
            return None

        return card.short_code

    def update_card(self, card_id: str, short_code: str):
        card = (
            self.session.query(CardRepositoryModel).filter_by(card_id=card_id).first()
        )

        if card is None:
            return None

        card.short_code = short_code
        self.session.commit()
