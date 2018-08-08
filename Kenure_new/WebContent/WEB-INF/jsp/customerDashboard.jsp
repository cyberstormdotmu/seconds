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
<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/notifIt.css" rel="stylesheet" type="text/css" />
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->

<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/notifIt.js" type="text/javascript"></script>

<!-- <script src="js/angular-ui-router.js"></script> -->

<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script>localStorage.path = "${pageContext.request.contextPath}";</script>
<script>
if (localStorage.activeMenu == "customerDashboard") {
	localStorage.activeMenu = "";
}


</script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/controller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/usercontroller.js"></script>


</head>
<script>
	function not(data){
		/* var str = data.toString();
		var array = str.split(",");
		var noOfVarReq = array.length;
		for(var i=0;i<noOfVarReq;i++){alert(array[i]);} */
		notif({
		msg: "Alerts in endpoint with register id :<br><b> "+data+"</b>",
		type: "success",
		height:100,
		multiline:1
		});
}
</script>

<style>

.tooltip:hover .tooltiptext {
    visibility: visible;
}
</style>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="logincontroller" data-ng-init="getCustomerNotifications(${customerId})">
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>Current Status</h1>
			<div class="middle">
				<div class="dashbaord_tiles">
					<ul>
						<li>
						<i class="tooltip infotip pop"><span class="tooltiptext">Daily consumption is the aggregate consumption calculated over a 24hr period midnight to midnight.</span></i>
						<a href="#" class="hvr-sweep-to-top box3"> <span
								class="title">Daily Consumption</span> <span class="bottom_left">Units : 
									<strong>{{dailyConsumption}}</strong>
							</span>
						</a></li>
						<li>
						<i class="tooltip infotip pop"><span class="tooltiptext">Average consumption of households where number of occupants is known.</span></i>
						<a href="#" class="hvr-sweep-to-top box5"> <span
								class="title">Per Capita</span> <span class="bottom_left">
									<strong>{{perCapita}}</strong>
							</span>
						</a></li>
						<li>
						<i class="tooltip infotip pop"><span class="tooltiptext">It is generated from Endpoint readings that nn% higher than the avarage consumption for 7 days, calculated over 24hr period 
							midnight to midnight for that Endpoint.</span></i>
						<a href="#" class="hvr-sweep-to-top box4"> <span
								class="title">Abnormal Usage</span> <span class="bottom_left">
									<strong>{{abnormalUsers}}</strong>
							</span>
						</a></li>
						<li><a href="#" class="hvr-sweep-to-top box1"> <span
								class="title">Endpoints</span> <span class="bottom_left">Reporting : 
								<strong>{{totalEP}}</strong></span>
						</a></li>
						<li><a href="#" class="hvr-sweep-to-top box2"> <span
								class="title">Missing Endpoints</span> <span class="bottom_left">
									<strong>3</strong>
							</span>
						</a></li>
						<li>
						<i class="tooltip infotip pop"><span class="tooltiptext">New Alert is sum of new alerts (Consumer and Network) raised in past 24hr period midnight to midnight.</span></i>
						<a href="#" data-ng-click="showNotification()" class="hvr-sweep-to-top box6"> <span
								class="title">Alerts</span> <span class="bottom_left">New Alerts : <strong>{{totalAlert}}</strong>
							</span>
						</a></li>
						<li><a href="#" class="hvr-sweep-to-top box7"> <span
								class="title">Data Plan</span> <span class="bottom_left">Current Usage : 
									<strong>{{totalDataUsage}} MB</strong></span><span class="bottom_right">{{usagePer}} %</span>
						</a></li>
						<li><a href="#" class="hvr-sweep-to-top box8"> <span
								class="title">New Billings</span> <span class="bottom_left">Total new bill generated on {{billDate}} : 
									<strong>{{totalBillData}}</strong>
							</span>
						</a></li>
						<li>
						<i class="tooltip infotip pop"><span class="tooltiptext">It is the revenue calculated from the aggregate consumption within the current Billing period.</span></i>
						<a href="#" class="hvr-sweep-to-top box9"> <span
								class="title">Revenue</span> <span class="bottom_left">Generated revenue : 
									<strong>{{totalRevenue}}</strong>
							</span>
						</a></li>
					</ul>
				</div>
			</div>
			<!--Content end-->
		</div>


		<div id="footer" data-ng-include data-src="'../footer'"></div>
	</div>
	<!--wrapper end-->
</body>
</html>

