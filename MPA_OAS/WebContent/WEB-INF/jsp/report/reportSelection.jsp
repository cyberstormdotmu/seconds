<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<div id="content-outer">
<!-- start content -->
<div id="content">

<script type="text/javascript" charset="utf-8">

$(function()
		{
			$("#report_selection").focus();
			$('.date-pick').datePicker({startDate:'01/01/1996'});
		});  

	function showReport(){
		if(document.getElementById("date").value == ""){
			$("#error").show();
		}else{
			document.getElementById("id-form").action="${pageContext.request.contextPath}/report/selectedReport.pdf";
			document.getElementById("id-form").submit();
		}
	}

	function mailReport(){
		if(document.getElementById("date").value == ""){
			$("#error").show();
		}else{
			document.getElementById("id-form").action="${pageContext.request.contextPath}/report/mailSelectedReport.pdf";
			document.getElementById("id-form").submit();
		}
	}
</script>


<div id="wrapper">
<div id="middle">
      <!--content start-->
      	 <h1>Report</h1>
         <div class="form_field top_pad">
         	<div class="titlebg"><h2>Generate Your Report</h2></div>
            <div class="content_block">
            	<form method="post" id="id-form" target="_blank">
		<table cellspacing="10" cellpadding="0" border="0" >
			<tbody>
			<tr>
				<td valign="top">Select Report:</td>
				<td>
					<td>
					<select name = "report_selection" id="report_selection">
					<option value = "apt_schedule">Appointment Scheduled Report</option>
					<option value = "apt_reschedule">Appointment Rescheduled Report</option>
					<option value = "apt_cancelled">Appointment Cancelled Report</option>
					<option value = "waiting_time_rpt">Statistic of Waiting Time Report</option>
					<option value = "service_time_rpt">Statistic of Service Time Report</option>
					<option value = "apt_late">Late Appointment Report</option>
					<option value = "apt_no_show">Appointment No-Show Report</option>
					</select>
					
					</td>
			</tr>
			
			
			<tr>
				<td valign="top">Enter Date:<span style="color: red;"> *</span></td>
				<td>
					<td><input name="date" readonly="readonly" class="date-pick inp-form calendar-text" id="date"/>
				</td>
				<td>
					<div id="error" style="display: none;">
						<p style="color: red;">Please Select Date</p>
					</div>
				</td>
			</tr>
			
			<tr>
				<td></td>
				<td>
					<input type="button" class="inputbutton btn_small"  value="Show Report" onClick="javascript:showReport()"/>
					<input type="button" class="inputbutton btn_small"  value="Send Mail" onClick="javascript:mailReport()">
				</td>
			</tr>
		</table>
		</form>
           
            </div>
         </div>
      <!--content end-->
    </div>
    <!--main area end-->
  </div>