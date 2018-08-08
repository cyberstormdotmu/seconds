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
<script src="app/controller/controller.js"></script>
<script src="app/controller/usercontroller.js"></script>
<script src="app/controller/customercontroller.js"></script>
<base href="/Kenure/">


<!-- <script type="text/javascript">

	window.onload = function() {
		if('$status1'){
			 document.getElementById("country1").focus();
		}
		if('$status2'){
			 document.getElementById("country2").focus();
		}
	   
	};
</script>
 -->


</head>

<body data-ng-app="myApp" id="admin">

	<div id="wrapper" data-ng-controller="logincontroller"
		data-ng-init="getEditCountry()">
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'adminleftNavigationbar'"></div>
		<div id="content">
			<h1>Country Management
			
				<div class="rightbtns">
					<a href="countryManagement" class="primary ic_back">Back</a>
				</div>
				
			</h1>
			
			<!--Content Start-->

			<div class="middle" data-ng-show="addCountryDiv">
				
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>
				
				<div class="boxpanel">
					<span class="heading">Add Country</span>
					<div class="status success" data-ng-show="updatedSuccessfully">
						<span>record added successfully</span>
					</div>
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post" name="addCountry">
									<div class="form-horizontal">
										
										<div class="form-group">
											<label class="control-label required">Country Name</label>
												<input type="text" class="form-control" data-ng-trim="false"
													maxlength="50" data-ng-model="countryName" id="country1" required autofocus="autofocus"> 
										</div>
								
										<div class="form-group" data-ng-show="isCountryNameExist">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Country Name Already Exists.</label>
										</div>
										
										<div class="form-group" data-ng-show="enterCountryName">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Country Name Can't be Empty.</label>
										</div>
								
										<div class="form-group">
											<label class="control-label required">Country Code</label>
												<input type="text" class="form-control" data-ng-trim="false"
													maxlength="3" data-ng-model="countryCode"
													data-ng-change="countryCode = countryCode.split(' ').join('')"  required>
										</div>
										
										<div class="form-group" data-ng-show="enterCountryCode">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Country Code Can't be Empty.</label>
										</div>
										
										<div class="form-group" data-ng-show="isCountryCodeExist">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Country Code Already Exists.</label>
										</div>
										
										<div class="form-group">
											<label class="control-label">&nbsp;</label>
											<button class="primary ic_submit"
												data-ng-click="insertCountry()"
												data-ng-model="updateCountry" type="submit">Add
												Country</button>
										</div>
									</div>

								</form>
							</div>
						</div>

					</div>
				</div>
			</div>
			
			<div class="middle" data-ng-show="editCountryDiv">
			
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>
			
				<div class="boxpanel">
					<span class="heading">Edit Country</span>
					<div class="status success" data-ng-show="updatedSuccessfully">
						<span>record updated successfully</span>
					</div>
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post" name="editCountry">
									<div class="form-horizontal">
										
										<div class="form-group">
											<label class="control-label required">Country Name</label>
												<input type="text" class="form-control" data-ng-trim="false"
													maxlength="50" data-ng-model="countryName" id="country2" required autofocus="autofocus"> 
										</div>
										
										<div class="form-group" data-ng-show="isCountryNameExist">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Country Name Already Exists.</label>
										</div>
								
										<div class="form-group" data-ng-show="enterCountryName">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Country Name Can't be Empty.</label>
										</div>
								
										<div class="form-group">
											<label class="control-label required">Country Code</label>
												<input type="text" class="form-control" data-ng-trim="false"
													maxlength="3" data-ng-model="countryCode"
													data-ng-change="countryCode = countryCode.split(' ').join('')"  required>
										</div>
										
										<div class="form-group" data-ng-show="enterCountryCode">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Country Code Can't be Empty.</label>
										</div>
										
										<div class="form-group" data-ng-show="isCountryCodeExist">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Country Code Already Exists.</label>
										</div>
										
										<div class="form-group">
											<label class="control-label">&nbsp;</label>
											<button class="primary ic_submit" data-ng-click="updateCountry(countryId)"
												data-ng-model="updateCountryButton" type="submit">Update</button>
										</div>



									</div>

								</form>



							</div>
						</div>

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

