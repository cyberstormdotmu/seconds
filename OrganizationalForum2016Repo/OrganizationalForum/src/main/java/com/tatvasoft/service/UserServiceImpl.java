package com.tatvasoft.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tatvasoft.dao.UserDao;
import com.tatvasoft.entity.UserEntity;
import com.tatvasoft.util.PassEncry;

/**
 *	@author TatvaSoft
 *	This interface provide UserEntity's Service methods Implementation.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	/*
	 * All implemented methods of UserService Interface
	 */
	@Transactional
	public void addUser(UserEntity user) {

		String password = user.getPassword();
		String encryptedPassword = PassEncry.encrypass(password);
		user.setPassword(encryptedPassword);
		userDao.addUser(user);
	}

	@Transactional
	public List<UserEntity> userList() {
		return userDao.userList();
	}

	@Transactional
	public void deleteUser(long id) {
		userDao.deleteUserById(id);
	}

	public UserEntity getUserByUserId(long id) {
		return userDao.getUserByUserId(id);
	}

	@Transactional
	public void updateUser(UserEntity userUpdate) {
		userDao.updateUser(userUpdate);
	}

	public boolean isValidUser(String email, String password) {
		String encryptedPassword = PassEncry.encrypass(password);
		return userDao.isValidUser(email, encryptedPassword);
	}

	public UserEntity checkActiveUser(String email, String password) {
		String encryptedPassword = PassEncry.encrypass(password);
		UserEntity user = userDao.checkActiveUser(email, encryptedPassword);
		return user;
	}

	public boolean checkAvailableUser(String email) {
		boolean user = userDao.checkAvailableUser(email);
		return user;
	}

	public boolean sendPasswordRecovery(String email) {
		boolean result = userDao.sendPasswordRecovery(email);
		return result;
	}

	public boolean isAdmin(String emailID) {
		boolean result = userDao.isAdmin(emailID);
		return result;
	}

	public UserEntity getUserByEmail(String emailID) {
		return userDao.getUserByEmail(emailID);
	}

}
