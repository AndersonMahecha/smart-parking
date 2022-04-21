package co.edu.sergio.arboleda.smartparkingdesktop.enums;

public enum UserType {
	ROOT(0, "ROOT"),
	ADMIN(1, "ADMIN"),
	CASHIER(2, "CASHIER"),
	CLIENT(3, "CLIENT"),
	ENTRY(4, "ENTRY"),
	EXIT(5, "EXIT");

	private final Integer code;
	private final String type;

	UserType(Integer code, String type) {
		this.code = code;
		this.type = type;
	}

	public Integer getCode() {
		return code;
	}

	public String getType() {
		return type;
	}
}
