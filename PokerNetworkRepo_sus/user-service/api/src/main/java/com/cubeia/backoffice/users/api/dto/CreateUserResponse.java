/**
 * Copyright (C) 2010 Cubeia Ltd <info@cubeia.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.cubeia.backoffice.users.api.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Create user response.
 * @author w
 *
 */
@XmlRootElement(name="UserCreationResult")
public class CreateUserResponse {

	private CreationStatus status;
	private User user;
	
	/**
	 * Creation status.
	 * @return status
	 */
	@XmlElement()
	public CreationStatus getStatus() {
		return status;
	}
	
	public void setStatus(CreationStatus status) {
		this.status = status;
	}
	
	/**
	 * The user. 
	 * @return user
	 */
	@XmlElement(name="user")
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}