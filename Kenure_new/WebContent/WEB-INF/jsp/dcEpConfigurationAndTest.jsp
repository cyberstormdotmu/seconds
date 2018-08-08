<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/sweetalert.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->


<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/easy-tabs.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/sweetalert.min.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>

<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>

<script src="${pageContext.request.contextPath}/app/controller/commissioningcontroller.js"></script>


<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
</script>
</head>

<body data-ng-app="myApp">
	<div id="wrapper" data-ng-controller="commissioningcontroller"
		data-ng-init="initdcEpConfigTest(${selectedSiteId})">


		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>
		<div id="content">
			<!--Content Start-->
			<h1>Current Status</h1>
			<div class="middle">
				<span class="pull-right"><a href="" data-ng-click="redirectToCommissioningPage()">commissioning</a></span>
				<div class="filter_panel" style="background-color: white;">
					<ul class="customer-details">
						<li><span>Customer Code <i>:</i></span>{{custCode}}</li>
						<li><span>Installation <i>:</i></span>{{regionName}}</li>
						<li><span>Site Name <i>:</i></span>{{siteName}}</li>
						<li><span>Site ID <i>:</i></span>{{siteID}}</li>
						<li><span>Status <i>:</i></span>{{status}}</li>
					</ul>
					<ul class="customer-details">
						<li><span>Number of DataCollectors <i>:</i></span>{{totalDC}}</li>
						<li><span>Number of End Points <i>:</i></span>0</li>
						<li><span>Number of Repeaters <i>:</i></span>0</li>
					</ul>
				</div>
				<br>


				<div class="boxpanel">
					<span class="heading">Data Collector Installation <span
						style="margin-right: 10px;" data-ng-if="tag > 7" class="enable">7</span>
					<span style="margin-right: 10px;" data-ng-if="tag ==7"
						class="disable">7</span></span>

					<div class="boxpanel">
						<div class="installed-datacollectors">
							<div class="installed-box" style="width: 100%">
								<div class="border-box">
									<div class="box-body">
										<div class="grid-content">
											<table class="grid">
												<tr>
													<th><a style="color: white"
														data-ng-click="orderByField1='fileName'; reverseSort1 = !reverseSort1">
															File Name <span
															data-ng-show="orderByField1 == 'fileName'"> <span
																data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>
														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='installerName'; reverseSort1 = !reverseSort1">
															Installer Name <span
															data-ng-show="orderByField1 == 'installerName'"> <span
																data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='noOfEndPoints'; reverseSort1 = !reverseSort1">
															Number of DataCollectors <span
															data-ng-show="orderByField1 == 'noOfEndPoints'"> <span
																data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='isFileUploaded'; reverseSort1 = !reverseSort1">
															File Uploaded <span
															data-ng-show="orderByField1 == 'isFileUploaded'">
																<span data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='isFileVerified'; reverseSort1 = !reverseSort1">
															File Verified <span
															data-ng-show="orderByField1 == 'isFileVerified'">
																<span data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
												</tr>
												<tr
													data-ng-repeat="x in dcFileList | orderBy:orderByField1:reverseSort1"
													data-ng-if="x.noOfDatacollectors > -1">
													<td>{{x.fileName}}</td>
													<td>{{x.installerName}}</td>
													<td>{{x.noOfDatacollectors}}</td>
													<td>{{x.isFileUploaded}}</td>
													<td>{{x.isFileVerified}}</td>
												</tr>
											</table>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="boxpanel">
					<span class="heading">Online DataCollector Configuration
						and Test &nbsp;&nbsp;<button class="primary ic_reset default" data-ng-disabled="diableCheckConnectionBtn" data-ng-click="configureAndTestDataCollectors()">Click here to Check DC Connections!</button>
					{{waitForConfigurationMsg}}
					<span style="margin-right: 10px;" data-ng-if="tag > 8" class="enable">8</span> <span style="margin-right: 10px;" data-ng-if="tag <=8 && tag >=7" class="disable">8</span>	
					</span>
					<div class="boxpanel">
						<div class="installed-datacollectors">
							<div class="installed-box" style="width: 100%">
								<div class="border-box">
									<div class="box-body">
										<div class="grid-content">
											<table class="grid">
												<tr>
													<th><a style="color: white"
														data-ng-click="orderByField1='dcSerialNumber'; reverseSort1 = !reverseSort1">
															DC Serial Number <span
															data-ng-show="orderByField1 == 'dcSerialNumber'"> <span
																data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='installerName'; reverseSort1 = !reverseSort1">
															Installer Name <span
															data-ng-show="orderByField1 == 'installerName'"> <span
																data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='dcIp'; reverseSort1 = !reverseSort1">
															IP Address <span
															data-ng-show="orderByField1 == 'dcIp'"> <span
																data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='batteryVoltage'; reverseSort1 = !reverseSort1">
															Battery Voltage <span
															data-ng-show="orderByField1 == 'batteryVoltage'">
																<span data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='isConnectionOk'; reverseSort1 = !reverseSort1">
															Connection OK ? <span
															data-ng-show="orderByField1 == 'isConnectionOk'">
																<span data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='isConfigOk'; reverseSort1 = !reverseSort1">
															Configuration OK? <span
															data-ng-show="orderByField1 == 'isConfigOk'">
																<span data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
												</tr>
												<tr data-ng-repeat="x in datacollectorsList">
													<td>{{x.dcSerialNumber}}</td>
													<td>{{x.installerName}}</td>
													<td>{{x.dcIp}}</td>
													<td>{{x.batteryVoltage}}</td>
													<td>{{x.isConnectionOk}}</td>
													<td>{{x.isConfigOk}}</td>
												</tr>
											</table>

										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="boxpanel">
					<span class="heading">End Points Installation 
					<span style="margin-right: 10px;" data-ng-if="tag > 9" class="enable">9</span>
					<span style="margin-right: 10px;" data-ng-if="tag <=9 && tag>=7" class="disable">9</span>
					</span> 
					<div class="boxpanel">
						<div class="installed-datacollectors">
							<div class="installed-box" style="width: 100%">
								<div class="border-box">
									<div class="box-body">
										<div class="grid-content">
											<table class="grid">
												<tr>
													<th><a style="color: white"
														data-ng-click="orderByField1='fileName'; reverseSort1 = !reverseSort1">
															File Name <span
															data-ng-show="orderByField1 == 'fileName'"> <span
																data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='installerName'; reverseSort1 = !reverseSort1">
															Installer Name <span
															data-ng-show="orderByField1 == 'installerName'"> <span
																data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='noOfEndPoints'; reverseSort1 = !reverseSort1">
															Number of End Points <span
															data-ng-show="orderByField1 == 'noOfEndPoints'"> <span
																data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='isFileUploaded'; reverseSort1 = !reverseSort1">
															File Uploaded <span
															data-ng-show="orderByField1 == 'isFileUploaded'">
																<span data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='isFileVerified'; reverseSort1 = !reverseSort1">
															Setting OK ? <span
															data-ng-show="orderByField1 == 'isFileVerified'">
																<span data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
												</tr>
												<tr
													data-ng-repeat="x in dcFileList | orderBy:orderByField1:reverseSort1"
													data-ng-if="x.noOfEndPoints > -1">
													<td>{{x.fileName}}</td>
													<td>{{x.installerName}}</td>
													<td>{{x.noOfEndPoints}}</td>
													<td>{{x.isFileUploaded}}</td>
													<td>{{x.isFileVerified}}</td>
												</tr>
											</table>

										</div>
									</div>
								</div>

							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
		<!--Content end-->

		<div id="footer">
			<!--footer start-->
			<p class="copyright">
				&copy; 2016 Blu Tower.&nbsp;&nbsp;<a href="#">Privacy</a><span>|</span><a
					href="#">Terms</a>
			</p>
			<!--footer end-->
		</div>
		<!--wrapper end-->
	</div>
</body>
</html>
