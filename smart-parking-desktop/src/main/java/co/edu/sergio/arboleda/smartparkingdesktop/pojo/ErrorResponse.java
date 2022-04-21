package co.edu.sergio.arboleda.smartparkingdesktop.pojo;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class ErrorResponse extends GenericResponse {

	public HttpStatus status;
	public LocalDateTime timestamp;
	public String code;
	public String message;
	public String debugMessage;

	public ErrorResponse() {
		super();
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

}
