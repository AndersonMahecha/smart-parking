package edu.co.sergio.arboleda.smartparkingapi.repository.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Payment {
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "VARCHAR(255)")
	private UUID id;

	@Column(name = "date_time", nullable = false)
	private LocalDateTime dateTime;
	@Column(name = "amount", nullable = false)
	private BigDecimal amount;
	@Column(name = "method", nullable = false)
	private String method;
	@Column(name = "authorization_code")
	private String authorizationCode;

	private Payment(Builder builder) {
		dateTime = builder.dateTime;
		amount = builder.amount;
		method = builder.method;
		authorizationCode = builder.authorizationCode;
	}

	public Payment() {
		super();
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static final class Builder {

		private LocalDateTime dateTime;
		private BigDecimal amount;
		private String method;
		private String authorizationCode;

		private Builder() {
		}

		public Builder withDateTime(LocalDateTime dateTime) {
			this.dateTime = dateTime;
			return this;
		}

		public Builder withAmount(BigDecimal amount) {
			this.amount = amount;
			return this;
		}

		public Builder withMethod(String method) {
			this.method = method;
			return this;
		}

		public Builder withAuthorizationCode(String authorizationCode) {
			this.authorizationCode = authorizationCode;
			return this;
		}

		public Payment build() {
			return new Payment(this);
		}

	}

}
