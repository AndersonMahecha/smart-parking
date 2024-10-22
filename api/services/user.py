from api.model.user import User
from api.repositories.user import UserRepository
from api.repositories.models.user import User as UserSchema
from api.utils.passwords import verify_password


class UserService:
    def __init__(self, user_repository: UserRepository):
        self.user_repository = user_repository

    def create_user(self, user: User):
        self.user_repository.create_user(user)

    def authenticate_user(self, username, password):
        user = self.user_repository.get_user_by_username(username)
        if user is None:
            return None

        if verify_password(password, user.password):
            return user
