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
<script src="${pageContext.request.contextPath}/js/jquery.popupoverlay.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>

<script src="${pageContext.request.contextPath}/app/controller/maintenancecontroller.js"></script>

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

	<div id="wrapper" data-ng-controller="maintenancecontroller"
		data-ng-init="initDCMessageQueueData()">


		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>Maintenance&nbsp;&nbsp;>>&nbsp;&nbsp;DC Message Queue</h1>
			<div class="middle">
			
				<div class="boxpanel">
					<span class="heading">Search</span>
					<div class="box-body">
						<form>
							<div class="filter_panel">
								
								<div class="form-group">
									<label class="control-label">Installation Name</label>
									<select	class="form-control" data-ng-model="selectedRegion" data-ng-change="getSiteList()" autofocus="autofocus">
										<option selected="selected" value="">-- Select Installation --</option>
										<option data-ng-repeat="x in regionList" value="{{x.regionId}}">{{x.regionName}}</option>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label">Site ID</label>
									<select	class="form-control" data-ng-model="selectedSite" data-ng-disabled="regionSelected" data-ng-change="getDCList()">
										<option data-ng-repeat="x in siteList" value="{{x.siteId}}">{{x.siteName}}</option>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label">DC Serial Number</label>
									<select	class="form-control" data-ng-model="selectedDc" data-ng-disabled="siteSelected" data-ng-change="getMessageList()">
										<option data-ng-repeat="x in dcList" value="{{x.datacollectorId}}">{{x.dcSerialNumber}}</option>
									</select>
								</div>
								
							</div>
						</form>
					</div>
				</div>
			
			
			
				<div class="boxpanel">
					<div class="installed-datacollectors" data-ng-show="messageQueueListShow">
						<!-- ================================================= -->

						<div class="installed-box" style="width: 50%">
							<div class="title-hed">
								<span class="heading">Portal to DC queue</span>
								<div class="optopn-box">
									<a href="" class="toggle"><span></span></a>
									<ul class="optopn-link">
										<li><a href="">Delete Message</a></li>
									</ul>
								</div>
							</div>

							<div class="border-box">
								<div class="box-body">
									<div class="grid-content">
										<table class="grid">
											<tr>
												<th class="checkbox-col"><label class="checkbox"><input
														type="checkbox"><span></span></label></th>
												<th>Position</th>
												<th>Register ID</th>
												<th>Type</th>
												<th>Value</th>
												<th>Time Added</th>
											</tr>
											<tr data-ng-repeat="x in messageQueueList track by $index">
												<td class="checkbox-col"><label class="checkbox"><input
														type="checkbox"><span></span></label></td>
												<td style="color: {{x.messageColor}};">{{$index+1}}</td>
												<td style="color: {{x.messageColor}};">{{x.registerId}}</td>
												<td style="color: {{x.messageColor}};">{{x.type}}</td>
												<td style="color: {{x.messageColor}};">{{x.value}}</td>
												<td style="color: {{x.messageColor}};">{{x.timeAdded}}</td>
											</tr>
										</table>

									</div>
								</div>
							</div>

						</div>
						<!-- ================================================= -->

						<div class="installed-box" style="width: 50%">
							<div class="title-hed">
								<span class="heading">DC to EendPoint queue</span>
								<div class="optopn-box">
									<a href="" class="toggle"><span></span></a>
									<ul class="optopn-link">
										<li><a href="">Delete Message</a></li>
									</ul>
								</div>
							</div>

							<div class="border-box">
								<div class="box-body">
									<div class="grid-content">
										<table class="grid">
											<tr>
												<th class="checkbox-col"><label class="checkbox"><input
														type="checkbox"><span></span></label></th>
												<th>Command Index</th>
												<th>Register ID</th>
												<th>Type</th>
												<th>Value</th>
												<th>Time Added</th>
											</tr>
											<tr data-ng-repeat="x in messageQueueList track by $index" data-ng-if="x.messageColor == '#e67300'">
												<td   class="checkbox-col"><label class="checkbox">
													<input	type="checkbox"><span></span></label>
												</td>
												<td style="color: {{x.messageColor}};">{{$index+1}}</td>
												<td style="color: {{x.messageColor}};">{{x.registerId}}</td>
												<td style="color: {{x.messageColor}};">{{x.type}}</td>
												<td style="color: {{x.messageColor}};">{{x.value}}</td>
												<td style="color: {{x.messageColor}};">{{x.timeAdded}}</td>
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