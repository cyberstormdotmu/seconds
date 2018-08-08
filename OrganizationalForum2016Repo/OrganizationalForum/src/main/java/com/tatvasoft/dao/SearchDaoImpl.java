package com.tatvasoft.dao;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tatvasoft.entity.PostEntity;

@Repository
public class SearchDaoImpl implements SearchDao {

	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public List<PostEntity> searchPostByCriteria(String keyword , long category_id , String createDate , int sortlimit) {
		
		Session session = sessionFactory.getCurrentSession();
		
		Criteria cr = session.createCriteria(PostEntity.class);
		
		cr.add(Restrictions.like("question", "%"+keyword+"%").ignoreCase());
		
		if(category_id != 0){
			cr.add(Restrictions.like("category.categoryid", category_id));
		}
		
		if(createDate.equals("asce")){
			cr.addOrder(Order.asc("createDate"));
		} else {
			cr.addOrder(Order.desc("createDate"));
		}
		
		LocalDateTime today = LocalDateTime.now();
		System.out.println(today);
		
		Timestamp ts = Timestamp.valueOf(today);
		cr.add(Restrictions.lt("createDate", ts));
		
		switch (sortlimit) {
		case 1:
			LocalDateTime pastWeek = today.minus(1, ChronoUnit.WEEKS);
			Timestamp ts1 = Timestamp.valueOf(pastWeek);
			cr.add(Restrictions.gt("createDate", ts1));
			break;
		
		case 2:
			LocalDateTime pastMonth = today.minus(1, ChronoUnit.MONTHS);
			Timestamp ts2 = Timestamp.valueOf(pastMonth);
			cr.add(Restrictions.gt("createDate", ts2));
			break;
		
		case 3:
			LocalDateTime pastSixMonth = today.minus(6, ChronoUnit.MONTHS);
			Timestamp ts3 = Timestamp.valueOf(pastSixMonth);
			cr.add(Restrictions.gt("createDate", ts3));
			break;
		
		case 4:
			LocalDateTime pastYear = today.minus(1, ChronoUnit.YEARS);
			Timestamp ts4 = Timestamp.valueOf(pastYear);
			cr.add(Restrictions.gt("createDate", ts4));
			break;
			
		case 5:
			LocalDateTime pastDay = today.minus(1, ChronoUnit.DAYS);
			Timestamp ts5 = Timestamp.valueOf(pastDay);
			cr.add(Restrictions.gt("createDate", ts5));
			break;
		
		default:
			break;
		}
		
		@SuppressWarnings("unchecked")
		List<PostEntity> results = cr.list();
				
		System.out.println(results);
		return results;
	}
	
}
