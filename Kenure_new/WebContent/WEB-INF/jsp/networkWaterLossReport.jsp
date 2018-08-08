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
<link
	href="http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">
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
<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/reportscontroller.js"></script>
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
<script type="text/javascript">
	angular.module('myApp').requires.push('ui.bootstrap');
</script>
</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="reportscontroller">

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>Analytics&nbsp;&nbsp;>>&nbsp;&nbsp;Network Water Loss Report
			</h1>
			<div class="middle">
				<div class="boxpanel">
					<!-- <span class="heading">Search</span> -->
					<div class="box-body">
						<form>
							<div class="filter_panel">
								<div class="form-group auto">
									<label class="control-label"><h4>District Meter
											Reading Period</h4></label> <label class="control-label"><h5>Start</h5></label>
									<input type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="-- Click here to Select Reading Start Date --"
										data-ng-model="startDate" data-is-open="popup1.opened"
										data-datepicker-options="" onkeypress="return false"
										data-close-text="Close" data-ng-click="open1()"
										data-alt-input-formats="altInputFormats" autofocus="autofocus" required>
								</div>

								<div class="form-group" style="margin-top: 29px;">
									<label class="control-label"><h5>Finish</h5></label> <input
										type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="-- Click here to Select Reading End Date --"
										data-ng-model="endDate" data-is-open="popup2.opened"
										data-datepicker-options="" onkeypress="return false"
										data-close-text="Close" data-ng-click="open2()"
										data-alt-input-formats="altInputFormats" required>
								</div>

								<div class="form-group auto">
									<label class="control-label"><h4>Acceptable Loss</h4></label> <input
										type="number" class="form-control" min="1"
										data-ng-model="acceptableLoss" data-ng-trim="false"
										style="margin-top: 30px;">

								</div>
								<div class="form-group auto">
									<h5 style="margin-top: 65px;">Unit</h5>
								</div>
								<div class="form-group auto" style="margin-top: 25px;">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_search"
										data-ng-click="searchNetworkWaterLoss()">Search</button>
									<button type="reset"
										data-ng-click="refreshNetworkWaterLossPage()"
										class="default ic_reset">Reset</button>
								</div>
							</div>
						</form>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="noNetworkWaterLossFound">
					<span class="heading">Search Listing</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<td>
										<h3>No Network Water Loss Found</h3>
									</td>
								</tr>
							</table>

						</div>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="networkWaterLossDiv">

					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<th><a style="color: white"
										data-ng-click="orderByField='districtMeterSerialNo'; reverseSort = !reverseSort">
											District Meter Serial Number<span
											data-ng-show="orderByField == 'districtMeterSerialNo'">
												<span data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>

									<th><a style="color: white"
										data-ng-click="orderByField='districtMeterReading'; reverseSort = !reverseSort">
											District Meter Reading<span
											data-ng-show="orderByField == 'districtMeterReading'">
												<span data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>

									<th><a style="color: white"
										data-ng-click="orderByField='billingStartDate'; reverseSort = !reverseSort">
											Billing Start Date<span
											data-ng-show="orderByField == 'billingStartDate'"> <span
												data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>

									<th><a style="color: white"
										data-ng-click="orderByField='billingEndDate'; reverseSort = !reverseSort">
											Billing End Date<span
											data-ng-show="orderByField == 'billingEndDate'"> <span
												data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>

									<th><a style="color: white"
										data-ng-click="orderByField='totalConsumerConsumption'; reverseSort = !reverseSort">
											Total Consumer Consumption<span
											data-ng-show="orderByField == 'totalConsumerConsumption'">
												<span data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>

									<th><a style="color: white"
										data-ng-click="orderByField='waterLoss'; reverseSort = !reverseSort">
											Water Loss<span data-ng-show="orderByField == 'waterLoss'">
												<span data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>

									<th><a style="color: white"
										data-ng-click="orderByField='percentage'; reverseSort = !reverseSort">
											% <span data-ng-show="orderByField == 'percentage'"> <span
												data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>

								</tr>

								<tr
									data-ng-repeat="x in networkWaterLossList.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort">
									<!-- <td><button data-ng-click="openPopupForAlertActionDC(x.dcSerialNo)" class="btn btn-link">{{x.dcSerialNo}}</button></td> -->
									<td>{{x.districtMeterSerialNo}}</td>
									<td>{{x.districtMeterReading}}</td>
									<td>{{x.billingStartDate}}</td>
									<td>{{x.billingEndDate}}</td>
									<td>{{x.totalConsumerConsumption}}</td>
									<td data-ng-if="x.percentage != null" style="color: red;">{{x.waterLoss}}</td>
									<td data-ng-if="x.percentage == null">{{x.waterLoss}}</td>
									<td data-ng-if="x.percentage != null" style="color: red;">{{x.percentage}}</td>
									<td data-ng-if="x.percentage == null">{{x.percentage}}</td>
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
										data-ng-click="exportToCsvForWaterLoss()">Export
										to CSV file</button>
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
