package com.tatva.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tatva.dao.IGlobalAttributeDAO;
import com.tatva.domain.GlobalAttribute;

/**
 * 
 * @author pci94
 *	Utility Class for setting the values of global attributes 
 */
public class GlobalScheduler{

	@Autowired
	private IGlobalAttributeDAO globalAttributesDAO;
	
	/**
	 * this method will set the values of global attribute if its apply date is the today
	 */
	public void setGlobalAttributes(){
		List<GlobalAttribute> listGlobalAttributes=globalAttributesDAO.checkAttributes();
		if(listGlobalAttributes!=null){
			for(int i=0;i<listGlobalAttributes.size();i++){
				
				String attributeName=listGlobalAttributes.get(i).getAttributeName();
				
				if(attributeName.equals("START_TIME")){
					MPAContext.startTime=Integer.parseInt(listGlobalAttributes.get(i).getNewValue());
				}else if(attributeName.equals("LAST_TIME")){
					MPAContext.endTime=Integer.parseInt(listGlobalAttributes.get(i).getNewValue());
					System.out.println("Changed End Time::::"+MPAContext.endTime);
				}else if(attributeName.equals("PARALLEL_APT")){
					MPAContext.parallelAppointment=Integer.parseInt(listGlobalAttributes.get(i).getNewValue());
				}
			}
		}
		
	}
}
