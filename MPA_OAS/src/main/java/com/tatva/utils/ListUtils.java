package com.tatva.utils;

import java.util.List;

import com.tatva.domain.AppointmentCraft;
import com.tatva.domain.TransactionMaster;
/**
 * 
 * @author pci94
 * Utility class for showing list of data in separating by new line character in Grid 
 */
public class ListUtils
{
	/**
	 * 
	 * @param list
	 * @return converted list in string format
	 */
	public static String convertListToString(List<? extends Object> list)
	{
		StringBuilder sb = new StringBuilder();
		
		for(Object obj : list)
		{
			sb.append(obj.toString());
			sb.append("\n");
		}
		//sb.substring(0, sb.length()-2);
		return sb.toString();
	}
	
	/**
	 * 
	 * @param list of transactions types
	 * @return converted list of transaction types in string format
	 */
	public static String convertTransactionMasterListToString(List<TransactionMaster> list)
	{
		StringBuilder sb = new StringBuilder();
		
		for(TransactionMaster obj : list)
		{
			sb.append(obj.getTransacId().getTransactionType().toString());
			sb.append(" / ");
			sb.append(obj.getTransacId().getTransactionSubType().toString());
			sb.append("\n");
		}
		//sb.substring(0, sb.length()-2);
		return sb.toString();
	}
	
	/**
	 * 
	 * @param list craft numbers
	 * @return converted list of craft numbers in string format
	 */
	public static String convertAppointMentCraftListToString(List<AppointmentCraft> list)
	{
		StringBuilder sb = new StringBuilder();
		
		for(AppointmentCraft obj : list)
		{
			sb.append(obj.getCraftId().getCraftNo().toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
