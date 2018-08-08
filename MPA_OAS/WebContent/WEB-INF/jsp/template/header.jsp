<%@page import="com.tatva.utils.MPAContext"%>
<%@page import="com.tatva.domain.UserMaster"%>
<!-- Start: page-top-outer -->
<div id="page-top-outer">    
<!-- Start: page-top -->
<div id="page-top">

	<!-- start logo -->
	<div id="logo">
	<a href=""><img src="../images/shared/logo.png" width="226" height="73" alt="" /></a>
	</div>
	<!-- end logo -->
	
	<!--  start top-search -->
	<div id="top-search">
	<!--  	<table border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td><input type="text" value="Search" onblur="if (this.value=='') { this.value='Search'; }" onfocus="if (this.value=='Search') { this.value=''; }" class="top-search-inp" /></td>
		<td>
		 
		<select  class="styleds	elect">
			<option value="">All</option>
			<option value="">Products</option>
			<option value="">Categories</option>
			<option value="">Clients</option>
			<option value="">News</option>
		</select> 
		 
		</td>
		<td>
		<input type="image" src="../images/shared/top_search_btn.gif"  />
		</td>
		</tr>
		</table>
	-->
	</div>
 	<!--  end top-search -->
 	<div class="clear"></div>

</div>
<!-- End: page-top -->

</div>
<!-- End: page-top-outer -->
	
<div class="clear">&nbsp;</div>
 
