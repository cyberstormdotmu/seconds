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
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->

<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/commissioningcontroller.js"></script>

<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
</script>

</head>


<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="commissioningcontroller"
		data-ng-init="phase2data()">

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>Phase 2</h1>

			<div class="middle">

				<div class="status error" data-ng-show="isError">
					<span> {{error}}</span>
				</div>

				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}}</span>
				</div>

				<div class="boxpanel">
					<div class="installed-datacollectors">
						<div class="installed-box">
							<div class="title-hed">
								<span class="heading">Search Listing</span>
								<div class="optopn-box">
									<a href="" class="toggle"><span></span></a>
									<ul class="optopn-link">
										<li><a href="#">start/Continue Installation</a></li>
										<li><a href="#">Re-schedule Commissioning</a></li>
										<li><a href="#">Add more endpoints</a></li>
										<li><a href="#">Set Operating Mode</a></li>
										<li><a href="#">View Network Map</a></li>
										<li><a href="#">Check DC Connectivity</a></li>
										<li><a href="">Add/Edit Installation</a></li>
										<li><a href="">Add/Edit Site</a></li>
										<li><a href="">Assign/Configure DC</a></li>
										<li><a href="">Add Endpoints</a></li>
										<li><a href="">Add Repeaters</a></li>
									</ul>
								</div>
							</div>

							<div class="border-box">
								<div class="box-body">
									<div class="grid-content">
										<table class="grid">
											<tr>
												<th>Street Name</th>
												<th>Number of Endpoints</th>
												<th>Installer</th>
											</tr>

											<tr data-ng-repeat="(key,value) in hashData">
												<td>{{key}}</td>
												<td>{{value}}</td>
												<td><select data-ng-model="installerSelected"
													data-ng-options="installer as installer for installer in installerName"
													data-ng-change="update(installerSelected,key,value)"
													data-ng-disabled="checkForTag(); "></select></td>
											</tr>

										</table>

									</div>
								</div>
							</div>

						</div>
						<!-- ================================================= -->

						<div class="installed-box">
							<div class="title-hed">
								<span class="heading">Spare DataCollectors :
									{{totalSpareDC}}</span>
								<div class="optopn-box">
									<a href="" class="toggle"><span></span></a>
									<ul class="optopn-link">
										<li><a href="#">start/Continue Installation</a></li>
										<li><a href="#">Re-schedule Commissioning</a></li>
										<li><a href="#">Add more endpoints</a></li>
										<li><a href="#">Set Operating Mode</a></li>
										<li><a href="#">View Network Map</a></li>
										<li><a href="#">Check DC Connectivity</a></li>
										<li><a href="">Add/Edit Installation</a></li>
										<li><a href="">Add/Edit Site</a></li>
										<li><a href="">Assign/Configure DC</a></li>
										<li><a href="">Add Endpoints</a></li>
										<li><a href="">Add Repeaters</a></li>
									</ul>
								</div>
							</div>

							<div class="border-box">
								<div class="box-body">
									<div class="grid-content">
										<table class="grid">
											<tr>
												<th>Installer ID</th>
												<th>Number of Endpoints</th>
												<th>Street Assigned</th>
											</tr>
											<tr data-ng-repeat=" (key,value) in angularMap.data "
												data-ng-show="getNumberOfEndPoints(value) > 0">
												<td>{{key}}</td>
												<td>{{getNumberOfEndPoints(value)}}</td>
												<td>{{getAssignedStreetNames(value)}}</td>
											</tr>

										</table>

									</div>
								</div>
							</div>

							<!-- ================================================= -->
						</div>
					</div>

				</div>
				<!--Content end-->

				<div class="boxpanel" style="height: 100px">
					<div class="grid-content" style="overflow: scroll; height: 400px">
						<span>Total Number of Endpoints to be installed
							:{{totalNoOfEndpointsToBeInstalled}}</span><br> <span>Total
							Number of Endpoints to be Assigned :{{assignedEndpoints}}</span><br>

						<div class="form-group">
							<label class="control-label">&nbsp;</label>
							<button class="default ic_reset"
								data-ng-disabled=" checkForGenerateInsDisable() assignedEndpoints != totalNoOfEndpointsToBeInstalled || !fileNameList"
								data-ng-click="insFilesGenerated()">Generate
								Installation Files</button>
						</div>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="fileNameList || !checkForTag()">
					<span> Select File To View:<select class="form-control"
						data-ng-model="fileName" data-ng-init="recordSize = 10"
						data-ng-options="x for x in fileNameList"
						data-ng-change="updateFile()">
					</select>
					</span>
				</div>

				<div class="boxpanel" data-ng-show="fileName">
					<div class="box-body">
						<div class="grid-content" style="overflow: scroll; height: 400px">
							<table class="grid">
								<tr>
									<th>Consumer Account No</th>
									<th>Street Name</th>
									<th>Address2</th>
									<th>Address3</th>
									<th>Address4</th>
									<th>Zipcode</th>
									<th>District Meter ID</th>
									<th>Last Meter Reading</th>
									<th>Reading Timestamp</th>
									<th>K-Value</th>
									<th>direction</th>
									<th>Utility Code</th>
									<th>Usage Threshold</th>
									<th>Usage Interval</th>
									<th>Left Billing Digit</th>
									<th>Right Billing Digit</th>
									<th>Decimal Position</th>
									<th>Leakage Threshold</th>
									<th>Backflow Limit</th>
								</tr>
								<tr data-ng-repeat="x in insData track by $index">
									<td data-ng-repeat="y in x track by $index"><label
										data-ng-bind="x[$index]"></label></td>
								</tr>

							</table>
							<!-- <div class="grid-bottom">
								<div class="show-record col-sm-3">
									<span class="records">Records:</span> <select
										class="form-control" data-ng-model="recordSize"
										data-ng-change="changedValue(recordSize,customerList)"
										data-ng-options="x for x in paginationList ">
									</select>
								</div>
								<div class="paging">
									<ul data-ng-if="customerList">
										<li data-ng-repeat="i in getNumber(totalPage) track by $index"><a
											href="" data-ng-click="fetchRecord($index+1)">{{$index+1}}</a></li>
									</ul>
								</div>
							</div> -->
						</div>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="fileName">
					<button class="default ic_reset" data-ng-click="assignToIns()">Assign
						To Installers !</button>
					<button class="default ic_reset" data-ng-click="discardChanges()">Discard
						Changes !</button>
				</div>

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
	</div>
</body>
</html>





