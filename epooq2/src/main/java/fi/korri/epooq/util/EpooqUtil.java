package fi.korri.epooq.util;

import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

public class EpooqUtil {

	private static final String alphaNumericString="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; 
	
	public static String getRandomPassword(){
		int numberOfCharacter=8;
		StringBuilder password=new StringBuilder();
		for(int k=0;k<numberOfCharacter;k++){
			password.append(alphaNumericString.charAt(getRandomNum()));	
		}
		return password.toString();
	}
	
	public static int getRandomNum(){
		Random r=new Random();
		int randomNum = r.nextInt(62);
		return randomNum; 
	}
}
