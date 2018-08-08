<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.StringWriter"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isErrorPage="true" language="java"
	contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error</title>
<link href="${pageContext.request.contextPath}/resources/css/style3.css" rel="stylesheet" type="text/css" />
</head>
<body style="text-align: center;">
	<h1>Oops...</h1>
	<div style="width: 600px; margin-left: auto; margin-right: auto; ">
	<table width="100%" border="1">
		<tr valign="top">
			<td width="40%"><b>Error:</b></td>
			<td>${pageContext.exception}</td>
		</tr>
		<tr valign="top">
			<td><b>URI:</b></td>
			<td>${pageContext.errorData.requestURI}</td>
		</tr>
		<tr valign="top">
			<td><b>Status code:</b></td>
			<td>${pageContext.errorData.statusCode}</td>
		</tr>
		<tr valign="top">
			<td><b>Stack trace:</b></td>
			<td><c:forEach var="trace"
					items="${pageContext.exception.stackTrace}">
					<p>${trace}</p>
				</c:forEach></td>
		</tr>
		<tr>
			<td colspan="2"><a href="${pageContext.request.contextPath}/home.html">Go Home</a></td>
		</tr>
	</table>
	</div>
</html>