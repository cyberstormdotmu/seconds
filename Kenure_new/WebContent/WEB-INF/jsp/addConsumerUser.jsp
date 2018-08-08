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

</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="customercontroller"
		data-ng-init="getAddConsumerData()">
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>
		<div id="content">
			<!--Content Start-->
			<h1>Control&nbsp;&nbsp;>>&nbsp;&nbsp;Consumer Management
			
				<div class="rightbtns">
					<a href="consumerUserManagement" class="primary ic_back">Back</a>
				</div>
			
			</h1>
			<div class="middle">
			
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>
			
				<div class="boxpanel">
					<span class="heading">Add Consumer</span>
					<div class="box-body pad">
						<form class="login-form" name="addConsumerForm">
							<div class="two_col">
								<div class="col">
									<div class="form-horizontal">
										
										<div class="form-group">
											<label class="control-label required">Consumer Account Number :</label>
											<input type="text" class="form-control" maxlength="30"
												data-ng-model="consumerAccountNumber" data-ng-trim="false"
												data-ng-change="consumerAccountNumber = consumerAccountNumber.split(' ').join('')"
												required autofocus="autofocus">
										</div>
										<div class="form-group" data-ng-show="!isCodeExist">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Consumer Account Number Already Exists</label>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Consumer First
												Name :</label> <input type="text" class="form-control"
												data-ng-model="firstName" maxlength="45" required>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Consumer Last
												Name :</label> <input type="text" class="form-control"
												data-ng-model="lastName" maxlength="45" required>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Username :</label> <input
												type="text" class="form-control" data-ng-model="userName"
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
											<label class="control-label required">Phone:</label> <input
												type="text" class="form-control"
												data-ng-model="cell_number1" maxlength="15" required>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Email:</label> <input
												type="text" class="form-control" data-ng-model="email1"
												data-ng-trim="false" maxlength="50" name="email1"
												data-ng-pattern="emailFormat" required>
										</div>
										
										<div class="form-group" data-ng-show="addConsumerForm.email1.$error.pattern">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Please Insert Valid Email Address.</label>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Address:</label> <input
												type="text" class="form-control " maxlength="255"
												data-ng-model="address1" required>
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
										
										<div class="form-group">
											<label class="control-label required">Tariff Plan :</label>
											<select data-ng-model="tariffPlan" class="form-control" required>
												<option value="">-- Select Tariff Plan --</option>
												<option data-ng-repeat="x in tariffPlanList" value="{{x.consumerMeterId}}">{{x.tariffPlanName}}</option>
											</select>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Active Status :</label>
											<!-- <select data-ng-model="activeStatus" required>
												<option value="">-- Select Status --</option>
												<option value="true">Active</option>
												<option value="false">Inactive</option>
												
											</select> -->
											<select data-ng-model="activeStatus"
												data-ng-options="item for item in ['Active', 'Inactive']"
												required>
											</select>
										</div>
										
										<div class="form-group">
											<label class="control-label">&nbsp;</label>
											<button type="submit" data-ng-click="addConsumerUser()" data-ng-model="addConsumerButton" class="primary ic_submit">Add	Consumer</button>
											<button type="button" data-ng-click="resetPageForAddConsumer()" class="default ic_reset">Reset</button>
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


		<div id="footer" data-ng-include data-src="'../footer'"></div>
	</div>
	<!--wrapper end-->


</body>
</html>