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
<link href="css/dropkick.css" rel="stylesheet" type="text/css" />
<link href="css/toastr.css" rel="stylesheet" type="text/css" />
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->

<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/angular.js"></script>
<script src="js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="js/angular-cookies.js"></script>

<script src="js/dropkick.js" type="text/javascript"></script>
<script src="js/custom.js" type="text/javascript"></script>

<script src="js/angular-ui-router.js"></script>
<script src="js/html5.js"></script>
<script src="js/toastr.js"></script>
<script src="app/app.js"></script>
<script src="app/controller/controller.js"></script>
<script src="app/controller/usercontroller.js"></script>
<script src="app/controller/customercontroller.js"></script>

<base href="/Kenure/">
<script>
	function refreshPage() {
		window.location.reload();
	}
</script>

</head>


<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="logincontroller"
		data-ng-init="getCustomerInstaller(${customerId})">

		<!--wrapper Start-->
		<div id="header" ng-include="'header'"></div>
		<div id="customerleftNavigationbar" data-ng-include src="'customerleftNavigationbar'"></div>
		<div id="content">
			<!--Content Start-->
			<h1>Setup&nbsp;&nbsp;>>&nbsp;&nbsp;Assign Installer to DataCollector</h1>
			<div class="middle">
				
				<div class="status success" ng-show="showMessage">
						<span>{{message}}</span>
				</div>
				<div class="status error" ng-show="showErrorMessage">
						<span>{{errMessage}}</span>
				</div>
				

				<div class="boxpanel">
					<span class="heading">Search DataCollector</span>
					<div class="box-body">


						<div class="filter_panel">
							<form name="IPForm">

							<div class="form-group">
									<label class="control-label">Select Installer
										:</label> <select data-ng-model="selectedInstaller" data-ng-change="getInstallersDC()" class="form-control" required>
										<option value="">--- Please Select Installer ---</option>
										<option data-ng-repeat="x in installerList" value="{{x.id}}"><span data-ng-model="installerName">{{x.name}}</span></option>
									</select>
								</div>

							<div class="form-group">
									<label class="control-label">IP Address</label> <input
										type="text" class="form-control" data-ng-model="dcIp" ng-pattern="/^([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})\.([0-9]{1,3})$/" name="IPAddress">
								</div>
								<div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_search" data-ng-click="searchDataCollectorByIP()">Search</button>
									<button type="reset" onclick="refreshPage()"
										class="default ic_reset">Reset</button>
										
								</div>
								<div class="form-group" data-ng-show="IPForm.IPAddress.$error.pattern">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Please Insert Valid IP address</label>
								</div>
								


							</form>
							</div>
							

						

					</div>
				</div>






				<div>
				<div class="boxpanel" style="float: left;width: 20%;height: 60%;overflow: auto;margin-right: 25px;">
					<span class="heading">DataCollectors Without Installer </span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<th style="padding: 8px 13px;">DataCollector IP</th>
									<th style="padding: 8px 13px;">Action</th>
								</tr>

								<tr data-ng-repeat="x in DCWithoutInstaller">
									<td>{{x.dcIp}}</td>
									<td class="action">
										<input type="checkbox" style="outline: 1px solid #1e5180" data-ng-click="selectMe(x.datacollectorId)"><span></span>
									</td>
								</tr>

							</table>
							
						</div>
					</div>
				</div>
				
				<div class="" style="float: left;height: 60%;overflow: auto;">
					<div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_submit"
										data-ng-click="assignSelectedDCToInstaller()" type="submit">Assign</button>
								</div>
					
				</div>
				
				<div class="boxpanel" style="float: left;width: 25%;height: 60%;overflow: auto;margin-left: 25px;">
					<span class="heading">DataCollectors With Installer</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<th style="padding: 8px 13px;">DataCollector IP</th>

									<!-- <th><a style="color: white" data-ng-click="orderByField='createdDate'; reverseSort = !reverseSort">
          								Created Date 
          							<span data-ng-show="orderByField == 'createdDate'">
          								<span data-ng-show="!reverseSort">^</span>
          								<span data-ng-show="reverseSort">v</span>
          							</span>
          							</a></th> -->
								</tr>

								<tr data-ng-repeat="x in DCWithInstaller">
									<td>{{x.dcIp}}</td>
								
								</tr>

							</table>
							
						</div>
					</div>
				</div>
			</div>

			</div>
			<!--Content end-->
		</div>


		<div id="footer" ng-include="'footer'"></div>
	</div>

</body>
</html>