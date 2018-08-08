<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="com.tatva.utils.MPAContext"%>
<%@page import="com.tatva.domain.UserMaster"%>
<%@page import = "com.tatva.utils.SearchConstants" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page isELIgnored="false"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View All Users</title>
<script type="text/javascript">


var flag=true;
$(function()
		  {
		$("#searchDiv").hide();
		   $('.date-pick').datePicker({startDate:'01/01/1996'});
		  });

	var el = document.getElementById('foo');
	el.onclick = changeOrder('userId');

	
	function slider(){
			
		if(flag){
			$("#searchDiv").slideDown(200);
			$("#searchDiv").show();
			flag=false;
		}else{
			$("#searchDiv").slideUp(200);
			
			flag=true;
		}
		
	}
	
	
	function changeRows() {
		var records = document.getElementById("recordsPerPage").value;
		// 	var curPage = document.getElementById("cPage").value;
		var curPage = 1;
		window.location.href = "../user/listUsers.mpa?recordsPerPage="
				+ records + "&page=" + curPage + "";
	}

	function deleteUser(userId) {
		var currentUser = document.getElementById("currentUser").value;
		if (userId == currentUser) {
			alert("You Cant Delete YourSelf....");
			return false;
		}
		var answer = confirm("Are you sure want to delete this User?");
		if (answer) {
			window.location.href = '../user/deleteUser.mpa?userId=' + userId
					+ "";
			//window.location.href="/user/deleteUser.mpa?method=delete&id="+id;
		} else {
			return false;
		}
	}

	function updateUser(userId,role) {
			
		var flagUserId = userId;
		var flagUserRole = role;
		var availableresult = null;
		$.ajax({
			url:"flagUpdateUser.mpa?flagUserId="+flagUserId+"&flagUserRole="+flagUserRole,
			type:"POST",
			contentType: "application/json; charset=utf-8",
	        dataType:"json",
	        success:function(result){
	        	
	        	availableresult = result;
				
	        	if(availableresult == "setFlag"){
					window.location.href = '../user/updateUserForm.mpa';
				}
	        },
	        error:function(errorThrown){
				
	        }
		});			
		
		
	//	
	
	
	}

	function changeOrder(property) {
	
		var value = document.getElementById(property+"grid").value;
		window.location.href = '../user/orderUserList.mpa?' + property + '='
				+ value + '';
	}
