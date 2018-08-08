<!DOCTYPE>

<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/toastr.css" rel="stylesheet" type="text/css" />
<link
	href="http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />	
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
<script src="${pageContext.request.contextPath}/app/controller/popupcontroller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/customercontroller.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>

<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
</script>

<script>
function markFieldsDisabled() {
	
	var divElement = document.getElementById("disableId");

	var inputs = divElement.getElementsByTagName("input");
    for (var i = 0; i < inputs.length; i++) {
    	inputs[i].disabled = true;
    }
    
    var selects = divElement.getElementsByTagName("select");
    for (var i = 0; i < selects.length; i++) {
    	selects[i].disabled = true;
    }
}
</script>
</head>

<body data-ng-app="myApp">
	<div id="wrapper" data-ng-controller="customercontroller"
		data-ng-init="getEditConsumerDetails()">
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
			
				<div class="boxpanel">
					<span class="heading">Edit Endpoint</span>
					<div class="box-body pad">
						<form class="" name="editConsumerForm">
							<div class="two_col">
								<div class="col">
									<div id="disableId" class="form-horizontal">
										<div class="form-group">
											<label class="control-label">Consumer Account Number :</label> <input
												type="text" class="form-control" maxlength="30"
												data-ng-model="conAccNumber" data-ng-disabled="true">
										</div>
										<div class="form-group">
											<label class="control-label">Register Id :</label> <input
												type="text" class="form-control" maxlength="30"
												data-ng-model="registerId" data-ng-trim="false"
												data-ng-change="registerId = registerId.split(' ').join('')"
												disabled> <input type="hidden"
												data-ng-model="consumerMeterId" />
											<!-- Hidden field to set consumerMeterId -->
										</div>
										<div class="form-group">
											<label class="control-label">Street Name :</label> <input
												type="text" class="form-control" maxlength="255"
												data-ng-model="streetName" data-ng-trim="false"
												data-ng-change="streetName = streetName.split(' ').join('')" autofocus="autofocus">
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
											<label class="control-label">Endpoint Serial Number :</label>
											<input type="text" class="form-control" maxlength="10"
												data-ng-model="endpointSerialNumber" data-ng-trim="false"
												data-ng-change="endpointSerialNumber = endpointSerialNumber.split(' ').join('')"
												disabled>
										</div>
										<div class="form-group">
											<label class="control-label">Latitude:</label> <input
												type="text" class="form-control" data-ng-model="latitude"
												data-ng-pattern="/^[0-9]{1,2}.[0-9]{1,5}$/" name="latitude"
												maxlength="8">
										</div>
										<div class="form-group"
											data-ng-show="editConsumerForm.latitude.$error.pattern">
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
											data-ng-show="editConsumerForm.longitude.$error.pattern">
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
												Frequency :</label> <select data-ng-model="savedBillingFrequency"
												class="form-control"
												data-ng-options="y.billingFrequencyId as y.billingFrequencyName for y in billingFrequencyList"
												required>
											</select>
										</div>
										<div class="form-group">
											<label class="control-label required">Tariff Name :</label> <select
												data-ng-model="savedTariffId" class="form-control"
												data-ng-options="y.tariffId as y.tariffName for y in tariffList"
												required>
											</select>
										</div>
										<div class="form-group">
											<label class="control-label required">Site Name :</label> <select
												data-ng-model="savedSiteId" class="form-control"
												data-ng-options="y.siteId as y.siteName for y in siteList"
												required>
											</select>
										</div>








										<div class="form-group">
											<label class="control-label">No. of endpoints
												attached :</label> <input type="text" class="form-control"
												maxlength="2" data-ng-model="totalEndpointAttached"
												data-ng-trim="false"
												data-ng-change="totalEndpointAttached = totalEndpointAttached.split(' ').join('')"
												disabled>
										</div>
										<div data-ng-if="batteryVoltage < 3.2">
											<div class="form-group">
												<label class="control-label">Battery Voltage :</label> <input
													type="text" class="form-control" maxlength="10"
													data-ng-model="batteryVoltage" data-ng-trim="false"
													data-ng-change="batteryVoltage = batteryVoltage.split(' ').join('')"
													style="color: red; font-weight: bold; border-color: red;"
													disabled>
											</div>
										</div>
										<div data-ng-if="batteryVoltage >= 3.2 || batteryVoltage == null">
											<div class="form-group">
												<label class="control-label">Battery Voltage :</label> <input
													type="text" class="form-control" maxlength="10"
													data-ng-model="batteryVoltage" data-ng-trim="false"
													data-ng-change="batteryVoltage = batteryVoltage.split(' ').join('')"
													disabled>
											</div>
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
									</div>
								</div>
								<div class="col">
									<div id="disableId1" class="form-horizontal">
										<div class="form-group">
											<label class="control-label">Current Reading
												Time Stamp :</label> <input type="text"
												class="form-control calendar-input"
												data-uib-datepicker-popup="{{format}}"
												placeholder="{{currentReadingDate}}"
												data-ng-model="currentReadingDate"
												data-is-open="popup4.opened" data-datepicker-options=""
												onkeydown="return false" data-close-text="Close"
												data-ng-click="open4()"
												data-alt-input-formats="altInputFormats" disabled>
										</div>

										<div class="form-group">
											<label class="control-label">Install Date :</label>

											<input type="text" class="form-control calendar-input"
												data-uib-datepicker-popup="{{format}}"
												placeholder="{{installDate}}" data-ng-model="installDate"
												data-is-open="popup5.opened" data-datepicker-options=""
												onkeydown="return false" data-close-text="Close"
												data-ng-click="open5()"
												data-alt-input-formats="altInputFormats" disabled>
										</div>
										<div class="form-group">
											<label class="control-label">Battery
												Replaced Date :</label> <input type="text"
												class="form-control calendar-input"
												data-uib-datepicker-popup="{{format}}"
												placeholder="{{batteryReplacedDate}}"
												data-ng-model="batteryReplacedDate"
												data-is-open="popup6.opened" data-datepicker-options=""
												onkeydown="return false" data-close-text="Close"
												data-ng-click="open6()"
												data-alt-input-formats="altInputFormats" disabled>
										</div>
										<div class="form-group">
											<label class="control-label">Asset
												Inspection Date :</label> <input type="text"
												class="form-control calendar-input"
												data-uib-datepicker-popup="{{format}}"
												placeholder="{{assetInspectionDate}}"
												data-ng-model="assetInspectionDate"
												data-is-open="popup7.opened" data-datepicker-options=""
												onkeydown="return false" data-close-text="Close"
												data-ng-click="open7()"
												data-alt-input-formats="altInputFormats" disabled>
										</div>
										<div class="form-group">
											<label class="control-label">District Meter Serial
												No. :</label> <input type="text" class="form-control" maxlength="11"
												data-ng-model="districtMeterSerialNo" data-ng-trim="false"
												data-ng-change="districtMeterSerialNo = districtMeterSerialNo.split(' ').join('')"
												disabled>
										</div>






										<div class="form-group">
											<label class="control-label required">Last Meter
												Reading :</label> <input type="text" name="lastMeterReadingId"
												class="form-control" maxlength="11"
												data-ng-model="lastMeterReading" data-ng-trim="false"
												data-ng-change="lastMeterReading = lastMeterReading.split(' ').join('')"
												data-ng-pattern="/^[0-9]{1,10}$/" required>
										</div>
										<div class="form-group"
											data-ng-show="editConsumerForm.lastMeterReadingId.$error.pattern">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Only Numbers are
												allowed.</label>
										</div>
										<div class="form-group">
											<label class="control-label required">Last Meter
												Reading Date :</label> <input type="text"
												class="form-control calendar-input"
												data-uib-datepicker-popup="{{format}}"
												placeholder="{{latsMeterReadingDate}}"
												data-ng-model="latsMeterReadingDate"
												data-is-open="popup3.opened" data-datepicker-options=""
												onkeydown="return false" data-close-text="Close"
												data-ng-click="open3()"
												data-alt-input-formats="altInputFormats" required="required">
										</div>

										<!-- <div class="form-group">
											<label class="control-label required">Meter Billing
												Date :</label>
											<p class="input-group">
												<input type="text" class="form-control"
													data-uib-datepicker-popup="{{format}}"
													data-ng-model="meterReadingDate"
													placeholder="{{meterReadingDate}}"
													data-is-open="popup1.opened" data-datepicker-options=""
													data-close-text="Close"
													data-alt-input-formats="altInputFormats" /> <span
													class="input-group-btn">

													<button type="button" class="btn btn-default"
														data-ng-click="open1()">
														<i class="glyphicon glyphicon-calendar"></i>
													</button>
												</span>
											</p>
										</div> -->
										<div class="form-group">
											<label class="control-label required">Meter Billing
												Date :</label> <input type="text"
												class="form-control calendar-input"
												data-uib-datepicker-popup="{{format}}"
												placeholder="{{meterReadingDate}}"
												data-ng-model="meterReadingDate"
												data-is-open="popup1.opened" data-datepicker-options=""
												onkeydown="return false" data-close-text="Close"
												data-ng-click="open1()"
												data-alt-input-formats="altInputFormats">
										</div>
										<!-- <div class="form-group">
											<label class="control-label required">Billing Start
												Date :</label>
											<p class="input-group">
												<input type="text" class="form-control"
													data-uib-datepicker-popup="{{format}}"
													data-ng-model="billingStartDate"
													placeholder="{{billingStartDate}}"
													data-is-open="popup2.opened" data-datepicker-options=""
													data-close-text="Close"
													data-alt-input-formats="altInputFormats" /> <span
													class="input-group-btn">

													<button type="button" class="btn btn-default"
														data-ng-click="open2()">
														<i class="glyphicon glyphicon-calendar"></i>
													</button>
												</span>
											</p>
										</div> -->
										<div class="form-group">
											<label class="control-label required">Billing Start
												Date :</label> <input type="text"
												class="form-control calendar-input"
												data-uib-datepicker-popup="{{format}}"
												placeholder="{{billingStartDate}}"
												data-ng-model="billingStartDate"
												data-is-open="popup2.opened" data-datepicker-options=""
												onkeydown="return false" data-close-text="Close"
												data-ng-click="open2()"
												data-alt-input-formats="altInputFormats">
										</div>
										<div class="form-group" data-ng-controller="popupcontroller">
											<label class="control-label"></label>
											<button data-ng-click="viewUploadNotes(consumerMeterId, textNote, filePath, originalFileName)" 
													class="btn primary noicon" style="margin-top:-2px;">View/Upload Note</button>
											<!-- <label class="control-label">Note :</label>
											<span>
												<input type="text" name="noteText"
												maxlength="200"
												data-ng-model="noteText" data-ng-trim="false"
												data-ng-change="noteText = noteText.split(' ').join('')">
											</span>
											<span>
												<button data-ng-click="checkAlerts(consumerMeterId)" class="btn btn-primary" style="margin-top:-2px;">View/Upload Note</button>
												<input type="file" name="file" onchange="angular.element(this).scope().uploadFile(this.files)"/>
											</span> -->
										</div>
										<div class="form-group">
											<label class="control-label required">Active Status :</label>
											<select data-ng-model="isActive"
												data-ng-options="boolToStr(item) for item in [true, false]"
												required="required">
												<option selected="selected">== Select Status ==</option>
											</select>
										</div>
										<div class="form-group" data-ng-controller="popupcontroller">
											<label class="control-label"></label>
											<button data-ng-click="checkAlerts(consumerMeterId)" class="btn primary noicon">Check Alerts</button>
											<button data-ng-click="resetAlerts(consumerMeterId)" class="btn default noicon">Reset Alerts</button>
										</div>
										
										
										<div data-ng-if="isCustomer == false">
											<div class="form-group">
												<label class="control-label">Hosepipe :</label> <input
													type="checkbox" data-ng-model="hosepipe.value"
													style="outline: 1px solid #1e5180"> <span></span>
											</div>
											<div class="form-group">
												<label class="control-label">Irrigation System :</label> <input
													type="checkbox" data-ng-model="irrigationSystem.value"
													style="outline: 1px solid #1e5180"> <span></span>
											</div>
											<div class="form-group">
												<label class="control-label">Swimming Pool :</label> <input
													type="checkbox" data-ng-model="swimmingPool.value"
													style="outline: 1px solid #1e5180"> <span></span>
											</div>
											<div class="form-group">
												<label class="control-label">Hot Tub :</label> <input
													type="checkbox" data-ng-model="hotTub.value"
													style="outline: 1px solid #1e5180"> <span></span>
											</div>
											<div class="form-group">
												<label class="control-label">Pond :</label> <input
													type="checkbox" data-ng-model="pond.value"
													style="outline: 1px solid #1e5180"> <span></span>
											</div>
										</div>
									</div>
								</div>

							</div>
							<div class="bottom_buttons">
								<label class="control-label">&nbsp;</label>
								<button type="submit" data-ng-click="updateConsumer('customer')"
									data-ng-model="addConsumerButton" class="primary ic_submit">Update
									Endpoint</button>
							</div>
						</form>
					</div>
				</div>

			</div>
			<!--Content end-->
		</div>

		<div id="" data-ng-include data-src="'../footer'"></div>
	</div>
	<!--wrapper end-->
</body>
</html>