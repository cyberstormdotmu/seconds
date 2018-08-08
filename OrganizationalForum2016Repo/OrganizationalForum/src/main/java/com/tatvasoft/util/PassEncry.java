package com.tatvasoft.util;

import java.security.*;

/**
 * @author TatvaSoft 
 * This class provide password encryption functionality to
 *    store password in MD5 format in Database for security of user's
 *       details
 */
public class PassEncry {
	
	/*
	 * This method encrypt the entered password.
	 */
	public static String encrypass(String password) {

		String encrypassword = null; // this local variable for store encrypted password

		try {

			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] output = md.digest();

			encrypassword = bytesToHex(output);

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}

		return encrypassword;

	}

	/*
	 *	This method converts password in byte to Hex code.	
	 */
	public static String bytesToHex(byte[] b) {
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < b.length; j++) {
			buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
			buf.append(hexDigit[b[j] & 0x0f]);
		}
		return buf.toString();
	}
}