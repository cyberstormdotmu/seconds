<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/toastr.css" rel="stylesheet" type="text/css" />
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->

<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>

<script src="${pageContext.request.contextPath}/app/controller/customercontroller.js"></script>


<!-- <script src="js/activeClass.js"></script> -->

<!-- <script>var url = "${pageContext.request.contextPath}";</script> -->

<script>
	
</script>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="customercontroller">
		<!--wrapper Start-->
		
		<div id="header" data-ng-include data-src="'../header'"></div>
	   	<div id="customerleftNavigationbar" data-ng-include data-src="'customerleftNavigationbar'"></div>
		<div id="content">
			<!--Content Start-->
			<h1>
				Control&nbsp;&nbsp;>>&nbsp;&nbsp;Installer Management
				<div class="rightbtns">
					<a href="installerManagement" class="primary ic_back">Back</a>
				</div>
			</h1>
			<div class="middle">
				<div class="boxpanel">
					<span class="heading">Add New Installer</span>
					<div class="box-body pad">
						<form class="login-form" name="addInstallerForm">
							<div class="two_col">
								<div class="col">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label required">Installer First Name</label>
											<input type="text" class="form-control" 
												data-ng-model="installer.firstname" data-ng-trim="false" maxlength="45" 
												required autofocus="autofocus">
										</div>
										
										<div class="form-group">
											<label class="control-label required">Installer Last Name</label>
											<input type="text" class="form-control" 
												data-ng-model="installer.lastname" data-ng-trim="false" maxlength="45" 
												required>
										</div>
									
										<div class="form-group">
											<label class="control-label required">Username :</label> <input
												type="text" class="form-control" data-ng-model="installer.username"
												data-ng-trim="false"
												data-ng-change="checkUserName()"
												maxlength="50" required>
										</div>
										<div class="form-group" data-ng-show="userNameExist" ng-init="userNameExist = false">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error"><span>{{userNameMessage}}</span></label>
										</div>
									

										<div class="form-group">
											<label class="control-label required">Phone :</label> <input
												type="text" name="phone_field" class="form-control" data-ng-model="installer.phone" maxlength="15" 
												required >
										</div>
										
										<div class="form-group">
											<label class="control-label required">Email 1:</label> <input
												type="email" class="form-control" data-ng-model="installer.email1"
												data-ng-trim="false" name="email1" data-ng-pattern="/^[_a-zA-Z0-9]+(\.[_a-zA-Z0-9]+)*@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*(\.[a-zA-Z]{2,4})$/" 
												data-ng-change="email1 = email1.split(' ').join('')" required>
										</div>
										<div class="form-group" data-ng-show="addInstallerForm.email1.$error.pattern">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Please Insert Valid Email.</label>
										</div>
										<div class="form-group">
											<label class="control-label required">Address 1 :</label> <input
												type="text" class="form-control" data-ng-model="installer.address1"
												maxlength="100" required>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Street Name :</label> <input
												type="text" class="form-control" data-ng-model="installer.streetname"
												maxlength="100" required>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Zipcode :</label> <input
												type="text" class="form-control" data-ng-model="installer.zipcode" maxlength="10" 
												required>
										</div>
										
										<div class="form-group">
											<label class="control-label">&nbsp;</label>
											<button type="submit" data-ng-click="addInstaller(${customerId})"
												class="primary ic_submit">Add Installer</button>
											<!-- <button type="reset" class="default ic_reset">Reset</button> -->
										</div>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>

			</div>
			<!--Content end-->
		</div>


		<div id="footer">
			<!--footer start-->
			<p class="copyright">
				&copy; 2016 Blu Tower.&nbsp;&nbsp;<a href="#">Privacy</a><span>|</span><a
					href="#">Terms</a>
			</p>
			<!--footer end-->
		</div>
	</div>
	<!--wrapper end-->


</body>
</html>



