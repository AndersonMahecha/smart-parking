import uuid

from sqlalchemy import Column, String

from api.repositories.mysql import Base


class User(Base):
    __tablename__ = "users"
    id = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()))
    first_name = Column(String(100), nullable=False)
    last_name = Column(String(100), nullable=False)
    username = Column(String(50), unique=True, nullable=False)
    password = Column(String(100), nullable=False)
    role = Column(String(50), nullable=False)

    def __init__(self, identifier, first_name, last_name, username, password, role):
        super().__init__()
        self.id = identifier
        self.first_name = first_name
        self.last_name = last_name
        self.username = username
        self.password = password
        self.role = role

    def __repr__(self):
        return f"<User {self.id}>"
