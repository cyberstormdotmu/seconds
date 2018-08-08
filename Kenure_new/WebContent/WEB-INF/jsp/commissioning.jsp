<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/sweetalert.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->


<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/easy-tabs.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/sweetalert.min.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>

<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>

<script src="${pageContext.request.contextPath}/app/controller/commissioningcontroller.js"></script>


<script type="text/javascript">
 angular.module('myApp').requires.push('ngAnimate', 'ngSanitize','ui.bootstrap');
</script>
</head>

<body data-ng-app="myApp">
	<div id="wrapper" data-ng-controller="commissioningcontroller"
		data-ng-init="initCommissioningPage(${selectedSiteId})">


		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>
		
		<div id="content">
	<!--Content Start-->
	<h1>Current Status</h1>
	<div class="middle">	
		<span class="pull-right"><a href="" data-ng-click="redirectToVerficationPage()">Verification</a></span>
		<div class="filter_panel" style="background-color: white;">
			    <ul class="customer-details">
			      <li><span>Customer Code <i>:</i></span>{{custCode}}</li>
			      <li><span>Installation <i>:</i></span>{{regionName}}</li>
			      <li><span>Site Name <i>:</i></span>{{siteName}}</li>
			      <li><span>Site ID <i>:</i></span>{{siteID}}</li>
			      <li><span>Status <i>:</i></span>{{status}}</li>
			    </ul>
			    <ul class="customer-details">
			      <li><span>Number of DataCollectors <i>:</i></span>{{totalDC}}</li>
			      <li><span>Number of End Points <i>:</i></span>0</li>
			      <li><span>Number of Repeaters <i>:</i></span>0</li>
			    </ul>
  			</div> <br>
			
			<div class="resp-tabs-container ver_1" data-ng-if="!toggleCommissioningType">
				
				<div class="boxpanel">
					
					<span class="heading"><h1>Scheduled Commissioning</h1></span>
					<br>
					
					<span class="enable" data-ng-if="tag > 10" style="margin-right: 10px;">10</span>
					<span class="disable" data-ng-if="tag == 10" style="margin-right: 10px;">10</span>
					
					<div style="text-align: center;">
				
						<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The Scheduled Commissioning run will be completed by 25/11/2016 4:00 p.m.</p><br>
						<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Current Status : Waiting for Commissioning to Start/Finish</p><br>
				</div>
				</div>
				
			</div>
		
			<div class="resp-tabs-container ver_1" data-ng-if="toggleCommissioningType">
				
				<div class="boxpanel">
					<span class="heading"><h1>Manual Commissioning</h1></span>
						
					<br><h4><p>&nbsp;&nbsp;&nbsp;<b>Level - 1 Commissioning</b></p></h4><br>
					
					<div style="text-align: center;">
					<span class="enable" data-ng-if="tag > 10" style="margin-right: 10px;">10</span>
					<span class="disable" data-ng-if="tag == 10" style="margin-right: 10px;">10</span>
					<div class="form-group" >
						<button type="button" data-ng-click="startLevel1Commissioning()" style="
							width: 50%;
							height: 30px;
							color: white;
							background-color: slategray;" data-ng-disabled="tag!=10">Start Level - 1 Commissioning</button>
						<br><br>
						<p data-ng-if="tag<=11">Level - 1 Commissioning started on 25/11/2016 10:00 am. Please refresh this page after few minutes! and Inform installers to start scan on all endpoints.</p>
						<br><br>
					</div>
					<div class="boxpanel" data-ng-if="tag>10 && tag<=13">
						<div class="installed-datacollectors">
							<div class="installed-box" style="width: 100%">
								<div class="border-box">
									<div class="box-body">
										<div class="grid-content">
											<table class="grid">
												<tr>
													<th><a style="color: white"
														data-ng-click="orderByField1='dcSerialNumber'; reverseSort1 = !reverseSort1">
															DC Serial Number <span
															data-ng-show="orderByField1 == 'dcSerialNumber'"> <span
																data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='dcIp'; reverseSort1 = !reverseSort1">
															IP Address <span
															data-ng-show="orderByField1 == 'dcIp'"> <span
																data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='isLevel1CommStarted'; reverseSort1 = !reverseSort1">
															Level-1 Commissioning Started? 
															<span data-ng-show="orderByField1 == 'isLevel1CommStarted'">
																<span data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
												</tr>
												<tr data-ng-repeat="x in level1DatacollectorsList">
													<td>{{x.dcSerialNumber}}</td>
													<td>{{x.dcIp}}</td>
													<td>{{x.isLevel1CommStarted}}</td>
												</tr>
											</table>

										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				<span class="enable" data-ng-if="tag > 11" style="margin-right: 10px;">11</span>
					<span class="disable" data-ng-if="tag == 11" style="margin-right: 10px;">11</span>
					<div class="form-group">
						<input type="checkbox"
							name="Scheduled" value="option1"
							data-ng-click="changeTag()" data-ng-checked="isCommCheckboxSelected" data-ng-disabled="commCheckboxDisable"><span></span><label>Check this CheckBox when Level-1 Commissioning is Successfully Started</label>
						<br><br>
					</div>
				</div>	
					
				<br><h4><p>&nbsp;&nbsp;&nbsp;<b data-ng-if="tag==12">Level - N Commissioning</b></p></h4><br>
					
					<div style="text-align: center;">
					<span class="enable" data-ng-if="tag > 12" style="margin-right: 10px;">12</span>
					<span class="disable" data-ng-if="tag == 12" style="margin-right: 10px;">12</span>
					
					<div class="form-group">
						<button type="button" data-ng-click="startLevelNCommissioning()" style="
							width: 50%;
							height: 30px;
							color: white;
							background-color: slategray;" data-ng-disabled="tag!=12">Start Level - N Commissioning</button>
						<br><br>
						<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; The Level - n Commissioning run will be completed by 25/11/2016 4:00 p.m.</p><br>
						<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Current Status : Waiting for Level - N Commissioning to Start/Finish</p><br>
						<br><br>
					
					</div>	
					<div class="boxpanel" data-ng-if="tag==13">
						<div class="installed-datacollectors">
							<div class="installed-box" style="width: 100%">
								<div class="border-box">
									<div class="box-body">
										<div class="grid-content">
											<table class="grid">
												<tr>
													<th><a style="color: white"
														data-ng-click="orderByField1='dcSerialNumber'; reverseSort1 = !reverseSort1">
															DC Serial Number <span
															data-ng-show="orderByField1 == 'dcSerialNumber'"> <span
																data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='dcIp'; reverseSort1 = !reverseSort1">
															IP Address <span
															data-ng-show="orderByField1 == 'dcIp'"> <span
																data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
													<th><a style="color: white"
														data-ng-click="orderByField1='isLevelnCommStarted'; reverseSort1 = !reverseSort1">
															Level-N Commissioning Started? 
															<span data-ng-show="orderByField1 == 'isLevelnCommStarted'">
																<span data-ng-show="!reverseSort1"><img
																	src="images/up-ar.png" alt="" /></span> <span
																data-ng-show="reverseSort1"><img
																	src="images/down-ar.png" alt="" /></span>

														</span>
													</a></th>
												</tr>
												<tr data-ng-repeat="x in levelNDatacollectorsList">
													<td>{{x.dcSerialNumber}}</td>
													<td>{{x.dcIp}}</td>
													<td>{{x.isLevelnCommStarted}}</td>
												</tr>
											</table>

										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					</div>
				</div>
			</div>
			
	</div>
	</div>		
			
			
	</div>
</body>
</html>		
			