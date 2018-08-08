<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<!-- <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css"/> -->
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/sweetalert.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->


<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/easy-tabs.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/sweetalert.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.popupoverlay.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>

<script src="${pageContext.request.contextPath}/app/controller/commissioningcontroller.js"></script>

<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>
<script>
	$(function() {
		$('#fadeandscale').popup();
	});
</script>


<script type="text/javascript">
	angular.module('myApp').requires.push('ui.bootstrap');
</script>
</head>


<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="commissioningcontroller"
		data-ng-init="initInstallationAndCommissioningData()">


		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>Setup&nbsp;&nbsp;>>&nbsp;&nbsp;Installation and Commissioning</h1>
			<div class="middle">
				<div class="boxpanel">
					<div class="installed-datacollectors">
						<div class="installed-box">
							<div class="title-hed">

								<span class="heading">Installed Datacollectors (25) </span>
								<div id="" class="optopn-box">
									<a href="" class="toggle"><span></span></a>
									<ul class="optopn-link">
										<li>
					                        <ul>
					                          <li><a href=""  data-ng-click="startContinueInstallation()">start/Continue Installation</a></li>
					                        </ul>
					                    </li>
					                    <li>
					                    	<ul>
					                        	<li><a href="#" data-ng-if="setOperatingMode">Set Operating Mode</a></li>
					                         	<li><a href="" data-ng-if="viewNetworkMap" data-ng-click="redirectToNetworkMap()" target="_blank">View Network Map</a></li>
				  		                        <li><a href="#" data-ng-if="checkDCConnectivity">Check DC Connectivity</a></li>
					                        </ul> 
					                     </li>
					                     <li>
					                       <ul>
					                         <li><a href="regionManagement" target="_blank">Add/Edit Installation</a></li>
					                         <li><a href="siteManagement" target="_blank">Add/Edit Site</a></li>     
					                         <li><a href="dcManagement" target="_blank">Assign/Configure DC</a></li>
					                         <li><a href="customerEPManagement" target="_blank">Add Endpoints</a></li>
					                      </ul>
					                    </li>
									</ul>
								</div>
							</div>
							<div class="border-box">
								<ul class="install-checkbox">
									<li><label class="checkbox"><input type="checkbox"><span></span>pre-install
											(2)</label></li>
									<li><label class="checkbox"><input type="checkbox"><span></span>Commissioning
											(1) </label></li>
									<li><label class="checkbox"><input type="checkbox"><span></span>Normal
											(22)</label></li>
									<li><label class="checkbox"><input type="checkbox"><span></span>Normalconfig
											(0)</label></li>
								</ul>
								<!-- <ul class="check-list">
									<li><label class="checkbox"><input type="checkbox"><span></span>Auto</label></li>
									<li><label class="checkbox"><input type="checkbox"><span></span>Auto</label></li>
									<li><label class="checkbox"><input type="checkbox"><span></span>Auto</label></li>
									<li><label class="checkbox"><input type="checkbox"><span></span>Auto</label></li>
									<li><label class="checkbox"><input type="checkbox"><span></span>Auto</label></li>
								</ul> -->

								<ul class="check-list" data-ng-repeat="x in regionList track by $index" data-ng-if="x.siteList.length >= 1">

									<li>
										<label class="checkbox">
											 <input type="radio" name="region" data-ng-click="showHideSites(x.regionId)" id="{{x.regionId}}"><span></span>Installation Name : {{x.regionName}}
										</label>
									</li>

									<ul data-ng-repeat="y in x.siteList track by $index" data-ng-show="x.regionId==selectedRegionid">

										<li>
											<label class="checkbox" style="margin-left: 40px">
												<input type="radio" name="site" data-ng-click="showHideDc(y.siteId,y.dc)" id="{{y.siteId}}"><span></span>Site Name : {{y.siteName}}
											</label>
										</li>

										<ul data-ng-repeat="z in y.dc track by $index" data-ng-show="y.siteId==selectedSiteid">

											<li>
												<label class="checkbox"  style="margin-left: 80px">
													<input type="checkbox" data-ng-checked="selectAllDc" data-ng-click="pushOrPopDcSerialNo(z.dcSerialNumber)"><span></span>DataCollector Serial Number : {{z.dcSerialNumber}}
												</label>
											</li>
										</ul>
									</ul>
								</ul>
							</div>
						</div>
						<!-- ================================================= -->

						<div class="installed-box">
							<div class="title-hed">
								<span class="heading">Installation in progress</span>
								<!-- <div class="optopn-box">
									<a href="" class="toggle"><span></span></a>
									<ul class="optopn-link">
										<li><a href="">Option 1</a></li>
										<li><a href="">Option 2</a></li>
										<li><a href="">Option 3</a></li>
										<li><a href="#">Set Operating Mode</a></li>
										<li><a href="#">View Network Map</a></li>
										<li><a href="#">Check DC Connectivity</a></li>
										<li><a href="">Add/Edit Installation</a></li>
										<li><a href="">Add/Edit Site</a></li>
										<li><a href="">Assign/Configure DC</a></li>
										<li><a href="">Add Endpoints</a></li>
										<li><a href="">Add Repeaters</a></li>
									</ul>
								</div> -->
							</div>

							<div class="border-box">
								<div class="box-body">
									<div class="grid-content">
										<table class="grid">
											<tr>
												<!-- <th class="checkbox-col"><label class="checkbox"><input
														type="checkbox"><span></span></label></th> -->
												<th><a style="color: white"
													data-ng-click="orderByField1='siteId'; reverseSort1 = !reverseSort1">
														Site ID <span data-ng-show="orderByField1 == 'siteId'">

														<span data-ng-show="!reverseSort1" ><img src="${pageContext.request.contextPath}/images/up-ar.png" alt="" /></span>
														<span data-ng-show="reverseSort1" ><img src="${pageContext.request.contextPath}/images/down-ar.png" alt="" /></span>

													</span>
												</a></th>
												<th><a style="color: white"
													data-ng-click="orderByField1='currentStatus'; reverseSort1 = !reverseSort1">
														Current Status <span data-ng-show="orderByField1 == 'currentStatus'">

														<span data-ng-show="!reverseSort1" ><img src="${pageContext.request.contextPath}/images/up-ar.png" alt="" /></span>
														<span data-ng-show="reverseSort1" ><img src="${pageContext.request.contextPath}/images/down-ar.png" alt="" /></span>

													</span>
												</a></th>
												<th><a style="color: white"
													data-ng-click="orderByField1='commissioningStartTime'; reverseSort1 = !reverseSort1">
														Commissioning Start Time <span data-ng-show="orderByField1 == 'commissioningStartTime'">

														<span data-ng-show="!reverseSort1" ><img src="${pageContext.request.contextPath}/images/up-ar.png" alt="" /></span>
														<span data-ng-show="reverseSort1" ><img src="${pageContext.request.contextPath}/images/down-ar.png" alt="" /></span>

													</span>
												</a></th>
											</tr>
											<tr data-ng-repeat="x in installationList | orderBy:orderByField1:reverseSort1">
												<!-- <td class="checkbox-col"><label class="checkbox"><input
														type="checkbox"><span></span></label></td> -->
												<td>{{x.siteId}}</td>
												<td>{{x.currentStatus}}</td>
												<td>{{x.commissioningStartTime}}</td>
											</tr>
										</table>

									</div>
								</div>
							</div>

						</div>
						<!-- ================================================= -->

						<div class="installed-box">
							<div class="title-hed">
								<span class="heading">Spare DataCollectors :
									{{totalSpareDC}}</span>
								<!-- <div class="optopn-box">
									<a href="" class="toggle"><span></span></a>
									<ul class="optopn-link">
										<li><a href="">Option 1</a></li>
										<li><a href="">Option 2</a></li>
										<li><a href="">Option 3</a></li>
										<li><a href="#">Set Operating Mode</a></li>
										<li><a href="#">View Network Map</a></li>
										<li><a href="#">Check DC Connectivity</a></li>
										<li><a href="">Add/Edit Installation</a></li>
										<li><a href="">Add/Edit Site</a></li>
										<li><a href="">Assign/Configure DC</a></li>
										<li><a href="">Add Endpoints</a></li>
										<li><a href="">Add Repeaters</a></li>
									</ul>
								</div> -->
							</div>

							<div class="border-box">
								<div class="box-body">
									<div class="grid-content">
										<table class="grid">
											<tr>
												<!-- <th class="checkbox-col">
													<label class="checkbox">
														<input type="checkbox" data-ng-click="selectAllCheckBox()"><span></span></label>
												</th> -->

												<th><a style="color: white"
													data-ng-click="orderByField='dcSerialNumber'; reverseSort = !reverseSort">
														DC Serial Number <span data-ng-show="orderByField == 'dcSerialNumber'">

														<span data-ng-show="!reverseSort" ><img src="${pageContext.request.contextPath}/images/up-ar.png" alt="" /></span>
														<span data-ng-show="reverseSort" ><img src="${pageContext.request.contextPath}/images/down-ar.png" alt="" /></span>

													</span>
												</a></th>
												<th><a style="color: white"
													data-ng-click="orderByField='dcIp'; reverseSort = !reverseSort">
														IP Address <span data-ng-show="orderByField == 'dcIp'">
															<span data-ng-show="!reverseSort" ><img src="${pageContext.request.contextPath}/images/up-ar.png" alt="" /></span>
														<span data-ng-show="reverseSort" ><img src="${pageContext.request.contextPath}/images/down-ar.png" alt="" /></span>
													</span>
												</a></th>
											</tr>
											<tr	data-ng-repeat="x in dcList | orderBy:orderByField:reverseSort track by $index">
												<!-- <td class="checkbox-col">
													<label class="checkbox">
														<input type="checkbox" data-ng-model="default.check"
															data-ng-change="pushOrPopDc(x.datacollectorId)" data-ng-checked="selectAll"><span></span>
													</label>
												</td> -->
												<td>{{x.dcSerialNumber}}</td>
												<td>{{x.dcIp}}</td>
											</tr>

										</table>

									</div>
								</div>
							</div>

						</div>
						<!-- ================================================= -->


					</div>
				</div>

			</div>
			<!--Content end-->
		</div>


		<div id="footer">
			<!--footer start-->
			<p class="copyright">
				&copy; 2016 Blu Tower.&nbsp;&nbsp;<a href="#">Privacy</a><span>|</span><a
					href="#">Terms</a>
			</p>
			<!--footer end-->
		</div>
	</div>

</body>
</html>