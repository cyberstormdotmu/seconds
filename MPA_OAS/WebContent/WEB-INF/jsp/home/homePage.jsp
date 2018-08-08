<%@page import="com.tatva.utils.MPAContext"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Maritime Port Authority</title>
</head>
<body>
<center>
		
		<%
			if(request.getAttribute("temp")!=null){
		%>
			<h1 style="padding-top: 50px;">You Have Successfully Logged Out...</h1>
		<%
			}else{
		%>	
	
		<h1 style="padding-top: 50px;">Welcome <% if(session.getAttribute(MPAContext.currentUser)==null) %>Guest <% else %><%=session.getAttribute("UserNameTemp")%></h1>
		<%
			}
		%>
		</center>
</body>
</html>