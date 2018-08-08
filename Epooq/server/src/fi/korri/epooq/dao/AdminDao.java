package fi.korri.epooq.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import fi.korri.epooq.model.MapImage;
import fi.korri.epooq.model.Story;

@Repository
public class AdminDao 
{
	@Resource
	private DataSource dataSource;
	
	// insert map image
	public MapImage insertMapImage(MapImage mapImage) throws SQLException
	{
		MapImage mi = mapImage;
		Connection conn = null;
		CallableStatement stmt = null;
		
		try
		{
			conn = dataSource.getConnection();
			
			// call stored procedure to insert
			StringBuffer insertSql = new StringBuffer().append(" { CALL MAP_IMAGE_INSERT(?, ?, ?, ?, ?, ?, ?, ?, ?) } ");
			
			stmt = conn.prepareCall(insertSql.toString());
			
			stmt.setString(1, mapImage.getStartLat());
			stmt.setString(2,  mapImage.getStartLong());
			stmt.setString(3,  mapImage.getEndLat());
			stmt.setString(4, mapImage.getEndLong());
			stmt.setString(5, mapImage.getFile());
			stmt.setString(6, "Y");
			stmt.setInt(7, mapImage.getLevel());
			stmt.setString(8, mapImage.getTitle());
			stmt.registerOutParameter(9, java.sql.Types.NUMERIC);
			stmt.execute();
			long mapImageId = stmt.getLong(9);
			
			mi.setId(mapImageId);
			
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
		
		return mi;
	}
	
	// get all map image for list
	public List<MapImage> getAllMapImages() throws SQLException
	{
		List<MapImage> mapImageList = new ArrayList<MapImage>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			conn = dataSource.getConnection();
			String selectSql="select * from MAP_IMAGE where enable_flag = 'Y'";
			ps = conn.prepareStatement(selectSql);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				MapImage mi = new MapImage();
				mi.setId(rs.getLong("id"));
				mi.setStartLat(rs.getString("start_lat"));
				mi.setStartLong(rs.getString("start_long"));
				mi.setEndLat(rs.getString("end_lat"));
				mi.setEndLong(rs.getString("end_long"));
				mi.setFile(rs.getString("file"));
				mi.setEnableFlag(StringUtils.equals(rs.getString("enable_flag"),"Y") ? true : false);
				mi.setLevel(rs.getInt("level"));
				mi.setFile(rs.getString("file"));
				mi.setTitle(rs.getString("title"));
				
				mapImageList.add(mi);
			}
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
		
		return mapImageList;
		
	}
	
	public List<MapImage> getAllMapImages(int offset,
										  int rows) throws SQLException
	{
		List<MapImage> mapImageList = new ArrayList<MapImage>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			conn = dataSource.getConnection();
			String selectSql="select * from MAP_IMAGE limit " + offset + ", " + rows;
			ps = conn.prepareStatement(selectSql);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				MapImage mi = new MapImage();
				mi.setId(rs.getLong("id"));
				mi.setStartLat(rs.getString("start_lat"));
				mi.setStartLong(rs.getString("start_long"));
				mi.setEndLat(rs.getString("end_lat"));
				mi.setEndLong(rs.getString("end_long"));
				mi.setFile(rs.getString("file"));
				mi.setEnableFlag(StringUtils.equals(rs.getString("enable_flag"),"Y") ? true : false);
				mi.setLevel(rs.getInt("level"));
				mi.setFile(rs.getString("file"));
				mi.setTitle(rs.getString("title"));
				
				mapImageList.add(mi);
			}
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
		
		return mapImageList;
		
	}
	// get no. of map images 
	public int getAllMapImageCount() throws SQLException
	{
		int count = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			conn = dataSource.getConnection();
			String selectSql="select count(*) as image_count from MAP_IMAGE";
			ps = conn.prepareStatement(selectSql);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				count = rs.getInt("image_count");
			}
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
		
		return count;
		
	}
	// get map image by id
	public MapImage getMapImage(long id) throws SQLException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		MapImage mi = new MapImage();
		
		try
		{
			conn = dataSource.getConnection();
			String selectSql="select * from MAP_IMAGE where ID = " + id;
			ps = conn.prepareStatement(selectSql);
			rs = ps.executeQuery();
			
			while(rs.next())
			{
				mi.setId(rs.getLong("id"));
				mi.setStartLat(rs.getString("start_lat"));
				mi.setStartLong(rs.getString("start_long"));
				mi.setEndLat(rs.getString("end_lat"));
				mi.setEndLong(rs.getString("end_long"));
				mi.setFile(rs.getString("file"));
				mi.setEnableFlag(StringUtils.equals(rs.getString("enable_flag"),"Y") ? true : false);
				mi.setLevel(rs.getInt("level"));
				mi.setFile(rs.getString("file"));
				mi.setTitle(rs.getString("title"));
				
			}
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
		
		return mi;
	}
	// update map image
	public boolean updateMapImage(MapImage mapImage) throws SQLException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		boolean result = false;
		
		try
		{
			conn = dataSource.getConnection();
			
			StringBuffer updateSql = new StringBuffer().append(" UPDATE ")
													   .append(" 	MAP_IMAGE ")
													   .append(" SET ")
													   .append(" 	START_LAT = ?, ")													    
													   .append(" 	START_LONG = ?, ")													    
													   .append(" 	END_LAT = ?, ")													    
													   .append(" 	END_LONG = ?, ")													    
													   .append(" 	FILE = ?, ")													    
													   .append(" 	ENABLE_FLAG = ?, ")													    
													   .append(" 	LEVEL = ?, ")													    
													   .append(" 	TITLE = ? ")													    
													   .append(" WHERE ID = ? ");
		
			ps = conn.prepareStatement(updateSql.toString());
			ps.setString(1, mapImage.getStartLat());
			ps.setString(2, mapImage.getStartLong());
			ps.setString(3, mapImage.getEndLat());
			ps.setString(4, mapImage.getEndLong());
			ps.setString(5, mapImage.getFile());
			ps.setString(6, (mapImage.isEnableFlag() ? "Y" : "N"));
			ps.setInt(7, mapImage.getLevel());
			ps.setString(8,  mapImage.getTitle());
			ps.setLong(9, mapImage.getId());			
			
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
	
	// delete map image
	public boolean deleteMapImage(long id) throws SQLException
	{
		Connection conn = null;
		PreparedStatement ps = null;
		boolean result = false;
		
		try
		{
			conn = dataSource.getConnection();
			
			StringBuffer deleteSql = new StringBuffer().append(" DELETE FROM ")
													   .append(" 	MAP_IMAGE ")
													   .append(" WHERE ID = ? ");
			
			ps = conn.prepareStatement(deleteSql.toString());
			ps.setLong(1, id);			
			
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
	
}
