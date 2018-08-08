package fi.korri.epooq.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fi.korri.epooq.model.Story;
import fi.korri.epooq.model.User;

@Repository
public class UserDao {

	@Resource
	private DataSource dataSource;
	
	@Autowired
	ServletContext servletContext;

	// get user who is logged in
	public User getLogin(User user){
		
		try
		{
			Connection conn = dataSource.getConnection();
			
			

			
			StringBuffer selectSql = new StringBuffer().append(" SELECT ")
													   .append(" * ")
													   .append(" FROM user ")
													   .append(" WHERE email = ? and password = ?");
			
			
			PreparedStatement preparedStatement = conn.prepareStatement(selectSql.toString());
			
			preparedStatement.setString(1, user.getEmail());
			preparedStatement.setString(2, user.getPassword());
			ResultSet rs = preparedStatement.executeQuery();
			List<User> userList=convertResultSetToUserList(rs);
			if(userList.size()>0){
				   return userList.get(0);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}
	
	// save registered user
	public boolean saveUser(User user){
		try {
			Connection conn = dataSource.getConnection();

			String insertTableSQL = "INSERT INTO user"
					+ "(firstName, email,birthDate,password) VALUES"
					+ "(?,?,?,?)";
			PreparedStatement preparedStatement= conn.prepareStatement(insertTableSQL);
			
			preparedStatement.setString(1, user.getFirstName());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.setDate(3, new java.sql.Date(user.getBirthDate().getTime()));
			preparedStatement.setString(4, user.getPassword());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// update password
	public boolean updateUser(User user){
		try {
			Connection conn = dataSource.getConnection();

			StringBuffer updateSql = new StringBuffer().append(" UPDATE user ")
					   									.append(" SET PASSWORD = ? ")
					   									.append(" WHERE email = ? ");
			PreparedStatement preparedStatement= conn.prepareStatement(updateSql.toString());
			preparedStatement.setString(1, user.getPassword());
			preparedStatement.setString(2, user.getEmail());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	// get user by mail id
	public User getUserByEmail(User user){
		User userObj=null;
		try
		{
			Connection conn = dataSource.getConnection();
	
			StringBuffer selectSql = new StringBuffer().append(" SELECT ")
													   .append(" * ")
													   .append(" FROM user ")
													   .append(" WHERE email = ? ");
			
			PreparedStatement ps = conn.prepareStatement(selectSql.toString());
			ps.setString(1, user.getEmail());
			ResultSet rs = ps.executeQuery();
			List<User> userList=convertResultSetToUserList(rs);
			if(userList.size()>0)
			userObj=userList.get(0);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return userObj;
	}
	
	// get user by id
	public User getUserById(long userId) throws SQLException{
		
		User user = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{
			conn = dataSource.getConnection();
			
			StringBuffer selectSql = new StringBuffer().append(" SELECT ")
													   .append(" * ")
													   .append(" FROM user ")
													   .append(" WHERE id = ? ");
			
			ps = conn.prepareStatement(selectSql.toString());
			ps.setLong(1, userId);
			
			rs = ps.executeQuery();
			user = convertResultSetToUserList(rs).get(0);
			
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
		
		return user;
	}
	
	// convert resultset to user object
	private List<User> convertResultSetToUserList(ResultSet rs) throws SQLException 
	{
		List<User> userList=new ArrayList<User>();
		while (rs.next()) {
		    long id = rs.getLong("ID");
		    String firstName = rs.getString("FIRSTNAME");
		    String email = rs.getString("EMAIL");
		    Date birthDate = rs.getDate("BIRTHDATE");
		    String password = rs.getString("PASSWORD");
		    
		    User user = new User(id, firstName, email, birthDate, password);
		    userList.add(user);
		}
		return userList;
	}

	
	
}
