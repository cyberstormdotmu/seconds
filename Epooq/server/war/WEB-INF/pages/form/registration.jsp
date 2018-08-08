 <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<script type="text/javascript">

		$(document).ready(function(){
			 
			 if($("#errorMessage").val().trim()!=""){
					$(".msgbg").show();
					$("#error_msg").html("");
					$("#error_msg").append($("#errorMessage").val());
			 }
			 
			  if($("#firstNameHidden").val().trim()!=""){
				 $("#firstName").val($("#firstNameHidden").val());
			 }
			 if($("#emailHidden").val().trim()!=""){
				 $("#email").val($("#emailHidden").val());
			 }
			 if($("#confirmEmailHidden").val().trim()!=""){
				 $("#confirmEmail").val($("#confirmEmailHidden").val());
			 } 

		});



		function validation(){
			email= document.getElementById("email");
			confirmEmail= document.getElementById("confirmEmail");
			firstName=document.getElementById("firstName");
			terms=document.getElementById("terms").checked;
			var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
			$("#error_msg").html("");
			$(".msgbg").hide();
			if( !(regex.test( email.value )) ) {
				$(".msgbg").show();
				$("#error_msg").html("");
				$("#error_msg").append("<spring:message code="invalid.email"/>");
			    return false;
			}else if(email.value.trim() != confirmEmail.value.trim()){
				$(".msgbg").show();
				$("#error_msg").append("<spring:message code="invalid.confirmEmail"/>");
				return false;
			}
			if(firstName.value.trim()==""){
				$(".msgbg").show();
				$("#error_msg").append("<spring:message code="invalid.firstName"/>");
				return false;
			}
	 		if(!terms){
				$(".msgbg").show();
				$("#error_msg").append("<spring:message code="invalid.terms_usage"/>");
				return false;
			} 
			return true;
		}
		
		function closeError(){
			$(".msgbg").hide();
		}

</script>
<head>
<title>Epooq</title>
<link href="${pageContext.request.contextPath}/resources/css/form/form_style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<form:form action="${pageContext.request.contextPath}/user/registration.html" method="post" onsubmit="return validation();">
<div class="boxpanel regwindow">
<h1 id="popup_page"><spring:message code="register.message"/><a href="${pageContext.request.contextPath}/home.html" class="close"  style="left: 96%"></a></h1>

<div class="box-content">
	<h2><spring:message code="register.messageOne"></spring:message></h2>
	<p><spring:message code="register.messageTwo"></spring:message><br /><br />
	<spring:message code="register.messageThree"></spring:message> 
</p>

 <div class="contblock" onclick="closeError()">
    	 <div class="row">
	 		<div class="msgbg" style="display: none;">
	              <div class="msg_left">
	                <div class="msg_right">
	                  <div class="msg_error"><img src="${pageContext.request.contextPath}/resources/img/form/ic_error.png" alt="" onclick="closeError()"/><span id="error_msg"></span></div>
	                </div>
	              </div>
	    	</div>  
 		</div>  
   <div class="leftpart">
         <div class="row"><label class="full"><spring:message code="register.firstName"/></label><form:input path="firstName" class="medium" /></div>
    	<div class="row"><label class="full"><spring:message code="register.email"/></label><form:input path="email" class="medium" /><span style="color: red; display: none;" >*</span></div>
    </div>
    <div class="rightpart">
        <div class="row"><label class="full"><spring:message code="register.DOB"/></label>
        					<select name="birthDay" class="medium registration-select" style="display: none;" >
        						<option value="1" selected="selected"><c:out value="1"/></option>
        						<%-- <c:forEach var="i" begin="1" end="31">
						           	<c:choose>
						           		<c:when test="${i==year}">
						           			<option value="${i}" selected="selected"><c:out value="${i}"/></option>
						           		</c:when>
						           		<c:otherwise>
						           			<option value="${i}"><c:out value="${i}"/></option>
						           		</c:otherwise>
						           	</c:choose>
								  </c:forEach> --%>
							</select>
						
							 <select name="birthMonth" class="medium registration-select" style="display: none;">
							 	
							 
								<option value="1" selected="selected">January</option>
								<!-- <option value="2">Februuary</option>
								<option value="3">March</option>
								<option value="4">April</option>
								<option value="5">May</option>
								<option value="6">June</option>
								<option value="7">July</option>
								<option value="8">August</option>
								<option value="9">September</option>
								<option value="10">October</option>
								<option value="11">November</option>
								<option value="12">December</option> -->
							</select>
						
							<select name="birthYear" class="medium registration-select">
								<c:forEach var="i" begin="1900" end="2016">
						           	<c:choose>
						           		<c:when test="${i==year}">
						           			<option value="${i}" selected="selected"><c:out value="${i}"/></option>
						           		</c:when>
						           		<c:otherwise>
						           			<option value="${i}"><c:out value="${i}"/></option>
						           		</c:otherwise>
						           	</c:choose>
								  </c:forEach>
							</select>
						
        <!-- <input name="" type="text" class="medium" /> --></div>
        <div class="row"><label class="full"><spring:message code="register.confirmEmail"/> </label><input class="medium" name="confirmEmail" id="confirmEmail" type="text"/></div> 
    </div> 
</div> 
 <div class="row"><label class="chk" >
 <input type="checkbox" value="" id="terms" onclick="closeError()"/><spring:message code="register.i_have_read"/> <a href="${pageContext.request.contextPath}/terms/termsOfUsage.jsp" target="_blank"><spring:message code="register.terms_usage"/></a> <spring:message code="register.accept_them"/> </label></div>
 <div class="row"><input name="" type="submit" class="inputbutton" value="<spring:message code="button.register"/>"/></div>
    <div class="bottomrow"><p><spring:message code="register.already_user"/>&nbsp;&nbsp;<a href="${pageContext.request.contextPath}/user/loginMain.html"><spring:message code="register.please"/>&nbsp<spring:message code="register.login_here"/></a></p></div>
</div>
</div>
</form:form>
</body>
</html>