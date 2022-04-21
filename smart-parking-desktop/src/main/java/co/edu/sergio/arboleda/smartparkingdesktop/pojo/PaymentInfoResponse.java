package co.edu.sergio.arboleda.smartparkingdesktop.pojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentInfoResponse extends GenericResponse {

	@JsonProperty
	private LocalDateTime entryTime;
	@JsonProperty
	private LocalDateTime exitTime;
	@JsonProperty
	private long totalMinutes;
	@JsonProperty
	private BigDecimal totalAmount;
	@JsonProperty
	private ClientApi clientApi;
	@JsonProperty
	private String licensePlate;

	public PaymentInfoResponse() {
		super();
	}

	public LocalDateTime getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(LocalDateTime entryTime) {
		this.entryTime = entryTime;
	}

	public LocalDateTime getExitTime() {
		return exitTime;
	}

	public void setExitTime(LocalDateTime exitTime) {
		this.exitTime = exitTime;
	}

	public long getTotalMinutes() {
		return totalMinutes;
	}

	public void setTotalMinutes(long totalMinutes) {
		this.totalMinutes = totalMinutes;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public ClientApi getClientApi() {
		return clientApi;
	}

	public void setClientApi(ClientApi clientApi) {
		this.clientApi = clientApi;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

}
