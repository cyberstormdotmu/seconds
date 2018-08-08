<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
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
<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/maintenancecontroller.js"></script>
<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
</script>
<script type="text/javascript">
	angular.module('myApp').requires.push('ui.bootstrap');
</script>
</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="maintenancecontroller">

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include data-src="'customerleftNavigationbar'"></div>
	
		<div id="content">
			<!--Content Start-->
			<h1>
				Maintenance&nbsp;&nbsp;>>&nbsp;&nbsp;Asset Inspection Schedule
			</h1>
			<div class="middle">
				<div class="boxpanel">
					<span class="heading">Search</span>
					<div class="box-body">
						<form>
							<div class="filter_panel">
								
								<div class="form-group">
									<label class="control-label"><h5>Select Asset</h5></label> <select
												class="form-control " data-ng-model="selectedAsset" required autofocus="autofocus">
												<option value="">---Please Select Asset---</option>
												<option value="Endpoints">Endpoints</option>
												<option value="DataCollectors">DataCollectors</option></select>
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Select Inspection Interval</h5></label> <select
												class="form-control " data-ng-model="selectedInspectionInterval" required>
												<option value="">---Please Select Interval---</option>
												<option value="12 Months">12 Months</option>
												<option value="18 Months">18 Months</option>
												<option value="24 Months">24 Months</option></select>
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Report By</h5></label> <select
												class="form-control " data-ng-model="selectedReport" required>
												<option value="">---Please Select Report---</option>
												<option value="Week">Week</option>
												<option value="Month">Month</option></select>
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Show Inspections Due Within</h5></label> <input
										type="number" class="form-control" data-ng-model="inspectionDueWithin" min="0" required>
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Site Id</h5></label> <input
										type="text" class="form-control" data-ng-model="siteId">
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Site Name</h5></label> <input
										type="text" class="form-control" data-ng-model="siteName" data-ng-trim="false">
								</div>
								
								<div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_search" data-ng-click="searchAssetInspectionSchedule()">Search</button>
									<button type="reset" data-ng-click="refreshAssetInspectionPage()"
										class="default ic_reset">Reset</button>
								</div>
							</div>
						</form>
					</div>
				</div>

				 <div class="boxpanel" data-ng-show="noAssetInspectionScheduleFound">
					<span class="heading">Search Listing</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<td>
										<h3>No Asset Inspection Schedule Found</h3>
									</td>
								</tr>
							</table>

						</div>
					</div>
				</div> 

				<div class="boxpanel" data-ng-show="assetInspectionScheduleDiv">
					<!-- <span class="heading">Data Collector List </span> -->
					<div class="box-body">
						<div class="grid-content">
							<table class="grid" >
								<tr>
								<th><a style="color: white"
										data-ng-click="orderByField='assetSerialNumber'; reverseSort = !reverseSort">
											Asset Serial Number <span
											data-ng-show="orderByField == 'assetSerialNumber'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
								</a></th>
								
								<th><a style="color: white"
										data-ng-click="orderByField='assetInspectionDate'; reverseSort = !reverseSort">
											Next Inspection Date <span
											data-ng-show="orderByField == 'assetInspectionDate'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									
								<th><a style="color: white"
										data-ng-click="orderByField='streetName'; reverseSort = !reverseSort">
											Address 1 <span
											data-ng-show="orderByField == 'streetName'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									
									<th><a style="color: white"
										data-ng-click="orderByField='address1'; reverseSort = !reverseSort">
											Address 2 <span
											data-ng-show="orderByField == 'address1'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
								
									<th><a style="color: white"
										data-ng-click="orderByField='address2'; reverseSort = !reverseSort">
											Address 3 <span
											data-ng-show="orderByField == 'address2'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									
									<th><a style="color: white"
										data-ng-click="orderByField='address3'; reverseSort = !reverseSort">
											Address 4 <span
											data-ng-show="orderByField == 'address3'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									
									<th><a style="color: white"
										data-ng-click="orderByField='zipcode'; reverseSort = !reverseSort">
											Zip Code <span	data-ng-show="orderByField == 'zipcode'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
																			     
							</tr>
			
	   						 <tr data-ng-repeat="x in assetInspectionScheduleList.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort">
									<td>{{x.assetSerialNumber}}</td>
									<td data-ng-if="x.changeCss" style="color:red;">{{x.assetInspectionDate}}</td>
									<td data-ng-if="!x.changeCss">{{x.assetInspectionDate}}</td>
									<td>{{x.streetName}}</td>
									<td>{{x.address1}}</td>
									<td>{{x.address2}}</td>
									<td>{{x.address3}}</td>
									<td>{{x.zipcode}}</td>
							</tr>

							</table>
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
				</div>
				<div class="boxpanel" data-ng-show="assetInspectionScheduleForDCDiv">
					<!-- <span class="heading">Data Collector List </span> -->
					<div class="box-body">
						<div class="grid-content">
							<table class="grid" >
								<tr>
								<th><a style="color: white"
										data-ng-click="orderByField='assetSerialNumber'; reverseSort = !reverseSort">
											Asset Serial Number <span
											data-ng-show="orderByField == 'assetSerialNumber'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
								</a></th>
								
								<th><a style="color: white"
										data-ng-click="orderByField='assetInspectionDate'; reverseSort = !reverseSort">
											Inspection Date <span
											data-ng-show="orderByField == 'assetInspectionDate'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									
								<th><a style="color: white"
										data-ng-click="orderByField='latitude'; reverseSort = !reverseSort">
											Latitude<span
											data-ng-show="orderByField == 'latitude'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									
									<th><a style="color: white"
										data-ng-click="orderByField='longitude'; reverseSort = !reverseSort">
											Longitude<span
											data-ng-show="orderByField == 'longitude'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
							</tr>
			
	   						 <tr data-ng-repeat="x in assetInspectionScheduleListForDC.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort">
									<td>{{x.assetSerialNumber}}</td>
									<td data-ng-if="x.changeCss" style="color:red;">{{x.assetInspectionDate}}</td>
									<td data-ng-if="!x.changeCss">{{x.assetInspectionDate}}</td>
									<td>{{x.latitude}}</td>
									<td>{{x.longitude}}</td>
							</tr>

							</table>
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
										class="pagination-sm" data-items-per-page="itemsPerPage">
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!--Content end-->
		</div>


		<div id="footer" data-ng-include src="'../footer'"></div>
	</div>
	<!--wrapper end-->


</body>
</html>
