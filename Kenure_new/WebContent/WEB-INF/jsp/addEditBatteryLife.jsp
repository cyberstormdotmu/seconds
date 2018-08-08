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
<base href="/Kenure/">


</head>

<body data-ng-app="myApp" id="admin">

	<div id="wrapper" data-ng-controller="logincontroller"
		data-ng-init="getEditBattery()">
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'adminleftNavigationbar'"></div>
		<div id="content">
			<h1>
				Battery Management

				<div class="rightbtns">
					<a href="batteryManagement" class="primary ic_back">Back</a>
				</div>

			</h1>

			<!--Content Start-->

			<div class="middle" data-ng-show="addBatteryDiv">
				
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>
				
				<div class="boxpanel">
					<span class="heading">Add Battery</span>
					<div class="status success" data-ng-show="updatedSuccessfully">
						<span>Record added successfully</span>
					</div>
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post" name="addBatteryLife">
									<div class="form-horizontal">

										<div class="form-group">
											<label class="control-label required">Number of Child
												Nodes</label> <input type="number" class="form-control" min="1" max="17" data-ng-model="numberOfChildNodes" required>
										</div>
										
										<div class="form-group" data-ng-show="isnumOfChildNodeExist">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Value of number of child nodes is already exists.</label>
										</div>
							
										<div class="form-group" data-ng-show="invalidvalue">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Value of number of child nodes must in between 1 to 17.</label>
										</div>
							
										<div class="form-group">
											<label class="control-label required">Estimated
												Battery Life (in years)</label> <input type="text"
												class="form-control" data-ng-trim="false" maxlength="10"
												data-ng-model="estimatedBatteryLifeInYears"
												data-ng-change="estimatedBatteryLifeInYears = estimatedBatteryLifeInYears.split(' ').join('')"
												required>
										</div>

										<div class="form-group">
											<label class="control-label">&nbsp;</label>
											<button class="primary ic_submit"
												data-ng-click="insertBatteryLife()"
												data-ng-model="updateBatteryLife" type="submit">Add
												Battery Life</button>
										</div>
									</div>

								</form>
							</div>
						</div>

					</div>
				</div>
			</div>

			<div class="middle" data-ng-show="editBatteryDiv">
			
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>
			
				<div class="boxpanel">
					<span class="heading">Edit Battery</span>
					<div class="status success" data-ng-show="updatedSuccessfully">
						<span>Record updated successfully</span>
					</div>
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post" name="editBattery">
									<div class="form-horizontal">

										<div class="form-group">
											<label class="control-label required">Number of Child
												Nodes</label> <input type="number" class="form-control" min="1" max="17" data-ng-model="numberOfChildNodes" required> 
										</div>

										<div class="form-group" data-ng-show="inValidChildNodes">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Number of child nodes can't be null.</label>
										</div>
		
										<div class="form-group" data-ng-show="isnumOfChildNodeExist">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Value of number of child nodes is already exists.</label>
										</div>
		
										<div class="form-group" data-ng-show="invalidvalue">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Value of number of child nodes must in between 1 to 17.</label>
										</div>
		
										<div class="form-group">
											<label class="control-label required">Estimated
												Battery Life (in years)</label> <input type="text"
												class="form-control" data-ng-trim="false" maxlength="10"
												data-ng-model="estimatedBatteryLifeInYears"
												data-ng-change="estimatedBatteryLifeInYears = estimatedBatteryLifeInYears.split(' ').join('')"
												required>
										</div>

										<div class="form-group" data-ng-show="inValidBatteryLife">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Estimated battery life can't be null.</label>
										</div>
	
										<div class="form-group">
											<label class="control-label">&nbsp;</label>
											<button class="primary ic_submit"
												data-ng-click="updateBattery(batteryLifeId)"
												data-ng-model="updateBatteryButton" type="submit">Update</button>
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

