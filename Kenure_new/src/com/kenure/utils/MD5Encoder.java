package com.kenure.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;

/**
 * This class provides MD5 Encryption of Password of all users
 * 
 * @author TatvaSoft
 *
 */
public class MD5Encoder {

	/**
	 * Logger object
	 */
	private static final Logger logger = LoggerUtils.getInstance(MD5Encoder.class);
	
	// MD5 Encoder object 
	private static MD5Encoder md5Instance;

	private MD5Encoder(){
		// Singleton object throughout application
	}

	public static String MD5Encryptor(String input){
		
		MD5Encoder instance = getMD5EncoderInstance();
		
		if(instance != null){
			
			MessageDigest md5Instance = instance.getMessageDigestInstance();
			
			if(md5Instance != null){
				byte[] hashedByteInput = null;
				
				try {
					hashedByteInput = md5Instance.digest(input.getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.error(e.getMessage());
				}
				
				return conversionOfByteToHexString(hashedByteInput);
			}
			logger.warn("Can't catch Message Digest Instance");
		}
		logger.warn("Not able to encrypt password");
		return null;
	}

	private static MD5Encoder getMD5EncoderInstance(){
		
		if(md5Instance == null){
			md5Instance = new MD5Encoder(); 
			return md5Instance;
		}else{
			return md5Instance;
		}
	}

	private MessageDigest getMessageDigestInstance(){
		
		try {
			return MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	private static String conversionOfByteToHexString(byte[] arrayBytes) {
		
		StringBuffer stringInputBuffer = new StringBuffer();
		
		for (int i = 0; i < arrayBytes.length; i++) {
			stringInputBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		
		return stringInputBuffer.toString();
	}
}