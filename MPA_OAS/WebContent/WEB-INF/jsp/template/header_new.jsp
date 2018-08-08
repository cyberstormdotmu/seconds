
	<%@page import="com.tatva.domain.UserMaster"%>
<%@page import="com.tatva.utils.MPAContext"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Admin Panel</title>
<link href="../css/stylesheet.css" rel="stylesheet" type="text/css" />
</head>
<body>
<%
		UserMaster userMaster=(UserMaster)request.getSession().getAttribute(MPAContext.currentUser);
%>
<script type="text/javascript">



</script>
<div id="main" class="mainbg">
	
  <div id="wrapper">
    <!--main area start-->
    <div id="header"  >
      <!--Header start-->
      <div class="logo"><a href="${pageContext.request.contextPath}/login/homePage.mpa"><img src="../images/logo.png" alt="" /></a></div>
      <div class="user_info">
			<table border="0" cellspacing="0" cellpadding="0">
			<%
			if(userMaster!=null){
			%>
			<tr>
                <td><img src="../images/ic_user.png" alt="" /></td>
                <td class="text_pad"><span>Welcome,</span> <%=userMaster.getFirstName() %>&nbsp;<%=userMaster.getLastName() %> <br />
                <a href="${pageContext.request.contextPath}/user/flagUpdateProfile.mpa" style="color: red;cursor: pointer;">My Account</a>
                <a href="${pageContext.request.contextPath}/login/changePasswordForm.mpa"style="color: red;">Change Password</a>
                </td>
			<td><a href="${pageContext.request.contextPath}/login/logout.mpa" id="logout"><img src="../images/btn_logout.png" width="75" height="80" alt="" /></a></td>
              </tr>
			<%
				}else{
			%>
			<tr>
                <td><img src="../images/ic_user.png" alt="" /></td>
                
                <td colspan="2"><a href="${pageContext.request.contextPath}/login/loginForm.mpa" id="logout"><img src="../images/btn_login.png" width="75" height="80" alt="" /></a></td>
              </tr>
			<%
				}
			%>
              
            </table>
      </div>
      <div class="clear"></div>
      <div id="navigation">
          <ul id="nav">
          <%
			if(userMaster==null){
			%>
			<li class="top"><a href="${pageContext.request.contextPath}/appointment/appointmentForm.mpa" class="nav_link"><span>Create Appointment</span></a></li>
            <li class="top"><a href="${pageContext.request.contextPath}/appointment/checkAppointment.mpa" class="nav_link"><span>Check Your Appointments</span></a></li>
            <%
            } else if(userMaster.getRole().equalsIgnoreCase("Administrator")) {
			%>
            <li class="top"><a href="#" class="nav_link"><span>User Administration</span></a>
            <ul class="sub">
                    <li><a href="${pageContext.request.contextPath}/user/listUsers.mpa?temp=temp">View all users</a></li>
                    <li><a href="${pageContext.request.contextPath}/user/createUserForm.mpa">Add new user</a></li>
            </ul>
            </li>
            <li class="top"><a href="#" class="nav_link"><span>Appointment</span></a>
            <ul class="sub">
                <li><a href="${pageContext.request.contextPath}/appointment/listAppointment.mpa?temp=temp">List Appointments</a></li>
            </ul>
            </li>
            <li class="top"><a href="#" class="nav_link"><span>Admin Control</span></a>
                <ul class="sub">
                	<li><a href="${pageContext.request.contextPath}/bookingPeriod/addBookingPeriodForm.mpa">Add Blocking Period</a></li>
					<li><a href="${pageContext.request.contextPath}/bookingPeriod/listBookingPeriodBlock.mpa?temp=temp">List Blocking Period</a></li>
	 				<li><a href="${pageContext.request.contextPath}/bookingSlot/addBookingSlotForm.mpa">Booking Slot</a></li>
                </ul>
            </li>
            <li class="top"><a href="#" class="nav_link"><span>Check In Process</span></a>
            <ul class="sub">
            	<li><a href="${pageContext.request.contextPath}/checkin/checkinForm.mpa">Check In</a></li>
				<li><a href="${pageContext.request.contextPath}/appointment/checkAppointment.mpa">Check Appointment</a></li>
            </ul>
            </li>
            <li class="top"><a href="${pageContext.request.contextPath}/report/Report.mpa" class="nav_link"><span>Reports</span></a>
            </li>
            <%}else if(userMaster.getRole().equalsIgnoreCase("Counter")){ %>
            <li class="top"><a href="#" class="nav_link"><span>Check In Process</span></a>
            <ul class="sub">
            	<li><a href="${pageContext.request.contextPath}/checkin/checkinForm.mpa">Check In</a></li>
				<li><a href="${pageContext.request.contextPath}/appointment/checkAppointment.mpa">List Appointment</a></li>
            	
            </ul>
            </li>
            <li class="top"><a href="${pageContext.request.contextPath}/report/Report.mpa" class="nav_link"><span>Reports</span></a>
            </li>
            <% } %>
          </ul>
      </div>
      
    </div>
  
         
    
</body>

</html>