</script>
</head>
<body>
	<div id="wrapper">
		<!--main area start-->
		<div id="middle">
			<!--content start-->
			
			<input type="hidden" id="currentUser" name="currentUser"
				value="<%=MPAContext.currentUser%>">

			<div class="form_grid top_pad" >
				<div class="titlebg" ><h2><span id="search"  onclick="slider();" style="width:100%; color: white; cursor: pointer;">Search User<img align="right" style="margin-right: 3px;" src="../images/down-ar.png"/></span></h2>
         		</div>
					<div class="content_block" id="searchDiv">
						<form:form action= "searchUser.mpa" method="post" commandName="searchUser" >
						<table width="100%" cellpadding="0" cellspacing="10" border="0">
							<tr>
											<td><spring:message code="lable.sUserId"/></td>
											
											<td>
											<select name="userIdRel">
											<option value="<%=SearchConstants.EQUALS %>"><%=SearchConstants.EQUALS %></option>
											<option value="<%=SearchConstants.CONTAINS %>"><%=SearchConstants.CONTAINS %></option>
											<option value="<%=SearchConstants.LT %>"><%=SearchConstants.LT %></option>
											<option value="<%=SearchConstants.LT_EQ %>"><%=SearchConstants.LT_EQ %></option>
											<option value="<%=SearchConstants.GT %>"><%=SearchConstants.GT %></option>
											<option value="<%=SearchConstants.GT_EQ %>"><%=SearchConstants.GT_EQ %></option>
											<option value="<%=SearchConstants.NOT_EQUALS %>"><%=SearchConstants.NOT_EQUALS %></option>
											</select>
											</td>
										<td>
										<form:input path="userId"/>
										</td>

								</tr>
									
								<tr>
									<td><spring:message code="lable.sFirstName"/></td>
									<td>
											<select name="firstNameRel">
											<option value="<%=SearchConstants.EQUALS %>"><%=SearchConstants.EQUALS %></option>
											<option value="<%=SearchConstants.CONTAINS %>"><%=SearchConstants.CONTAINS %></option>
											<option value="<%=SearchConstants.LT %>"><%=SearchConstants.LT %></option>
											<option value="<%=SearchConstants.LT_EQ %>"><%=SearchConstants.LT_EQ %></option>
											<option value="<%=SearchConstants.GT %>"><%=SearchConstants.GT %></option>
											<option value="<%=SearchConstants.GT_EQ %>"><%=SearchConstants.GT_EQ %></option>
											<option value="<%=SearchConstants.NOT_EQUALS %>"><%=SearchConstants.NOT_EQUALS %></option>
											</select>
											</td>
								
								<td>
										<form:input path="firstName" />
										</td>
								
								</tr>

								<tr>
								<td ><spring:message code="lable.sMiddleName"/></td>
								<td>
											<select name="middleNameRel">
											<option value="<%=SearchConstants.EQUALS %>"><%=SearchConstants.EQUALS %></option>
											<option value="<%=SearchConstants.CONTAINS %>"><%=SearchConstants.CONTAINS %></option>
											<option value="<%=SearchConstants.LT %>"><%=SearchConstants.LT %></option>
											<option value="<%=SearchConstants.LT_EQ %>"><%=SearchConstants.LT_EQ %></option>
											<option value="<%=SearchConstants.GT %>"><%=SearchConstants.GT %></option>
											<option value="<%=SearchConstants.GT_EQ %>"><%=SearchConstants.GT_EQ %></option>
											<option value="<%=SearchConstants.NOT_EQUALS %>"><%=SearchConstants.NOT_EQUALS %></option>
											</select>
											</td>
										<td>
										<form:input path="middleName"/>
										</td>
								
								</tr>

								<tr>
								<td><spring:message code="lable.sLastName"/></td>
								<td>
											<select name="lastNameRel">
											<option value="<%=SearchConstants.EQUALS %>"><%=SearchConstants.EQUALS %></option>
											<option value="<%=SearchConstants.CONTAINS %>"><%=SearchConstants.CONTAINS %></option>
											<option value="<%=SearchConstants.LT %>"><%=SearchConstants.LT %></option>
											<option value="<%=SearchConstants.LT_EQ %>"><%=SearchConstants.LT_EQ %></option>
											<option value="<%=SearchConstants.GT %>"><%=SearchConstants.GT %></option>
											<option value="<%=SearchConstants.GT_EQ %>"><%=SearchConstants.GT_EQ %></option>
											<option value="<%=SearchConstants.NOT_EQUALS %>"><%=SearchConstants.NOT_EQUALS %></option>
											</select>
											</td>
											
									<td>
										<form:input path="lastName"/>
										</td>
										
																								
								</tr>

								<tr>
								<td><spring:message code="lable.sJoiningDate"/></td>
								<td>
											<select name="joiningDateRel" style="width: 90px;">
											<option value="<%=SearchConstants.EQUALS %>"><%=SearchConstants.EQUALS %></option>
											<option value="<%=SearchConstants.LT %>"><%=SearchConstants.LT %></option>
											<option value="<%=SearchConstants.LT_EQ %>"><%=SearchConstants.LT_EQ %></option>
											<option value="<%=SearchConstants.GT %>"><%=SearchConstants.GT %></option>
											<option value="<%=SearchConstants.GT_EQ %>"><%=SearchConstants.GT_EQ %></option>
											<option value="<%=SearchConstants.NOT_EQUALS %>"><%=SearchConstants.NOT_EQUALS %></option>
											</select>
											</td>
								
								<td>
										<form:input path="dateOfJoiningString"  cssClass="date-pick inp-form calendar-text"  readonly="true"/>
										</td>
								
								</tr>

								<tr>
								<td><spring:message code="lable.sRole"/></td>
								<td>
											<select name="roleRel">
											<option value="<%=SearchConstants.EQUALS %>"><%=SearchConstants.EQUALS %></option>
											<option value="<%=SearchConstants.CONTAINS %>"><%=SearchConstants.CONTAINS %></option>
											<option value="<%=SearchConstants.LT %>"><%=SearchConstants.LT %></option>
											<option value="<%=SearchConstants.LT_EQ %>"><%=SearchConstants.LT_EQ %></option>
											<option value="<%=SearchConstants.GT %>"><%=SearchConstants.GT %></option>
											<option value="<%=SearchConstants.GT_EQ %>"><%=SearchConstants.GT_EQ %></option>
											<option value="<%=SearchConstants.NOT_EQUALS %>"><%=SearchConstants.NOT_EQUALS %></option>
											</select>
											</td>
											
											
								<td>
										<form:input path="role"/>
										</td>
											
								</tr>

								<tr>
								<td><spring:message code="lable.sCounter"/></td>
								<td>
											<select name="counterRel">
											<option value="<%=SearchConstants.EQUALS %>"><%=SearchConstants.EQUALS %></option>
											<option value="<%=SearchConstants.CONTAINS %>"><%=SearchConstants.CONTAINS %></option>
											<option value="<%=SearchConstants.LT %>"><%=SearchConstants.LT %></option>
											<option value="<%=SearchConstants.LT_EQ %>"><%=SearchConstants.LT_EQ %></option>
											<option value="<%=SearchConstants.GT %>"><%=SearchConstants.GT %></option>
											<option value="<%=SearchConstants.GT_EQ %>"><%=SearchConstants.GT_EQ %></option>
											<option value="<%=SearchConstants.NOT_EQUALS %>"><%=SearchConstants.NOT_EQUALS %></option>
											</select>
											</td>
											
									<td>
										<form:input path="counter"/>
										</td>	
								
								</tr>
								<tr>
								<td colspan="3" class="submit">
								
								<input type="submit" class="inputbutton btn_small" value="Search"/>
								</td>
								
								</tr>
								</table>
						</form:form>
					</div>
				
				</div>
				<br/><br/>
				<c:choose>
							<c:when test="${userMasterList.size() == 0}">
							<h3>There are No User According to your Search</h3>
							</c:when>							
							<c:otherwise>
				<h1>User List</h1>
				<div class="form_grid top_pad">
					<div class="titlebg">
						<h2>User List</h2>
					</div>
					<div class="content_block" >
						<div id="page-heading">
							<h1>
								<c:out value="${param['deletedSuccess']}" />
							</h1>
						</div>
						<c:if test="${not empty messages}">
							<div id="message-green">
								<table width="100%" cellspacing="0" cellpadding="0" border="0">
									<tbody>
										<tr>
											<td class="green-left">${messages}</td>
											<td class="green-right"><a class="close-green"><img
													alt="" src="../images/table/icon_close_green.gif"/></a></td>
										</tr>

										<tr style="height: 5px;"></tr>
									</tbody>
								</table>
							</div>
						</c:if>
						<table width="100%" class="grid" border="0" cellspacing="1"
							cellpadding="0">
							<tr>
								<th class="table-header-repeat line-left minwidth-1" width="5%"><a
									href="#">No.</a></th>
								<th class="table-header-repeat line-left minwidth-1"><a
									href="javascript:;" onclick="changeOrder('userId');">User
										Id</a><input type="hidden" id="userIdgrid" name="userId"
									value="${userIdOrder}" /></th>
								<th class="table-header-repeat line-left minwidth-1"><a
									href="javascript:;" onclick="changeOrder('firstName');">First
										Name</a><input type="hidden" id="firstNamegrid" name="firstName"
									value="${firstNameOrder}" /></th>
								<th class="table-header-repeat line-left minwidth-1"><a
									href="javascript:;" onclick="changeOrder('middleName');">Middle
										Name</a><input type="hidden" id="middleNamegrid" name="middleName"
									value="${middleNameOrder}" /></th>
								<th class="table-header-repeat line-left"><a
									href="javascript:;" onclick="changeOrder('lastName');">Last
										Name</a><input type="hidden" id="lastNamegrid" name="lastName"
									value="${lastNameOrder}" /></th>
								<th class="table-header-repeat line-left" width="10%"><a
									href="javascript:;" onclick="changeOrder('dateOfJoining');">Joining
										Date</a><input type="hidden" id="dateOfJoininggrid"
									name="dateOfJoining" value="${dateOfJoiningOrder}" /></th>
								<th class="table-header-repeat line-left" width="10%"><a
									href="javascript:;" onclick="changeOrder('role');">Role</a><input
									type="hidden" id="rolegrid" name="role" value="${roleOrder}" /></th>
								<th class="table-header-repeat line-left" width="5%"><a
									href="javascript:;" onclick="changeOrder('counter');">Counter</a><input
									type="hidden" id="countergrid" name="counter"
									value="${counterOrder }" /></th>
								<th width="5%">Edit</th>
								<th width="5%">Delete</th>
							</tr>
							<%
								int a = request.getAttribute("start") == null ? 0
										: (Integer) request.getAttribute("start");
							%>
							
							<c:forEach var="user" items="${userMasterList}">
														
								<%
									if (a % 2 == 0) {
								%>
								<tr class="row2">
									<%
										} else {
									%>
								
								<tr class="row1">
									<c:set var="userMaster"
										value="<%=(UserMaster) request.getSession()
								.getAttribute(MPAContext.currentUser)%>"></c:set>
									<%
										}
											a++;
									%>
									<td style="text-align: center;"><%=a%></td>
									<td style="text-align: center;">${user.userId}</td>
									<td style="text-align: center;">${user.firstName}</td>
									<td style="text-align: center;">${user.middleName}</td>
									<td style="text-align: center;">${user.lastName}</td>
									<td style="text-align: center;">${user.dateOfJoiningString}</td>
									<td style="text-align: center;">${user.role}</td>
									<td style="text-align: center;">${user.counter}</td>
									<td align="center"><c:if test="${user.role ne 'Administrator'}"><input type="image"
										src="../images/ic_edit.png"
										onclick="updateUser('${user.userId}','${user.role}')" /></c:if></td>
									<td align="center">
									<c:if test="${user.role ne 'Administrator'}">
											<img src="../images/ic_delete.png" alt="" title="Delete"
												onclick="deleteUser('${user.userId}')" />
										</c:if></td>
								</tr>

							</c:forEach>
							
							

						</table>
						<div class="paging">
							<input type="hidden" id="cPage" name="cPage"
								value="${currentPage}">
							<ul>
								<c:if test="${currentPage gt 1 }">
									<li><a
										href="${pageContext.request.contextPath}/user/listUsers.mpa?page=${currentPage - 1}">&laquo;
											Prev</a></li>
								</c:if>
								<c:forEach begin="1" end="${noOfPages}" var="i">
									<c:choose>
										<c:when test="${currentPage eq i }">
											<li><a href="#" class="active">${i}</a></li>
										</c:when>
										<c:otherwise>
											<li><a
												href="${pageContext.request.contextPath}/user/listUsers.mpa?page=${i}">${i}</a></li>
										</c:otherwise>
									</c:choose>
								</c:forEach>
								<c:if test="${currentPage lt noOfPages }">
									<li><a
										href="${pageContext.request.contextPath}/user/listUsers.mpa?page=${currentPage + 1}">Next
											&raquo;</a></li>
								</c:if>
							</ul>
							<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; No of rows: &nbsp; <select
								name="recordsPerPage" id="recordsPerPage" style="width: 60px;">
									<%
										int userRows = session.getAttribute("userRows") == null ? 10
												: (Integer) session.getAttribute("userRows");
										if (userRows == 5) {
									%>
									<option value="5" selected="selected">5</option>
									<%
										} else {
									%>
									<option value="5">5</option>
									<%
										}
										if (userRows == 10) {
									%>
									<option value="10" selected="selected">10</option>
									<%
										} else {
									%>
									<option value="10">10</option>
									<%
										}
										if (userRows == 15) {
									%>
									<option value="15" selected="selected">15</option>
									<%
										} else {
									%>
									<option value="15">15</option>
									<%
										}
										if (userRows == 20) {
									%>
									<option value="20" selected="selected">20</option>
									<%
										} else {
									%>
									<option value="20">20</option>
									<%
										}
									%>
							</select> <input type="button" value="Go" onclick="changeRows()">
							</span>

						</div>
					</div>
				</div>
				</c:otherwise>
							</c:choose>
				<!--content end-->
			</div>
			<!--main area end-->
		</div>
</body>
</html>