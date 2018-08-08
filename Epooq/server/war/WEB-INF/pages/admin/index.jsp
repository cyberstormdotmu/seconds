<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head></head>
<body>
	
	<fieldset>
		<legend>Yhteisöt</legend>
		<ul>
		<c:forEach items="${communities}" var="community">
			<li>
				<img src="../picture.html?id=${community.pictureId}" alt="${community.name}" width="100" height="130">
				${community.name}
	
				<form:form action="deleteCommunity.html" method="post">
					<input type="hidden" name="community_id" value="${community.id}">
					<input type="submit" value="Poista">
				</form:form>
			</li>
		</c:forEach>
		</ul>
	</fieldset>
	
	<fieldset>
		<legend>Lisää yhteisö</legend>
		<form:form action="addCommunity.html" method="post" enctype="multipart/form-data">
			
			<table>
				<tr>
					<td><label for="community-name">Otsikko</label></td>
					<td><input type="text" id="community-name" name="name"></td>
				</tr>
				
				<tr>
					<td><label for="community-picture">Kuva</label></td>
					<td><input type="file" id="community-picture" name="file"></td>
				</tr>
				
				<tr>
					<td></td>
					<td><input type="submit" value="Tallenna"></td>
				</tr>
			</table>
		</form:form>
	</fieldset>
	
	<fieldset>
		<legend>Asetukset</legend>
		<form:form action="settings.html" method="post" enctype="multipart/form-data">
			
			<table>
				<tr>
					<td><label for="test-user-birth-year">Testikäyttäjän syntymävuosi</label></td>
					<td><input type="text" id="test-user-birth-year" name="birthYear" value="${settings.birthYear}"></td>
				</tr>
				
				<tr>
					<td></td>
					<td><input type="submit" value="Tallenna"></td>
				</tr>
			</table>
		</form:form>
	</fieldset>
	
	<fieldset>
		<legend>Avaimet</legend>
		<ul>
		<c:forEach items="${keys}" var="key">
			<li>
				<c:choose>
  					<c:when test="${key.pictureId} != null">
						<img src="../picture.html?id=${key.pictureId}" alt="${key.question}" width="100" height="130">
  					</c:when>
				</c:choose>
				${key.question} - (Yhteisö id: ${key.communityId}) 
	
				<form:form action="deleteKey.html" method="post">
					<input type="hidden" name="key_id" value="${key.id}">
					<input type="submit" value="Poista">
				</form:form>
			</li>
		</c:forEach>
		</ul>
	</fieldset>
	
	<fieldset>
		<legend>Lisää avain</legend>
		<form:form action="addKey.html" method="post" enctype="multipart/form-data">
			
			<table>
				<tr>
					<td><label for="key-question">Kysymys</label></td>
					<td><input type="text" id="key-question" name="question"></td>
				</tr>

				<tr>
					<td><label for="key-age">Ikävuosi</label></td>
					<td><input type="text" id="key-age" name="age"></td>
				</tr>
				
				<tr>
					<td><label for="key-picture">Kuva</label></td>
					<td><input type="file" id="key-picture" name="file"></td>
				</tr>
				
				<tr>
					<td><label for="key-community">Yhteisö</label></td>
					<td>
						<select id="key-community" name="community_id">
							<option value="-1">Julkinen</option>
						<c:forEach items="${communities}" var="community">
							<option value="${community.id}">${community.name}</option>
						</c:forEach>
						</select>
					</td>
				</tr>				
				
				<tr>
					<td></td>
					<td><input type="submit" value="Tallenna"></td>
				</tr>
			</table>
		</form:form>
	</fieldset>

</body>
</html>