package com.tatva.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.tatva.domain.UserMaster;
import com.tatva.service.IUser;
import com.tatva.utils.DateUtil;

/**
 * 
 * @author pci94
 *	Validation class for User Module
 */
@Component
public class UserValidator implements Validator  {

	
	@Autowired
	private IUser user;
	
	@SuppressWarnings("rawtypes")
	/*
	 * (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	public boolean supports(Class c) {
		return UserMaster.class.isAssignableFrom(c);

	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@SuppressWarnings("unused")
	public void validate(Object target, Errors errors) {
		
		UserMaster userMaster=(UserMaster)target;
		if(userMaster.getDateOfJoiningString()!=null && !("".equals(userMaster.getDateOfJoiningString()))){
			userMaster.setDateOfJoining(DateUtil.convertDateFromStringtoDate(userMaster.getDateOfJoiningString()));
		}
		
		List<UserMaster> userMasterList= user.selectAllUsers();
		
		ValidationUtils.rejectIfEmpty(errors, "userId", "required.user.userId");
		ValidationUtils.rejectIfEmpty(errors, "firstName", "required.user.firstname");
		ValidationUtils.rejectIfEmpty(errors, "emailAddress", "required.user.emailAddress");
	}
}
