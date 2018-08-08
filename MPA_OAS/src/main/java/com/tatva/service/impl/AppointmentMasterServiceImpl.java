package com.tatva.service.impl;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tatva.controller.AppointmentController;
import com.tatva.dao.IAppointmentMasterDAO;
import com.tatva.domain.AppointmentMaster;
import com.tatva.domain.GlobalAttribute;
import com.tatva.model.AppointmentForm;
import com.tatva.model.ServiceTimeModel;
import com.tatva.model.WaitingTimeModel;
import com.tatva.service.IAppointmentMasterService;
import com.tatva.utils.DateUtil;
import com.tatva.utils.MPAContext;


@Service
public class AppointmentMasterServiceImpl implements IAppointmentMasterService {

	@Autowired
	private IAppointmentMasterDAO appointmentMasterDao;

	
	private AppointmentMaster appointmentMaster = new AppointmentMaster();



/*
 * Validating the register form values
 * @see com.tatva.service.IAppointmentMasterService#registerValidate(com.tatva.model.AppointmentForm)
 */
	@Override
	public Boolean registerValidate(AppointmentForm appointmentForm) 
	{
		Integer counter = 0;
		String referenceGenerator = null;
		if(appointmentForm.getTransactionType() != null)
		{
			for(Integer i= 0 ; i <appointmentForm.getTransactionType().size() ; i ++ )
			{
				if(appointmentForm.getTransactionType().get(i).equals("HCL"))		//cheking if selected transaction is in this main-type
				{
					if(appointmentForm.getHarbourCraftCheckBox()!= null)
					{
						for(Integer i1 = 0; i1 <appointmentForm.getHarbourCraftCheckBox().size() ; i1++)
						{
							if(appointmentForm.getHarbourCraftCheckBox().get(i1).equals("HCLNL")) // checking if the selected transaction is for this sub-type
							{
								if(Integer.parseInt(appointmentForm.getHCLNLSelect()) != 0)
								{   
									counter += 	Integer.parseInt(appointmentForm.getHCLNLSelect());  
									if(counter > 1)		//check the total appointment 
									{
										if(appointmentForm.getAppointmentType().equals("single"))		//Validation for approprate type selection
										{
											return false;
										}
									}
									if(counter > 3)		// Validate total appointment
									{
										return false;
									}
									if(counter == 1)	
									{
										if(appointmentForm.getAppointmentType().equalsIgnoreCase("group")){
											
										return false;
										}
										else{
										referenceGenerator = appointmentForm.getHarbourCraftCheckBox().get(i1);		//Generate reference number according to perticular selection
										}
										}

									else
									{
										
										
										referenceGenerator = "MULTI" ;
									}
								}
								else
								{
									return false;
								}
							}
							else if(appointmentForm.getHarbourCraftCheckBox().get(i1).equals("HCLAD"))		// checking if the selected transaction is for this sub-type	
							{
								if(Integer.parseInt(appointmentForm.getHCLADSelect()) != 0)
								{   
									counter += 	Integer.parseInt(appointmentForm.getHCLADSelect());  
									if(counter > 1)
									{  
										if(appointmentForm.getAppointmentType().equals("single"))		//Validation for approprate type selection
										{  	
											return false;
										}
									}
									if(counter > 3)		// Validate total appointment
									{
										return false;
									}
									if(counter == 1)		
									{
										referenceGenerator = appointmentForm.getHarbourCraftCheckBox().get(i1); 		//Generate reference number according to perticular selection
									}
									else
									{	
										referenceGenerator = "MULTI" ;
									}
								}
								else
								{	
									return false;
								}
							}
							else if(appointmentForm.getHarbourCraftCheckBox().get(i1).equals("HCLUC"))		// checking if the selected transaction is for this sub-type
							{
								if(Integer.parseInt(appointmentForm.getHCLUCSelect()) != 0)
								{   
									counter += 	Integer.parseInt(appointmentForm.getHCLUCSelect());  
									if(counter > 1)
									{  
										if(appointmentForm.getAppointmentType().equals("single"))		//Validation for approprate type selection
										{  	
											return false;
										}
									}
									if(counter > 3)			// Validate total appointment
									{
										return false;
									}
									if(counter == 1)
									{
										referenceGenerator = appointmentForm.getHarbourCraftCheckBox().get(i1);		//Generate reference number according to perticular selection
									}
									else
									{	
										referenceGenerator = "MULTI" ;
									}
								}
								else
								{	
									return false;
								}
							}
							else if(appointmentForm.getHarbourCraftCheckBox().get(i1).equals("HCLCO"))		// checking if the selected transaction is for this sub-type
							{
								if(Integer.parseInt(appointmentForm.getHCLCOSelect()) != 0)
								{   
									counter += 	Integer.parseInt(appointmentForm.getHCLCOSelect());  
									if(counter > 1)
									{  
										if(appointmentForm.getAppointmentType().equals("single"))		//Validation for approprate type selection
										{  	
											return false;
										}
									}
									if(counter > 3)		// Validate total appointment
									{
										return false;
									}
									if(counter == 1)
									{
										referenceGenerator = appointmentForm.getHarbourCraftCheckBox().get(i1);		//Generate reference number according to perticular selection
									}
									else
									{	
										referenceGenerator = "MULTI" ;
									}
								}
								else
								{	
									return false;
								}
							}
							else if(appointmentForm.getHarbourCraftCheckBox().get(i1).equals("HCLNM"))		// checking if the selected transaction is for this sub-type
							{
								if(Integer.parseInt(appointmentForm.getHCLNMSelect()) != 0)
								{   
									counter += 	Integer.parseInt(appointmentForm.getHCLNMSelect());  
									if(counter > 1)
									{  
										if(appointmentForm.getAppointmentType().equals("single"))		//Validation for approprate type selection
										{  	
											return false;
										}
									}
									if(counter > 3)		// Validate total appointment
									{
										return false;
									}
									if(counter == 1)		//Generate reference number according to perticular selection
									{
										referenceGenerator = appointmentForm.getHarbourCraftCheckBox().get(i1);		//Generate reference number according to perticular selection
									}
									else
									{	
										referenceGenerator = "MULTI" ;
									}
								}
								else
								{	
									return false;
								}
							}
							else if(appointmentForm.getHarbourCraftCheckBox().get(i1).equals("HCLRH"))		// checking if the selected transaction is for this sub-type
							{
								if(Integer.parseInt(appointmentForm.getHCLRHSelect()) != 0)
								{   
									counter += 	Integer.parseInt(appointmentForm.getHCLRHSelect());
									if(counter > 1)
									{  
										if(appointmentForm.getAppointmentType().equals("single"))		//Validation for approprate type selection
										{
											return false;
										}
									}
									if(counter > 3)		// Validate total appointment
									{
										return false;
									}
									if(counter == 1)		//Generate reference number according to perticular selection
									{
										referenceGenerator = appointmentForm.getHarbourCraftCheckBox().get(i1);  
									}
									else
									{	
										referenceGenerator = "MULTI" ;
									}
								}
								else
								{	
									return false;
								}	
							}	
						}	
					}
					else
					{
						return false;
					}
				}
				else if(appointmentForm.getTransactionType().get(i).equals("PCL") )		//cheking if selected transaction is in this main-type
				{
					if(appointmentForm.getPleasureCraftCheckBox() != null)
					{
						for(Integer i2 = 0; i2 <appointmentForm.getPleasureCraftCheckBox().size() ; i2++)
						{
							if(appointmentForm.getPleasureCraftCheckBox().get(i2).equals("PCLNL"))		// checking if the selected transaction is for this sub-type
							{   
								if(Integer.parseInt(appointmentForm.getPCLNLSelect()) != 0)
								{   
									counter += 	Integer.parseInt(appointmentForm.getPCLNLSelect());  
									if(counter > 1)
									{  
										if(appointmentForm.getAppointmentType().equals("single"))		//Validation for approprate type selection
										{ 	
											return false;
										}
									}
									if(counter > 3)		// Validate total appointment
									{
										return false;
									}
									if(counter == 1)
									{
										referenceGenerator = appointmentForm.getPleasureCraftCheckBox().get(i2);		//Generate reference number according to perticular selection
									}
									else
									{
										referenceGenerator = "MULTI" ;
									}
								}
								else
								{	
									return false;
								}
							}
							else if(appointmentForm.getPleasureCraftCheckBox().get(i2).equals("PCLAD"))		// checking if the selected transaction is for this sub-type
							{
								if(Integer.parseInt(appointmentForm.getPCLADSelect()) != 0)		
								{
									counter += 	Integer.parseInt(appointmentForm.getPCLADSelect()); 
									if(counter > 1)
									{  
										if(appointmentForm.getAppointmentType().equals("single"))		//Validation for approprate type selection
										{  	
											return false;
										}
									}
									if(counter > 3)		// Validate total appointment
									{
										return false;
									}
									if(counter == 1)		//Generate reference number according to perticular selection
									{
										referenceGenerator = appointmentForm.getPleasureCraftCheckBox().get(i2);  //Generate reference number according to perticular selection
									}
									else
									{	
										referenceGenerator = "MULTI" ;
									}
								}
								else
								{	
									return false;
								}
							}
							else if(appointmentForm.getPleasureCraftCheckBox().get(i2).equals("PCLUC"))		// checking if the selected transaction is for this sub-type
							{
								if(Integer.parseInt(appointmentForm.getPCLUCSelect()) != 0)
								{  
									counter += 	Integer.parseInt(appointmentForm.getPCLUCSelect());  
									if(counter > 1)
									{  
										if(appointmentForm.getAppointmentType().equals("single"))		//Validation for approprate type selection
										{  	
											return false;
										}
									}
									if(counter > 3)		// Validate total appointment
									{
										return false;
									}
									if(counter == 1)
									{
										referenceGenerator = appointmentForm.getPleasureCraftCheckBox().get(i2);  //Generate reference number according to perticular selection
									}
									else
									{	
										referenceGenerator = "MULTI" ;
									}
								}
								else
								{	
									return false;
								}
							}
							else if(appointmentForm.getPleasureCraftCheckBox().get(i2).equals("PCLNP"))		// checking if the selected transaction is for this sub-type
							{
								if(Integer.parseInt(appointmentForm.getPCLNPSelect()) != 0)
								{   
									counter += 	Integer.parseInt(appointmentForm.getPCLNPSelect());  
									if(counter > 1)
									{  
										if(appointmentForm.getAppointmentType().equals("single"))		//Validation for approprate type selection
										{  	
											return false;
										}
									}
									if(counter > 3)		// Validate total appointment
									{
										return false;
									}
									if(counter == 1)
									{
										referenceGenerator = appointmentForm.getPleasureCraftCheckBox().get(i2);  //Generate reference number according to perticular selection
									}
									else
									{	
										referenceGenerator = "MULTI" ;
									}
								}
								else
								{	
									return false;
								}	
							}	
						}	
					}
					else
					{	
						return false;
					}
				}
				else if(appointmentForm.getTransactionType().get(i).equals("PC"))		//cheking if selected transaction is in this main-type
				{
					if(appointmentForm.getPortClearanceCheckBox() != null)
					{
						for(Integer i3 = 0; i3 <appointmentForm.getPortClearanceCheckBox().size() ; i3++)
						{
							if(appointmentForm.getPortClearanceCheckBox().get(i3).equals("PCGD"))		// checking if the selected transaction is for this sub-type
							{
								if(Integer.parseInt(appointmentForm.getPCGDSelect()) != 0)
								{   
									counter += 	Integer.parseInt(appointmentForm.getPCGDSelect());  
									if(counter > 1)
									{  
										if(appointmentForm.getAppointmentType().equals("single"))		//Validation for approprate type selection
										{  	
											return false;
										}
									}
									if(counter > 3)		// Validate total appointment
									{
										return false;
									}
									if(counter == 1)
									{
										referenceGenerator = appointmentForm.getPortClearanceCheckBox().get(i3);		//Generate reference number according to perticular selection
									}
									else
									{
										referenceGenerator = "MULTI" ;
									}
								}
								else
								{
									return false;
								}
							}
							else if(appointmentForm.getPortClearanceCheckBox().get(i3).equals("PCAL"))		// checking if the selected transaction is for this sub-type
							{
								if(Integer.parseInt(appointmentForm.getPCALSelect()) != 0)
								{   
									counter += 	Integer.parseInt(appointmentForm.getPCALSelect());
									if(counter > 1)
									{  
										if(appointmentForm.getAppointmentType().equals("single"))		//Validation for approprate type selection
										{  	
											return false;
										}
									}
									if(counter > 3)		// Validate total appointment
									{
										return false;
									}
									if(counter == 1)
									{
										referenceGenerator = appointmentForm.getPortClearanceCheckBox().get(i3);		//Generate reference number according to perticular selection
									}
									else
									{	
										referenceGenerator = "MULTI" ;
									}
								}
								else
								{
									return false;
								}
							}
							else if(appointmentForm.getPortClearanceCheckBox().get(i3).equals("PCAB"))		// checking if the selected transaction is for this sub-type
							{
								if(Integer.parseInt(appointmentForm.getPCABSelect()) != 0)
								{   
									counter += 	Integer.parseInt(appointmentForm.getPCABSelect()); 
									if(counter > 1)
									{
										if(appointmentForm.getAppointmentType().equals("single"))		//Validation for approprate type selection
										{  	
											return false;
										}
									}
									if(counter > 3)		// Validate total appointment
									{
										return false;
									}
									if(counter == 1)
									{
										referenceGenerator = appointmentForm.getPortClearanceCheckBox().get(i3);  //Generate reference number according to perticular selection
									}
									else
									{
										referenceGenerator = "MULTI" ;
									}
								}
								else
								{
									return false;
								}		
							}						
						}	
					}			
					else
					{
						return false;
					}
				}
				else if(appointmentForm.getTransactionType().get(i).equals("OTHS") )		//cheking if selected transaction is in this main-type
				{
					if(appointmentForm.getOthersCheckBox() != null)
					{
						for(Integer i4 = 0; i4 <appointmentForm.getOthersCheckBox().size() ; i4++)
						{
							if(appointmentForm.getOthersCheckBox().get(i4).equals("OTHS"))		// checking if the selected transaction is for this sub-type
							{
								if(Integer.parseInt(appointmentForm.getOTHSSelect()) != 0)
								{
									counter += 	Integer.parseInt(appointmentForm.getOTHSSelect());
									if(counter > 1)
									{
										if(appointmentForm.getAppointmentType().equals("single"))		//Validation for approprate type selection
										{  	
											return false;
										}
									}
									if(counter > 3)		// Validate total appointment
									{
										return false;
									}
									if(counter == 1)
									{
										referenceGenerator = appointmentForm.getOthersCheckBox().get(i4);		//Generate reference number according to perticular selection
									}
									else
									{
										referenceGenerator = "MULTI" ;
									}
								}
								else
								{
									return false;
								}
							}
						}
					}
					else
					{	
						return false;
					}	
				}	
			}
		}
		else
		{
			return false;
		}
		Calendar calendar = Calendar.getInstance();
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		//Display the date now:
		Date now = calendar.getTime();
		DateFormat fmt = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.UK);

