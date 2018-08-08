<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>
	<tiles:insertAttribute name="title"/>
</title>
<%-- <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" /> --%>

	<style type="text/css">
body
{
	margin: auto;
	padding: auto;
	width:70%;
	font:12px verdana;
	color:#515151;
}

.header
{
	margin: 0px;
	padding: 0px;
	height: 50px;
	background-color: #B8DBFF;
	margin-top: 10px;
}

.menu
{
	margin: 0px;
	padding: 0px;
	background-color: #E6BEF3;
	display: inline-block;
	float: left;
	height:400px;
}

.content
{

	margin: 0px;
	padding: 0px;
	background-color: #D4D4C9;
	height: 400px;
	overflow: auto;
}

.leftmenu
{
	margin: 0px;
	padding: 0px;
	width: 300px;
	padding-left:0px;
	padding-top: 10px;
	background-color:#E6BEF3;
	float: left;
}

.bodyContent
{
	text-align: left;
	height: auto;
}

.footer
{
	margin: 0px;
	padding: 0px;
	text-align: center;
	background-color:#FFFF82;
	height:50px; 
}

.menuUl
{
	display: inline-block;
}
		
	
	</style>

</head>
<body>

	<div class="main">
	
		<div class="header">
			<tiles:insertAttribute name="header"/>		
		</div>
		
		
		<div class="content">
			
			
			<div class="menu">
				<tiles:insertAttribute name="menu"/>
			</div>
			
			<div class="bodyContent">
				<tiles:insertAttribute name="body"/>
			</div>
		
		</div>
			
		<div class="footer">
			<tiles:insertAttribute name="footer"/>
		</div>
	</div>
	
</body>
</html>