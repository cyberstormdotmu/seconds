package fi.korri.epooq.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.korri.epooq.dao.StoryDao;
import fi.korri.epooq.dao.UserDao;
import fi.korri.epooq.model.User;
import fi.korri.epooq.util.EpooqConstant;
import fi.korri.epooq.util.EpooqUtil;


@Service
public class UserService {

	

	@Autowired
	private UserDao userDao;
	
	public String saveUser(User user){
		boolean flag;
		String message="";
		try {
			String password=EpooqUtil.getRandomPassword();
			String shaHex = DigestUtils.sha1Hex(password);
			user.setPassword(shaHex);
			User userDuplicate=userDao.getUserByEmail(user);
			if(userDuplicate==null){
				
				flag=userDao.saveUser(user);
					if(flag){
						message=EpooqConstant.USER_REG_SUCCESS;
						ReportMailService reportMailService=new ReportMailService();
						StringBuilder sb=new StringBuilder();
						sb.append("<div style='font-size:16px'> Hi "+user.getFirstName() + ",<br><br>");
						sb.append("You have successfully registered with Epooq. <br><br>");
						sb.append("Following are the login details: <br><br>");
						sb.append("Username: "+user.getEmail()+"<br><br>");
						sb.append("Password: "+password+"<br><br>");
						sb.append("Login into Epooq at http://www.epooq.net<br><br>");
						sb.append("Best Wishes,<br>");
						sb.append("Epooq Team<br>");
						sb.append("email:info@epooq.net</div>");
						reportMailService.executeMail(user.getEmail(), "Epooq Account", sb.toString());
					}else{
						message=EpooqConstant.USER_REG_FAIL;
					}
			}else{
				message=EpooqConstant.USER_EXIST;
			}
		} catch (Exception e) {
			message=EpooqConstant.USER_REG_FAIL;
			e.printStackTrace();
		}
		return message;
	}
	
	
	public User getLogin(User user){
		user.setPassword(DigestUtils.sha1Hex(user.getPassword()));
		User chekUser=userDao.getLogin(user);
		return chekUser;
	}
	
	public void forgotPassword(User object){
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
					sb.append("You have successfully set new password with Zarek. <br><br>");
					sb.append("Following are the login details: <br><br>");
					sb.append("Username: "+user.getEmail()+"<br><br>");
					sb.append("Password: "+password+"<br><br>");
					sb.append("Login into Zarek at http://www.epooq.net<br><br>");
					sb.append("Best Wishes,<br>");
					sb.append("Epooq Team<br>");
					sb.append("email:zarek@qvantel.com</div>");
					
					
					
					reportMailService.executeMail(user.getEmail(), "Epooq Account", sb.toString());
					EpooqConstant.FORGOT_PASSWORD_STATUS = true;
					EpooqConstant.FORGOT_PASSWORD_MESSAGE=EpooqConstant.EMAIL_SEND_SUCC;
				}else{
					EpooqConstant.FORGOT_PASSWORD_STATUS = false;
					EpooqConstant.FORGOT_PASSWORD_MESSAGE=EpooqConstant.FORGOT_PASSWORD_FAIL;
				}
			}else{
				EpooqConstant.FORGOT_PASSWORD_MESSAGE=EpooqConstant.EMAILID_NOT_EXIST;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			EpooqConstant.FORGOT_PASSWORD_STATUS = false;
			EpooqConstant.FORGOT_PASSWORD_MESSAGE=EpooqConstant.FORGOT_PASSWORD_FAIL;
			e.printStackTrace();
		}
	}
	
	
}
