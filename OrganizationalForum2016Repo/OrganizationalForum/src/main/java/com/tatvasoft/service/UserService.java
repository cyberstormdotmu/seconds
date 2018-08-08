package com.tatvasoft.service;

import java.util.List;

import com.tatvasoft.entity.UserEntity;

/**
 *	@author TatvaSoft
 *	This interface provide UserEntity's Service methods Declaration.
 */
public interface UserService {

	/*
	 * This method add new user. 
	 */
	public void addUser(UserEntity user);

	/*
	 * This method returns List of all users. 
	 */
	public List<UserEntity> userList();

	/*
	 * This method is use for delete post by it's userId. 
	 */
	public void deleteUser(long userid);

	/*
	 * This method is use for find user by it's userId.
	 */
	public UserEntity getUserByUserId(long id);

	/*
	 * This method is use for update user. 
	 */
	public void updateUser(UserEntity user);

	/*
	 * This method is use for checking valid user. 
	 */
	public boolean isValidUser(String email, String password);

	/*
	 * This method is use for check whether user is active or not. 
	 */
	public UserEntity checkActiveUser(String email, String password);

	/*
	 * This method is use for find user is already register or not. 
	 */
	public boolean checkAvailableUser(String email);

	/*
	 * This method is use for send recovery password. 
	 */
	public boolean sendPasswordRecovery(String email);

	/*
	 * This method is use for checking logged in user is admin or not. 
	 */
	public boolean isAdmin(String emailID);

	/*
	 * This method is use for find user by it's emailId. 
	 */
	public UserEntity getUserByEmail(String emailID);

}
