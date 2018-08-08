package com.tatva.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tatva.dao.IUserDao;
import com.tatva.domain.UserMaster;
import com.tatva.service.IUser;
import com.tatva.utils.MPAContext;

@Service
public class UserImpl implements IUser
{
	
	@Autowired
	private IUserDao userDao;

	public void insertUser(UserMaster user) 
	{
		if(user.getRole().equalsIgnoreCase("Administrator")){
			user.setCounter("0");
		}
		user.setPassword(DigestUtils.shaHex(user.getPassword()));
		userDao.insertUser(user);
	}

	public List<UserMaster> selectAllUsers() 
	{
		return userDao.listUser();
	}

	public void deleteUser(String userId) {
		userDao.deleteUser(userId);
	}
	
	public UserMaster getById(String userId) 
	{
		UserMaster master= userDao.getById(userId);
		if(master!=null){
			if(!master.getUserId().equalsIgnoreCase(MPAContext.currentUser)){
				if(master.getRole().equalsIgnoreCase(MPAContext.currentRole)){
					master= null;
				}
			}
			
		}	
		return master;
	}
	
	public UserMaster getById(String userId , String userRole) 
	{
		return userDao.getById(userId,userRole);
	}

	@Override
	public UserMaster getLogin(String userId, String password) {
		String password1=DigestUtils.shaHex(password);
		return userDao.userAuthentication(userId, password1);
	}

	@Override
	public void updateUser(UserMaster user) {
		
		if(user.getRole().equalsIgnoreCase("Administrator")){
			user.setRole("Administrator");
			user.setCounter("0");
			userDao.insertUser(user);
		}
		else if(user.getRole().equalsIgnoreCase("Counter")){
			if(Integer.parseInt(user.getCounter()) > 3){
				user.setCounter("3");
			}
			else if(Integer.parseInt(user.getCounter()) < 1){
				user.setCounter("1");
			}
			userDao.insertUser(user);
		}
	}

	@Override
	public List<UserMaster> listUsers(int offset, int rows)
	{
		return userDao.listUsers(offset, rows);
	}

	@Override
	public int getTotalUsers() 
	{
		return userDao.listUser().size();
	}

	@Override
	public List<UserMaster> listOrderdUser(int offset, int rows, String property, String orderValue)
	{
		return userDao.listOrderdUser(offset, rows, property, orderValue);
	}

	@Override
	public List<UserMaster> getListPage(List<UserMaster> list, int offset, int rows)
	{
		List<UserMaster> userList = new ArrayList<UserMaster>();
		
		for(int i = offset; i < offset + rows; i++)
		{
			userList.add(list.get(i));
			if(i == list.size() - 1)
			{
				break;
			}
		}
		return userList;
	}

	@Override
	public List<UserMaster> searchUser(UserMaster userMaster, String userIdRel,
			String firstNameRel, String middleNameRel, String lastNameRel,String joiningDateRel,
			String roleRel, String counterRel) {
		
		return userDao.searchUser(userMaster, userIdRel, firstNameRel, middleNameRel, lastNameRel,joiningDateRel, roleRel , counterRel);
	}

	@Override
	public List<UserMaster> listSearchOrder(int offset, int rows,
			String property, String orderValue, UserMaster usermaster,
			String userIdRel, String firstNameRel, String middleNameRel,
			String lastNameRel, String joiningDateRel, String roleRel,
			String counterRel) {
		
		return userDao.listSearchOrder(offset,rows,property,orderValue,usermaster,userIdRel,firstNameRel,middleNameRel,lastNameRel,joiningDateRel,roleRel,counterRel);
	}

	@Override
	public void forgotPassword(String userId) throws Exception {
			userDao.forgotPassword(userId);
	}

	@Override
	public String changePassword(String oldPassword, String newPassword) {
		String message=userDao.changePassword(oldPassword,newPassword);
		return message;
	}

	
	
	@Override
	public String availableUser(String userid) {
		
		String flagresult = userDao.availableUser(userid);
		
		return flagresult;
	}

}
