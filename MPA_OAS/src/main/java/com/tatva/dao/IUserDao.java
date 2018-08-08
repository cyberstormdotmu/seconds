package com.tatva.dao;

import java.util.List;

import com.tatva.domain.UserMaster;

/**
 * 
 * @author pci94
 *
 */
public interface IUserDao 
{

	
	 public List<UserMaster> searchUser (UserMaster userMaster,
	          String userIdRel,
	          String firstNameRel,
	          String middleNameRel,
	          String lastNameRel,
	          String joiningDateRel,
	          String roleRel,
	          String counterRel);
	
	
	
	
	/**
	 * 
	 * @param userMaster
	 * insert the user details
	 */
	public void insertUser(UserMaster userMaster);
	
	
	
	
	public String availableUser(String userid);
	
	
	/**
	 * 
	 * @return list of users 
	 */
	public List<UserMaster> listUser();
	
	/**
	 * 
	 * @param userId
	 * delete the user based on userId
	 */
	public void deleteUser(String userId);
	
	/**
	 * 
	 * @param userId
	 * @return User by userId.
	 */
	public UserMaster getById(String userId );
	public UserMaster getById(String userId , String userRole);

	/**
	 * 
	 * @param userId
	 * @param password
	 * @return User if it authenticate successfully otherwise return null 
	 */
	public UserMaster userAuthentication(String userId,String password);
	
	/**
	 * 
	 * @param offset
	 * @param rows
	 * @return list of users that start with offset and list size will be the value of rows for Grid 
	 */
	public List<UserMaster> listUsers(int offset, int rows);
	
	/**
	 * 
	 * @param offset
	 * @param rows
	 * @param property
	 * @param orderValue
	 * @return list of users with sorting by particular property(column)
	 */
	public List<UserMaster> listOrderdUser(int offset, int rows, String property, String orderValue);
	 
	public List<UserMaster> listSearchOrder(int offset, int rows, String property, String orderValue,UserMaster userMaster,String userIdRel,String firstNameRel , String middleNameRel, String lastNameRel , String joiningDateRel , String roleRel , String counterRel);



	
	public void forgotPassword(String userId) throws Exception;
	
	public String changePassword(String oldPassword,String newPassword);
}
