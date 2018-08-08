package com.tatva.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tatva.dao.ITransactionMasterDAO;
import com.tatva.domain.TransactionMaster;
import com.tatva.domain.composite.CompositeTranc;
import com.tatva.model.AppointmentForm;
import com.tatva.service.ITransactionMasterService;

@Service
public class TransactionMasterServiceImpl implements ITransactionMasterService {

	@Autowired
	private ITransactionMasterDAO transactionMasterDao;


	private TransactionMaster transactionMaster = new TransactionMaster();

	@Override
	public void addTransactions(AppointmentForm appointmentform,String referenceGenerator) 
	{
		List<String>transactionType = appointmentform.getTransactionType();     //storing main transactions into temporary list
		for(Integer i = 0 ; i < transactionType.size() ; i ++){
			if(transactionType.get(i).equals("HCL")){          // check for selected sub-types if this perticular main-type is selected
				if(appointmentform.getHarbourCraftCheckBox().size() != 0){
					for(int i1 = 0 ; i1 <appointmentform.getHarbourCraftCheckBox().size() ; i1++){
						if(appointmentform.getHarbourCraftCheckBox().get(i1).equals("HCLNL")){        // this is check of exact sub-type of above selected main-type.
							TransactionMaster transactionMaster = new TransactionMaster();
							CompositeTranc compositetransaction = new CompositeTranc();
							compositetransaction.setReferenceNo(referenceGenerator);
							compositetransaction.setTransactionType("HCL");
							compositetransaction.setTransactionSubType("HCLNL");
							transactionMaster.setTransacId(compositetransaction);
							transactionMaster.setTransactionQuantity(Short.parseShort(appointmentform.getHCLNLSelect()));  // fetching the total no of transaction appointment
							transactionMasterDao.addTransaction(transactionMaster);  //add single record into the transaction_master table
						}
						else if(appointmentform.getHarbourCraftCheckBox().get(i1).equals("HCLAD")){			// this is check of exact sub-type of above selected main-type.
							TransactionMaster transactionMaster = new TransactionMaster();
							CompositeTranc compositetransaction = new CompositeTranc();
							compositetransaction.setReferenceNo(referenceGenerator);
							compositetransaction.setTransactionType("HCL");
							compositetransaction.setTransactionSubType("HCLAD");
							transactionMaster.setTransacId(compositetransaction);
							transactionMaster.setTransactionQuantity(Short.parseShort(appointmentform.getHCLADSelect()));	// fetching the total no of transaction appointment
							transactionMasterDao.addTransaction(transactionMaster);		//add single record into the transaction_master table
						}
						else if(appointmentform.getHarbourCraftCheckBox().get(i1).equals("HCLUC")){		// this is check of exact sub-type of above selected main-type.			
							TransactionMaster transactionMaster = new TransactionMaster();
							CompositeTranc compositetransaction = new CompositeTranc();
							compositetransaction.setReferenceNo(referenceGenerator);
							compositetransaction.setTransactionType("HCL");
							compositetransaction.setTransactionSubType("HCLUC");
							transactionMaster.setTransacId(compositetransaction);
							transactionMaster.setTransactionQuantity(Short.parseShort(appointmentform.getHCLUCSelect()));		// fetching the total no of transaction appointment
							transactionMasterDao.addTransaction(transactionMaster);		//add single record into the transaction_master table							
						}
						else if(appointmentform.getHarbourCraftCheckBox().get(i1).equals("HCLCO")){		// this is check of exact sub-type of above selected main-type.	
							TransactionMaster transactionMaster = new TransactionMaster();
							CompositeTranc compositetransaction = new CompositeTranc();
							compositetransaction.setReferenceNo(referenceGenerator);
							compositetransaction.setTransactionType("HCL");
							compositetransaction.setTransactionSubType("HCLCO");
							transactionMaster.setTransacId(compositetransaction);
							transactionMaster.setTransactionQuantity(Short.parseShort(appointmentform.getHCLCOSelect()));		// fetching the total no of transaction appointment
							transactionMasterDao.addTransaction(transactionMaster);		//add single record into the transaction_master table
						}
						else if(appointmentform.getHarbourCraftCheckBox().get(i1).equals("HCLNM")){		// this is check of exact sub-type of above selected main-type.
							TransactionMaster transactionMaster = new TransactionMaster();
							CompositeTranc compositetransaction = new CompositeTranc();
							compositetransaction.setReferenceNo(referenceGenerator);
							compositetransaction.setTransactionType("HCL");
							compositetransaction.setTransactionSubType("HCLNM");
							transactionMaster.setTransacId(compositetransaction);
							transactionMaster.setTransactionQuantity(Short.parseShort(appointmentform.getHCLNMSelect()));		// fetching the total no of transaction appointment
							transactionMasterDao.addTransaction(transactionMaster);		//add single record into the transaction_master table
						}
						else if(appointmentform.getHarbourCraftCheckBox().get(i1).equals("HCLRH")){		// this is check of exact sub-type of above selected main-type.		
							TransactionMaster transactionMaster = new TransactionMaster();
							CompositeTranc compositetransaction = new CompositeTranc();
							compositetransaction.setReferenceNo(referenceGenerator);
							compositetransaction.setTransactionType("HCL");
							compositetransaction.setTransactionSubType("HCLRH");
							transactionMaster.setTransacId(compositetransaction);
							transactionMaster.setTransactionQuantity(Short.parseShort(appointmentform.getHCLRHSelect()));		// fetching the total no of transaction appointment
							transactionMasterDao.addTransaction(transactionMaster);		//add single record into the transaction_master table
						}	
					}	
				}
			}
			else if(transactionType.get(i).equals("PCL")){		 // check for selected sub-types if this perticular main-type is selected
				if(appointmentform.getPleasureCraftCheckBox().size() != 0){
					for(int i2 = 0 ; i2 <appointmentform.getPleasureCraftCheckBox().size() ; i2++){
						if(appointmentform.getPleasureCraftCheckBox().get(i2).equals("PCLNL")){		// this is check of exact sub-type of above selected main-type.		
							TransactionMaster transactionMaster = new TransactionMaster();
							CompositeTranc compositetransaction = new CompositeTranc();
							compositetransaction.setReferenceNo(referenceGenerator);
							compositetransaction.setTransactionType("PCL");
							compositetransaction.setTransactionSubType("PCLNL");
							transactionMaster.setTransacId(compositetransaction);
							transactionMaster.setTransactionQuantity(Short.parseShort(appointmentform.getPCLNLSelect()));		// fetching the total no of transaction appointment
							transactionMasterDao.addTransaction(transactionMaster);		//add single record into the transaction_master table
						}
						else if(appointmentform.getPleasureCraftCheckBox().get(i2).equals("PCLAD")){		// this is check of exact sub-type of above selected main-type.		
							TransactionMaster transactionMaster = new TransactionMaster();
							CompositeTranc compositetransaction = new CompositeTranc();
							compositetransaction.setReferenceNo(referenceGenerator);
							compositetransaction.setTransactionType("PCL");
							compositetransaction.setTransactionSubType("PCLAD");
							transactionMaster.setTransacId(compositetransaction);
							transactionMaster.setTransactionQuantity(Short.parseShort(appointmentform.getPCLADSelect()));		// fetching the total no of transaction appointment
							transactionMasterDao.addTransaction(transactionMaster);		//add single record into the transaction_master table
						}
						else if(appointmentform.getPleasureCraftCheckBox().get(i2).equals("PCLUC")){		// this is check of exact sub-type of above selected main-type.		
							TransactionMaster transactionMaster = new TransactionMaster();
							CompositeTranc compositetransaction = new CompositeTranc();
							compositetransaction.setReferenceNo(referenceGenerator);
							compositetransaction.setTransactionType("PCL");
							compositetransaction.setTransactionSubType("PCLUC");
							transactionMaster.setTransacId(compositetransaction);
							transactionMaster.setTransactionQuantity(Short.parseShort(appointmentform.getPCLUCSelect()));		// fetching the total no of transaction appointment
							transactionMasterDao.addTransaction(transactionMaster);		//add single record into the transaction_master table	
						}
						else if(appointmentform.getPleasureCraftCheckBox().get(i2).equals("PCLNP")){		// this is check of exact sub-type of above selected main-type.		
							TransactionMaster transactionMaster = new TransactionMaster();
							CompositeTranc compositetransaction = new CompositeTranc();
							compositetransaction.setReferenceNo(referenceGenerator);
							compositetransaction.setTransactionType("PCL");
							compositetransaction.setTransactionSubType("PCLNP");
							transactionMaster.setTransacId(compositetransaction);
							transactionMaster.setTransactionQuantity(Short.parseShort(appointmentform.getPCLNPSelect()));		// fetching the total no of transaction appointment
							transactionMasterDao.addTransaction(transactionMaster);		//add single record into the transaction_master table		
						}
					}
				}
			}
			else if(transactionType.get(i).equals("PC"))		// check for selected sub-types if this perticular main-type is selected
			{
				if(appointmentform.getHarbourCraftCheckBox().size() != 0)
				{
					for(int i3 = 0 ; i3 <appointmentform.getPortClearanceCheckBox().size() ; i3++)
					{
						if(appointmentform.getPortClearanceCheckBox().get(i3).equals("PCGD"))		// this is check of exact sub-type of above selected main-type.
						{
							TransactionMaster transactionMaster = new TransactionMaster();
							CompositeTranc compositetransaction = new CompositeTranc();
							compositetransaction.setReferenceNo(referenceGenerator);
							compositetransaction.setTransactionType("PC");
							compositetransaction.setTransactionSubType("PCGD");
							transactionMaster.setTransacId(compositetransaction);
							transactionMaster.setTransactionQuantity(Short.parseShort(appointmentform.getPCGDSelect()));		// fetching the total no of transaction appointment
							transactionMasterDao.addTransaction(transactionMaster);		//add single record into the transaction_master table		
						}
						else if(appointmentform.getPortClearanceCheckBox().get(i3).equals("PCAL"))		// this is check of exact sub-type of above selected main-type.
						{
							TransactionMaster transactionMaster = new TransactionMaster();
							CompositeTranc compositetransaction = new CompositeTranc();
							compositetransaction.setReferenceNo(referenceGenerator);
							compositetransaction.setTransactionType("PC");
							compositetransaction.setTransactionSubType("PCAL");
							transactionMaster.setTransacId(compositetransaction);
							transactionMaster.setTransactionQuantity(Short.parseShort(appointmentform.getPCALSelect()));		// fetching the total no of transaction appointment
							transactionMasterDao.addTransaction(transactionMaster);		//add single record into the transaction_master table				
						}
						else if(appointmentform.getPortClearanceCheckBox().get(i3).equals("PCAB"))		// this is check of exact sub-type of above selected main-type.
						{	
							TransactionMaster transactionMaster = new TransactionMaster();
							CompositeTranc compositetransaction = new CompositeTranc();
							compositetransaction.setReferenceNo(referenceGenerator);
							compositetransaction.setTransactionType("PC");
							compositetransaction.setTransactionSubType("PCAB");
							transactionMaster.setTransacId(compositetransaction);
							transactionMaster.setTransactionQuantity(Short.parseShort(appointmentform.getPCABSelect()));		// fetching the total no of transaction appointment
							transactionMasterDao.addTransaction(transactionMaster);		//add single record into the transaction_master table				
						}
					}
				}			
			}
			else if(transactionType.get(i).equals("OTHS"))		// check for selected sub-types if this perticular main-type is selected
			{
				if(appointmentform.getOthersCheckBox().size() != 0)
				{
					for(int i4 = 0 ; i4 <appointmentform.getOthersCheckBox().size() ; i4++)
					{
						if(appointmentform.getOthersCheckBox().get(i4).equals("OTHS"))		// this is check of exact sub-type of above selected main-type.
						{
							TransactionMaster transactionMaster = new TransactionMaster();
							CompositeTranc compositetransaction = new CompositeTranc();
							compositetransaction.setReferenceNo(referenceGenerator);
							compositetransaction.setTransactionType("OTHS");
							compositetransaction.setTransactionSubType("OTHS");
							transactionMaster.setTransacId(compositetransaction);
							transactionMaster.setTransactionQuantity(Short.parseShort(appointmentform.getOTHSSelect()));		// fetching the total no of transaction appointment
							transactionMasterDao.addTransaction(transactionMaster);		//add single record into the transaction_master table				
						}
					}
				}
			}
		}
	}
}
