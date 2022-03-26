package edu.co.sergio.arboleda.smartparkingapi.rest.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ParkingRegisterApi {
	private UUID id;
	private ClientApi client;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime entry;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime exit;
	private BigDecimal totalCost;
	private Boolean payed;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

}
