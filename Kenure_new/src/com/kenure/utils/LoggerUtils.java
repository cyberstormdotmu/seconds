package com.kenure.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates and provide Slf4j Logger instance
 * 
 * @author TatvaSoft - JAVA
 *
 */
public class LoggerUtils {
	/**
	 * Logger object
	 */
	private static Logger log;

	private LoggerUtils() {

	}

	public static Logger getInstance(String className) {
		log = LoggerFactory.getLogger(className);
		return log;
	}

	public static <T> Logger getInstance(Class<T> className) {
		log = LoggerFactory.getLogger(className);
		return log;
	}

	public static String getCurrentDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss");
		return dateFormat.format(new Date());
	}
}