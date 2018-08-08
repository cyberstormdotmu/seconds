package com.kenure.utils;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

public class HibernateObjectPersistenceUtil {

	public static <T> T unproxy(T proxied)
	{
	    T entity = proxied;
	    Hibernate.initialize(entity);
	    if (entity != null && entity instanceof HibernateProxy) {
	        Hibernate.initialize(entity);
	        entity = (T) ((HibernateProxy) entity)
	                  .getHibernateLazyInitializer()
	                  .getImplementation();
	    }
	    return entity;
	}
	
}
