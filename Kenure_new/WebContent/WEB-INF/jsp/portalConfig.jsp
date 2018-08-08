<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge"/>
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

<script src="js/custom.js" type="text/javascript"></script>
<script src="js/dropkick.js" type="text/javascript"></script>
<script src="js/easy-tabs.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/controller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/usercontroller.js"></script>
<base href="/Kenure/">


</head>


<body data-ng-app="myApp" id="admin">

	<div id="wrapper" data-ng-controller="logincontroller"
	data-ng-init="portalConfig()">
	<!--wrapper Start-->
	<div id="header" data-ng-include data-src="'header'"></div>
	<div id="leftNavigationbar" data-ng-include data-src="'adminleftNavigationbar'"></div>
	
	<div id="content">
		<h1>Portal Configuration</h1>
			<div class="middle">
		<form name="">
				<div class="boxpanel">
					<span class="heading">VPN Configuration</span>
					<div class="boxpanel">
							<div class="two_col">
								<div class="col">
									<div class="form-horizontal">
										
										<div class="form-group">
											<label class="control-label">VPN Server :  </label>
												<input type="text" data-ng-model="vpnServerName" maxlength="40" class="form-control" data-ng-disabled="fieldDisable">
										</div>
										
										<div class="form-group">
											<label class="control-label">User Name :  </label>
												<input type="text" data-ng-model="vpnUserName" maxlength="40" class="form-control" data-ng-disabled="fieldDisable">
										</div>
										
										<div class="form-group">
											<label class="control-label">Password :  </label>
												<input type="password" data-ng-model="vpnPassword" maxlength="40" class="form-control" data-ng-disabled="fieldDisable">
										</div>
										
										<div class="form-group">
											<label class="control-label">VPN Domain :  </label>
												<input type="text" data-ng-model="vpnDomainName" maxlength="40" class="form-control" data-ng-disabled="fieldDisable">
										</div>
										
									
									</div>
								</div>
							</div>
					</div>
			</div>
			<div class="boxpanel">
					<span class="heading">Network Parameters</span>
				<div class="boxpanel">
							<div class="two_col">
								<div class="col">
									<div class="form-horizontal">
										
										<div class="form-group">
											<label class="control-label">Bytes per Endpoint :  </label>
												<input type="text" data-ng-model="noOfBytesPerEndpointRead" maxlength="40" class="form-control" data-ng-disabled="fieldDisable">
										</div>
										
										<div class="form-group">
											<label class="control-label">Bytes :  </label>
												<input type="text" data-ng-model="noOfBytesPerPacket" maxlength="40" class="form-control" data-ng-disabled="fieldDisable">
										</div>
										
										<input type="hidden" class="form-control" data-ng-model="configId" data-ng-disabled="fieldDisable">
										
									</div>
								</div>
							</div>
						</div>
				</div>
				<div class="boxpanel">
					<span class="heading">Other Setting</span>
					<div class="boxpanel">
						<div class="two_col">
							<div class="col">
								<div class="form-horizontal">
									<div class="form-group">
										<label class="control-label">Abnormal Threshold (in %) :  </label>
										<input type="text" class="form-control" data-ng-model="abnormalThreshold" data-ng-disabled="fieldDisable">
									</div>
								
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="boxpanel">
					<div class="two_col">
						<div class="col">
							<div class="form-horizontal">
								<div class="form-group">
									<label class="control-label">&nbsp;</label>
										<button class="default ic_reset" data-ng-click="unableFields()" data-ng-show="updateEnable">Update</button>
										<button type="submit" class="primary ic_save" data-ng-click="saveUpdatePortalConfig()" data-ng-show="saveEnable">Save</button>
								</div>
							</div>
						</div>
					</div>
				</div>
		</form>
		</div>
	</div>
	

	<div id="" data-ng-include data-src="'footer'"></div>
</div>

</body>
</html>

