<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
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
<script src="app/app.js"></script>
<script src="app/controller/controller.js"></script>
<script src="app/controller/usercontroller.js"></script>
<base href="/Kenure/">


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

				<form class="login-form" data-ng-controller="logincontroller" method="post" name="forgotPasswordForm">

					<div class="status error" data-ng-show="error">
						<span>Username and Email Can't be Empty.</span>
					</div>
					<div class="status error" data-ng-show="userNotAvailable">
						<span>No such Register Email Id found.</span>
					</div>
					<div class="status error" data-ng-show="sendmailEroor">
						<span>Something was wrong, please try again</span>
					</div>
					<div class="status success" data-ng-show="sendEmailSuccess">
						<span>New Password successfully sent to your email</span>
					</div>
					<div class="status error" data-ng-show="emailNotAvailable">
						<span>Email does not match.</span>
					</div>
					<div class="status error" data-ng-show="validationfailed">
						<span>Username or Email Can't be Empty</span>
					</div>

					<div class="status error" data-ng-show="forgotPasswordForm.email.$error.pattern">
						<span>Please Insert Valid Email Address.</span>
					</div>	


					<div class="form-group">
						<label class="control-label required">Username :</label> <input
							type="text" class="form-control" data-ng-model="userName"
							data-ng-trim="false"
							data-ng-change="userName = userName.split(' ').join('')" required autofocus="autofocus">
					</div>
					<div class="form-group">
						<label class="control-label required">Email :</label> <input
							type="email" class="form-control" data-ng-model="email"
							data-ng-trim="false" maxlength="50" name="email"
							data-ng-pattern="emailFormat" required>
					</div>

					<button class="primary ic_back" data-ng-click="backButton()"
						type="button">Back</button>
					<button class="primary ic_login" data-ng-click="forgetpwd()"
						type="submit">Get New Password</button>

				</form>
			</div>
			<p class="copyright">&copy; 2016 Blu Tower.</p>
		</div>
	</div>
</div>



</body>
</html>
