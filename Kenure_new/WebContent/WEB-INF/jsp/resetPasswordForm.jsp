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

				<form class="login-form" data-ng-controller="logincontroller"
					method="post">

					<div class="status error" data-ng-show="passwordIsIncorrect">
						<span>You have entered Incorrect Old Password.</span>
					</div>
					<div class="status error" data-ng-show="changedSuccessfully">
						<span>No Such Email Found According to Your Username.</span>
					</div>
					
					<div class="status error" data-ng-show="mismatchpassword">
						<span>New Password and Confirm Password Must be Same.</span>
					</div>	
					
					<div class="form-group">
						<label class="control-label required">Old Password:</label> <input type="password"
							class="form-control" name="oldpwd" data-ng-model="oldpwd" required autofocus="autofocus"><br>
						<br> <label class="control-label required">New Password :</label> <input type="password"
							class="form-control" name="newpwd" data-ng-model="newpwd"
							required /><br>
						<br> <label class="control-label required">Confirm Password :</label> <input type="password"
							class="form-control" name="confirmnewpwd"
							data-ng-model="confirmnewpwd" required />

					</div>

					<button class="primary ic_login" data-ng-click="resetpassword()"
						type="submit">Change Password</button>

				</form>
			</div>
			<p class="copyright">&copy; 2016 Blu Tower.</p>
		</div>
	</div>
</div>


</body>
</html>
