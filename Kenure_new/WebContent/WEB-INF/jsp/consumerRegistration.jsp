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
<script>
	localStorage.path = "${pageContext.request.contextPath}";
</script>
<script src="app/app.js"></script>
<script src="app/controller/consumercontroller.js"></script>


</head>


<body data-ng-app="myApp">

	<div class="login-wrapper" data-ng-controller="consumercontroller" data-ng-init="initConsumerRegistration()">
		<div class="login-box">
			<div class="login">
				<div class="login-content">
					<div class="login-header">
						<!-- <h1 data-ng-bind="postStatus"></h1> -->
						<a href="#"><img src="images/logo.jpg" alt=""></a>
						<h4>Registration</h4>
					</div>
					

					<form class="login-form" name="consumerRegistrationForm">


						<div class="status error" data-ng-show="errorAlert">
							<span>No Such Consumer Found in Our Database.</span>
						</div>
						
						<div class="status error" data-ng-show="alreadyRegistered">
							<span>Consumer Already Registered.</span>
						</div>
						
						<div class="status error" data-ng-show="zipcodeError">
							<span>No Such Zipcode Found in Our Database.</span>
						</div>	
						
						<div class="form-group">
							<label class="control-label required">Customer Code : </label>
							<select name="customerCode" data-ng-model="customerCode" class="form-control" required autofocus>
								<option selected="selected" value="">-- Select Customer Code --</option>
								<option data-ng-repeat="x in customerCodeList" value="{{x.customerCode}}">{{x.customerCode}}</option>
							</select>
						</div>
						
						<div class="status error" data-ng-show="custCodeError">
							<span>Please Select Customer Code.</span>
						</div>
						
						<div class="form-group">

							<label class="control-label required">Consumer Account Number : </label> <input
								type="text" class="form-control" name="consumerAccountNumber"
								data-ng-model="consumerAccountNumber" maxlength="30"
								data-ng-trim="false" required>
						</div>
						
						<div class="form-group">

							<label class="control-label required">Consumer Zipcode : </label> <input
								type="text" class="form-control" name="zipcode"
								data-ng-model="zipcode" maxlength="15"
								data-ng-change="zipcode = zipcode.split(' ').join('')"
								data-ng-trim="false" required>
						</div>
						
						<button class="primary ic_back" data-ng-click="backButton()"
							type="button">Back</button>
						<button class="primary ic_login" data-ng-click="validateConsumer()"
							data-ng-model="submitButton" type="submit">Register
						</button>
						



					</form>
				</div>
				<p class="copyright">&copy; 2016 Blu Tower.</p>
			</div>
		</div>
	</div>

</body>
</html>

