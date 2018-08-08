<!DOCTYPE>
<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<link href="http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<link href="${pageContext.request.contextPath}/css/toastr.css" rel="stylesheet" type="text/css" />

<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->

<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/easy-tabs.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>

<script src="${pageContext.request.contextPath}/app/controller/controller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/usercontroller.js"></script>

<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>

<!-- <script src="js/activeClass.js"></script> -->

<!-- <script>var url = "${pageContext.request.contextPath}";</script> -->

<script>
	
</script>

<style>
.full button span {
	background-color: lightblue;
	border-radius: 32px;
	color: black;
}

.partially button span {
	background-color: orange;
	border-radius: 32px;
	color: black;
}
</style>
<base href="/Kenure/">
</head>

<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
</script>

<body data-ng-app="myApp" id="admin">

	<div id="wrapper" data-ng-controller="logincontroller"
		data-ng-init="getDataPlanList()">
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'header'"></div>
		<div id="adminleftNavigationbar" data-ng-include
			data-src="'adminleftNavigationbar'"></div>
		
		<div id="content">
			<!--Content Start-->
			<h1>Customer Management
			
				<div class="rightbtns">
					<a href="userManagement" class="primary ic_back">Back</a>
				</div>
				
			</h1>
			<div class="middle">
			
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>
			
				<div class="boxpanel">
					<span class="heading">Add New Customer</span>
					<div class="box-body pad">
						<form class="login-form" name="addUserFrom">
							<div class="two_col">
								<div class="col">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label required">Customer Code :</label>
											<input type="text" class="form-control" maxlength="4"
												data-ng-model="customerCode" data-ng-trim="false"
												data-ng-change="customerCode = customerCode.split(' ').join('')"
												required autofocus="autofocus">
										</div>
										<div class="form-group" data-ng-show="!isCodeExist">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Customer Code
												Already Exists.</label>
										</div>
										<div class="form-group">
											<label class="control-label required">Company Name :
											</label> <input type="text" class="form-control"
												data-ng-model="firstName" maxlength="45" required>
										</div>
										<div class="form-group">
											<label class="control-label required">Contact Name (First and Last Name) :
											</label> <input type="text" class="form-control"
												data-ng-model="lastName" required>
										</div>
										<div class="form-group">
											<label class="control-label required">Username :</label> <input
												type="text" class="form-control" data-ng-model="userNameInput"
												data-ng-trim="false"
												data-ng-change="userName = userName.split(' ').join('')"
												maxlength="50" required>
										</div>
										<div class="form-group" data-ng-show="!isUserNameExist">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">UserName Already
												Exist</label>
										</div>
										<div class="form-group">
											<label class="control-label required">Data Plan (annual) :</label>
												 <select data-ng-model="dataplan"
												class="form-control" name="dataplan" required 
												data-ng-change="removeError('dataPlan')">
												<option selected="selected" value="">-- Select Data Plan --</option>
												<option data-ng-repeat="x in dataPlanList"
													value="{{x.dataPlanId}}">{{x.mbPerMonth}} MB</option>
											</select>
										</div>

										<div class="form-group" data-ng-show="isDataPlan">
											<label class="control-label ">&nbsp;</label>
											<span class="status error">Select Data Plan</span>
										</div>

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
												maxlength="100" required>
										</div>
										<div class="form-group">
											<label class="control-label required">Zipcode :</label> <input
												type="text" class="form-control" data-ng-model="zipcode"
												required>
										</div>
										<div class="form-group">
									<label class="control-label required">Data Plan Active
										Date:</label> <input type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="-- Click here to Select Date --"
										data-ng-model="addDataplanActivedate"
										data-is-open="popup1.opened" data-datepicker-options=""
										onkeypress="return false" data-close-text="Close"
										data-ng-click="open1()"
										data-alt-input-formats="altInputFormats" required>
									<!-- <span class="input-group-btn">  <button type="button" class="btn btn-default" data-ng-click="open1()">  <i class="glyphicon glyphicon-calendar"></i>  </button>  </span> -->
								</div>
								<div class="form-group">
									<label class="control-label required">Data Plan duration (in Years) :</label>
										<input type="number" class="form-control" data-ng-model="dataPlanDuration" min="1" max="20" required>
								</div>
								
								<div class="form-group ">
									<label class="control-label required">PortalPlan Active
										Date:</label> <input type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="-- Click here to Select Date --"
										data-ng-model="addPortalplanActiveDate"
										data-is-open="popup3.opened" data-datepicker-options=""
										onkeypress="return false" data-close-text="Close"
										data-ng-click="open3()"
										data-alt-input-formats="altInputFormats" required>
									<!-- <span class="input-group-btn">  <button type="button" class="btn btn-default" data-ng-click="open1()">  <i class="glyphicon glyphicon-calendar"></i>  </button>  </span> -->
								</div>
								
								<div class="form-group">
									<label class="control-label required">PortalPlan Expiry
										Date:</label> <input type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="-- Click here to Select Date --"
										data-ng-model="addPortalplanExpirydate"
										data-is-open="popup4.opened" data-datepicker-options=""
										onkeypress="return false" data-close-text="Close"
										data-ng-click="open4()"
										data-alt-input-formats="altInputFormats" required>
									<!-- <span class="input-group-btn">  <button type="button" class="btn btn-default" data-ng-click="open1()">  <i class="glyphicon glyphicon-calendar"></i>  </button>  </span> -->
								</div>

										<div class="form-group"
											data-ng-show="isActivePortalDateBigger">
											<label class="control-label ">&nbsp; </label>

											<div class="status error">
												<span> Portal Plan Expiry Date Not Be Bigger Than
													Portal Plan Start Date !!</span>
											</div>
										</div>

										<div class="form-group">
											<label class="control-label required">Country </label>
											<select name="country" data-ng-model="selectedCountry" class="form-control"
												required data-ng-change="removeError('country')">
												<option selected="selected" value="">-- Select Country --</option>
												<option data-ng-repeat="x in countryList"
												value="{{x.countryId}}">{{x.countryName}}</option>
											</select>
										</div>
										
										
										<div class="form-group" data-ng-show="isCountry">
											<label class="control-label ">&nbsp;</label>
											<span class="status error">Select Country</span>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Currency </label>
											<select name="currency" data-ng-model="selectedCurrency" class="form-control"
												required data-ng-change="removeError('currency')">
												<option selected="selected" value="">-- Select Currency --</option>
												<option data-ng-repeat="x in currencyList"
												value="{{x.currencyId}}" >{{x.currencyName}}</option>
											</select>
										</div>
										
										<div class="form-group" data-ng-show="isCurrency">
											<label class="control-label ">&nbsp;</label>
											<span class="status error">Select Currency</span>
										</div>
										
										<div class="form-group">
											<label class="control-label required">TimeZone (In Standard UTC) : </label>
											<select data-ng-model="selectedTimeZone" class="form-control"
												required>
												<option selected="selected" value="">-- Select TimeZone --</option>
												<option data-ng-repeat="x in timeZoneList" value="{{x}}" >{{x}}</option>
											</select>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Status :</label>
											<select data-ng-model="activeStatus"
												data-ng-options="item for item in ['ACTIVE', 'INACTIVE', 'INACTIVE-DATAPLAN', 'INACTIVE-PORTALPLAN']"
												required>
												<option selected="selected" value="">-- Select Status --</option>
											</select>
										</div>

										<div class="form-group">
											<label class="control-label">&nbsp;</label>
											<button type="submit" data-ng-click="addUser()"
												data-ng-model="addUserButton" class="primary ic_submit">Add
												User</button>
											<button type="button" data-ng-click="resetPageForAddCustomer()" class="default ic_reset">Reset</button>
										</div>

										<!-- Added below repeat actions for resolving full date appearance issue -->
										<div class="form-group"></div>
										<div class="form-group"></div>
										<div class="form-group"></div>
										<div class="form-group"></div>
										<div class="form-group"></div>
										<div class="form-group"></div>
										<div class="form-group"></div>
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



