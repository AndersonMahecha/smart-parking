package co.edu.sergio.arboleda.smartparkingdesktop.pojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ParkingRegisterApi extends GenericResponse {

	private ClientApi client;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime entry;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime exit;
	private BigDecimal totalCost;
	private Boolean payed;
	private String licensePlate;

	public ParkingRegisterApi() {
		super();
	}

	public ClientApi getClient() {
		return client;
	}

	public void setClient(ClientApi client) {
		this.client = client;
	}

	public LocalDateTime getEntry() {
		return entry;
	}

	public void setEntry(LocalDateTime entry) {
		this.entry = entry;
	}

	public LocalDateTime getExit() {
		return exit;
	}

	public void setExit(LocalDateTime exit) {
		this.exit = exit;
	}

	public BigDecimal getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(BigDecimal totalCost) {
		this.totalCost = totalCost;
	}

	public Boolean getPayed() {
		return payed;
	}

	public void setPayed(Boolean payed) {
		this.payed = payed;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

}
