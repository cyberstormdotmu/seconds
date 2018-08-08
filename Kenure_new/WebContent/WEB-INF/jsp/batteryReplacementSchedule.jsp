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
<link href="http://netdna.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
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
<script src="${pageContext.request.contextPath}/js/angular-animate.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-sanitize.js"></script>
<script src="${pageContext.request.contextPath}/js/ui-bootstrap-tpls-2.1.3.js"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/maintenancecontroller.js"></script>
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
<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize',
			'ui.bootstrap');
	function refreshPage() {
		document.location.href=url+"/batteryReplacementSchedule";
	}
</script>

</head>

<body data-ng-app="myApp">

	<div id="wrapper" data-ng-controller="maintenancecontroller">

		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include data-src="'customerleftNavigationbar'"></div>
	
		<div id="content">
			<!--Content Start-->
			<h1>
				Maintenance&nbsp;&nbsp;>>&nbsp;&nbsp;Battery Replacement Schedule
			</h1>
			<div class="middle">
			<div class="status error" data-ng-show="isError">
				<span> {{errorMessage}}</span>
			</div>
				<div class="boxpanel">
					<span class="heading">Search</span>
					<div class="box-body">
						<form>
							<div class="filter_panel">
								
								<div class="form-group">
									<label class="control-label"><h5>Report By</h5></label> <select
												class="form-control " data-ng-model="selectedReport" data-ng-change="showReplacementDueDiv()" autofocus="autofocus">
												<option value="">---Please Select Report---</option>
												<option value="Week">Week</option>
												<option value="Month">Month</option>
									</select>
								</div>
								<div class="form-group" data-ng-show="replacementDueDiv">
									<label class="control-label"><h5>Report Period</h5></label> <input
										type="number" class="form-control" data-ng-model="reportPeriod" min="0">
								</div>
						
								<div class="form-group">
									<label class="control-label"><h5>Installation</h5></label> <input
										type="text" class="form-control" data-ng-model="installation"  data-ng-trim="false">
								</div>
								<div class="form-group">
									<label class="control-label"><h5>SiteId</h5></label> <input
										type="text" class="form-control" data-ng-model="siteId"  data-ng-trim="false">
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Site Name</h5></label> <input
										type="text" class="form-control" data-ng-model="siteName"  data-ng-trim="false">
								</div>
								<div class="form-group">
									<label class="control-label"><h5>Zipcode</h5></label> <input
										type="text" class="form-control" data-ng-model="zipcode"  data-ng-trim="false" >
								</div>
							
								<div class="form-group auto">
									<label class="control-label">&nbsp;</label>
									<button class="primary ic_search" data-ng-click="searchEPForBatteryReplacement()">Search</button>
									<button type="reset" onclick="refreshPage()"
										class="default ic_reset">Reset</button>
								</div>
							</div>
						</form>
					</div>
				</div>

				 <div class="boxpanel" data-ng-show="noConsumerMeterAlertsFoundDiv">
					<span class="heading">Search Listing</span>
					<div class="box-body">
						<div class="grid-content">
							<table class="grid">
								<tr>
									<td>
										<h3>No Consumer Meter Found</h3>
									</td>
								</tr>
							</table>

						</div>
					</div>
				</div> 

				<div class="boxpanel" data-ng-show="consumerMeterAlertDiv">
					<!-- <span class="heading">Data Collector List </span> -->
					<div class="box-body">
						<div class="grid-content">
							<table class="grid" >
								<tr>
								<th><a style="color: white"
										data-ng-click="orderByField='registerId'; reverseSort = !reverseSort">
											Register Id<span
											data-ng-show="orderByField == 'registerId'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
								</a></th>
								<th><a style="color: white"
										data-ng-click="orderByField='batteryAlert'; reverseSort = !reverseSort">
											Battery alert<span
											data-ng-show="orderByField == 'batteryAlert'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
								</a></th>
								<th><a style="color: white"
										data-ng-click="orderByField='dateFlagged'; reverseSort = !reverseSort">
											Date Flagged<span
											data-ng-show="orderByField == 'dateFlagged'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
								</a></th>
								
								<th><a style="color: white"
										data-ng-click="orderByField='scheduledReplacementDate'; reverseSort = !reverseSort">
											Scheduled Replacement Date<span
											data-ng-show="orderByField == 'scheduledReplacementDate'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
								
								<th><a style="color: white"
										data-ng-click="orderByField='address1'; reverseSort = !reverseSort">
											address1<span
											data-ng-show="orderByField == 'address1'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									
								<th><a style="color: white"
										data-ng-click="orderByField='address2'; reverseSort = !reverseSort">
											address2<span
											data-ng-show="orderByField == 'address2'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									
									<th><a style="color: white"
										data-ng-click="orderByField='address3'; reverseSort = !reverseSort">
											address3<span
											data-ng-show="orderByField == 'address3'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
								
									<th><a style="color: white"
										data-ng-click="orderByField='address4'; reverseSort = !reverseSort">
											address4<span
											data-ng-show="orderByField == 'address4'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
									
									<th><a style="color: white"
										data-ng-click="orderByField='zipcode'; reverseSort = !reverseSort">
											Zipcode<span
											data-ng-show="orderByField == 'zipcode'">
											<span data-ng-show="!reverseSort" class="glyphicon glyphicon-triangle-top"></span>
											<span data-ng-show="reverseSort" class="glyphicon glyphicon-triangle-bottom"></span>
										</span>
									</a></th>
							</tr>
	   						 <tr data-ng-repeat="meter in meterData.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) | orderBy:orderByField:reverseSort">
	   					
									<td>{{meter.registerId}}</td>
									<td ng-if="meter.batteryAlert == 1">Yes</td>
									<td ng-if="meter.batteryAlert == 0">No</td>
									<td>{{meter.dateFlagged}}</td>
									<td ng-if="meter.due == 1" style="color: red">{{meter.scheduledReplacementDate}}</td>
									<td ng-if="meter.due == 0">{{meter.scheduledReplacementDate}}</td>
									<td>{{meter.address1}}</td>
									<td>{{meter.address2}}</td>
									<td>{{meter.address3}}</td>
									<td>{{meter.address4}}</td>
									<td>{{meter.zipcode}}</td>
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


		<div id="footer" data-ng-include src="'../footer'"></div>
	</div>
	<!--wrapper end-->


</body>
</html>
