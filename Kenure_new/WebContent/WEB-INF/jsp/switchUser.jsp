	<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<title>BLU Tower</title>
<link href="css/dropkick.css" rel="stylesheet" type="text/css" />
<link href="css/toastr.css" rel="stylesheet" type="text/css" />
<link
	href="http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">

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
<script src="app/app.js"></script>
<script src="app/controller/controller.js"></script>
<script src="app/controller/usercontroller.js"></script>

<script src="js/angular-animate.js"></script>
<script src="js/angular-sanitize.js"></script>
<script src="js/ui-bootstrap-tpls-2.1.3.js"></script>

<link href="css/style.css" rel="stylesheet" type="text/css" />

<style type="text/css">
	.adminborderclass{
		padding: 8px 20px;border-bottom:solid 4px #30cdd7;
	}
	.customerborderclass{
		padding: 8px 20px;border-bottom:solid 4px #206bad;
	}
</style>

<base href="/Kenure/">

</head>
<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate',
			'ui.bootstrap');
	
	window.onload = function() {
		if('$currentRoleAdmin'){
			 document.getElementById("adminDiv").focus();
			 $("#adminDiv").prop('required',true);
		}
		if('$currentRoleCustomer'){
			 document.getElementById("customerDiv").focus();
			 $("#customerDiv").prop('required',true);
		}
	   
	};
</script>

<body data-ng-app="myApp" data-ng-controller="logincontroller" >

	<div class="login-wrapper">
	<div class="login-box">
		<div class="login">
			<div class="login-content">
				<div class="login-header" data-ng-class="setBorderColor(${currentRoleAdmin})">
					<!-- <h1 data-ng-bind="postStatus"></h1> -->
					<a href="#"><img src="images/logo.jpg" alt=""></a>
					<h4></h4>
				</div>

				<form class="login-form" data-ng-show="${currentRoleAdmin}"
					name="loginForm" method="GET">
					<div class="status error" data-ng-show="error" data-ng-init="error=false">
							<span>Please select proper customer.</span>
						</div>
					<div id="admin">
							<div class="form-group" data-ng-init="getCustomerList()" >
								<label class="control-label required">Login As Customer:</label>
								<input type="text" id="adminDiv" data-ng-model="selected" uib-typeahead="c as c.customerName for c in suggestedCustomerList | filter:{customerName:$viewValue}" 
									class="form-control" typeahead-show-hint="true" typeahead-min-length="0">
							</div>
						
						<button class="primary ic_login" data-ng-click="loginAsCustomer()"
							data-ng-model="loginButton" type="submit">Login</button>
							<a href="adminOperation/adminDashboard" title="loginAsAdmin" class="primary ic_login">Login As Admin</a>
			
					</div>
				</form>
					<form class="login-form" data-ng-show="${currentRoleCustomer}"
					name="customerForm" method="GET">
					<div class="status error" data-ng-show="error" data-ng-init="error=false">
							<span>Please select proper consumer.</span>
						</div>
					<div id="customer">
							<div class="form-group" data-ng-init="getConsumerUserList()" >
								<label class="control-label required">Login As Consumer:</label>
								<input type="text" id="customerDiv" data-ng-model="selected" uib-typeahead="c as c.consumerAccNo for c in suggestedConsumerList | filter:{consumerAccNo:$viewValue}"
									 class="form-control" typeahead-show-hint="true" typeahead-min-length="0">
							</div>
						
						<button class="primary ic_login" data-ng-click="loginAsConsumer()"
							data-ng-model="loginButton" type="submit">Login</button>
							<a href="customerOperation/customerDashboard" title="loginAsAdmin" class="primary ic_login">Login As Customer</a>
			
					</div>
					
					
				<%-- 	<div  data-ng-show="${currentRoleCustomer}">
						<div class="form-group"  data-ng-init="getConsumerUserList()">
							<label>Login As Consumer:</label>
							<input type="text" data-ng-model="selected" uib-typeahead="consumer as consumer.firstName for consumer in suggestedConsumerList" class="form-control" typeahead-show-hint="true" typeahead-min-length="0">
						</div>
						
						<button class="primary ic_login" data-ng-click="loginAsConsumer()"
							data-ng-model="loginButton" type="submit">Login</button>
							<a href="adminDashboard" title="loginAsAdmin" class="primary ic_login">login As Admin</a>
					</div> --%>				
					
					
				</form>
			</div>
			<p class="copyright">&copy; 2016 Blu Tower.</p>
		</div>
	</div>
</div>

</body>
</html>
