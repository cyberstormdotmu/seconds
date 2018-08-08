package com.tatvasoft.util;

import java.util.Random;

/**
 *	@author TatvaSoft
 *	This class contain Random password generator functionality
 *		for sending recovery password to user to provide 
 *			forgot password functionality. 
 */
public class RandomPasswordGenerator {
	
	/**
	 * These instance variables are for allowed characters in password. 
	 */
	private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
	private static final String NUM = "0123456789";
	private static final String SPL_CHARS = "!@#$%^&*_=+-/";

	/**
	 * These instance variables are for generators of password.
	 */
	private static final int noOfCAPSAlpha = 1;
	private static final int noOfDigits = 1;
	private static final int noOfSplChars = 1;
	private static final int minLen = 8;
	private static final int maxLen = 12;

	/*
	 * This method generate random password
	 */
	public static char[] generatePswd() {

		Random rnd = new Random();
		int len = rnd.nextInt(maxLen - minLen + 1) + minLen;
		char[] pswd = new char[len];
		int index = 0;
		for (int i = 0; i < noOfCAPSAlpha; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = ALPHA_CAPS.charAt(rnd.nextInt(ALPHA_CAPS.length()));
		}
		for (int i = 0; i < noOfDigits; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = NUM.charAt(rnd.nextInt(NUM.length()));
		}
		for (int i = 0; i < noOfSplChars; i++) {
			index = getNextIndex(rnd, len, pswd);
			pswd[index] = SPL_CHARS.charAt(rnd.nextInt(SPL_CHARS.length()));
		}
		for (int i = 0; i < len; i++) {
			if (pswd[i] == 0) {
				pswd[i] = ALPHA.charAt(rnd.nextInt(ALPHA.length()));
			}
		}
		return pswd;
	}

	private static int getNextIndex(Random rnd, int len, char[] pswd) {
		int index = rnd.nextInt(len);
		while (pswd[index = rnd.nextInt(len)] != 0)
			;
		return index;
	}
}