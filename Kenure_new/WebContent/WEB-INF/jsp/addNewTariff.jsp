<!DOCTYPE>
<!--  -->
<html lang="en">

<head>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
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

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/controller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/usercontroller.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/customercontroller.js"></script>

<style type="text/css">
md-checkbox {
	margin: 8px;
	cursor: pointer;
	padding-left: 18px;
	padding-right: 0;
	line-height: 26px;
	min-width: 18px;
	min-height: 18px;
}

md-checkbox.md-checked.green .md-icon {
	background-color: rgba(0, 255, 0, 0.87);
}

.users {
	table-layout: fixed;
	width: 80%;
	white-space: normal;
}
/* Column widths are based on these cells */
.row-start {
	width: 20%;
}

.row-end {
	width: 19%;
}

.row-cost {
	width: 18%;
}

.row-name {
	width: 15%;
}

.row-checked {
	width: 5%;
}

.users td {
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
}

.users th {
	background: #206bad;
	color: white;
}

.users td, .users th {
	text-align: center;
	padding: 15px 10px;
}
</style>


</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="customercontroller" data-ng-init="initAddTeriffVariable()">

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>
				Control&nbsp;&nbsp;>>&nbsp;&nbsp;Tariff Management
				<div class="rightbtns">
					<a href="tariffManagement" class="primary ic_back">Back</a>
				</div>
			</h1>
			<div class="middle">

				<!-- <div class="boxpanel" data-ng-show="noUserFoundDiv">
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
				</div> -->

				<div class="boxpanel">

					<span class="heading">Add New Tariff</span>

					<div class="status error" data-ng-show="isNotProperlyFilled">
						<span>{{error}}</span>
					</div>

					<div class="box-body">
						<div class="grid-content">

							<div style="margin-top: 20px;margin-left: 40px;margin-bottom: 20px;">
								<span class="control-label required"> Tariff Name :</span> <span>  <input type="text" 
										data-ng-model="tariffName" autofocus="autofocus" required> </span>
							</div>
							<table class="users" style="">
								<thead>
									<tr>
										<th class="row-name"></th>
										<th class="row-checked">Checked</th>
										<th class="row-start">Consumption Band Start</th>
										<th class="row-end">Consumption Band End</th>
										<th class="row-cost">Cost</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="row-name" data-ng-model="trans1">Tariff
											Transaction 1</td>
										<td class="row-checked"><input type="checkbox"
											data-ng-model="trans1.check"
											data-ng-change="tariffAdded(trans1,1)"><span></span></td>
										<td class="row-start"><input type="number"
											data-ng-disabled="!trans1.check" data-ng-model="trans1.start"
											min="0" /></td>
										<td class="row-end"><input type="number"
											data-ng-disabled="!trans1.check" data-ng-model="trans1.end"
											min="0" data-ng-required="trans1.check" /></td>
										<td class="row-cost"><input type="number" min="0"
											data-ng-disabled="!trans1.check" data-ng-model="trans1.cost"
											data-ng-required="trans1.check"></td>

									</tr>

									<tr>
										<td class="row-name" data-ng-model="trans2">Tariff
											Transaction 2</td>
										<td class="row-checked"><input type="checkbox"
											data-ng-model="trans2.check"
											data-ng-change="tariffAdded(trans2,2)"><span></span></td>
										<td class="row-start"><input type="number"
											data-ng-disabled="true" data-ng-model="trans2.start"
											data-ng-value="trans2.startValue" min="0" /></td>
										<td class="row-end"><input type="number"
											data-ng-disabled="!trans2.check" data-ng-model="trans2.end"
											min="0" data-ng-required="trans2.check" /></td>
										<td class="row-cost"><input type="number" min="0"
											data-ng-disabled="!trans2.check" data-ng-model="trans2.cost"
											data-ng-required="trans2.check"></td>

									</tr>

									<tr>
										<td class="row-name" data-ng-model="trans3">Tariff
											Transaction 3</td>
										<td class="row-checked"><input type="checkbox"
											data-ng-model="trans3.check"
											data-ng-change="tariffAdded(trans3,3)"><span></span></td>
										<td class="row-start"><input type="number"
											data-ng-disabled="true" data-ng-model="trans3.start"
											data-ng-value="trans3.startValue" min="0" /></td>
										<td class="row-end"><input type="number"
											data-ng-disabled="!trans3.check" data-ng-model="trans3.end"
											min="0" data-ng-required="trans3.check" /></td>
										<td class="row-cost"><input type="number" min="0"
											data-ng-required="trans3.check"
											data-ng-disabled="!trans3.check" data-ng-model="trans3.cost"></td>

									</tr>

									<tr>
										<td class="row-name" data-ng-model="trans4">Tariff
											Transaction 4</td>
										<td class="row-checked"><input type="checkbox"
											data-ng-model="trans4.check"
											data-ng-change="tariffAdded(trans4,4)"><span></span></td>
										<td class="row-start"><input type="number"
											data-ng-disabled="true" data-ng-model="trans4.start"
											data-ng-value="trans4.startValue" min="0" /></td>
										<td class="row-end"><input type="number"
											data-ng-disabled="!trans4.check" data-ng-model="trans4.end"
											min="0" data-ng-required="trans4.check" /></td>
										<td class="row-cost"><input type="number" min="0"
											data-ng-required="trans4.check"
											data-ng-disabled="!trans4.check" data-ng-model="trans4.cost"></td>

									</tr>

									<tr>
										<td class="row-name" data-ng-model="trans5">Tariff
											Transaction 5</td>
										<td class="row-checked"><input type="checkbox"
											data-ng-model="trans5.check"
											data-ng-change="tariffAdded(trans5,5)"><span></span></td>
										<td class="row-start"><input type="number"
											data-ng-disabled="true" data-ng-model="trans5.start"
											data-ng-value="trans5.startValue" min="0" /></td>
										<td class="row-end"><input type="number"
											data-ng-disabled="!trans5.check" data-ng-model="trans5.end"
											min="0" data-ng-required="trans4.check" /></td>
										<td class="row-cost"><input type="number" min="0"
											data-ng-required="trans5.check"
											data-ng-disabled="!trans5.check" data-ng-model="trans5.cost"></td>

									</tr>

									<tr>
										<td class="row-name" data-ng-model="trans6">Tariff
											Transaction 6</td>
										<td class="row-checked"><input type="checkbox"
											data-ng-model="trans6.check"
											data-ng-change="tariffAdded(trans6,6)"><span></span></td>
										<td class="row-start"><input type="number"
											data-ng-disabled="true" data-ng-model="trans6.start"
											data-ng-value="trans6.startValue" min="0" /></td>
										<td class="row-end"><input type="number"
											data-ng-disabled="!trans6.check" data-ng-model="trans6.end"
											min="0" data-ng-required="trans6.check" /></td>
										<td class="row-cost"><input type="number" min="0"
											data-ng-required="trans6.check"
											data-ng-disabled="!trans6.check" data-ng-model="trans6.cost"></td>

									</tr>

									<tr>
										<td class="row-name" data-ng-model="trans7">Tariff
											Transaction 7</td>
										<td class="row-checked"><input type="checkbox"
											data-ng-model="trans7.check"
											data-ng-change="tariffAdded(trans7,7)"><span></span></td>
										<td class="row-start"><input type="number"
											data-ng-disabled="true" data-ng-model="trans7.start"
											data-ng-value="trans7.startValue" min="0" /></td>
										<td class="row-end"><input type="number"
											data-ng-disabled="!trans7.check" data-ng-model="trans7.end"
											min="0" data-ng-required="trans7.check" /></td>
										<td class="row-cost"><input type="number" min="0"
											data-ng-required="trans7.check"
											data-ng-disabled="!trans7.check" data-ng-model="trans7.cost"></td>

									</tr>

									<tr>
										<td class="row-name" data-ng-model="trans8">Tariff
											Transaction 8</td>
										<td class="row-checked"><input type="checkbox"
											data-ng-model="trans8.check"
											data-ng-change="tariffAdded(trans8,8)"><span></span></td>
										<td class="row-start"><input type="number"
											data-ng-disabled="true" data-ng-model="trans8.start"
											data-ng-value="trans8.startValue" min="0" /></td>
										<td class="row-end"><input type="number"
											data-ng-disabled="!trans8.check" data-ng-model="trans8.end"
											min="0" data-ng-required="trans8.check" /></td>
										<td class="row-cost"><input type="number" min="0"
											data-ng-required="trans8.check"
											data-ng-disabled="!trans8.check" data-ng-model="trans8.cost"></td>

									</tr>

									<tr>
										<td class="row-name" data-ng-model="trans9">Tariff
											Transaction 9</td>
										<td class="row-checked"><input type="checkbox"
											data-ng-model="trans9.check"
											data-ng-change="tariffAdded(trans9,9)"><span></span></td>
										<td class="row-start"><input type="number"
											data-ng-disabled="true" data-ng-model="trans9.start"
											data-ng-value="trans9.startValue" min="0" /></td>
										<td class="row-end"><input type="number"
											data-ng-disabled="!trans9.check" data-ng-model="trans9.end"
											min="0" data-ng-required="trans9.check" /></td>
										<td class="row-cost"><input type="number" min="0"
											data-ng-required="trans9.check"
											data-ng-disabled="!trans9.check" data-ng-model="trans9.cost"></td>

									</tr>

									<tr>
										<td class="row-name" data-ng-model="trans10">Tariff
											Transaction 10</td>
										<td class="row-checked"><input type="checkbox"
											data-ng-model="trans10.check" data-ng-model=""
											data-ng-change="tariffAdded(trans10,10)"><span></span></td>
										<td class="row-start"><input type="number"
											data-ng-disabled="true" data-ng-model="trans10.start"
											data-ng-value="trans10.startValue" min="0" /></td>
										<td class="row-end"><input type="number"
											data-ng-disabled="!trans10.check" data-ng-model="trans10.end"
											min="0" data-ng-required="trans10.check" /></td>
										<td class="row-cost"><input type="number" min="0"
											data-ng-required="trans10.check"
											data-ng-disabled="!trans10.check"
											data-ng-model="trans10.cost"></td>

									</tr>

								</tbody>
							</table>

							<div class="form-group"
								style="margin-left: 500px; margin-top: 50">
								<label class="control-label"> </label>

								<button
									class="primary ic_submit ng-pristine ng-valid ng-empty ng-touched"
									data-ng-model="addTariff" data-ng-click="addTariff()"
									type="submit">Add Tariffs</button>

							</div>

							<!-- <div class="grid-bottom">
								<div class="show-record col-sm-3">
									<span class="records">Records:</span> <select
										class="form-control" data-ng-model="recordSize"
										data-ng-init="recordSize = 10"
										data-ng-change="changedValue(recordSize,siteList)">
										<option>10</option>
										<option>20</option>
										<option>30</option>
										<option>40</option>
										<option>50</option>

									</select>
								</div>
								<div class="paging">
									<ul data-ng-if="siteList">
										<li data-ng-repeat="i in getNumber(totalPage) track by $index"><a
											href="" data-ng-click="fetchRecord($index+1)">{{$index+1}}</a></li>
									</ul>
								</div>
							</div> -->

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

