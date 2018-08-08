<!DOCTYPE>
<!--  -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>

<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>


<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script>localStorage.path = "${pageContext.request.contextPath}";</script>
<script>
function onClickMenu(clicked_id) {
	//alert('abc:'+clicked_id);
	localStorage.activeMenu = clicked_id;
}

window.onload = (function(){
	if (localStorage.activeMenu == "" || localStorage.activeMenu == "customerDashboard") {
		var a = document.getElementById("customerDashboard");
		if (a != null) {
			a.classList.add("active");
		}
	} else {
    var aDiv = document.getElementById(localStorage.activeMenu);
    var tempDiv = aDiv.parentElement;
    var subMenuDiv = tempDiv.parentElement;
    var dropDownDiv = subMenuDiv.parentElement;

    aDiv.classList.add("active");
    subMenuDiv.style.display='block';
    dropDownDiv.classList.add("selected"); }

})();
</script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>


</head>

<body>

	<div class="leftbar customer" id="leftbar">
		<ul class="menulist">
			<li class="nav"><a class="menu" href="customerDashboard" id="customerDashboard" onclick="onClickMenu(this.id)"><i><img
						src="${pageContext.request.contextPath}/images/ic-dashboard.png" alt="" /></i>
				<p>Customer Dashboard</p></a>
			</li>
					
			<li class="has_dropdown"><a class="menu" href="" id="control" ><i><img
						src="${pageContext.request.contextPath}/images/ic-control.png" alt="" /></i>
				<p>Control</p></a>
				<ul class="sub-menu-nav">
					
					<!-- <li><a class="menu" href="customerProfile" id="customerProfile" title="" onclick="onClickMenu(this.id)">My Profile</a></li> -->
					
					<li><a class="menu" href="consumerUserManagement" id="consumerUserManagement" title="" onclick="onClickMenu(this.id)">Consumer Management</a></li>
					
					<c:if test="${sessionScope.normalCustomer eq null}"> <li><a class="menu" href="normalUserManagement" id="normalUserManagement" title="" onclick="onClickMenu(this.id)">User Management</a></li> </c:if>
					
					<li><a class="menu" href="customerEPManagement" id="customerEPManagement" title="" onclick="onClickMenu(this.id)">Endpoint Management</a></li>
					
					<li><a class="menu" href="siteManagement" id="siteManagement" title="" onclick="onClickMenu(this.id)">Site Management</a></li>
					
					<li><a class="menu" href="dcManagement" id="consumerList" title="" onclick="onClickMenu(this.id)">DataCollector Management</a></li>
					
					<li><a class="menu" href="regionManagement" id="regionmanagement" title="" onclick="onClickMenu(this.id)">Installation Management</a></li>
					
					<li><a class="menu" href="districtUtilityMeter" id="districtutilitymeter" title="" onclick="onClickMenu(this.id)">District Meter Management</a></li>
					
					<li><a class="menu" href="tariffManagement" id="tariffManagement" title="" onclick="onClickMenu(this.id)">Tariff Management</a></li>
					
					<li><a class="menu" href="installerManagement" id="installerManagement" title="" onclick="onClickMenu(this.id)">Installer Management</a></li>
					
					<li><a class="menu" href="technicianManagement" id="technicianManagement" title="" onclick="onClickMenu(this.id)">Technician Management</a></li>
					
					<li><a class="menu" href="schedulerManagement" id="scheduler" title="" onclick="onClickMenu(this.id)">Scheduler</a></li>
				
				</ul>
			</li>
			<li class="has_dropdown"><a class="menu" href="" ><i><img src="${pageContext.request.contextPath}/images/ic-analytics.png" alt="" /></i>
				<p>Analytics</p></a>
				<ul class="sub-menu-nav">
					
					<li><a class="menu" href="networkWaterLossReport" id="networkWaterLossReport" title="" onclick="onClickMenu(this.id)">Network Water Loss Report</a></li>
					
					<li><a class="menu" href="dataUsageReport" id="dataUsageReport" title="" onclick="onClickMenu(this.id)">Data Usage Report</a></li>
					<li><a class="menu" href="networkConsumptionReport" id="networkConsumptionReport" title="" onclick="onClickMenu(this.id)">Network Consumption Report</a></li>
					<li><a class="menu" href="consumptionReport" id="consumptionReport" title="" onclick="onClickMenu(this.id)">Consumption Report</a></li>
					
					<li><a class="menu" href="billingDataReport" id="billingDataReport" title="" onclick="onClickMenu(this.id)">Billing Data Report</a></li>
					
					<li><a class="menu" href="abnormalConsumptionReport" id="abnormalConsumptionReport" title="" onclick="onClickMenu(this.id)">Abnormal Consumption Report</a></li>
					
					<li><a class="menu" href="alertReport" id="alertReport" title="" onclick="onClickMenu(this.id)">Alert Report</a></li>
					<li><a class="menu" href="aggregateConsumptionReport" id="aggregateConsumptionReport" title="" onclick="onClickMenu(this.id)">Aggregate Consumption Report</a></li>
					
					<li><a class="menu" href="freeReport" id="freeReport" title="" onclick="onClickMenu(this.id)">Free Report</a></li>
					
					<li><a class="menu" href="whatIfReport" id="whatIfReport" title="" onclick="onClickMenu(this.id)">Financial What If Report</a></li>
					
				</ul>
			</li>
			
			<li class="has_dropdown"><a class="menu" href="" id="setup" ><i><img
						src="${pageContext.request.contextPath}/images/ic-setup.png" alt="" /></i>
				<p>Setup</p></a>
				<ul class="sub-menu-nav">
					<!-- <li><a class="menu" href="assigninstallerToConsumer" id="assigninstallerToConsumer" title="" onclick="onClickMenu(this.id)">Assign Installer to Endpoint </a></li>
					
					<li><a class="menu" href="assignInstallerToDC" id="assignInstallerToDC" title="" onclick="onClickMenu(this.id)">Assign Installer to DataCollector</a></li> -->
					
					<li><a class="menu" href="installationAndCommissioning" id="installationAndCommissioning" title="" onclick="onClickMenu(this.id)">Installation and Commissioning</a></li>
					<li><a class="menu" href="dcConnectionMap" id="dcConnectionMap" title="" onclick="onClickMenu(this.id)">Connection Map</a></li>
				</ul>
			</li>
			<li class="has_dropdown"><a class="menu" href="" id="maintenance"><i><img
						src="${pageContext.request.contextPath}/images/ic-controlpanel.png" alt="" /></i>
				<p>Maintenance</p></a>
				
				<ul class="sub-menu-nav">
				
					<li><a class="menu" href="assetInspectionSchedule" id="assetInspectionSchedule" title="" onclick="onClickMenu(this.id)">Asset Inspection Schedule</a></li>
				
					<li><a class="menu" href="batteryReplacementSchedule" id="batteryReplacementSchedule" title="" onclick="onClickMenu(this.id)">Battery Replacement Schedule</a></li>
				
					<li><a class="menu" href="consumerMeterAlerts" id="consumerMeterAlerts" title="" onclick="onClickMenu(this.id)">Consumer Meter Alerts</a></li>

					<li><a class="menu" href="dcMessageQueue" id="dcMessageQueue" title="" onclick="onClickMenu(this.id)">Message Queue</a></li>
				
					<li><a class="menu" href="networkAlerts" id="networkAlerts" title="" onclick="onClickMenu(this.id)">Network Alerts</a></li>
					
				
				
				</ul></li>
		</ul>
	</div>
</body>

</html>
