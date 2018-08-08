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
<link href="${pageContext.request.contextPath}/css/toastr.css" rel="stylesheet" type="text/css" />
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->

<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>
<script src="${pageContext.request.contextPath}/js/angularjs-dropdown-multiselect.js"></script>

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/controller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/usercontroller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/customercontroller.js"></script>

<style type="text/css">
.dcStyle {
	height: 150px;
}
</style>

</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="customercontroller"
		data-ng-init="initCustomerSiteData()">
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'../header'"></div>

		<div id="customerleftNavigationbar" data-ng-include data-src="'customerleftNavigationbar'"></div>
		<div id="content">
			<!--Content Start-->
			<h1>
				Control&nbsp;&nbsp;>>&nbsp;&nbsp;Site Management
				<div class="rightbtns">
					<a href="siteManagement" class="primary ic_back">Back</a>
				</div>
			</h1>
			<div class="middle">
			
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>
			
				<div class="boxpanel">
					<span class="heading">Add New Site</span>
					
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label">Site ID:</label> <input
												type="text" class="form-control"
												data-ng-model="addSite.siteId"
												 disabled>
										</div>
										<div class="form-group">
											<label class="control-label required">Site Name:</label> <input
												type="text" class="form-control"
												data-ng-model="addSite.siteName"
												 required autofocus="autofocus">
										</div>
										<!-- <div class="form-group">
										<label class="control-label required">Password:</label> <input
											type="text" class="form-control" data-ng-model="userdetails.password" value="userdetails.password">
									</div> -->
										<div class="form-group">
											<label class="control-label required">Installation Name:</label> <select
												class="form-control " data-ng-model="addSite.selectedRegion"
												data-ng-options="x for x in regionNames"></select>
										</div>

										<div class="form-group">
											<label class="control-label required">Boundary DataCollector:</label>
											<select class="form-control dcStyle "
												data-ng-model="addSite.boundryDC"
												data-ng-options="x for x in boundryCollector" multiple
												size="4"></select>
										</div>

										<div class="form-group">
											<label class="control-label required">Assign DataCollector:</label>
											<select class="form-control dcStyle "
												data-ng-model="addSite.assignDC"
												data-ng-options="x for x in assignCollector" multiple
												size="4"></select>
										</div>
										<div class="form-group">
											<label class="control-label">Assigned
												Installer:</label> <select class="form-control"
												data-ng-model="addSite.installerName"
												data-ng-options="x for x in installerName"></select>
										</div>
										<div class="form-group">

											<label class="control-label"> </label> <input type="checkbox"
												data-ng-model="addSite.isChecked" style="outline: 1px solid #1e5180"> <span>are
												district meter being used ?</span> <br />
										</div>
										<div class="form-group" data-ng-show="addSite.isChecked">
											<label class="control-label required">District Meter
												Serial Number:</label> <select class="form-control "
												data-ng-model="addSite.districtMSN"
												data-ng-options="x for x in districtUMList"
												></select>
										</div>
										
										<div class="form-group" data-ng-show="isUndefined" data-ng-model="isUndefined">
										<label class="control-label">&nbsp;</label>
										<div class="status error"><span>Fill Proper DropDown.</span></div>
										</div>
										
										<label class="control-label">&nbsp;</label>
										<button class="primary ic_submit"
											data-ng-click="addNewSite()" data-ng-model="updateProfile"
											type="submit">Add Site</button>

									</div>

								</form>

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

