<meta HTTP-EQUIV="REFRESH" content="60; url=${pageContext.request.contextPath}/appointment/listAppointment.mpa?temp1=temp">

<%@page import="com.tatva.utils.SearchConstants"%>
<%@page import="com.tatva.domain.AppointmentMaster"%>
<%@page import="com.tatva.utils.MPAContext"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isELIgnored="false" %>

<script type="text/javascript">
var flag=true;
$(function()
		{
	$("#searchDiv").hide();
			$('.date-pick').datePicker({startDate:'01/01/1996'});
		});

$(document).ready(function(){
	$("#search").click(function(){
		if(flag){
			$("#searchDiv").slideDown(200);
			$("#searchDiv").show();
			flag=false;
		}else{
			$("#searchDiv").slideUp(200);
			flag=true;
		}
	});
});

function alertbox(property){
	//alert(property.value);
}

function changeRows()
{
	var records = document.getElementById("recordsPerPage").value;
// 	var curPage = document.getElementById("cPage").value;
	var curPage = 1;
	window.location.href = "../appointment/listAppointment.mpa?recordsPerPage="+records+"&page="+curPage+"";
}


function changeOrder(property)
{
	
	var value = document.getElementById(property+"grid").value;
	window.location.href='../appointment/orderAppointmentList.mpa?'+property+'='+value+'';
}

function changeStatus(refNo){
	
	 var form = document.getElementById("mainform");
	form.action='../appointment/changeAppointmentSatuts.mpa?referenceNo='+refNo;
	form.submit();
}

