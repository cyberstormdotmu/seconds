<!DOCTYPE>
<!--  -->
<html lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>BLU Tower</title>
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
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
<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/sweetalert.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/controller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/customercontroller.js"></script>
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

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="customercontroller"
		data-ng-init="getConsumerList()">

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div>
			<div id="customerleftNavigationbar" data-ng-include
				data-src="'customerleftNavigationbar'"></div>
		</div>
		<div id="content">
			<!--Content Start-->
			<h1>
				Control&nbsp;&nbsp;>>&nbsp;&nbsp;Endpoint Management
				<div>
					<div class="rightbtns">
						<a href="addConsumerRedirect" class="primary ic_add pull-right">Add
							New Endpoint</a>
					</div>
				</div>
			</h1>
			<div class="middle">

				<div class="status success" data-ng-show="isSuccess">
					<span> {{success}} </span>
				</div>

				<div class="status error" data-ng-show="isMapped">
					<span>${errorMessage}</span>
				</div>

				<div class="boxpanel">
					<span class="heading">Search Endpoint</span>
					<div class="box-body">
						<form>
							<div class="filter_panel">
								<!-- <div class="status success" data-ng-show="searchCompleted">
									<span>Successfully Searched Endpoint List.</span>
								</div> -->
								<div class="form-group">
									<label class="control-label"><h5>Consumer Account
											Number</h5></label> <input type="text" class="form-control"
										data-ng-model="searchConsumerAccNo" data-ng-trim="false"
										data-ng-change="searchConsumerAccNo = searchConsumerAccNo.split(' ').join('')"
										autofocus="autofocus">
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Endpoint Register
											Id</h5></label> <input type="text" class="form-control"
										data-ng-model="searchRegisterId" data-ng-trim="false"
										data-ng-change="searchRegisterId = searchRegisterId.split(' ').join('')">
								</div>
								<div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_search"
										data-ng-click="searchConsumer()">Search</button>
									<button type="reset" onclick="refreshPage()"
										class="default ic_reset">Reset</button>
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
										<h3>No Such Endpoint Found</h3>
									</td>
								</tr>
							</table>

						</div>
					</div>
				</div>

				<div class="boxpanel" data-ng-show="consumerListDiv">
					<span class="heading">Endpoint List</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tbody>
									<tr>
										<th><a style="color: white"
											data-ng-click="orderByField='conAccNum'; reverseSort = !reverseSort">
												Consumer Account Number <span
												data-ng-show="orderByField == 'conAccNum'"> <span
													data-ng-show="!reverseSort"
													class="glyphicon glyphicon-triangle-top"></span> <span
													data-ng-show="reverseSort"
													class="glyphicon glyphicon-triangle-bottom"></span>
											</span>
										</a></th>
									
										<th><a style="color: white"
											data-ng-click="orderByField='registerId'; reverseSort = !reverseSort">
												Register Id <span
												data-ng-show="orderByField == 'registerId'"> <span
													data-ng-show="!reverseSort"
													class="glyphicon glyphicon-triangle-top"></span> <span
													data-ng-show="reverseSort"
													class="glyphicon glyphicon-triangle-bottom"></span>
											</span>
										</a></th>
										<th><a style="color: white"
											data-ng-click="orderByField='billingFrequencyInDays'; reverseSort = !reverseSort">
												Billing Frequency (in days) <span
												data-ng-show="orderByField == 'billingFrequencyInDays'">
													<span data-ng-show="!reverseSort"
													class="glyphicon glyphicon-triangle-top"></span> <span
													data-ng-show="reverseSort"
													class="glyphicon glyphicon-triangle-bottom"></span>
											</span>
										</a></th>
										<th><a style="color: white"
											data-ng-click="orderByField='billingDay'; reverseSort = !reverseSort">
												Billing Day <span
												data-ng-show="orderByField == 'billingDay'"> <span
													data-ng-show="!reverseSort"
													class="glyphicon glyphicon-triangle-top"></span> <span
													data-ng-show="reverseSort"
													class="glyphicon glyphicon-triangle-bottom"></span>
											</span>
										</a></th>
										<th><a style="color: white"
											data-ng-click="orderByField='currentReading'; reverseSort = !reverseSort">
												Current Reading <span
												data-ng-show="orderByField == 'currentReading'"> <span
													data-ng-show="!reverseSort"
													class="glyphicon glyphicon-triangle-top"></span> <span
													data-ng-show="reverseSort"
													class="glyphicon glyphicon-triangle-bottom"></span>
											</span>
										</a></th>
										<th><a style="color: white"
											data-ng-click="orderByField='lastReading'; reverseSort = !reverseSort">
												Last Billing Reading <span
												data-ng-show="orderByField == 'lastReading'"> <span
													data-ng-show="!reverseSort"
													class="glyphicon glyphicon-triangle-top"></span> <span
													data-ng-show="reverseSort"
													class="glyphicon glyphicon-triangle-bottom"></span>
											</span>
										</a></th>
										<th><a style="color: white"
											data-ng-click="orderByField='lastBillingDate'; reverseSort = !reverseSort">
												Last Billing Date <span
												data-ng-show="orderByField == 'lastBillingDate'"> <span
													data-ng-show="!reverseSort"
													class="glyphicon glyphicon-triangle-top"></span> <span
													data-ng-show="reverseSort"
													class="glyphicon glyphicon-triangle-bottom"></span>
											</span>
										</a></th>
										<th><a style="color: white"
											data-ng-click="orderByField='currentUsage'; reverseSort = !reverseSort">
												Current Usage <span
												data-ng-show="orderByField == 'currentUsage'"> <span
													data-ng-show="!reverseSort"
													class="glyphicon glyphicon-triangle-top"></span> <span
													data-ng-show="reverseSort"
													class="glyphicon glyphicon-triangle-bottom"></span>
											</span>
										</a></th>
										<th><a style="color: white"
											data-ng-click="orderByField='nextBillingDate'; reverseSort = !reverseSort">
												Next Billing Date <span
												data-ng-show="orderByField == 'nextBillingDate'"> <span
													data-ng-show="!reverseSort"
													class="glyphicon glyphicon-triangle-top"></span> <span
													data-ng-show="reverseSort"
													class="glyphicon glyphicon-triangle-bottom"></span>
											</span>
										</a></th>
										<th><a style="color: white"
											data-ng-click="orderByField='isActive'; reverseSort = !reverseSort">
												Status <span data-ng-show="orderByField == 'isActive'">
													<span data-ng-show="!reverseSort"
													class="glyphicon glyphicon-triangle-top"></span> <span
													data-ng-show="reverseSort"
													class="glyphicon glyphicon-triangle-bottom"></span>
											</span>
										</a></th>
										<th>Bill Data</th>
										<th>Action</th>
									</tr>

									<tr	data-ng-repeat="x in consumerList.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort">
										<td>{{x.conAccNum}}</td>
										<td>{{x.registerId}}</td>
										<td>{{x.billingFrequencyInDays}}</td>
										<td></td>
										<td>{{x.currentReading}}</td>
										<td>{{x.lastReading}}</td>
										<td>{{x.lastBillingDate}}</td>
										<td>{{x.currentReading - x.lastReading}}</td>
										<td></td>
										<td data-ng-if="x.isActive == true">Active</td>
										<td data-ng-if="x.isActive == false">InActive</td>
										
										<!-- <td data-ng-if="x.isActive == false" class="action">
											<a href="" title="edit">
												<img alt=""	src="images/edit-ic.png" data-ng-click="editConsumer(x.consumerMeterId)">
											</a>&nbsp;
											<a href="" title="Show Billing">
												<img alt="" src="images/notes.png" data-ng-click="showBillingData(x.consumerMeterId, x.registerId)">
											</a>&nbsp;
											<a title="delete">
												<img alt="" src="images/delete-ic.png">
											</a>
										</td> -->
										
										<td class="action">
											<a href="" title="Show Billing">
												<img alt=""	src="${pageContext.request.contextPath}/images/notes.png" data-ng-click="showBillingData(x.consumerMeterId, x.registerId)">
											</a>&nbsp;
										</td>
									
										<td class="action">
											<a href="" title="edit">
												<img alt=""	src="${pageContext.request.contextPath}/images/edit-ic.png" data-ng-click="editCustomer(x.consumerMeterId)">
											</a>&nbsp;
											<a href="" title="delete">
												<img data-ng-if="x.isActive == true" alt=""	src="${pageContext.request.contextPath}/images/delete-ic.png" data-ng-click="removeConsumer(x.consumerMeterId, x.registerId)">
												<img data-ng-if="x.isActive == false" alt="" src="${pageContext.request.contextPath}/images/delete-ic.png" data-ng-click="abortEndPointDelete()">
											</a>
										</td>

									</tr>
								</tbody>
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