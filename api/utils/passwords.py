import bcrypt


def hash_password(password: str) -> str:
    """Hashes a password using the bcrypt algorithm."""
    return bcrypt.hashpw(password.encode(), bcrypt.gensalt()).decode()


def verify_password(password: str, hashed_password: str) -> bool:
    """Verifies a password against a hashed password."""
    return bcrypt.checkpw(password.encode(), hashed_password.encode())
