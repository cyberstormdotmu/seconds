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
<script>
	function refreshPage() {
		window.location.reload();
	}
</script>
<base href="/Kenure/">


</head>


<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="logincontroller"
		data-ng-init="">


		<div id="header" data-ng-include data-src="'header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>
		<div id="content">
			<!--Content Start-->
			<h1>
				Consumer Meter Management
				<div class="rightbtns">
					<a href="addConsumerRedirect" class="primary ic_add pull-right">Add	Consumer Meter</a>
				</div>
			</h1>
			<div class="middle">

				<div class="boxpanel">
					<span class="heading">Search Consumer meter</span>
					<div class="box-body">
						<form>
							<div class="filter_panel">
								<div class="form-group">
									<label class="control-label">Consumer Account Number</label> <input
										type="text" class="form-control" data-ng-model="consumerInput"
										data-ng-trim="false"
										data-ng-change="consumerInput = consumerInput.split(' ').join('')"
										required>
								</div>
								<div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_search" data-ng-click="searchConsumer()">Search</button>
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
										<h3>No Such Consumer User Found</h3>
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
											Account Number <span
											data-ng-show="orderByField == 'consumerAccNo'"> <span
												data-ng-show="!reverseSort">^</span> <span
												data-ng-show="reverseSort">v</span>
										</span>
									</a></th>
									<!-- <th><a style="color: white"
										data-ng-click="orderByField='address1'; reverseSort = !reverseSort">
											Address1 <span
											data-ng-show="orderByField == 'address1'">
												<span data-ng-show="!reverseSort">^</span> <span
												data-ng-show="reverseSort">v</span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='address2'; reverseSort = !reverseSort">
											Address2 <span
											data-ng-show="orderByField == 'address2'">
												<span data-ng-show="!reverseSort">^</span> <span
												data-ng-show="reverseSort">v</span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='address3'; reverseSort = !reverseSort">
											Address3 <span
											data-ng-show="orderByField == 'address3'">
												<span data-ng-show="!reverseSort">^</span> <span
												data-ng-show="reverseSort">v</span>
										</span>
									</a></th> -->
									<th><a style="color: white"
										data-ng-click="orderByField='streetName'; reverseSort = !reverseSort">
											Street Name <span
											data-ng-show="orderByField == 'streetName'">
												<span data-ng-show="!reverseSort">^</span> <span
												data-ng-show="reverseSort">v</span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='billingFrequencyInDays'; reverseSort = !reverseSort">
											Billing Frequency (in days) <span
											data-ng-show="orderByField == 'billingFrequencyInDays'">
												<span data-ng-show="!reverseSort">^</span> <span
												data-ng-show="reverseSort">v</span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='billingDay'; reverseSort = !reverseSort">
											Billing Day <span
											data-ng-show="orderByField == 'billingDay'">
												<span data-ng-show="!reverseSort">^</span> <span
												data-ng-show="reverseSort">v</span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='currentReading'; reverseSort = !reverseSort">
											Current Reading <span
											data-ng-show="orderByField == 'currentReading'">
												<span data-ng-show="!reverseSort">^</span> <span
												data-ng-show="reverseSort">v</span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='lastReading'; reverseSort = !reverseSort">
											Last Billing Reading <span
											data-ng-show="orderByField == 'lastReading'"> <span
												data-ng-show="!reverseSort">^</span> <span
												data-ng-show="reverseSort">v</span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='lastBillingDate'; reverseSort = !reverseSort">
											Last Billing Date <span
											data-ng-show="orderByField == 'lastBillingDate'"> <span
												data-ng-show="!reverseSort">^</span> <span
												data-ng-show="reverseSort">v</span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='currentUsage'; reverseSort = !reverseSort">
											Current Usage <span
											data-ng-show="orderByField == 'currentUsage'"> <span
												data-ng-show="!reverseSort">^</span> <span
												data-ng-show="reverseSort">v</span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='nextBillingDate'; reverseSort = !reverseSort">
											Next Billing Date <span
											data-ng-show="orderByField == 'nextBillingDate'"> <span
												data-ng-show="!reverseSort">^</span> <span
												data-ng-show="reverseSort">v</span>
										</span>
									</a></th>
									<th><a style="color: white"
										data-ng-click="orderByField='isActive'; reverseSort = !reverseSort">
											Status <span data-ng-show="orderByField == 'isActive'">
												<span data-ng-show="!reverseSort">^</span> <span
												data-ng-show="reverseSort">v</span>
										</span>
									</a></th>
									<th>Action</th>
								</tr>

								<tr
									data-ng-repeat="x in consumerList.slice(begin,limit) | orderBy:orderByField:reverseSort">
									<td>{{x.consumerAccNo}}</td>
									<!-- <td>{{x.address1}}</td>
									<td>{{x.address2}}</td>
									<td>{{x.address3}}</td> -->
									<td>{{x.streetName}}</td>
									<td>{{x.billingFrequencyInDays}}</td>
									<td></td>
									<td>{{x.currentReading}}</td>
									<td>{{x.lastReading}}</td>
									<td>{{x.lastBillingDate}}</td>
									<td>{{x.currentReading - x.lastReading}}</td>
									<td></td>
									
									
									<td data-ng-if="x.isActive == true">Active</td>
									<td data-ng-if="x.isActive == false">InActive</td>
									<td data-ng-if="x.isActive == false" class="action"><a
										href="" title="edit"> <img alt="" src="images/edit-ic.png"
											data-ng-click="editConsumer(x)">
									</a>&nbsp; <a title="delete"><img alt=""
											src="images/delete-ic.png"></a></td>

									<td data-ng-if="x.isActive == true" class="action"><a
										href="" title="edit"> <img alt="" src="images/edit-ic.png"
											data-ng-click="editConsumer(x)">
									</a>&nbsp; <a href="" title="delete"> <img alt=""
											src="images/delete-ic.png"
											data-ng-click="removeConsumer(x.consumerMeterId, x.consumerAccNo)">
									</a></td>

								</tr>

							</table>
							<div class="grid-bottom">
								<div class="show-record col-sm-3">
									<span class="records">Records:</span> <select
										class="form-control" data-ng-model="recordSize"
										data-ng-init="recordSize = 10"
										data-ng-change="changedValue(recordSize, consumerList)">
										<option>10</option>
										<option>20</option>
										<option>30</option>
										<option>40</option>
										<option>50</option>
									</select>
								</div>
								<div class="paging">
									<ul data-ng-if="consumerList">
										<li data-ng-repeat="i in getNumber(totalPage) track by $index"><a
											href="" data-ng-click="fetchRecord($index+1)">{{$index+1}}</a></li>
									</ul>
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