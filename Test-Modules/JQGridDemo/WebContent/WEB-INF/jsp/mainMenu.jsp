		<%@page import="com.tatva.model.User"%>
<ul class="menuUl">
			<%
				User u=(User)request.getSession().getAttribute("user");
				if(u!=null)
				{
			%>
			<li> <a href="${pageContext.request.contextPath}/returnHome.html">Home</a> </li>
			<li><a href="${pageContext.request.contextPath}/listUser1.html">List All User</a></li>
		<!-- 	<li><a href="${pageContext.request.contextPath}/viewProfile.html">Update Profile</a></li>
			<li><a href="${pageContext.request.contextPath}/listUserTemp.html">List Users</a></li>  -->
			<li><a href="${pageContext.request.contextPath}/logout.html">Logout</a></li>
			<%
				}
				else
				{
			%>	
			<li><a href="${pageContext.request.contextPath}/login.html">Login</a></li>
			<li><a href="${pageContext.request.contextPath}/signUp.html">Sign Up</a></li>	
			<%
				}
			%>
		</ul>