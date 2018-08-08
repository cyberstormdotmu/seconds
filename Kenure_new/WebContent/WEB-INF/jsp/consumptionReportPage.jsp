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

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/reportscontroller.js"></script>
	
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<link href="${pageContext.request.contextPath}/css/jquery.dataTables.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/js/jquery.dataTables.js"></script>


<script type="text/javascript">
	google.charts.load("current", {packages:['corechart', 'line']});
</script>

</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="reportscontroller">

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>Analytics&nbsp;&nbsp;>>&nbsp;&nbsp;Consumption Report</h1>
			<div class="middle">
				<div class="status error" data-ng-show="nosuchSiteFoundDiv">
					<span>{{message}}</span>
				</div>
				<div class="boxpanel">
					<span class="heading">Search</span>
					<div class="box-body">
						<div class="filter_panel">
							<div class="form-group">
								<label class="control-label"><h5>Generate a graph based on</h5></label> 
								<select data-ng-model="selectedBaseType" data-ng-change="setBaseTypeIds()" autofocus="autofocus">
									<option value="">---Please select---</option>
									<option value="installation">Installation</option>
									<option value="districtWaterMeter">District Water Meter</option>
									<option value="consumerAccNo">Consumer Acc No</option>
									<option value="registerId">Register ID</option>
								</select>
							</div>
							<div class="form-group" data-ng-show="installaionType">
								<label class="control-label"><h5>Select Installation id</h5></label>
								<select data-ng-model="selectedInstallation"
									data-ng-options="region as region.regionName for region in baseTypeInstallationList" style="width: 150px;"><option value="">---Please Select---</option></select>
							</div>
							<div class="form-group" data-ng-show="districtWaterMeterType">
								<label class="control-label"><h5>Select DistrictWaterMeter</h5></label>
								<select data-ng-model="selectedDWT"
									data-ng-options="duMeter as duMeter.duMeterSerialNo for duMeter in baseTypeDWMList" style="width: 150px;"><option value="">---Please Select---</option></select>
							</div>
							<div class="form-group" data-ng-show="consumerAccNoType">
								<label class="control-label"><h5>Select Consumer Acc no</h5></label>
								<select data-ng-model="selectedConsumerAccNo"
									data-ng-options="consumer as consumer.consumerAccNo for consumer in baseTypeConsumerList" style="width: 150px;"><option value="">---Please Select---</option></select>
							</div>
							<div class="form-group" data-ng-show="registerIdType">
								<label class="control-label"><h5>Select Register id</h5></label>
								<select data-ng-model="selectedRegisterId"
									data-ng-options="register as register.registerId for register in baseTypeRegisterList" style="width: 150px;"><option value="">---Please Select---</option></select>
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
									data-ng-click="getConsumptionGraph()">Search</button>
								<button type="reset" data-ng-click="consumptionGraphReset()"
									class="default ic_reset">Reset</button>
							</div>
						</div>
					</div>
				</div>
				<div class="boxpanel">
					<div class="box-body">
						<div class="filter_panel" data-ng-show="graphTypeShow">
							<div class="form-group">
								<label class="control-label"><h5>Select Graph Type</h5></label> 
								<select data-ng-model="selectedGraphType" data-ng-change="showTypeBasedGraph()" 
									data-ng-options="typeList as typeList for typeList in graphTypeList" style="width: 150px;">
								</select>
							</div>
						</div>
						<div class="filter_panel" data-ng-show="chartShow">
							<div>
								<div id="chart_div" align="center"></div>
							</div>
							<button data-ng-click="consumptionExportToCSV()"
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