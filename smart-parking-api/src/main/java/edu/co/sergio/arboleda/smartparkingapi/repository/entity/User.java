package edu.co.sergio.arboleda.smartparkingapi.repository.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class User {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "VARCHAR(255)")
	private UUID id;
	@Column(name = "username", nullable = false)
	private String username;
	@Column(name = "password", nullable = false)
	private String password;
	@Column(name = "is_enabled")
	private Boolean enabled;
	@Column(name = "user_type")
	private Integer userType;

	public User() {
		super();
	}

	private User(Builder builder) {
		setUsername(builder.username);
		setPassword(builder.password);
		setEnabled(builder.enabled);
		setUserType(builder.userType);
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public static final class Builder {

		private String username;
		private String password;
		private Boolean enabled;
		private Integer userType;

		private Builder() {
		}

		public Builder withUsername(String username) {
			this.username = username;
			return this;
		}

		public Builder withPassword(String password) {
			this.password = password;
			return this;
		}

		public Builder withEnabled(Boolean enabled) {
			this.enabled = enabled;
			return this;
		}

		public Builder withUserType(Integer userType) {
			this.userType = userType;
			return this;
		}

		public User build() {
			return new User(this);
		}

	}

}
