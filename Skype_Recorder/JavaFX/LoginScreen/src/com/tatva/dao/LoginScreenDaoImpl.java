package com.tatva.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.tatva.model.UserModel;

/**
 * {@link : ILoginScreenDao}
 * @author pci94
 *
 */
public class LoginScreenDaoImpl  implements ILoginScreenDao {
	
	/*
	 * return the SessionFactory object
	 */
	public SessionFactory getSessionFactory(){
		
		Configuration configuration=new Configuration();
		configuration.configure();
		
		ServiceRegistry sr=new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
		SessionFactory factory=configuration.buildSessionFactory(sr);
		
		return factory;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.ILoginScreenDao#authenticate(java.lang.String, java.lang.String)
	 */
	public boolean authenticate(String userName, String password) {
		
		SessionFactory factory=getSessionFactory();
		
		Session session=factory.openSession();
		session.beginTransaction();
		
		@SuppressWarnings("rawtypes")
		List list=session.createQuery("From UserModel where userName='"+userName+"' And password='"+password+"'").list();
		
		session.getTransaction().commit();
		session.close();
		
		if(list!=null && list.size()>0){
			return true;
		}else{
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.ILoginScreenDao#listUsers()
	 */
	public List<UserModel> listUsers() {
	
		SessionFactory factory=getSessionFactory();
		
		Session session=factory.openSession();
		session.beginTransaction();
		
		@SuppressWarnings("unchecked")
		List<UserModel> listUsers=session.createQuery("From UserModel").list();
		
		session.getTransaction().commit();
		session.close();
		
		return listUsers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.ILoginScreenDao#deleteUser(int)
	 */
	public void deleteUser(int id) {

		SessionFactory factory=getSessionFactory();
		
		Session session=factory.openSession();
		session.beginTransaction();
		
		UserModel model=(UserModel)session.get(UserModel.class, id);
		if(model!=null){
			session.delete(model);
			session.getTransaction().commit();
		}
		session.close();	
	}

	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.ILoginScreenDao#editUser(int)
	 */
	public UserModel editUser(int id) {

		SessionFactory factory=getSessionFactory();
		
		Session session=factory.openSession();
		session.beginTransaction();
		
		UserModel model=(UserModel)session.get(UserModel.class, id);
		session.getTransaction().commit();
		session.close();
		return model;
	}

	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.ILoginScreenDao#updateUser(com.tatva.model.UserModel)
	 */
	public void updateUser(UserModel model) {

		SessionFactory factory=getSessionFactory();
		
		Session session=factory.openSession();
		session.beginTransaction();
		
		UserModel userModel=(UserModel)session.get(UserModel.class, model.getId());
		
		userModel.setFirstName(model.getFirstName());
		userModel.setLastName(model.getLastName());
		
		session.update(userModel);
		
		session.getTransaction().commit();
		session.close();
	}

	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.ILoginScreenDao#insertUser(com.tatva.model.UserModel)
	 */
	public void insertUser(UserModel model) {

		SessionFactory factory=getSessionFactory();
		
		Session session=factory.openSession();
		session.beginTransaction();
		
		session.save(model);
		
		session.getTransaction().commit();
		session.close();
	}

}
