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
<script src="js/easy-tabs.js" type="text/javascript"></script>
<script src="js/custom.js" type="text/javascript"></script>

<script src="js/angular-ui-router.js"></script>
<script src="js/html5.js"></script>
<script src="js/toastr.js"></script>
<script src="app/app.js"></script>
<script src="app/controller/controller.js"></script>
<script src="app/controller/usercontroller.js"></script>
<script src="app/controller/customercontroller.js"></script>
<base href="/Kenure/">


</head>

<body data-ng-app="myApp" id="admin">

	<div id="wrapper" data-ng-controller="logincontroller"
		data-ng-init="getEditDataPlan()">
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'adminleftNavigationbar'"></div>
		<div id="content">
			<h1>Data Plan Management
			
				<div class="rightbtns">
					<a href="dataPlanManagement" class="primary ic_back">Back</a>
				</div>
				
			</h1>
				
		<!--Content Start-->

			<div class="middle" data-ng-show="addDataPlanDiv">
			
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}}</span>
				</div>
			
				<div class="boxpanel">
					<span class="heading">Add Data Plan</span>
					<div class="status success" data-ng-show="updatedSuccessfully">
						<span>record added successfully</span>
					</div>
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post" name="addDataPlan">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label required">Data Plan (annual) </label>
											<input type="text" name="mb" class="form-control" data-ng-trim="false"
												maxlength="4" data-ng-model="mbPerMonth" data-ng-pattern="/^[0-9]{1,10}$/" required autofocus="autofocus">
										</div>
										
										<div class="form-group" data-ng-show="isDataPlanExist">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Data Plan Already Exists.</label>
										</div>
										
										<div class="form-group" data-ng-show="addDataPlan.mb.$error.pattern">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Only Integer Value allowed.</label>
										</div>
										
										<label class="control-label">&nbsp;</label>
										<button class="primary ic_submit"
											data-ng-click="insertDataPlan()"
											data-ng-model="updateDataPlan" type="submit">Add Data Plan</button>

									</div>

								</form>
							</div>
						</div>

					</div>
				</div>
			</div>
			
			<div class="middle" data-ng-show="editDataPlanDiv">
			
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}}</span>
				</div>
				
				<div class="boxpanel">
					<span class="heading">Edit Data Plan</span>
					<div class="status success" data-ng-show="updatedSuccessfully">
						<span>record updated successfully</span>
					</div>
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post" name="editPlan">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label required">Data Plan (annual)
												</label> <input type="text" class="form-control" name="mb" data-ng-trim="false"
												maxlength="4" data-ng-model="mbPerMonth" data-ng-pattern="/^[0-9]{1,10}$/" required>
										</div>
										
										<div class="form-group" data-ng-show="isDataPlanExist">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Data Plan Already Exists.</label>
										</div>
										
										<div class="form-group" data-ng-show="editPlan.mb.$error.pattern">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Only Integer Value allowed.</label>
										</div>
										
										
										<label class="control-label">&nbsp;</label>
										<button class="primary ic_submit"
											data-ng-click="updateDataPlan(dataPlanId)"
											data-ng-model="updateDataPlan" type="submit">Update</button>




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

