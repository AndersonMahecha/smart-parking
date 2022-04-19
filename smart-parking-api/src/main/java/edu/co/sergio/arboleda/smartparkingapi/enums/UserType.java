package edu.co.sergio.arboleda.smartparkingapi.enums;

public enum UserType {
	ROOT(0, "ROOT"),
	ADMIN(1, "ADMIN"),
	CASHIER(2, "CASHIER"),
	CLIENT(3, "CLIENT");

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
