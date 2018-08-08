package com.digiwise.reader;

import java.util.Date;

import hello.DijiWiseResultsArray;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;

public class JavaListener {
	final static Logger logger = Logger.getLogger(JavaListener.class);

	public static final String MONGO_HOST = "104.130.31.144";
	
	@Autowired
	public static DijiWiseResultsArray getResult(String parent_id,String child_user_id,String access_token, String oauth_token_secret, String social_media_type,String social_site_user_id, DijiWiseResultsArray array) throws Exception {
			PropertyConfigurator.configure("D:\\Sushant\\WorkSpaces\\Eclipse2Workspace\\DigiwiseReader\\src\\main\\resources\\log4j.properties");
			logger.info("start of the program");
			/*	
			   Properties prop = new Properties();
				InputStream input = null;
				try {
					File file = new File("/dijiwise/config.properties");
					input = new FileInputStream(file);
					prop.load(input);
				} catch (Exception e) {
					logger.error("Exception Info:" + e.getMessage() + " e.toSting: "
							+ e.toString());
				} finally {
					input.close();
				}
			*/
			//int count = 0;
			System.out.println("start of the webservice call: " + new Date());
			
			if (social_media_type.equalsIgnoreCase("facebook")) {
			
				logger.info("From Facebook");
				return FacebookListner.newQueueMessage(  parent_id, child_user_id, access_token, social_media_type, social_site_user_id,array);
			}
			if (social_media_type.equalsIgnoreCase("instagram")) {
			
				logger.info("From instagram");
				return InstagramListner.instraGramMain(parent_id, child_user_id, access_token, social_media_type, social_site_user_id,array);
			
			}
			if (social_media_type.equalsIgnoreCase("twitter")) {
				
				logger.info("From twitter");
				return TwitterListener.getTwitterTweets(  parent_id, child_user_id, access_token, oauth_token_secret, social_media_type, social_site_user_id,array);
			}
			if (social_media_type.equalsIgnoreCase("google_plus1")) {
			
				logger.info("From GooglePlus");
				return GooglePlusListener.getUserGoogleFeeds( parent_id, child_user_id, access_token, oauth_token_secret, social_media_type, social_site_user_id,array);
			}
		    return null;
	}
	

}
