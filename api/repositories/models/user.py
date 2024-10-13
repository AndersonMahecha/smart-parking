import uuid

import sqlalchemy
from sqlalchemy import Column, Integer, String
from api.repositories.mysql import Base


class User(Base):
    __tablename__ = "users"
    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()))
    first_name = Column(String(100), unique=False)
    last_name = Column(String(100), unique=False)
    username = Column(String(100), unique=True)
    password = Column(String(100), unique=False)
    role = Column(String(20), unique=False)
    created_at = Column(sqlalchemy.DateTime, unique=False)

    def __init__(
        self, identifier, first_name, last_name, username, password, role, created_at
    ):
        super().__init__()
        self.id = identifier
        self.first_name = first_name
        self.last_name = last_name
        self.username = username
        self.password = password
        self.role = role
        self.created_at = created_at

    def __repr__(self):
        return f"<User {self.id}>"
