<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<!-- <link href="http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"> -->
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
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
<script src="${pageContext.request.contextPath}/app/controller/customercontroller.js"></script>

<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>

<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
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

</head>


<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="customercontroller"
		data-ng-init="getTariffPlanAndBillingFrequencyAndSiteList()">
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>
		<div id="content">
			<!--Content Start-->
			<h1>
				Control&nbsp;&nbsp;>>&nbsp;&nbsp;Endpoint Management
				<div class="rightbtns">
					<a href="customerEPManagement" class="primary ic_back">Back</a>
				</div>
			</h1>

			

			<div class="middle">
			
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>
			
				<div class="form-group" data-ng-show="unKnownErrorDiv">
					<label class="control-label">&nbsp;</label>
					<label class="form-control status error">Unknown error occurred. 
					Please try with other Register Id AND/OR Endpoint Serial Number.</label>
				</div>
			
				<div class="boxpanel">
					<span class="heading">Add New Endpoint</span>
					<div class="box-body pad">
						<form name="addConsumerForm">
							<div class="two_col">
								<div class="col" style="width: 60%;">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label required">Register Id :</label> <input
												type="text" name="regId" class="form-control" maxlength="30"
												data-ng-model="registerId" data-ng-trim="false"
												data-ng-change="registerId = registerId.split(' ').join('')"
												data-ng-pattern="/^[0-9]{1,10}$/" required
												autofocus="autofocus">
										</div>
										<div class="form-group" data-ng-show="isRegisterIdExist">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Register id
												Already Exists !!</label>
										</div>
										<div class="form-group"
											data-ng-show="addConsumerForm.regId.$error.pattern">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Only Numbers are
												allowed.</label>
										</div>


										<div class="form-group">
											<label class="control-label">Street Name :</label> <input
												type="text" class="form-control" maxlength="255"
												data-ng-model="streetName" data-ng-trim="false"
												data-ng-change="streetName = streetName.split(' ').join('')">
										</div>
										<div class="form-group">
											<label class="control-label">Address1 :</label> <input
												type="text" class="form-control" maxlength="255"
												data-ng-model="address1" data-ng-trim="false"
												data-ng-change="address1 = address1.split(' ').join('')">
										</div>
										<div class="form-group">
											<label class="control-label">Address2 :</label> <input
												type="text" class="form-control" maxlength="255"
												data-ng-model="address2" data-ng-trim="false"
												data-ng-change="address2 = address2.split(' ').join('')">
										</div>
										<div class="form-group">
											<label class="control-label">Address3 :</label> <input
												type="text" class="form-control" maxlength="255"
												data-ng-model="address3" data-ng-trim="false"
												data-ng-change="address3 = address3.split(' ').join('')">
										</div>
										<div class="form-group">
											<label class="control-label">Zip Code :</label> <input
												type="text" class="form-control" maxlength="10"
												data-ng-model="zipcode" data-ng-trim="false"
												data-ng-change="zipcode = zipcode.split(' ').join('')">
										</div>
										<div class="form-group">
											<label class="control-label required">Endpoint Serial
												Number :</label> <input type="text" class="form-control"
												maxlength="10" data-ng-model="endpointSerialNumber"
												data-ng-trim="false"
												data-ng-change="endpointSerialNumber = endpointSerialNumber.split(' ').join('')"
												required>
										</div>
										<div class="form-group" data-ng-show="isSerialNumberExist">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Endpoint Serial
												Number Already Exists !!</label>
										</div>
										<div class="form-group">
											<label class="control-label">Latitude:</label> <input
												type="text" class="form-control" data-ng-model="latitude"
												data-ng-pattern="/^[0-9]{1,2}.[0-9]{1,5}$/" name="latitude"
												maxlength="8">
										</div>
										<div class="form-group"
											data-ng-show="addConsumerForm.latitude.$error.pattern">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Only Decimal
												Values are allowed. e.g(12.36, 1236)</label>
										</div>
										<div class="form-group">
											<label class="control-label">Longitude:</label> <input
												type="text" class="form-control" data-ng-model="longitude"
												data-ng-pattern="/^[0-9]{1,2}.[0-9]{1,5}$/" name="longitude"
												maxlength="8">
										</div>
										<div class="form-group"
											data-ng-show="addConsumerForm.longitude.$error.pattern">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Only Decimal
												Values are allowed. e.g(12.36, 1236)</label>
										</div>
										<div class="form-group">
											<label class="control-label">Endpoint Integrity :</label> <input
												type="text" class="form-control" maxlength="10"
												data-ng-model="endpointIntegrity" data-ng-trim="false"
												data-ng-change="endpointIntegrity = endpointIntegrity.split(' ').join('')">
										</div>
										<div class="form-group">
											<label class="control-label">Repeater :</label> <input
												type="checkbox" data-ng-model="isRepeater"
												style="outline: 1px solid #1e5180"> <span></span>
										</div>

										<div class="form-group">
											<label class="control-label required">Billing
												Frequency :</label> <select data-ng-model="selectedBillingFrequency"
												class="form-control" required>
												<option selected="selected" value="">--- Select Billing Frequency ---</option>
												<option
													data-ng-repeat="x in billingFrequencyNames track by $index"
													value="{{x.billingFrequencyId}}">{{x.billingFrequencyName}}</option>
											</select>
										</div>
										<div class="form-group">
											<label class="control-label required">Tariff Name :</label> <select
												data-ng-model="selectedTariffId" class="form-control"
												required>
												<option selected="selected" value="">--- Select Tariff Plan ---</option>
												<option data-ng-repeat="x in tariffPlanList track by $index"
													value="{{x.tariffId}}">{{x.tariffName}}</option>
											</select>
										</div>
										<div class="form-group">
											<label class="control-label required">Site Name :</label> <select
												data-ng-model="selectedSiteId" class="form-control" required>
												<option selected="selected" value="">--- Select Site ---</option>
												<option data-ng-repeat="x in siteList track by $index"
													value="{{x.siteId}}">{{x.siteName}}</option>
											</select>
										</div>
										<div class="form-group">
											<label class="control-label required">Last Meter
												Reading :</label> <input type="text" name="lastMeterReadingId"
												class="form-control" data-ng-model="lastMeterReading"
												maxlength="11" data-ng-trim="false"
												data-ng-change="lastMeterReading = lastMeterReading.split(' ').join('')"
												data-ng-pattern="/^[0-9]{1,10}$/" required>
										</div>
										<div class="form-group"
											data-ng-show="addConsumerForm.lastMeterReadingId.$error.pattern">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Only Numbers are
												allowed.</label>
										</div>
										<div class="form-group">
											<label class="control-label required">Last Meter
												Reading Date :</label> <input type="text"
												class="form-control calendar-input"
												data-uib-datepicker-popup="{{format}}"
												placeholder="-- Click here to Select Last Meter Reading Date --"
												data-ng-model="latsMeterReadingDate"
												data-is-open="popup3.opened" data-datepicker-options=""
												onkeydown="return false" data-close-text="Close"
												data-ng-click="open3()"
												data-alt-input-formats="altInputFormats" required>
										</div>
										<div class="form-group">
											<label class="control-label required">Meter Billing
												Date :</label> <input type="text"
												class="form-control calendar-input"
												data-uib-datepicker-popup="{{format}}"
												placeholder="-- Click here to Select Meter Billing Date --"
												data-ng-model="meterReadingDate"
												data-is-open="popup1.opened" data-datepicker-options=""
												onkeydown="return false" data-close-text="Close"
												data-ng-click="open1()"
												data-alt-input-formats="altInputFormats" required>
										</div>
										<div class="form-group">
											<label class="control-label required">Billing Start
												Date :</label> <input type="text"
												class="form-control calendar-input"
												data-uib-datepicker-popup="{{format}}"
												placeholder="-- Click here to Select Billing Start Date --"
												data-ng-model="billingStartDate"
												data-is-open="popup2.opened" data-datepicker-options=""
												onkeydown="return false" data-close-text="Close"
												data-ng-click="open2()"
												data-alt-input-formats="altInputFormats" required>
										</div>
										<div class="form-group">
											<label class="control-label">Unit Of Measure :</label> <input
												type="text" class="form-control" maxlength="5"
												data-ng-model="unitOfMeasure" data-ng-trim="false"
												data-ng-change="unitOfMeasure = unitOfMeasure.split(' ').join('')">
										</div>
										<div class="form-group">
											<label class="control-label">No Of Occupants :
											</label> <input type="text" name="noOfOccupants"
												class="form-control" data-ng-model="noOfOccupants"
												maxlength="2" data-ng-trim="false"
												data-ng-change="noOfOccupants = noOfOccupants.split(' ').join('')"
												data-ng-pattern="/^[0-9]{1,10}$/">
										</div>
										<div class="form-group"
											data-ng-show="addConsumerForm.noOfOccupants.$error.pattern">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Only Numbers are
												allowed.</label>
										</div>
										
										<div class="form-group"></div>
										<div class="form-group">
											<label class="control-label">&nbsp;</label>
											<button type="submit" data-ng-click="addConsumer()"
												data-ng-model="addConsumerButton" class="primary ic_submit">Add
												New Endpoint</button>
											<button type="button" data-ng-click="resetErrors()"
												class="default ic_reset">Reset</button>
										</div>
										<!-- Added below repeat actions for resolving full date appearance issue -->
										<div class="form-group"></div>
										<div class="form-group"></div>
										<div class="form-group"></div>
										<div class="form-group"></div>
										<div class="form-group"></div>
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

		<div id="footer" data-ng-include data-src="'../footer'"></div>
	</div>
	<!--wrapper end-->

</body>
</html>