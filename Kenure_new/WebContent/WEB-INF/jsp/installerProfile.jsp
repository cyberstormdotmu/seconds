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

<script src="app/controller/installercontroller.js"></script>

<base href="/Kenure/">

</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="installercontroller" data-ng-init="installerProfileInit()">
		
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'header'"></div>
		<div id="leftNavigationbar" data-ng-include data-src="'installerLeftNavigationbar'"></div>
		
		<div id="content">
			<!--Content Start-->
			<h1>Profile</h1>
			<div class="middle">
				<div class="boxpanel">
					<span class="heading">Update Profile</span>
					<div class="box-body pad">
						<form class="login-form" name="updateInstallerForm">
							<div class="two_col">
								<div class="col">
									<div class="form-horizontal">
										
										<div class="form-group">
											<label class="control-label required">Installer First
												Name :</label> <input type="text" class="form-control"
												data-ng-model="firstName" maxlength="45" required autofocus="autofocus">
										</div>
										
										<div class="form-group">
											<label class="control-label required">Installer Last
												Name :</label> <input type="text" class="form-control"
												data-ng-model="lastName" maxlength="45" required>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Username :</label> <input
												type="text" class="form-control" data-ng-model="userName" disabled>
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
										
										<div class="form-group" data-ng-show="updateInstallerForm.email1.$error.pattern">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Please Insert Valid Email Address.</label>
										</div>
										
										<div class="form-group">
											<label class="control-label">Email2:</label> <input
												type="email" class="form-control" data-ng-model="email2"
												data-ng-trim="false" maxlength="50"
												data-ng-change="email2 = email2.split(' ').join('')">
										</div>
										
										<div class="form-group" data-ng-show="invalidemail2">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Please Insert Valid Email Address.</label>
										</div>
										
										<div class="form-group">
											<label class="control-label">Email3:</label> <input
												type="email" class="form-control" data-ng-model="email3"
												data-ng-trim="false" maxlength="50"
												data-ng-change="email3 = email3.split(' ').join('')">
										</div>
										
										<div class="form-group" data-ng-show="invalidemail3">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Please Insert Valid Email Address.</label>
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
										
										<!-- <div class="form-group">
											<label class="control-label required">Active Status :</label>
											<select data-ng-model="activeStatus" 
												data-ng-options=" x for x in ['ACTIVE','INACTIVE'] " required>
												<option value="true" selected="selected">Active</option>
												<option value="false">Inactive</option>
											</select>
										</div> -->
										
										<div hidden="true">
											<input type="text" data-ng-model="installerId" onkeydown="return false" required>
										</div>
										
										<div class="form-group">
											<label class="control-label">&nbsp;</label>
											<button type="submit" data-ng-click="updateInstallerProfile()" data-ng-model="updateInstallerButton" class="primary ic_submit">Update Installer</button>
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


		<div id="footer" data-ng-include data-src="'footer'"></div>
	</div>
	<!--wrapper end-->


</body>
</html>