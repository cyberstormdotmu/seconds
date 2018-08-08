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
<title>Update User</title>
<link
	href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css"
	rel="stylesheet" />
<link href="<%=request.getContextPath()%>/resources/css/style.css"
	rel="stylesheet" />
<link href="<%=request.getContextPath()%>/resources/css/menu.css"
	rel="stylesheet" />

<!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->

<script src="<%=request.getContextPath()%>/resources/js/jquery.min.js"></script>
</head>

<body>
	<div id="wrap">


		<header class="main-header">
		<h1>Tatvasoft</h1>
		<div class="searchbar">
			<input name="" type="text" class="form-control"
				placeholder="Search...">
		</div>
		</header>
		<!-- Header End -->

		<!-- Content Start -->
		<section class="page-body">
		<ol class="breadcrumb">
			<li><a href="dashboard"><span
					class="glyphicon glyphicon-home"></span></a></li>
			<li><a href="listuser">User List</a></li>
		</ol>
		<div class="page-content">
			<h2 class="title bord">Registration</h2>

			<form:form name="userUpdateForm" method="post" action="updateuser"
				modelAttribute="userUpdate" class="form-horizontal cus-form">
				<div class="form-group">
					<label class="col-sm-2 control-label">Username:</label>
					<div class="col-sm-10">
						<form:input id="username" name="username" path="username"
							class="form-control fc" />
						<p>Length must be between 3 characters and 20 characters.</p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">Email address:</label>
					<div class="col-sm-10">
						<form:input id="email" name="email" path="email"
							class="form-control fc" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">Password:</label>
					<div class="col-sm-10">
						<form:input name="password" id="password" path="password"
							type="password" class="form-control fc" />
						<p>Length Must be between 6 characters and 100 characters.</p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">Confirm Password:</label>
					<div class="col-sm-10">
						<input type="password" id="repassword" name="repassword"
							class="form-control fc" />

					</div>
				</div>
				<br><br>
				<div class="buttonbar">
					<div class="right">
						<input type="submit" name="Update" value="Update"
							onclick="return validateform();" class="btn btn-primary" />
						&nbsp; <input name="Reset" type="reset" class="btn btn-default"
							value=" Reset ">
					</div>
				</div>

				<form:input type="hidden" path="userid" /></form:form>
		</div>
		</section>
		<!-- Content End -->

		<!-- Footer Start -->
			<jsp:include page="footer.jsp"></jsp:include>
		<!-- Footer End -->
	</div>
	<p id="back-top">
		<a href="#top"><span></span></a>
	</p>

	<script	src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
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
		function validateform() {
			var username = document.userUpdateForm.username.value;
			var email = document.userUpdateForm.email.value;
			var password = document.userUpdateForm.password.value;
			var repassword = document.getElementById("repassword").value;
			var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

			if (username == null || username == "" || 3 > username.length
					|| username.length > 20) {

				alert("Username must be between 3 characters and 20 characters.");
				return false;

			} else if (!filter.test(email) || email == "") {

				alert("Please enter a valid e-mail address.");
				return false;

			} else if (6 > password.length || password.length > 100
					|| 6 > repassword.length || repassword.length > 100) {

				alert("Password Must be between 6 characters and 100 characters.");
				return false;
			} else if (password != repassword) {
				alert("Passwords must match.");
				return false;
			} else {
				return true;
			}
			;
		};
	</script>

	<!-- end -->

</body>
</html>