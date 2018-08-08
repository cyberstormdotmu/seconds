<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	
	<center>
		<h1>Spring Multiple File Upload example</h1>
		
		<p>Following files are uploaded successfully.</p>
		<ol>
			<c:forEach items="${files}" var="file">
				<li>${file}</li>
			</c:forEach>
		</ol>
	</center>
</body>
</html>