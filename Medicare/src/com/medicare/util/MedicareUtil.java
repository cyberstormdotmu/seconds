package com.medicare.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class MedicareUtil {

	public static String getPath(){
		String path = MedicareUtil.class.getClassLoader().getResource("").getPath();

		String fullPath="";
		try {
			fullPath = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String pathArr[] = fullPath.split("/classes/");
		String  reponsePath = pathArr[0]+ File.separatorChar;
		System.out.println("fullPath >>>>>"+pathArr[0]);
		/*System.out.println("pathArr[0] >>>>>"+pathArr[0]);
		fullPath = pathArr[0];
		String reponsePath = "";
		reponsePath = new File(fullPath).getPath() + File.separatorChar + "newfile.txt";*/
        return reponsePath;
        
	}
}
