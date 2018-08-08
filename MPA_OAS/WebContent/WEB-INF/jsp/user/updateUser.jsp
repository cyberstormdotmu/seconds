<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="com.tatva.utils.MPAContext"%>
<%@page import="com.tatva.utils.DateUtil"%>
<%@page import="com.tatva.domain.UserMaster"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
function goBack()
{
	 window.history.back();
}


function mandatory() {

	var flag=true;
	document.getElementById("errorDiv").innerHTML="";
	document.getElementById("firstNameDiv").innerHTML="";
	document.getElementById("emailAddressDiv").innerHTML="";
	document.getElementById("roleDiv").innerHTML="";
	if(document.getElementById("role").value == "Counter"){
			
		if(document.getElementById("counter").value=="select"){
			document.getElementById("errorDiv").innerHTML ="Select Counter";
			flag=false;
		}
	}

	
	if( $.trim(document.getElementById('emailAddress').value) ==""){

		document.getElementById("emailAddressDiv").innerHTML ="Enter Email Address";
		flag=false;
	} else {
			var email=$.trim(document.getElementById("emailAddress").value);	
		 	 var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		  	if(!regex.test(email)){
				document.getElementById("emailAddressDiv").innerHTML ="Enter Proper Email Address";
		    	flag=false;
		  }
	}
	
	if(document.getElementById("role").value=="select"){
		document.getElementById("roleDiv").innerHTML ="Select Role";
		flag=false;
	}
	
	
	if($.trim(document.getElementById("firstName").value)== ""){
		document.getElementById("firstNameDiv").innerHTML ="Enter First Name";
		flag=false;
	}
	return flag;
}

$( document ).ready(function() {
	$("#firstName").focus();
	$(".roleClass").bind("change", function(){
		if($(".roleClass").val() == 'Administrator'){
			$(".counterDiv").hide();
		}else{
			$(".counterDiv").show();
		}
	});

	var role = $("#role").val();
	if(role=="Counter"){
		$(".counterDiv").show();
	}else{
		$(".counterDiv").hide();	
	}
});

$(function()
		{
			$('.date-pick').datePicker({startDate:'01/01/1996'});
		}); 
		
</script>
</head>
<body>
<div id="wrapper">
<div id="middle">
      <!--content start-->
      	 <h1>Update User</h1>
         <div class="form_field top_pad">
         	<div class="titlebg"><h2>Update User Details</h2></div>
            <div class="content_block">
            <form:form method="post" action="updateUser.mpa" modelAttribute="userMaster" id="id-form">
		<table cellspacing="10" cellpadding="0" border="0" id="id-form">
			<tbody>
			<tr>
				<th align="right">User Id:<span style="color: red;"> *</span></th>
				<td>
				<form:input path="userId" maxlength="20" class="inp-form" readonly="true"/>
				<form:errors path="userId" cssClass="field-error"/>
				</td>
			</tr>
			
			
			<tr>
				<th align="right">First Name:<span style="color: red;"> *</span></td>
				<td>
				<form:input path="firstName" maxlength="25" class="inp-form" />
				<form:errors path="firstName" cssClass="field-error"/>
				<span id="firstNameDiv" style="color: red;"></span>
				</td>
			</tr>
			<tr>
				<th align="right">Middle Name:</td>
				<td><form:input path="middleName" maxlength="25" class="inp-form" />
					<form:errors path="middleName" cssClass="field-error"/>
				</td>
			</tr>
			<tr>
				<th align="right">Last Name:</td>
				<td><form:input path="lastName" maxlength="25" class="inp-form" />
					<form:errors path="lastName" cssClass="field-error"/>
				</td>
			</tr>
			
			<tr>
				<th align="right"><spring:message code="lable.emailAddress"/><span style="color: red;"> *</span></th>
				<td><form:input path="emailAddress" maxlength="100" class="inp-form" cssStyle="width:138px !important;"/>
					<form:errors path="emailAddress" cssClass="field-error"/>
					<span id="emailAddressDiv" style="color: red;"></span>
					</td>
			</tr>
			
			
			<tr>
				<th align="right">Joining Date:</td>
 				<td><form:input path="dateOfJoiningString"   class="date-pick inp-form calendar-text" readonly="true" id="dateOfJoining" /></td> 
			</tr>
			<c:set var="cuurentUser" value="<%=MPAContext.currentUser %>"></c:set>
			<c:choose>
			
			<c:when test="${userMaster.userId ne cuurentUser }">
			<tr>
				<th align="right"><spring:message code="lable.role"/><span style="color: red;"> *</span></th>
				<td>
					<form:select id="role" path="role" class="roleClass">
						<form:option value="select">--Select--</form:option>
						<form:option value="Counter">Counter</form:option>
						<form:option value="Administrator">Administrator</form:option>
					</form:select><form:errors path="role" cssClass="field-error"/>
					<span id="roleDiv" style="color: red;"></span>
					<form:errors path="role" cssClass="field-error"/>
				</td>
			</tr>
			<tr>
			<th align="right"><div class="counterDiv" name="counterDiv" style="display: none;"> <spring:message code="lable.counter"/><span style="color: red;"> *</span></div></th>
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

			</c:when>
			<c:otherwise>
			<form:hidden id = "role" path="role"></form:hidden>
			
			</c:otherwise>
			</c:choose>
			<tr>
				<td align="right" valign="top"><input type="submit" value="Update" class="inputbutton btn_small" onclick="javascript:return mandatory();"/></td>
				<td  align="center" valign="top"><input type="button" value="Cancel" class="inputbutton btn_small" onclick="goBack()"/></td>
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