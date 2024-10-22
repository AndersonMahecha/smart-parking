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
            role=user.role.value,
        )
        self.db_session.add(user_schema)
        self.db_session.commit()

    def get_user_by_username(self, username: str) -> UserModel:
        user_schema = (
            self.db_session.query(UserSchema).filter_by(username=username).first()
        )

        if user_schema is None:
            return None

        return UserModel(
            identifier=user_schema.id,
            first_name=user_schema.first_name,
            last_name=user_schema.last_name,
            username=user_schema.username,
            password=user_schema.password,
            role=user_schema.role,
        )
