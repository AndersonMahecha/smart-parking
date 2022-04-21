package co.edu.sergio.arboleda.smartparkingdesktop.pojo;

public class LoginRequest {

	public final String username;
	public final String password;

	public LoginRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}
