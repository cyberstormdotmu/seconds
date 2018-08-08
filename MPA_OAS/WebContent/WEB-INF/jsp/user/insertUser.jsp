<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">


<script type="text/javascript" charset="utf-8">

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
		
</script>

<div id="content-outer">
<!-- start content -->
<div id="content">


<div id="page-heading"><h1>Add User</h1></div>


<table width="100%" cellspacing="0" cellpadding="0" border="0" id="content-table">
<tbody><tr>
	<th class="sized" rowspan="3"><img width="20" height="300" alt="" src="../images/shared/side_shadowleft.jpg"></th>
	<th class="topleft"></th>
	<td id="tbl-border-top">&nbsp;</td>
	<th class="topright"></th>
	<th class="sized" rowspan="3"><img width="20" height="300" alt="" src="../images/shared/side_shadowright.jpg"></th>
</tr>
<tr>
	<td id="tbl-border-left"></td>
	<td>
	<!--  start content-table-inner -->
	<div id="content-table-inner">
	
	<table width="100%" cellspacing="0" cellpadding="0" border="0">
	<tbody><tr valign="top">
	<td>	
	<form:form method="post" action="createUser.mpa" modelAttribute="userMaster" id="id-form">
		<table cellspacing="0" cellpadding="0" border="0" id="id-form">
			<tbody>
			<tr>
				<th valign="top"><spring:message code="lable.userId"/></td>
				<td>
					<form:input path="userId" maxlength="20" class="inp-form" cssStyle="width:138px !important;"/>
					<form:errors path="userId" cssClass="field-error"/>
				</td>
			</tr>
			<tr>
				<th valign="top"><spring:message code="lable.password"/></td>
				<td>
					<form:password path="password" maxlength="256" class="inp-form" cssStyle="width:138px !important;"/>
					<form:errors path="password" cssClass="field-error"/></td>
			</tr>
			<tr>
				<th valign="top"><spring:message code="lable.firstName"/></td>
				<td><form:input path="firstName" maxlength="25" class="inp-form" cssStyle="width:138px !important;"/>
					<form:errors path="firstName" cssClass="field-error"/></td>
			</tr>
			<tr>
				<th valign="top"><spring:message code="lable.middleName"/></td>
				<td><form:input path="middleName" maxlength="25" class="inp-form" cssStyle="width:138px !important;"/>
					<form:errors path="middleName" cssClass="field-error"/></td>
			</tr>
			<tr>
				<th valign="top"><spring:message code="lable.lastName"/></td>
				<td><form:input path="lastName" maxlength="25" class="inp-form" cssStyle="width:138px !important;"/>
					<form:errors path="lastName" cssClass="field-error"/></td>
			</tr>
			<tr>
				<th valign="top"><spring:message code="lable.joiningDate"/></td>
 				<td><form:input path="dateOfJoiningString" class="date-pick inp-form calendar-text" readonly="true" id="dateOfJoiningString" cssStyle="width:138px !important;" /></td> 
			</tr>
			<tr>
				<th valign="top"><spring:message code="lable.role"/></td>
				<td>
				
					<form:select id="role" path="role" class="roleClass">
						<form:option value="">--Select--</form:option>
						<form:option value="Counter">Counter</form:option>
						<form:option value="Administrator">Administrator</form:option>
					</form:select>
				</td>
			</tr>
			<tr>
			<th valign="top"><div class="counterDiv"> <spring:message code="lable.counter"/></div></th>
			<td>
				<div class="counterDiv">
					<form:select path="counter" class="styledselect_form_1" id="counter">
						<form:option value="">--Select--</form:option>
						<form:option value="1">1</form:option>
						<form:option value="2">2</form:option>
						<form:option value="3">3</form:option>
					</form:select>
					</div>
				</td>
			</tr>
			<tr>
				
				<td>
				</td>
			</tr>
			<tr>
				<td colspan="2" valign="top"><input type="submit" value="Create User" class="form-submit"/></td>
			</tr>
		</table>
		</form:form>
		
		
		
	<!-- end id-form  -->

	</td>
	<td>


</td>
</tr>
<tr>
<td><img width="695" height="1" alt="blank" src="../images/shared/blank.gif"></td>
<td></td>
</tr>
</tbody></table>
 
<div class="clear"></div>
 

</div>
<!--  end content-table-inner  -->
</td>
<td id="tbl-border-right"></td>
</tr>
<tr>
	<th class="sized bottomleft"></th>
	<td id="tbl-border-bottom">&nbsp;</td>
	<th class="sized bottomright"></th>
</tr>
</tbody></table>

<div class="clear">&nbsp;</div>

</div>
<!--  end content -->
<div class="clear">&nbsp;</div>
</div>