package com.tatva.service;

import java.util.List;

import com.tatva.model.Role;
import com.tatva.model.User;
import com.tatva.model.Vehicle;

public interface UserService {

	public void addUser(User user);
	public List<User> listUser();
	public void deleteUser(int userId);
	public void updateUser(User user);
	public User authenticateUser(String userName,String password);
	public List<Role> roles();
	public User editUser(int id);
	public List<Vehicle> listVehicle(int userId);
	public List<User> getUsers(int pageSize, int page, String sidx, String sord);
	public int getNoOfRecords();
}
