import uuid

from api.repositories.user import UserRepository
from api.model.user import User


class UserService:
    def __init__(self, user_repository: UserRepository):
        self.user_repository = user_repository

    def create_user(self, user: User):
        self.user_repository.create_user(user)
