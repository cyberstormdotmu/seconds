package fi.korri.epooq.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class EpooqConstant {

	
	public static final String USER_EXIST = "V��r� s�hk�posti jo olemassa";
	
	
	public static final String USER_REG_FAIL = "K�ytt�j�n Rekister�inti ep�onnistui";
	
	public static final String USER_REG_SUCCESS = "K�ytt�j�n Rekister�inti menestys";

	//Forgot Password
	
	public static final String EMAILID_NOT_EXIST = "V��r� s�hk�posti ei olemassa";
	
	public static final String EMAIL_SEND_SUCC = "S�hk�posti on l�hetetty onnistuneesti";
	
	public static final String  FORGOT_PASSWORD_FAIL = "ep�onnistui to p�ivitys salasana";
	
	public static String  FORGOT_PASSWORD_MESSAGE = "";
	
	public static boolean  FORGOT_PASSWORD_STATUS;
	
	public static final String STORY_MODE_EDIT = "EDIT";
	
	public static final String STORY_MODE_NEW = "NEW";

}
