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
		data-ng-init="initVerification(${selectedSiteId})">
		
		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>
		
		<div id="content">
	<!--Content Start-->
	<h1>Current Status</h1>
	<div class="middle">	
			
		<div class="resp-tabs-container ver_1">

    <ul class="customer-details">
      <li><span>Customer Code <i>:</i></span> 123</li>
      <li><span>Installation <i>:</i></span>Aldershot</li>
      <li><span>SiteName <i>:</i></span>Northtown</li>
      <li><span>Site ID <i>:</i></span>1234</li>
      <li><span>Status <i>:</i></span>Commissioning 8</li>
    </ul>

    <ul class="customer-details">
      <li><span>NUmber of DataCollectorsvc <i>:</i></span>2</li>
      <li><span>Number of Endpoints <i>:</i></span>349</li>
      <li><span>Number of repeaters <i>:</i></span> 1</li>
    </ul>
  </div> 	
			
		<p>
			<div class="resp-tabs-container ver_1">
				<div class="form-group">
				<span class="heading"><h1>Missing End Points</h1></span><br>

				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button type="button" data-ng-click="" data-ng-model="" style="
							width: 50%;
							height: 30px;
							color: white;
							background-color: slategray;">Button Name</button>
				<span class="enable">13</span>
				</div>
							
				
				<div class="installed-box" style="width: 100%">
					<div class="border-box">
						<div class="box-body">
							<div class="grid-content">
								<table class="grid">
									<tr>
										<th><a style="color: white"
											data-ng-click="orderByField1='fileName'; reverseSort1 = !reverseSort1">
												Register ID <span data-ng-show="orderByField1 == 'fileName'">

													<span data-ng-show="!reverseSort1"><img
														src="images/up-ar.png" alt="" /></span> <span
													data-ng-show="reverseSort1"><img
														src="images/down-ar.png" alt="" /></span>

											</span>
										</a></th>
										<th><a style="color: white"
											data-ng-click="orderByField1='fileName'; reverseSort1 = !reverseSort1">
												Address 1 <span data-ng-show="orderByField1 == 'fileName'">

													<span data-ng-show="!reverseSort1"><img
														src="images/up-ar.png" alt="" /></span> <span
													data-ng-show="reverseSort1"><img
														src="images/down-ar.png" alt="" /></span>

											</span>
										</a></th>
										<th><a style="color: white"
											data-ng-click="orderByField1='fileName'; reverseSort1 = !reverseSort1">
												Address 2 <span data-ng-show="orderByField1 == 'fileName'">

													<span data-ng-show="!reverseSort1"><img
														src="images/up-ar.png" alt="" /></span> <span
													data-ng-show="reverseSort1"><img
														src="images/down-ar.png" alt="" /></span>

											</span>
										</a></th>
										<th><a style="color: white"
											data-ng-click="orderByField1='fileName'; reverseSort1 = !reverseSort1">
												Address 3 <span data-ng-show="orderByField1 == 'fileName'">

													<span data-ng-show="!reverseSort1"><img
														src="images/up-ar.png" alt="" /></span> <span
													data-ng-show="reverseSort1"><img
														src="images/down-ar.png" alt="" /></span>

											</span>
										</a></th>
										<th><a style="color: white"
											data-ng-click="orderByField1='fileName'; reverseSort1 = !reverseSort1">
												Street Name <span data-ng-show="orderByField1 == 'fileName'">

													<span data-ng-show="!reverseSort1"><img
														src="images/up-ar.png" alt="" /></span> <span
													data-ng-show="reverseSort1"><img
														src="images/down-ar.png" alt="" /></span>

											</span>
										</a></th>
										<th><a style="color: white"
											data-ng-click="orderByField1='installerName'; reverseSort1 = !reverseSort1">
												Zipcode <span
												data-ng-show="orderByField1 == 'installerName'"> <span
													data-ng-show="!reverseSort1"><img
														src="images/up-ar.png" alt="" /></span> <span
													data-ng-show="reverseSort1"><img
														src="images/down-ar.png" alt="" /></span>

											</span>
										</a></th>
										<th><a style="color: white"
											data-ng-click="orderByField1='noOfEndPoints'; reverseSort1 = !reverseSort1">
												Latitude <span
												data-ng-show="orderByField1 == 'noOfEndPoints'"> <span
													data-ng-show="!reverseSort1"><img
														src="images/up-ar.png" alt="" /></span> <span
													data-ng-show="reverseSort1"><img
														src="images/down-ar.png" alt="" /></span>

											</span>
										</a></th>
										<th><a style="color: white"
											data-ng-click="orderByField1='isFileUploaded'; reverseSort1 = !reverseSort1">
												Longitude <span
												data-ng-show="orderByField1 == 'isFileUploaded'"> <span
													data-ng-show="!reverseSort1"><img
														src="images/up-ar.png" alt="" /></span> <span
													data-ng-show="reverseSort1"><img
														src="images/down-ar.png" alt="" /></span>

											</span>
										</a></th>
									</tr>
									<!-- <tr	data-ng-repeat="x in dcFileList | orderBy:orderByField1:reverseSort1" data-ng-if="x.noOfEndPoints > -1"> -->
									<tr>
										<td>KDL</td>
										<td>KDL</td>
										<td>KDL</td>
										<td>KDL</td>
										<td>KDL</td>
										<td>KDL</td>
										<td>KDL</td>
										<td>KDL</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
					<br>
				<button type="button" data-ng-click="redirectToRepeaterPage()" data-ng-model="" style="width: 50%; height: 30px; color: white; background-color: slategray;">Add Repeaters</button>
				</div>
			</div>
        </p>
		
	</div>
	</div>		
			
			
	</div>
</body>
</html>		
			