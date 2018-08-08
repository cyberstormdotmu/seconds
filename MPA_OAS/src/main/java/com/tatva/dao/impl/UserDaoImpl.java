package com.tatva.dao.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatva.dao.IUserDao;
import com.tatva.domain.UserMaster;
import com.tatva.utils.DaoUtils;
import com.tatva.utils.MPAContext;
import com.tatva.utils.ReportMailService;

/**
 * {@link IUserDao}
 * @author pci94
 *
 */
@Repository
public class UserDaoImpl implements IUserDao
{
	@Autowired
    private SessionFactory sessionFactory;
	
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IUserDao#insertUser(com.tatva.domain.UserMaster)
	 */
	public void insertUser(UserMaster userMaster) 
	{
		/*
		 * save or update the User Details in database with time stamp 
		 */
		userMaster.setTimeStamp(new Date());
		sessionFactory.getCurrentSession().saveOrUpdate(userMaster);
	}


	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IUserDao#listUser()
	 */
	public List<UserMaster> listUser() 
	{
		//retrieve list of Users  
		return sessionFactory.getCurrentSession().createQuery("from UserMaster").list();
	}
	@SuppressWarnings("unused")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IUserDao#deleteUser(java.lang.String)
	 */
	public void deleteUser(String userId) {
		//delete User if it exists
		try {		
			Session session= sessionFactory.getCurrentSession();
			Query query = session.createQuery("delete from UserMaster where userId = :userId");
			query.setParameter("userId", userId);
			int result = query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IUserDao#getById(java.lang.String)
	 */
	public UserMaster getById(String userId) {
		//retrieve the User Details by userId
		UserMaster userMaster=null;
		try {
			Session session= sessionFactory.getCurrentSession();
			Query query = session.createQuery("from UserMaster where userId = :userId ");
			query.setParameter("userId", userId);
			List<UserMaster>  userMasterList= query.list();
			if(userMasterList.size()>0){
				userMaster=userMasterList.get(0);
				userMaster.getDateOfJoiningString();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userMaster;
	}
	
	public UserMaster getById(String userId , String userRole) {
		//retrieve the User Details by userId
		UserMaster userMaster=null;
		try {
			Session session= sessionFactory.getCurrentSession();
			Query query = session.createQuery("from UserMaster where userId = :userId AND role =:role");
			query.setParameter("userId", userId);
			query.setParameter("role", userRole);
			List<UserMaster>  userMasterList= query.list();
			if(userMasterList.size()>0){
				userMaster=userMasterList.get(0);
				userMaster.getDateOfJoiningString();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userMaster;
	}
	
	


	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IUserDao#userAuthentication(java.lang.String, java.lang.String)
	 */
	public UserMaster userAuthentication(String userId, String password) {
		// authenticate the user by userId and password return the user details if authenticate successfully
		UserMaster userMaster=null;
		try {
			Session session= sessionFactory.getCurrentSession();
			Query query = session.createQuery("from UserMaster where userId = :userId and password = :password");
			query.setParameter("userId", userId);
			query.setParameter("password", password);
			List<UserMaster>  userMasterList= query.list();
			if(userMasterList.size()!=0)
				userMaster=userMasterList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userMaster;
	}

	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IUserDao#listUsers(int, int)
	 */
	public List<UserMaster> listUsers(int offset, int rows)
	{
		/*
		 *return user list that starts with offset and list size will be same as rows
		 */
		try
		{
			Session session = sessionFactory.getCurrentSession();
			
			Criteria crit = session.createCriteria(UserMaster.class);
			crit.setFirstResult(offset);
			crit.setMaxResults(rows);
			return crit.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IUserDao#listOrderdUser(int, int, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<UserMaster> listOrderdUser(int offset, int rows, String property, String orderValue)
	{
		/*
		 * retrieve the list of users in order.
		 */
		try
		{
			Session session = sessionFactory.getCurrentSession();
			
			Criteria crit = session.createCriteria(UserMaster.class);
			crit.setFirstResult(offset);
			crit.setMaxResults(rows);
			
			/*
			 * if the property is not empty then it will check the order value for that property
			 * 	if order value is ascending then sorting will be done in descending order and vice versa
			 * 
			 *  if property is empty then sorting will be done in ascending order 
			 */
			if(StringUtils.isNotEmpty(property))
			{
				if(StringUtils.isEmpty(orderValue) || StringUtils.equals(orderValue, "ASC"))
				{
					crit.addOrder(Order.asc(property));
				}
				else if(StringUtils.equals(orderValue, "DSC"))
				{
					crit.addOrder(Order.desc(property));
				}
			}
			return crit.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@SuppressWarnings("unchecked")
	@Override
	
	public List<UserMaster> searchUser(UserMaster userMaster, String userIdRel,
			String firstNameRel, String middleNameRel, String lastNameRel,String joiningDateRel,
			String roleRel, String counterRel) {
		
		Session session = sessionFactory.getCurrentSession();
		  String userId = userMaster.getUserId();
		  String firstName = userMaster.getFirstName();
		  String middleName = userMaster.getMiddleName();
		  String lastName = userMaster.getLastName();
		  Date joiningDate = userMaster.getDateOfJoining();
		  String joiningDateString = userMaster.getDateOfJoiningString();
		  String role = userMaster.getRole();
		  String counter = userMaster.getCounter();
		  
		  Criteria criteria = session.createCriteria(UserMaster.class);
		  if(StringUtils.isNotEmpty(userId))
		  {
		   DaoUtils.createCriteria("userId", 
		         userIdRel,
		         userId, 
		         criteria);
		  }
		  if(StringUtils.isNotEmpty(firstName))
		  {
		   DaoUtils.createCriteria("firstName", 
		     firstNameRel, 
		     firstName, 
		     criteria);
		  }
		  if(StringUtils.isNotEmpty(middleName))
		  {
		   DaoUtils.createCriteria("middleName", 
		     middleNameRel, 
		     middleName, 
		     criteria);
		  }
		  if(StringUtils.isNotEmpty(lastName))
		  {
		   DaoUtils.createCriteria("lastName",
		     lastNameRel, 
		     lastName, 
		     criteria);
		  }
		 
		  if(StringUtils.isNotEmpty(joiningDateString))
		  {
			  
			 DaoUtils.createCriteria("dateOfJoining",
		     joiningDateRel,
		     joiningDate,
		     criteria);
		  }
		  
		  if(StringUtils.isNotEmpty(role))
		  {
		   DaoUtils.createCriteria("role", 
		     roleRel,
		     role,
		     criteria);
		  }
		  if(StringUtils.isNotEmpty(counter))
		  {
		   DaoUtils.createCriteria("counter", 
		     counterRel, 
		     counter, 
		     criteria);
		  }
		  
		
		  
		  return criteria.list();
		 
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<UserMaster> listSearchOrder(int offset, int rows,
			String property, String orderValue,
			UserMaster userMaster, String userIdRel,
			String firstNameRel, String middleNameRel, String lastNameRel,
			String joiningDateRel, String roleRel, String counterRel) {
		
		try{
			
			Session session = sessionFactory.openSession();
			
			/*
			 * get the property value by which user wants to search
			 */			
			
			String userId = userMaster.getUserId();
			String firstName = userMaster.getFirstName();
			String middleName = userMaster.getMiddleName();
			String lastName = userMaster.getLastName();
			String joiningDateString = userMaster.getDateOfJoiningString();
			Date joiningDate = userMaster.getDateOfJoining();
			String role = userMaster.getRole();
			String counter = userMaster.getCounter();
			
			/*
			 *  Criteria for search by property 
			 */
			Criteria criteria = session.createCriteria(UserMaster.class);
			criteria.setFirstResult(offset);
			criteria.setMaxResults(rows);
			
			
			  if(StringUtils.isNotEmpty(userId))
			  {
			   DaoUtils.createCriteria("userId", 
			         userIdRel,
			         userId, 
			         criteria);
			  }
			  if(StringUtils.isNotEmpty(firstName))
			  {
			   DaoUtils.createCriteria("firstName", 
			     firstNameRel, 
			     firstName, 
			     criteria);
			  }
			  if(StringUtils.isNotEmpty(middleName))
			  {
			   DaoUtils.createCriteria("middleName", 
			     middleNameRel, 
			     middleName, 
			     criteria);
			  }
			  if(StringUtils.isNotEmpty(lastName))
			  {
			   DaoUtils.createCriteria("lastName",
			     lastNameRel, 
			     lastName, 
			     criteria);
			  }
			 
			  if(StringUtils.isNotEmpty(joiningDateString))
			  {
				  
				 DaoUtils.createCriteria("dateOfJoining",
			     joiningDateRel,
			     joiningDate,
			     criteria);
			  }
			  
			  if(StringUtils.isNotEmpty(role))
			  {
			   DaoUtils.createCriteria("role", 
			     roleRel,
			     role,
			     criteria);
			  }
			  if(StringUtils.isNotEmpty(counter))
			  {
			   DaoUtils.createCriteria("counter", 
			     counterRel, 
			     counter, 
			     criteria);
			  }
			  
			  
				/*
				 * if the property is not empty then it will check the order value for that property
				 * 	if order value is ascending then sorting will be done in descending order and vice versa
				 * 
				 *  if property is empty then sorting will be done in ascending order 
				 */
			  
			  if(StringUtils.isNotEmpty(property))
				{
					if(StringUtils.isEmpty(orderValue) || StringUtils.equals(orderValue, "ASC"))
					{
						criteria.addOrder(Order.asc(property));
					}
					else if(StringUtils.equals(orderValue, "DSC"))
					{
						criteria.addOrder(Order.desc(property));
					}
				}
				return criteria.list();
		}
		catch(Exception e){
			e.printStackTrace();
			
		}
		return null;
	}


	@Override
	public void forgotPassword(String userId) throws Exception {

		UserMaster master=(UserMaster)sessionFactory.getCurrentSession().get(UserMaster.class, userId);
		if(master!=null){

			String tempPassword=String.valueOf(Math.random());
			String subject="Password Recovery";
			String message="Your new password is:"+tempPassword;
			master.setPassword(DigestUtils.shaHex(tempPassword));
			insertUser(master);
			ReportMailService mailService=new ReportMailService();
			mailService.executeMail(master.getEmailAddress(),subject, message);
			
		}
		
	}


	@Override
	public String changePassword(String oldPassword, String newPassword) {

			Session session=sessionFactory.getCurrentSession();
			Query query=session.createQuery("from UserMaster where userId = :userId ");
			query.setParameter("userId", MPAContext.currentUser);
			List<UserMaster> list=(List<UserMaster>)query.list();
			String message="";
			if(list!=null){
				UserMaster master=list.get(0);
				String password=master.getPassword();
				if(StringUtils.equalsIgnoreCase(password, DigestUtils.shaHex(oldPassword))){
					if(StringUtils.equalsIgnoreCase(oldPassword, newPassword)){
						message="You Cant Set same password again";
					}else{
						master.setPassword(DigestUtils.shaHex(newPassword));
						insertUser(master);
						message="Passsword Successfull Updated";
					}
					
				}else{
					message="Old Password is incorrect";
				}
			}
			return message;
		
	}
	@Override
	public String availableUser(String userid) {
		
		int count = 1;
		String  flagresult = null;
		Session session = sessionFactory.openSession();

		Criteria criteria = session.createCriteria(UserMaster.class);
		criteria.add(Restrictions.eq("userId", userid));
		
		criteria.setProjection(Projections.rowCount());
		count = Integer.parseInt(criteria.uniqueResult().toString());
		
		if(count == 0){
			
			flagresult = "available";
		}
		else{
			
			flagresult = "unavailable";
		}
		
		return flagresult;
	}
	
}
