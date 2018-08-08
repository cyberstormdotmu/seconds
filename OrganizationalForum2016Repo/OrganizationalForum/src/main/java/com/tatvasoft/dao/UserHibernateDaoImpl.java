package com.tatvasoft.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.catalina.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatvasoft.entity.PostEntity;
import com.tatvasoft.entity.RoleEntity;
import com.tatvasoft.entity.UserEntity;
import com.tatvasoft.util.MailSender;
import com.tatvasoft.util.PassEncry;
import com.tatvasoft.util.RandomPasswordGenerator;

@Repository
public class UserHibernateDaoImpl implements UserHibernateDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public void addUser(UserEntity user) {
		
		Session session = sessionFactory.getCurrentSession();
		
		RoleEntity role = new RoleEntity();
		role.setRoleid(2);
		
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		user.setCreateDate(timestamp);
		
		System.out.println("Date And Time : " + timestamp);
		
		user.setRole(role);
		
		session.save(user);
	
	}

	public List<UserEntity> userList() {
		
		Session session = sessionFactory.getCurrentSession();
		
		Query queryResult = session.createQuery("from UserEntity");

		@SuppressWarnings("unchecked")
		List<UserEntity> allUsers = (List<UserEntity>) queryResult.list();
		
		return allUsers;
	}

	/*public void deleteUser(UserEntity user) {
		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();

		session.delete(user);
		transaction.commit();

	}*/

	
	 /*public void deleteUserById(Integer id) { 
		 Session session =
	 sessionFactory.openSession(); Transaction transaction =
	  session.beginTransaction();
	  
	  User user = new User(); user.setUserid(id);
	  
	  session.delete(user); transaction.commit();
	  
	 }*/
	 

	public void deleteUserById(long id) {
		
		Session session = sessionFactory.getCurrentSession();
		
		UserEntity userEntity = getUserByUserId(id);
		
		session.delete(userEntity);
		
	}

	public UserEntity getUserByUserId(long id) {

		Session session = sessionFactory.getCurrentSession();
		
		Query queryResult = session.createQuery("from UserEntity where userid = "
				+ id);

		@SuppressWarnings("unchecked")
		List<UserEntity> allUsers = (List<UserEntity>) queryResult.list();

		UserEntity userEntity = allUsers.get(0);
		
		return userEntity;

	}

	
	public void updateUser(UserEntity userUpdate) {
		
		Session session = sessionFactory.getCurrentSession();
		
		long userID = userUpdate.getUserid();
		String username = userUpdate.getUsername();
		String password = userUpdate.getPassword();
		//String email = userUpdate.getEmail();
		
		//update UserEntity set password=:password where email=:email 
		Date date = new Date();
		Timestamp timestamp = new Timestamp(date.getTime());
		
		Query updateUser = session.createQuery("update UserEntity set username=:username, last_modified=:lastmodified,password=:password where userid=:userID ");
		//updateUser.setParameter("email", email);
		updateUser.setParameter("username", username);
		updateUser.setParameter("password", PassEncry.encrypass(password));
		updateUser.setParameter("userID", userID);
		updateUser.setParameter("lastmodified", timestamp);
		
		System.out.println("Date And Time : " + timestamp);
		
		updateUser.executeUpdate();
		
	}

	public UserEntity checkActiveUser(String email, String password) {
		
		Session session = sessionFactory.getCurrentSession();

		Query query = session
				.createQuery("from UserEntity where email=:email AND password=:password");
		query.setParameter("email", email);
		query.setParameter("password", password);

		@SuppressWarnings("unchecked")
		List<UserEntity> list = (List<UserEntity>) query.list();
		UserEntity user2 = new UserEntity();
		for (int i = 0; i < list.size(); i++) {
			user2 = (UserEntity) list.get(i);
		}
		return user2;

	}

	
	public boolean isValidUser(String email, String password) {

		boolean result = false;

		try {
			
			Session session = sessionFactory.getCurrentSession();

			Query query = session.createQuery("from UserEntity as u where u.email=:email AND u.password=:password");
			query.setParameter("email", email);
			query.setParameter("password", password);

			@SuppressWarnings("unchecked")
			List<UserEntity> list = (List<UserEntity>) query.list();

			if (list.isEmpty()) {
				result = false;
			} else {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	public boolean checkAvailableUser(String email) {
		boolean result = false;

		try {
			Session session = sessionFactory.getCurrentSession();

			Query query = session.createQuery("from UserEntity as u where u.email=:email");
			query.setParameter("email", email);
			
			@SuppressWarnings("unchecked")
			List<UserEntity> list = (List<UserEntity>) query.list();

			if (list.isEmpty()) {
				result = false;
			} else {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	public boolean sendPasswordRecovery(String email) {
		boolean result = false;

		try {
			Session session = sessionFactory.getCurrentSession();
			
			Query query = session.createQuery("from UserEntity as u where u.email=:email");
			query.setParameter("email", email);
				
			@SuppressWarnings("unchecked")
			List<UserEntity> list = (List<UserEntity>) query.list();
			
			if (list.isEmpty()) {
				result = false;
			} else {
				
				String randomPassword = String.valueOf(RandomPasswordGenerator.generatePswd()) ;
				String pwdMsgBody = "Your recovery Password is :  " + randomPassword + "\n Use this password for login.";
				
				
				
				Query updateQuery = session.createQuery("update UserEntity set password=:password where email=:email");
				updateQuery.setParameter("email", email);
				updateQuery.setParameter("password", PassEncry.encrypass(randomPassword));
				
				
				
				updateQuery.executeUpdate();
								
				MailSender mailSender = new MailSender(email, "Recovery Password", pwdMsgBody);
				result = true;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	public boolean isAdmin(String emailID) {
		boolean result = false;
		long roleid = 1;
		
		try {
			Session session = sessionFactory.getCurrentSession();

			Query query = session.createQuery("from UserEntity where roleid=:roleid AND email=:email");
			query.setParameter("email", emailID);
			query.setParameter("roleid", roleid);

			@SuppressWarnings("unchecked")
			List<UserEntity> list = (List<UserEntity>) query.list();

			if (list.isEmpty()) {
				result = false;
			} else {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}

	public UserEntity getUserByEmail(String emailID) {
		Session session = sessionFactory.getCurrentSession();
		
		Query queryResult = session.createQuery("from UserEntity where email=:email");
		queryResult.setParameter("email",emailID);
		
		@SuppressWarnings("unchecked")
		List<UserEntity> allUsers = (List<UserEntity>) queryResult.list();

		UserEntity userEntity = allUsers.get(0);
		
		return userEntity;
	}

}
