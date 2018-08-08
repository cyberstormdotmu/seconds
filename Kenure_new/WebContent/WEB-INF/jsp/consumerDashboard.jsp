<!DOCTYPE>
<!--  -->
<html lang="en">


<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
<title>BLU Tower</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/dropkick.css" rel="stylesheet" type="text/css" />
<link href="css/notifIt.css" rel="stylesheet" type="text/css" />
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->

<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/angular.js"></script>
<script type="text/javascript" src="js/angular-cookies.js"></script>

<script src="js/dropkick.js" type="text/javascript"></script>
<script src="js/custom.js" type="text/javascript"></script>
<script src="js/notifIt.js" type="text/javascript"></script>

<!-- <script src="js/angular-ui-router.js"></script> -->

<script src="js/angular-ui-router.js"></script>
<script src="js/html5.js"></script>
<script>localStorage.path = "${pageContext.request.contextPath}";</script>
<script>
if (localStorage.activeMenu == "customerDashboard") {
	localStorage.activeMenu = "";
}


</script>
<script src="app/app.js"></script>
<script src="app/controller/consumercontroller.js"></script>
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
.tooltip {
    /* position: relative;
    display: inline-block;
    border-bottom: 1px dotted black; */
}



.tooltip:hover .tooltiptext {
    visibility: visible;
}
</style>

</head>
<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="consumercontroller" data-ng-init="initConsumerDashboardData()">
		
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'header'"></div>
		<div id="leftNavigationbar" data-ng-include data-src="'consumerLeftNavigationbar'"></div>
		
		<div id="content">
			<!--Content Start-->
			<h1>Current Status
			
				<div class="rightbtns" data-ng-show="endPointDropDown">
						<form>
						<select name="country" class="form-control" data-ng-model="selectedEndpoint" data-ng-change="showDashboardData()" required>
							<option selected="selected" value="">-- Select EndPoint --</option>
							<option data-ng-repeat="x in endPointList" value="{{x.registerId}}">{{x.registerId}}</option>
						</select></form>
				</div>
			
			</h1>
			<div class="middle">
				<div class="dashbaord_tiles">
					<ul>
						<li><a href="#" class="hvr-sweep-to-top box10">
								<span class="title">Daily Consumption</span>
									<span class="bottom_left">Units :	<strong>{{totalConsumption}}</strong></span>
							</a>
						</li>
						
						<li><a href="#" class="hvr-sweep-to-top box11">
								<span class="title">Estimated Bill</span>
									<span class="bottom_left"><strong>{{estimatedBill}}</strong></span>
							</a>
						</li>
						
						<li><a href="#" class="hvr-sweep-to-top box12">
								<span class="title">Last Bill</span>
									<span class="bottom_left"><strong>{{lastBillData}}</strong></span>
							</a>
						</li>
						
						<li><a href="#" class="hvr-sweep-to-top box13">
								<span class="title">How am i doing ?</span>
									<span class="bottom_left">{{tariffStatus}}<strong></strong></span>
							</a>
						</li>
						
						<li><a href="billingReport" class="hvr-sweep-to-top box14">
								<span class="title">Billing Data</span>
									<span class="bottom_left">Click Here to see analysis</span>
							</a>
						</li>
						
						<li><a href="usageReport" class="hvr-sweep-to-top box15">
								<span class="title">Usage Data</span>
									<span class="bottom_left">Click here to see analysis</span>
							</a>
						</li>
					</ul>
				</div>
			</div>
			<!--Content end-->
		</div>



		<div id="footer" data-ng-include data-src="'footer'"></div>
	</div>

</body>
</html>



	
	
	

