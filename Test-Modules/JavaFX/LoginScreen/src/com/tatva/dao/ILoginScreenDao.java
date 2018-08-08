package com.tatva.dao;

import java.util.List;

import com.tatva.model.UserModel;

/**
 * 
 * @author pci94
 * This interface will call to the database for CRUD operation
 */
public interface ILoginScreenDao {

	 /** 
	 * This method will authenticate the user.
	 * @param userName
	 * @param password
	 * @return true is user credentials are valid otherwise false.
	 */
	public boolean authenticate(String userName,String password);
	
	/**
	 * This method will return the list of users 
	 * @return list of users
	 */
	public List<UserModel> listUsers();
	
	/**
	 * This method will delete the User if exists
	 * @param id
	 */
	public void deleteUser(int id);
	
	/**
	 * return the User Details if it exists
	 * @param id
	 * @return UserModel by id
	 */
	public UserModel editUser(int id);
	
	/**
	 * This method will update the user record
	 * @param model
	 */
	public void updateUser(UserModel model);
	
	/**
	 * This method will insert the user.
	 * @param model
	 */
	public void insertUser(UserModel model);
}
