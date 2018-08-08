package com.tatva.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tatva.dao.UserDao;
import com.tatva.model.Role;
import com.tatva.model.User;
import com.tatva.model.Vehicle;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Transactional
	public void addUser(User user) {
		userDao.addUser(user);
	}

	@Transactional
	public List<User> listUser() {
		return userDao.listUser();
	}

	@Transactional
	public void deleteUser(int userId) {
		userDao.deleteUser(userId);
	}

	@Transactional
	public void updateUser(User user) {
		userDao.updateUser(user);
	}

	@Transactional
	public User authenticateUser(String userName,String password) {
		return userDao.authenticateUser(userName, password);
	}

	@Transactional
	public List<Role> roles() {
		return userDao.roles();
	}

	@Transactional
	public User editUser(int id) {
		System.out.println("**********till Service***************");
		return userDao.editUser(id);
	}

	@Transactional
	public List<Vehicle> listVehicle(int userId) {
		return userDao.listVehicle(userId);
	}
	
	@Transactional
	public List<User> getUsers(int pageSize, int page, String sidx, String sord) {
		return userDao.getUsers(pageSize, page, sidx, sord);
	}

	@Transactional
	public int getNoOfRecords() {
		// TODO Auto-generated method stub
		return userDao.getNoOfRecords();
	}
	
}
