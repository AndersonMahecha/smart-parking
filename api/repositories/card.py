from api.repositories.models.card import CardRepositoryModel
from sqlalchemy.orm import Session
from api.model.card import Card as CardModel


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

    def update_card(self, card_id: str, short_code: str | None) -> None:
        card = (
            self.session.query(CardRepositoryModel).filter_by(card_id=card_id).first()
        )

        if card is None:
            return None

        if card.short_code is not None and short_code is not None:
            raise Exception("Card already has a short code")

        card.short_code = short_code

        self.session.commit()

    def get_by_id(self, card_id: str) -> CardModel | None:
        card = (
            self.session.query(CardRepositoryModel).filter_by(card_id=card_id).first()
        )

        if card is None:
            return None

        return CardModel(
            identifier=card.identifier,
            card_id=card.card_id,
            short_code=card.short_code,
        )
