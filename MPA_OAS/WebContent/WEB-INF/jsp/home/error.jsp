<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script>
function goBack()
 {
 	 window.history.back();
 }
</script>
</head>
<body>
	<div align="center" style="margin: 90px 10px 10px 10px;">
	<center>
		<img alt="Error Here" src="../images/error.jpg"><br><br>
		<h3>Sorry, Something goes wrong ...</h3>
		<h3>We can Understand your risks..</h3>
		<h4>Our team is working hard to fix this..We will be back to you shortly..!</h4>
		<h3>Thank You..!</h3>

		<br/><br/>
 		<a  onclick="goBack()" style="font-size: 16px ; cursor: pointer; ">Go Back</a><br/>
 		<a href="${pageContext.request.contextPath}/login/homePage.mpa" style=" font-size:18px;cursor: pointer;">Return To Home Page</a>
		
	</center>
	</div>
</body>
</html>