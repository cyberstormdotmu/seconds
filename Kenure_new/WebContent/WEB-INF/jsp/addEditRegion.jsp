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

<script src="${pageContext.request.contextPath}/app/controller/controller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/usercontroller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/customercontroller.js"></script>

<script>

$(document).ready(function(){
	$('#name1').focus();
	$('#name2').focus();
	});

</script>

</head>
<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="customercontroller" data-ng-init="getEditRegion()">

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include data-src="'customerleftNavigationbar'"></div>
	
		<div id="content">
			<!--Content Start-->
			<h1>
				Control&nbsp;&nbsp;>>&nbsp;&nbsp;Installation Management
				
				<div class="rightbtns">
					<a href="regionManagement" class="primary ic_back">Back</a>
				</div>
				
			</h1>
			<div class="middle" data-ng-show="addRegionDiv" onload="document.addInstallationForm.name1.focus()">
			
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}}</span>
				</div>
			
				<div class="status success" data-ng-show="updatedSuccessfully">
					<span>Installation added successfully.</span>
				</div>
				
				<div class="status error" data-ng-show="errorStatus" data-ng-init="errorStatus = false">
					<span>Please enter proper installation detail.</span>
				</div>
				
				<div class="boxpanel">
					<span class="heading">Add Installation</span>
					
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post" name="addInstallationForm">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label required">Installation Name :</label>
											 <input type="text" class="form-control" maxlength="45" data-ng-model="regionName" 
											 required autofocus="autofocus">
										</div>
										<div class="form-group" data-ng-show="isRegionExist">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Installation
												Already Exists.</label>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Country :</label> <select
												data-ng-model="selectedCountryId" class="form-control"
												required>
												<option value="">--- Select Country ---</option>
												<option data-ng-repeat="x in countryList track by $index"
													value="{{x.countryId}}">{{x.countryName}}</option>
											</select>
										</div>
										<div class="form-group">
											<label class="control-label required">Currency :</label> <select
												data-ng-model="selectedCurrencyId" class="form-control"
												required>
												<option value="">--- Select Currency ---</option>
												<option data-ng-repeat="x in currencyList track by $index"
													value="{{x.currencyId}}">{{x.currencyName}}</option>
											</select>
										</div>
										<div class="form-group">
											<label class="control-label required">Time Zone :</label> <select
												data-ng-model="selectedTimeZone" class="form-control"
												required>
												<option value="">--- Select Time Zone ---</option>
												<option data-ng-repeat="x in timeZoneList track by $index"
													value="{{x}}">{{x}}</option>
											</select>
										</div>
										
										<label class="control-label">&nbsp;</label>
										<button class="primary ic_submit"
											data-ng-click="insertRegion()"
											data-ng-model="updateRegion" type="submit">Add Installation</button>




									</div>

								</form>



							</div>
						</div>

					</div>
				</div>

			</div>
			
			<div class="middle" data-ng-show="editRegionDiv">
			
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}}</span>
				</div>
			
				<div class="status success" data-ng-show="updatedSuccessfully">
					<span>Installation Updated Successfully</span>
				</div>
				
				<div class="status error" data-ng-show="errorStatus" data-ng-init="errorStatus = false">
					<span>Please enter proper installation detail.</span>
				</div>
			
				<div class="boxpanel">
					<span class="heading">Edit Installation</span>
				
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label required">Installation Name</label>
												<input type="text" class="form-control" maxlength="45" 
													 data-ng-model="regionName" 
													 required autofocus="autofocus">
												<!-- <input type="hidden" data-ng-model="regionId" /> -->
										</div>
										
										<div class="form-group" data-ng-show="isRegionExist">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Installation
												Already Exists.</label>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Country :</label> <select
												data-ng-model="savedCountryId" class="form-control"
												data-ng-options="y.countryId as y.countryName for y in countryList"
												required>
											</select>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Currency :</label> <select
												data-ng-model="savedCurrencyId" class="form-control"
												data-ng-options="y.currencyId as y.currencyName for y in currencyList"
												required>
											</select>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Time Zone :</label> <select
												data-ng-model="savedTimeZone" class="form-control"
												data-ng-options="y as y for y in timeZoneList"
												required>
											</select>
										</div>
										
										<label class="control-label">&nbsp;</label>
										<button class="primary ic_submit"
											data-ng-click="updateRegion(regionId)"
											data-ng-model="updateRegion" type="submit">Update</button>




									</div>

								</form>



							</div>
						</div>

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

