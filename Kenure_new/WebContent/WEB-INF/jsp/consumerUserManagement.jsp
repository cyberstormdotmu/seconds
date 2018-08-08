<!DOCTYPE>
<!--  -->
<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<!-- <link href="http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"> -->
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/sweetalert.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/swal-forms.css" rel="stylesheet" type="text/css" />
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
<script src="${pageContext.request.contextPath}/js/swal-forms.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/customercontroller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/popupcontroller.js"></script>
<script>
	function refreshPage() {
		window.location.reload();
	}
	
	$(document).ready(function() {
		if('${successMsg}'){
			$('#successMsgdiv').show();
		}else if('${errorMsg}'){
			$('#errorMsgdiv').show();
		}
		
		setTimeout(function() {
			$('#successMsgdiv').hide();
			$('#errorMsgdiv').hide();
	    }, 5000);
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
	<div id="wrapper" data-ng-controller="customercontroller"
		data-ng-init="getConsumerUserList()">
		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>
		<div id="content">
			<!--Content Start-->
			<h1>
				Control&nbsp;&nbsp;>>&nbsp;&nbsp;Consumer Management
				<div class="rightbtns" data-ng-controller="popupcontroller">
					<a href="addConsumerUserRedirect" class="primary ic_add pull-right">Add	Consumer</a>
				</div>
			</h1>
			<div class="middle">
				<!-- <div class="status success"data-ng-show="isSuccess">
					<span>{{successMessage}}</span>
				</div> -->
				<div class="status error" data-ng-show="isError">
					<span>{{errorMessage}}</span>
				</div>
				<div class="status success" id="successMsgdiv" style="display: none;">
					<span>${successMsg}</span>
				</div>
				<div class="status error" id="errorMsgdiv" style="display: none;">
					<span>${errorMsg}</span>
				</div>
				
				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>
				
				<div class="boxpanel">
					<span class="heading">Search Consumer</span>
					<div class="box-body">
						<form>
							<div class="filter_panel">
							
							<div class="status success" data-ng-show="deleteSuccessfully">
								<span> Consumer Deleted Successfully !</span>
							</div>
							
								<div class="form-group">
									<label class="control-label"><h5>Consumer Account Number</h5></label> <input
										type="text" class="form-control" data-ng-model="consumerAccNumInput"
										data-ng-trim="false" maxlength="30"
										
										required autofocus="autofocus">
								</div>
								<div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button type="submit" class="primary ic_search" data-ng-click="searchConsumerByAccNum()">Search</button>
									<button type="reset" onclick="refreshPage()" class="default ic_reset">Reset</button>
								</div>
							</div>
						</form>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="noConsumerFoundDiv">
					<span class="heading">Search Listing</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<td>
										<h3>No Such Consumer Found</h3>
									</td>
								</tr>
							</table>

						</div>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="consumerListDiv">
					<span class="heading">Consumer List</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<th><a style="color: white"
										data-ng-click="orderByField='consumerAccNo'; reverseSort = !reverseSort">
											Consumer Account Number <span
											data-ng-show="orderByField == 'consumerAccNo'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='firstName'; reverseSort = !reverseSort">
											Consumer Name <span
											data-ng-show="orderByField == 'firstName'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='isActive'; reverseSort = !reverseSort">
											Status <span data-ng-show="orderByField == 'isActive'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									<th>Action</th>
								</tr>

								<tr data-ng-repeat="x in consumerUserList.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort">
									<td>{{x.consumerAccNo}}</td>
									<td>{{x.firstName}}</td>
									<td class="action" data-ng-if="x.isActive == true">Active</td>
									<td class="action" data-ng-if="x.isActive == false">InActive</td>
									<td class="action"><a
										href="" title="edit"> <img alt="" src="${pageContext.request.contextPath}/images/edit-ic.png"
											data-ng-click="editConsumerUser(x)">
									</a>&nbsp; <a href="" title="delete">
										<img alt="" src="${pageContext.request.contextPath}/images/delete-ic.png" data-ng-if="x.isActive == true" data-ng-click="removeConsumerUser(x.consumerId)">
										<img alt="" src="${pageContext.request.contextPath}/images/delete-ic.png" data-ng-if="x.isActive == false" data-ng-click="abortDelete()">
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
	<!--wrapper end-->
</body>
</html>