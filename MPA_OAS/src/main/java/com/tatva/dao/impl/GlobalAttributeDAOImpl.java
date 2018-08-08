package com.tatva.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatva.dao.IGlobalAttributeDAO;
import com.tatva.domain.GlobalAttribute;
import com.tatva.utils.DateUtil;

/**
 * {@link IGlobalAttributeDAO}
 * @author pci94
 *
 */

@Repository
public class GlobalAttributeDAOImpl implements IGlobalAttributeDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IGlobalAttributeDAO#checkAttributes()
	 */
	public List<GlobalAttribute> checkAttributes() {
		
		// retrieve list of Global Attributes For Which Apply Date is the today
		return sessionFactory.openSession().createQuery("from GlobalAttribute where applyDate='"+DateUtil.convertInDashedFormat(new Date())+"'").list();
	}

	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IGlobalAttributeDAO#save(com.tatva.domain.GlobalAttribute)
	 */
	public void save(GlobalAttribute globalAttributes) {
		//update the Global Attribute Value 
		sessionFactory.getCurrentSession().saveOrUpdate(globalAttributes);
	}

	@SuppressWarnings("unchecked")
	/*
	 * (non-Javadoc)
	 * @see com.tatva.dao.IGlobalAttributeDAO#list()
	 */
	public List<GlobalAttribute> list() {
		//retrieve list of Global Attributes
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(GlobalAttribute.class);
		List<GlobalAttribute> rows = criteria.list();
		return rows;
	}

}
