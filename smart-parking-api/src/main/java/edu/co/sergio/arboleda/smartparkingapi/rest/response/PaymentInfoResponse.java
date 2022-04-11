package edu.co.sergio.arboleda.smartparkingapi.rest.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.co.sergio.arboleda.smartparkingapi.rest.api.UserApi;

public class PaymentInfoResponse {

	@JsonProperty
	private LocalDateTime entryTime;
	@JsonProperty
	private LocalDateTime exitTime;
	@JsonProperty
	private long totalMinutes;
	@JsonProperty
	private BigDecimal totalAmount;
	@JsonProperty
	private UserApi user;

	private PaymentInfoResponse(Builder builder) {
		entryTime = builder.entryTime;
		exitTime = builder.exitTime;
		totalMinutes = builder.totalMinutes;
		totalAmount = builder.totalAmount;
		user = builder.user;
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static final class Builder {

		private LocalDateTime entryTime;
		private LocalDateTime exitTime;
		private long totalMinutes;
		private BigDecimal totalAmount;
		private UserApi user;

		private Builder() {
		}

		public Builder withEntryTime(LocalDateTime entryTime) {
			this.entryTime = entryTime;
			return this;
		}

		public Builder withExitTime(LocalDateTime exitTime) {
			this.exitTime = exitTime;
			return this;
		}

		public Builder withTotalMinutes(long totalMinutes) {
			this.totalMinutes = totalMinutes;
			return this;
		}

		public Builder withTotalAmount(BigDecimal totalAmount) {
			this.totalAmount = totalAmount;
			return this;
		}

		public Builder withUser(UserApi user) {
			this.user = user;
			return this;
		}

		public PaymentInfoResponse build() {
			return new PaymentInfoResponse(this);
		}

	}

}
