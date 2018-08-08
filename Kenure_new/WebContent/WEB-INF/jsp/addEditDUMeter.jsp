<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>

<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/toastr.css" rel="stylesheet" type="text/css" />
<link href="http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
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
<script src="${pageContext.request.contextPath}/app/controller/customercontroller.js"></script>

<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>

<style>
.full button span {
	background-color: blue;
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
<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
</script>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="customercontroller" data-ng-init="getEditDUMeter()">

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include data-src="'customerleftNavigationbar'"></div>
	
		<div id="content">
			<!--Content Start-->
			<h1>
				Control&nbsp;&nbsp;>>&nbsp;&nbsp;District Meter Management
				
				
			
				<div class="rightbtns">
					<a href="districtUtilityMeter" class="primary ic_back">Back</a>
				</div>
				
			</h1>
			<div class="middle" data-ng-show="addDUMeterDiv">
			
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}}</span>
				</div>
			
				<div class="boxpanel">
					<span class="heading">Add District Utility Meter</span>
					
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post" name="addDUMeter">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label required">Serial Number</label>
											<input type="text" class="form-control" name="SerialNumber" data-ng-trim="false" maxlength="50"
												 data-ng-model="DUMeterSerialNumber" required autofocus="autofocus">
										</div>
										<div class="form-group" data-ng-show="enterSerialNumber">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Serial number can't be empty.</label>
										</div>
										<div class="form-group" data-ng-show="isDUMeterExist">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Meter Already Exists.</label>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Meter Reading</label>
											<input type="text" class="form-control" name="MeterReading" data-ng-trim="false" data-ng-pattern="/^[0-9]{1,10}$/" maxlength="10" data-ng-model="DUMeterReading" required>
										</div>
										<div class="form-group" data-ng-show="addDUMeter.MeterReading.$error.pattern">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Only Integer Value allowed.</label>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Start Meter Billing Date</label>

												<input type="text" class="form-control calendar-input"
													data-uib-datepicker-popup="{{format}}" placeholder="-- Click here to Select Start Billing Date --"
													data-ng-model="startBillingDate" data-is-open="popup2.opened"
													data-datepicker-options=""  onkeypress="return false"
													data-close-text="Close" data-ng-click="open2()"
													data-alt-input-formats="altInputFormats" required>
												
												<!-- <span class="input-group-btn">
													<button type="button" class="btn btn-default" data-ng-click="open1()">
														<i class="glyphicon glyphicon-calendar"></i>
													</button>
												</span> -->
										</div>
										<div class="form-group" data-ng-show="startBilling">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Start Meter Billing Date Can't be Empty.</label>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Meter Reading Date</label>

												<input type="text" class="form-control calendar-input"
													data-uib-datepicker-popup="{{format}}" placeholder="-- Click here to Select End Billing Date --"
													data-ng-model="endBillingDate" data-is-open="popup3.opened"
													data-datepicker-options=""  onkeypress="return false"
													data-close-text="Close" data-ng-click="open3()"
													data-alt-input-formats="altInputFormats" required>
												
												<!-- <span class="input-group-btn">
													<button type="button" class="btn btn-default" data-ng-click="open1()">
														<i class="glyphicon glyphicon-calendar"></i>
													</button>
												</span> -->
										</div>
										<div class="form-group" data-ng-show="endBilling">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">End Meter Billing Date Can't be Empty.</label>
										</div>
										<div class="form-group">
											<label class="control-label">&nbsp;</label>
											<button type="submit" data-ng-click="insertDUMeter()"
												data-ng-model="addDUMeter" class="primary ic_submit">Add Meter</button>
											
										</div>


									</div>

								</form>



							</div>
						</div>

					</div>
				</div>

			</div>
			
			
			<div class="middle" data-ng-show="editDUMeterDiv">
			
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}}</span>
				</div>
			
				<div class="boxpanel">
					<span class="heading">Edit Installation</span>
					
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post" name="editDUMeter">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label required">Serial Number</label>
											<input type="text" class="form-control" name="SerialNumber" data-ng-trim="false" maxlength="50"
												 data-ng-model="districtUtilityMeterSerialNumber" data-ng-trim="false" required disabled="disabled">
										</div>
										<div class="form-group" data-ng-show="enterSerialNumber">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Serial number can't be empty.</label>
										</div>
										<div class="form-group" data-ng-show="isDUMeterExist">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Meter Already Exists.</label>
										</div>
										
										<div class="form-group">
											<label class="control-label required">Meter Reading</label>
											<input type="text" class="form-control" name="MeterReading" data-ng-trim="false"
												 data-ng-pattern="/^[0-9]{1,10}$/" maxlength="10" data-ng-model="currentReading" autofocus="autofocus" required>
										</div>
										<div class="form-group" data-ng-show="addDUMeter.MeterReading.$error.pattern">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Only Integer Value allowed.</label>
										</div>
																				
										<div class="form-group">
											<label class="control-label required">Start Meter Billing Date</label>

												<input type="text" class="form-control calendar-input"
													data-uib-datepicker-popup="{{format}}" placeholder="-- {{startBillingDate}} --"
													data-ng-model="startBillingDate" data-is-open="popup2.opened"
													data-datepicker-options="" data-ng-required="true"
													data-close-text="Close" data-ng-click="open2()"
													data-alt-input-formats="altInputFormats" onkeypress="return false">
										</div>
										
										<div class="form-group">
											<label class="control-label required">End Meter Billing Date</label>

												<input type="text" class="form-control calendar-input"
													data-uib-datepicker-popup="{{format}}" placeholder="-- {{endBillingDate}} --"
													data-ng-model="endBillingDate" data-is-open="popup3.opened"
													data-datepicker-options="" data-ng-required="true"
													data-close-text="Close" data-ng-click="open3()"
													data-alt-input-formats="altInputFormats" onkeypress="return false">
										</div>
										
										<input type="hidden" data-ng-model="districtMeterTransactionId" >
										
										<div class="form-group">
											<label class="control-label"></label>
											<button class="primary ic_submit" data-ng-click="updateDUMeter(districtMeterTransactionId)"
												 type="submit">Update</button>
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


		<div id="footer" data-ng-include data-src="'../footer'"></div>
	</div>
	<!--wrapper end-->
</body>
</html>

