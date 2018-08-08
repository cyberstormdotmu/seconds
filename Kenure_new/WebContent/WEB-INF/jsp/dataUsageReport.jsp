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
<link href="${pageContext.request.contextPath}/css/sweetalert.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<link
	href="http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->


<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/easy-tabs.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/sweetalert.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.popupoverlay.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>

<script src="${pageContext.request.contextPath}/app/controller/reportscontroller.js"></script>

<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>
<script>
	$(function() {
		$('#fadeandscale').popup();
	});
</script>

<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
</script>
<script type="text/javascript">
	angular.module('myApp').requires.push('ui.bootstrap');
</script>
</head>


<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="reportscontroller"
		data-ng-init="initDataUsageReportData()">


		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>Analytics&nbsp;&nbsp;>>&nbsp;&nbsp;Data Usage Report</h1>
			<div class="middle">
			
				<div class="boxpanel">
					<div class="box-body">
						<form>
							<div class="filter_panel">
								<div class="form-group" style="border-width: 1px">
									<label class="control-label"><h3 style="margin-top: 15px;">Current Usage : </h3></label>
								</div>
								<div class="form-group">
									<label class="control-label"><h3 style="margin-top: 15px;">{{totalUsage}} MB</h3></label>
								</div>
								<div class="form-group">
									<label class="control-label"><h3 style="margin-top: 15px;">Usage Percentage : </h3></label>
								</div>
								<div class="form-group">
										<label class="control-label"><h3 style="margin-top: 20px;">{{usagePer}} %</h3></label>
								</div>
								<div class="form-group">
									<label class="control-label"><h3 style="margin-top: 15px;">Annual Data Plan : </h3></label>
								</div>
								<div class="form-group">
										<label class="control-label"><h3 style="margin-top: 15px;">{{dataPlan}} MB</h3></label>
								</div>
							</div>
						</form>
					</div>
				</div>
				<div class="boxpanel">
					<span class="heading">Search</span>
					<div class="box-body">
						<form>
							<div class="filter_panel">
								
								<div class="form-group">
									<label class="control-label"><h5>Site ID</h5></label> <input
										type="number" class="form-control" data-ng-model="siteId" min="1" autofocus="autofocus">
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Site Name</h5></label> <input
										type="text" class="form-control" data-ng-model="siteName" data-ng-trim="false">
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Installation Name</h5></label> <input
										type="text" class="form-control" data-ng-model="installationName" data-ng-trim="false">
								</div>
								<div class="form-group">
									<label class="control-label"><h5>DC Serial Number</h5></label> <input
										type="text" class="form-control" data-ng-model="dcSerial" data-ng-trim="false">
								</div>
								<div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_search" data-ng-click="searchDataUsageReport()">Search</button>
									<button type="reset" data-ng-click="resetSearch()" class="default ic_reset">Reset</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			
			
			
				<div class="boxpanel" data-ng-show="dcDataUsageListFound">
					<div class="installed-datacollectors">
						<!-- ================================================= -->

						<div class="boxpanel" style="width: 100%">
							<div class="border-box">
								<div class="box-body">
									<div class="grid-content">
										<table class="grid">
											<tr>
												<th><a style="color: white"	data-ng-click="orderByField='dcSerialNumber'; reverseSort = !reverseSort">DC Serial Number 
													<span data-ng-show="orderByField == 'dcSerialNumber'">
														<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
														<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
													</span>
													</a>
												</th>
												<th><a style="color: white"	data-ng-click="orderByField='dcDataUsage'; reverseSort = !reverseSort">Data Usage 
													<span data-ng-show="orderByField == 'dcDataUsage'">
														<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
														<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
													</span>
													</a>
												</th>
												
											</tr>
											<tr data-ng-repeat="x in dcDataUsageList.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort">
												<td>{{x.dcSerialNumber}}</td>
												<td>{{x.dcDataUsage}}</td>
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
										<div class="grid-bottom">
											<div class="form-group auto" style="text-align: right;">
												&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<button class="default ic_reset"
												data-ng-click="downloadResultCSVfile()">Export
												to CSV file</button>
											</div>
											</div>
									</div>
								</div>
							</div>

						</div>
						
						<!-- ================================================= -->
					</div>
				</div>
				
				<div class="boxpanel" data-ng-show="noDcDataUsageListFoundDiv">
					<span class="heading">Search Listing</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<td>
										<h3>No Such Data Usage Found</h3>
									</td>
								</tr>
							</table>

						</div>
					</div>
				</div>

			</div>
			<!--Content end-->
		</div>


		<div id="footer" data-ng-include src="'../footer'"></div>
	</div>

</body>
</html>