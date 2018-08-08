<!DOCTYPE>
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<!-- <link href="http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"> -->
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/toastr.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/sweetalert.css" rel="stylesheet" type="text/css" />
<!-- <link href="http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"> -->
<!-- [if IE]> -->
<script type="text/javascript" src="js/html5.js"></script>
<!-- <![endif]  -->
<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
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

<script src="${pageContext.request.contextPath}/app/controller/controller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/usercontroller.js"></script>

<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>

<!-- <script src="js/activeClass.js"></script> -->

<!-- <script>var url = "${pageContext.request.contextPath}";</script> -->

<style>
.full button span {
	background-color: blue;
	border-radius: 32px;
	color: black;
}

.partially button span {
	background-color: orange;
	border-radius: 32px;
	color: black;
}

</style>

<base href="/Kenure/">
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
		data-ng-init="init()">


		<div id="header" data-ng-include data-src="'header'"></div>
		<div id="leftNavigationbar" data-ng-include
			data-src="'adminleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>
				User Management
				<div class="rightbtns">
					<a href="adminOperation/addUserRedirect" class="primary ic_add pull-right">Add
						New User</a>
				</div>
			</h1>
			<div class="middle">

				<div class="status error" data-ng-show="searchEmpty">
					<span>Please Enter Any Search Criteria !</span>
				</div>

				<div class="status error" data-ng-show="isError">
					<span> User is mapped with other object. Can't delete.</span>
				</div>

				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>

				<div class="boxpanel">
					<span class="heading">Search</span>
					<div class="box-body">
						<form>
							<div class="filter_panel ">
								<div class="form-group">
									<label class="control-label"><h5>Customer Name</h5></label> <input
										type="text" class="form-control"
										data-ng-model="customerNameCriteria" autofocus="autofocus">
								</div>

								<div class="form-group">
									<label class="control-label"><h5>Data Plan Active
											date</h5></label> <input type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="-- Click here to Select Date --"
										data-ng-model="dataplanActivedate"
										data-is-open="popup1.opened" data-datepicker-options=""
										onkeypress="return false" data-close-text="Close"
										data-ng-click="open1()"
										data-alt-input-formats="altInputFormats">
									<!-- <span class="input-group-btn">  <button type="button" class="btn btn-default" data-ng-click="open1()">  <i class="glyphicon glyphicon-calendar"></i>  </button>  </span> -->
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Data Plan Expiry
											date</h5></label> <input type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="-- Click here to Select Date --"
										data-ng-model="dataplanExpirydate"
										data-is-open="popup2.opened" data-datepicker-options=""
										onkeypress="return false" data-close-text="Close"
										data-ng-click="open2()"
										data-alt-input-formats="altInputFormats">
									<!-- <span class="input-group-btn">  <button type="button" class="btn btn-default" data-ng-click="open1()">  <i class="glyphicon glyphicon-calendar"></i>  </button>  </span> -->
								</div>

								<div class="form-group">
									<label class="control-label"><h5>PortalPlan Active
											date</h5></label> <input type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="-- Click here to Select Date --"
										data-ng-model="portalplanActivedate"
										data-is-open="popup3.opened" data-datepicker-options=""
										onkeypress="return false" data-close-text="Close"
										data-ng-click="open3()"
										data-alt-input-formats="altInputFormats">
									<!-- <span class="input-group-btn">  <button type="button" class="btn btn-default" data-ng-click="open1()">  <i class="glyphicon glyphicon-calendar"></i>  </button>  </span> -->
								</div>

								<div class="form-group">
									<label class="control-label"><h5>PortalPlan Expiry
											date</h5></label> <input type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="-- Click here to Select Date --"
										data-ng-model="portalplanExpirydate"
										data-is-open="popup4.opened" data-datepicker-options=""
										onkeypress="return false" data-close-text="Close"
										data-ng-click="open4()"
										data-alt-input-formats="altInputFormats">
									<!-- <span class="input-group-btn">  <button type="button" class="btn btn-default" data-ng-click="open1()">  <i class="glyphicon glyphicon-calendar"></i>  </button>  </span> -->
								</div>

								<div class="form-group ">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_search" type="submit"
										data-ng-click="searchCustomer()">Search</button>
									<button type="reset" data-ng-click="refreshAdminUserPage()"
										class="default ic_reset">Reset</button>
								</div>
							</div>
						</form>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="noUserFoundDiv">
					<span class="heading">Search Listing</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<td>
										<h3>No Such User Found</h3>
									</td>
								</tr>
							</table>

						</div>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="!noUserFoundDiv">
					<span class="heading">Search Listing</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<th><a style="color: white"
										data-ng-click="orderByField='companyName'; reverseSort = !reverseSort">
											Company Name <span
											data-ng-show="orderByField == 'companyName'"> <span
												data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='contactName'; reverseSort = !reverseSort">
											Contact Name <span
											data-ng-show="orderByField == 'contactName'"> <span
												data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='contractExpiry'; reverseSort = !reverseSort">
											PortalPlan Expiry Date <span
											data-ng-show="orderByField == 'contractExpiry'"> <span
												data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<!-- <th><a style="color: white"
										data-ng-click="orderByField='activeDate'; reverseSort = !reverseSort">
											Plan Active Date <span data-ng-show="orderByField == 'activeDate'">
												<span data-ng-show="!reverseSort">^</span> <span
												data-ng-show="reverseSort">v</span>
										</span>
									</a></th> -->
									<th><a style="color: white"
										data-ng-click="orderByField='expiryDate'; reverseSort = !reverseSort">
											Data Plan Expiry Date <span
											data-ng-show="orderByField == 'expiryDate'"> <span
												data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='status'; reverseSort = !reverseSort">
											Status <span data-ng-show="orderByField == 'status'">
												<span data-ng-show="!reverseSort"
												class="glyphicon glyphicon-triangle-top"></span> <span
												data-ng-show="reverseSort"
												class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th>Action</th>
								</tr>

								<tr
									data-ng-repeat="x in customerList.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort">
									<td>{{x.companyName}}</td>
									<td>{{x.contactName}}</td>
									<td>{{x.contractExpiry}}</td>
									<td>{{x.expiryDate}}</td>
									<td>{{x.status}}</td>
									<td class="action"><a href="" title="edit"> <img
											alt="" src="images/edit-ic.png"
											data-ng-click="editUser(x.customerId)">
									</a>&nbsp; <a href="" title="delete"> <img alt=""
											src="images/delete-ic.png"
											data-ng-click="removeRow(x.customerId)">
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


		<div id="footer" data-ng-include data-src="'footer'"></div>
	</div>
	<!--wrapper end-->


</body>
</html>





