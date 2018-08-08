	<!DOCTYPE>
<!--  -->
<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compati\ble" content="IE=edge" />
<title>BLU Tower</title>

<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<link href="${pageContext.request.contextPath}/css/org-chart.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="https://www.google.com/jsapi"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/reportscontroller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/popupcontroller.js"></script>

<script type="text/javascript">
	google.load("visualization", "1", {
		packages : [ "corechart" ]
	});
</script>
<script type="text/javascript">
	angular.module('myApp').requires.push('ui.bootstrap');
</script>

<style type="text/css">
.app-modal-window .modal-dialog {
	width: 2500px;
}
</style>

</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="reportscontroller"
		data-ng-init="initFreeReportData()">

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>Analytics&nbsp;&nbsp;>>&nbsp;&nbsp;Free Report</h1>
			<div class="middle">
				<div class="status error" data-ng-show="nosuchSiteFoundDiv">
					<span>No such Records Found!</span>
				</div>
				<div class="boxpanel">
					<span class="heading">Select Fields</span>
					<div class="box-body">
						<div class="filter_panel">
							<div class="form-group">

								<div class="form-group" style="margin-top: 7px; width: 650px;">
									<input type="radio" data-ng-model="selectedField"
										value="dcCheckBox" data-ng-change="selectionChanged()" /><span></span>DataCollector Fields <input type="radio"
										data-ng-model="selectedField" value="consumerEPCheckBox"
										data-ng-change="selectionChanged()" /><span></span>Consumer
									EP Fields <input type="radio" data-ng-model="selectedField"
										value="siteCheckBox" data-ng-change="selectionChanged()" /><span></span>Site
									Fields <input type="radio" data-ng-model="selectedField"
										value="installerCheckBox" data-ng-change="selectionChanged()" /><span></span>Installer
									Fields <input type="radio" data-ng-model="selectedField"
										value="consumerCheckBox" data-ng-change="selectionChanged()" /><span></span>Consumer
									Fields
								</div>
							</div>
						</div>
					</div>

					<!-- Field Selection with select ends here -->

					<div class="boxpanel">
						<div class="box-body">
							<form>
								<div class="filter_panel">
									<div class="form-group">
										<label class="control-label"><h5>Column 1</h5></label> <select
											class="form-control" data-ng-model="column1"
											data-ng-change="freeReportfunCalled('col1',column1)">
											<option value="" selected="selected">-- Select Column 1 field --</option>
											<option data-ng-repeat="(key,value) in displayList" value="{{value}}">{{key}}</option>
										</select>
									</div>
									<div class="form-group">
										<label class="control-label"><h5>Column 2</h5></label> <select
											class="form-control" data-ng-model="column2"
											data-ng-change="freeReportfunCalled('col2',column2)">
											<option value="" selected="selected">-- Select Column 2 field --</option>
											<option data-ng-repeat="(key,value) in displayList" value="{{value}}">{{key}}</option>
										</select>
									</div>
									<div class="form-group">
										<label class="control-label"><h5>Column 3</h5></label> <select
											class="form-control" data-ng-model="column3"
											data-ng-change="freeReportfunCalled('col3',column3)">
											<option value="" selected="selected">-- Select Column 3 field --</option>
											<option data-ng-repeat="(key,value) in displayList" value="{{value}}">{{key}}</option>
										</select>
									</div>
									<div class="form-group">
										<label class="control-label"><h5>Column 4</h5></label> <select
											class="form-control" data-ng-model="column4"
											data-ng-change="freeReportfunCalled('col4',column4)">
											<option value="" selected="selected">-- Select Column 4 field --</option>
											<option data-ng-repeat="(key,value) in displayList" value="{{value}}">{{key}}</option>
										</select>
									</div>
									<div class="form-group">
										<label class="control-label"><h5>Column 5</h5></label> <select
											class="form-control" data-ng-model="column5"
											data-ng-change="freeReportfunCalled('col5',column5)">
											<option value="" selected="selected">-- Select Column 5 field --</option>
											<option data-ng-repeat="(key,value) in displayList" value="{{value}}">{{key}}</option>
										</select>
									</div>
									<div class="form-group">
										<label class="control-label"><h5>Column 6</h5></label> <select
											class="form-control" data-ng-model="column6"
											data-ng-change="freeReportfunCalled('col6',column6)">
											<option value="" selected="selected">-- Select Column 6 field --</option>
											<option data-ng-repeat="(key,value) in displayList" value="{{value}}">{{key}}</option>
										</select>
									</div>
								</div>
							</form>
						</div>
					</div>
					<!-- Field Selection with select ends here -->

					<!-- Search Type Here -->

					<div class="boxpanel">
						<span class="heading">Search Fields :</span>
						<div class="box-body">
							<form>
								<div class="filter_panel">

									<div class="form-group">
										<label class="control-label"><h5>Report By</h5></label> <select
											class="form-control " data-ng-model="selectedReport"
											data-ng-change="showReportPeriodDiv()" required>
											<option value="default">---Please Select Report---</option>
											<option value="Week">Week</option>
											<option value="Month">Month</option>
											<option value="Select Dates">Select Dates</option>
										</select>
									</div>
									<div class="form-group" data-ng-show="reportPeriodDiv">
										<label class="control-label"><h5>Report Period</h5></label> <input
											type="number" class="form-control"
											data-ng-model="reportPeriod" min="0" required>
									</div>
									<div class="form-group" data-ng-show="startDateDiv">
										<label class="control-label"><h5>Start Date</h5></label> <input
											type="text" class="form-control calendar-input"
											data-uib-datepicker-popup="{{format}}"
											placeholder="-- Click here to Select Start Date --"
											data-ng-model="startDate" data-is-open="popup1.opened"
											data-datepicker-options="" onkeypress="return false"
											data-close-text="Close" data-ng-click="open1()"
											data-alt-input-formats="altInputFormats" required>
									</div>
									<div class="form-group" data-ng-show="endDateDiv">
										<label class="control-label"><h5>End Date</h5></label> <input
											type="text" class="form-control calendar-input"
											data-uib-datepicker-popup="{{format}}"
											placeholder="-- Click here to Select End Date --"
											data-ng-model="endDate" data-is-open="popup2.opened"
											data-datepicker-options="" onkeypress="return false"
											data-close-text="Close" data-ng-click="open2()"
											data-alt-input-formats="altInputFormats" required>
									</div>

									<div class="form-group">
										<label class="control-label"><h5>Installation
												Name</h5></label> <input type="text" class="form-control"
											data-ng-model="installationName" data-ng-trim="false">
									</div>

									<div class="form-group">
										<label class="control-label"><h5>Site Name</h5></label> <input
											type="text" class="form-control" data-ng-model="siteName"
											data-ng-trim="false">
									</div>

									<div class="form-group">
										<label class="control-label"><h5>DC Serial Number</h5></label>
										<input type="text" class="form-control"
											data-ng-model="dcSerialNumber" data-ng-trim="false">
									</div>

									<div class="form-group auto">
										<label class="control-label">&nbsp;</label>
										<button class="primary ic_search"
											data-ng-click="searchFreeReportData()">Search</button>
										<button type="reset"
											data-ng-click="refreshAggregateConsumptionReportPage()"
											class="default ic_reset">Reset</button>
									</div>

								</div>
							</form>
						</div>
					</div>

					<!-- Search Type Ends Here -->
					<br>
					<div class="grid-content">
						<table class="grid">
							<tr data-ng-repeat="y in tableHeaderArray">
							<th data-ng-if="y.col1.length>0">{{getHeaderValueBasedOnKey(y.col1)}}</th>
							<th data-ng-if="y.col2.length>0">{{getHeaderValueBasedOnKey(y.col2)}}</th>
							<th data-ng-if="y.col3.length>0">{{getHeaderValueBasedOnKey(y.col3)}}</th>
							<th data-ng-if="y.col4.length>0">{{getHeaderValueBasedOnKey(y.col4)}}</th>
							<th data-ng-if="y.col5.length>0">{{getHeaderValueBasedOnKey(y.col5)}}</th>
							<th data-ng-if="y.col6.length>0">{{getHeaderValueBasedOnKey(y.col6)}}</th>
						</tr>

							<tr data-ng-repeat="x in finalArray.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort ">
								<td data-ng-if="x.col1 != null">{{x.col1}}</td>
								<td data-ng-if="x.col2 != null">{{x.col2}}</td>
								<td data-ng-if="x.col3 != null">{{x.col3}}</td>
								<td data-ng-if="x.col4 != null">{{x.col4}}</td>
								<td data-ng-if="x.col5 != null">{{x.col5}}</td>
								<td data-ng-if="x.col6 != null">{{x.col6}}</td>
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
									data-ng-click="downloadFreeReportCSVFile(tableHeaderArray)">Export
									to CSV file</button>
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