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
<base href="/Kenure/">


</head>

<body data-ng-app="myApp" id="admin">

	<div id="wrapper" data-ng-controller="logincontroller"
		data-ng-init="getEditdatacollector()">
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'header'"></div>

		<div id="leftNavigationbar" data-ng-include
			data-src="'adminleftNavigationbar'"></div>
		<div id="content">

			<h1>
				Allocate DataCollector Management

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
					<span class="heading">Edit DataCollector</span>
					
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post" name="editDataCollector">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label required">DataCollector
												ID:</label> <input type="text" class="form-control"
												data-ng-model="datacollectorId" disabled>
										</div>
										<div class="form-group">
											<label class="control-label required">DC Serial
												Number:</label> <input type="text" class="form-control"
												maxlength="45" data-ng-model="dcSerialNumber">
										</div>
	
										<div class="form-group" data-ng-show="isDcSerialNumExists">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">DC serial number is already exists.</label>
										</div>

										<div class="form-group">
											<label class="control-label required">IP Address:</label> <input
												maxlength="15"
												type="text" class="form-control" data-ng-model="dcIp">
										</div>


										<div class="form-group">
											<label class="control-label required"> Simcard
												Number:</label> <input type="text" class="form-control"
												data-ng-model="dcSimcardNo" maxlength="40">
										</div>

										<div class="form-group" data-ng-show="isSimCardNumExists">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Simcard number is already exists.</label>
										</div>

										<div class="form-group">
											<label class="control-label required">User Id:</label> <input
												maxlength="45"
												type="text" class="form-control " data-ng-model="dcUserId">
										</div>
									 	<div class="form-group">
											<label class="control-label required">Password:</label> <input
												type="password" class="form-control" maxlength="45"
												data-ng-model="dcUserPassword">
										</div> 
										<div class="form-group">
											<label class="control-label required">Total No. End
												Points:</label> <input type="text" class="form-control"
												data-ng-model="totalEndpoints" disabled>
										</div>

										<div class="form-group">
											<label class="control-label">Latitude:</label> <input
												type="text" class="form-control" data-ng-trim="false"
												data-ng-pattern="/^[0-9]{1,2}.[0-9]{1,5}$/" 
												data-ng-model="latitude" name="latitude">
										</div>
										<div class="form-group"
											data-ng-show="editDataCollector.latitude.$error.pattern">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Only Decimal Value
												Allowed. Decimal Point is After First 2 Digits.
												e.g(12.36)</label>
										</div>
										<div class="form-group">
											<label class="control-label">Longitude:</label> <input
												type="text" class="form-control" data-ng-trim="false"
												data-ng-pattern="/^[0-9]{1,2}.[0-9]{1,5}$/" 
												data-ng-model="longitude" name="longitude">
										</div>
										<div class="form-group"
											data-ng-show="editDataCollector.longitude.$error.pattern">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Only Decimal Value
												Allowed. Decimal Point is After First 2 Digits.
												e.g(12.36)</label>
										</div>
										<div class="form-group" data-ng-show="customerDivListdisabled">
											<label class="control-label">Select
												Customer:</label> <select class="form-control" name="customerName"
												disabled style="background-color: #F0F0F0; ">
												<option value="{{customerName}}">{{customerName}}</option>

											</select>
										</div>

										<div class="form-group" data-ng-show="customerDivList">
											<label class="control-label">Select
												Customer:</label> <select class="form-control" name="customerName"
												data-ng-model="customerName">
												<option value="">---Please select---</option>
												<option data-ng-repeat="c in customerList"
													data-ng-if="$index > 0">{{c.customerName}}</option>

											</select>
										</div>

										<label class="control-label">&nbsp;</label>
										<button class="primary ic_submit"
											data-ng-click="updateDataCollector(datacollectorId)"
											data-ng-model="updateProfile" type="submit">Update</button>





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

