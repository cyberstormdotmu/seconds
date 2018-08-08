<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="../css/stylesheet.css" type="text/css"
	media="screen" title="default" />
	
	<link rel="stylesheet" href="../css/colorbox.css" />
		<script src="../js/jquery10.min.js"></script>
		<script src="../js/jquery.colorbox.js"></script>
	
<script type="text/javascript">
	$(document).ready(function() {
		$("#userId").focus();
		$(".inline").colorbox({inline:true, width:"35%", escKey: false,overlayClose: false});
		$(document).pngFix();
	});

	function test(){
		$("#errorBlock").hide();
	}
	
	function check(){
		if($.trim(document.getElementById("userId").value)==""){
			$("#errorBlock").show();
			return false;
		}
		return true;
	}
</script>
<title>MPA - Login</title>
</head>
<body>
	<div class="login_area">
 <!--Login Area Start-->
 	<div class="login_topspac"></div>
    <div class="login_top"></div>
    <div class="login_middle">
    	<div class="login_header">
        	<div class="login_logo"><a href="${pageContext.request.contextPath}/login/homePage.mpa"><img src="../images/logo.png" alt="" /></a></div>
            <div class="key_img"><img src="../images/key.png" alt="" /></div>
        </div>
        <div class="login_content">
        	<form:form action="${pageContext.request.contextPath}/login/getLogin.mpa" method="post">
        	<table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td width="120" align="right"></td>
                <td><h2>Login Area</h2></td>
              </tr>
              <tr>
                <td align="right">User Id:</td>
                <td><input id="userId" name="userId" type="text" style="width:225px;" value="" /></td>
              </tr>
              <tr>
                <td align="right">Password:</td>
                <td><input name="password" onfocus="this.value=''" type="password" style="width:225px;" value="" /></td>
              </tr>
              <tr>
						<td></td>
						<td><font color="pink">${loginError}</font></td>
					</tr>
              <tr>
                <td>&nbsp;</td>
                <td><input name="" type="submit" value="LOGIN" class="inputbutton btn_small" />
                <a class='inline' href="#inline_content" onclick="test()">Forgot Password?</a> 
                </td>
              </tr>
            </table>
            </form:form>
        </div>
    </div>
    <div class="login_bottom"></div>
    
    
    <div style='display:none'>
			<div id='inline_content' style='padding:10px; background:#fff;'>
			<h3>Your Password will be sent to your email address</h3><br><br>
				<form method="post" action="${pageContext.request.contextPath}/login/forgotPassword.mpa">
					
					<table cellspacing="10">
						<tr>
							<td>Enter your User Id:<span style="color: red;"> *</span></td>
							<td> <input type="text" id="userId" name="userId" maxlength="20"/> </td>
						</tr>
						<tr>
							<td></td>
							<td>
								<div id="errorBlock" style="color: red; display: none;">Please enter UserId</div>
							</td>
						</tr>
						<tr>
							<td></td>
							<td><input type="submit" value="Submit" class="inputbutton btn_small" onclick="javascript:return check();"/></td>
						</tr>
					</table>
				
				</form>
				
			</div>
		</div>
    
    
    
 <!--Login Area End-->
</div>
</body>
</html>