from sqlalchemy import Column, String
from sqlalchemy.dialects.postgresql import UUID
from api.repositories.mysql import Base
import uuid


class CardRepositoryModel(Base):
    __tablename__ = "cards"

    identifier = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()))
    card_id = Column(String(10), unique=True, nullable=False)
    short_code = Column(String(6), unique=True, nullable=False)

    def __init__(self, identifier, card_id, short_code):
        super().__init__()
        self.identifier = identifier
        self.card_id = card_id
        self.short_code = short_code

    def __repr__(self):
        return f"<Card {self.identifier}>"
