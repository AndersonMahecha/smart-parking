package co.edu.sergio.arboleda.smartparkingdesktop.pojo;

public class ClientApi extends GenericResponse {

	private String name;
	private String documentType;
	private String documentNumber;
	private String mobileNumber;
	private String email;
	private String licenseCode;

	public ClientApi() {
		super();
	}

	private ClientApi(Builder builder) {
		setName(builder.name);
		setDocumentType(builder.documentType);
		setDocumentNumber(builder.documentNumber);
		setMobileNumber(builder.mobileNumber);
		setEmail(builder.email);
		setLicenseCode(builder.licenseCode);
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLicenseCode() {
		return licenseCode;
	}

	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}

	public static final class Builder {

		private String name;
		private String documentType;
		private String documentNumber;
		private String mobileNumber;
		private String email;
		private String licenseCode;

		private Builder() {
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withDocumentType(String documentType) {
			this.documentType = documentType;
			return this;
		}

		public Builder withDocumentNumber(String documentNumber) {
			this.documentNumber = documentNumber;
			return this;
		}

		public Builder withMobileNumber(String mobileNumber) {
			this.mobileNumber = mobileNumber;
			return this;
		}

		public Builder withEmail(String email) {
			this.email = email;
			return this;
		}

		public Builder withLicenseCode(String licenseCode) {
			this.licenseCode = licenseCode;
			return this;
		}

		public ClientApi build() {
			return new ClientApi(this);
		}

	}

}
