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
	localStorage.activeAdminMenu = clicked_id;
}

window.onload = (function(){
	
	if (localStorage.activeAdminMenu == "") {
		var a = document.getElementById("dashboard");
		if (a != null) {
			a.classList.add("active");
		}
	} else {
		var a = document.getElementById(localStorage.activeAdminMenu);
		if (a != null) {
			a.classList.add("active");
		}
	}

})();
</script>


</head>

<head>
<script src="js/activeClass.js"></script>

<body>

	<div class="leftbar" id="leftbar">
		<ul class="menulist">

			<li class="nav">
				<a class="menu" href="adminOperation/adminDashboard" id="dashboard" onclick="onClickMenu(this.id)"><i>
					<img src="images/ic-dashboard.png" alt="" /></i> Dashboard
				</a>
			</li>

			<li class="nav"><a class="menu" id="usermanagement" 
				href="adminOperation/userManagement" onclick="onClickMenu(this.id)"><i>
						<img src="images/ic-user_mgmt.png" alt="" />
				</i>Customer Management </a></li>
			
			<li class="nav">
				
				<a class="menu" href="adminOperation/dataPlanManagement" id="dataPlanManagement" onclick="onClickMenu(this.id)"><i>
					<img src="images/ic-usage_report.png" alt="" /></i>Data Plan Management
			</a>
			</li>

			<li class="nav">
				<a class="menu" href="adminOperation/spareDataCollectorPanel" id="spareDC" onclick="onClickMenu(this.id)"><i>
					<img src="images/ic-data_locator.png" alt="" /></i>Allocate DataCollector
				</a>
			</li>

			<li class="nav">
				<a class="menu" href="adminOperation/config" id="portalconfig" onclick="onClickMenu(this.id)"><i>
					<img src="images/ic-controlpanel.png" alt="" /></i>Portal Configuration
				</a>
			</li>
			
			<li class="nav">
				<a class="menu" id="countrymanagement" href="adminOperation/countryManagement" onclick="onClickMenu(this.id)"><i>
					<img src="images/ic-country_locator.png" alt="" /></i>Country Management
				</a>
			</li>
			
			<li class="nav">
				<a class="menu" id="currencymanagement" href="adminOperation/currencyManagement" onclick="onClickMenu(this.id)"><i>
					<img src="images/ic-currancy_mgmt.png" alt="" /></i>Currency Management
				</a>
			</li>
			
			<li class="nav">
				<a class="menu" id="batterymanagement" href="adminOperation/batteryManagement" onclick="onClickMenu(this.id)"><i>
					<img src="images/ic-battery_mgmt.png" alt="" /></i>Battery Management
				</a>
			</li>
			
		</ul>
	</div>

</body>
</html>