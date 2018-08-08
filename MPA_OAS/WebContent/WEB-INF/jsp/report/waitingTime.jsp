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
			$('.date-pick').datePicker({startDate:'01/01/1996'});
		});  

	function showReport(){
		if(document.getElementById("date").value == ""){
			$("#error").show();
		}else{
			document.getElementById("id-form").action="${pageContext.request.contextPath}/report/waitingTimeReport.pdf";
			document.getElementById("id-form").submit();
		}
	}

	function mailReport(){
		if(document.getElementById("date").value == ""){
			$("#error").show();
		}else{
			document.getElementById("id-form").action="${pageContext.request.contextPath}/report/mailWaitingTimeReport.pdf";
			document.getElementById("id-form").submit();
		}
	}
</script>

<div id="page-heading"><h1>Waiting Time Report</h1></div>


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
	<form method="post" id="id-form">
		<table cellspacing="0" cellpadding="0" border="0" >
			<tbody>
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
				<td>
					<input type="button" value="Show Report" onClick="javascript:showReport()"/>
					<input type="button" value="Send Mail" onClick="javascript:mailReport()">
				</td>
			</tr>
		</table>
		</form>
		
		
		
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