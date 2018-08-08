package fi.korri.epooq.service;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;

import org.apache.commons.lang.CharUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fi.korri.epooq.dao.StoryDao;
import fi.korri.epooq.model.Story;
import fi.korri.epooq.model.StoryContent;
import fi.korri.epooq.model.StoryPage;
import fi.korri.epooq.util.MediaUtils;

@Service
public class StoryService {
	private final Logger log = Logger.getLogger(StoryService.class.getName());
	private static String TEMP_AUDIO_PATH = "resources/audio/temp";
	private static String TEMP_VIDEO_PATH = "resources/video/temp";
	private static String TEMP_IMAGE_PATH = "resources/image/temp";
	
	@Autowired
	private StoryDao storyDao;
	
	@Autowired
	ServletContext servletContext;
	
	// get story by id
	public Story getStoryById(long storyId) 
	{	
		
		Story story = null;
		try 
		{
			story = storyDao.getStoryById(storyId);
			log.debug("Story: "+storyId+" "+"loaded");
		} 
		catch (SQLException e) 
		{	
			log.error("Failed to load story");
			e.printStackTrace();
		}
		
		return story;
	}
	
	// get story by title
	public List<Story> getStoryByTitle(String storyTitle) 
	{
		List<Story> storyList = null;
		try 
		{
			storyList = storyDao.getStoryByTitle(storyTitle);
			log.debug("Story: "+storyTitle+" "+"loaded");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return storyList;
	}
	
	// add story page with image,video,audio or text
	public Story addMediaStoryPage(Story story,
							  	   MultipartFile file,
							  	   String description,
							  	   String contentType) throws Exception
	{
		log.info("Add media story page");
		StoryContent storyContent = new StoryContent();
		storyContent.setDescription(description);
		storyContent.setType(contentType);
		
		// store file in temp folder with unique UUID
		String tempFilePath = null;
		String relativeFilePath = null;
		boolean directoryExists = false;
		if(contentType.contains("video"))
		{	log.debug("Content type video");
			tempFilePath = servletContext.getRealPath(TEMP_VIDEO_PATH);
			directoryExists = createMediaDirectoresIfNotExists(tempFilePath);
			relativeFilePath = TEMP_VIDEO_PATH;
		}
		else if(contentType.contains("audio"))
		{	log.debug("Content type audio");
			tempFilePath = servletContext.getRealPath(TEMP_AUDIO_PATH);
			directoryExists = createMediaDirectoresIfNotExists(tempFilePath);
			relativeFilePath = TEMP_AUDIO_PATH;
		}
		else if(contentType.contains("image"))
		{	
			log.debug("Content type image");
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
		
		String tempFileName = "temp" + "_" + uuid + extension;
		
		FileOutputStream fos = new FileOutputStream(tempFilePath+"\\"+tempFileName);
		fos.write(file.getBytes());
		fos.close();

		storyContent.setContent(relativeFilePath+"/"+tempFileName);
		
		StoryPage storyPage = new StoryPage();
		storyPage.setStoryContent(storyContent); // add content to storypage
		storyPage.setCreated(new Date());
		log.info("Media page added: "+relativeFilePath+"/"+tempFileName);
		// add storypage to newstory
		story.getStoryPages().add(storyPage);
		
		return story;
	}
	
	// add story page with video using webcam
	public Story addWebcamStoryPage(Story story,
									String videoId) throws Exception
	{
		log.debug("Add video using webcam");
		StoryContent webcamContent = new StoryContent();
		webcamContent.setContent("streams/"+videoId+".flv");
		webcamContent.setType("video/webcam");
		
		StoryPage webcamPage = new StoryPage();
		webcamPage.setStoryContent(webcamContent);
		webcamPage.setCreated(new Date());

		story.getStoryPages().add(webcamPage);
		
		String tempFilePath = null;
		tempFilePath = servletContext.getRealPath(TEMP_VIDEO_PATH);
		createMediaDirectoresIfNotExists(tempFilePath);
		
		return story;
	}
	// add story page with audio using microphone
	public Story addMicrophoneStoryPage(Story story,
										String audioId) throws Exception
	{
		log.debug("Add audio using microphone");
		String streamPath = servletContext.getRealPath("streams");
		String flvFilePath = streamPath + "\\" + audioId + ".flv";
		String mp3FilePath = streamPath + "\\" + audioId + ".mp3";
		File flvFile = new File(flvFilePath);
		File mp3File = new File(mp3FilePath);
		
		System.out.println("FLV: " + flvFilePath);
		System.out.println("MP3: " + mp3FilePath);
		
		String workingDir = System.getProperty("user.dir");
		String executableDir = workingDir + "\\exe" + "\\ffmpeg";
		
		boolean isConvertSuccess = MediaUtils.convertAudio( flvFile.getAbsolutePath(), 
											   				mp3File.getAbsolutePath(), 
											   				executableDir);
		
		if(isConvertSuccess)
		{
			StoryContent microphoneContent = new StoryContent();
			microphoneContent.setContent("streams/"+audioId+".mp3");
			microphoneContent.setType("audio/mic");
	
			StoryPage webcamPage = new StoryPage();
			webcamPage.setStoryContent(microphoneContent);
			webcamPage.setCreated(new Date());
	
			story.getStoryPages().add(webcamPage);
	
			String tempFilePath = null;
			tempFilePath = servletContext.getRealPath(TEMP_AUDIO_PATH);
			createMediaDirectoresIfNotExists(tempFilePath);
			flvFile.delete();
		}
		return story;
	}
	
	
	
	
	public Story changeMicrophoneStoryPage(Story story,
			String audioId,int pageIndex) throws Exception{
		
			List<StoryPage> storyPages = story.getStoryPages();
			StoryContent storyContent = storyPages.get(pageIndex).getStoryContent();
		
				String streamPath = servletContext.getRealPath("streams");
				String flvFilePath = streamPath + "\\" + audioId + ".flv";
				String mp3FilePath = streamPath + "\\" + audioId + ".mp3";
				File flvFile = new File(flvFilePath);
				File mp3File = new File(mp3FilePath);
				
				System.out.println("FLV: " + flvFilePath);
				System.out.println("MP3: " + mp3FilePath);
				
				String workingDir = System.getProperty("user.dir");
				String executableDir = workingDir + "\\exe" + "\\ffmpeg";
				
				boolean isConvertSuccess = MediaUtils.convertAudio( flvFile.getAbsolutePath(), 
								   				mp3File.getAbsolutePath(), 
								   				executableDir);
				
				if(isConvertSuccess)
				{
					storyContent.setContent("streams/"+audioId+".mp3");
					storyContent.setType("audio/mic");
					storyPages.get(pageIndex).setStoryContent(storyContent);
					story.setStoryPages(storyPages);
					flvFile.delete();
				}
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
		
		/*String tempFileName = uploadFileName.substring(0, uploadFileName.lastIndexOf('.')) + "_" + uuid + extension;*/
		String tempFileName = "temp" + "_" + uuid + extension;
		
		FileOutputStream fos = new FileOutputStream(tempFilePath+"\\"+tempFileName);
		fos.write(file.getBytes());
		fos.close();
		
		return relativeFilePath+"/"+tempFileName;
	}
	
	// remove story content
	public boolean removeStoryContent(String relativePath)
	{	log.debug("Remove story content");
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
	
	// remove story 
	public boolean removeStory(Story story)
	{	log.debug("Remove story");
		boolean result = false;
		
		try
		{
			for(StoryPage storyPage : story.getStoryPages())
			{	log.debug("Remove story page");
				storyDao.deleteStoryPage(storyPage);
			}
			
			storyDao.deleteStory(story);
			
			result = true;
		}
		catch(Exception e)
		{	log.error("Can not remove story page");
			result = false;
			e.printStackTrace();
		}
		
		return result;
	}
	
	// add text to story page
	public Story addTextStoryPage(Story story,
								  String text)
	{
		log.debug("Add story page with text");
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
	// save story
	public Story saveStory(Story newStory,
						   String publishing)
	{
		log.debug("Save story");
		newStory.setPublishing(publishing);
		
		Story insertedStory = null;
		try 
		{
			insertedStory = storyDao.insertStory(newStory);
			log.info("Story saved successfully");
		} 
		catch (SQLException e) 
		{	log.error("Can not save story");
			e.printStackTrace();
		}
		
		return insertedStory;
	}
	// update story
	public Story updateStory(Story story,
							 String publishing)
	{	log.debug("update story");
		try 
		{
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
			int oldStorySize = oldStory.getStoryPages().size();
			for(int i=oldStorySize-1; i>=0; i--) // Deletion
			{
				long oldStoryPageId = oldStory.getStoryPages().get(i).getId();
				if(!storyPageIdList.contains(oldStoryPageId))
				{
					// Delete story page & its story content
					storyDao.deleteStoryPage(oldStory.getStoryPages().get(i));
					oldStory.getStoryPages().remove(i);
					
				}
			}
			
			
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
							String newContent=null;
							
							newContent = storyDao.insertNewMediaFileForStoryContent(story.getId(), content);
							content.setContent(newContent);
						}
						
						if(content.getType().contains("image") && !oldContent.getDescription().equalsIgnoreCase(content.getDescription()))
						{
							// description needs to change
							String newDescription = null;
							if(CharUtils.toString(content.getNarrationAudio()).equals("Y"))
							{
								// insert new media file
								newDescription = storyDao.insertNewMediaFileForStoryContent(story.getId(), content);
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
				log.info("Creating directory: " + dir);
				result = dir.mkdirs();
			}
			catch(Exception e)
			{
				log.info("Failed to create directory: " + dir);
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
	// get all story list
	public List<Story> getAllStories(){
		List<Story> listStory = null;
		try {
			listStory = storyDao.getAllStories();
			log.info("All story list loaded");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("Failed to load story list");
			e.printStackTrace();
		}
		
		return listStory;
	}
	
}
