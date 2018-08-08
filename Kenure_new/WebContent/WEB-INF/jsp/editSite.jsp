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
<script src="${pageContext.request.contextPath}/js/angularjs-dropdown-multiselect.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

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
		data-ng-init="editSiteData()">
		<!--wrapper Start-->
		<div id="header" data-ng-include data-src="'../header'"></div>

		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>
		<div id="content">
			<!--Content Start-->
			<h1>
				Control&nbsp;&nbsp;>>&nbsp;&nbsp;Site
				Management
				<div class="rightbtns">
					<a href="siteManagement" class="primary ic_back">Back</a>
				</div>
			</h1>
			<div class="middle">
			
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>
			
				<div class="boxpanel">
					<span class="heading">Edit Site</span>
					
					<div class="box-body pad">

						<div class="two_col">
							<div class="col">
								<form method="post">
									<div class="form-horizontal">
										<div class="form-group">
											<label class="control-label required">Site ID:</label> <input
												type="text" class="form-control" data-ng-model="siteId"
												disabled>
										</div>
										<div class="form-group">
											<label class="control-label required">Site Name:</label> <input
												type="text" class="form-control" data-ng-model="siteName"
												required>
										</div>
										<!-- <div class="form-group">
										<label class="control-label required">Password:</label> <input
											type="text" class="form-control" data-ng-model="userdetails.password" value="userdetails.password">
									</div> -->
										<div class="form-group">
											<label class="control-label required">Installation
												Name:</label> <select class="form-control "
												data-ng-model="selectedRegion"
												data-ng-options="x for x in regionNames" required="required"></select>
										</div>

										<div class="form-group">
											<label class="control-label">Boundary DataCollector:</label>
											<select class="form-control dcStyle "
												data-ng-model="boundryDC" multiple="multiple" size="4">
												<option
													data-ng-repeat="x in boundryCollector track by $index"
													value="{{x}}">{{x}}</option>
											</select>
										</div>

										<div class="form-group">
											<label class="control-label ">Assign DataCollector:</label>
											<select class="form-control dcStyle "
												data-ng-model="assignDC"
												data-ng-options="x for x in assignCollector" multiple
												size="4" data-ng-change="toggleInstaller()"></select>
										</div>
										<div class="form-group">
											<label class="control-label">Assigned
												Installer:</label> <select class="form-control "
												data-ng-model="selectedInstaller"
												data-ng-options="x for x in installerName" data-ng-disabled="!isInstaller"></select>
										</div>
										<!--	<div class="form-group">

											<label class="control-label"> </label> <input type="checkbox"
												data-ng-model="isChecked" style="outline: 1px solid #1e5180"> <span>are
												district meter being used ?</span> <br />
										</div>
										<div class="form-group" data-ng-show="isChecked">
											<label class="control-label required">District Meter
												Serial Number:</label> <select class="form-control "
												data-ng-model="installerName"
												data-ng-options="x for x in districtUMList"
												></select>
										</div> -->

										<div class="form-group" data-ng-show="isUndefined"
											data-ng-model="isUndefined">
											<label class="control-label">&nbsp;</label>
											<div class="status error">
												<span>Fill Proper DropDown.</span>
											</div>
										</div>
										
										<div class="form-group">
											<label class="control-label">Status :</label>
											<select class="form-control" data-ng-model="status"
											 data-ng-options="item for item in ['Active', 'Inactive']" required="required">
												
												
											</select>
										</div>
										
										<label class="control-label">&nbsp;</label>
										<button class="primary ic_submit"
											data-ng-click="editCurrentSite(siteId)"
											data-ng-model="updateProfile" type="submit">Edit
											Site</button>

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

