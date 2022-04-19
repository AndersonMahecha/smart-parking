package edu.co.sergio.arboleda.smartparkingapi.rest.handler;

import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import edu.co.sergio.arboleda.smartparkingapi.util.exceptions.GenericException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	public RestExceptionHandler() {
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
																  HttpHeaders headers,
																  HttpStatus status,
																  WebRequest request) {
		String error = "Malformed JSON request";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "", error, ex));
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																  HttpHeaders headers,
																  HttpStatus status,
																  WebRequest request) {
		StringBuilder error = new StringBuilder("Validation Failed: ");
		for (ObjectError fieldError : ex.getBindingResult().getAllErrors()) {
			error.append(fieldError.getDefaultMessage());
			error.append(", ");
		}
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "", error.substring(0, error.length() - 2)));
	}

	@ExceptionHandler(GenericException.class)
	protected ResponseEntity<Object> handleProviderError(GenericException ex) {
		try {
			ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ex.getErrorCode(),
					ex.getMessage(), ex);
			return buildResponseEntity(apiError);
		} catch (Exception e) {
			return buildResponseEntity(new ApiError(HttpStatus.FORBIDDEN, ex.getErrorCode(), ex.getMessage(), ex));
		}
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleInternalError(Exception ex) {
		logger.error("internal error", ex);
		return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "", ex.getMessage(), ex));

	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
		logger.error("Constraint violation exception", ex);
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "", ex.getMessage(), ex));
	}

}
