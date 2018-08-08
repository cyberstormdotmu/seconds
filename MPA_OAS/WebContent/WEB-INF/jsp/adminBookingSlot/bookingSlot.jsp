<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">


<script type="text/javascript" charset="utf-8">

$( document ).ready(function() {
	
	$("#periodStartDate").focus();
	
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
	var today=new Date();
	var day=today.getDate()+1;
	var month=today.getMonth()+1;
	var year=today.getFullYear();
	var mm=today.getMinutes();
	var hour=today.getHours();
	var startDate = null ;
	if(year % 4 == 0){
		if(month == 2){
			if(day > 29){
				day = day -29;
				month = month +1;						
			}
			
		}
	}
	else{
		if(month == 2){
			if(day > 28){
				day = day - 28;
				month = month + 1;
				
				
			}
			
		}
		
	}
	if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 ){
			if(day > 31){
				day = day - 31;
				month = month +1;	
			}
		}
		else if(month == 4 || month == 6 || month == 9 || month == 11){
			if(day >30){
				day = day - 30;
				month = month +1 ;
			}
		}
		else if (month == 12){
			if(day > 31){
				day = day - 31;
				month = month - 11;
				year = year + 1;
			}					
		}
			startDate = day+"/"+month+"/"+year;

			alert(startDate);
			$('.date-pick').datePicker({startDate:startDate});
		}); 

function check(){
	var errors="Please Select Number Of Slots";
		if(document.getElementById("newValue").value=="select"){
			$("#errorDiv").show();
			document.getElementById("errorDiv").innerHTML =errors;
			return false;
		}
		return true;
}
</script>
<div id="wrapper">
<div id="middle">
      <!--content start-->
      	 <h1>Admin Booking Slot</h1>
         <div class="form_field top_pad">
         	<div class="titlebg"><h2>Insert Details</h2></div>
            <div class="content_block">
            <c:if test="${not empty messages}">
		<div id="message-green">
		<table width="100%" cellspacing="0" cellpadding="0" border="0">
		<tbody>
			<tr>
			<td class="green-left">${messages}</td>
			<td class="green-left">${message1}</td>
			<td class="green-right"><a class="close-green"><img alt="" src="../images/table/icon_close_green.gif"></a></td>
			</tr>
			</tbody></table>
			</div>
	</c:if>
          <div id="errorDiv" style="border-color: red; border-style: dashed;border-width: 2px; margin-top: 20px; margin-bottom: 50px; display: none;padding:15px;color: red;">
	</div>  
            <form:form method="post" id="id-form" modelAttribute="globalAttribute" action="addBookingSlot.mpa">
	<form:hidden path="oldValue"/>
	<form:hidden path="attributeName"/>
	<form:hidden path="tempValue"/>
		<table cellspacing="10" cellpadding="0" border="0" id="id-form">
			<tbody>
			<tr>
				<th valign="top"><spring:message code="lable.effectiveDate"/><span style="color: red;"> *</span></td>
				<td><form:input path="applyDateString"  class="date-pick inp-form calendar-text" readonly="true" id="periodStartDate" cssStyle="width:138px !important;"/>
					<br>
					<form:errors path="applyDateString" cssClass="field-error"/>
				</td> 
			</tr>
			<tr>
				<th valign="top" ><spring:message code="lable.numberOfSlotAvailableForBooking"/>
				<span style=" color: red;"> *</span>
				</th>
				<td>
					<form:select path="newValue" id="newValue" >
						<form:option value="select">---Select---</form:option>
						<form:options items="${globalAttribute.slots}"/>
					</form:select>
				</td>
 				<%-- <td><form:input path="periodStartTime" class="inp-form" id="periodStartTime" cssStyle="width:138px !important;" /></td> --%>
			</tr>
			<tr>
			<td></td>
				<td colspan="2" valign="top"><input type="submit" value="Book" class="inputbutton btn_small" onclick="javascript:return check()"/></td>
			</tr>
									
		</table>
	</form:form>
           
            </div>
         </div>
      <!--content end-->
    </div>
    <!--main area end-->
  </div>
