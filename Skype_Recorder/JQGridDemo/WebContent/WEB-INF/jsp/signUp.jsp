<%@page import="java.util.Locale"%>
<%@page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<html>
<head>
<title></title>
</head>
<body>
<p align="right" style="margin-right: 10px;">
Language : <a href="./signUp?lang=en">English</a>|<a href="./signUp?lang=zh_CN">China</a>
</p>
<center>
	<h1>Registration Form</h1>
	
	<form:form action="registerUser.html" method="post" modelAttribute="user">
	
		<table>
			<tr>
					<td><form:label path="userName"><spring:message code="label.username"/></form:label></td>
					<td><form:input path="userName" /></td>
					<td><form:errors path="userName" /></td>
			</tr>
			<tr>
				<td><form:label path="firstName"><spring:message code="label.firstname"/></form:label></td>
				<td><form:input path="firstName"/> </td>
				<td><form:errors path="firstName" /></td>
			</tr>
			<tr>
					<td><form:label path="lastName"><spring:message code="label.lastname"/></form:label></td>
					<td><form:input path="lastName" /></td>
					<td><form:errors path="lastName" /></td>
			</tr>
			
			<tr>
					<td><form:label path="roleId"><spring:message code="label.roleid"/></form:label></td>
					<td><form:input path="roleId" /></td>
					<td><form:errors path="roleId" /></td>
			</tr>
			
			<tr>
				<td><form:label path="password"><spring:message code="label.password"/></form:label></td>
				<td><form:password path="password"/></td>
				<td><form:errors path="password" /></td>
			</tr>
			
			<tr>
					<td><form:label path="active"><spring:message code="label.active"/></form:label></td>
					<td>
						<form:radiobutton path="active" value="T"/>True 
						<form:radiobutton path="active" value="F"/>False
					</td>
					<td><form:errors path="active" /></td>
			</tr>
			
			<tr>
					<td> <input type="submit" value="ADD"> </td>
					<td><input type="reset" value="CLEAR"></td>
			</tr>
		</table>
	</form:form>
	
	</center>
</body>
</html>