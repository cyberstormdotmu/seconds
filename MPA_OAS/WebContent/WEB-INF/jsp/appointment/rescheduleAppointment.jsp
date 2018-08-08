<%@page import="com.tatva.utils.DateUtil"%>
<%@page import="com.tatva.domain.UserMaster"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" charset="utf-8">

	$(function(){

		 $("#referenceNo").focus();
		
	//datepicker function to enable only 1 month..
		var today=new Date();
		var day=today.getDate()+1;
		var month=today.getMonth()+1;
		var year=today.getFullYear();
		var startDate=day+"/"+month+"/"+year;

	
		var monthLast=today.getMonth()+2;
		if(month==12){
			monthLast=1;
			year=year+1;
		}
		
		var lastDay=day+"/"+monthLast+"/"+year;
	
		$('.date-pick').datePicker({startDate:startDate
								,endDate:lastDay
								});

	}); 

	function checkAll()
	{
		var flag=false;
		var errors="Please Fill The Following Details<br/>";
		errors+="===========================<br/>";
		
		if(document.getElementById("date").value=="")
		{
			errors +="<br/>Date";
			flag=true;
		}

		if(document.getElementById("time").value=="select")
		{
			errors +="<br/>Time";
			flag=true;
		}

		if(flag)
		{
			$("#errorDiv").show();
			document.getElementById("errorDiv").innerHTML =errors;
			return false;
		}
	}

	function checkDate()
	{
		document.getElementById("time").disabled=false;

		var selectedDate=document.getElementById("date").value;

		var nextDay=new Date();
		var day=nextDay.getDate();
		var year=nextDay.getFullYear();
		var month=nextDay.getMonth()+1;

		if(month==0 || month==2 || month==4 || month==6 || month==7 || month==9 || month==11)
		{
			if(day==31)
				day=0;
		}
		else if(month==1)
		{
			if(year%4==0)
			{
				if(day==29)
					day=0;
			}

			else
			{
				if(day==28)
					day=0;
			}
		}
		else
		{
			if(day==30)
				day=0;
		}

		day++;

		var selectedDay=selectedDate.substring(0,2);

		var temp=parseInt(selectedDay)-parseInt(day);

		var hours=nextDay.getHours();
		
		if(temp==0 && hours>17)
		{
			alert("You Cant Make Appointment for tomorrow");
			document.getElementById("date").value="";
			document.getElementById("time").disabled=true;
			document.getElementById("time").value="select";			
		}		
	}

</script>

<div id="wrapper">
<div id="middle">

	<h1>Reschedule Appointment </h1>
	<div class="form_field top_pad">
	<div class="titlebg"><h2>Reschedule Appointment Details</h2></div>
	<div class="content_block">
	<form:form method="post" action="${pageContext.request.contextPath}/appointment/rescheduleAppointment.mpa" modelAttribute="appointmentForm" id="id-form">
		<table cellspacing="10" cellpadding="0" border="0" id="id-form">
			<tbody>
			<tr>
				<th valign="top">Reference Number:<span style="color: red;"> *</span></td>
				<td>
				<form:input path="referenceNo" readonly="true" class="inp-form" cssStyle="width:138px !important;"/>
				<form:errors path="referenceNo" cssClass="field-error"/>
				</td>
			</tr>
			<tr>
				<th valign="top">Date:</td>
				<td><form:input path="date" readonly="true" name="date" class="date-pick inp-form calendar-text" id="date" style="width:147px;" onchange="checkDate()"/>				 	
				 	<form:select path="time" id="time"  class="inp-form" style="margin:0px;padding: 0px;height:31px;padding-left: 3px;">
						<form:option value="select">---Select---</form:option  >
						<% int k=0;
						String temp="AM";
						String valTime="";
						for(int i=8;i<16;i++) {%>
							<% for(int j=0;j<4;j++) {
								if(15*j==0) {
									if(i>=12)
										temp="PM";
									valTime=String.valueOf(i)+":00";
									%>
									
									 <form:option value="<%=valTime%>"> <%=valTime %>&nbsp;<%=temp%>&nbsp;&nbsp;</form:option>
								<%} else {
									valTime=String.valueOf(i)+":"+String.valueOf(15*j);
								%>
									 <form:option value="<%=valTime%>"><%=i%>:<%=15*j%>&nbsp;<%=temp%>&nbsp;&nbsp;</form:option  >  
								<% } %>
							<%} %>
						<%} %>  
							
					</form:select>
				 	
				</td>
			</tr>
			
			<tr>
			<td></td>	<td colspan="2" valign="top"><input value="Submit" type="submit" class="inputbutton btn_small" onclick="javascript:return checkAll()"/></td>
			</tr>
		</table>
		</form:form>
		</div>
		</div>
		</div>
		</div>
		