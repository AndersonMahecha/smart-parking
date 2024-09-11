from api.model.user import User as UserModel


class UserRepository:
    def __init__(self):
        self.users = []

    def create_user(self, user: UserModel):
        self.users.append(user)
        print("saved users", len(self.users))

    def get_user(self, user_id: str) -> UserModel | None:
        for user in self.users:
            if user.identifier == user_id:
                return user
        return None
