<!DOCTYPE>
<!--  -->
<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>

<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<link href="css/org-chart.css" rel="stylesheet" type="text/css" />

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/customercontroller.js"></script>
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

<script type="text/javascript">
	google.charts.load('current', {
		packages : [ "orgchart" ]
	});
</script>

</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="customercontroller"
		data-ng-init="initdcConnectionMap()">

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>Setup&nbsp;&nbsp;>>&nbsp;&nbsp;Connection Map</h1>
			<div class="middle">
				<div class="status error" data-ng-show="nosuchSiteFoundDiv">
					<span>No such Records Found!</span>
				</div>
				<div class="boxpanel">
					<span class="heading">DataCollector-Endpoint Mapping</span>
					<div class="box-body">
						<div class="filter_panel">
							<div class="form-group">
								<label class="control-label">Select DataCollector</label>
								<select data-ng-model="seletedDc"
									data-ng-options="dc as dc.dcSerialNo for dc in dclist"
									data-ng-change="getConnectionMap()">
									<option value="" selected="selected">-- Please Select DataCollector --</option>	
								</select>
							</div>
						</div>
					</div>
				</div>
				<div class="boxpanel">
					<div class="box-body">
						<div class="filter_panel">
							<div class="form-group">
								<div id="chart_div" align="center"></div>
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