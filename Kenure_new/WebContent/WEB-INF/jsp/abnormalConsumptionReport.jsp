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
<link href="${pageContext.request.contextPath}/css/sweetalert.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/sweetalert.min.js" type="text/javascript"></script>

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
		data-ng-init="initAbnormalConsumptionInitData()">

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>Analytics&nbsp;&nbsp;>>&nbsp;&nbsp;Abnormal Consumption Report</h1>
			<div class="middle">
				<div class="status error" data-ng-show="nosuchSiteFoundDiv">
					<span>No such Records Found!</span>
				</div>
				<div class="boxpanel">
					<div class="box-body">
						<div class="filter_panel">
								<table style="width: 25%">
									<tr>
										<td style="width: 10%"><label class="control-label">
												<font size="4" style="font-weight: 200;">Abnormal Threshold Factor</font>
										</label></td>
										<td style="width: 5%"><input type="number"
											data-ng-model="thresholdFactor" min="0" max="99"
											value="{{thresholdFactor}}" style="width: 80%"> % </td>
										<!-- <td style="width: 15%">
											<h3>&nbsp;% &nbsp;&nbsp;&nbsp;</h3>
										</td> -->
									</tr>
								</table>
						</div>
					</div>
					<br>
					<div class="grid-content">
						<table class="grid">
							<tr>
								<th><a style="color: white"
									data-ng-click="orderByField='consumerAccountNumber'; reverseSort = !reverseSort">
										Consumer Account No<span
										data-ng-show="orderByField == 'consumerAccountNumber'">
											<span data-ng-show="!reverseSort"
											class="glyphicon glyphicon-triangle-top"></span> <span
											data-ng-show="reverseSort"
											class="glyphicon glyphicon-triangle-bottom"></span>
									</span>
								</a></th>
								<th><a style="color: white"
									data-ng-click="orderByField='averageConsumption'; reverseSort = !reverseSort">
										Average Consumption<span
										data-ng-show="orderByField == 'averageConsumption'"> <span
											data-ng-show="!reverseSort"
											class="glyphicon glyphicon-triangle-top"></span> <span
											data-ng-show="reverseSort"
											class="glyphicon glyphicon-triangle-bottom"></span>
									</span>
								</a></th>
								<th><a style="color: white"
									data-ng-click="orderByField=''; reverseSort = !reverseSort">
										24hr Consumption<span data-ng-show="orderByField == ''">
											<span data-ng-show="!reverseSort"
											class="glyphicon glyphicon-triangle-top"></span> <span
											data-ng-show="reverseSort"
											class="glyphicon glyphicon-triangle-bottom"></span>
									</span>
								</a></th>
								<th>Last 24hrs Consumption Graph</th>
							</tr>

							<tr
								data-ng-repeat="x in consumptionList.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort"
								data-ng-controller="popupcontroller">
								<td>{{x.consumerAccountNumber}}</td>
								<td>{{x.averageConsumption}}</td>
								<td data-ng-if="x.last24hrUsage > 0"
									data-ng-style="setConsumptionStyle({{x}})">{{x.last24hrUsage}}</td>
								<td data-ng-if="x.last24hrUsage <= 0"
									data-ng-style="{color: 'black'}">Last 24hrs Record Not
									Found</td>
								<td data-ng-if="x.last24hrUsage > 0"
									data-ng-style="setConsumptionStyle({{x}})"><a href=""
									data-ng-click="openAbnormalActionGraph(x.registerId,x.consumerAccountNumber)"
									title="edit"> Consumption Graph </a>&nbsp;</td>
								<td data-ng-if="x.last24hrUsage <= 0"
									data-ng-style="{color: '#0F9AD0'}">Record Not Found</td>
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
									data-ng-click="downloadAbnormalConsumptionCSVFile()">Export
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