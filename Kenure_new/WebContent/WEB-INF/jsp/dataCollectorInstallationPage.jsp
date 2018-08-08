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
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/toastr.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/sweetalert.css" rel="stylesheet" type="text/css" />
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
<script src="${pageContext.request.contextPath}/js/sweetalert.min.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>
<script src="${pageContext.request.contextPath}/js/easy-tabs.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/commissioningcontroller.js"></script>
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
<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
</script>
</head>
<body data-ng-app="myApp">
	<div id="wrapper" data-ng-controller="commissioningcontroller"
		data-ng-init="initDcLocaionInstallation(${selectedSiteId})">
		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>
		<div id="content">
			<!--Content Start-->
			<h1>DataCollector Planning</h1>
			<div class="middle">
			
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
					<span class="heading">Boundry DC Management :&nbsp;&nbsp; <!-- <button data-ng-click="addBDC()" data-ng-disabled="tag>5">Add
				       Boundry DataCollector</button>&nbsp;&nbsp;
				      <button data-ng-click="removeDcLocation()"
				       data-ng-disabled="tag>5">Remove Boundry DataCollector</button> -->
					</span>
					<div class="installed-datacollectors">
						<div class="installed-box" style="width: 50%; text-align: center;">
							<h5>Customer's All DC</h5>
							<br>
							<div class="border-box">
								<div class="box-body">
									<div class="grid-content">
										<table class="grid">
											<tr>
												<th><span><button
															data-ng-click="selectAllDcSerialNo()"
															class="primary ic_submit">Select All</button> </span></th>
												<th>DC Serial Number</th>
												<th>Network ID</th>
											</tr>
											<tr data-ng-repeat="obj in bdcList ">
												<td><input type="checkbox"
													data-ng-model="default.check"
													data-ng-click="pushOrPopDCSerialNo(obj.dcSerialNumber)"
													data-ng-checked="selectAll"><span></span></td>
												<td>{{obj.dcSerialNumber}}</td>
												<td>{{obj.networkID == 0 ? '-':obj.networkID}}</td>
											</tr>

										</table>

									</div>
								</div>
							</div>
							<br>
							<button class="primary ic_add" data-ng-click="addBDC()" data-ng-disabled="tag>7">Add Boundry DataCollector</button>
						</div>
						<div class="installed-box" style="width: 50%; text-align: center;">
							<p>
							<h5>Current Site's BDC</h5>
							</p>
							<br>
							<div class="border-box">
								<div class="box-body">
									<div class="grid-content">
										<table class="grid">
											<tr>
												<th><span><button
															data-ng-click="selectAllSiteBDcSerialNo()"
															class="primary ic_submit">Select All</button> </span></th>
												<th>DC Serial Number</th>
												<th>Network ID</th>
											</tr>
											<tr data-ng-repeat="obj in sitebdc ">
												<td><input type="checkbox"
													data-ng-model="default.check1"
													data-ng-click="pushOrPopSiteBDCSerialNo(obj.dcSerialNumber)"
													data-ng-checked="selectAllBDC"><span></span></td>
												<td>{{obj.dcSerialNumber}}</td>
												<td>{{obj.networkID == 0 ? '-':obj.networkID}}</td>
											</tr>

										</table>

									</div>
								</div>
							</div>
							<br>
							<button class="primary ic_delete" data-ng-click="removeBDC()" data-ng-disabled="tag>7">Remove Boundry DataCollector</button>
						</div>
					</div>
				</div>
				<div class="boxpanel" data-ng-if="tag === 5">
					<span style="margin-right: 10px; margin-top: 10px;" data-ng-if="tag > 5" class="enable">5</span>
					<span style="margin-right: 10px; margin-top: 10px;" data-ng-if="tag <=5" class="disable">5</span>
					<span class="heading">DataCollector Location :
						&nbsp;&nbsp;&nbsp;&nbsp;
						<button data-ng-click="addNewDclocation()"
							data-ng-disabled="tag>5">Add New Line</button>&nbsp;&nbsp;&nbsp;&nbsp;
						<button data-ng-click="removeDcLocation()"
							data-ng-disabled="tag>5">Remove Line</button>
					</span>
					<div class="boxpanel">
						<div class="installed-datacollectors">
							<div class="installed-box" style="width: 70%;">
								<div class="border-box">
									<div class="box-body">
										<div class="grid-content">
											<table class="grid">
												<tr>
													<th><span></span><a data-ng-click="selectAllFun()"
														style="color: white;">Select All</a></th>
													<th>DC Location</th>
													<th>Network Type</th>
													<th>Latitude</th>
													<th>Longitude</th>
													<th>Installer</th>
												</tr>

												<tr
													data-ng-repeat="x in dcLocations.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) track by $index">
													<td><input type="checkbox"
														data-ng-model="default.dcLocationCheck"
														data-ng-click="pushOrPopDclocation($index)"
														data-ng-checked="selectAllDcLocations"><span></span></td>
													<td data-ng-repeat="y in x track by $index"><input
														data-ng-blur="updateRightSideTable(installerSelected,$parent.$index)"
														style="border: 0px solid; background: transparent;"
														placeholder="Enter Value" type="text"
														data-ng-model="x[$index]"></td>
													<td><select data-ng-model="installerSelected"
														data-ng-change="updateRightSideTable(installerSelected,$index)"
														data-ng-options="installer as installer for installer in installerName"
														style="width: 100%"></select></td>
												</tr>
											</table>
										</div>
									</div>
								</div>

							</div>
							<!-- ================================================= -->

							<div class="installed-box" style="width: 30%;">
								<div class="border-box">
									<div class="box-body">
										<div class="grid-content">
											<table class="grid">
												<tr>
													<th>Installer Name</th>
													<th>Number of DataCollector</th>
												</tr>
												<tr data-ng-repeat=" (key,value) in angularMap.data ">
													<td>{{key}}</td>
													<td>{{value.length}}</td>
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

					<div class="boxpanel" style="height: 50px">
					
						<div class="grid-content">
							<div class="form-group">
								<label class="control-label">&nbsp;</label>
								&nbsp;&nbsp;&nbsp;&nbsp;
								<button class="default ic_reset"
									data-ng-disabled="enableDisableGenreateBtn()"
									data-ng-click="generateDcInstallationFiles()">Generate
									Installation Files</button>

								<span>&nbsp;&nbsp;Total Number of DataCollectors to be
									installed : {{totalNoOfDCsToBeInstalled}}</span> <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total
									Number of DataCollectors to be Assigned : {{assignedDCs}}</span>


							</div>
						</div>
					</div>
				</div>
				<div>
					<div class="boxpanel" data-ng-if="tag === 6">
						<br><span style="margin-right: 10px;" data-ng-if="tag===6" class="disable">6</span>
						<table>
							<tr>
								<td	style="width: 10%; text-align: right; vertical-align: middle;">Select
									File To View :</td>
								<td style="width: 1%"></td>
								<td><select class="form-control" data-ng-model="dcFileName"
									data-ng-options="x for x in fileNameList"
									data-ng-change="viewDCFilesData(dcFileName)"
									style="width: 30%;">
								</select> <!-- <select class="form-control" data-ng-model="dcFileName" data-ng-change="viewDCFilesData(dcFileName)" style="width: 30%;">
				        	<option value="" selected="selected">Select Options</option>
				        	<option data-ng-repeat="x in fileNameList">{{x}}</option>
				        </select> --></td>
							</tr>
						</table>
						<br>
					</div>
					<div class="installed-datacollectors" data-ng-show="dcData && tag === 6" style="background-color: white;">
						<div class="installed-box" style="width: 100%">
							<div class="grid-content" style="overflow: scroll; height: 350px; width: 100%; float: left">
								<table class="grid">
									<tr data-ng-repeat="x in dcData track by $index" data-ng-if="$index==0">
										<th data-ng-repeat="y in x track by $index"><label
											data-ng-bind="x[$index]"></label></th>
									</tr>
									<tr data-ng-repeat="x in dcData track by $index" data-ng-if="$index!=0">
										<td data-ng-repeat="y in x track by $index"><label
											data-ng-bind="x[$index]"></label></td>
									</tr>
								</table>
							</div>
							<p style="text-align: center; margin-top: 10px;">
									<button class="primary ic_add" data-ng-click="assignDcToInstaller()"
										data-ng-mouseenter="changeBorder('assign')"
										data-ng-mouseleave="recoverBorder('assign')">Assign
										To Installer</button>
									&nbsp;&nbsp;
									<button class="default ic_reset" data-ng-click="discardInstallationFiles()"
										data-ng-mouseenter="changeBorder('discard')"
										data-ng-mouseleave="recoverBorder('discard')">Discard
										Files</button>
								</p>
						</div>
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

</body>
</html>