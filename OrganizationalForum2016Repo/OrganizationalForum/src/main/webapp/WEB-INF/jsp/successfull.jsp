<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>successfull</title>

<link  href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" rel="stylesheet"/>
<link href="<%=request.getContextPath()%>/resources/css/style.css" rel="stylesheet">

<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
<script src="<%=request.getContextPath()%>/resources/js/jquery.min.js"></script>

</head>
<body class="loginbg">
<div id="login">
  <header class="main-header">
      <h1>Login</h1>
    </header>    
  
  <div class="login-body">
  	<form:form method="post" action="loginRedirect">
	  	<div class="alert alert-success"> <strong> Success !</strong> Password has been successfully sent on your Email ID. </div>
	        <div class="loginbtn">
	        	<input name="Login" type="submit" class="btn btn-primary" value = "Login" > 
	    </div>
	</form:form>         
  </div>
  
</div>
<script src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
</body>
</html>