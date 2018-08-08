<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/toastr.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/sweetalert.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->

<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/sweetalert.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/controller.js"></script>
<base href="/Kenure/">
<script>
	function refreshPage() {
		window.location.reload();
	}
</script>
<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
</script>
<script type="text/javascript">
	angular.module('myApp').requires.push('ui.bootstrap');
</script>
</head>


<body data-ng-app="myApp" id="admin">

	<div id="wrapper" data-ng-controller="logincontroller"
		data-ng-init="initBattery()">


		<div id="header" data-ng-include data-src="'header'"></div>
		<div id="leftNavigationbar" data-ng-include
			data-src="'adminleftNavigationbar'"></div>


		<div id="content">
			<!--Content Start-->
			<h1>
				Battery Management
				<div class="rightbtns">
					<button class="primary ic_add pull-right"
						data-ng-click="editBattery()" type="submit">Add New
						Battery</button>
				</div>
			</h1>
			<div class="middle">

				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>

				<div class="status error" data-ng-show="isMapped">
					<span> Battery is mapped with other object. Can't Delete</span>
				</div>

				<div class="boxpanel">
					<span class="heading">Search</span>
					<div class="box-body">
						<form name="searchBatteryForm">
							<div class="filter_panel">
								<div class="form-group">
									<label class="control-label"><h5>Number of Child
											Nodes</h5></label> <input type="text" class="form-control"
										name="NumOfChildNode" data-ng-trim="false"
										data-ng-change="childNodeInput = childNodeInput.split(' ').join('')"
										maxlength="5" data-ng-model="childNodeInput" required
										autofocus="autofocus">
								</div>

								<div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_search" type="submit"
										data-ng-click="searchBattery()">Search</button>
									<button type="reset" onclick="refreshPage()"
										class="default ic_reset">Reset</button>
								</div>
							</div>
						</form>
						<form name="saveBatteryPercentage">
							<div class="filter_panel">
								<div class="form-group"  style="margin-top: 10px;">
									<input type="number" class="form-control" min="1" max="50"
										name="batteryPercentage" data-ng-trim="false"
										data-ng-model="batteryPercentage" required>
								</div>
								<div class="form-group auto"  style="margin-top: 10px;">
									<h5 style="margin-top: 10px;">%</h5>
								</div>
								<div class="form-group auto" style="margin-top: 15px;">
									<input type="radio"	value="up"  data-ng-model="percentageUpDown" data-ng-required="!percentageUpDown"><span></span>Up &nbsp;&nbsp;&nbsp;
									<input type="radio"	value="down" data-ng-model="percentageUpDown" data-ng-required="!percentageUpDown"><span></span>Down
								</div>
								<div class="form-group auto"  style="margin-top: 10px;">
									<button type="submit" class="primary ic_save" data-ng-click="updateBatteryByPercent()">Update</button>
								</div>
							</div>
						</form>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="noBatteryFoundDiv">
					<span class="heading">Search Listing</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<td>
										<h3>No Such Battery Found</h3>
									</td>
								</tr>
							</table>

						</div>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="batteryListDiv">
					<span class="heading">Battery List</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<th><a style="color: white"
										data-ng-click="orderByField='numberOfChildNodes'; reverseSort = !reverseSort">
											Number of Child Nodes <span
											data-ng-show="orderByField == 'numberOfChildNodes'"> <span
												data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='estimatedBatteryLifeInYears'; reverseSort = !reverseSort">
											Estimated Battery Life (in years) <span
											data-ng-show="orderByField == 'estimatedBatteryLifeInYears'">
												<span data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th>Action</th>
								</tr>

								<tr data-ng-repeat="x in batteryList.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort">
									<td>{{x.numberOfChildNodes}}</td>
									<td>{{x.estimatedBatteryLifeInYears}}</td>
									<td class="action"><a href="" title="edit"> <img
											alt="" src="images/edit-ic.png"
											data-ng-click="editBattery(x.batteryLifeId)">
									</a>&nbsp; <a href="" title="delete"> <img alt=""
											src="images/delete-ic.png"
											data-ng-click="removeBattery(x.batteryLifeId)">
									</a></td>
								</tr>

							</table>
							<div class="grid-bottom">
								<div class="show-record col-sm-3">
									<span class="records">Records:</span> <select
										data-ng-model="recordSize"
										data-ng-change="setItemsPerPage(recordSize)"
										data-ng-options="x for x in paginationList"></select>
								</div>
								<div class="paging">
									<ul uib-pagination data-previous-text="&lt;&lt;"
										data-next-text="&gt;&gt;" data-total-items="totalItems"
										data-ng-model="currentPage" data-max-size="maxSize"
										data-boundary-links="true" data-ng-change="pageChanged()"
										class="pagination-sm" data-items-per-page="itemsPerPage"></ul>
								</div>
							</div>
						</div>
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





