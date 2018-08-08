package com.tatva.service;

import java.util.List;

import com.tatva.domain.UserMaster;

public interface IUser 
{
	public void insertUser(UserMaster user);
	
	 public List<UserMaster> searchUser (UserMaster userMaster,
	          String userIdRel,
	          String firstNameRel,
	          String middleNameRel,
	          String lastNameRel,
	          String joiningDateRel,
	          String roleRel,
	          String counterRel);
	
	public String availableUser(String userid) ;
	 
	public void updateUser(UserMaster user);
	
	public List<UserMaster> selectAllUsers();
	
	public void deleteUser(String userId);
	
	public UserMaster getById(String userId);
	
	public UserMaster getById(String userId , String userRole);
	
	public UserMaster getLogin(String userId,String password);
	
	public List<UserMaster> listUsers(int offset, int rows);
	
	public int getTotalUsers();
	
	public List<UserMaster> listOrderdUser(int offset, int rows, String property, String orderValue);
	
	public List<UserMaster> getListPage(List<UserMaster> list, int offset, int rows);
	
	public List<UserMaster> listSearchOrder(int offset, int rows, String property, String orderValue,UserMaster usermaster, String userIdRel , String firstNameRel , String middleNameRel, String lastNameRel , String joiningDateRel , String roleRel , String counterRel);
	
	public void forgotPassword(String userId)throws Exception;
	
	public String changePassword(String oldPassword,String newPassword);
}
