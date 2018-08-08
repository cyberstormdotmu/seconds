package fi.korri.epooq.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class EpooqConstant {

	
	public static final String USER_EXIST = "Väärä sähköposti jo olemassa";
	
	
	public static final String USER_REG_FAIL = "Käyttäjän Rekisteröinti epäonnistui";
	
	public static final String USER_REG_SUCCESS = "Käyttäjän Rekisteröinti menestys";

	//Forgot Password
	
	public static final String EMAILID_NOT_EXIST = "Väärä sähköposti ei olemassa";
	
	public static final String EMAIL_SEND_SUCC = "Sähköposti on lähetetty onnistuneesti";
	
	public static final String  FORGOT_PASSWORD_FAIL = "epäonnistui to päivitys salasana";
	
	public static String  FORGOT_PASSWORD_MESSAGE = "";
	
	public static boolean  FORGOT_PASSWORD_STATUS;
	
	public static final String STORY_MODE_EDIT = "EDIT";
	
	public static final String STORY_MODE_NEW = "NEW";

}
