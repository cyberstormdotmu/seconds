package com.kenure.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kenure.entity.NormalCustomer;
import com.kenure.entity.User;

@Repository
public class NormalCustomerDAOImpl implements INormalCustomerDAO {

	@Autowired
	private SessionFactory sessionFactory;

	private Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public String saveNormalCustomer(User user) {
		try{
			Session session = getCurrentSession();
			session.save(user);
			return "success";
		}catch(Exception e){
			return "error";
		}
	}

	@Override
	public String saveNormalCustomerByNormal(NormalCustomer nc) {
		try{
			Session session = getCurrentSession();
			session.save(nc);
			return "success";
		}catch(Exception e){
			return "error";
		}
		
	}
	
}
