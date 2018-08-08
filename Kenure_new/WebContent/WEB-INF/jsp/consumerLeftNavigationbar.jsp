<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->

<script src="js/custom.js" type="text/javascript"></script>


<script src="js/html5.js"></script>
<script>localStorage.path = "${pageContext.request.contextPath}";</script>
<script src="app/app.js"></script>

<script>
function onClickMenu(clicked_id) {
	localStorage.activeConsumerMenu = clicked_id;
}

window.onload = (function(){
	
	if (localStorage.activeConsumerMenu == "") {
		var a = document.getElementById("dashboard");
		if (a != null) {
			a.classList.add("active");
		}
	} else {
		var a = document.getElementById(localStorage.activeConsumerMenu);
		if (a != null) {
			a.classList.add("active");
		}
	}

})();
</script>

</head>



<body>
	<div class="leftbar" id="leftbar">
			<ul class="menulist">
				
				<li class="nav">
					<a class="menu" href="consumerDashboard" id="dashboard" onclick="onClickMenu(this.id)">
						<i><img src="images/ic-dashboard.png" alt="" /></i> Dashboard
					</a>
				</li>
				<li>
					<a class="menu" href="consumerEPManagement" id="consumeEPManagement" title="" onclick="onClickMenu(this.id)">
						<i><img src="images/ic-endpoint_mgmt.png" alt="" /></i>Endpoint Management
					</a>
				</li>
				<li>
					<a class="menu" href="usageReport" id="usageReport" title="" onclick="onClickMenu(this.id)">
						<i><img src="images/ic-usage_report.png" alt="" /></i>Usage Report
					</a>
				</li>
				<li>
					<a class="menu" href="billingReport" id="billingReport" title="" onclick="onClickMenu(this.id)">
						<i><img src="images/ic-billing_report.png" alt="" /></i>Billing Report
					</a>
				</li>
			</ul>
		</div>
</body>

</html>
