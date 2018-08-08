package fi.korri.epooq.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable{
	
	
	private long id;
	private String firstName;
	private String email;
	private Date birthDate;
	private String password;
	
	
	public User() {
	}
	
	public User(long id,
			 String firstName, 
			 String email, 
			 Date birthDate, 
			 String password) 
{
	this.id = id;
	this.firstName = firstName;
	this.email = email;
	this.birthDate = birthDate;
	this.password = password;
}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Date getBirthDate() {
		return birthDate;
	}


	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

}
