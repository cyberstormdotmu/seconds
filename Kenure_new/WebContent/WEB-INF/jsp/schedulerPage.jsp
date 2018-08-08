<!DOCTYPE>
<!--  -->
<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<!-- <link href="css/style.css" rel="stylesheet" type="text/css" /> -->
<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/toastr.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/sweetalert.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->
<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/sweetalert.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/popupcontroller.js"></script>
<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
</script>
</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="popupcontroller"
		data-ng-init="initSiteDataForScheduler()">

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include data-src="'customerleftNavigationbar'"></div>
	
		<div id="content">
			<!--Content Start-->
			<h1>
				Control&nbsp;&nbsp;>>&nbsp;&nbsp;Scheduler Management
			</h1>
			<div class="middle">
			<div class="status error" data-ng-show="nosuchSiteFoundDiv">
						<span>No such Records Found!</span>
					</div>
				<div class="boxpanel">
					<div class="box-body">
						<form>
							<div class="filter_panel">
								<div class="form-group">
									<label class="control-label"><h5>Site ID</h5></label> <input
										type="number" class="form-control" data-ng-model="sitIdSearch" autofocus="autofocus" min="0">
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Installation Name</h5></label> <input
										type="text" class="form-control" data-ng-model="regionNameSearch">
								</div>
								<div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_search" data-ng-click="searchSite()">Search</button>
									<button type="reset" data-ng-click="refreshPage()"
										class="default ic_reset">Reset</button>
								</div>
								<div  class="pull-right">
									<label class="control-label">Select Default Meter Reading Interval for all Sites</label>
									<button class="primary" data-ng-click="setDefaultMRIForAllSites()">Default</button>
								</div>
							</div>
						</form>
						
					</div>
				</div>
				<div class="boxpanel">
					<div class="box-body">
						<div class="grid-content" data-ng-show = "siteListDiv">
						<div class="status success" data-ng-show="successdiv">
							<span>{{successmsg}}</span>
						</div>
							<table class="grid">
								<tr>
									<th><a style="color: white"
										data-ng-click="orderByField='siteId'; reverseSort = !reverseSort">
											Site ID <span
											data-ng-show="orderByField == 'siteId'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='regionName'; reverseSort = !reverseSort">
											Installation Name <span
											data-ng-show="orderByField == 'regionName'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<!-- <th><a style="color: white"
										data-ng-click="orderByField='NRI'; reverseSort = !reverseSort">
											Network Reading Interval <span
											data-ng-show="orderByField == 'NRI'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th> -->
									<th><a style="color: white"
										data-ng-click="orderByField='MRI'; reverseSort = !reverseSort">
											Meter Reading Interval <span
											data-ng-show="orderByField == 'MRI'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="">
										<span><button data-ng-click="selectAllFun()" class="primary ic_submit">Select All</button>
										</span>
									</a></th>
								</tr>

								<tr data-ng-repeat="x in siteList.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort">
									<td><button data-ng-click="open(x.siteId)" class="btn btn-link">{{x.siteId}}</button></td>
									<td>{{x.regionName}}</td>
									<!-- <td>{{x.NRI}}</td> -->
									<td>{{x.MRI}}</td>
									<td><input type="checkbox" data-ng-model="default.check"
											data-ng-change="pushOrPop(x.siteId)" data-ng-checked="selectAll"><span></span></td>
								</tr>

							</table>
							<div style='display: none'>
							<div style='display: none'>
							<div id='inline_content' style='padding: 10px; background: #fff;'>
								<br></br>
								<h3>Enter Your Email Address Here!!</h3>
							</div>
						</div>
						</div>
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

			</div>
			<!--Content end-->
		</div>
		<div id="footer" data-ng-include data-src="'../footer'"></div>
	</div>
	<!--wrapper end-->


</body>
</html>
