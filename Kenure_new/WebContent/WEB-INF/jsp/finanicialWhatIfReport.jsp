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

.tariffTableStyle {
	table-layout: fixed;
	width: 100%;
	white-space: normal;
}
/* Column widths are based on these cells */
.row-start {
	width: 20%;
}

.row-to {
	width: 5%;
}

.row-end {
	width: 19%;
}

.row-cost {
	width: 18%;
}

.row-name {
	width: 15%;
}

.row-checked {
	width: 5%;
}

.tariffTableStyle td {
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.tariffTableStyle th {
	color: black;
}

.tariffTableStyle td, .tariffTableStyle th {
	text-align: center;
	padding: 6px 10px;
}

.calculationfield {
	width: 45px;
	margin-left: 15px;
	padding-left: 5px;
}
</style>

</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="reportscontroller"
		data-ng-init="initFinanicalWhatIfData()">

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>Analytics&nbsp;&nbsp;>>&nbsp;&nbsp;What If Report</h1>
			<div class="middle">

				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>

				<div class="status error" data-ng-show="isError">
					<span> {{errorMessage}} </span>
				</div>

				<div class="boxpanel">
					<!-- <span class="heading">Select Fields</span> -->

					<div class="filter_panel" data-ng-show="installerList">
						<div class="form-group">
							<label class="control-label"><h5>Region</h5></label> <select
								class="form-control" data-ng-model="selectregionModel"
								data-ng-change="whatIfRegionSelectionChanged()" autofocus="autofocus">
								<option value="" selected="selected">-- Select Region
									field --</option>
								<option data-ng-repeat="x in installerList" value="{{x}}">{{x}}</option>
							</select>
						</div>

						<div class="form-group" data-ng-show="siteNameList">
							<label class="control-label"><h5>Site Under Region
									{{selectregionModel}}</h5></label> <select class="form-control"
								data-ng-model="selectsiteModel"
								data-ng-change="whatIfSiteSelectionChanged()">
								<option value="default" selected="selected">-- Select
									Site field --</option>
								<option data-ng-repeat="x in siteNameList track by $index"
									value="{{x}}">{{x}}</option>
							</select>
						</div>
					</div>
				</div>

				<!-- Fron here -->

				<form>
					<div class="installed-box" style="width: 50%"
						data-ng-show="displayTariffDiv">
						<div class="title-hed">
							<span class="heading"><h3>Current Charges</h3></span>
						</div>

						<div class="border-box">
							<div class="box-body">
								<div class="grid-content">
									<table class="tariffTableStyle">
										<thead>
											<tr>
												<th class="row-name"></th>
												<th class="row-start">Consumption Band Start</th>
												<th class="row-end">Consumption Band End</th>
												<th class="row-cost">Cost</th>
											</tr>
										</thead>
										<tbody>
											<tr data-ng-show="trans1.start">
												<td class="row-name" data-ng-model="trans1">Threshold 1</td>
												<td class="row-start"><input type="number"
													data-ng-disabled="true" data-ng-model="trans1.start"
													min="0" max="9999999999" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="trans1.end" data-ng-disabled="true" min="0"
													max="9999999999" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-disabled="true"
													data-ng-model="trans1.cost"></td>

											</tr>

											<tr data-ng-show="trans2.start">
												<td class="row-name" data-ng-model="trans2">Threshold 2</td>
												<td class="row-start"><input type="number"
													data-ng-disabled="true" data-ng-model="trans2.start"
													data-ng-value="trans2.startValue" min="0" max="9999999999" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="trans2.end" data-ng-disabled="true" min="0"
													max="9999999999" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-disabled="true"
													data-ng-model="trans2.cost"></td>

											</tr>

											<tr data-ng-show="trans3.start">
												<td class="row-name" data-ng-model="trans3">Threshold 3</td>
												<td class="row-start"><input type="number"
													data-ng-disabled="true" data-ng-model="trans3.start"
													data-ng-value="trans3.startValue" min="0" max="9999999999" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="trans3.end" data-ng-disabled="true" min="0"
													max="9999999999" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-disabled="true"
													data-ng-model="trans3.cost"></td>

											</tr>

											<tr data-ng-show="trans4.start">
												<td class="row-name" data-ng-model="trans4">Threshold 4</td>
												<td class="row-start"><input type="number"
													data-ng-disabled="true" data-ng-model="trans4.start"
													data-ng-value="trans4.startValue" min="0" max="9999999999" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="trans4.end" data-ng-disabled="true" min="0"
													max="9999999999" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-disabled="true"
													data-ng-model="trans4.cost"></td>

											</tr>

											<tr data-ng-show="trans5.start">
												<td class="row-name" data-ng-model="trans5">Threshold 5</td>
												<td class="row-start"><input type="number"
													data-ng-disabled="true" data-ng-model="trans5.start"
													data-ng-value="trans5.startValue" min="0" max="9999999999" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="trans5.end" data-ng-disabled="true" min="0"
													max="9999999999" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-disabled="true"
													data-ng-model="trans5.cost"></td>

											</tr>

											<tr data-ng-show="trans6.start">
												<td class="row-name" data-ng-model="trans6">Threshold 6</td>
												<td class="row-start"><input type="number"
													data-ng-disabled="true" data-ng-model="trans6.start"
													data-ng-value="trans6.startValue" min="0" max="9999999999" /></td>
												<td class="row-end"><input type="number"
													data-ng-disabled="true" data-ng-model="trans6.end" min="0"
													max="9999999999" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-disabled="true"
													data-ng-model="trans6.cost"></td>

											</tr>

											<tr data-ng-show="trans7.start">
												<td class="row-name" data-ng-model="trans7">Threshold 7</td>
												<td class="row-start"><input type="number"
													data-ng-disabled="true" data-ng-model="trans7.start"
													data-ng-value="trans7.startValue" min="0" max="9999999999"
													data-ng-disabled="true" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="trans7.end" min="0" max="9999999999"
													data-ng-disabled="true" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-disabled="true"
													data-ng-model="trans7.cost"></td>

											</tr>

											<tr data-ng-show="trans8.start">
												<td class="row-name" data-ng-model="trans8">Threshold 8</td>
												<td class="row-start"><input type="number"
													data-ng-disabled="true" data-ng-model="trans8.start"
													data-ng-value="trans8.startValue" min="0" max="9999999999"
													data-ng-disabled="true" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="trans8.end" min="0" max="9999999999"
													data-ng-disabled="true" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-disabled="true"
													data-ng-model="trans8.cost"></td>

											</tr>

											<tr data-ng-show="trans9.start">
												<td class="row-name" data-ng-model="trans9">Threshold 9</td>
												<td class="row-start"><input type="number"
													data-ng-disabled="true" data-ng-model="trans9.start"
													data-ng-value="trans9.startValue" min="0" max="9999999999"
													data-ng-disabled="true" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="trans9.end" min="0" max="9999999999"
													data-ng-disabled="true" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-disabled="true"
													data-ng-model="trans9.cost"></td>

											</tr>

											<tr data-ng-show="trans10.start">
												<td class="row-name" data-ng-model="trans10">Threshold
													10</td>
												<td class="row-start"><input type="number"
													data-ng-disabled="true" data-ng-model="trans10.start"
													data-ng-value="trans10.startValue" min="0" max="9999999999"
													data-ng-disabled="true" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="trans10.end" min="0" max="9999999999"
													data-ng-disabled="true" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-disabled="true"
													data-ng-model="trans10.cost"></td>

											</tr>

										</tbody>
									</table>
								</div>
							</div>
						</div>

					</div>
				</form>

				<form>

					<div class="installed-box" style="width: 50%"
						data-ng-show="displayTariffDiv">
						<div class="title-hed">
							<span class="heading"><h3>What IF Charges</h3></span>
						</div>

						<div class="border-box">
							<div class="box-body">
								<div class="grid-content">
									<table class="tariffTableStyle">
										<thead>
											<tr>
												<th class="row-name"></th>
												<th class="row-start">Consumption Band Start</th>
												<th class="row-end">Consumption Band End</th>
												<th class="row-cost">Cost</th>
											</tr>
										</thead>
										<tbody>
											<tr data-ng-model="iftrans1">
												<td class="row-name">Threshold 1</td>
												<td class="row-start"><input type="number"
													data-ng-model="iftrans1.ifstart1" min="0" max="9999999999" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="iftrans1.ifend1" min="0" max="9999999999"
													data-ng-change="whatIfEndChanged(1)" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-model="iftrans1.ifcost1" /></td>

											</tr>

											<tr>
												<td class="row-name">Threshold 2</td>
												<td class="row-start"><input type="number"
													data-ng-model="iftrans2.ifstart2" data-ng-disabled="true"
													data-ng-value="iftrans2.startValue" min="0"
													max="9999999999" data-ng-change="alert('changed !!');" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="iftrans2.ifend2" min="0" max="9999999999"
													data-ng-change="whatIfEndChanged(2)" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-model="iftrans2.ifcost2"></td>

											</tr>

											<tr>
												<td class="row-name">Threshold 3</td>
												<td class="row-start"><input type="number"
													data-ng-model="iftrans3.ifstart3" data-ng-disabled="true"
													data-ng-value="iftrans3.startValue" min="0"
													max="9999999999" /></td>
												<td class="row-ifend"><input type="number"
													data-ng-model="iftrans3.ifend3" min="0" max="9999999999"
													data-ng-change="whatIfEndChanged(3)" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-model="iftrans3.ifcost3"></td>

											</tr>

											<tr>
												<td class="row-name">Threshold 4</td>
												<td class="row-start"><input type="number"
													data-ng-model="iftrans4.ifstart4" data-ng-disabled="true"
													data-ng-value="iftrans4.startValue" min="0"
													max="9999999999" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="iftrans4.ifend4" min="0" max="9999999999"
													data-ng-change="whatIfEndChanged(4)" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-model="iftrans4.ifcost4"></td>

											</tr>

											<tr>
												<td class="row-name">Threshold 5</td>
												<td class="row-start"><input type="number"
													data-ng-model="iftrans5.ifstart5" data-ng-disabled="true"
													data-ng-value="iftrans5.startValue" min="0"
													max="9999999999" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="iftrans5.ifend5" min="0" max="9999999999"
													data-ng-change="whatIfEndChanged(5)" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-model="iftrans5.ifcost5"></td>

											</tr>

											<tr>
												<td class="row-name">Threshold 6</td>
												<td class="row-start"><input type="number"
													data-ng-model="iftrans6.ifstart6" data-ng-disabled="true"
													data-ng-value="iftrans6.startValue" min="0"
													max="9999999999" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="iftrans6.ifend6" min="0" max="9999999999"
													data-ng-change="whatIfEndChanged(6)" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-model="iftrans6.ifcost6"></td>

											</tr>

											<tr>
												<td class="row-name">Threshold 7</td>
												<td class="row-start"><input type="number"
													data-ng-model="iftrans7.ifstart7" data-ng-disabled="true"
													data-ng-value="iftrans7.startValue" min="0"
													max="9999999999" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="iftrans7.ifend7" min="0" max="9999999999"
													data-ng-change="whatIfEndChanged(7)" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-model="iftrans7.ifcost7"></td>

											</tr>

											<tr>
												<td class="row-name">Threshold 8</td>
												<td class="row-start"><input type="number"
													data-ng-model="iftrans8.ifstart8" data-ng-disabled="true"
													data-ng-value="iftrans8.startValue" min="0"
													max="9999999999" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="iftrans8.ifend8" min="0" max="9999999999"
													data-ng-change="whatIfEndChanged(8)" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-model="iftrans8.ifcost8"></td>

											</tr>

											<tr>
												<td class="row-name">Threshold 9</td>
												<td class="row-start"><input type="number"
													data-ng-model="iftrans9.ifstart9" data-ng-disabled="true"
													data-ng-value="iftrans9.startValue" min="0"
													max="9999999999" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="iftrans9.ifend9" min="0" max="9999999999"
													data-ng-change="whatIfEndChanged(9)" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-model="iftrans9.ifcost9"></td>

											</tr>

											<tr>
												<td class="row-name">Threshold 10</td>
												<td class="row-start"><input type="number"
													data-ng-model="iftrans10.ifstart10" data-ng-disabled="true"
													data-ng-value="iftrans10.startValue" min="0"
													max="9999999999" /></td>
												<td class="row-end"><input type="number"
													data-ng-model="iftrans10.ifend10" min="0" max="9999999999"
													data-ng-change="whatIfEndChanged(10)" /></td>
												<td class="row-cost"><input type="number" min="0"
													max="9999999999" data-ng-model="iftrans10.ifcost10"></td>

											</tr>

										</tbody>
									</table>
								</div>
							</div>
						</div>

						<div class="form-group"></div>
						<div class="form-group">
							<label class="control-label">&nbsp;</label>
							<button class="primary ic_search"
								data-ng-click="findrevenueBasedOnNewTariff(0)"
								data-ng-disabled="revenue &le; 0">Apply This Tariff</button>
						</div>

					</div>
				</form>
				<!-- To here -->

				<div class="boxpanel" data-ng-show="displayTariffDiv">
					<!-- <span class="heading">Select Fields</span> -->

					<div class="filter_panel">
						<div class="form-group">
							<label class="control-label"><h5>Calculate For</h5></label> <select
								class="form-control" data-ng-model="timeSelection"
								data-ng-change="timeSelectionChanged()">
								<option value="default" selected="selected">-- Select
									time field --</option>
								<option value="lastYear" selected="selected">Last Year</option>
								<option value="lastQuarter" selected="selected">Last
									Quarter</option>
								<option value="lastMonth" selected="selected">Last
									Month</option>
							</select>

							<div class="form-group"></div>
							<div class="form-group"></div>
						</div>

						<div class="form-group" style="width: 110px;">
							<label class="control-label">&nbsp;</label>
							<button class="primary ic_search"
								data-ng-click="calculateTimeData()">Calculate</button>
						</div>

						<div class="form-group">
							<div
								style="border: 1px solid black; height: 110px; border-radius: 25px; text-align: center; vertical-align: middle;">
								<font size="4" style="font-weight: bold"><p>Revenue
										Generated</p> <span> {{revenue || 0}} </span> </font>
							</div>
						</div>

						<div class="form-group"></div>
						<div class="form-group"></div>

						<div class="form-group">
							<div
								style="border: 1px solid black; height: 110px; border-radius: 25px; text-align: center; vertical-align: middle;">
								<font size="4" style="font-weight: bold"><p>What If
										Generated</p> <span> {{whatIfRevenue || 0}} </span>
									<p>Which is</p> <span data-ng-style="statusColor">
										{{percentage}}% {{profitLossStatus}} </span> </font>
							</div>
						</div>

					</div>
				</div>
				<div class="boxpanel" data-ng-show="displayTariffDiv">
					<div class="form-group">
						<form>

							<div class="filter_panel">
								<div class="form-group">
									<label class="control-label"><h5>Consumption Var</h5></label> <input
										class="calculationfield" type="number" min="-1000" max="1000"
										data-ng-model="consumptionVar" style="width: 70px" /> %
									<button class="primary ic_search" style="margin-left: 10px;"
										data-ng-click="findrevenueBasedOnNewTariff(consumptionVar)">Apply
									</button>
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Meter Growth</h5></label> <input
										class="calculationfield" type="number" min="-1000" max="1000"
										style="width: 70px" data-ng-model="meterGrowthVar"
										data-ng-change="calculateMeterGrowth()" /> %
								</div>
							</div>
						</form>
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