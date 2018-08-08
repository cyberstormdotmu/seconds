<%@page import="org.springframework.ui.Model"%>
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
<title>Login</title>

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
  
  <%	
  		request.getAttribute("error");
  %>
  
	<c:if test="${not empty error}">
  		<div class="alert alert-danger" id="logindiv"> <strong>${error}</strong> ${msg}</div>
  	</c:if>  
  
	<form:form name ="loginForm" method="post" action="authenticate" modelAttribute="userLogin">
        
        
        
        <div class="input-group">
          <span class="input-group-addon"><span class="glyphicon glyphicon-user"></span></span>
          <form:input path="email" id="email" class="form-control" placeholder="Email ID" />
        </div>
        <div class="input-group">
          <span class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></span>
          <form:input path="password" id="password" type="password" class="form-control" placeholder="Password" />
        </div>
        
        <input type="hidden" value="${referer}" name="referer">

        <div class="loginbtn">
        	<input name="Login" type="submit" class="btn btn-primary" value = "Login" onclick="return validLogin();"> <a href="updatepassword">I forgot my password</a> 
        </div>
        <h3>Register</h3>
        <p>In order to login you must be registered. Registering takes only a few moments but gives you increased capabilities. The board administrator may also grant additional permissions to registered users. Before you register please ensure you are familiar with our terms of use and related policies. Please ensure you read any forum rules as you navigate around the board.</p>
        <br>
        <a href="terms-condition"> Terms & Conditions</a> | <a href="privacy-policies">Privacy Statement</a>
        <br><br>
  		
    </form:form>
  		<form:form method="post" action="register">
  			<input name="Register" type="submit" class="btn btn-primary" value="Register" >
  			<a href="dashboard"><input name="Forum Dashboard" type="button" class="btn btn-primary" value="Forum Dashboard" ></a>
  		</form:form>
  		<form:form method="post" action="dashboard">
  			
  		</form:form>  		
  		
  </div>
  
</div>
<script src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>

<!-- Validation for Login -->
	<script>  
		function validLogin(){
			var email = document.loginForm.email.value
			var password = document.loginForm.password.value
			var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			
			if (!filter.test(email) || email == "") {  
				  
				alert("Please enter a valid e-mail address."); 
				return false;
				  	  
			} else if(6 > password.length || password.length > 100) {  
			  
				alert("Password Must be between 6 characters and 100 characters.");  
				return false;
			}else {
				return true;
			}
			
		}
	
	
	</script>
</body>
</html>