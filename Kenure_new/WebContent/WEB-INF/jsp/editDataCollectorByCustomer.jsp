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

</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="customercontroller"
		data-ng-init="getEditdatacollectorByCustomer()">
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include	data-src="'customerleftNavigationbar'"></div>
		<div id="content">
			<!--Content Start-->
			<h1>
				Control&nbsp;&nbsp;>>&nbsp;&nbsp;DataCollector Management
					
				<div class="rightbtns">
					<a href="dcManagement" class="primary ic_back">Back</a>
				</div>
					
			</h1>
			<div class="middle">
			
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}}</span>
				</div>
			
				<div class="status error" data-ng-show="isErrorDiv" data-ng-init="isErrorDiv=false">
					<span>{{errorMsg}}</span>
				</div>

				<div class="boxpanel">
					<span class="heading">Edit DataCollector</span>
					<div class="status success" data-ng-show="updatedSuccessfully">
						<span>record updated successfully</span>
					</div>
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post" name="editDataCollectorByCustomer">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label required">DataCollector
												ID:</label> <input type="text" class="form-control"
												data-ng-model="datacollectorId" disabled>
										</div>
										<div class="form-group">
											<label class="control-label required">DC Serial
												Number:</label> <input type="text" class="form-control"
												data-ng-model="dcSerialNumber" data-ng-disabled="toggle">
										</div>

										<div class="form-group">
											<label class="control-label">&nbsp;</label>
											<button class="default ic_reset" data-ng-click=" dcSerialBtnClicked()" >{{toggleText}}</button>
										</div>

										<div class="form-group" data-ng-show="isError">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">{{error}}</label>
										</div>

										<div class="form-group">
											<label class="control-label">DC SIM Number:</label> <input
												type="text" class="form-control"
												data-ng-disabled="nullSiteToggle" data-ng-model="dcSimNo">
										</div>

										<div class="form-group" data-ng-show="nullSite">
											<label class="control-label">&nbsp;</label>
											<button class="default ic_reset" data-ng-click="dcSimBtnClicked()">{{nullSiteToggleText}}</button>
										</div>

										<div class="form-group">
											<label class="control-label required"> Site ID
												Number:</label> <input type="text" class="form-control"
												data-ng-model="site" disabled>
										</div>

										<div class="form-group">
											<label class="control-label required">IP Address:</label> <input
												type="text" class="form-control" data-ng-model="dcIp"
												disabled>
										</div>


										<div class="form-group">
											<label class="control-label required"> No. of
												EndPoints Number:</label> <input type="text" class="form-control"
												data-ng-model="totalEndpoints" disabled>
										</div>

										<div class="form-group">
											<label class="control-label required"> Meter Reading
												Interval Number:</label> <input type="text" class="form-control"
												data-ng-model="meterReadingInterval" disabled>
										</div>

										<div class="form-group">
											<label class="control-label required"> Network status
												Interval Number:</label> <input type="text" class="form-control"
												data-ng-model="networkStatusInterval" disabled>
										</div>

										<div class="form-group">
											<label class="control-label required"> Is Connected</label> <input
												type="text" class="form-control"
												data-ng-model="iscommissioned" disabled>
										</div>

										<div class="form-group">
											<label class="control-label">Latitude:</label> <input
												type="text" class="form-control" data-ng-trim="false"
												data-ng-pattern="/^[0-9]{1,2}.[0-9]{1,5}$/" 
												data-ng-model="latitude" name="latitude">
										</div>
										<div class="form-group"
											data-ng-show="editDataCollectorByCustomer.latitude.$error.pattern">
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
											data-ng-show="editDataCollectorByCustomer.longitude.$error.pattern">
											<label class="control-label">&nbsp;</label> <label
												class="form-control status error">Only Decimal Value
												Allowed. Decimal Point is After First 2 Digits.
												e.g(12.36)</label>
										</div>



										<label class="control-label">&nbsp;</label>
										<button class="primary ic_submit" data-ng-click="updateDataCollectorByCustomer(datacollectorId)"
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


		<div id="footer" data-ng-include data-src="'../footer'"></div>
	</div>
	<!--wrapper end-->
</body>
</html>

