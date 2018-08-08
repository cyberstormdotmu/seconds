<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert New User</title>
<script type="text/javascript" charset="utf-8">


$(document).ready(function () {

	$("#userId").focus();
    $('#password').bind('copy paste', function (e) {
       e.preventDefault();
    });
  });


function mandatory(){
	var flag=true;
	document.getElementById("errorDiv").innerHTML="";
	document.getElementById("userIdDiv").innerHTML="";
	document.getElementById("passwordDiv").innerHTML="";
	document.getElementById("firstNameDiv").innerHTML="";
	document.getElementById("emailAddressDiv").innerHTML="";
	document.getElementById("roleDiv").innerHTML="";
	
	if(document.getElementById("role").value=="Counter"){
		if(document.getElementById("counter").value=="select"){
			document.getElementById("errorDiv").innerHTML ="Select Counter";
			flag=false;
		}
	}
	if($.trim(document.getElementById("emailAddress").value) !="") {
		  var email=$.trim(document.getElementById("emailAddress").value);	
		  var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		  if(!regex.test(email)){
				document.getElementById("emailAddressDiv").innerHTML ="Enter Proper Email Address";
		    	flag=false;
		  }
	}

	if($.trim(document.getElementById("emailAddress").value) ==""){
		document.getElementById("emailAddressDiv").innerHTML ="Enter Email Address";
		flag=false;
	} 
	
	if($.trim(document.getElementById("password").value)== ""){
		document.getElementById("passwordDiv").innerHTML ="Enter password";
		flag=false;
	}
	
	if(document.getElementById("role").value=="select"){
		document.getElementById("roleDiv").innerHTML ="Select Role";
		flag=false;
	}
	
	if($.trim(document.getElementById("userId").value)== ""){
		document.getElementById("userIdDiv").innerHTML ="Enter User Id";
		flag=false;
	}

	if($.trim(document.getElementById("firstName").value)== ""){
		document.getElementById("firstNameDiv").innerHTML ="Enter First Name";
		flag=false;
	}
	return flag;
}

$( document ).ready(function() {
	$(".roleClass").bind("change", function(){
		if($(".roleClass").val() == 'Administrator'){
			$(".counterDiv").hide();
		}else{
			$(".counterDiv").show();
		}
	});
});

$(function()
		{
			$('.date-pick').datePicker({startDate:'01/01/1996'});
		}); 
	
function userIdloadingStart(){
	
	var loading = document.getElementById('loading');
	loading.style.display = 'inline';
	var userNotAvailable = document.getElementById('usernotavailable');
	userNotAvailable.style.display = 'none';
	var errorid = document.getElementById('errorId');
	errorid.style.display = 'none';
	
 	
}


