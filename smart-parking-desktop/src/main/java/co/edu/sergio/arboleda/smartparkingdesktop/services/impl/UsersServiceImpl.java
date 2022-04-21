package co.edu.sergio.arboleda.smartparkingdesktop.services.impl;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.sergio.arboleda.smartparkingdesktop.exceptions.GenericException;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.ErrorResponse;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.GenericResponse;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.LoginRequest;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.LoginResponse;
import co.edu.sergio.arboleda.smartparkingdesktop.services.UsersService;
import reactor.core.publisher.Mono;

public class UsersServiceImpl implements UsersService {

	private final WebClient client;
	private final ObjectMapper mapper;

	public UsersServiceImpl() {
		String url = Optional.ofNullable(System.getenv("API_URL")).orElse("http://localhost:8080");

		client = WebClient.builder()
				.baseUrl(url)
				.defaultCookie("cookieKey", "cookieValue")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
		mapper = new ObjectMapper();
	}

	@Override
	public LoginResponse login(String username, String password) throws GenericException, JsonProcessingException {
		AtomicBoolean error = new AtomicBoolean(false);
		GenericResponse genericResponse = client.post()
				.uri("/user/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(mapper.writeValueAsString(new LoginRequest(username, password))), String.class)
				.accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {

						return response.bodyToMono(LoginResponse.class);
					} else {
						error.set(true);
						return response.bodyToMono(ErrorResponse.class);
					}
				}).block();

		if (error.get() && genericResponse instanceof ErrorResponse) {
			ErrorResponse errorResponse = (ErrorResponse) genericResponse;
			throw new GenericException(errorResponse.getMessage(), errorResponse.getCode());
		}

		return (LoginResponse) genericResponse;

	}

}
