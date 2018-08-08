package enersis.envisor.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import enersis.envisor.entity.Project;



public class PrimefacesUtils {
	
	public static Map<Project,String> projectStringConverter(List<Project> projects)
	{
		Map<Project,String> projectStringMap = new HashMap<Project, String>();
		for (Project project : projects) {
			projectStringMap.put(project, project.getProjectName());
		}
		return projectStringMap;
	}
	
	public static String DateTimeToDateString(Date date)
	{
		DateTime dt = new DateTime(date);
		 DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		 String str = fmt.print(dt);

		return str;
	}
}
