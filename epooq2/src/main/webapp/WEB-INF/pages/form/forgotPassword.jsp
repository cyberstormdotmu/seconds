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
				$(".msgbgError").show();
				$("#error_msg").append($("#errorMessage").val());
		 }
		 if($("#successMessage").val().trim()!=""){
			 $(".msgbgSuccess").show();
			 $("#successs_msg").append($("#successMessage").val());
		 }
			 
	});
	
	

	function checkEmail(){
			$("#error_msg").html("");
		email= document.getElementById("email");
		confirmEmail= document.getElementById("confirmEmail");
		
		var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		if( !(regex.test( email.value )) ) {
			$(".msgbgError").show();
			$("#error_msg").append("<spring:message code="invalid.email"/>");
		    return false;     
		}
		
		return true;
	}
	
	function closeError(){
		$(".msgbg").hide();
	}

	
</script>
</head>
<body>
<form:form action="${pageContext.request.contextPath}/user/forgotPassword.html" method="POST" onsubmit="return checkEmail();">
	<div class="boxpanel loginwindow">
	<h1 id="popup_page"><spring:message code="forgot.message"/><a href="${pageContext.request.contextPath}/home.html" class="close"></a></h1>
	<div class="box-content" onclick="closeError()">
		<div class="row">
	 		<div class="msgbg msgbgError" style="display: none;">
	              <div class="msg_left">
	                <div class="msg_right">
	                
	                  <c:if test="${empty errorMessage && empty successMessage}">
	                  		<div class="msg_error"><img src="${pageContext.request.contextPath}/resources/img/form/ic_error.png" alt="" onclick="closeError()"/><span id="error_msg">${message}</span></div>
	                  </c:if>
	                </div>
	              </div>
	    	</div>
	    	<div class="msgbg msgbgSuccess" style="display: none;">
	              <div class="msg_left">
	                <div class="msg_right">
	                  		<div class="msg_success"><img alt="" src="../resources/img/form/ic_success.png"><span id="successs_msg"></span></div>
	                </div>
	              </div>
	    	</div>
	    	
	    	
	    	  
 		</div>
		<div class="row"><label class="full"><spring:message code="forgot.email_address"/></label><form:input path="email" class="large" /></div>
		<div class="row" id="errorMessageDiv" style="color: red;">${message}</div>	
	    <div class="row"><input name="" type="submit" class="inputbutton" value="<spring:message code="register.email"/>" /></div>
	    <div class="bottomrow"><p><spring:message code="register.already_user"/><spring:message code="register.please"/>&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/user/loginMain.html"><spring:message code="register.login_here"/></a></p></div>
	</div>
	</div>
</form:form>
</body>
</html>