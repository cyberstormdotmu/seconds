<%@page import="com.tatva.model.User"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>
	<%
		User user=(User)request.getSession().getAttribute("user");
	%>
	<center>
		<h1>Home Page..........
		Welcome <%=user.getFirstName() %></h1>
	</center>
</body>
</html>