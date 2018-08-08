<%@page import="com.tatva.utils.MPAContext"%>
<%@page import="com.tatva.domain.UserMaster"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %>
<script type="text/javascript">



function deleteUser(userId){
	var answer=confirm("Are you sure want to delete this User?");
	if(answer){
		$('#userId').val(userId);
		var form = document.getElementById("mainform");
		form.action='../user/deleteUser.mpa';
		form.submit();
		//window.location.href="/user/deleteUser.mpa?method=delete&id="+id;
	}else{
		return false;
	}	
}

function updateUser(userId){
	
		$('#userId').val(userId);
		var form = document.getElementById("mainform");
		form.action='updateUserForm.mpa';
		form.submit();
}



</script>
<div id="content-outer">
    
<!-- start content -->
<div id="content">

	<!--  start page-heading -->
	<div id="page-heading">
		<h1><c:out value="${param['deletedSuccess']}" /></h1>
	</div>
	<!-- end page-heading -->

	<table border="0" width="100%" cellpadding="0" cellspacing="0" id="content-table">
	<tr>
		<th rowspan="3" class="sized"><img src="../images/shared/side_shadowleft.jpg" width="20" height="300" alt="" /></th>
		<th class="topleft"></th>
		<td id="tbl-border-top">&nbsp;</td>
		<th class="topright"></th>
		<th rowspan="3" class="sized"><img src="../images/shared/side_shadowright.jpg" width="20" height="300" alt="" /></th>
	</tr>
	<tr>
		<td id="tbl-border-left"></td>
		<td>
		<!--  start content-table-inner ...................................................................... START -->
		<div id="content-table-inner">
		
			<!--  start table-content  -->
			
			<!--
			  start message-green
			-->
			<c:if test="${not empty messages}">
				<div id="message-green">
				<table width="100%" cellspacing="0" cellpadding="0" border="0">
				<tbody><tr>
							<td class="green-left">${messages}</td>
							<td class="green-right"><a class="close-green"><img alt="" src="../images/table/icon_close_green.gif"></a></td>
				</tr>
				</tbody></table>
				</div>
			</c:if>
			
				<!--  start message-yellow -->
				 
				<!--  start product-table ..................................................................................... -->
				<form id="mainform" action="" method="post">
				<input type="hidden" id="userId" name="userId"/>
				<table border="0" width="100%" cellpadding="0" cellspacing="0" id="product-table">
				<tr>
					<th class="table-header-repeat line-left minwidth-1"><a href="#">User Id</a>	</th>
					<th class="table-header-repeat line-left minwidth-1"><a href="#">First Name</a>	</th>
					<th class="table-header-repeat line-left minwidth-1"><a href="#">Middle Name</a></th>
					<th class="table-header-repeat line-left"><a href="#">Last Name</a></th>
					<th class="table-header-repeat line-left"><a href="#">Joining Date</a></th>
					<th class="table-header-repeat line-left"><a href="#">Role</a></th>
					<th class="table-header-repeat line-left"><a href="#">Counter</a></th>
					<th class="table-header-options line-left"><a href="#">Options</a></th>
				</tr>
				<% int a = 0; %>
				<c:forEach var = "user" items = "${userMasterList}">
				<% if (a%2 == 0) {%>
				<tr>
				<% } else { %>
				<tr>
				<c:set var="userMaster" value="<%=(UserMaster)request.getSession().getAttribute(MPAContext.currentUser)%>"></c:set>
				<% }  a++;
				%>
				
					<td>${user.userId}</td>
					<td>${user.firstName}</td>
					<td>${user.middleName}</td>
					<td>${user.lastName}</td>
					<td>${user.dateOfJoiningString}</td>
					<td>${user.role}</td>
					<td>${user.counter}</td>
					<td class="options-width">
					<a href="#" title="Update" onclick="updateUser('${user.userId}')" class="icon-1 info-tooltip"></a>
					<c:if test="${userMaster.userId ne user.userId}">
						<a href="#" title="Delete" onclick="deleteUser('${user.userId}')" class="icon-2 info-tooltip"></a>
					</c:if>
					</td>
				</tr>
				
				</c:forEach>
				
				</table>
				<!--  end product-table................................... --> 
				</form>
			</div>
			<!--  end content-table  -->
		
			
			<div class="clear"></div>
		 
		</div>
		<!--  end content-table-inner ............................................END  -->
		</td>
		<td id="tbl-border-right"></td>
	</tr>
	<tr>
		<th class="sized bottomleft"></th>
		<td id="tbl-border-bottom">&nbsp;</td>
		<th class="sized bottomright"></th>
	</tr>
	</table>
	<div class="clear">&nbsp;</div>

</div>
<!--  end content -->
</div>