package fi.korri.epooq.service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
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
	private final Logger log = Logger.getLogger(AdminService.class.getName());
	
	private static String TEMP_IMAGE_PATH = "resources/image/admin/temp";

	@Autowired
	ServletContext servletContext;
	
	@Autowired
	AdminDao adminDao;
	// add map image
	public MapImage addMediaStoryPage(	MapImage mapImage,
										MultipartFile file) throws Exception
	{
		log.info("Add map image");
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

		String tempFileName = "temp"+ "_" + uuid + extension;

		FileOutputStream fos = new FileOutputStream(tempFilePath+"\\"+tempFileName);
		fos.write(file.getBytes());
		fos.close();

		mapImage.setFile(relativeFilePath+"/"+tempFileName);
		log.debug("Map image added and stored at: "+relativeFilePath+"/"+tempFileName);
		return mapImage;
	}
	
	// remove uploaded map image
	public boolean removeUploadedMapImage(String relativePath)
	{
		String filePath = servletContext.getRealPath(relativePath);
		
		File file = new File(filePath);
		
		if(file.exists() && file.isFile())
		{
			boolean deleted = file.delete();
			log.debug("Map image from: "+relativePath+" "+"is removed" );
			return deleted;
		}
		else
		{
			log.error("Map image can not be removed" );
			return false;
		}
		
	}

	// create folder to store image
	private boolean createMediaDirectoresIfNotExists(String directoryPath)
	{
		boolean result = false;
		File dir = new File(directoryPath);
		if(!dir.exists())
		{
			try
			{
				log.debug("Creating directory to store image: " + dir);
				result = dir.mkdirs();
			}
			catch(Exception e)
			{
				log.error("Failed to create directory: " + dir);
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
	// insert map image
	public void insertMapImage(MapImage mapImage)
	{	
		try
		{
			adminDao.insertMapImage(mapImage);
			log.debug("Map image inserted successfully");
		}
		catch(Exception e)
		{
			log.error("Failed to insert map image");
			e.printStackTrace();
		}
	}
	// methods to get map image list
	public List<MapImage> getMapImageList()
	{	
		try {
			log.debug("List of map images");
			return adminDao.getAllMapImages();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<MapImage>();
	}
	
	public List<MapImage> getMapImageList(int page)
	{
		log.debug("Selecting map image list with page no: " + page);
		int rows = 6;
		int offset = (page - 1) * rows;
		try {
			return adminDao.getAllMapImages(offset, rows);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<MapImage>();
	}
	
	// update map image list
	public void updateMapImage(MapImage mapImage)
	{
		try
		{	
			adminDao.updateMapImage(mapImage);
			log.debug("Map image updated successfully");
		}
		catch(Exception e)
		{
			log.error("Failed to update map image");
			e.printStackTrace();
		}
	}
	
	// delete map image
	public void deleteMapImage(long id)
	{
		try
		{
			adminDao.deleteMapImage(id);	
			log.debug("Map image "+id+" "+"is deleted");
		}
		catch(Exception e)
		{
			log.error("failed to delete map image: "+id);
			e.printStackTrace();
		}
	}
	
	// get map image by id 
	public MapImage selectMapImage(long id)
	{
		try
		{
			log.debug("Load map image: "+id);
			return adminDao.getMapImage(id);	
		}
		catch(Exception e)
		{
			log.error("Failed to load map image");
			e.printStackTrace();
		}
		return new MapImage();
	}
	
	// get no. of map images
	public int getMapImageCount()
	{
		int count = 0;
		
		try
		{
			count = adminDao.getAllMapImageCount();
			log.debug("No. of images: "+count);
		}
		catch(Exception e)
		{
			log.error("Failed to get map image count");
			e.printStackTrace();
		}
		return count;
	}
}
