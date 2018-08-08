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
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/toastr.css" rel="stylesheet" type="text/css" />
<link
	href="http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">
<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/controller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/usercontroller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/customercontroller.js"></script>
<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
</script>
<script type="text/javascript">
	angular.module('myApp').requires.push('ui.bootstrap');
</script>
</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="customercontroller"
		data-ng-init="getDcDetailsforCustomer()" >

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include data-src="'customerleftNavigationbar'"></div>
	
		<div id="content">
			<!--Content Start-->
			<h1>
				Control&nbsp;&nbsp;>>&nbsp;&nbsp;DataCollector Management
			</h1>
			<div class="middle">
				<div class="boxpanel">
					<span class="heading">Search</span>
					<div class="box-body">
						<form>
							<div class="filter_panel">
								<!-- <div class="form-group auto">
									<label class="control-label">Data plan</label>
									<input type="text" id="from" data-ng-model="activedate" class="form-control calendar-input half marginright" jqdatepicker>
									<input type="text" id="to" data-ng-model="expirydate" class="form-control calendar-input half">
								</div> -->
								<div class="form-group">
									<label class="control-label"><h5>Serial Number</h5></label> <input
										type="text" class="form-control" data-ng-model="dcSerialNumber" autofocus="autofocus">
								</div>
								<div class="form-group">
									<label class="control-label"><h5>IP Address</h5></label> <input
										type="text" class="form-control" data-ng-model="dcIp">
								</div>
								<div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_search" data-ng-click="searchDataCollectorByCustomer()">Search</button>
									<button type="reset" data-ng-click="refPage()" class="default ic_reset">Reset</button>
								</div>
							</div>
						</form>
					</div>
				</div>

				 <div class="boxpanel" data-ng-show="noDataCollectorFoundDiv">
					<span class="heading">Search Listing</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<td>
										<h3>No DataCollector Found</h3>
									</td>
								</tr>
							</table>

						</div>
					</div>
				</div> 

				<div class="boxpanel" data-ng-show="dataCollectorListDiv">
					<span class="heading">DataCollector List </span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid" >
								<tr>
								<th><a style="color: white"
										data-ng-click="orderByField='dcSerialNumber'; reverseSort = !reverseSort">
											Serial Number <span
											data-ng-show="orderByField == 'dcSerialNumber'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
								</a></th>
								
								<th><a style="color: white"
										data-ng-click="orderByField='site'; reverseSort = !reverseSort">
											Site Id <span
											data-ng-show="orderByField == 'site'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									
								<th><a style="color: white"
										data-ng-click="orderByField='dcIp'; reverseSort = !reverseSort">
											IP Address <span
											data-ng-show="orderByField == 'dcIp'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									
									<th><a style="color: white"
										data-ng-click="orderByField='totalEndpoints'; reverseSort = !reverseSort">
											No. of End points <span
											data-ng-show="orderByField == 'totalEndpoints'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
								
									<th><a style="color: white"
										data-ng-click="orderByField='iscommissioned'; reverseSort = !reverseSort">
											Is Connected <span
											data-ng-show="orderByField == 'iscommissioned'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									
									<th><a style="color: white"
										data-ng-click="orderByField='latitude'; reverseSort = !reverseSort">
											Latitude <span
											data-ng-show="orderByField == 'latitude'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									
									<th><a style="color: white"
										data-ng-click="orderByField='longitude'; reverseSort = !reverseSort">
											Longitude <span	data-ng-show="orderByField == 'longitude'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									
									
							    <th>Action</th>
							     
							</tr>

								<tr data-ng-repeat="x in dataCollector.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort">
									<td>{{x.dcSerialNumber}}</td>
									<td>{{x.site}}</td>
									<td>{{x.dcIp}}</td>
									<td>{{x.totalEndpoints}}</td>
										<td data-ng-if="x.iscommissioned == true">Yes</td>
										<td data-ng-if="x.iscommissioned == false">No</td>
									<td>{{x.latitude}}</td>
									<td>{{x.longitude}}</td>
									<td class="action"><a href="" title="edit"> <img alt="" src="${pageContext.request.contextPath}/images/edit-ic.png"
											data-ng-click="editDataCollectorForCustomer(x.datacollectorId)"></a>
									</td> 
									

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

			</div>
			<!--Content end-->
		</div>


		<div id="footer" data-ng-include data-src="'../footer'"></div>
	</div>
	<!--wrapper end-->


</body>
</html>