<!--  start nav-outer-repeat................................................................................................. START -->
<div class="nav-outer-repeat"> 
<!--  start nav-outer -->
<div class="nav-outer"> 

	<%
		UserMaster userMaster=(UserMaster)request.getSession().getAttribute(MPAContext.currentUser);
	%>

		<!-- start nav-right -->
		<div id="nav-right">
		
			<div class="nav-divider">&nbsp;</div>
			<div class="showhide-account"><!--  <img src="../images/shared/nav/nav_myaccount.gif" width="93" height="14" alt="" />--></div>
			<div class="nav-divider">&nbsp;</div>
			<%
				if(userMaster!=null){
			%>
			<a href="${pageContext.request.contextPath}/login/logout.mpa" id="logout"><img src="../images/shared/nav/nav_logout.gif" width="64" height="14" alt="" /></a>
			<%
				}else{
			%>
				<a href="${pageContext.request.contextPath}/login/loginForm.mpa" id="logout"><img src="../images/shared/nav/nav_login.gif" width="64" height="14" alt="" /></a>
			<%
				}
			%>
			<div class="clear">&nbsp;</div>
		
			<!--  start account-content -->
			<div class="account-content">
			<div class="account-drop-inner">
				<a href="" id="acc-settings">Settings</a>
				<div class="clear">&nbsp;</div>
				<div class="acc-line">&nbsp;</div>
				<a href="" id="acc-details">Personal details </a>
				<div class="clear">&nbsp;</div>
				<div class="acc-line">&nbsp;</div>
				<a href="" id="acc-project">Project details</a>
				<div class="clear">&nbsp;</div>
				<div class="acc-line">&nbsp;</div>
				<a href="" id="acc-inbox">Inbox</a>
				<div class="clear">&nbsp;</div>
				<div class="acc-line">&nbsp;</div>
				<a href="" id="acc-stats">Statistics</a> 
			</div>
			</div>
			<!--  end account-content -->
		
		</div>
		<!-- end nav-right -->


		<!--  start nav -->
		<div class="nav">
		<div class="table">
		
		<%
			if(userMaster==null){
		%>
			<div class="nav-divider">&nbsp;</div>
		
			<ul class="select"><li><a href="#nogo"><b>Appointment</b><!--[if IE 7]><!--></a><!--<![endif]-->
			<!--[if lte IE 6]><table><tr><td><![endif]-->
			<div class="select_sub">
			<ul class="sub">
				<li><a href="${pageContext.request.contextPath}/appointment/appointmentForm.mpa">Create Appointment</a></li>
				<li><a href="${pageContext.request.contextPath}/appointment/checkAppointment.mpa">Check Your Appointments</a></li>
			</ul>
		</div>
		<!--[if lte IE 6]></td></tr></table></a><![endif]-->
		</li>
		</ul>
		<%}else	if(userMaster.getRole().equalsIgnoreCase("Administrator")){
			
		%>
					
		<div class="nav-divider">&nbsp;</div>
		                    
		<ul class="select"><li><a href="#nogo"><b>User Administration</b><!--[if IE 7]><!--></a><!--<![endif]-->
		<!--[if lte IE 6]><table><tr><td><![endif]-->
		<div class="select_sub show">
			<ul class="sub">
				<li><a href="${pageContext.request.contextPath}/user/listUsers.mpa">View all users</a></li>
				<li class="sub_show"><a href="${pageContext.request.contextPath}/user/createUserForm.mpa">Add User</a></li>
			</ul>
		</div>
		<!--[if lte IE 6]></td></tr></table></a><![endif]-->
		</li>
		</ul>
		
		
		<div class="nav-divider">&nbsp;</div>
		
		<ul class="select"><li><a href="#nogo"><b>Appointment</b><!--[if IE 7]><!--></a><!--<![endif]-->
		<!--[if lte IE 6]><table><tr><td><![endif]-->
		<div class="select_sub">
			<ul class="sub">
				<li><a href="${pageContext.request.contextPath}/appointment/listAppointment.mpa">List Appointment</a></li>
			</ul>
		</div>
		<!--[if lte IE 6]></td></tr></table></a><![endif]-->
		</li>
		</ul>
		
		<div class="nav-divider">&nbsp;</div>
		<ul class="select"><li onclick=""><a href="#nogo"><b>Admin Control</b><!--[if IE 7]><!--></a><!--<![endif]-->
		<!--[if lte IE 6]><table><tr><td><![endif]-->
		<div class="select_sub">
			<ul class="sub">
				<li><a href="${pageContext.request.contextPath}/bookingPeriod/addBookingPeriodForm.mpa">Add Booking Period</a></li>
				<li><a href="${pageContext.request.contextPath}/bookingPeriod/listBookingPeriodBlock.mpa">List Booking Period</a></li>
	 			<li><a href="${pageContext.request.contextPath}/bookingSlot/addBookingSlotForm.mpa">Booking Slot</a></li>

			</ul>
		</div>
		<!--[if lte IE 6]></td></tr></table></a><![endif]-->
		</li>
		</ul>
		
		
		<div class="nav-divider">&nbsp;</div>
		
		<ul class="select"><li><a href="#nogo"><b>Check In Process</b><!--[if IE 7]><!--></a><!--<![endif]-->
		<!--[if lte IE 6]><table><tr><td><![endif]-->
		<div class="select_sub">
			<ul class="sub">
				<li><a href="${pageContext.request.contextPath}/checkin/checkinForm.mpa">Check In</a></li>
				<li><a href="${pageContext.request.contextPath}/appointment/checkAppointment.mpa">List Appointment</a></li>
			</ul>
		</div>
		<!--[if lte IE 6]></td></tr></table></a><![endif]-->
		</li>
		</ul>
		
		
		<div class="nav-divider">&nbsp;</div>
		
		<ul class="select"><li><a href="${pageContext.request.contextPath}/report/Report.mpa"><b>Reports</b><!--[if IE 7]><!--></a><!--<![endif]-->
			</ul>
		</div>
		
		<%}else if(userMaster.getRole().equalsIgnoreCase("Counter")){ %>
		
		<div class="nav-divider">&nbsp;</div>
		
		<ul class="select"><li><a href="#nogo"><b>Check In Process</b><!--[if IE 7]><!--></a><!--<![endif]-->
		<!--[if lte IE 6]><table><tr><td><![endif]-->
		<div class="select_sub">
			<ul class="sub">
				<li><a href="${pageContext.request.contextPath}/checkin/checkinForm.mpa">Check In</a></li>
				<li><a href="${pageContext.request.contextPath}/appointment/checkAppointment.mpa">List Appointment</a></li>
			</ul>
		</div>
		<!--[if lte IE 6]></td></tr></table></a><![endif]-->
		</li>
		</ul>
		
		<div class="nav-divider">&nbsp;</div>
		
		<ul class="select"><li><a href="#nogo"><b>Reports</b><!--[if IE 7]><!--></a><!--<![endif]-->
		<!--[if lte IE 6]><table><tr><td><![endif]-->
		<div class="select_sub">
			<ul class="select"><li><a href="${pageContext.request.contextPath}/report/Report.mpa"><b>Reports</b><!--[if IE 7]><!--></a><!--<![endif]-->
			</ul>
		</div>
		</li>
		</ul>
		
		<%}%>
			
		<div class="clear"></div>
		</div>
		<div class="clear"></div>
		</div>
		<!--  start nav -->
</div>
<div class="clear"></div>
<!--  start nav-outer -->
</div>
<!--  start nav-outer-repeat................................................... END -->