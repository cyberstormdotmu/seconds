package fi.korri.epooq.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.commons.lang.CharUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fi.korri.epooq.model.StoryContent;
import fi.korri.epooq.model.Story;
import fi.korri.epooq.model.StoryPage;

@Repository
public class StoryDao {

	@Resource
	private DataSource dataSource;
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	UserDao userDao;
	
	
	/**
	 * Get All Stories from story table with Story Pages and Story Content  
	 * @return
	 * @throws SQLException
	 */
	public List<Story> getAllStories() throws SQLException{
		List<Story> listStories = null;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			conn = dataSource.getConnection();
			String selectSql="select * from STORY order by STORY_DATE DESC";
			ps = conn.prepareStatement(selectSql);
			rs = ps.executeQuery();
			listStories = convertResultSetToStoryList(rs);
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		
		return listStories;
		
	}
	
	public List<StoryPage> getStoryPagesByStoryId(long storyId) throws SQLException{
		List<StoryPage> listStoryPages=null;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			conn = dataSource.getConnection();
			StringBuffer selectSql = new StringBuffer().append(" SELECT ")
					   .append(" * ")
					   .append(" FROM STORY_PAGE ")
					   .append(" WHERE STORY_ID = ? ");
					
			ps = conn.prepareStatement(selectSql.toString());
			ps.setLong(1, storyId);
					
			rs = ps.executeQuery();
			listStoryPages = convertResultSetToStoryPageList(rs);

			rs.close();
			ps.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		return listStoryPages;
	}

	
	public StoryContent getStoryContentById(long id) throws SQLException{
		List<StoryContent> listStoryConetnt=null;
		StoryContent storyContent=null;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			conn = dataSource.getConnection();
			StringBuffer selectSql = new StringBuffer().append(" SELECT ")
					   .append(" * ")
					   .append(" FROM STORY_CONTENT ")
					   .append(" WHERE ID = ? ");
					
			ps = conn.prepareStatement(selectSql.toString());
			ps.setLong(1, id);
					
			rs = ps.executeQuery();
			listStoryConetnt = convertResultSetToStoryContentList(rs);
			if(listStoryConetnt.size()>0){
				storyContent=listStoryConetnt.get(0);
			}
			rs.close();
			ps.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		
		return storyContent;
	}

	public Story getStoryById(long storyId) throws SQLException
	{
		Story story = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			conn = dataSource.getConnection();
			StringBuffer selectSql = new StringBuffer().append(" SELECT ")
													   .append(" * ")
													   .append(" FROM STORY ")
													   .append(" WHERE id = ? ");

			ps = conn.prepareStatement(selectSql.toString());
			ps.setLong(1, storyId);

			rs = ps.executeQuery();

			story = convertResultSetToStory(rs);
			List<StoryPage> storyPages = getStoryPagesByStoryId(storyId);
			story.setStoryPages(storyPages);
			
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		
		return story;
	}
	
	
	
	
	public List<Story> getStoryByTitle(String storyTitle) throws SQLException
	{
		List<Story> storyList = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			conn = dataSource.getConnection();
			StringBuffer selectSql = new StringBuffer().append(" SELECT ")
													   .append(" * ")
													   .append(" FROM STORY ")
													   .append(" WHERE title = ?  order by STORY_DATE DESC");

			ps = conn.prepareStatement(selectSql.toString());
			ps.setString(1, storyTitle);

			rs = ps.executeQuery();

			storyList = convertResultSetToStoryList(rs);
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		
		return storyList;
	}
	
	
	public StoryContent getContent(long contentId) throws SQLException
	{
		StoryContent content = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			conn = dataSource.getConnection();
			
			StringBuffer selectSql = new StringBuffer().append(" SELECT ")
													   .append(" * ")
													   .append(" FROM STORY_CONTENT ")
													   .append(" WHERE ID = ? ");
			
			ps = conn.prepareStatement(selectSql.toString());
			ps.setLong(1, contentId);
			
			rs = ps.executeQuery();
			
			content = convertResultSetToStoryContent(rs);
			
			rs.close();
			ps.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		
		return content;
	}
	
