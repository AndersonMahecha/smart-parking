package edu.co.sergio.arboleda.smartparkingapi.repository.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class ParkingRegister {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "VARCHAR(255)")
	private UUID id;

	@JoinColumn(name = "client_id")
	@ManyToOne
	private Client client;
	@Column(name = "entry_time", nullable = false)
	private LocalDateTime entry;
	@Column(name = "exit_time")
	private LocalDateTime exit;
	@Column(name = "total_cost")
	private BigDecimal totalCost;
	@Column(name = "payed")
	private Boolean payed;

	public ParkingRegister() {
		super();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
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

	private ParkingRegister(Builder builder) {
		client = builder.client;
		entry = builder.entry;
		exit = builder.exit;
		totalCost = builder.totalCost;
		payed = builder.payed;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public Boolean getPayed() {
		return payed;
	}

	public void setPayed(Boolean payed) {
		this.payed = payed;
	}

	public static final class Builder {

		private Client client;
		private LocalDateTime entry;
		private LocalDateTime exit;
		private BigDecimal totalCost;
		private Boolean payed;

		private Builder() {
		}

		public Builder withClient(Client client) {
			this.client = client;
			return this;
		}

		public Builder withEntry(LocalDateTime entry) {
			this.entry = entry;
			return this;
		}

		public Builder withExit(LocalDateTime exit) {
			this.exit = exit;
			return this;
		}

		public Builder withTotalCost(BigDecimal totalCost) {
			this.totalCost = totalCost;
			return this;
		}

		public Builder withPayed(Boolean payed) {
			this.payed = payed;
			return this;
		}

		public ParkingRegister build() {
			return new ParkingRegister(this);
		}

	}

}
