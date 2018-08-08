package fi.korri.epooq.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class Test {

	long i=78;
	String str="Hello";
	String str1="HeLlo";
	
	public static void main(String[] args) {
		
		new Test().testMethod();
	}
	
	public void testMethod()
	{
		/*List<Long> storyPageIdList = new ArrayList<Long>();
		
		if(i!=0)
		{
			storyPageIdList.add(i);			
		
			long test = 78;
			
			if(storyPageIdList.contains(test))
			{
				System.out.println("Pass");
			}
			else
			{
				System.out.println("Fail");
			}
		}
		else
		{
			System.out.println("Thullu");
		}*/
		
		/*Map<Long, Integer> map = new HashMap<Long, Integer>();
		map.put(i,1);
		
		int value = map.get(new Long(98));
		if(value!=0)
		{
			System.out.println("Pass");
		}
		else
		{
			System.out.println("Fail");
		}*/
		
		if(StringUtils.equals(str, str1))
		{
			System.out.println("Pass");
		}
		else
		{
			System.out.println("Fail");
		}
	}
}
