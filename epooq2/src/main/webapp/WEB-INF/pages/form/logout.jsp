 <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Epooq</title>
<link href="${pageContext.request.contextPath}/resources/css/form/form_style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<form:form action="${pageContext.request.contextPath}/user/loginMain.html" method="POST">
	<div class="boxpanel logoutwindow">
	<h1 id="popup_page"><spring:message code="logout.message"/><a href="${pageContext.request.contextPath}/home.html" class="close"></a></h1>
	<div class="box-content" onclick="closeError()">
		<div class="row"></div>
		<div class="row"><p><spring:message code="logout.messageOne"/></p></div>
		<div class="row"></div>
		<div class="row"><P><spring:message code="logout.messageTwo"/></P></div>
	    <div class="row"></div>
	    <div class="row" style="margin-left: 31%"><input name="" type="submit" class="inputbutton" value="<spring:message code="logout.login_again"/>" /></div>
	    <div class="bottomrow"></div>
	</div>
	</div>
</form:form>
</body>
</html>