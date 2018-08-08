package com.ishoal.ws.buyer.dto;

public class BuyerSummaryDto {
	private String firstName;
	private String surname;
	private String emailAddress;
	private String mobileNumber;
	private String appliedFor;
	private String westcoastNumber;
	private String registrationToken;

	public BuyerSummaryDto() {

	}

	private BuyerSummaryDto(Builder builder) {

		firstName = builder.firstName;
		surname = builder.surname;
		emailAddress = builder.emailAddress;
		mobileNumber = builder.mobileNumber;
		appliedFor = builder.appliedFor;
		westcoastNumber = builder.westcoastNumber;
		registrationToken = builder.registrationToken;
	}

	public String getWestcoastNumber() {
		return westcoastNumber;
	}

	public String getFirstName() {

		return firstName;
	}

	public String getSurname() {

		return surname;
	}

	public String getAppliedFor() {
		return appliedFor;
	}

	public String getEmailAddress() {

		return emailAddress;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getRegistrationToken() {
		return registrationToken;
	}

	public static Builder aUserSummary() {

		return new Builder();
	}

	public static final class Builder {
		private String emailAddress;
		private String surname;
		private String firstName;
		private String mobileNumber;
		private String appliedFor;
		private String westcoastNumber;
		private String registrationToken;

		public Builder() {

		}

		public Builder(BuyerSummaryDto copy) {

			this.firstName = copy.firstName;
			this.surname = copy.surname;
			this.emailAddress = copy.emailAddress;
			this.mobileNumber = copy.mobileNumber;
			this.appliedFor = copy.appliedFor;
			this.westcoastNumber = copy.westcoastNumber;
		}

		public Builder emailAddress(String val) {

			emailAddress = val;
			return this;
		}

		public Builder registrationToken(String val) {

			registrationToken = val;
			return this;
		}

		public Builder surname(String val) {

			surname = val;
			return this;
		}

		public Builder westcoastNumber(String val) {

			westcoastNumber = val;
			return this;
		}

		public Builder appliedFor(String val) {

			appliedFor = val;
			return this;
		}

		public Builder firstName(String val) {

			firstName = val;
			return this;
		}

		public Builder mobileNumber(String val) {

			mobileNumber = val;
			return this;
		}

		public BuyerSummaryDto build() {

			return new BuyerSummaryDto(this);
		}
	}
}
