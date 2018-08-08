package fi.korri.epooq.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.korri.epooq.dao.StoryDao;
import fi.korri.epooq.dao.UserDao;
import fi.korri.epooq.model.User;
import fi.korri.epooq.util.EpooqConstant;
import fi.korri.epooq.util.EpooqUtil;


@Service
public class UserService {
	private final Logger log = Logger.getLogger(UserService.class.getName());
	
	@Autowired
	private UserDao userDao;
	// save registered user
	public String saveUser(User user){
		log.debug("Save Registered user");
		boolean flag;
		String message="";
		try {
			String password=EpooqUtil.getRandomPassword();
			String shaHex = DigestUtils.sha1Hex(password);
			user.setPassword(shaHex);
			User userDuplicate=userDao.getUserByEmail(user);
			if(userDuplicate==null){
				
				flag=userDao.saveUser(user);
				log.info("Save Registered user successfully");
					if(flag){
						message=EpooqConstant.USER_REG_SUCCESS;
						ReportMailService reportMailService=new ReportMailService();
						StringBuilder sb=new StringBuilder();
						sb.append("<div style='font-size:16px'> Hi "+user.getFirstName() + ",<br><br>");
						sb.append("Olet onnistuneesti rekisteröitynyt Epooq. <br><br>");
						sb.append("Seuraavat ovat kirjautumistiedot: <br><br>");
						sb.append("Käyttäjätunnus: "+user.getEmail()+"<br><br>");
						sb.append("Salasana: "+password+"<br><br>");
						sb.append("Kirjautuaksesi Epooq at http://www.epooq.net<br><br>");
						sb.append("Paras Toiveet,<br>");
						sb.append("Epooq Team<br>");
						sb.append("email:info@epooq.net</div>");
						reportMailService.executeMail(user.getEmail(), "Epooq Account", sb.toString());
						log.info("Mail sent to Registered user successfully");
					}else{
						message=EpooqConstant.USER_REG_FAIL;
						log.info("User registration failed");
					}
			}else{
				message=EpooqConstant.USER_EXIST;
				log.info("User already exist");
			}
		} catch (Exception e) {
			log.debug("User registration failed");
			message=EpooqConstant.USER_REG_FAIL;
			e.printStackTrace();
		}
		return message;
	}
	
	// get user who is logged in
	public User getLogin(User user){
		log.debug("Load logedin user");
		user.setPassword(DigestUtils.sha1Hex(user.getPassword()));
		User chekUser=userDao.getLogin(user);
		return chekUser;
	}
	// method for forgot password
	public void forgotPassword(User object){
		log.debug("User forgot Password");
		try {
			User user=userDao.getUserByEmail(object);
			if(user!=null){
				String password=EpooqUtil.getRandomPassword();
				String shaHex = DigestUtils.sha1Hex(password);
				user.setPassword(shaHex);
				boolean flag=userDao.updateUser(user);
				if(flag){
					ReportMailService reportMailService=new ReportMailService();
					
					StringBuilder sb=new StringBuilder();
					sb.append("<div> Hi "+user.getFirstName() + ",<br><br>");
					sb.append("Olet määrittänyt uuden salasanan kanssa Zarek. <br><br>");
					sb.append("Seuraavat ovat kirjautumistiedot: <br><br>");
					sb.append("Käyttäjätunnus: "+user.getEmail()+"<br><br>");
					sb.append("Salasana: "+password+"<br><br>");
					sb.append("Kirjautuaksesi Zarek at http://www.epooq.net<br><br>");
					sb.append("Paras Toiveet,<br>");
					sb.append("Epooq Team<br>");
					sb.append("email:zarek@qvantel.com</div>");
					
					reportMailService.executeMail(user.getEmail(), "Epooq Account", sb.toString());
					EpooqConstant.FORGOT_PASSWORD_STATUS = true;
					EpooqConstant.FORGOT_PASSWORD_MESSAGE=EpooqConstant.EMAIL_SEND_SUCC;
					log.info("Password recovery mail sent to user mail id");
				}else{
					EpooqConstant.FORGOT_PASSWORD_STATUS = false;
					EpooqConstant.FORGOT_PASSWORD_MESSAGE=EpooqConstant.FORGOT_PASSWORD_FAIL;
					log.error("Password recovery failed");
				}
			}else{
				EpooqConstant.FORGOT_PASSWORD_MESSAGE=EpooqConstant.EMAILID_NOT_EXIST;
				EpooqConstant.FORGOT_PASSWORD_STATUS = false;
				log.error("Email id not exist");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			EpooqConstant.FORGOT_PASSWORD_STATUS = false;
			EpooqConstant.FORGOT_PASSWORD_MESSAGE=EpooqConstant.FORGOT_PASSWORD_FAIL;
			log.error("Password recovery failed");
			e.printStackTrace();
		}
	}
	
	
}
