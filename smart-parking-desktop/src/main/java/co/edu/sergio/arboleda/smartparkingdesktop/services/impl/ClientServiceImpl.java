package co.edu.sergio.arboleda.smartparkingdesktop.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.sergio.arboleda.smartparkingdesktop.exceptions.GenericException;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.ClientApi;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.ErrorResponse;
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.GenericResponse;
import co.edu.sergio.arboleda.smartparkingdesktop.services.ClientService;
import reactor.core.publisher.Mono;

public class ClientServiceImpl implements ClientService {

	private final WebClient client;
	private final ObjectMapper mapper;

	public ClientServiceImpl() {
		String url = Optional.ofNullable(System.getenv("API_URL")).orElse("http://localhost:8080");

		client = WebClient.builder()
				.baseUrl(url)
				.defaultCookie("cookieKey", "cookieValue")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
		mapper = new ObjectMapper();
	}

	@Override
	public List<ClientApi> getClients(String token) throws GenericException {
		AtomicBoolean error = new AtomicBoolean(false);
		AtomicReference<Mono<ErrorResponse>> errorResponseAtom = new AtomicReference<>();
		ClientApi[] genericResponse = client.get()
				.uri("/clients")
				.headers(httpHeaders -> httpHeaders.setBearerAuth(token))
				.accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {
						return response.bodyToMono(ClientApi[].class);
					}
					error.set(true);
					errorResponseAtom.set(response.bodyToMono(ErrorResponse.class));
					return Mono.just(new ClientApi[0]);
				}).block();

		if (error.get()) {
			ErrorResponse errorResponse = errorResponseAtom.get().block();
			assert errorResponse != null;
			throw new GenericException(errorResponse.getMessage(), errorResponse.getCode());
		}

		return Arrays.stream(genericResponse != null ? genericResponse : new ClientApi[0])
				.collect(Collectors.toList());
	}

	@Override
	public ClientApi create(ClientApi clientApi, String token) throws JsonProcessingException, GenericException {
		AtomicBoolean error = new AtomicBoolean(false);
		GenericResponse genericResponse = client.post()
				.uri("/clients")
				.contentType(MediaType.APPLICATION_JSON)
				.headers(httpHeaders -> httpHeaders.setBearerAuth(token))
				.body(Mono.just(mapper.writeValueAsString(clientApi)), String.class)
				.accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {

						return response.bodyToMono(ClientApi.class);
					} else {
						error.set(true);
						return response.bodyToMono(ErrorResponse.class);
					}
				}).block();

		if (error.get() && genericResponse instanceof ErrorResponse) {
			ErrorResponse errorResponse = (ErrorResponse) genericResponse;
			throw new GenericException(errorResponse.getMessage(), errorResponse.getCode());
		}

		return (ClientApi) genericResponse;
	}

}