function noShowClick(refNo){
	var answer=confirm("Are you sure want to change it to NO-SHOW?");
	if(answer){
		
		$('#noShow').val('true');
		var form = document.getElementById("mainform");
		
		form.action='../appointment/changeAppointmentSatuts.mpa?referenceNo='+refNo;
		form.submit();
	}else{
		return false;
	}
}
</script>
<div id="wrapper">
    <!--main area start-->
    <div id="middle">
      <!--content start-->
      	
      	<div class="form_grid top_pad">
      		<div class="titlebg" ><h2><span id="search"  style="width:100%; color: white; cursor: pointer;">Search Appointment<img align="right" style="margin-right: 3px;" src="../images/down-ar.png"/></span></h2>
         	</div>
      		
      		<div class="content_block" id="searchDiv">
      			<form:form action="searchAppointment.mpa" method="post" commandName="searchAppointment">
      				<table width="100%" cellspacing="10" cellpadding="0" border="0"> 
      					<tr>
      						<td><form:label path="appointmentType"/>Type Of Appointment</td>
      						<td>
      							<select name="appointmentTypeRel">
      								<option value="<%=SearchConstants.EQUALS %>"><%=SearchConstants.EQUALS %></option>
      								<option value="<%=SearchConstants.CONTAINS %>"><%=SearchConstants.CONTAINS %></option>
								    <option value="<%=SearchConstants.LT %>"><%=SearchConstants.LT %></option>
								    <option value="<%=SearchConstants.LT_EQ %>"><%=SearchConstants.LT_EQ %></option>
								    <option value="<%=SearchConstants.GT %>"><%=SearchConstants.GT %></option>
								    <option value="<%=SearchConstants.GT_EQ %>"><%=SearchConstants.GT_EQ %></option>
								    <option value="<%=SearchConstants.NOT_EQUALS %>"><%=SearchConstants.NOT_EQUALS %></option>
     							</select>
      						</td>
      						 <td><form:input path="appointmentType"/></td>    
      					</tr>
      					
      					<tr>
      						<td><form:label path="appointmentStatus"/>Status</td>
      						<td>
      							<select name="appointmentStatusRel">
      								<option value="<%=SearchConstants.EQUALS %>"><%=SearchConstants.EQUALS %></option>
      								<option value="<%=SearchConstants.CONTAINS %>"><%=SearchConstants.CONTAINS %></option>
								    <option value="<%=SearchConstants.LT %>"><%=SearchConstants.LT %></option>
								    <option value="<%=SearchConstants.LT_EQ %>"><%=SearchConstants.LT_EQ %></option>
								    <option value="<%=SearchConstants.GT %>"><%=SearchConstants.GT %></option>
								    <option value="<%=SearchConstants.GT_EQ %>"><%=SearchConstants.GT_EQ %></option>
								    <option value="<%=SearchConstants.NOT_EQUALS %>"><%=SearchConstants.NOT_EQUALS %></option>
     							</select>
      						</td>
      						 <td><form:input path="appointmentStatus"/></td>    
      					</tr>
      					
      					<tr>
      						<td><form:label path="appointmentDateString"/>Date</td>
      						<td>
      							<select name="appointmentDateStringRel">
      								<option value="<%=SearchConstants.EQUALS %>"><%=SearchConstants.EQUALS %></option>
      								<option value="<%=SearchConstants.CONTAINS %>"><%=SearchConstants.CONTAINS %></option>
								    <option value="<%=SearchConstants.LT %>"><%=SearchConstants.LT %></option>
								    <option value="<%=SearchConstants.LT_EQ %>"><%=SearchConstants.LT_EQ %></option>
								    <option value="<%=SearchConstants.GT %>"><%=SearchConstants.GT %></option>
								    <option value="<%=SearchConstants.GT_EQ %>"><%=SearchConstants.GT_EQ %></option>
								    <option value="<%=SearchConstants.NOT_EQUALS %>"><%=SearchConstants.NOT_EQUALS %></option>
     							</select>
      						</td>
      						 <td><form:input path="appointmentDateString" id="appointmentDate" onchange="alertbox(this)" class="date-pick inp-form calendar-text" readonly="true" /></td>    
      					</tr>
      					
      					
      					<tr>
						    <td colspan="3" class="submit">
						     <input class="inputbutton btn_small" id="submit" type="submit" value="Search"/>
						    </td>
   						</tr>
      				</table>
      			</form:form>
      		</div>
      	</div>
      	
      	
      	
      	<br><br>
		<c:choose>
							<c:when test="${listAppointment.size() == 0}">
							<h3>There are No Appointments According to your Search</h3>
							</c:when>							
							<c:otherwise> 	
      	<form name ="mainform" method="post" id="mainform">
      <input type="hidden" id="referenceNo" name="referenceNo"/>
					<input type="hidden" id="noShow" name="noShow" />
      	 <h1>Appointment List</h1>
         <div class="form_grid top_pad">
         	<div class="titlebg"><h2>Appointment List</h2>
         	</div>
            <div class="content_block">
            <div id="page-heading">
				<h1><c:out value="${param['deletedSuccess']}" /></h1>
			</div>
			<c:if test="${not empty messages}">
				<div id="message-green">
				<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<tbody><tr>
							<td class="green-left">${messages}</td>
							<td class="green-right"><a class="close-green"><img alt="" src="../images/table/icon_close_green.gif"></a></td>
				</tr>
				<tr style="height: 5px;"></tr>
				</tbody></table>
				</div>
			</c:if>
            	<table width="100%" class="grid" border="0" cellspacing="1" cellpadding="0">
                  <tr>
                  	<th class="table-header-repeat line-left minwidth-1"><a href="#">No.</a>	</th>
                  	<th class="table-header-repeat line-left minwidth-1"><a href="javascript:;" onclick="changeOrder('appointmentType');">Types Of Appointment</a><input type="hidden" id="appointmentTypegrid" name="appointmentType" value="${appointmentTypeOrder  }"/>	</th>
					<th class="table-header-repeat line-left minwidth-1"><a href="javascript:;" onclick="">Types Of Transaction</a></th>
					<th class="table-header-repeat line-left minwidth-1"><a href="javascript:;" onclick="changeOrder('appointmentDate');">Date</a><input type="hidden" id="appointmentDategrid" name="appointmentDate" value="${appointmentDateOrder }"/></th>
					<th class="table-header-repeat line-left"><a href="javascript:;" onclick="changeOrder('appointmentTime');">Time of Appointment</a><input type="hidden" id="appointmentTimegrid" name="appointmentTime" value="${appointmentTimeOrder }"/></th>
					<th class="table-header-repeat line-left"><a href="javascript:;" onclick="">Contact No.</a></th>
					<th class="table-header-repeat line-left"><a href="javascript:;" onclick="changeOrder('referenceNo');">Reference No.</a><input type="hidden" id="referenceNogrid" name="referenceNo" value="${referenceNoOrder}"/></th>
					<th class="table-header-repeat line-left"><a href="javascript:;" onclick="">Status</a></th>
					<th class="table-header-repeat line-left"><a href="javascript:;">Action</a></th>
					<th class="table-header-repeat line-left"><a href="javascript:;">No - Show</a></th>
                  </tr>
						<%
							int a = request.getAttribute("start") == null ? 0 : (Integer)request.getAttribute("start");
						%>
						<c:forEach var="appointment" items="${listAppointment}">
							<%
								if (a % 2 == 0)
									{
							%>
							<tr class="row2">
								<%
									} else
										{
								%>
							
							<tr class="row1">
							
								<% }  a++;
				%>
					<td width="2%" style="text-align: center;"><%=a %></td>
					<td width="11%" style="text-align: center;">${appointment.appointmentType}</td>
					<td width="13%" style="text-align: center;">
					<c:forEach items="${appointment.transacMaster}" var="transactions">
   							<c:out value="${transactions.transacId.transactionType}"/><br/>
					</c:forEach></td>
					
					<td width="5%" style="text-align: center;">${appointment.appointmentDateString}</td>
					<td width="10%" style="text-align: center;">${appointment.appointmentTime}</td>
					<td width="10%" style="text-align: center;">${appointment.contactNo}</td>
					<td width="30%" style="text-align: center;">${appointment.referenceNo}</td>
					<td width="5%" style="text-align: center;">${appointment.appointmentStatus}</td>
					<td width="5%" style="text-align: center;">
						<c:if test="${appointment.appointmentStatus eq 'In Progress'}">
							<input type="button" value="Complete" onclick="changeStatus('${appointment.referenceNo}')"/>
						</c:if> 
						<c:if test="${appointment.appointmentStatus eq 'Checked In'}">
							<input type="button" value="Progress" onclick="changeStatus('${appointment.referenceNo}')"/>
						</c:if>
						<c:if test="${appointment.appointmentStatus eq 'Complete'}">
							Complete
						</c:if>
						<c:if test="${appointment.appointmentStatus eq 'NO-SHOW'}">
							NO-SHOW
						</c:if>
						
					</td>
					
						<td width="9%" style="text-align: center;">
						<c:if test="${appointment.appointmentStatus ne 'Complete' and appointment.appointmentStatus ne 'NO-SHOW'}">
							<input type="button"  value="No-Show" onclick="noShowClick('${appointment.referenceNo}')"/>
						</c:if> 
					</td>
				</tr>
				
				</c:forEach>

					</table>
                <div class="paging">
                	<input type="hidden" id="cPage" name="cPage" value="${currentPage }">
                  <ul>
                  	<c:if test="${currentPage gt 1 }">
                  		<li><a href="${pageContext.request.contextPath}/appointment/listAppointment.mpa?page=${currentPage - 1}">&laquo; Prev</a></li>
                  	</c:if>
                  	<c:forEach begin="1" end="${noOfPages}" var="i">
                  		<c:choose>
                  			<c:when test="${currentPage eq i }">
                  				<li><a href="#" class="active">${i}</a></li>
                  			</c:when>
                  			<c:otherwise>
                  				<li><a href="${pageContext.request.contextPath}/appointment/listAppointment.mpa?page=${i}">${i}</a></li>
                  			</c:otherwise>
                  		</c:choose>
                  	</c:forEach>
                  	<c:if test="${currentPage lt noOfPages }">
                  		<li><a href="${pageContext.request.contextPath}/appointment/listAppointment.mpa?page=${currentPage + 1}">Next &raquo;</a></li>
                  	</c:if>
                  </ul>
                  <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                No of rows:  &nbsp;
             <select name="recordsPerPage" id="recordsPerPage" style="width: 60px;">
             <% int userRows = session.getAttribute("userRows") == null ? 10 : (Integer)session.getAttribute("userRows"); 
             if(userRows == 5) { %>
				<option value="5" selected="selected">5</option>
			<% } else { %>
				<option value="5">5</option>
			<% } if(userRows == 10) { %>
				<option value="10" selected="selected">10</option>
			<% } else { %>
				<option value="10">10</option>
			<% } if(userRows == 15) { %>
				<option value="15" selected="selected">15</option>
			<% } else { %>
				<option value="15">15</option>
			<% } if(userRows == 20) { %>
				<option value="20" selected="selected">20</option>
			<% } else { %>
				<option value="20">20</option>
			<% } %>
			</select>
			<input type="button" value="Go" onclick="changeRows()">
                  </span>
                  
                </div>
            </div>
         </div>   
         	</c:otherwise>
							</c:choose>      
         </form>
      <!--content end-->
    </div>
    <!--main area end-->
  </div>