		String formattedToday = fmt.format(now);
		String formattedDate = fmt.format(now);
		String formattedDateTomorrow = fmt.format(now);

		//Dispaly date for after 1 month
		calendar1.add(calendar1.MONTH, 1);
		Date dateLast = calendar1.getTime();
		formattedDate = fmt.format(dateLast);
		
		//Dispaly date for tomorrow
		calendar2.add(calendar2.DAY_OF_MONTH , 1);
		Date dateTomorrow = calendar2.getTime();
		formattedDateTomorrow = fmt.format(dateTomorrow);
		
		try
		{
			SimpleDateFormat dateformatter = new SimpleDateFormat("dd/MM/yyyy");
			Date dateConverted = dateformatter.parse(appointmentForm.getDate());
			SimpleDateFormat timeformatter = new SimpleDateFormat("hh:mm");
			Date formatedTime = timeformatter.parse(appointmentForm.getTime());
			if((dateConverted.compareTo(dateLast)) < 0)
			{
				//if(timeformatter.format(formatedTime).compareTo() <0){}
				String fixTime = "05:00:00 pm";
				DateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
				Date fixformatedtime = (Date)formatter.parse(fixTime);
				if(dateConverted.compareTo(now) > 0)
				{
					if((formatedTime.compareTo(fixformatedtime)) > 0)
					{
						if(dateConverted.compareTo(dateTomorrow) <= 0)
						{  // this is correct final
							return false;
						}				 
					}
					referenceGenerator += calendar.get(Calendar.YEAR);
					referenceGenerator += (calendar.get(Calendar.MONTH)+1);
					referenceGenerator += calendar.get(Calendar.DATE);
					referenceGenerator += calendar.get(Calendar.HOUR_OF_DAY);
					referenceGenerator += calendar.get(Calendar.MINUTE);
					referenceGenerator += calendar.get(Calendar.SECOND);
					AppointmentController.refNo = referenceGenerator;
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		catch(Exception ex)
		{
			return false;
		}
	}

	@Override
	/*
	 * service method for adding details in appoint_master table
	 * @see com.tatva.service.IAppointmentMasterService#registerAppointment(com.tatva.model.AppointmentForm, java.lang.String)
	 */
	public void registerAppointment(AppointmentForm appointmentForm, String referenceGenerator) 
	{
		appointmentMaster.setReferenceNo(referenceGenerator);   // setting reference number
		appointmentMaster.setIdNo(appointmentForm.getNricPpassportNumber()); // setting passport number field;
		appointmentMaster.setIdType("NRIC"); // Keep Id Type as a fix value.
		appointmentMaster.setAppointmentType(appointmentForm.getAppointmentType()); // appointment type
		String modelDate = appointmentForm.getDate();
		String modelTime = appointmentForm.getTime();
		DateFormat formatter0 = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat formatter1 = new SimpleDateFormat("hh:mm");

		try{
			Date date0 = formatter0.parse(modelDate); 
			Date date1 = (Date)formatter1.parse(modelTime);
			long ms = formatter1.parse(modelTime).getTime();
			Time convertedTime = new Time(ms);
			appointmentMaster.setAppointmentDate(date0);    //set the appointment date
			appointmentMaster.setAppointmentTime(convertedTime);  // set the appointment time
		}
		catch(Exception we)
		{
		}
		appointmentMaster.setName(appointmentForm.getName()); // set the client name
		appointmentMaster.setCompany(appointmentForm.getCompany()); // set the company name
		appointmentMaster.setRemark(appointmentForm.getRemark()); // set the remark
		appointmentMaster.setContactNo(appointmentForm.getContactNumber()); // set the contact number
		appointmentMaster.setEmail(appointmentForm.getEmailAddress()); // set the email address
		appointmentMasterDao.registerAppointment(appointmentMaster);
	}
	
	@Override
	public List<AppointmentForm> listAppointments(String passport) {
		List<AppointmentMaster> appointmentMasterList = appointmentMasterDao.listAppointmentsByPassport(passport);
		List<AppointmentForm> appointmentFormList = new ArrayList<>();
		AppointmentForm appointmentForm = null;
		if(appointmentMasterList != null){
			for(int i=0;i<appointmentMasterList.size();i++){
				appointmentForm = new AppointmentForm();
				appointmentForm.setReferenceNo(appointmentMasterList.get(i).getReferenceNo());
				Date appointmentDate = appointmentMasterList.get(i).getAppointmentDate();
				Time apointmentTime = appointmentMasterList.get(i).getAppointmentTime();
				String date= DateUtil.convertDateToString(appointmentDate);
				appointmentForm.setDate(date);
				appointmentForm.setTime(apointmentTime.toString());
				appointmentForm.setAppointmentType(appointmentMasterList.get(i).getAppointmentType());
				appointmentFormList.add(appointmentForm);
			}
		}
		return appointmentFormList;
	}

	@Override
	public void cancelAppointment(String refNo) {
		appointmentMasterDao.cancelAppointment(refNo);
	}

	@Override
	public AppointmentForm rescheduleAppointment(String refNo) {
		
		AppointmentMaster appointmentMaster= appointmentMasterDao.rescheduleAppointment(refNo);
		AppointmentForm appointmentForm=new AppointmentForm();
		appointmentForm.setReferenceNo(appointmentMaster.getReferenceNo());
		String date= DateUtil.convertDateToString(appointmentMaster.getAppointmentDate());
		appointmentForm.setDate(date);
		String time=DateUtil.formatTime(appointmentMaster.getAppointmentTime().toString());
		appointmentForm.setTime(time);
		return appointmentForm;
	}

	@Override
	public void rescheduleAppointment(AppointmentForm appointmentForm) {
		
		String date=appointmentForm.getDate();
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		Time timeValue=null;
		try {
			timeValue = new Time(formatter.parse(appointmentForm.getTime()).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date d=DateUtil.convertDateFromStringtoDate(date);
		AppointmentMaster appointmentMaster=new AppointmentMaster();
		appointmentMaster.setReferenceNo(appointmentForm.getReferenceNo());
		appointmentMaster.setAppointmentDate(d);
		appointmentMaster.setAppointmentTime(timeValue);
		appointmentMasterDao.rescheduleAppointment(appointmentMaster);
	}
	
	@Override
	public List<AppointmentMaster> selectAllAppointments() {
		return appointmentMasterDao.listAppointment();
	}
	
	
	@Override
	public List<AppointmentMaster> selectAllAppointments(Date date) {
		return appointmentMasterDao.listAppointment(date);
	}
	
	
	
	public AppointmentMaster getById(String refNo) 
	{
		return appointmentMasterDao.getAppointmentMasterByRefNo(refNo);
	}

	@Override
	public Set<String> availableTime(String date, String appointmentType) {
		
		List<AppointmentMaster> list=appointmentMasterDao.listAppointmentsByDate(date);
		List<GlobalAttribute> listAttributes=appointmentMasterDao.checkAttributes();
		
		
			for(int i=0;i<listAttributes.size();i++){
				if(listAttributes.get(i).getAttributeName().equalsIgnoreCase("END_TIME")){
					if(listAttributes.get(i).getApplyDate().compareTo(DateUtil.convertDateFromStringtoDate(date))<=0){
						MPAContext.endTime=Integer.parseInt(listAttributes.get(i).getNewValue());
					}else{
						MPAContext.endTime=Integer.parseInt(listAttributes.get(i).getOldValue());
					}
				}else if(listAttributes.get(i).getAttributeName().equalsIgnoreCase("START_TIME")){
					if(listAttributes.get(i).getApplyDate().compareTo(DateUtil.convertDateFromStringtoDate(date))<=0){
						MPAContext.startTime=Integer.parseInt(listAttributes.get(i).getNewValue());
					}else{
						MPAContext.startTime=Integer.parseInt(listAttributes.get(i).getOldValue());
					}
				}else if(listAttributes.get(i).getAttributeName().equalsIgnoreCase("PARALLEL_APT")){
					if(listAttributes.get(i).getApplyDate().compareTo(DateUtil.convertDateFromStringtoDate(date))<=0){
						MPAContext.parallelAppointment=Integer.parseInt(listAttributes.get(i).getNewValue());
					}else{
						MPAContext.parallelAppointment=Integer.parseInt(listAttributes.get(i).getOldValue());
					}
				}
			}
			
		List<String> reqList=new ArrayList<>();
		List<String> totalTimeList=new ArrayList<>();
		List<String> bookTimeList=new ArrayList<>();
		Set<String> availableTimeList;
		Set<String> set;
		
		for(int i=0;i<list.size();i++){
			if(list.get(i).getAppointmentType().equalsIgnoreCase("single")){
				reqList.add(list.get(i).getAppointmentTime().toString());
			}else{
				Time temp=list.get(i).getAppointmentTime();
				Time temp1= new Time(temp.getTime());
				
				reqList.add(temp1.toString());
				
				int min=temp1.getMinutes();
				if(min==45){
					temp1.setMinutes(temp1.getMinutes()-45);
					temp1.setHours(temp1.getHours()+1);
				}else{
					temp1.setMinutes(temp1.getMinutes()+15);
				}
				reqList.add(temp1.toString());
			}
		}
		
		for(int i=0;i<MPAContext.parallelAppointment;i++){
			
			for(int j=MPAContext.startTime;j<MPAContext.endTime;j++){
				
				for(int k=0;k<4;k++){
					String tempTime=null;
					if(j<10){
						tempTime="0"+j+":";
					}else{
						tempTime=j+":";
					}
					
					if(k==0){
						tempTime+="00:00";
					}else{
						tempTime+=k*15+":00";
					}
					totalTimeList.add(tempTime);
				}
			}
		}
		
		boolean flag = false;
		for(Iterator<String> itr = totalTimeList.iterator();itr.hasNext();) {  
            String element = itr.next();  
            for(Iterator<String> itr1 = reqList.iterator();itr1.hasNext();) {  
                String element1 = itr1.next(); 
                if(element.equalsIgnoreCase(element1)){
                	flag=true;
                	itr1.remove();
                	break;
                }
            }  
            if(flag){
            	itr.remove();
            	bookTimeList.add(element);
            	flag=false;
            }
        }
		availableTimeList=new TreeSet<>(totalTimeList);
		if(appointmentType.equalsIgnoreCase("group")){
			
			set = new HashSet<>();

			for (int i = 0; i < bookTimeList.size(); i++) {
			   for (int j = bookTimeList.size()-1; j > 0 ; j--) {
				   if(bookTimeList.get(i)==bookTimeList.get(j)){
					   set.add(bookTimeList.get(i));
				   }
			   	}
			  }
			  
			  for (String temp : set){
				  DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
				  Date d = null; 
				  Time time=null;
				  try {
					d = (Date)formatter.parse(temp);
					time=new Time(d.getTime());
					int min=time.getMinutes();
					if(min==00){
						time.setMinutes(45);
						time.setHours(time.getHours()-1);
					}else{
						time.setMinutes(time.getMinutes()-15);
					}
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
			        availableTimeList.remove(time.toString());
			  }
		}
		return availableTimeList;
	}

	@Override
	public void registerAppointment(AppointmentMaster appointmentMaster) {
		// TODO Auto-generated method stub
		
		appointmentMasterDao.registerAppointment(appointmentMaster);
		
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public List<WaitingTimeModel> getWaitingTime(String date) {
		List<AppointmentMaster> appointmentMastersList=appointmentMasterDao.getWaitingTime(date);
		
		Map<String, List<WaitingTimeModel>> map=new HashMap<>();
		List<String> keyInMap=new ArrayList<>();
		for(int i=MPAContext.startTime;i<=MPAContext.endTime;i++){
			map.put("listAt"+i, new ArrayList<WaitingTimeModel>());
			keyInMap.add("listAt"+i);
		}
		
		int tempTime=MPAContext.startTime;
		Map<String,List<WaitingTimeModel>> mapOfRefNo=new HashMap<>();

			for(int j=0;j<map.size();j++){
				List<WaitingTimeModel> list=map.get(keyInMap.get(j));
				for(int i=0;i<appointmentMastersList.size();i++){
					String temp1=tempTime+":00:00";
					String temp2=(tempTime+1)+":00:00";
					
					Time lowerLimit=new Time(new Date().getTime());
					lowerLimit=lowerLimit.valueOf(temp1);
					
					Time upperLimit=new Time(new Date().getTime());
					upperLimit=upperLimit.valueOf(temp2);
					
					Time currentTime=appointmentMastersList.get(i).getAppointmentTime();
					
				if((currentTime.compareTo(lowerLimit)>0 && currentTime.compareTo(upperLimit)<0) || currentTime.compareTo(lowerLimit)==0 ) {
					WaitingTimeModel waitingTimeModel=new WaitingTimeModel();
					waitingTimeModel.setAppointmentTime(appointmentMastersList.get(i).getAppointmentTime());
					waitingTimeModel.setProcessTime(appointmentMastersList.get(i).getProcessTime());
					waitingTimeModel.setRefrenceNumber(appointmentMastersList.get(i).getReferenceNo());
					list.add(waitingTimeModel);
				}
			}
			mapOfRefNo.put(keyInMap.get(j),list);
			tempTime++;
		}
		
		List<WaitingTimeModel> listWaitingTime=new ArrayList<>();
		
		for(int i=0;i<mapOfRefNo.size();i++){
			List<WaitingTimeModel> list1=mapOfRefNo.get(keyInMap.get(i));
			int counter=0;
			int counterLessThan5=0;
			int counterBetween5And10=0;
			int counterGreaterThan10=0;

			int totalLessThan5=0;
			int totalBetween5And10=0;
			int totalGreaterThan10=0;
			
			long avg=0;
			long avgLessThan5=0;
			long avgBetween5And10=0;
			long avgGreaterThan10=0;
			
			if(list1.size()>0){
				for(int j=0;j<list1.size();j++){
					Time processTime=list1.get(j).getProcessTime();
					Time appointmentTime=list1.get(j).getAppointmentTime();
					long differenceOfTime=0;
					if(processTime!=null && appointmentTime!=null){
						differenceOfTime=processTime.getMinutes()-appointmentTime.getMinutes();
					}
					
					if(differenceOfTime<=5 && differenceOfTime>=0){
						counterLessThan5++;
						totalLessThan5+=differenceOfTime;
					}else if(differenceOfTime>=10 && differenceOfTime<15){
						counterGreaterThan10++;
						totalGreaterThan10+=differenceOfTime;
					}else if(differenceOfTime>5 && differenceOfTime<10){
						counterBetween5And10++;
						totalBetween5And10+=differenceOfTime;
					}
					counter++;
				}	
				if(counterLessThan5>0)
					avgLessThan5=totalLessThan5/counterLessThan5;
				if(counterBetween5And10>0)
					avgBetween5And10=totalBetween5And10/counterBetween5And10;
				if(counterGreaterThan10>0)
					avgGreaterThan10=totalGreaterThan10/counterGreaterThan10;
				
				if(counterBetween5And10>0 || counterGreaterThan10>0 || counterLessThan5>0)
					avg=(totalBetween5And10+totalGreaterThan10+totalLessThan5)/(counterBetween5And10+counterGreaterThan10+counterLessThan5);
				
			}
			
			WaitingTimeModel waitingTimeModel=new WaitingTimeModel();
			waitingTimeModel.setCounterLessThan5(counterLessThan5);
			waitingTimeModel.setCounterBetween5And10(counterBetween5And10);
			waitingTimeModel.setCounterGreaterThan10(counterGreaterThan10);
			waitingTimeModel.setCounter(counter);
			
			
			waitingTimeModel.setAvgLessThan5(avgLessThan5);
			waitingTimeModel.setAvgBetween5And10(avgBetween5And10);
			waitingTimeModel.setAvgGreaterThan10(avgGreaterThan10);
			waitingTimeModel.setAvg(avg);
			
			listWaitingTime.add(waitingTimeModel);
		}
		return listWaitingTime;
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	@Override
	public List<ServiceTimeModel> getServiceTime(String date) {
		List<AppointmentMaster> appointmentMastersList=appointmentMasterDao.getServiceTime(date);
		
		Map<String, List<ServiceTimeModel>> map=new HashMap<>();
		List<String> keyInMap=new ArrayList<>();
		for(int i=MPAContext.startTime;i<=MPAContext.endTime;i++){
			map.put("listAt"+i, new ArrayList<ServiceTimeModel>());
			keyInMap.add("listAt"+i);
		}
		
		int tempTime=MPAContext.startTime;
		Map<String,List<ServiceTimeModel>> mapOfRefNo=new HashMap<>();

			for(int j=0;j<map.size();j++){
				List<ServiceTimeModel> list=map.get(keyInMap.get(j));
				for(int i=0;i<appointmentMastersList.size();i++){
					String temp1=tempTime+":00:00";
					String temp2=(tempTime+1)+":00:00";
					
					Time lowerLimit=new Time(new Date().getTime());
					lowerLimit=lowerLimit.valueOf(temp1);
					
					Time upperLimit=new Time(new Date().getTime());
					upperLimit=upperLimit.valueOf(temp2);
					
					Time currentTime=appointmentMastersList.get(i).getAppointmentTime();
					
				if((currentTime.compareTo(lowerLimit)>0 && currentTime.compareTo(upperLimit)<0) || currentTime.compareTo(lowerLimit)==0 ) {
					ServiceTimeModel serviceTimeModel=new ServiceTimeModel();
					serviceTimeModel.setProcessTime(appointmentMastersList.get(i).getProcessTime());
					serviceTimeModel.setCompleteTime(appointmentMastersList.get(i).getCompleteTime());
					serviceTimeModel.setRefrenceNumber(appointmentMastersList.get(i).getReferenceNo());
					list.add(serviceTimeModel);
				}
			}
			mapOfRefNo.put(keyInMap.get(j),list);
			tempTime++;
		}
		
		List<ServiceTimeModel> listServiceTime=new ArrayList<>();
		
		for(int i=0;i<mapOfRefNo.size();i++){
			List<ServiceTimeModel> list1=mapOfRefNo.get(keyInMap.get(i));
			int counter=0;
			int counterLessThan5=0;
			int counterBetween5And10=0;
			int counterGreaterThan10=0;

			int totalLessThan5=0;
			int totalBetween5And10=0;
			int totalGreaterThan10=0;
			
			long avg=0;
			long avgLessThan5=0;
			long avgBetween5And10=0;
			long avgGreaterThan10=0;
			
			if(list1.size()>0){
				for(int j=0;j<list1.size();j++){
					Time processTime=list1.get(j).getProcessTime();
					Time completeTime=list1.get(j).getCompleteTime();
					long differenceOfTime=0;
					if(processTime!=null && completeTime!=null){
						differenceOfTime=completeTime.getMinutes()-processTime.getMinutes();
					}
					
					if(differenceOfTime<=5 && differenceOfTime>0){
						counterLessThan5++;
						totalLessThan5+=differenceOfTime;
					}else if(differenceOfTime>=10 && differenceOfTime<15){
						counterGreaterThan10++;
						totalGreaterThan10+=differenceOfTime;
					}else if(differenceOfTime>5 && differenceOfTime<10){
						counterBetween5And10++;
						totalBetween5And10+=differenceOfTime;
					}
					counter++;
				}	
				if(counterLessThan5>0)
					avgLessThan5=totalLessThan5/counterLessThan5;
				if(counterBetween5And10>0)
					avgBetween5And10=totalBetween5And10/counterBetween5And10;
				if(counterGreaterThan10>0)
					avgGreaterThan10=totalGreaterThan10/counterGreaterThan10;
				
				if(counterBetween5And10>0 || counterGreaterThan10>0 || counterLessThan5>0)
					avg=(totalBetween5And10+totalGreaterThan10+totalLessThan5)/(counterBetween5And10+counterGreaterThan10+counterLessThan5);
				
			}
			
			ServiceTimeModel serviceTimeModel=new ServiceTimeModel();
			serviceTimeModel.setCounterLessThan5(counterLessThan5);
			serviceTimeModel.setCounterBetween5And10(counterBetween5And10);
			serviceTimeModel.setCounterGreaterThan10(counterGreaterThan10);
			serviceTimeModel.setCounter(counter);
			
			serviceTimeModel.setAvgLessThan5(avgLessThan5);
			serviceTimeModel.setAvgBetween5And10(avgBetween5And10);
			serviceTimeModel.setAvgGreaterThan10(avgGreaterThan10);
			serviceTimeModel.setAvg(avg);
			
			listServiceTime.add(serviceTimeModel);
		}
		return listServiceTime;
	}

	@Override
	public List<AppointmentMaster> selectcancelledAppointments(Date date) {
		// TODO Auto-generated method stub
		return appointmentMasterDao.listCancelledAppointments(date);
	}

	@Override
	public List<AppointmentMaster> selectNoShowAppointments(Date date) {
		// TODO Auto-generated method stub
		return appointmentMasterDao.selectNoShowAppointments(date);
	}

	@Override
	public List<AppointmentMaster> selectLateAppointments(Date date) {
		
		return appointmentMasterDao.selectLateAppointments(date);
		
	}

	@Override
	public List<AppointmentMaster> listAppointments(int offset, int rows) {
		// TODO Auto-generated method stub
		return appointmentMasterDao.listAppointments(offset, rows);
	}

	@Override
	public List<AppointmentMaster> getListPage(List<AppointmentMaster> list,
			int offset, int rows) {
		// TODO Auto-generated method stub
		List<AppointmentMaster> appointmentList = new ArrayList<AppointmentMaster>();
		
		for(int i = offset; i < offset + rows; i++)
		{
			appointmentList.add(list.get(i));
			if(i == list.size() - 1)
			{
				break;
			}
		}
		
		return appointmentList;
	}

	@Override
	public int getTotalUsers() {
		// TODO Auto-generated method stub
		return appointmentMasterDao.listAppointment().size();
	}

	@Override
	public List<AppointmentMaster> listOrderdAppointment(int offset, int rows,
			String property, String orderValue) {
		// TODO Auto-generated method stub
		return appointmentMasterDao.listOrderdAppointment(offset, rows, property, orderValue);
	}

	@Override
	public List<AppointmentMaster> searchAppointment(AppointmentMaster appointmentMaster, String appointmentTypeRel,String appointmentDateStringRel,String appointmentStatusRel) {
		// TODO Auto-generated method stub
		return appointmentMasterDao.searchAppointment(appointmentMaster,appointmentTypeRel,appointmentDateStringRel,appointmentStatusRel);
	}

	@Override
	public List<AppointmentMaster> listSearchOrder(int offset, int rows,
													String property, String orderValue,
													AppointmentMaster appointmentMaster, String appointmentTypeRel,
													String appointmentDateStringRel,
													String appointmentStatusRel) {
		// TODO Auto-generated method stub
		return appointmentMasterDao.listSearchOrder(offset, rows, property, orderValue,appointmentMaster,appointmentTypeRel,appointmentDateStringRel,appointmentStatusRel);
	}
}