package com.tatva.dao;

import java.util.List;


import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatva.model.Role;
import com.tatva.model.User;
import com.tatva.model.Vehicle;

@Repository
public class UserDaoImpl implements UserDao {

	private static int noOfRecords;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private Session getSession(){
        Session session = null;
        if(session == null || !session.isOpen())
            session = sessionFactory.openSession();
        return session;
    }
	
	@Override
	 public int getNoOfRecords() {
	        return noOfRecords;
	    }
	
	 @SuppressWarnings("unchecked")
	@Override
	 public List<User> getUsers(int pageSize, int page, String sidx, String sord) {
       List<User> list = null;
       Query query = getSession().createQuery("from User order by "+sidx+" "+sord);
       noOfRecords = query.list().size();
       query.setFirstResult((page - 1) * pageSize);
       query.setMaxResults(pageSize);
       list = (List<User>)query.list();
       return list;
   }
	
	
	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().save(user);
	}

	@Override
	public void deleteUser(int userId) {
		// TODO Auto-generated method stub
		
		User user=(User)sessionFactory.getCurrentSession().get(User.class, userId);
		
		if(user!=null)
			sessionFactory.getCurrentSession().delete(user);

	}

	@Override
	public List<User> listUser() {
		return sessionFactory.getCurrentSession().createQuery("from User").list();
	}

	@Override
	public void updateUser(User user) {
		System.out.println("User parameter --"+user.getUserId());
		User u=(User)sessionFactory.getCurrentSession().get(User.class, user.getUserId());
		System.out.println("User parameter --"+u);
		u.setFirstName(user.getFirstName());
		u.setLastName(user.getLastName());
		u.setActive(user.getActive());
		u.setRoleId(user.getRoleId());
		u.setUserName(user.getUserName());
		
		sessionFactory.getCurrentSession().update(u);
	}

	@Override
	public User authenticateUser(String userName,String password) {
		List list=(List)sessionFactory.getCurrentSession().
				createQuery("from User where userName='"+userName+"' AND password='"+password+"'").
				list();
		if(list!=null && list.size()>0)
		{
			User user=(User)list.get(0);
			return user;
		}
		else
		{
			return null;
		}
		
	}

	@Override
	public List<Role> roles() {
		return sessionFactory.getCurrentSession().createQuery("from Role").list();
	}

	@Override
	public User editUser(int id) {
		User user=(User)sessionFactory.getCurrentSession().get(User.class, id);
		System.out.println("**********till DAo***************");
		return user;
	}

	@Override
	public List<Vehicle> listVehicle(int userId) {
		return sessionFactory.getCurrentSession().createQuery("From Vehicle where userId='"+userId+"'").list();
	}

}
