<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>

<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
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

<script src="js/angular-animate.js"></script>
<script src="js/angular-sanitize.js"></script>
<script src="js/ui-bootstrap-tpls-2.1.3.js"></script>

<!-- <script>
	$(function() {
		/* $("#datepicker1").datepicker();
		$("#datepicker2").datepicker(); */

		$("#from").datepicker({
			defaultDate : "+1w",
			changeMonth : true,
			dateFormat : 'dd-mm-yy',
			numberOfMonths : 1,
			onClose : function(selectedDate) {
				$("#to").datepicker("option", "minDate", selectedDate);
			}
		});
		$("#to").datepicker({
			defaultDate : "+1w",
			changeMonth : true,
			dateFormat : 'dd-mm-yy',
			numberOfMonths : 1,
			onClose : function(selectedDate) {
				$("#from").datepicker("option", "maxDate", selectedDate);
			}
		});

	});
	
	
	//var resetForm = function(){ $('input,select,textarea').not('[disabled],:button').val('10'); }
	
</script> -->

<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
</script>

</head>


<body data-ng-app="myApp" id="admin">

	<div id="wrapper" data-ng-controller="logincontroller"
		data-ng-init="getEditUserDetails()">
		<!--wrapper Start-->
		<!-- <div id="header">
			<div class="logo">
				<a href=""><img src="images/logo.jpg" alt="" /></a>
			</div>
			<a class="menu-toggle" href="#menu"> <i class="menu-icon"> <span
					class="line1"></span> <span class="line2"></span> <span
					class="line3"></span>
			</i>
			</a>
			
		</div> -->
		<div id="leftNavigationbar" data-ng-include
			data-src="'adminleftNavigationbar'"></div>
			<div id="header" data-ng-include data-src="'header'"></div>
		
		<div id="content">
			<!--Content Start-->
			<h1>Customer Management
			
				<div class="rightbtns">
					<a href="adminOperation/userManagement" class="primary ic_back">Back</a>
				</div>
			
			</h1>
			<div class="middle">

				<div class="status error" data-ng-show="isError">
					<span>{{errorMessage}}</span>
				</div>

				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>

				<div class="boxpanel">
					<span class="heading">Edit Customer</span>
					<div class="box-body pad">
						<form class="login-form" name="addUserFrom">
							<div class="two_col">
								<div class="col">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label required">Customer Code :</label>
											<input type="text" class="form-control"
												data-ng-model="customerCode" data-ng-trim="false"
												data-ng-change="customerCode = customerCode.split(' ').join('')"
												data-ng-disabled="disableFields" data-ng-readonly="disableFields">
											
											<input type="hidden" data-ng-model="usersId" data-ng-readonly="disableFields"/>
											<!-- Hidden field to set userId -->
										</div>
										<div class="form-group">
											<label class="control-label required">Company Name :</label>
											<input type="text" class="form-control"
												data-ng-model="firstName" maxlength="45" required autofocus="autofocus">
										</div>
										<div class="form-group">
											<label class="control-label required">Contact Name (First and Last Name) :</label>
											<input type="text" class="form-control"
												data-ng-model="lastName" maxlength="45" required>
										</div>
										<div class="form-group">
											<label class="control-label required">Username :</label> <input
												type="text" class="form-control" data-ng-model="userName"
												data-ng-trim="false"
												data-ng-change="userName = userName.split(' ').join('')"
												data-ng-disabled="disableFields">
										</div>
										<!-- <!-- <div class="form-group">
											<label class="control-label required">Data plan mb/month :</label>
											 <input	type="text" class="form-control" data-ng-model="dataplan" required>
										</div> 
										<div>
											<label class="control-label required">Data plan mb/month :</label>
											<select data-ng-model="dataplan" class="form-control" required>
												<option value="">--- Please Select Data Plan ---</option>
												<option data-ng-repeat="x in dataplanlst">{{x.mbPerMonth}}</option>
											</select><br><br><br>
										</div> -->

										<div class="form-group">
											<label class="control-label required">Phone :</label> <input
												type="text" class="form-control" data-ng-model="phone"
												required>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Email :</label> <input
												type="email" class="form-control" data-ng-model="email"
												data-ng-trim="false" maxlength="50" name="email1"
												data-ng-pattern="emailFormat" required>
										</div>
										
										<div class="form-group" data-ng-show="addUserFrom.email1.$error.pattern">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Please Insert Valid Email Address.</label>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Address :</label> <input
												type="text" class="form-control" data-ng-model="address"
												required>
										</div>
										<div class="form-group">
											<label class="control-label required">Zipcode :</label> <input
												type="text" class="form-control" data-ng-model="zipcode"
												required>
										</div>
										<div class="form-group">
											<label class="control-label required">Data Plan (annual) :</label>
												 <select data-ng-model="currentDataPlanId"
												class="form-control" name="currentDataPlan" required 
												data-ng-change="removeError('dataPlan')">
												<option selected="selected" value="">-- Select Data Plan --</option>
												<option data-ng-repeat="x in dataPlanList"
													value="{{x.dataPlanId}}">{{x.mbPerMonth}} MB</option>
											</select>
										</div>
										<div class="form-group" data-ng-show="invalidDataPlan">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Cannot Set the selected Dataplan.</label>
										</div>							
										<div class="form-group">
										<label class="control-label required">Data Plan Active
										Date:</label> <input type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="{{editDataPlanActiveDate}}"
										data-ng-model="editDataPlanActiveDate"
										data-is-open="popup1.opened" data-datepicker-options=""
										onkeydown="return false" data-close-text="Close"
										data-ng-click="open1()"
										data-alt-input-formats="altInputFormats" >
									<!-- <span class="input-group-btn">  <button type="button" class="btn btn-default" data-ng-click="open1()">  <i class="glyphicon glyphicon-calendar"></i>  </button>  </span> -->
								</div>
								
								<div class="form-group">
									<label class="control-label required">Data Plan duration (in Years) :</label>
										<input type="number" class="form-control" data-ng-model="dataPlanDuration" placeholder="Select Yearly Duration" min="1" max="20" required>
								</div>
								
								<div class="form-group" data-ng-show="invalidDuration">
									<label class="control-label">&nbsp;</label>
									<label class="form-control status error">Invalid Data Plan duration.</label>
								</div>
								
								<div class="form-group">
									<label class="control-label required">PortalPlan Active
										Date:</label> <input type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="{{editPortalActiveDate}}"
										data-ng-model="editPortalActiveDate"
										data-is-open="popup3.opened" data-datepicker-options=""
										onkeydown="return false" data-close-text="Close"
										data-ng-click="open3()"
										data-alt-input-formats="altInputFormats" >
									<!-- <span class="input-group-btn">  <button type="button" class="btn btn-default" data-ng-click="open1()">  <i class="glyphicon glyphicon-calendar"></i>  </button>  </span> -->
								</div>
								
								<div class="form-group">
									<label class="control-label required">PortalPlan Expiry
										Date:</label> <input type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="{{editPortalExpiryDate}}"
										data-ng-model="editPortalExpiryDate"
										data-is-open="popup4.opened" data-datepicker-options=""
										onkeydown="return false" data-close-text="Close"
										data-ng-click="open4()"
										data-alt-input-formats="altInputFormats" >
									<!-- <span class="input-group-btn">  <button type="button" class="btn btn-default" data-ng-click="open1()">  <i class="glyphicon glyphicon-calendar"></i>  </button>  </span> -->
								</div>
										
										<div class="form-group">
											<label class="control-label required">Country :</label> <select
												data-ng-model="selectedCountryId" class="form-control"
												data-ng-options="y.id as y.name for y in countryList"
												required>
												<option selected="selected" value="">-- Select Country --</option>
											</select>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Currency </label>
											<select data-ng-model="selectedCurrencyId" class="form-control"
												data-ng-options="y.id as y.name for y in currencyList" required="required">
												<option selected="selected" value="">-- Select Currency --</option>
											</select>
										</div>
										
										<div class="form-group">
											<label class="control-label required">TimeZone (In Standard UTC) : </label>
											<select data-ng-model="selectedTimeZone" class="form-control"
												required="required">
												<option selected="selected" value="">-- Select TimeZone --</option>
												<option data-ng-repeat="x in timeZoneList track by $index"
												value="{{x}}" >{{x}}</option>
											</select>
										</div>

										<!-- <div class="form-group">
											<p class="control-label ">
												Selected timezone: <b>{{ timezone }}<b>
											</p>
											<timezone-selector ng-model="timezone" />
										</div> -->

										<div class="form-group">
											<label class="control-label required">Active Status :</label>
											<select data-ng-model="activeStatus"
												data-ng-options="item for item in ['ACTIVE', 'INACTIVE', 'INACTIVE-DATAPLAN', 'INACTIVE-PORTALPLAN']"
												required="required">
												<option selected="selected" value="">-- Select Status --</option>
											</select>
										</div>
										
										<input type="hidden" data-ng-model="customerId">
										
										<div class="form-group">
											<label class="control-label">&nbsp;</label>
											<button type="submit" data-ng-click="updateCustomer()"
												data-ng-model="addUserButton" class="primary ic_submit">Update
												Customer</button>
											<button type="button" data-ng-click="editUser(customerId)" class="default ic_reset">Reset</button>
										</div>
										<div class="form-group" data-ng-show="false">
											<label class="control-label">&nbsp;</label>
											<button id="trigger" type="button" onclick="test()" class="default ic_reset">Trigger</button>
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


		<div id="" data-ng-include data-src="'footer'"></div>
	</div>
	<!--wrapper end-->

</body>
</html>