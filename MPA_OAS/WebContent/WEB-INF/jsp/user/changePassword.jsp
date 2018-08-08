<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">


<script type="text/javascript">

$(document).ready(function () {
	
	$("#oldPassword").focus();
    $('#newPassword').bind('copy paste', function (e) {
       e.preventDefault();
    });
  });
	
				function checkChangePassword(){
		
					document.getElementById("newPasswordDiv").innerHTML="";
					document.getElementById("oldPasswordDiv").innerHTML ="";
					document.getElementById("rePasswordDiv").innerHTML ="";
					document.getElementById("misMatchDiv").innerHTML ="";

					var flag=true;
						
					if($.trim(document.getElementById("oldPassword").value)==""){
						document.getElementById("oldPasswordDiv").innerHTML ="Enter Old Password";
						flag=false;
					}
					if($.trim(document.getElementById("newPassword").value)==""){
						document.getElementById("newPasswordDiv").innerHTML ="Enter New Password";
						flag=false;
					}
					if($.trim(document.getElementById("rePassword").value)==""){
						document.getElementById("rePasswordDiv").innerHTML ="Enter Retype Password";
						flag=false;
					}

					if(!($.trim(document.getElementById("newPassword").value)=="" && $.trim(document.getElementById("rePassword").value)=="")){
						if(!($.trim(document.getElementById("newPassword").value) == $.trim(document.getElementById("rePassword").value))){
							document.getElementById("misMatchDiv").innerHTML ="Password And Retype Password Mismatch";
							flag=false;
						}
					}
				
					return flag;
				}
		
		</script>

<div id="wrapper">
<div id="middle">
      <!--content start-->
      	 <h1>Change Your Password</h1>
         <div class="form_field top_pad">
         	<div class="titlebg"><h2>Password Details</h2></div>
            <div class="content_block">
           <h3 align="center" style="color: red;">${message}</h3>
	<form method="post" action="${pageContext.request.contextPath}/login/changePasswordForm.mpa" id="id-form">
		<table cellspacing="10" cellpadding="0" border="0" id="id-form">
			<tbody>
			<tr>
				<th align="right">Enter Your Old Password: </th>
				<td>
						<input type="password" name="oldPassword" id="oldPassword" class="inp-form">
						<span id="oldPasswordDiv" style="color: red;"></span>
				</td>
			</tr>
			<tr>
				<th align="right">Enter Your New Password: </th>
				<td>
						<input type="password" name="newPassword" id="newPassword" class="inp-form">
						<span id="newPasswordDiv" style="color: red;"></span>
				</td>
			</tr>
			<tr>
				<th align="right">Re type Password: </th>
				<td>
						<input type="password" name="rePassword" id="rePassword" class="inp-form">
						<span id="rePasswordDiv" style="color: red;"></span>
				</td>
			</tr>
			<tr>
				<td><span id="misMatchDiv" style="color: red;"></span></td>
				
			</tr>
			<tr>
				<td></td>
				<td>
					<input type="submit" class="inputbutton btn_small" value="Submit" onclick="javascript:return checkChangePassword();"/>
				</td>
			</tr>
		</table>
		</form>
		           
            </div>
         </div>
      <!--content end-->
    </div>
    <!--main area end-->
  </div>