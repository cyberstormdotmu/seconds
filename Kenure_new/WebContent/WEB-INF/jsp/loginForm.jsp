<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/dropkick.css" rel="stylesheet" type="text/css" />
<link href="css/toastr.css" rel="stylesheet" type="text/css" />
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->

<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/angular.js"></script>
<script src="js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="js/angular-cookies.js"></script>

<script src="js/dropkick.js" type="text/javascript"></script>
<script src="js/custom.js" type="text/javascript"></script>

<script src="js/angular-ui-router.js"></script>
<script src="js/html5.js"></script>
<script src="js/toastr.js"></script>
<script>localStorage.path = "${pageContext.request.contextPath}";</script>
<script src="app/app.js"></script>
<script src="app/controller/controller.js"></script>
<script src="app/controller/usercontroller.js"></script>
<script>localStorage.activeMenu = "";</script>
<script>localStorage.activeAdminMenu = "";</script>
<script>localStorage.activeConsumerMenu = "";</script>


</head>


<body data-ng-app="myApp">

	<div class="login-wrapper">
		<div class="login-box">
			<div class="login">
				<div class="login-content">
					<div class="login-header">
						<!-- <h1 data-ng-bind="postStatus"></h1> -->
						<a href="#"><img src="images/logo.jpg" alt=""></a>
						<h4>Please Login</h4>
					</div>
					<!-- 	<div data-ui-view></div> -->

					<form class="login-form" data-ng-controller="logincontroller" name="loginForm">

						<!-- <div class="status error" data-ng-if="(loginForm.userName.$dirty && loginForm.userName.$invalid) || (loginForm.password.$dirty && loginForm.password.$invalid)">
							<span data-ng-message="required">Username or Password Can't be Empty.</span>
						</div> -->
						<div class="status success" data-ng-show="isFirstTImeLoginTrue">
							<span>Firsttime login user</span>
						</div>
						<div class="status error" data-ng-show="error">
							<span>Username or Password Can't be Empty.</span>
						</div>
						<div class="status error" data-ng-show="authenticationError">
							<span>Invalid Username or Password</span>
						</div>
						<div class="status error" data-ng-show="notAuthorizedError">
							<span>You are not Authorized to Login.</span>
						</div>
						<div class="status success" data-ng-show="${status}">
							<span>Your New Password sent successfully. Please login with New Password.</span>
						</div>
						
						<div class="status success" data-ng-show="${registercompleted}">
							<span>Your details registered successfully. Please login with provided Username and Password.</span>
						</div>
						
						<div class="status success" data-ng-show="success">
							<span>You have successfully Logged in.</span>
						</div>

						
						<div class="status error" data-ng-show="inactiveDataPlan">
							<span>Your Data Plan is inactive. Please contact admin.</span>
						</div>
						<div class="status error" data-ng-show="inactivePortalPlan">
							<span>Your Portal Plan is inactive. Please contact admin.</span>
						</div>
						<div class="status error" data-ng-show="inactiveUser">
							<span>Your status is inactive. Please contact admin.</span>
						</div>

						<div class="form-group">

							<!-- ********** -->
							<!-- <div class="status error" data-ng-messages="loginForm.userName.$error" data-ng-if="loginForm.userName.$dirty && loginForm.userName.$invalid">
							
								<span data-ng-message="required">Username Can't be Empty.</span>
							
							</div>
							 -->
							<!-- ************************ -->

						<label class="control-label required">Username : </label> <input type="text" class="form-control "
							name="userName" data-ng-model="userName" data-ng-trim="false"
							data-ng-change="userName = userName.split(' ').join('')" required autofocus>
					</div>

						<div class="form-group">

							<label class="control-label required">Password : </label> <input
								type="password" class="form-control" name="password"
								data-ng-model="password"
								data-ng-change="password = password.split(' ').join('')"
								data-ng-trim="false" required>
						</div>
						<div class="forgot-psw">
							<label class="checkbox"><input type="checkbox" data-ng-model="remember" data-ng-click="rememberMe()"><span></span>Remember
								me</label> <a href="forgetPasswordForm" title="Forgot Password?">Forgot
								Password?</a>
						</div>
						
						<button class="primary ic_login" data-ng-click="loginUser()"
							data-ng-model="loginButton" type="submit">Login</button>
						
						<a href="consumerRegistration" class="primary ic_add pull-right">New Registration</a>
						
						<!-- <a href="consumerRegistration">
							<button class="primary ic_add pull-right">New Registration</button>	
						</a> -->
						
						
						
					</form>
				</div>
				<p class="copyright">&copy; 2016 Blu Tower.</p>
			</div>
		</div>
	</div>

</body>
</html>

