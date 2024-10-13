from api.model.user import User as UserModel
from api.repositories.models.user import User as UserSchema


class UserRepository:
    def __init__(self, db_session):
        self.db_session = db_session

    def create_user(self, user: UserModel):
        user_schema = UserSchema(
            identifier=user.identifier,
            first_name=user.first_name,
            last_name=user.last_name,
            username=user.username,
            password=user.password,
            role=user.role,
        )
        self.db_session.add(user_schema)
