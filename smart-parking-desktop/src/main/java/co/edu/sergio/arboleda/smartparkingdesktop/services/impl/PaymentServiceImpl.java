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
import co.edu.sergio.arboleda.smartparkingdesktop.pojo.PaymentInfoResponse;
import co.edu.sergio.arboleda.smartparkingdesktop.services.PaymentService;

public class PaymentServiceImpl implements PaymentService {

	private final WebClient client;
	private final ObjectMapper mapper;

	public PaymentServiceImpl() {
		String url = Optional.ofNullable(System.getenv("API_URL")).orElse("http://localhost:8080");

		client = WebClient.builder()
				.baseUrl(url)
				.defaultCookie("cookieKey", "cookieValue")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
		mapper = new ObjectMapper();
	}

	@Override
	public PaymentInfoResponse getPayment(String licenseCode, String token) throws GenericException {
		AtomicBoolean error = new AtomicBoolean(false);
		GenericResponse genericResponse = client.get()
				.uri(uriBuilder -> uriBuilder.path("/payments/amount/{licenseCode}")
						.build(licenseCode.trim()))
				.headers(httpHeaders -> httpHeaders.setBearerAuth(token))
				.accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {

						return response.bodyToMono(PaymentInfoResponse.class);
					} else {
						error.set(true);
						return response.bodyToMono(ErrorResponse.class);
					}
				}).block();

		if (error.get() && genericResponse instanceof ErrorResponse) {
			ErrorResponse errorResponse = (ErrorResponse) genericResponse;
			throw new GenericException(errorResponse.getMessage(), errorResponse.getCode());
		}

		return (PaymentInfoResponse) genericResponse;
	}

	@Override
	public PaymentInfoResponse pay(String paymentLicenseCode, String token) throws GenericException {
		AtomicBoolean error = new AtomicBoolean(false);
		GenericResponse genericResponse = client.post()
				.uri(uriBuilder -> uriBuilder.path("/payments/pay/{licenseCode}")
						.build(paymentLicenseCode.trim()))
				.headers(httpHeaders -> httpHeaders.setBearerAuth(token))
				.accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (!response.statusCode().is2xxSuccessful()) {
						error.set(true);
						return response.bodyToMono(ErrorResponse.class);
					}
					return response.bodyToMono(PaymentInfoResponse.class);
				}).block();

		if (error.get() && genericResponse instanceof ErrorResponse) {
			ErrorResponse errorResponse = (ErrorResponse) genericResponse;
			throw new GenericException(errorResponse.getMessage(), errorResponse.getCode());
		}

		return (PaymentInfoResponse) genericResponse;

	}

}
