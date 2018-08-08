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

<body data-ng-app="myApp" id="admin">
	
	<div id="wrapper" data-ng-controller="logincontroller" data-ng-init="getCustomerList()">
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'header'"></div>
		
		<div id="leftNavigationbar" data-ng-include data-src="'adminleftNavigationbar'"></div>
		
		<div id="content">
			<h1>Allocate DataCollector Management
			
				<div class="rightbtns">
					<a href="spareDataCollectorPanel" class="primary ic_back">Back</a>
				</div>
				
			</h1>
			<!--Content Start-->
			<div class="middle">
			
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>
			
				<div class="boxpanel">
					<span class="heading">Add DataCollector</span>
					
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post" name="addDataCollector">
								<div class="form-horizontal">
									
									<div class="form-group">
										<label class="control-label required">DC Serial Number:</label> 
											<input type="text" class="form-control" data-ng-model="dcSerialNumber" 
											 	maxlength="45" required autofocus="autofocus">
									</div>
									
									<div class="form-group" data-ng-show="isDcSerialNumExists">
										<label class="control-label">&nbsp;</label>
										<label class="form-control status error">DC serial number is already exists.</label>
									</div>
									
									
									<div class="form-group">
										<label class="control-label required">IP Address:</label> <input
											type="text" class="form-control" maxlength="15"
											 data-ng-model="dcIp" required>
									</div>
									
									
									<div class="form-group">
										<label class="control-label"> Simcard Number:</label> <input
											type="text" class="form-control" maxlength="40"
											 data-ng-model="dcSimcardNo">
									</div>
								
									<div class="form-group" data-ng-show="isSimCardNumExists">
										<label class="control-label">&nbsp;</label>
										<label class="form-control status error">Simcard number is already exists.</label>
									</div>
								
									<div class="form-group">
										<label class="control-label required">User Id:</label> <input
											type="text" class="form-control" maxlength="45"
											 data-ng-model="dcUserId" required>
									</div>
									<div class="form-group">
										<label class="control-label required">Password:</label> <input
											type="password" class="form-control" maxlength="45"
											 data-ng-model="dcUserPassword" required>
									</div>
									<!-- <div class="form-group">
										<label class="control-label">Latitude:</label> <input
											type="text" class="form-control" data-ng-model="latitude" data-ng-pattern="/^[0-9]{1,2}.[0-9]{1,5}$/" name="latitude"  maxlength="8" >
									</div>
									<div class="form-group" data-ng-show="addDataCollector.latitude.$error.pattern">
									<label class="control-label">&nbsp;</label>
									<label class="form-control status error">Only Decimal Value allowed. and Decimal point is after first 2 digits. e.g(12.36)</label>
								</div>
								 	<div class="form-group">
										<label class="control-label">Longitude:</label> <input
											type="text" class="form-control" data-ng-model="longitude" data-ng-pattern="/^[0-9]{1,2}.[0-9]{1,5}$/" name="longitude"  maxlength="8" >
									</div> 
									<div class="form-group" data-ng-show="addDataCollector.longitude.$error.pattern">
									<label class="control-label">&nbsp;</label>
									<label class="form-control status error">Only Decimal Value allowed. and Decimal point is after first 2 digits. e.g(12.36)</label>
								</div> -->
									<div class="form-group">
									<label class="control-label">Select Customer:</label>
									<select class="form-control" name="customerName" data-ng-model="cust_id">
										<option value="">---Please select---</option>
  										<option data-ng-repeat="customerList in suggestedCustomerList" value="{{customerList.customerId}}">{{customerList.customerName}}</option>
  						
									</select>
									</div>
										<label class="control-label">&nbsp;</label>
										<button class="primary ic_submit" data-ng-click="addDataCollectorByAdmin()" type="submit">Add DataCollector</button>
									
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

