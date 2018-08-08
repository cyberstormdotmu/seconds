<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User Login</title>
</head>
<script type="text/javascript">
	function menu(){
		alert("Sushant6");
		}
</script>
<body>

<form:form action="${pageContext.request.contextPath}/user/authenticate.htm" method="post">
	<a href='javascript:;' onclick='menu();'>More </a>
	
	<table style="border: thin;">
		<tr>
			<td><label>Username</label></td>
			<td><input type="text" name="userName"/></td>
		</tr>
		<tr>
			<td><label>Password</label></td>
			<td><input type="password" name="password"/></td>
		</tr>
		<tr>
			<td><input name="submit" type="submit" value="Login" /></td>
		</tr>
		<tr>
			<td>
				<a href="${pageContext.request.contextPath}/user/signup.htm">Sign up here</a>
			</td>
		</tr>
	</table>
	
</form:form>
</body>
</html>