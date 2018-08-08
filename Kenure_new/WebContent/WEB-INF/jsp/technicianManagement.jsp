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
<link href="${pageContext.request.contextPath}/css/toastr.css" rel="stylesheet" type="text/css" />
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
<script src="${pageContext.request.contextPath}/app/controller/customercontroller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/usercontroller.js"></script>
<script>
	function refreshPage() {
		document.location.href=url+"/customerOperation/technicianManagement";
	}
	$(document).ready(function() {
		  if('${messageFlag}'){
		   $('#successMsgdiv').show();
		  }else if('${errorMsg}'){
		   $('#errorMsgdiv').show();
		  }
		  
		  setTimeout(function() {
		   $('#successMsgdiv').hide();
		   $('#errorMsgdiv').hide();
		     }, 3000);
		 });
	</script>
<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
</script>
<script type="text/javascript">
	angular.module('myApp').requires.push('ui.bootstrap');
</script>
</head>


<body data-ng-app="myApp">

<form id="editForm" action="editTechnician" method="post">
  <input type="hidden" id="editTechId1">

</form>

	<div id="wrapper" data-ng-controller="customercontroller"
		data-ng-init="getTechnicianByCustomerId()">

		

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>
				Control&nbsp;&nbsp;>>&nbsp;&nbsp;Technician Management
				<div class="rightbtns">
					<a href="addTechnicianRedirect" class="primary ic_add pull-right">Add
						New Technician</a>
				</div>
			</h1>
			<div class="middle">

				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>
				
				<div class="status error" data-ng-show="isError">
					<span>{{errorMessage}}</span>
				</div>

				<div class="boxpanel">
					<span class="heading">Search</span>
					<div class="box-body">
						<form>
							<div class="filter_panel">

								<div class="form-group">
									<label class="control-label"><h5>Technician Name</h5></label>
									<input type="text" class="form-control"
									 data-ng-model="technician.name" autofocus="autofocus" required>
								</div>

								<div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_search"
										data-ng-click="searchTechnician()">Search</button>
									<button type="reset" onclick="refreshPage()"
										class="default ic_reset">Reset</button>
								</div>
							</div>
						</form>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="noTechnicianFoundDiv">
					<span class="heading">Search Listing</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<td>
										<h3>No Such Technician Found</h3>
									</td>
								</tr>
							</table>

						</div>
					</div>

				</div>

				<div class="boxpanel" data-ng-show="technicianFoundDiv">
					<span class="heading">Technician List</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
								<!-- 	<th><a style="color: white"
										data-ng-click="orderByField='id'; reverseSort = !reverseSort">
											Technician Id <span
											data-ng-show="orderByField == 'id'"> <span
												data-ng-show="!reverseSort">^</span> <span
												data-ng-show="reverseSort">v</span>
										</span>
									</a></th> -->
									<th><a style="color: white"
										data-ng-click="orderByField='name'; reverseSort = !reverseSort">
											Technician Name <span data-ng-show="orderByField == 'name'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='status'; reverseSort = !reverseSort">
											Status <span data-ng-show="orderByField == 'status'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th>Action</th>
								</tr>
								<tr
									data-ng-repeat="x in technicians.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort">
									<!-- <td>{{x.id}}</td> -->
									<td>{{x.name}}</td>
									<td data-ng-if="x.status == true">Active</td>
									<td data-ng-if="x.status == false">InActive</td>
									<td class="action"><a
										href="editTechnicianAction?editTechId={{x.id}}" title="edit"> <img
											alt="" src="${pageContext.request.contextPath}/images/edit-ic.png">
									</a>&nbsp;<a href="" title="delete"> <img alt=""
											src="${pageContext.request.contextPath}/images/delete-ic.png" data-ng-if="x.status == true"
											data-ng-click="removeTechnician(x.id)">
									<img alt="" src="${pageContext.request.contextPath}/images/delete-ic.png" data-ng-if="x.status == false" data-ng-click="abortDeleteTechnician()">
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



		<div id="footer" data-ng-include data-src="'../footer'"></div>
	</div>

</body>
</html>





