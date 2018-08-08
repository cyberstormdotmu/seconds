package com.tatva.validator;

import java.util.Calendar;
import java.util.Date;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.tatva.domain.GlobalAttribute;

/**
 * 
 * @author pci94
 * Validation class for Booking Slot Module
 */
public class BookingSlotValidator implements Validator  {


	/*
	 * (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors) {
		GlobalAttribute globalAttribute=(GlobalAttribute)target;
		Date appliedDate=globalAttribute.getApplyDate();
		Calendar calendar=Calendar.getInstance();
		calendar.add(java.util.Calendar.MONTH, 1);  
		Date d=calendar.getTime();
		
		if(appliedDate==null){
			errors.rejectValue("applyDateString", "invalid.appliedDate");
		}else if(appliedDate.before(d)){
			errors.rejectValue("applyDateString", "invalid.appliedDate");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	public boolean supports(Class c) {
		// TODO Auto-generated method stub
		return GlobalAttribute.class.isAssignableFrom(c);
	}
}

