package com.tatva.utils;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
/**
 * 
 * @author pci94
 *	Utility Class for filtering the list
 */
public class DaoUtils {

	/**
	 * 
	 * @param propertyName -> Column Name
	 * @param relation	-> Expression
	 * @param value	-> Value
	 * @param crit	-> Criteria object 
	 */
	public static void createCriteria(String propertyName,
            							String relation,
            							Object value,
            							Criteria crit) {
		
		//check for the relation and add the criteria accordingly
	  if(StringUtils.equals(relation, SearchConstants.EQUALS))
	  {
		  crit.add(Restrictions.eq(propertyName, value));
	  }
	  else if(StringUtils.equals(relation, SearchConstants.CONTAINS))
	  {
		  crit.add(Restrictions.ilike(propertyName, "%"+value+"%"));
	  }
	  else if(StringUtils.equals(relation, SearchConstants.GT))
	  {
		  crit.add(Restrictions.gt(propertyName, value));
	  }
	  else if(StringUtils.equals(relation, SearchConstants.GT_EQ))
	  {
		  crit.add(Restrictions.ge(propertyName, value));
	  }
	  else if(StringUtils.equals(relation, SearchConstants.LT))
	  {
		  crit.add(Restrictions.lt(propertyName, value));
	  }
	  else if(StringUtils.equals(relation, SearchConstants.LT_EQ))
	  {
		  crit.add(Restrictions.le(propertyName, value));
	  }
	  else if(StringUtils.equals(relation, SearchConstants.NOT_EQUALS))
	  {
		  crit.add(Restrictions.ne(propertyName, value));
	  }
  
 }
	
	
}
