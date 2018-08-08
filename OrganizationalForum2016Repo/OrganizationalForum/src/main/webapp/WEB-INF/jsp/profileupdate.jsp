<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>Edit Profile</title>
<link
	href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css"
	rel="stylesheet" />
<link href="<%=request.getContextPath()%>/resources/css/style.css"
	rel="stylesheet">
<link href="<%=request.getContextPath()%>/resources/css/menu.css"
	rel="stylesheet">

<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->

<script src="<%=request.getContextPath()%>/resources/js/jquery.min.js"></script>

<!-- TatvaSoft - set current user from scope -->
<c:set var="currentUser" value="${sessionScope.currentLoggedinUser}"/>
<c:if test="${currentUser.role.roleid eq 2}">
	<script>
			 function disableEmail() {
			 	document.getElementById("email").disabled = true;
			 }
	</script>
</c:if>

</head>
<body onload="disableEmail()">
	<div id="wrap">
		<!-- Header Start -->
		<header class="sticky-header">
    	<div class="sticky-bar">
        
          <div class="topmenu">
          	<a class="menu-toggle" href="#menu">
              <i class="menu-icon">
                <span class="line1"></span>
                <span class="line2"></span>
                <span class="line3"></span>
              </i>
            </a>
            <nav id="menu" class="menu">
                <ul class="mob_menu">
                  <li class="top"><a href="#" class="toplink">FORUM</a></li>
                  <li class="top"><a href="#" class="toplink">FAQ</a></li>
                  <li class="top has-submenu"><a href="#" class="toplink">Dropdown <img src="<%=request.getContextPath()%>/resources/images/ar_bot.png" alt=""></a>
                    <ul class="sub-menu">
                    
             	  	 <%	request.getAttribute("admin"); %>
  
					<c:if test="${currentUser.role.roleid eq 1}">
				  		<li><a href="listuser">Users</a></li>
				  	</c:if>  
                      
                      <li><a href="#">Another link</a></li>
                      <li><a href="https://www.google.co.in/" target="_blank">google.co.in</a></li>
                      <li><a href="#">Link Here</a></li>
                      <li><a href="#">Another link</a></li>
                    </ul>
                  </li>
                </ul>
            </nav>
          </div>
          
          <div class="user-area">
            <div class="dropdown pull-right">
            	<a class="rl" href="logout">Logout</a>
            </div>
          	<div class="rlink" style="margin-top:14px;color:#208fc8;font-weight:bold;">WelCome ${currentUser.username }</div>
          
        </div>
                    
        </div>
    </header>

		<header class="main-header">
		<h1>Tatvasoft</h1>
		<!-- <div class="searchbar">
			<input name="" type="text" class="form-control"
				placeholder="Search...">
		</div> -->
		</header>
		<!-- Header End -->

		<!-- Content Start -->
		<section class="page-body">
		<ol class="breadcrumb">
			<li><span class="glyphicon glyphicon-home"></span></li>
			<li><a href="dashboard">Home</a></li>
			<li>Edit Profile</li>
		</ol>
		<div class="page-content">
			<h2 class="title bord">Edit Profile</h2>


			<form:form name ="registerForm" method="post" action="updatedprofile" modelAttribute="profileUpdate" class="form-horizontal cus-form">
				<div class="form-group">
					<label class="col-sm-2 control-label">Username:</label>
					<div class="col-sm-10">
						<form:input id="username" name="username" path="username" class="form-control fc" />
						<p>Length must be between 3 characters and 20 characters.</p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">Email address:</label>
					<div class="col-sm-10">
						<form:input id="email" name="email" path="email" value="${email}" class="form-control fc"/>
						<p>You can't change your registered Email ID.</p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">Password:</label>
					<div class="col-sm-10">
						<form:input name="password" id="password" path="password" placeholder="Enter New Password" type="password"
							class="form-control fc" />
						<p>Length Must be between 6 characters and 100 characters.</p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">Confirm Password:</label>
					<div class="col-sm-10">
					<input type="password" id="repassword" name="repassword" placeholder="Enter Confirm Password" class="form-control fc" />
						<%-- <form:input id="repassword" name="repassword" path="password" type="password"
							class="form-control fc" /> --%>
					</div>
				</div>
				<br>
				<!-- <div class="category-header">Confirmation of Registration</div>
				<div class="stext">
					To prevent automated registrations the board requires you to enter
					a confirmation code. The code is displayed in the image you should
					see below. If you are visually impaired or cannot otherwise read
					this code please contact the <a href="#">Board Administrator</a>.
				</div> -->
				<%-- <div class="form-group">
					<label class="col-sm-2 control-label">Confirmation code:</label>
					<div class="col-sm-10">
						<div style="padding-bottom: 8px;">
							<img
								src="<%=request.getContextPath()%>/resources/images/test.jpg"
								class="img-responsive" alt="">
						</div>
						<p>In an effort to prevent automatic submissions, we require
							that you type the text displayed into the field underneath.</p>
					</div>
				</div> --%>
				<br>
				<div class="buttonbar">
					<div class="right">
						<input type="submit" name="Update Details" value="Update Details" onclick="return validateform();" class="btn btn-primary" /> &nbsp;
						<input name="Reset" type="reset" class="btn btn-default" value=" Reset ">
					</div>
				</div>
				
				<form:input type="hidden" path="userid" />
			</form:form>





		</div>
		</section>
		<!-- Content End -->

		<!-- Content Start -->
		<jsp:include page="footer.jsp"></jsp:include>
		<!-- Content End -->
	</div>
	<p id="back-top">
		<a href="#top"><span></span></a>
	</p>


	<script
		src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/resources/js/menu.js"></script>

	<!-- Back to Top Arrow Script -->
	<script type="text/javascript">
		jQuery(function() {
			jQuery(window).scroll(function() {
				if (jQuery(this).scrollTop() > 100) {
					jQuery('#back-top').fadeIn();
				} else {
					jQuery('#back-top').fadeOut();
				}
			});
			jQuery('#back-top a').click(function() {
				jQuery('body,html').stop(false, false).animate({
					scrollTop : 0
				}, 800);
				return false;
			});
		});		
	</script>
	
	<!-- Validation for Registration -->
	<script>  
		function validateform(){  
		var username = document.registerForm.username.value;  
		var password = document.registerForm.password.value;
		var repassword = document.getElementById("repassword").value;
		var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		
		
			if (username == null || username == "" || 3 > username.length || username.length > 20){  
			  
				alert("Username must be between 3 characters and 20 characters.");
				return false;
			  	  
			
			} else if(6 > password.length || password.length > 100 || 6 > repassword.length || repassword.length > 100 ) {  
			  
				alert("Password Must be between 6 characters and 100 characters.");  
				return false;
			}else if(password != repassword) {
					alert("Passwords must match.");
					return false;	
			} else {
				return true;
			};
		};  
</script>  
	
	<!-- end -->



</body>
</html>