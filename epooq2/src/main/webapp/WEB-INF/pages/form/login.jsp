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
<script type="text/javascript">
		
	$(document).ready(function(){
		 if($("#errorMessage").val().trim()!=""){
				$(".msgbg").show();
				$("#error_msg").html("");
				$("#error_msg").append($("#errorMessage").val());
		 }
		 if($("#emailIdHidden").val().trim()!=""){
			$("#email").val($("#emailIdHidden").val());	
		 }
	});

	function closeError(){
		$(".msgbg").hide();
	}
	
	
	function validation(){
		email= document.getElementById("email");
		password= document.getElementById("email");
	
		var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		if( !(regex.test( email.value )) ) {
			$(".msgbg").show();
			$("#error_msg").html("");
			$("#error_msg").append("<spring:message code="invalid.email"/>");
		    /* $(".msg_error").append("Invalid EmailId");*/
		    return false;     
		}else if( password.value.trim()=="" ) {
			$(".msgbg").show();
			$("#error_msg").html("");
			$("#error_msg").append("<spring:message code="login.enter_password"/>");
			return false;
		}
		return true;
	}
</script>
</head>
<body>
<form:form action="${pageContext.request.contextPath}/user/login.html" method="POST" onsubmit="return validation();">
	<div class="boxpanel loginwindow">
	<h1 id="popup_page"><spring:message code="login.message" /><a href="${pageContext.request.contextPath}/home.html" class="close"></a></h1>
	<div class="box-content" onclick="closeError()">
	    <div class="row">
	 		<div class="msgbg" style="display: none;">
	              <div class="msg_left">
	                <div class="msg_right">
	                  <div class="msg_error"><img src="${pageContext.request.contextPath}/resources/img/form/ic_error.png" alt="" onclick="closeError()"/><span id="error_msg"></span></div>
	                </div>
	              </div>
	    	</div>  
 		</div>
		<div class="row"><label class="full"><spring:message code="forgot.email_address"/></label><form:input path="email" class="large" /></div>
	    <div class="row"><label class="full"><spring:message code="login.password"/></label><form:password path="password" class="large"/></div>
	    <!-- <div class="row"><label class="chk"><input name="" type="checkbox" value="" />Remember me</label></div> -->
	    <div class="row"><input name="" type="submit" class="inputbutton" value="Login" />
	    <a href="${pageContext.request.contextPath}/user/forgotPasswordMain.html" class="forget"><spring:message code="forgot.message" />?</a></div>
	    <div class="bottomrow"><p><spring:message code="newUser.message"/>&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/user/registrationMain.html"><spring:message code="register.message"/></a></p></div>
	</div>
	</div>
</form:form>
</body>
</html>