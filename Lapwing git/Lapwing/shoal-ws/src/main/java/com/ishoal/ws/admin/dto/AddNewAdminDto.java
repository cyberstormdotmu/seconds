package com.ishoal.ws.admin.dto;

public class AddNewAdminDto {
	
	private String firstname;
    private String surname;
    private String emailAddress;
    private String password;
    private String mobileNumber;

    private AddNewAdminDto() {
        super();
    }
    
    private AddNewAdminDto(Builder builder) {
        this();
        firstname = builder.firstname;
        surname = builder.surname;
        emailAddress = builder.emailAddress;
        password = builder.password;
        mobileNumber = builder.mobileNumber;
    }
    
    public static Builder anAddNewAdmin() {
        return new Builder();
    }

	public String getFirstname() {
		return firstname;
	}

	public String getSurname() {
		return surname;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}
    
	public static final class Builder {
		private String firstname;
	    private String surname;
	    private String emailAddress;
	    private String password;
	    private String mobileNumber;

        private Builder() {
        }

        public Builder firstname(String val) {
        	firstname = val;
            return this;
        }
        
        public Builder surname(String val) {
        	surname = val;
            return this;
        }

        public Builder emailAddress(String val) {
        	emailAddress = val;
            return this;
        }

        public Builder password(String val) {
        	password = val;
            return this;
        }
        
        public Builder mobileNumber(String val) {
        	mobileNumber = val;
            return this;
        }
        
        public AddNewAdminDto build() {
            return new AddNewAdminDto(this);
        }
    }
}
