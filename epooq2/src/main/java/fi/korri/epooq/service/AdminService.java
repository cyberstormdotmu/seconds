package fi.korri.epooq.service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fi.korri.epooq.dao.AdminDao;
import fi.korri.epooq.model.MapImage;
import fi.korri.epooq.model.Story;
import fi.korri.epooq.model.StoryContent;
import fi.korri.epooq.model.StoryPage;

@Service
public class AdminService 
{
	private static String TEMP_IMAGE_PATH = "resources/image/admin/temp";

	@Autowired
	ServletContext servletContext;
	
	@Autowired
	AdminDao adminDao;

	public MapImage addMediaStoryPage(	MapImage mapImage,
										MultipartFile file) throws Exception
	{
		// store file in temp folder with unique UUID
		String tempFilePath = null;
		String relativeFilePath = null;
		boolean directoryExists = false;

		tempFilePath = servletContext.getRealPath(TEMP_IMAGE_PATH);
		directoryExists = createMediaDirectoresIfNotExists(tempFilePath);
		relativeFilePath = TEMP_IMAGE_PATH;


		// Return null if File location could not retrieved
		if(tempFilePath==null)
		{
			return null;
		}

		// Return null if temp directory doesn't exists
		if(!directoryExists)
		{
			return null;
		}

		String uuid = UUID.randomUUID().toString();

		// append uuid at end of file name
		String uploadFileName = file.getOriginalFilename();
		String extension = uploadFileName.substring(uploadFileName.lastIndexOf('.'));

		String tempFileName = uploadFileName.substring(0, uploadFileName.lastIndexOf('.')) + "_" + uuid + extension;

		FileOutputStream fos = new FileOutputStream(tempFilePath+"\\"+tempFileName);
		fos.write(file.getBytes());
		fos.close();

		mapImage.setFile(relativeFilePath+"/"+tempFileName);

		return mapImage;
	}
	
	public boolean removeUploadedMapImage(String relativePath)
	{
		String filePath = servletContext.getRealPath(relativePath);
		
		File file = new File(filePath);
		
		if(file.exists() && file.isFile())
		{
			boolean deleted = file.delete();
			return deleted;
		}
		else
		{
			return false;
		}
	}

	private boolean createMediaDirectoresIfNotExists(String directoryPath)
	{
		boolean result = false;
		File dir = new File(directoryPath);
		if(!dir.exists())
		{
			try
			{
				System.out.println("Creating directory: " + dir);
				result = dir.mkdirs();
			}
			catch(Exception e)
			{
				System.out.println("Failed to create directory: " + dir);
				result = false;
				e.printStackTrace();
			}
		}
		else
		{
			result = true;
		}

		return result;
	}
	
	public void insertMapImage(MapImage mapImage)
	{
		try
		{
			adminDao.insertMapImage(mapImage);
		}
		catch(Exception e)
		{
			System.out.println("Failed to Insert Map Image");
			e.printStackTrace();
		}
	}
	
	public List<MapImage> getMapImageList()
	{
		try {
			return adminDao.getAllMapImages();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<MapImage>();
	}
	
	public List<MapImage> getMapImageList(int page)
	{
		int rows = 10;
		int offset = (page - 1) * rows;
		try {
			return adminDao.getAllMapImages(offset, rows);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<MapImage>();
	}
	
	
	public void updateMapImage(MapImage mapImage)
	{
		try
		{
			adminDao.updateMapImage(mapImage);
		}
		catch(Exception e)
		{
			System.out.println("Failed to Update Map Image");
			e.printStackTrace();
		}
	}
	
	public void deleteMapImage(long id)
	{
		try
		{
			adminDao.deleteMapImage(id);			
		}
		catch(Exception e)
		{
			System.out.println("Failed to Delete Map Image");
			e.printStackTrace();
		}
	}
	
	public MapImage selectMapImage(long id)
	{
		try
		{
			return adminDao.getMapImage(id);	
		}
		catch(Exception e)
		{
			System.out.println("Failed to load Map Image");
			e.printStackTrace();
		}
		return new MapImage();
	}
	
	public int getMapImageCount()
	{
		int count = 0;
		
		try
		{
			count = adminDao.getAllMapImageCount();
		}
		catch(Exception e)
		{
			System.out.println("Failed to Get Map Image count");
			e.printStackTrace();
		}
		return count;
	}
}
