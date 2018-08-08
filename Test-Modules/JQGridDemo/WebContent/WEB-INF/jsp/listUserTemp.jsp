<%@page import="com.tatva.model.User"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript">
function getInfo(userId)
{
    $("div"+userId).hide();
    $("vehicleDataRow"+userId+"").html('');
    var vD = document.getElementById("vehicleData"+userId+"");
    var vD1 = document.getElementById("vehicleDataRow"+userId+"");
    $.ajax({
            url: "list/"+userId,
    		type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType:"json",
            async: true,
	        success: function(result) {
	        	//	alert("Second..........#vehicleData"+userId+"");
	        	alert("inside success....."+result);
	        	 var str="<td align='center'>"+result.name+"</td> <td align='center'>"+result.type+"</td>";
	        	// alert("Checking.... -"+str);
	        	 vD.style.display='block';
	        	 vD1.style.display='block';
	            /* $("#vehicleData"+userId+"")
	                .html(str)
	                .slideDown('slow'); */
	                $("#div").slideDown('slow').delay(1000);
	            $("#vehicleDataRow"+userId+"").html(str);
	            
	        },
	        error: function(jqXHR, textStatus, errorThrown,response) {
	            alert(jqXHR.status + ' '+ errorThrown+' '+textStatus+' '+response);
	            if(jqXHR.status==500)
	            {
		           	 $("#vehicleData"+userId+"")
		                .html("You Dont Have Any Vehicle")
		                .slideDown('slow');
	           	}
	        }
    });
 };

 /* $(document).ready(function(){
	  sendAjax();
 });
 
 function sendAjax() {
	 
	 $.ajax({ 
	     url: "listget", 
	     type: 'POST', 
	     
	     data: {
	    	 fname : "Sushant",
	    	 lname : "Banugariya"
	     }, 
	     contentType: 'application/json',
	     mimeType: 'application/json',
	     success: function(data) { 
	         alert(data.fname + " " + data.lname);
	     },
	     error:function(data,status,er) { 
	         alert("error: "+data+" status: "+status+" er:"+er);
	     }
	 }); */
  //}
</script>


</head>
<body>
	<center>
	<%
		User u=(User)session.getAttribute("user");
	    int rId =0;
		if(u != null){
			rId=u.getRoleId();	
		}
	%>
		<h1>List Of Users</h1>
		
		<c:if test="${not empty users }">
			<table border="0">	
				<tr>
					<th>User Id</th>
					<th>User Name</th>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Role Name</th>
					<th>Is Active</th>
					<th>Vehicle List</th>
					<%	if(rId==1)
						{
					%>
					<th colspan="2">Actions</th>
					<%
						}
					%>
				</tr>
				
		<c:forEach var="current" items="${users }">
				<tr>
					<td>${ current.userId}</td>
					<td>${ current.userName}</td>
					<td>${ current.firstName}</td>
					<td>${ current.lastName}</td>
					
					<c:if test="${current.roleId==1 }">
					<td>Admin</td>
					</c:if>
					<c:if test="${current.roleId==2 }">
					<td>User</td>
					</c:if>
					<td>${ current.active}</td>
					<td>
<%-- 					<a href="${pageContext.request.contextPath}/list/${current.userId}">Expand</a> --%>
					<input type="button" value="Expand" onclick="getInfo(${current.userId})">
					</td>
					<%	if(rId==1)
						{
					%>
					<td><a href="editUser/${current.userId }">Edit</a></td>
					<td><a href="deleteUser/${current.userId }">Delete</a></td>
					<%
						}
					%>
				</tr>
				<div id="div${current.userId}">
					<tr id="vehicleData${current.userId }" style="display: none;"> 
						<td colspan="7" style="width: 50%">Name</td>
						<td colspan="7" style="width: 50%">Type</td>
					</tr>
					<tr id="vehicleDataRow${current.userId }" style="display: none;">
					</tr>
				</div>
				<%-- <tr id="temp${current.userId }" style="display: ;">
					<table style="display: none;">
						<tr  id="vehicleData${current.userId }">
							<td>Name</td>
							<td>Type</td>
						</tr>
					</table>
				</tr> --%>
			<%-- <div id="vehicleData${current.userId }" style="display: none;">
				 <table  style="display: none;">
					<tr>
						<td>HIEE</td>
						<td>HIE</td>
					</tr>
				</table> 
			</div>	 --%>
		
		</c:forEach>
		</table>
	</c:if>
	</center>
</body>
</html>