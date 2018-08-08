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
			<h1>Setup&nbsp;&nbsp;>>&nbsp;&nbsp;Assign Installer to Endpoint</h1>
			<div class="middle">
				
				<div class="status success" ng-show="showMessage">
						<span>{{message}}</span>
				</div>
				<div class="status error" ng-show="showErrorMessage">
						<span>{{errMessage}}</span>
				</div>
				

				<div class="boxpanel">
					<span class="heading">Search Endpoint</span>
					<div class="box-body">


						<div class="filter_panel">
							<form>

							

								<div class="form-group">
									<label class="control-label">Select Installer
										:</label> <select data-ng-model="selectedInstaller" data-ng-change="getInstallersConsumer()" class="form-control" required>
										<option value="">--- Please Select Installer ---</option>
										<option data-ng-repeat="x in installerList" value="{{x.id}}"><span ng-model="installerName">{{x.name}}</span></option>
									</select>
								</div>

								<!-- <div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_submit"
										data-ng-click="assignConsumertoInstaller()" type="submit">Assign</button>
								</div>
 -->

							</form>

							<form name="endpointform">

								<div class="form-group">
									<label class="control-label">Endpoint Register Id </label> <input
										type="text" class="form-control" data-ng-model="streetName" data-ng-pattern="/^[1-9]+[0-9]*$/" name="registerId"
										value="{{streetName}}" required>

								</div>
								<div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_search"
										data-ng-click="searchConsumerByStreetName(${customerId})"
										type="submit">search</button>

								</div>
								
								<div class="form-group" data-ng-show="endpointform.registerId.$error.pattern">
											<label class="control-label">&nbsp;</label>
											<label class="form-control status error">Please Insert Valid Register Id</label>
								</div>
							</form>


						</div>

					</div>
				</div>






				<div>
				<div class="boxpanel" style="float: left;width: 20%;height: 60%;overflow: auto;margin-right: 25px;">
					<span class="heading">Endpoints Without Installer </span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<th style="padding: 8px 13px;">Register Id</th>
									<th style="padding: 8px 13px;"><!-- <button ng-click="selectAllFun()" class="primary ic_submit">Select All</button> -->Action</th>
								</tr>

								<tr data-ng-repeat="x in consumerWithoutInstaller">
									<td>{{x.acc_no}}</td>
									<td>
									<input type="checkbox" style="outline: 1px solid #1e5180" data-ng-click="selectMe(x.meter_id)" ng-checked="selectAll"><span></span>
									<!-- <a href="" title="check"> <img
											alt="" src="images/ic-success.png"
											>
											
									</a>&nbsp; -->
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
										data-ng-click="assignConsumertoInstaller()" type="submit">Assign</button>
								</div>
					
				</div>
				
				<div class="boxpanel" style="float: left;width: 25%;height: 60%;overflow: auto;margin-left: 25px;">
					<span class="heading">Endpoints With Installer</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<th style="padding: 8px 13px;">Endpoint Register Id</th>

									<!-- <th><a style="color: white" data-ng-click="orderByField='createdDate'; reverseSort = !reverseSort">
          								Created Date 
          							<span data-ng-show="orderByField == 'createdDate'">
          								<span data-ng-show="!reverseSort">^</span>
          								<span data-ng-show="reverseSort">v</span>
          							</span>
          							</a></th> -->
								</tr>

								<tr data-ng-repeat="x in consumerWithInstaller">
									<td>{{x.acc_no}}</td>
								
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





