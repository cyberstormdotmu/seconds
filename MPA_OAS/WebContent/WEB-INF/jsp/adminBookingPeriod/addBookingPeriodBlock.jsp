<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">


<script type="text/javascript" charset="utf-8">

function checkAll()
{
	var flag=false;
	var timeFlag=false;
	var errors="Please Fill The Following Details<br/>";
	errors+="===========================<br/>";
	
	if(document.getElementById("periodStartDate").value=="")
	{
		errors +="<br/>Period Start Date";
		flag=true;
	}
	
	if(document.getElementById("periodStartTime").value=="")
	{
		errors +="<br/>Period Start Date";
		flag=true;
		timeFlag=true;
	}
	
	if(document.getElementById("periodEndTime").value=="")
	{
		errors +="<br/>Period End Date";
		flag=true;
		timeFlag=true;
	}
	if(timeFlag==false){
		var startTime=document.getElementById("periodStartTime").value;
		var endTime=document.getElementById("periodEndTime").value;
		
		var startHours=startTime.substring(0,2);
		var startMinutes=startTime.substring(3,5);
		
		var endHours=endTime.substring(0,2);
		var endMinutes=endTime.substring(3,5);

		var invalidHours=false;
		if(startHours > endHours){
			errors +="<br/>Invalid Start Time and End Time";
			invalidHours=true;
			flag=true;
		}
		if(invalidHours==false){
			if(startMinutes > endMinutes){
				errors +="<br/>Invalid Start Time and End Time";
				flag=true;
			}
		}
	}
	if($.trim(document.getElementById("reason").value)=="")
	{
		errors +="<br/>Please Enter Reason";
		flag=true;
	}
	

	if(flag)
	{
		$("#errorDiv").show();
		document.getElementById("errorDiv").innerHTML =errors;
		return false;
	}
}



$( document ).ready(function() {

	 $("#periodStartDateString").focus();
	
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
<div id="wrapper">
<div id="middle">
      <!--content start-->
      	 <h1>Add Blocking Period</h1>
         <div class="form_field top_pad">
         	<div class="titlebg"><h2>Insert Details</h2></div>
            <div class="content_block">
            <div id="errorDiv" style="border-color: red; border-style: dashed;border-width: 2px; margin-top: 20px; margin-bottom: 50px; display: none;padding:15px;color: red;">
	</div>
            <form:form method="post" action="addBookingPeriodBlock.mpa" modelAttribute="bookingPeriodBlock" id="id-form">
		<table cellspacing="10" cellpadding="0" border="0" id="id-form">
			<tbody>
			<tr>
				<th valign="top"><spring:message code="lable.dateToBlock"/><span style="color: red;"> *</span></td>
 				<td><form:input path="periodStartDateString" class="date-pick inp-form calendar-text" readonly="true" id="periodStartDate" cssStyle="width:138px !important;" /></td> 
			</tr>
			<tr>
				<th valign="top"><spring:message code="lable.periodStartTime"/>
				<span style="color: red;"> *</span>
				</td>
				<td>
					<form:select path="periodStartTimeString" class="" id="periodStartTime">
						<form:option value="">---Select---</form:option>
						<form:options items="${bookingPeriodBlock.timeList}"/>
					</form:select>
				</td>
			</tr>
			<tr>
				<th valign="top"><spring:message code="lable.periodEndTime"/><span style=" color: red;"> *</span></td>
				<td>
					<form:select path="periodEndTimeString"  id="periodEndTime">
						<form:option value="">---Select---</form:option>
						<form:options items="${bookingPeriodBlock.timeList}"/>
					</form:select>
				</td>
			</tr>
			
			<tr>
				<th valign="top"><spring:message code="lable.reason"/><span style="color: red;"> *</span></td>
				<td>
					<form:textarea path="reason" class="form-textarea" id="reason" />
				</td>
 				<%-- <td><form:input path="periodStartTime" class="inp-form" id="periodStartTime" cssStyle="width:138px !important;" /></td> --%> 
			</tr>
			<tr>
				<td></td>
				<td colspan="2" valign="top"><input type="submit" class="inputbutton btn_small" value="Book" class="form-submit" onclick="javascript:return checkAll()"/></td>
			</tr>
									
		</table>
	</form:form>
           
            </div>
         </div>
      <!--content end-->
    </div>
    <!--main area end-->
  </div>
