<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<script type="text/javascript">

	window.onload = function() {
	  document.getElementById("passport").focus();
	};
	function check(){
			
		if($.trim(document.getElementById("passport").value) == ""){
			$("#error").show();
			return false;
		}
		return true;
	}
	
</script>
<div id="wrapper">
<div id="middle">
<h1>Check Your Appointment</h1>
<div class="form_field top_pad">
<div class="titlebg"><h2>Check Your Appointment Details Here</h2></div>
<div class="content_block">
	<form method="post" action="${pageContext.request.contextPath}/appointment/checkAppointment.mpa" id="id-form">
		<table cellspacing="10" cellpadding="0" border="0" id="id-form">
			<tbody>
			<tr>
				<th valign="top">Enter Passport Number<span style="color: red;"> *</span></th>
				<td>
					<input type="text" id="passport" name="passport" class="inp-form"  />
				</td>
				<td>
					<div id="error" style="display: none;">
						<p style="color: red;">Please enter passport number</p>
					</div>
				</td>
			</tr>
			
			<tr>
				<td></td>
				<td>
					<input type="submit" value="Submit" class="inputbutton btn_small" onClick="javascript:return check()"/>
					<input type="reset" value="Cancel" class="inputbutton btn_small">
				</td>
			</tr>
		</table>
		</form>
</div>
</div>
</div>
</div>