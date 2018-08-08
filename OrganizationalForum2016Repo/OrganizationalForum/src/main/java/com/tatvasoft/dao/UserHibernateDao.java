package com.tatvasoft.dao;

import java.util.List;

import com.tatvasoft.entity.UserEntity;

public interface UserHibernateDao {
	
	public void addUser(UserEntity user);

	public List<UserEntity> userList();
	
	//public void deleteUser(UserEntity user);

	public UserEntity getUserByUserId(long id);

	public void updateUser(UserEntity userUpdate);

	public UserEntity checkActiveUser(String email, String password);

	public boolean isValidUser(String email, String password);

	public boolean checkAvailableUser(String email);

	public boolean sendPasswordRecovery(String email);

	public void deleteUserById(long id);

	public boolean isAdmin(String emailID);

	public UserEntity getUserByEmail(String emailID);
}
