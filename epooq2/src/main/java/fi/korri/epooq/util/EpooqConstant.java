package fi.korri.epooq.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class EpooqConstant {

	
	public static final String USER_EXIST = "Email id Already existed";
	
	
	public static final String USER_REG_FAIL = "User Registration Failed";
	
	public static final String USER_REG_SUCCESS = "User Registration Success";

	//Forgot Password
	
	public static final String EMAILID_NOT_EXIST = "Email id is not exist";
	
	public static final String EMAIL_SEND_SUCC = "Email send successfully";
	
	public static final String  FORGOT_PASSWORD_FAIL = "Failed to update password";
	
	public static String  FORGOT_PASSWORD_MESSAGE = "";
	
	public static boolean  FORGOT_PASSWORD_STATUS;
	
	public static final String STORY_MODE_EDIT = "EDIT";
	
	public static final String STORY_MODE_NEW = "NEW";

}
