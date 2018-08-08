package com.ishoal.core.buyer;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.util.ErrorType;
import com.ishoal.core.domain.Address;

public class AddressDatabaseExceptionHandler {

	public static final String UNIQUE_DEPARTMENT_NAME = "UNIQUE_BUYER_DEPARTMENT";
	public static final String DELIVERY_ADDRESS_EXISTS = "FK_BUYER_CREDIT_APPLICATION_INFORMATION_DELIVERY_ADDRESS";
	public static final String INVOICE_ADDRESS_EXISTS= "FK_BUYER_CREDIT_APPLICATION_INFORMATION_INVOICE_ADDRESS";
	public static final String REGISTERED_ADDRESS_EXISTS= "FK_BUYER_CREDIT_APPLICATION_INFORMATION_REGISTERED_ADDRESS";
	
	public AddressDatabaseExceptionHandler(){
	}
	 public PayloadResult<Address> handleDatabaseException(AddressRequest addNewAddressRequest, DataIntegrityViolationException dbException)
	{
		 PayloadResult<Address> result;
		 Throwable cause = dbException.getCause();
		 if (cause instanceof ConstraintViolationException) {
	            ConstraintViolationException exception = (ConstraintViolationException) dbException.getCause();

	            result = checkUniqueDepartmentnameViolation(addNewAddressRequest, exception);
	        } else {
	            throw dbException;
	        }
	        return result;
	}
	 
	 PayloadResult<Address> checkUniqueDepartmentnameViolation(AddressRequest addNewAddressRequest,
		        ConstraintViolationException exception) {

		        PayloadResult<Address> result = null;
		        if (UNIQUE_DEPARTMENT_NAME.equalsIgnoreCase(exception.getConstraintName())) {
		            result = PayloadResult.error(ErrorType.CONFLICT, String.format("Department '%s' already existing",
		            		addNewAddressRequest.getDepartmentName() ));
		        }
		        else if (DELIVERY_ADDRESS_EXISTS.equalsIgnoreCase(exception.getConstraintName())) {
		            result = PayloadResult.error(ErrorType.CONFLICT, String.format("Can not delete this address because this address is used as a delivery address in buyer credit application information."));
		        }
		        else if (INVOICE_ADDRESS_EXISTS.equalsIgnoreCase(exception.getConstraintName())) {
		            result = PayloadResult.error(ErrorType.CONFLICT, String.format("Can not delete this address because this address is used as an invoice address in buyer credit application information."));
		        }
		        else if (REGISTERED_ADDRESS_EXISTS.equalsIgnoreCase(exception.getConstraintName())) {
		            result = PayloadResult.error(ErrorType.CONFLICT, String.format("Can not delete this address because this address is used as a registered address in buyer credit application information." ));
		        }
		        return result;
		    }
}