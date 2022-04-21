package co.edu.sergio.arboleda.smartparkingdesktop.services.impl;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.sergio.arboleda.smartparkingdesktop.exceptions.GenericException;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.ErrorResponse;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.GenericResponse;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.ParkingRegisterApi;
import co.edu.sergio.arboleda.smartparkingdesktop.services.ParkingService;

public class ParkingServiceImpl implements ParkingService {

	private final WebClient client;
	private final ObjectMapper mapper;

	public ParkingServiceImpl() {
		String url = Optional.ofNullable(System.getenv("API_URL")).orElse("http://localhost:8080");

		client = WebClient.builder()
				.baseUrl(url)
				.defaultCookie("cookieKey", "cookieValue")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
		mapper = new ObjectMapper();
	}

	@Override
	public ParkingRegisterApi registerEntry(String licenseCode, String token) throws GenericException {
		AtomicBoolean error = new AtomicBoolean(false);
		GenericResponse genericResponse = client.post()
				.uri(uriBuilder -> uriBuilder.path("/parking/entry")
						.queryParam("licenseCode", licenseCode.trim())
						.build())
				.headers(httpHeaders -> httpHeaders.setBearerAuth(token))
				.accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {

						return response.bodyToMono(ParkingRegisterApi.class);
					} else {
						error.set(true);
						return response.bodyToMono(ErrorResponse.class);
					}
				}).block();

		if (error.get() && genericResponse instanceof ErrorResponse) {
			ErrorResponse errorResponse = (ErrorResponse) genericResponse;
			throw new GenericException(errorResponse.getMessage(), errorResponse.getCode());
		}

		return (ParkingRegisterApi) genericResponse;
	}

}
