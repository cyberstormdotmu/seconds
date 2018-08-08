package com.ishoal.ws.admin.dto;

public class AdminUpdateVendorCreditDto{
	
	private String id;
    private String usedCredits;
    private String vendorName;
    private String totalCredits;
    private String availableCredits;

    private AdminUpdateVendorCreditDto() {
        super();
    }
    
    private AdminUpdateVendorCreditDto(Builder builder) {
        this();
        id = builder.id;
        usedCredits = builder.usedCredits;
        vendorName = builder.vendorName;
        totalCredits = builder.totalCredits;
        availableCredits = builder.availableCredits;
    }
    
    public static Builder aUpdateVendorCredit() {
        return new Builder();
    }

	
	public String getId() {
		return id;
	}

	public String getUsedCredits() {
		return usedCredits;
	}

	public String getVendorName() {
		return vendorName;
	}

	public String getTotalCredits() {
		return totalCredits;
	}

	public String getAvailableCredits() {
		return availableCredits;
	}
    
	public static final class Builder {
		private String id;
	    private String usedCredits;
	    private String vendorName;
	    private String totalCredits;
	    private String availableCredits;

        private Builder() {
        }

        public Builder id(String val) {
        	id = val;
            return this;
        }
        
        public Builder usedCredits(String val) {
        	usedCredits = val;
            return this;
        }

        public Builder vendorName(String val) {
        	vendorName = val;
            return this;
        }

        public Builder totalCredits(String val) {
        	totalCredits = val;
            return this;
        }
        
        public Builder availableCredits(String val) {
        	availableCredits = val;
            return this;
        }
        
        public AdminUpdateVendorCreditDto build() {
            return new AdminUpdateVendorCreditDto(this);
        }
    }
}
