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
<script>
	function refreshPage() {
		window.location.reload();
	}
</script>
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
		data-ng-init="initStartContinueinstallation(${selectedSiteId})">
		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>
		<div id="content">
			<!--Content Start-->
			<h1>Endpoint Planning</h1>

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
				<div class="filter_panel"  style="background-color: white;">
					<span class="heading"><h4>Select Commissioning Type</h4></span>
					<div class="form-group">
						<label class="checkbox"><input type="radio"
							name="Scheduled" value="option1"
							data-ng-click="setflag('scheduled')" data-ng-disabled="tag>2"><span></span>Scheduled</label>
						<label class="checkbox"><input type="radio"
							name="Scheduled" value="option2"
							data-ng-click="setflag('manual')" data-ng-disabled="tag>2"><span></span>Manual</label>
					</div>

					<div class="form-group">
						<input type="text" class="form-control calendar-input"
							style="margin-top: 10px;"
							data-uib-datepicker-popup="{{format}}"
							placeholder="-- Select Date Here --"
							data-ng-model="scheduledDate" data-is-open="popup1.opened"
							data-datepicker-options="" onkeypress="return false"
							data-close-text="Close" data-ng-click="open1()"
							data-alt-input-formats="altInputFormats"
							data-ng-disabled="!scheduledFlag || tag>2"
							data-ng-change="setflag('scheduled')" required>
					</div>
					<div class="table-hint">
						<span data-ng-if="tag > 1" class="enable">1</span>
						<span data-ng-if="tag == 1"  class="disable">1</span>
					</div>
				</div>
				<br> 
				<div class="filter_panel uploadFile-box"  style="background-color: white;">
				<span class="heading"><h4>Select Route File</h4></span><br>
				<span data-ng-if="tag > 2" class="enable">2</span> <span
						data-ng-if="tag <= 2" class="disable">2</span>
					<div class="form-group">
						<!-- <label class="lbl"> 
					<input type="checkbox" style="outline: 1px solid #1e5180" data-ng-model="isHeader"><span></span>Header Included?
						&nbsp;&nbsp;&nbsp;
					</label> <label class="control-label">&nbsp;</label>  -->
						<input id="browseBtn" type="file" data-file-model="routeFile"
							class="formfield" accept=".csv" style="width: 400px"
							data-ng-disabled="tag != 2" data-ng-click="onBrowseClick()" />
					</div>
					<button class="primary ic_upload" type="button"
						data-ng-click="uploadRouteFile()" data-ng-disabled="tag>2">Upload
						Route File</button>

					


				</div>
				<div class="loading-spiner-holder" data-loading style="text-align: center;">
					<div class="loading-spiner">
						 <img src="images/loading.gif" /> 
					</div>
				</div>
				<br>
				<div class="boxpanel" data-ng-if="displayUploadedRouteFile">
					<span class="heading">Route File :&nbsp;&nbsp;
						<button data-ng-click="editSelectedLines()" class="primary ic_submit btn btn-default"
							data-ng-disabled="tag>2">Update Endpoint Parameters</button>&nbsp;&nbsp;
						<button data-ng-click="addLine()" data-ng-disabled="tag>2">Add
							New Line</button>&nbsp;&nbsp;
						<button data-ng-click="removeLine()" data-ng-disabled="tag>2">Remove
							Line</button>
					</span>
					<div class="box-body">
						<div class="grid-content" style="overflow: scroll; height: 400px">
							<table class="grid">
								<tr>
									<th><span><button data-ng-click="selectAllFun()"
												class="primary ic_submit">Select All</button> </span></th>
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
									<th data-ng-if="scheduledDate">Scheduled Date</th>
								</tr>

								<tr
									data-ng-repeat="x in routeFiledata.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) track by $index">
									<td><input type="checkbox" data-ng-model="default.check"
										data-ng-click="pushOrPop($index)" data-ng-checked="selectAll"><span></span></td>
									<td data-ng-repeat="y in x track by $index"><input
										style="border: 0px solid; background: transparent;"
										placeholder="Enter Value" type="text"
										data-ng-model="x[$index]" data-ng-disabled="$index &lt; 9"></td>
								</tr>
							</table>
						</div>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button class="primary ic_save" type="button"
							data-ng-click="generateMasterFile()" data-ng-disabled="tag>2">Save
							File</button>
						<span>&nbsp;&nbsp;Total End Points : {{routeFilelength}}</span>
						<span data-ng-if="updatedDateTime">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Last updated : {{updatedDateTime}}</span>
						<span data-ng-if="updatedBy">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Last updated by : {{updatedBy}}</span>
						<div class="grid-bottom">
							<div class="show-record col-sm-3">
								<span class="records">Records:</span> <select
									data-ng-model="recordSize"
									data-ng-change="setItemsPerPage(recordSize)"
									data-ng-options="x for x in paginationList"></select>
							</div>
							<div class="paging">
								<ul uib-pagination data-previous-text="&lt;&lt;"
									data-next-text="&gt;&gt;" data-total-items="totalItems"
									data-ng-model="currentPage" data-max-size="maxSize"
									data-boundary-links="true" data-ng-change="pageChanged()"
									class="pagination-sm" data-items-per-page="itemsPerPage"></ul>
							</div>
						</div>
					</div>
				</div>
				<div class="boxpanel" data-ng-if="tag >= 3">
					<br>
					<span style="margin-right: 10px;" data-ng-if="tag > 3" class="enable">3</span>
					<span style="margin-right: 10px;" data-ng-if="tag <=3" class="disable">3</span>
					<span class="heading">Assign Installers :</span>
					<!-- New Content Start -->
					<div class="boxpanel">


						<div class="installed-datacollectors">
							<div class="installed-box" style="width: 50%;">
								<!-- <div class="title-hed">
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
							</div> -->

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
														data-ng-disabled="checkForTag(); " style="width: 100%"></select></td>
												</tr>

											</table>

										</div>
									</div>
								</div>

							</div>
							<!-- ================================================= -->

							<div class="installed-box" style="width: 50%;">
								<!-- <div class="title-hed">
								<span class="heading">Spare Data Collectors :
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
							</div> -->

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

					<div class="boxpanel" style="height: 50px">
						<div class="grid-content">


							<div class="form-group">
								<label class="control-label">&nbsp;</label>
								&nbsp;&nbsp;&nbsp;&nbsp;
								<button class="default ic_reset"
									data-ng-disabled="checkForGenerateInsDisable()"
									data-ng-click="insFilesGenerated()">Generate
									Installation Files</button>

								<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total Number
										of Endpoints to be installed :
										{{totalNoOfEndpointsToBeInstalled}}</span> <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										Total Number of Endpoints to be Assigned :
										{{assignedEndpoints}}</span>
							</div>
						</div>
					</div>
				</div>
				<div>
					<div class="boxpanel" data-ng-show="checkForEnable()">
						<br> <span style="margin-right: 10px;" data-ng-if="tag===4"
							class="disable">4</span>
						<table>
							<tr>
								<td
									style="width: 10%; text-align: right; vertical-align: middle;">Select
									File To View :</td>
								<td style="width: 1%"></td>
								<td><select class="form-control" data-ng-model="fileName"
									data-ng-init="recordSize = 10"
									data-ng-options="x for x in fileNameList"
									data-ng-change="updateFile()" style="width: 30%;">
								</select></td>
							</tr>
						</table>
						<br>

					</div>

					<div class="boxpanel" data-ng-show="fileName && tag >= 4">
						<div class="box-body">
							<div class="grid-content" style="overflow: scroll; height: 400px">
								<table class="grid">
									<!-- <tr>
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
									</tr> -->
									<tr data-ng-repeat="x in insData track by $index" data-ng-if="$index==0">
										<th data-ng-repeat="y in x track by $index"><label
											data-ng-bind="x[$index]"></label></th>
									</tr>
									<tr data-ng-repeat="x in insData track by $index" data-ng-if="$index!=0">
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

					<div style="text-align: center;" class="boxpanel" data-ng-show="fileName && tag >= 4"><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<button class="primary ic_add" data-ng-click="assignToIns()">Assign
							To Installers !</button>&nbsp;&nbsp;
						&nbsp;&nbsp;
						<button class="default ic_reset" data-ng-click="discardChanges()">Discard
							Changes !</button><br><br>
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