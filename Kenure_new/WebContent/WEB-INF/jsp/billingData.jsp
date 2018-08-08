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
<link
	href="http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">
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

<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/customercontroller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/usercontroller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/popupcontroller.js"></script>

<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>

<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
</script>

</head>

<body data-ng-app="myApp">
	<div id="wrapper" data-ng-controller="customercontroller"
		data-ng-init="getBillingDataByConsumerId()">
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'../header'"></div>
		<div data-ng-if="isCustomer == true">
			<div id="customerleftNavigationbar" data-ng-include
				data-src="'customerleftNavigationbar'"></div>
		</div>
		<div id="content">
			<!--Content Start-->
			<h1>
				Control&nbsp;&nbsp;>>&nbsp;&nbsp;Endpoint Management
				<div class="rightbtns">
					<a href="customerEPManagement" class="primary ic_back">Back</a>
				</div>
			</h1>
			<div class="middle">
				<div class="boxpanel">
					<span class="heading">Billing History</span>
					<span class="heading">Register Id : {{registerId}}</span>
					<div class="box-body pad">
						<form class="" name="billingHistoryForm">
						
					<div class="boxpanel">
					<div class="box-body" data-ng-show="noDataFoundDiv">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<td>
										<h3>No Such Data Found</h3>
									</td>
								</tr>
							</table>

						</div>
					</div>
						<div class="box-body" data-ng-show="billingHistoryDiv">
						<div class="grid-content">
							<table class="grid">
								<tbody>
								<tr>
									<th><a style="color: white"
										data-ng-click="orderByField='billDate'; reverseSort = !reverseSort">
											Bill Date <span
											data-ng-show="orderByField == 'billDate'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='billingStartDate'; reverseSort = !reverseSort">
											Billing Start Date<span data-ng-show="orderByField == 'billingStartDate'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='billingEndDate'; reverseSort = !reverseSort">
											Billing End Date<span
											data-ng-show="orderByField == 'billingEndDate'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='currentReading'; reverseSort = !reverseSort">
											Current Reading <span data-ng-show="orderByField == 'currentReading'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='lastReading'; reverseSort = !reverseSort">
											Last Reading <span
											data-ng-show="orderByField == 'lastReading'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='consumedUnit'; reverseSort = !reverseSort">
											Consumed Unit <span
											data-ng-show="orderByField == 'consumedUnit'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='totalAmount'; reverseSort = !reverseSort">
											Total Amount <span
											data-ng-show="orderByField == 'totalAmount'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
								</tr>

								<tr
									data-ng-repeat="x in billingDataList.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort">
									<td>{{x.billDate}}</td>
									<td>{{x.billingStartDate}}</td>
									<td>{{x.billingEndDate}}</td>
									<td>{{x.currentReading}}</td>
									<td>{{x.lastReading}}</td>
									<td>{{x.consumedUnit}}</td>
									<td>{{x.totalAmount}}</td>
								</tr>
								</tbody>
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
							
						</form>
					</div>
				</div>

			</div>
			<!--Content end-->
		</div>

		<div id="" data-ng-include data-src="'../footer'"></div>
	</div>
	<!--wrapper end-->
</body>
</html>