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
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
<link href="${pageContext.request.contextPath}/css/dropkick.css" rel="stylesheet" type="text/css" />
<link
	href="http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"
	rel="stylesheet">
	
<script src="${pageContext.request.contextPath}/js/jquery.min.js" type="text/javascript"></script>
<!-- [if IE]>
  <script type="text/javascript" src="js/html5.js"></script>
  <![endif] -->


<script src="${pageContext.request.contextPath}/js/angular.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/angular-cookies.js"></script>

<script src="${pageContext.request.contextPath}/js/dropkick.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/easy-tabs.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/custom.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery.popupoverlay.js" type="text/javascript"></script>

<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>

<script src="${pageContext.request.contextPath}/app/controller/reportscontroller.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>
<script>
	$(function() {
		$('#fadeandscale').popup();
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

	<div id="wrapper" data-ng-controller="reportscontroller"
		data-ng-init="initBillingDataReport()">


		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include
			data-src="'customerleftNavigationbar'"></div>

		<div id="content">
			<!--Content Start-->
			<h1>Analytics&nbsp;&nbsp;>>&nbsp;&nbsp;Billing Data Report</h1>
			<div class="middle">
				<div class="boxpanel">
					<span class="heading">Manage Search Fields</span>
					<div class="box-body">
						<form>
							<div class="filter_panel">
							
								<div class="form-group">
									<label class="control-label"><h5>Column 2</h5></label>
									<select class="form-control" data-ng-model="column2" data-ng-change="funCalled('col2',column2)" autofocus="autofocus">
										<option value="">-- Select Column 2 field --</option>
										<option data-ng-repeat="(key,value) in columnDataMap" value="{{value}}">{{key}}</option>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Column 3</h5></label>
									<select	class="form-control" data-ng-model="column3" data-ng-change="funCalled('col3',column3)">
										<option value="">-- Select Column 3 field --</option>
										<option data-ng-repeat="(key,value) in columnDataMap" value="{{value}}">{{key}}</option>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Column 4</h5></label>
									<select	class="form-control" data-ng-model="column4" data-ng-change="funCalled('col4',column4)">
										<option value="">-- Select Column 4 field --</option>
										<option data-ng-repeat="(key,value) in columnDataMap" value="{{value}}">{{key}}</option>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Column 5</h5></label>
									<select	class="form-control" data-ng-model="column5" data-ng-change="funCalled('col5',column5)">
										<option value="">-- Select Column 5 field --</option>
										<option data-ng-repeat="(key,value) in columnDataMap" value="{{value}}">{{key}}</option>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Column 6</h5></label>
									<select	class="form-control" data-ng-model="column6" data-ng-change="funCalled('col6',column6)">
										<option value="">-- Select Column 6 field --</option>
										<option data-ng-repeat="(key,value) in columnDataMap" value="{{value}}">{{key}}</option>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Column 7</h5></label>
									<select	class="form-control" data-ng-model="column7" data-ng-change="funCalled('col7',column7)">
										<option value="">-- Select Column 7 field --</option>
										<option data-ng-repeat="(key,value) in columnDataMap" value="{{value}}">{{key}}</option>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Column 8</h5></label>
									<select	class="form-control" data-ng-model="column8" data-ng-change="funCalled('col8',column8)">
										<option value="">-- Select Column 8 field --</option>
										<option data-ng-repeat="(key,value) in columnDataMap" value="{{value}}">{{key}}</option>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Column 9</h5></label>
									<select	class="form-control" data-ng-model="column9" data-ng-change="funCalled('col9',column9)">
										<option value="">-- Select Column 9 field --</option>
										<option data-ng-repeat="(key,value) in columnDataMap" value="{{value}}">{{key}}</option>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Column 10</h5></label>
									<select	class="form-control" data-ng-model="column10" data-ng-change="funCalled('col10',column10)">
										<option value="">-- Select Column 10 field --</option>
										<option data-ng-repeat="(key,value) in columnDataMap" value="{{value}}">{{key}}</option>
									</select>
								</div>
							</div>
						</form>
					</div>
				</div>
				
				<div class="boxpanel">
					<span class="heading">Search</span>
					<div class="box-body">
						<form>
							<div class="filter_panel">
								
								<div class="form-group">
									<label class="control-label"><h5>Billing Start Date</h5></label>
									<input type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="-- Select Billing Start Date --"
										data-ng-model="billingStartDate"
										data-is-open="popup1.opened" data-datepicker-options=""
										onkeypress="return false" data-close-text="Close"
										data-ng-click="open1()"
										data-alt-input-formats="altInputFormats" required>
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Billing End Date</h5></label>
									<input type="text" class="form-control calendar-input"
										data-uib-datepicker-popup="{{format}}"
										placeholder="-- Select Billing End Date --"
										data-ng-model="billingEndDate"
										data-is-open="popup2.opened" data-datepicker-options=""
										onkeypress="return false" data-close-text="Close"
										data-ng-click="open2()"
										data-alt-input-formats="altInputFormats" required>
								</div>
							
								<div class="form-group" style="margin-top: 12px">
									<br><input type="checkbox" data-ng-model="onlyEstimatedReadAcc"><span></span>Show Estimated reads only									
								</div>
							
								<div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_search" data-ng-click="searchBillingDataReport()">Search</button>
									<button type="reset" data-ng-click="resetSearch()" class="default ic_reset">Reset</button>
								</div>
							</div>
						</form>
					</div>
				</div>
				
				<div class="boxpanel" data-ng-show="noBillingDataFoundDiv">
					<span class="heading">Search Listing</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<td>
										<h3>No such Billing Data found</h3>
									</td>
								</tr>
							</table>

						</div>
					</div>
				</div>
				
				<div class="boxpanel" data-ng-show="showBillingDataListDiv">
								<div class="box-body">
									<div class="grid-content">
										<table class="grid">
											<tr data-ng-repeat="y in tableHeaderArray">
												<th>{{getBillingReportHeaderBasedOnKey(y.col1)}}</th>
												<th data-ng-if="y.col2.length>0">{{getBillingReportHeaderBasedOnKey(y.col2)}}</th>
												<th data-ng-if="y.col3.length>0">{{getBillingReportHeaderBasedOnKey(y.col3)}}</th>
												<th data-ng-if="y.col4.length>0">{{getBillingReportHeaderBasedOnKey(y.col4)}}</th>
												<th data-ng-if="y.col5.length>0">{{getBillingReportHeaderBasedOnKey(y.col5)}}</th>
												<th data-ng-if="y.col6.length>0">{{getBillingReportHeaderBasedOnKey(y.col6)}}</th>
												<th data-ng-if="y.col7.length>0">{{getBillingReportHeaderBasedOnKey(y.col7)}}</th>
												<th data-ng-if="y.col8.length>0">{{getBillingReportHeaderBasedOnKey(y.col8)}}</th>
												<th data-ng-if="y.col9.length>0">{{getBillingReportHeaderBasedOnKey(y.col9)}}</th>
												<th data-ng-if="y.col10.length>0">{{getBillingReportHeaderBasedOnKey(y.col10)}}</th>
												<th>{{getBillingReportHeaderBasedOnKey(y.col11)}}</th>
												<!-- <th><a style="color: white"	data-ng-click="orderByField='consumerAccNum'; reverseSort = !reverseSort">Consumer Account Number
													<span data-ng-show="orderByField == 'consumerAccNum'">
														<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
														<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
													</span>
													</a>
												</th> -->
											</tr>
											<tr data-ng-repeat="x in finalArray.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort">
												<td>{{x.col1}}</td>
												<td data-ng-if="x.col2 != null">{{x.col2}}</td>
												<td data-ng-if="x.col3 != null">{{x.col3}}</td>
												<td data-ng-if="x.col4 != null">{{x.col4}}</td>
												<td data-ng-if="x.col5 != null">{{x.col5}}</td>
												<td data-ng-if="x.col6 != null">{{x.col6}}</td>
												<td data-ng-if="x.col7 != null">{{x.col7}}</td>
												<td data-ng-if="x.col8 != null">{{x.col8}}</td>
												<td data-ng-if="x.col9 != null">{{x.col9}}</td>
												<td data-ng-if="x.col10 != null">{{x.col10}}</td>
												<td>{{x.col11}}</td>
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
							<div class="grid-bottom">
								<div class="form-group auto" style="text-align: right;">
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<button class="default ic_reset"
										data-ng-click="billingDataCSVReportDownload(finalArray,tableHeaderArray)">Export
										to CSV file</button>
								</div>
							</div>
									</div>
								</div>
						</div>
						
						<!-- ================================================= -->

			</div>
			<!--Content end-->
		</div>


			<div id="footer" data-ng-include data-src="'../footer'"></div>
	</div>

</body>
</html>