function checkavailableUser(){
	
	var availabilityflag = null;
	var insertedUserId = $.trim(document.getElementById("userId").value);
	
	$.ajax({
		url:"availableUser.mpa?userId="+insertedUserId,
		type:"POST",
		contentType: "application/json; charset=utf-8",
        dataType:"json",
        success:function(result){
           availabilityflag = result;
	       	if(availabilityflag == "available"){
    		var userNotAvailable = document.getElementById('usernotavailable');
    		userNotAvailable.style.display = 'none';
    		document.getElementById('submitBtn').disabled = false;
    		}
	       	else if(availabilityflag == "unavailable"){
	    		var userNotAvailable = document.getElementById('usernotavailable');	    		
	    		userNotAvailable.style.display = 'inline';
	    		document.getElementById('submitBtn').disabled = true;
	       	}
    		else{
    			var loading = document.getElementById('loading');
    			loading.style.display = 'inline';
    		document.getElementById('submitBtn').disabled = true;
    		}           
           var loading = 	document.getElementById('loading');
       		   loading.style.display = 'none';
        },
        error:function(errorThrown){
			
        }
	});		
}
		
		
</script>
</head>
<body>
<div id="wrapper">
<div id="middle">
      <!--content start-->
      	 <h1>Add User</h1>
         <div class="form_field top_pad">
         	<div class="titlebg"><h2>Insert User Details</h2></div>
            <div class="content_block">
            <form:form method="post" action="createUser.mpa" modelAttribute="userMaster" id="id-form">
			<table cellspacing="10" cellpadding="0" border="0" id="id-form">
			<tbody>
			<tr>
				<th align="right"><spring:message code="lable.userId"/>
				<span style="color: red;"> *</span>
				</th>				
				<td>
					<form:input path="userId" maxlength="20" class="inp-form" onfocus="userIdloadingStart();" onblur="checkavailableUser();" cssStyle="width:138px !important;"/>

					<span id="loading" style="display: none;"><img src="../images/loading.gif"/></span>
					<span id="userIdDiv" style="color: red;"></span>
					<form:errors path="userId" id="errorId" cssClass="field-error" cssStyle="display:inline"/>					
					<span id="usernotavailable" style="vertical-align: 5px; color: red; display: none;">Entered User Id already exists.</span>
					</td>
				
			</tr>
			<tr>
				<th align="right"><spring:message code="lable.password"/>
				<span style="color: red;"> *</span>
				</th>
				<td>
					<form:password path="password" maxlength="256" class="inp-form" cssStyle="width:138px !important;"/>
					<form:errors path="password" cssClass="field-error"/>
					<span id="passwordDiv" style="color: red;"></span>
					</td>
			</tr>
			<tr>
				<th align="right"><spring:message code="lable.firstName"/>
				<span style="color: red;"> *</span>
				</th>
				<td><form:input path="firstName" maxlength="25" class="inp-form" cssStyle="width:138px !important;"/>
				
					<form:errors path="firstName" cssClass="field-error"/>
					<span id="firstNameDiv" style="color: red;"></span>
					</td>
			</tr>
			<tr>
				<th align="right"><spring:message code="lable.middleName"/>
				
				</th>
				<td><form:input path="middleName" maxlength="25" class="inp-form" cssStyle="width:138px !important;"/>
				</td><td><form:errors path="middleName" cssClass="field-error"/></td>
			</tr>
			<tr>
				<th align="right"><spring:message code="lable.lastName"/></th>
				<td><form:input path="lastName" maxlength="25" class="inp-form" cssStyle="width:138px !important;"/>
				</td><td><form:errors path="lastName" cssClass="field-error"/></td>
			</tr>
			
			<tr>
				<th align="right"><spring:message code="lable.emailAddress"/><span style="color: red;"> *</span></th>
				<td><form:input path="emailAddress" maxlength="100" class="inp-form" cssStyle="width:138px !important;"/>
					<form:errors path="emailAddress" cssClass="field-error"/>
					<span id="emailAddressDiv" style="color: red;"></span>
					</td>
			</tr>
			
			<tr>
				<th align="right"><spring:message code="lable.joiningDate"/></th>
 				<td><form:input path="dateOfJoiningString" class="date-pick inp-form calendar-text" readonly="true" id="dateOfJoiningString" cssStyle="width:138px !important;" /></td> 
			</tr>
			<tr>
				<th align="right"><spring:message code="lable.role"/><span style="color: red;"> *</span></th>
				<td>				
					<form:select id="role" path="role" class="roleClass">
						<form:option value="select">--Select--</form:option>
						<form:option value="Counter">Counter</form:option>
						<form:option value="Administrator">Administrator</form:option>
					</form:select>
					<span id="roleDiv" style="color: red;"></span>
					<form:errors path="role" cssClass="field-error"/>
				</td>
				<td></td>
			</tr>
			<tr>
   <th align="right"><div class="counterDiv" name="counterDiv" style="display: none;"> <spring:message code="lable.counter"/><span style="vertical-align: 5px; color: red;"> *</span></div></th>
   <td>
    <div name="counterDiv" class="counterDiv" style="display: none;">
     <form:select path="counter" id="counter">
      <form:option value="select">--Select--</form:option>
      <form:option value="1">1</form:option>
      <form:option value="2">2</form:option>
      <form:option value="3">3</form:option>
     </form:select>
     <span id="errorDiv" style="color: red;"></span>
     </div>
    </td>
   </tr>
			<tr>
				
				<td>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center"><input class="inputbutton btn_small" type="submit" id="submitBtn" value="Create User" onclick="javascript:return mandatory()"/></td>
				<td></td>
			</tr>
		</table>
		</form:form>
           
            </div>
         </div>
      <!--content end-->
    </div>
    <!--main area end-->
  </div>
</body>
</html>