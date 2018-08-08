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

<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<link href="${pageContext.request.contextPath}/css/org-chart.css" rel="stylesheet" type="text/css" />

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/reportscontroller.js"></script>
<script src="${pageContext.request.contextPath}/js/Chart.bundle.js"></script>
<script src="${pageContext.request.contextPath}/js/utils.js"></script>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<link href="${pageContext.request.contextPath}/css/jquery.dataTables.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/js/jquery.dataTables.js"></script>


<script type="text/javascript">
	google.charts.load("current", {packages:['corechart']});
</script>

    <style>
    canvas {
        -moz-user-select: none;
        -webkit-user-select: none;
        -ms-user-select: none;
    }
    </style>
</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="reportscontroller"
		data-ng-init="initNetworkConsumption()">

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>Analytics&nbsp;&nbsp;>>&nbsp;&nbsp;Network Consumption Report</h1>
			<div class="middle">
				<div class="status error" data-ng-show="nosuchSiteFoundDiv">
					<span>{{message}}</span>
				</div>
				<div class="boxpanel">
					<span class="heading">Search</span>
					<div class="box-body">
						<div class="filter_panel">
							<div class="form-group">
								<label class="control-label"><h5>Select District Utility Meter</h5></label>
								<select data-ng-model="selectedDUMeter"
									data-ng-options="duMeter as duMeter.duMeterSerialNo for duMeter in duMeterList" style="width: 150px;" autofocus="autofocus">
									<option value="">---Please Select---</option>
									</select>
							</div>
							<div class="form-group">
								<label class="control-label"><h5>Select period</h5></label> 
								<select data-ng-model="selectedPeriodType" data-ng-change="setPeriodTime()" style="width: 150px;">
									<option value="">---Please Select---</option>
									<!-- not selected / blank option -->
									<option value="hour">Hours</option>
									<option value="day">Days</option>
									<option value="month">Months</option>
									<option value="year">Year</option>
								</select>
							</div>
							<div class="form-group"  style="width: 150px;">
								<label class="control-label"><h5>No of Periods</h5></label> 
								<select data-ng-model="selectedPeriodTime"
									data-ng-options="periodTime as periodTime for periodTime in periodTimeList" style="width: 100px;"></select>
							</div>
							<div class="form-group" style="width: 100px;">
								<label class="control-label">&nbsp;</label>
								<input type="checkbox" data-ng-model="showDataTbl" style="outline: 1px solid #1e5180"><span></span>Show Data
							</div>
							<div class="form-group">
								<label class="control-label">&nbsp;</label>
								<button class="primary ic_search" type="submit"
									data-ng-click="getNWConsumptionGraph()">Search</button>
								<button type="reset" data-ng-click="reset()"
									class="default ic_reset">Reset</button>
							</div>
						</div>
					</div>
				</div>
				<div class="boxpanel">
					<div class="box-body">
						<div class="filter_panel" data-ng-show="chartShow">
							<div>
								 <canvas id="canvas"></canvas>
							</div>
							<button data-ng-click="exportToCSV()"
									class="default ic_reset">Export to CSV</button>
						</div>
						<div class="filter_panel" data-ng-show="tblShow" style="padding-left: 12%;">
							<div style="width: 500px;">
								<table id="chart_data" class="display" style="width: 100%;"></table>
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