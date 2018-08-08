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
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/sweetalert.css" rel="stylesheet" type="text/css" />
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
<script src="${pageContext.request.contextPath}/js/easy-tabs.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/angular-ui-router.js"></script>
<script src="${pageContext.request.contextPath}/js/html5.js"></script>
<script src="${pageContext.request.contextPath}/js/toastr.js"></script>
<script src="${pageContext.request.contextPath}/app/app.js"></script>
<script src="${pageContext.request.contextPath}/app/controller/commissioningcontroller.js"></script>
<script type="text/javascript">
	angular.module('myApp').requires.push('ngAnimate', 'ngSanitize','ui.bootstrap');
</script>
</head>
<body data-ng-app="myApp">
	<div id="wrapper" data-ng-controller="commissioningcontroller" data-ng-init="initRepeaterLocaionInstallation(${selectedSiteId})">
		<div id="header" data-ng-include data-src="'../header'"></div>
		<div id="customerleftNavigationbar" data-ng-include data-src="'customerleftNavigationbar'"></div>
		<div id="content">
			<!--Content Start-->
			<h1>
				Repeaters Planning
			</h1>
			<div class="middle">
				<div class="boxpanel">
				<span class="heading">Add Repeater Location&nbsp;&nbsp;<button data-ng-click="addNewRepeaterLocation()" data-ng-if="tag === 7">Add New Line</button>&nbsp;&nbsp;<button data-ng-click="removeRepeaterLocation()" data-ng-if="tag === 7">Remove Line</button></span>
				<div class="boxpanel" data-ng-if="tag === 7">
					<div class="installed-datacollectors">
						<div class="installed-box" style="width: 75%;">
							<div class="border-box">
								<div class="box-body">
									<div class="grid-content">
										<table class="grid">
											<tr>
												<th>
												<span></span><a data-ng-click="selectAllFun()" style="color: white;">Select All</a>
												</th>
												<th>Repeater Location</th>
												<th>Latitude</th>
												<th>Longitude</th>
												<th>Slots Required</th>
												<th>Levels Required</th>
												<th>Installer</th>
											</tr>
			
											<tr data-ng-repeat="x in repeatersLocations.slice(((currentPage-1)*itemsPerPage), ((currentPage)*itemsPerPage)) track by $index">
												<td><input type="checkbox" data-ng-model="default.repeaterLocationCheck" data-ng-click="pushOrPopRepeaterLocation($index)" data-ng-checked="selectAllRepeaterLocations"><span></span></td>
												<td data-ng-repeat="y in x track by $index"><input data-ng-blur="updateRepeaterRightSideTable(installerSelected,$parent.$index)" style="border: 0px solid; background: transparent;" placeholder="Enter Value" type="text" data-ng-model="x[$index]"></td>
												<td><select data-ng-model="installerSelected" data-ng-change="updateRepeaterRightSideTable(installerSelected,$index)" data-ng-options="installer as installer for installer in installerName" style="width: 100%"></select></td>								
											</tr>
										</table>
									</div>
								</div>
							</div>

						</div>
						<!-- ================================================= -->

						<div class="installed-box" style="width: 25%;">
							<div class="border-box">
								<div class="box-body">
									<div class="grid-content">
										<table class="grid">
											<tr>
												<th>Installer Name</th>
												<th>Number of Repeaters</th>
											</tr>
											<tr data-ng-repeat=" (key,value) in angularMap.data ">
												<td>{{key}}</td>
												<td>{{value.length}}</td>
											</tr>

										</table>

									</div>
								</div>
							</div>

							<!-- ================================================= -->
						</div>
					</div>

				</div>
				<!--Content end-->

				<div class="boxpanel" style="height: 100px" data-ng-if="tag === 7">
					<div class="grid-content" style="height:400px;">
						<span>Total Number of Repeaters to be installed
							:{{totalNoOfRepeatersToBeInstalled}}</span><br> <span>Total
							Number of Repeaters to be Assigned :{{assignedRepeaters}}</span><br>

						<div class="form-group">
							<label class="control-label">&nbsp;</label>
							<button class="default ic_reset"
								data-ng-disabled="enableDisableRepeaterGenerateBtn()"
								data-ng-click="generateRepeaterInstallationFiles()">Generate
								Installation Files</button>
						</div>
					</div>
				</div>
			</div>
			 <div>
			     <div class="boxpanel" data-ng-if="tag === 8">
			      <table>
			       <tr>
			        <td
			         style="width: 10%; text-align: right; vertical-align: middle;">Select
			         File To View:</td>
					<td style="width: 1%"></td>
			        <td>
				        <select class="form-control" data-ng-model="repeaterFileName" data-ng-options="x for x in fileNameList"
				         data-ng-change="viewRepeaterFilesData(repeaterFileName)" style="width: 30%;">
				        </select>
				        <!-- <select class="form-control" data-ng-model="dcFileName" data-ng-change="viewDCFilesData(dcFileName)" style="width: 30%;">
				        	<option value="" selected="selected">Select Options</option>
				        	<option data-ng-repeat="x in fileNameList">{{x}}</option>
				        </select> -->
			        </td>
			       </tr>
			      </table>
			     </div>
			     <div class="boxpanel" data-ng-show="dcData && tag === 8 && tag<=8">
			      <div class="box-body" style="width: 100%">
			       <div class="grid-content" style="overflow: scroll; height: 400px; width: 100%; float: left">
			        <table class="grid">
			         <tr>
			          	<th>Repeater Location</th>
						<th>Latitude</th>
						<th>Longitude</th>
						<th>Slots Required</th>
						<th>Levels Required</th>
			         </tr>
			         <tr data-ng-repeat="x in dcData track by $index">
			          <td data-ng-repeat="y in x track by $index"><label
			           data-ng-bind="x[$index]"></label></td>
			         </tr>
			        </table>
			        <p style="text-align: center; margin-top: 20px;">
			         <button data-ng-click="assignRepeatersToInstaller()"
			          data-ng-mouseenter="changeBorder('assign')"
			          data-ng-mouseleave="recoverBorder('assign')"
			          data-ng-style="borderColor">Assign To Installer</button>
			         &nbsp;&nbsp;
			         <button data-ng-click="discardRepeaterInstallationFiles()"
			          data-ng-mouseenter="changeBorder('discard')"
			          data-ng-mouseleave="recoverBorder('discard')"
			          data-ng-style="discardBorderColor">Discard Files</button>
			        </p>
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