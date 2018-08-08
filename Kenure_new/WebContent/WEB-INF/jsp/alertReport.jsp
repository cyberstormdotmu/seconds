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
			<h1>Analytics&nbsp;&nbsp;>>&nbsp;&nbsp;Alert Report</h1>
			<div class="middle">
			<div class="status error" data-ng-show="isError">
					<span> {{errorMessage}} </span>
				</div>
				<div class="boxpanel">
					<span class="heading">Search</span>
					<div class="box-body">
						<form>
							<div class="filter_panel">

								<div class="form-group">
									<label class="control-label"><h5>Select Alert Type</h5></label>
									<select class="form-control " data-ng-model="selectedAlertType"
										data-ng-change="showdiv()" autofocus="autofocus" required>
										<option value="">---Please Select Alert Type---</option>
										<option value="Consumer Alerts">Consumer Alerts</option>
										<option value="Network Alerts">Network Alerts</option>
										<option value="All">All</option>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Report By</h5></label> <select
										class="form-control " data-ng-model="selectedReport"
										data-ng-change="showReportPeriodDiv()">
										<option value="">---Please Select Report---</option>
										<option value="Week">Week</option>
										<option value="Month">Month</option>
										<option value="Select Dates">Select Dates</option>
									</select>
								</div>
								<div class="form-group" data-ng-show="reportPeriodDiv">
									<label class="control-label"><h5>Report Period</h5></label> <input
										type="number" class="form-control"
										data-ng-model="reportPeriod" min="0">
								</div>
								<div class="form-group" data-ng-show="startDateDiv">
									<label class="control-label"><h5>Start Date</h5></label> <input
										type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="-- Click here to Select Start Date --"
										data-ng-model="startDate" data-is-open="popup1.opened"
										data-datepicker-options="" onkeypress="return false"
										data-close-text="Close" data-ng-click="open1()"
										data-alt-input-formats="altInputFormats">
								</div>
								<div class="form-group" data-ng-show="endDateDiv">
									<label class="control-label"><h5>End Date</h5></label> <input
										type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="-- Click here to Select End Date --"
										data-ng-model="endDate" data-is-open="popup2.opened"
										data-datepicker-options="" onkeypress="return false"
										data-close-text="Close" data-ng-click="open2()"
										data-alt-input-formats="altInputFormats">
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Alerts</h5></label> <select
										class="form-control " data-ng-model="alerts">
										<option value="">---Please Select Alert---</option>
										<option value="New Alerts">New Alerts</option>
										<option value="Valid Alerts">Valid Alerts</option>
										<option value="All">All</option>
									</select>
								</div>

								<div class="form-group">
									<label class="control-label"><h5>Installation Name</h5></label>
									<input type="text" class="form-control"
										data-ng-model="installationName" data-ng-trim="false">
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Site Id</h5></label> <input
										type="text" class="form-control" data-ng-model="siteId">
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Site Name</h5></label> <input
										type="text" class="form-control" data-ng-model="siteName"
										data-ng-trim="false">
								</div>
								<div class="form-group" data-ng-show="zipcodediv">
									<label class="control-label"><h5>Zip Code</h5></label> <input
										type="text" class="form-control" data-ng-model="zipCode"
										data-ng-trim="false">
								</div>
								<div class="form-group" data-ng-show="consumerAccNodiv">
									<label class="control-label"><h5>Consumer Account
											Number</h5></label> <input type="text" class="form-control"
										data-ng-model="consumerAccNo" data-ng-trim="false">
								</div>
								<div class="form-group" data-ng-show="address1div">
									<label class="control-label"><h5>Address 1</h5></label> <input
										type="text" class="form-control" data-ng-model="address1"
										data-ng-trim="false">
								</div>
								<div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_search"
										data-ng-click="generateAlertReports()">Search</button>
									<button type="reset" data-ng-click="refreshAlertReportPage()"
										class="default ic_reset">Reset</button>
								</div>
							</div>
						</form>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="noConsumerMeterAlertsFoundDiv">
				<span class="heading">Consumer Alert Report </span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<td>
										<h3>No Consumer Meter Alerts Found</h3>
									</td>
								</tr>
							</table>

						</div>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="consumerMeterAlertDiv">
					<span class="heading">Consumer Alert Report </span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<th><a style="color: white"
										data-ng-click="orderByField='consumerAccNo'; reverseSort = !reverseSort">
											Consumer Account No<span
											data-ng-show="orderByField == 'consumerAccNo'"> <span
												data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='siteId'; reverseSort = !reverseSort">
											Site Id<span data-ng-show="orderByField == 'siteId'">
												<span data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='zipCode'; reverseSort = !reverseSort">
											Zipcode<span data-ng-show="orderByField == 'zipCode'">
												<span data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>

									<th><a style="color: white"
										data-ng-click="orderByField='alert'; reverseSort = !reverseSort">
											Alert<span data-ng-show="orderByField == 'alert'"> <span
												data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>

									<th><a style="color: white"
										data-ng-click="orderByField='dateFlagged'; reverseSort = !reverseSort">
											Date Flagged<span
											data-ng-show="orderByField == 'dateFlagged'"> <span
												data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>

									<th><a style="color: white"
										data-ng-click="orderByField='dateCleared'; reverseSort = !reverseSort">
											Date Cleared<span
											data-ng-show="orderByField == 'dateCleared'"> <span
												data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
								</tr>

								<tr
									data-ng-repeat="x in consumerMeterAlertsList.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort">
									<td>{{x.consumerAccNo}}</td>
									<td>{{x.siteId}}</td>
									<td>{{x.zipcode}}</td>
									<td>{{x.alert}}</td>
									<td>{{x.dateFlagged}}</td>
									<td></td>
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
										data-ng-click="exportToCsvForConsumerAlert()">Export
										to CSV file</button>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="noNetwrokAlertsFoundDiv">
				<span class="heading">Network Alert Report</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<td>
										<h3>No Network Alerts Found</h3>
									</td>
								</tr>
							</table>

						</div>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="networkAlertDiv">
					<span class="heading">Network Alert Report</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>

									<th><a style="color: white"
										data-ng-click="orderByField1='siteId'; reverseSortForNwAlert = !reverseSortForNwAlert">
											Site Id<span data-ng-show="orderByFieldForNwAlert == 'siteId'">
												<span data-ng-show="!reverseSortForNwAlert"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSortForNwAlert"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>

									<th><a style="color: white"
										data-ng-click="orderByFieldForNwAlert='dcSerialNo'; reverseSortForNwAlert = !reverseSortForNwAlert">
											DC Serial No<span
											data-ng-show="orderByFieldForNwAlert == 'dcSerialNo'"> <span
												data-ng-show="!reverseSortForNwAlert"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSortForNwAlert"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>

									<th><a style="color: white"
										data-ng-click="orderByFieldForNwAlert='alert'; reverseSortForNwAlert = !reverseSortForNwAlert">
											Alert<span data-ng-show="orderByFieldForNwAlert == 'alert'"> <span
												data-ng-show="!reverseSortForNwAlert"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSortForNwAlert"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>

									<th><a style="color: white"
										data-ng-click="orderByFieldForNwAlert='dateFlagged'; reverseSortForNwAlert = !reverseSortForNwAlert">
											Date Flagged<span
											data-ng-show="orderByFieldForNwAlert == 'dateFlagged'"> <span
												data-ng-show="!reverseSortForNwAlert"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSortForNwAlert"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>

									<th><a style="color: white"
										data-ng-click="orderByFieldForNwAlert='dateCleared'; reverseSortForNwAlert = !reverseSortForNwAlert">
											Date Cleared<span
											data-ng-show="orderByFieldForNwAlert == 'dateCleared'"> <span
												data-ng-show="!reverseSortForNwAlert"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSortForNwAlert"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>

								</tr>

								<tr
									data-ng-repeat="x in networkAlertsList.slice(((currentPageForNwAlert-1)*itemsPerPageForNwAlert), ((currentPageForNwAlert)*itemsPerPageForNwAlert)) | orderBy:orderByFieldForNwAlert:reverseSortForNwAlert">
									<td>{{x.siteId}}</td>
									<td>{{x.dcSerialNo}}</td>
									<td>{{x.alert}}</td>
									<td>{{x.dateFlagged}}</td>
									<td></td>
								</tr>

							</table>
							<div class="grid-bottom">
								<div class="show-record col-sm-3">
									<span class="records">Records:</span> <select
										data-ng-model="recordSizeForNwAlert"
										data-ng-change="setItemsPerPageForNwAlert(recordSizeForNwAlert)"
										data-ng-options="x for x in paginationList"></select>
								</div>
								<div class="paging">
									<ul uib-pagination data-previous-text="&lt;&lt;"
										data-next-text="&gt;&gt;" data-total-items="totalItemsForNwAlert"
										data-ng-model="currentPageForNwAlert" data-max-size="maxSize"
										data-boundary-links="true" data-ng-change="pageChangedForNwAlert()"
										class="pagination-sm" data-items-per-page="itemsPerPageForNwAlert"></ul>
								</div>
							</div>
							<div class="grid-bottom">
								<div class="form-group auto" style="text-align: right;">
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<button class="default ic_reset"
										data-ng-click="exportToCsvForNetworkAlert()">Export
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
