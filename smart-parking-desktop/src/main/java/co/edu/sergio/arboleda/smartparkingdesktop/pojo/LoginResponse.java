package co.edu.sergio.arboleda.smartparkingdesktop.pojo;

public class LoginResponse extends GenericResponse {

	private String token;
	private UserApi user;

	public LoginResponse() {
		super();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserApi getUser() {
		return user;
	}

	public void setUser(UserApi user) {
		this.user = user;
	}

}