	public Story insertStory(Story story) throws SQLException
	{
		Story insertedStory = null;
		Connection conn = null;
		CallableStatement stmt = null;
		
		try
		{
			conn = dataSource.getConnection();
			
			// call stored procedure to insert
			StringBuffer insertSql = new StringBuffer().append(" { CALL STORY_INSERT(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) } ");
			
			stmt = conn.prepareCall(insertSql.toString());
			stmt.setString(1, story.getTitle());
			stmt.setString(2, story.getCity());
			stmt.setString(3, story.getCountry());
			stmt.setDate(4, new java.sql.Date(story.getStoryDate().getTime()));
			stmt.setString(5, story.getPublishing());
			stmt.setString(6, String.valueOf(story.isReady()));
			stmt.setDate(7, new java.sql.Date(story.getCreated().getTime()));
			stmt.setString(8, story.getLattitude());
			stmt.setString(9, story.getLongitude());
			stmt.setLong(10, story.getUser().getId());
			stmt.registerOutParameter(11, java.sql.Types.NUMERIC);
			stmt.execute();
			long storyId = stmt.getLong(11);
			
			insertedStory = new Story(storyId, 
									  story.getTitle(), 
									  story.getCity(), 
									  story.getCountry(), 
									  story.getStoryDate(), 
									  story.getPublishing(), 
									  story.isReady(), 
									  story.getCreated(),
									  story.getLongitude(),
									  story.getLattitude(),
									  story.getUser());
			
			List<StoryPage> insertedStoryPages = new ArrayList<StoryPage>();
			int index=1;
			for(StoryPage storyPage : story.getStoryPages())
			{
				// insert content
				StoryContent insertedStoryContent = insertStoryContent(storyId, storyPage.getStoryContent());
				
				// insert story page
				storyPage.setStoryContent(insertedStoryContent);
				storyPage.setIndex(index);				
				StoryPage insertedStoryPage = insertStoryPage(storyId, storyPage);
				if(insertedStoryPage!=null)
				{
					index++;
				}
				
				insertedStoryPages.add(insertedStoryPage);
			}
			
			insertedStory.setStoryPages(insertedStoryPages);
			
			stmt.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		
		return insertedStory;
	}
	
	public StoryPage insertStoryPage(long storyId, StoryPage storyPage) throws SQLException
	{
		StoryPage insertedStoryPage = null;
		Connection conn = null;
		CallableStatement stmt = null;
		
		try
		{
			conn = dataSource.getConnection();
			
			// call stored procedure to insert
			StringBuffer insertSql = new StringBuffer().append(" { CALL STORY_PAGE_INSERT(?, ?, ?, ?, ?) } ");
			
			stmt = conn.prepareCall(insertSql.toString());
			stmt.setLong(1, storyId);
			stmt.setLong(2, storyPage.getStoryContent().getId());
			stmt.setDate(3, new java.sql.Date(storyPage.getCreated().getTime()));
			stmt.setInt(4, storyPage.getIndex());
			
			stmt.registerOutParameter(5, java.sql.Types.NUMERIC);
			
			stmt.execute();
			long storyPageId = stmt.getLong(5);
			
			insertedStoryPage = new StoryPage(storyPageId, 
											  storyId, 
											  storyPage.getStoryContent(), 
											  storyPage.getCreated());
			
			stmt.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		
		return insertedStoryPage;
	}
	
	public StoryContent insertStoryContent(long storyId,
										   StoryContent storyContent) throws SQLException
	{
		StoryContent insertedStoryContent = null;
		Connection conn = null;
		CallableStatement stmt = null;
		
		try
		{
			conn = dataSource.getConnection();
			
			// call stored procedure to insert
			StringBuffer insertSql = new StringBuffer().append(" { CALL STORY_CONTENT_INSERT( ?, ?, ?, ?, ? ) } ");

			stmt = conn.prepareCall(insertSql.toString());
			stmt.setString(1, storyContent.getDescription());
			stmt.setString(2, storyContent.getType());
			stmt.setString(3, storyContent.getContent());
			stmt.setString(4, String.valueOf(storyContent.getNarrationAudio()));

			stmt.registerOutParameter(5, java.sql.Types.NUMERIC);

			stmt.execute();

			long storyContentId = stmt.getLong(5);

			insertedStoryContent = new StoryContent(storyContentId, 
													storyContent.getDescription(), 
													storyContent.getType(), 
													storyContent.getContent(),
													storyContent.getNarrationAudio());
			
			
			// delete temp media file and store it in permenant folder
			String contentType = storyContent.getType();
			if(!contentType.equals("TEXT"))
			{
				File tempFile = new File(servletContext.getRealPath(storyContent.getContent()));
				String fileName = tempFile.getName();
				String filePath = tempFile.getParent();
				
				// extract uuid from fileName
				String extension = fileName.substring(fileName.lastIndexOf('.'));
				fileName = storyId + "_" +
						   storyContentId + "_" + 
						   fileName.substring(0, fileName.lastIndexOf('_')) + extension;
				
				// get the permenant directory
				filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
				
				// store file in permenant directory
				FileInputStream fis = new FileInputStream(tempFile);
				FileOutputStream fos = new FileOutputStream(filePath+"\\"+fileName);
				byte[] buffer = new byte[1024];
				int length;
				
				while ((length = fis.read(buffer)) > 0) 
				{
					fos.write(buffer, 0, length);
	            }
				fis.close();
				fos.close();
				
				// update story content to refer to the new permenant media file location
				StringBuffer updateSql = new StringBuffer().append(" UPDATE STORY_CONTENT ")
														   .append(" SET CONTENT = ? ")
														   .append(" WHERE ID = ? ");
				
				PreparedStatement ustmt = conn.prepareStatement(updateSql.toString());
				
				String relativePath = storyContent.getContent();
				ustmt.setString(1, relativePath.substring(0,relativePath.indexOf("/temp")) + "/" + fileName);
				ustmt.setLong(2, storyContentId);
				
				ustmt.executeUpdate();
				
				storyContent.setContent(relativePath.substring(0,relativePath.indexOf("/temp")) + "/" + fileName);
				insertedStoryContent.setContent(relativePath.substring(0,relativePath.indexOf("/temp")) + "/" + fileName);
				
				// delete temp file
				tempFile.delete();
				
				ustmt.close();
			}
			
			// store narration audio
			if(CharUtils.toString(storyContent.getNarrationAudio()).equals("Y"))
			{
				String narrationAudioPath = storyContent.getDescription();
				
				File tempFile = new File(servletContext.getRealPath(narrationAudioPath));
				String fileName = tempFile.getName();
				String filePath = tempFile.getParent();
				
				// extract uuid from fileName
				String extension = fileName.substring(fileName.lastIndexOf('.'));
				fileName = storyId + "_" +
						   storyContentId + "_" + 
						   fileName.substring(0, fileName.lastIndexOf('_')) + extension;
				
				// get the permenant directory
				filePath = filePath.substring(0, filePath.lastIndexOf("\\"));
				
				// store file in permenant directory
				FileInputStream fis = new FileInputStream(tempFile);
				FileOutputStream fos = new FileOutputStream(filePath+"\\"+fileName);
				byte[] buffer = new byte[1024];
				int length;
				
				while ((length = fis.read(buffer)) > 0) 
				{
					fos.write(buffer, 0, length);
	            }
				fis.close();
				fos.close();
				
				// update story content to refer the description as narration/audio from permenant media file location
				StringBuffer updateSql = new StringBuffer().append(" UPDATE STORY_CONTENT ")
														   .append(" SET DESCRIPTION = ? ")
														   .append(" WHERE ID = ? ");
				
				PreparedStatement ustmt = conn.prepareStatement(updateSql.toString());
				
				String relativePath = narrationAudioPath;
				ustmt.setString(1, relativePath.substring(0,relativePath.indexOf("/temp")) + "/" + fileName);
				ustmt.setLong(2, storyContentId);
				
				ustmt.executeUpdate();
				
				storyContent.setDescription(relativePath.substring(0,relativePath.indexOf("/temp")) + "/" + fileName);
				insertedStoryContent.setDescription(relativePath.substring(0,relativePath.indexOf("/temp")) + "/" + fileName);
				
				// delete temp file
				tempFile.delete();
				
				ustmt.close();
			}
			
			stmt.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		
		return insertedStoryContent;
	}
	
	public String insertNewMediaFileForStoryContent(long storyId,
										   			long storyContentId,
										   			String tempPath)
	{
		String fileName = null;
		
		try
		{
			// delete temp media file and store it in permenant folder
			File tempFile = new File(servletContext.getRealPath(tempPath));
			fileName = tempFile.getName();
			String filePath = tempFile.getParent();

			// extract uuid from fileName
			String extension = fileName.substring(fileName.lastIndexOf('.'));
			fileName = storyId + "_" +
					storyContentId + "_" + 
					fileName.substring(0, fileName.lastIndexOf('_')) + extension;

			// get the permenant directory
			filePath = filePath.substring(0, filePath.lastIndexOf("\\"));

			// store file in permenant directory
			FileInputStream fis = new FileInputStream(tempFile);
			FileOutputStream fos = new FileOutputStream(filePath+"\\"+fileName);
			byte[] buffer = new byte[1024];
			int length;

			while ((length = fis.read(buffer)) > 0) 
			{
				fos.write(buffer, 0, length);
			}
			fis.close();
			fos.close();

			// delete temp file
			tempFile.delete();
		}
		catch(Exception e)
		{
			
		}

		return tempPath.substring(0,tempPath.indexOf("/temp"))+"/"+fileName;
	}
	
	public boolean updateStoryContent(StoryContent storyContent) throws SQLException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		boolean result = false;
		
		try
		{
			conn = dataSource.getConnection();
			
			StringBuffer updateSql = new StringBuffer().append(" UPDATE ")
													   .append(" STORY_CONTENT ")
													   .append(" SET ")
													   .append(" DESCRIPTION = ?, ")													    
													   .append(" TYPE = ?, ")													    
													   .append(" CONTENT = ?, ")													    
													   .append(" NARRATION_AUDIO = ? ")													    
													   .append(" WHERE ID = ? ");
			
			ps = conn.prepareStatement(updateSql.toString());
			ps.setString(1, storyContent.getDescription());
			ps.setString(2, storyContent.getType());
			ps.setString(3, storyContent.getContent());
			ps.setString(4, String.valueOf(storyContent.getNarrationAudio()));
			ps.setLong(5, storyContent.getId());
			
			ps.executeUpdate();
			ps.close();
			
			result = true;
		}
		catch(Exception e)
		{
			result = false;
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		
		return result;
	}
	
	public boolean updateStory(Story story) throws SQLException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		boolean result = false;
		
		try
		{
			conn = dataSource.getConnection();
			
			StringBuffer updateSql = new StringBuffer().append(" UPDATE ")
													   .append(" STORY ")
													   .append(" SET ")
													   .append(" TITLE = ?, ")													    
													   .append(" CITY = ?, ")													    
													   .append(" COUNTRY = ?, ")													    
													   .append(" STORY_DATE = ?, ")													    
													   .append(" LATTITUDE = ?, ")													    
													   .append(" LONGITUDE = ? ")													    
													   .append(" WHERE ID = ? ");
			
			ps = conn.prepareStatement(updateSql.toString());
			ps.setString(1, story.getTitle());
			ps.setString(2, story.getCity());
			ps.setString(3, story.getCountry());
			ps.setDate(4, new java.sql.Date(story.getStoryDate().getTime()));
			ps.setString(5, story.getLattitude());
			ps.setString(6, story.getLongitude());
			ps.setLong(7, story.getId());
			
			ps.executeUpdate();
			ps.close();
			
			result = true;
		}
		catch(Exception e)
		{
			result = false;
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		
		return result;
	}
	
	public boolean updateStoryPageIndex(long storyPageId, int pageIndex) throws SQLException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		boolean result = false;
		
		try
		{
			conn = dataSource.getConnection();
			
			StringBuffer updateSql = new StringBuffer().append(" UPDATE ")
					   								   .append(" STORY_PAGE ")
					   								   .append(" SET PAGE_INDEX = ? ")
					   								   .append(" WHERE ID = ? ");
			
			ps = conn.prepareStatement(updateSql.toString());
			ps.setInt(1, pageIndex);
			ps.setLong(2, storyPageId);
			ps.executeUpdate();
			ps.close();
			
			result = true;
		}
		catch(Exception e)
		{
			result = false;
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		
		return result;
	}
	
	public boolean deleteStoryPage(StoryPage storyPage) throws SQLException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		boolean result = false;
		
		try
		{
			// Delete Story Page
			conn = dataSource.getConnection();
			
			StringBuffer deleteSql = new StringBuffer().append(" DELETE ")
					   								   .append(" FROM STORY_PAGE ")
					   								   .append(" WHERE ID = ? ");
			
			ps = conn.prepareStatement(deleteSql.toString());
			ps.setLong(1, storyPage.getId());
			ps.executeUpdate();
			ps.close();
			
			boolean deleted = deleteStoryContent(storyPage.getStoryContent());
			if(!deleted)
			{
				return false;
			}
			
			result = true;
		}
		catch(Exception e)
		{
			result = false;
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		
		return result;
	}
	
	public boolean deleteStoryContent(StoryContent storyContent) throws SQLException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		boolean result = false;
		
		try
		{
			conn = dataSource.getConnection();
			
			StringBuffer deleteSql = new StringBuffer().append(" DELETE ")
													   .append(" FROM STORY_CONTENT ")
													   .append(" WHERE ID = ? ");
			
			ps = conn.prepareStatement(deleteSql.toString());
			ps.setLong(1, storyContent.getId());
			ps.executeUpdate();
			ps.close();
			
			String type = storyContent.getType();
			
			if(!type.equalsIgnoreCase("TEXT"))
			{
				String filePath = servletContext.getRealPath(storyContent.getContent());
				
				File file = new File(filePath);
				if(file.exists() && file.isFile())
				{
					result = file.delete();
				}
				else
				{
					result = false;
				}
			}
			else
			{
				result = true;
			}			
		}
		catch(Exception e)
		{
			result = false;
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		
		return result;
	}
	
	public boolean deleteStory(Story story) throws SQLException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		boolean result = false;
		
		try
		{
			// Delete Story Page
			conn = dataSource.getConnection();
			
			StringBuffer deleteSql = new StringBuffer().append(" DELETE ")
					   								   .append(" FROM STORY ")
					   								   .append(" WHERE ID = ? ");
			
			ps = conn.prepareStatement(deleteSql.toString());
			ps.setLong(1, story.getId());
			ps.executeUpdate();
			ps.close();
			
			result = true;
		}
		catch(Exception e)
		{
			result = false;
			e.printStackTrace();
		}
		finally
		{
			conn.close();
		}
		
		return result;
	}
	
	private Story convertResultSetToStory(ResultSet rs) throws SQLException 
	{
		rs.next();
		
		long id = rs.getLong("ID");
		String title = rs.getString("TITLE");
		String city = rs.getString("CITY");
		String country = rs.getString("COUNTRY");
		Date storyDate = rs.getDate("STORY_DATE");
		String publishing = rs.getString("PUBLISHING");
		char ready = rs.getString("READY").charAt(0);
		Date created = rs.getDate("CREATED");
		String lattitude=rs.getString("LATTITUDE");
		String longitude=rs.getString("LONGITUDE");
		long userId = rs.getLong("USERID");
		
		Story story = new Story(id, title, city, country, storyDate, publishing, ready, created,lattitude,longitude, userDao.getUserById(userId));
		return story;
	}
	
	private StoryPage convertResultSetToStoryPage(ResultSet rs) throws SQLException
	{
		long id = rs.getLong("ID");
		long storyId = rs.getLong("STORY_ID");
		StoryContent content = getContent(rs.getLong("CONTENT_ID"));
		Date created = rs.getDate("CREATED");
		
		StoryPage storyPage = new StoryPage(id, storyId, content, created);
		
		return storyPage;
	}
	
	private StoryContent convertResultSetToStoryContent(ResultSet rs) throws SQLException
	{
		long id = rs.getLong("ID");
		String description = rs.getString("DESCRIPTION");
		String type = rs.getString("TYPE");
		String content = rs.getString("CONTENT");
		char narrationAudio = rs.getString("NARRATION_AUDIO").charAt(0);
		
		StoryContent storyContent = new StoryContent(id, description, type, content, narrationAudio);
		
		return storyContent;
	}	
	
	private List<Story> convertResultSetToStoryList(ResultSet rs) throws SQLException 
	{
		List<Story> storyList=new ArrayList<Story>();
		while (rs.next()) {
		    long id = rs.getLong("ID");
		    String title = rs.getString("TITLE");
		    String city = rs.getString("CITY");
		    String country = rs.getString("COUNTRY");
		    Date storyDate = rs.getDate("STORY_DATE");
		    String publishing = rs.getString("PUBLISHING");
		    char ready = rs.getString("READY").charAt(0);
		    Date created = rs.getDate("CREATED");
		    String lattitude=rs.getString("LATTITUDE");
		    String longitude=rs.getString("LONGITUDE");
		    long userId = rs.getLong("USERID");
		    
		    List<StoryPage>  storyPageList=getStoryPagesByStoryId(id);
		    Story story = new Story(id, title, city, country, storyDate, publishing, ready, created,lattitude,longitude, userDao.getUserById(userId));
		    SimpleDateFormat df = new SimpleDateFormat("yyyy");
		    story.setStoryPages(storyPageList);
		    story.setYear(df.format(storyDate));
		    storyList.add(story);
		}
		return storyList;
	}
	
	private List<StoryPage> convertResultSetToStoryPageList(ResultSet rs) throws SQLException 
	{
		List<StoryPage> storyPageList=new ArrayList<StoryPage>();
		while (rs.next()) {
		    long id = rs.getLong("ID");
		    long story_id = rs.getLong("STORY_ID");
		    long story_content_id = rs.getLong("STORY_CONTENT_ID");
		    Date created = rs.getDate("CREATED");
		    StoryContent storyContent =getStoryContentById(story_content_id);
		    StoryPage storyPage=new StoryPage(id, story_id, storyContent, created);
		    storyPageList.add(storyPage);
		}
		return storyPageList;
	}
	
	

	private List<StoryContent> convertResultSetToStoryContentList(ResultSet rs) throws SQLException 
	{
		List<StoryContent> storyContentList=new ArrayList<StoryContent>();
		while (rs.next()) {
		    long id = rs.getLong("ID");
		    String description = rs.getString("DESCRIPTION");
		    String type= rs.getString("TYPE");
		    String content= rs.getString("CONTENT");
		    char narrationAudio = rs.getString("NARRATION_AUDIO").charAt(0);
		    
		    StoryContent storyContent=new StoryContent(id,description,type,content, narrationAudio);
		    storyContentList.add(storyContent);
		}
		return storyContentList;
	}
}
