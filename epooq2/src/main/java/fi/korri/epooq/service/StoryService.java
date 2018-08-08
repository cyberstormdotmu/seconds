package fi.korri.epooq.service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.commons.lang.CharUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fi.korri.epooq.model.StoryContent;
import fi.korri.epooq.model.StoryPage;
import fi.korri.epooq.dao.StoryDao;
import fi.korri.epooq.model.Story;
import fi.korri.epooq.util.DateUtil;

@Service
public class StoryService {
	
	private static String TEMP_AUDIO_PATH = "resources/audio/temp";
	private static String TEMP_VIDEO_PATH = "resources/video/temp";
	private static String TEMP_IMAGE_PATH = "resources/image/temp";
	
	@Autowired
	private StoryDao storyDao;
	
	@Autowired
	ServletContext servletContext;

	public Story getStoryById(long storyId) 
	{
		Story story = null;
		try 
		{
			story = storyDao.getStoryById(storyId);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return story;
	}
	
	
	public List<Story> getStoryByTitle(String storyTitle) 
	{
		List<Story> storyList = null;
		try 
		{
			storyList = storyDao.getStoryByTitle(storyTitle);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return storyList;
	}
	
	
	public Story addMediaStoryPage(Story story,
							  	   MultipartFile file,
							  	   String description,
							  	   String contentType) throws Exception
	{
		StoryContent storyContent = new StoryContent();
		storyContent.setDescription(description);
		storyContent.setType(contentType);

		// store file in temp folder with unique UUID
		String tempFilePath = null;
		String relativeFilePath = null;
		boolean directoryExists = false;
		if(contentType.contains("video"))
		{
			tempFilePath = servletContext.getRealPath(TEMP_VIDEO_PATH);
			directoryExists = createMediaDirectoresIfNotExists(tempFilePath);
			relativeFilePath = TEMP_VIDEO_PATH;
		}
		else if(contentType.contains("audio"))
		{
			tempFilePath = servletContext.getRealPath(TEMP_AUDIO_PATH);
			directoryExists = createMediaDirectoresIfNotExists(tempFilePath);
			relativeFilePath = TEMP_AUDIO_PATH;
		}
		else if(contentType.contains("image"))
		{
			tempFilePath = servletContext.getRealPath(TEMP_IMAGE_PATH);
			directoryExists = createMediaDirectoresIfNotExists(tempFilePath);
			relativeFilePath = TEMP_IMAGE_PATH;
		}
		
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

		storyContent.setContent(relativeFilePath+"/"+tempFileName);

		StoryPage storyPage = new StoryPage();
		storyPage.setStoryContent(storyContent); // add content to storypage
		storyPage.setCreated(new Date());

		// add storypage to newstory
		story.getStoryPages().add(storyPage);
		
		return story;
	}
	
	public String addMediaFile(MultipartFile file, String contentType) throws Exception
	{
		// store file in temp folder with unique UUID
		String tempFilePath = null;
		String relativeFilePath = null;
		boolean directoryExists = false;
		if(contentType.contains("video"))
		{
			tempFilePath = servletContext.getRealPath(TEMP_VIDEO_PATH);
			directoryExists = createMediaDirectoresIfNotExists(tempFilePath);
			relativeFilePath = TEMP_VIDEO_PATH;
		}
		else if(contentType.contains("audio"))
		{
			tempFilePath = servletContext.getRealPath(TEMP_AUDIO_PATH);
			directoryExists = createMediaDirectoresIfNotExists(tempFilePath);
			relativeFilePath = TEMP_AUDIO_PATH;
		}
		else if(contentType.contains("image"))
		{
			tempFilePath = servletContext.getRealPath(TEMP_IMAGE_PATH);
			directoryExists = createMediaDirectoresIfNotExists(tempFilePath);
			relativeFilePath = TEMP_IMAGE_PATH;
		}
		
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
		
		return relativeFilePath+"/"+tempFileName;
	}
	
	public boolean removeStoryContent(String relativePath)
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
	
	public boolean removeStory(Story story)
	{
		boolean result = false;
		
		try
		{
			for(StoryPage storyPage : story.getStoryPages())
			{
				storyDao.deleteStoryPage(storyPage);
			}
			
			storyDao.deleteStory(story);
			
			result = true;
		}
		catch(Exception e)
		{
			result = false;
			e.printStackTrace();
		}
		
		return result;
	}
	
	public Story addTextStoryPage(Story story,
								  String text)
	{
		StoryContent storyContent = new StoryContent();
		storyContent.setDescription(null);
		storyContent.setType("TEXT");
		storyContent.setContent(text);
		
		StoryPage storyPage = new StoryPage();
		storyPage.setStoryContent(storyContent); // add storycontent to storypage
		storyPage.setCreated(new Date());
		
		// add storypage to newstory
		story.getStoryPages().add(storyPage);
		
		return story;
	}
	
	public Story saveStory(Story newStory,
						   String publishing)
	{
		newStory.setPublishing(publishing);
		
		Story insertedStory = null;
		try 
		{
			insertedStory = storyDao.insertStory(newStory);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return insertedStory;
	}
	
	public Story updateStory(Story story,
							 String publishing)
	{
		try 
		{
			/************************************************************************/
			
			// Delete story pages which are not present in updated story
			List<Long> storyPageIdList = new ArrayList<Long>();
			for(StoryPage storyPage : story.getStoryPages()) // Create list of story page ids
			{
				if(storyPage.getId()!=0)
				{
					storyPageIdList.add(storyPage.getId());
				}
			}
			
			Story oldStory = storyDao.getStoryById(story.getId());
			for(int i=0; i<oldStory.getStoryPages().size(); i++) // Deletion
			{
				long oldStoryPageId = oldStory.getStoryPages().get(i).getId();
				if(!storyPageIdList.contains(oldStoryPageId))
				{
					// Delete story page & its story content
					storyDao.deleteStoryPage(oldStory.getStoryPages().get(i));
					oldStory.getStoryPages().remove(i);
				}
			}
			
			/************************************************************************/
			
			// Insert/Update story pages
			List<StoryPage> oldStoryPages = oldStory.getStoryPages();
			Map<Long, Integer> oldStoryPagesMap = new HashMap<Long, Integer>();
			int index=0;
			for(StoryPage oldStoryPage : oldStoryPages) // Create map of old story page 
			{
				oldStoryPagesMap.put(oldStoryPage.getId(), index);
				index++;
			}
			
			for(int i=0; i<story.getStoryPages().size(); i++)
			{
				StoryPage storyPage = story.getStoryPages().get(i);
				
				// if old story page exists then update otherwise insert new story page
				if(oldStoryPagesMap.containsKey(storyPage.getId()))
				{
					StoryContent oldContent =  oldStoryPages.get(oldStoryPagesMap.get(storyPage.getId())).getStoryContent();
					StoryContent content = storyPage.getStoryContent();
					
					if(!oldContent.equals(content)) // match old & updated content
					{
						if(!oldContent.getContent().equalsIgnoreCase(content.getContent()) && !content.getType().equals("TEXT"))
						{
							// content needs to update
							// insert new media file
							String newContent = storyDao.insertNewMediaFileForStoryContent(story.getId(), content.getId(), content.getContent());
							content.setContent(newContent);
						}
						
						if(content.getType().contains("image") && !oldContent.getDescription().equalsIgnoreCase(content.getDescription()))
						{
							// description needs to change
							String newDescription = null;
							if(CharUtils.toString(content.getNarrationAudio()).equals("Y"))
							{
								// insert new media file
								newDescription = storyDao.insertNewMediaFileForStoryContent(story.getId(), content.getId(), content.getDescription());
								content.setDescription(newDescription);
							}
						}
						
						// update story content
						storyDao.updateStoryContent(content);
					}
				}
				else
				{
					// insert new story content(with media file)
					StoryContent newStoryContent = storyDao.insertStoryContent(story.getId(), storyPage.getStoryContent());
					storyPage.setStoryContent(newStoryContent);
					
					// insert new story page
					storyDao.insertStoryPage(story.getId(), storyPage);
				}
				
				// update story page with index number
				storyDao.updateStoryPageIndex(storyPage.getId(), i+1);
			}
			
			/************************************************************************/
			
			// Update Story
			storyDao.updateStory(story);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return null;
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

	public List<Story> getAllStories(){
		List<Story> listStory = null;
		try {
			listStory = storyDao.getAllStories();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return listStory;
	}
	
}
