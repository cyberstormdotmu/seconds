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
<script src="app/app.js"></script>
<script src="app/controller/consumercontroller.js"></script>

<script>
	localStorage.path = "${pageContext.request.contextPath}";
</script>

<style type="text/css">
.register{width:580px; margin: 0 auto;}
</style>
</head>

<body data-ng-app="myApp">


<div class="login-wrapper">
		<div class="login-box">
			<div class=register>
				<div class="login-content">
					<div class="login-header">
						<!-- <h1 data-ng-bind="postStatus"></h1> -->
						<a href="#"><img src="images/logo.jpg" alt=""></a>
						<h4>Please Register</h4>
					</div>
					<!-- 	<div data-ui-view></div> -->

					<form class="login-form"  data-ng-controller="consumercontroller" data-ng-init="initConsumerRegiPage()" name="registerConsumerForm">


										<div class="form-group">
											<label class="control-label required">Customer Code :</label>
											<input type="text" class="form-control" value="${customerCode}" placeholder="${customerCode}"
												data-ng-model="customerCode" maxlength="45" required disabled="disabled">
										</div>
										
										<div class="form-group">
											<label class="control-label required">Consumer Account Number :</label>
											<input type="text" class="form-control" maxlength="30" disabled="disabled"
												data-ng-model="consumerAccountNumber" value="${consumerAccountNumber}"
												placeholder="${consumerAccountNumber}" required>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Consumer First
												Name :</label> <input type="text" class="form-control"
												data-ng-model="firstName" maxlength="45" required  autofocus="autofocus">
										</div>
										
										<div class="form-group">
											<label class="control-label required">Consumer Last
												Name :</label> <input type="text" class="form-control"
												data-ng-model="lastName" maxlength="45" required>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Username :</label> <input
												type="text" class="form-control" data-ng-model="userName"
												data-ng-trim="false"
												data-ng-change="userName = userName.split(' ').join('')"
												maxlength="50" required>
										</div>
										<div class="status error" data-ng-show="isUserNameExist">
											<span>UserName already exists.</span>
										</div>
										<div class="status error" data-ng-show="isUserNameEmpty">
											<span>UserName can't be empty.</span>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Phone1:</label> <input
												type="text" class="form-control"
												data-ng-model="cell_number1" maxlength="15" required>
										</div>
										
										<div class="form-group">
											<label class="control-label">Phone2:</label> <input
												type="text" class="form-control"
												data-ng-model="cell_number2" maxlength="15">
										</div>
										
										<div class="form-group">
											<label class="control-label">Phone3:</label> <input
												type="text" class="form-control"
												data-ng-model="cell_number3" maxlength="15">
										</div>

										<div class="form-group">
											<label class="control-label required">Email1:</label> <input
												type="email" class="form-control" data-ng-model="email1"
												data-ng-trim="false" maxlength="50" name="email1"
												data-ng-pattern="emailFormat" required>
										</div>
										
										<div class="status error" data-ng-show="registerConsumerForm.email1.$error.pattern">
											<span>Please Insert Valid Email Address.</span>
										</div>
										
										<div class="form-group">
											<label class="control-label">Email2:</label> <input
												type="email" class="form-control" data-ng-model="email2"
												data-ng-trim="false" maxlength="50"
												data-ng-change="email2 = email2.split(' ').join('')">
										</div>
										
										<div class="status error" data-ng-show="invalidemail2">
											<span>Please Insert Valid Email Address.</span>
										</div>
										
										<div class="form-group">
											<label class="control-label">Email3:</label> <input
												type="email" class="form-control" data-ng-model="email3"
												data-ng-trim="false" maxlength="50"
												data-ng-change="email3 = email3.split(' ').join('')">
										</div>

										<div class="status error" data-ng-show="invalidemail3">
											<span>Please Insert Valid Email Address.</span>
										</div>

										<div class="form-group">
											<label class="control-label required">Address1:</label> <input
												type="text" class="form-control " maxlength="255"
												data-ng-model="address1" required>
										</div>

										<div class="form-group">
											<label class="control-label">Address2:</label> <input
												type="text" class="form-control" maxlength="255"
												data-ng-model="address2">
										</div>

										<div class="form-group">
											<label class="control-label">Address3:</label> <input
												type="text" class="form-control" maxlength="255"
												data-ng-model="address3">
										</div>

										<div class="form-group">
											<label class="control-label required">Street Name:</label> <input
												type="text" class="form-control" maxlength="255"
												data-ng-model="streetName" required>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Zipcode :</label> <input
												type="text" class="form-control" maxlength="10"
													data-ng-model="zipcode" required>
										</div>
										
										<div class="form-group">
											<label class="control-label">&nbsp;</label>
											<button type="submit" data-ng-click="registerConsumerUser()" class="primary ic_submit">Register</button>
											<button type="reset" class="default ic_reset">Reset</button>
										</div>
				

					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>