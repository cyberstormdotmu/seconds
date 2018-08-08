<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %>

<div id="wrapper">
    <!--main area start-->
    <div id="middle">
      <!--content start-->
      <c:choose>
				<c:when test="${listAppointments.size() < 1}">
				<h1> You have not Created Any Appointment
				</c:when>
				<c:otherwise>
      <h1>Your Scheduled Appointments</h1>
       <div class="form_grid top_pad">
         	<div class="titlebg"><h2>Appointment List</h2>
         	</div>


				<form id="mainform" action="" method="post">
				
				<table class="grid" width=100% border="0" cellspacing="1" cellpadding="0" id="product-table">
				<tr>

					<th class="table-header-repeat line-left minwidth-1" width="30%" style="color: #2671B0">Appointment Type</th>
					<th class="table-header-repeat line-left minwidth-1" style="color: #2671B0">Date</th>
					<th class="table-header-repeat line-left" style="color: #2671B0">Time</th>
					<th class="table-header-options line-left" width=30% style="color: #2671B0">Options</th>
				</tr>
				<% int a = 0; %>
				
				
				<c:forEach var = "appointment" items = "${listAppointments}">
				<% //if (a%2 == 0) {%>
				<tr class="row2">
				<% //} else { %>
				<tr class="row1">
				<% //}  a++;%>
					
						<input type="hidden" id="referenceNo" name="referenceNo" value="${appointment.referenceNo}"/>
					
					<td style="text-align: center;">${appointment.appointmentType}</td>
					<td style="text-align: center;">${appointment.date}</td>
					<td style="text-align: center;">${appointment.time}</td>
					<td style="text-align: center;">
					<a href="${pageContext.request.contextPath}/appointment/rescheduleAppointment.mpa?refNo=${appointment.referenceNo}" title="Reschedule" class="icon-1 info-tooltip">Reschedule</a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a  href="${pageContext.request.contextPath}/appointment/cancelAppointment.mpa?refNo=${appointment.referenceNo}" title="Cancel" class="icon-2 info-tooltip">Cancel</a>
					</td>
				</tr>
				</c:forEach>
				
				</table>
				
				<!--  end product-table................................... --> 
				</form>
				</div>
				</c:otherwise>
				</c:choose>
				</div>
				</div>