<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>
	<br><br><br>
<center>
	<c:if test="${not empty user }">
		<form:form modelAttribute="user" action="/updateUser.html" method="post">
			<table>
				<tr>
					<td><form:label path="userId">User Id</form:label></td>
					<td><form:input path="userId" readonly="true"/></td>
				</tr>
				
				<tr>
					<td><form:label path="userName">User Name</form:label></td>
					<td><form:input path="userName" /></td>
				</tr>
				
				<tr>
					<td><form:label path="firstName">First Name</form:label></td>
					<td><form:input path="firstName" /></td>
				</tr>
				
				<tr>
					<td><form:label path="lastName">Last Name</form:label></td>
					<td><form:input path="lastName" /></td>
				</tr>
				
				<tr>
					<td><form:label path="roleId">Role Id</form:label></td>
					<td><form:input path="roleId" /></td>
				</tr>
				
				<tr>
					<td><form:label path="active">Is Active</form:label></td>
					<td>
						<form:radiobutton path="active" value="T"/>True 
						<form:radiobutton path="active" value="F"/>False
					</td>
				</tr>
				
				<tr>
					<td> <input type="submit" value="Update"> </td>
				</tr>
			</table>		
		</form:form>
	</c:if>
	
	<a href="${pageContext.request.contextPath}/returnHome.html">Home Page</a>
</center>
</body>
</html>