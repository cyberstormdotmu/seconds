kenureApp.controller('maintenancecontroller', ['$scope','$http','$location','$window','$uibModal','$filter',function($scope, $http,$location,$window,$modal,$filter){
	$scope.paginationList = [10,20,30,40,50];
	$scope.isError = false;
	$scope.errorMessage = "";
	
	//Method for search asset Inspection Schedule
	$scope.searchAssetInspectionSchedule = function(){
		
		var selectedReport = $scope.selectedReport;
		var selectedAsset = $scope.selectedAsset;
		var selectedInspectionInterval = $scope.selectedInspectionInterval;
		var inspectionDueWithin = $scope.inspectionDueWithin;
		var siteId = $scope.siteId;
		var siteName = $scope.siteName;
		
		$http({
			method: "POST",
			url: url + "/customerOperation/searchAssetInspectionSchedule",
			params: {selectedReport:selectedReport, selectedAsset:selectedAsset, selectedInspectionInterval:selectedInspectionInterval,
				inspectionDueWithin:inspectionDueWithin,siteId:siteId,
				siteName:siteName},
		}).success(function (data) {

			if(angular.equals(data,'noAssetInspectionScheduleFound')){
				$scope.noAssetInspectionScheduleFound = true;
				$scope.assetInspectionScheduleDiv = false;
				$scope.assetInspectionScheduleForDCDiv = false;
			}else{
				if(selectedAsset == "Endpoints"){
					$scope.noAssetInspectionScheduleFound = false;
					$scope.assetInspectionScheduleForDCDiv = false;
					$scope.assetInspectionScheduleDiv = true;
					$scope.assetInspectionScheduleList = data;
					// For pagination
					$scope.recordSize = 10;
					$scope.totalItems = $scope.assetInspectionScheduleList.length;
					$scope.currentPage = 1;
					$scope.itemsPerPage = $scope.recordSize;
					$scope.maxSize = 5; //Number of pager buttons to show
				}
				if(selectedAsset == "DataCollectors"){
					$scope.noAssetInspectionScheduleFound = false;
					$scope.assetInspectionScheduleDiv = false;
					$scope.assetInspectionScheduleForDCDiv = true;
					$scope.assetInspectionScheduleListForDC = data;
					// For pagination
					$scope.recordSize = 10;
					$scope.totalItems = $scope.assetInspectionScheduleListForDC.length;
					$scope.currentPage = 1;
					$scope.itemsPerPage = $scope.recordSize;
					$scope.maxSize = 5; //Number of pager buttons to show
				}
			}
		}).error(function (data,status){

		});
	}
	
	// For Pagination - Start
	$scope.setPage = function (pageNo) {
		$scope.currentPage = pageNo;
	};

	$scope.pageChanged = function() {
		console.log('Page changed to: ' + $scope.currentPage);
	};

	$scope.setItemsPerPage = function(num) {
		$scope.itemsPerPage = num;
		$scope.currentPage = 1; //reset to first page
	}
	// Pagination End
	
	// Datepicker code start
	$scope.today = function() {
		$scope.activedate = new Date();
		$scope.expirydate;

	};
	$scope.today();

	$scope.clear = function() {
		$scope.activedate = null
		$scope.expirydate = null
	};

	$scope.dateOptions = {
			dateDisabled: disabled,
			formatYear: 'yy',
			maxDate: new Date(2020, 5, 22),
			minDate: new Date(),
			startingDay: 1
	};

	// Disable weekend selection
	function disabled(data) {
		var date = data.date,
		mode = data.mode;
		return mode === 'day' && (date.getDay() === 0 || date.getDay() === 6);
	}

	$scope.toggleMin = function() {
		$scope.inlineOptions.minDate = $scope.inlineOptions.minDate ? null : new Date();
		$scope.dateOptions.minDate = $scope.inlineOptions.minDate;
	};

	$scope.open1 = function() {
		$scope.popup1.opened = true;
	};

	$scope.open2 = function() {
		$scope.popup2.opened = true;
	};

	$scope.setDate = function(year, month, day) {
		$scope.activedate = new Date(year, month, day);
		$scope.expirydate = new Date(year, month, day);
	};

	$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
	$scope.format = $scope.formats[0];
	$scope.altInputFormats = ['M!/d!/yyyy'];

	$scope.popup1 = {
			opened: false
	};

	$scope.popup2 = {
			opened: false
	};
	// Datepicker code ends
	
	
	$scope.showReportPeriodDiv = function(){
		var selectedReport = $scope.selectedReport;
		
		if(selectedReport == "Day" || selectedReport == "Week" || selectedReport == "Month"){
			$scope.reportPeriodDiv = true;
			$scope.startDateDiv = false;
			$scope.endDateDiv = false;
		}
		if(selectedReport == "Select Dates"){
			$scope.reportPeriodDiv = false;
			$scope.startDateDiv = true;
			$scope.endDateDiv = true;
		}
		if(selectedReport == "---Please Select Report---" || selectedReport == ""){
			$scope.reportPeriodDiv = false;
			$scope.startDateDiv = false;
			$scope.endDateDiv = false;
		}
	}
	
	//Search and generate consumer meter alert report
	$scope.searchConsumerMeterAlerts = function(){
		
		var selectedReport = $scope.selectedReport;
		var reportPeriod = $scope.reportPeriod;
		var startDate = $filter('date')($scope.startDate,"dd/MM/yyyy");
		var endDate = $filter('date')($scope.endDate,"dd/MM/yyyy");
		var selectedAlertType = $scope.selectedAlertType;
		var alerts = $scope.alerts;
		var siteId = $scope.siteId;
		var siteName = $scope.siteName;
		var installationName = $scope.installationName;
		var zipCode = $scope.zipCode;
		if($scope.reportPeriodDiv == true && reportPeriod == null){
			$scope.isError = true;
			$scope.errorMessage = "Please Enter Report Period!";
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isError = false;
						});
					}, 3000);
		}

		if($scope.startDateDiv == true && startDate == null){
			$scope.isError = true;
			$scope.errorMessage = "Please Enter Start Date!";
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isError = false;
						});
					}, 3000);
		}

		if($scope.endDateDiv == true && endDate == null && $scope.startDateDiv == true && startDate !=null){
			$scope.isError = true;
			$scope.errorMessage = "Please Enter End Date!";
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isError = false;
						});
					}, 3000);
		}
		$http({
			method: "POST",
			url: url + "/customerOperation/searchConsumerMeterAlerts",
			params: {selectedReport:selectedReport, reportPeriod:reportPeriod, startDate:startDate,
				endDate:endDate,selectedAlertType:selectedAlertType,alerts:alerts,siteId:siteId,
				siteName:siteName,installationName:installationName,zipCode:zipCode},
		}).success(function (data) {

			if(angular.equals(data,'noConsumerMeterAlertsFound')){
				$scope.noConsumerMeterAlertsFoundDiv = true;
				$scope.consumerMeterAlertDiv = false;
			}else{
				$scope.noConsumerMeterAlertsFoundDiv = false;
				$scope.consumerMeterAlertDiv = true;

				$scope.consumerMeterAlertsList = $filter('orderBy')(data,"registerId");
				// For pagination
				$scope.recordSize = 10;
				$scope.totalItems = $scope.consumerMeterAlertsList.length;
				$scope.currentPage = 1;
				$scope.itemsPerPage = $scope.recordSize;
				$scope.maxSize = 5; //Number of pager buttons to show
			}
		}).error(function (data,status){

		});
	}
	
	$scope.refreshAssetInspectionPage = function(){
		window.location.href="assetInspectionSchedule";
	}
	
	$scope.refreshConsumerAlertPage = function(){
		window.location.href="consumerMeterAlerts";
	}
	
	$scope.refreshNetworkAlerts = function(){
		window.location.href="networkAlerts";
	}
	
	$scope.refreshBatteryReplacementSchedulePage = function(){
		window.location.href="batteryReplacementSchedule";
	}
	
	//Search and generate network alert report
	$scope.searchNetworkAlerts = function(){
		
		var selectedReport = $scope.selectedReport;
		var reportPeriod = $scope.reportPeriod;
		var startDate = $filter('date')($scope.startDate,"dd/MM/yyyy");
		var endDate = $filter('date')($scope.endDate,"dd/MM/yyyy");
		var selectedAlertType = $scope.selectedAlertType;
		var alerts = $scope.alerts;
		var siteId = $scope.siteId;
		var siteName = $scope.siteName;
		var installationName = $scope.installationName;
		var dcSerialNo = $scope.dcSerialNo;
		if($scope.reportPeriodDiv == true && reportPeriod == null){
			$scope.isError = true;
			$scope.errorMessage = "Please Enter Report Period!";
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isError = false;
						});
					}, 3000);
		}

		if($scope.startDateDiv == true && startDate == null){
			$scope.isError = true;
			$scope.errorMessage = "Please Enter Start Date!";
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isError = false;
						});
					}, 3000);
		}

		if($scope.endDateDiv == true && endDate == null && $scope.startDateDiv == true && startDate !=null){
			$scope.isError = true;
			$scope.errorMessage = "Please Enter End Date!";
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isError = false;
						});
					}, 3000);
		}
		$http({
			method: "POST",
			url: url + "/customerOperation/searchNetworkAlerts",
			params: {selectedReport:selectedReport, reportPeriod:reportPeriod, startDate:startDate,
				endDate:endDate,selectedAlertType:selectedAlertType,alerts:alerts,siteId:siteId,
				siteName:siteName,installationName:installationName,dcSerialNo:dcSerialNo},
		}).success(function (data) {

			if(angular.equals(data,'noNetworkAlertsFound')){
				$scope.noNetwrokAlertsFoundDiv = true;
				$scope.networkAlertDiv = false;
			}else{
				$scope.noNetwrokAlertsFoundDiv = false;
				$scope.networkAlertDiv = true;
				$scope.networkAlertsList = data;
				// For pagination
				$scope.recordSize = 10;
				$scope.totalItems = $scope.networkAlertsList.length;
				$scope.currentPage = 1;
				$scope.itemsPerPage = $scope.recordSize;
				$scope.maxSize = 5; //Number of pager buttons to show
			}
		}).error(function (data,status){

		});
	}
	
	// Popup Open and close for Consumer Meter alert action for particular register Id
	$scope.openPopupForAlertAction = function (registerId,dateFlagged) {

		var ModalInstance = $modal.open({
				size: 'sm',
				controller: function($uibModalInstance,$scope,registerId,dateFlagged){
					$scope.registerId = registerId;
					$scope.dateFlagged = dateFlagged;
					$scope.getTechnicians = function(){
						$http({
							method: "POST",
							url: url + "/customerOperation/getTechnicianByCustomerId"
						}).success(function (data) {
							$scope.finalList = data;
						}).error(function (data,status){

						});
					}
					
					$scope.close = function () {
						$uibModalInstance.dismiss('cancel');
					};
					$scope.updateConsumerMeterAlert = function () {
						
						var acknowledge = $scope.acknowledge;
						var requestToResetAllAlerts = $scope.requestToResetAllAlerts;
						var assignToTechnician = $scope.assignToTechnician;
						var technicianName = $scope.technicianName;
						$http({
							method: "POST",
							url: url + "/customerOperation/updateAlertStatusForConsumerMeters",
							params: {acknowledge:acknowledge,requestToResetAllAlerts:requestToResetAllAlerts,assignToTechnician:assignToTechnician,
								technicianName:technicianName,registerId:registerId,dateFlagged:dateFlagged},
						}).success(function (data) {
							if(angular.equals(data,'success')){
								$scope.successMessage = "Alert Action Performed Successfully!";
								$scope.isSuccess = true;
								window.setTimeout(function() {
									window.location.reload();
								}, 2000);
							}else if(angular.equals(data,'namecannotbenull')){

								
								$scope.errorMessage = "Please select technician !";
								$scope.isError = true;
								setTimeout(function ()
										{
									$scope.$apply(function()
											{
										$scope.isError = false;
										$scope.errorMessage = "";
											});
										}, 3000);
							
							}else{
								$scope.errorMessage = "Cannot Perform Alert Action!";
								$scope.isError = true;
								setTimeout(function ()
										{
									$scope.$apply(function()
											{
										$scope.isError = false;
										$scope.errorMessage = "";
											});
										}, 3000);
							}
						}).error(function (data,status){

						});
					}
				},
				resolve: {
						registerId: function () {
						return registerId;
					},dateFlagged: function () {
						return dateFlagged;
					}
				},
				templateUrl: url+'/customerOperation/consumerAlertsActionPopup',
			});
	}
	
	// Popup Open and close for Network alert action for particular register Id
	$scope.openPopupForAlertActionDC = function (dcSerialNo) {

		var ModalInstance = $modal.open({
				size: 'sm',
				controller: function($uibModalInstance ,$scope,dcSerialNo){
					$scope.dcSerialNo = dcSerialNo;
					$scope.getTechnicians = function(){
						$http({
							method: "POST",
							url: url + "/customerOperation/getTechnicianByCustomerId"
						}).success(function (data) {
							$scope.finalList = data;
						}).error(function (data,status){

						});
					}
					
					$scope.close = function () {
						$uibModalInstance.dismiss('cancel');
					};
					$scope.updateNetworkMeter = function () {
						
						var acknowledge = $scope.acknowledge;
						var assignToTechnician = $scope.assignToTechnician;
						var technicianName = $scope.technicianName;
						$http({
							method: "POST",
							url: url + "/customerOperation/updateAlertStatusForDC",
							params: {acknowledge:acknowledge,assignToTechnician:assignToTechnician,
								technicianName:technicianName,dcSerialNo:dcSerialNo},
						}).success(function (data) {
							if(angular.equals(data,'success')){
								$scope.successMessage = "Alert Action Performed Successfully!";
								$scope.isSuccess = true;
								window.setTimeout(function() {
									window.location.reload();
								}, 2000);
							}else if(angular.equals(data,'namecannotbenull')){

								
								$scope.errorMessage = "Please select technician !";
								$scope.isError = true;
								setTimeout(function ()
										{
									$scope.$apply(function()
											{
										$scope.isError = false;
										$scope.errorMessage = "";
											});
										}, 3000);
							
							}else{
									
								$scope.errorMessage = "Cannot Perform Alert Action!";
								$scope.isError = true;
								setTimeout(function ()
										{
									$scope.$apply(function()
											{
										$scope.isError = false;
										$scope.errorMessage = "";
											});
										}, 3000);
							}
						}).error(function (data,status){

						});
					}
				},
				resolve: {
					dcSerialNo: function () {
						return dcSerialNo;
					}
				},
				templateUrl: url+'/customerOperation/networkAlertsActionPopup',
			});
	}
	
	
	// Dhaval Code Start
	
	$scope.initDCMessageQueueData = function(){
		
		$scope.regionSelected = true;
		$scope.siteSelected = true;
		$scope.messageQueueListShow = false;
		
		$http({
			method: "POST",
			url:url+"/customerOperation/initDCMessageQueueData",
		}).success(function(data){
			if(angular.equals(data,'youCanNotAccessRegionList')){
				$window.location.href="customerDashboard";
			}
			$scope.regionList = data[0];
		}).error(function(data,status){
			// Error Code
		});
	}
	
	$scope.getSiteList = function(){
		
		$scope.regionSelected = true;
		$scope.siteSelected = true;
		
		$scope.selectedSite = null;
		$scope.selectedDc = null;
		
		$http({
			method: "POST",
			url:url+"/customerOperation/getSiteList",
			params:{regionId : $scope.selectedRegion},
		}).success(function(data){
			if(angular.equals(data,'error')){
				$scope.regionSelected = true;
				$scope.messageQueueListShow = false;
			}else{
				$scope.siteList = data[0];
				$scope.regionSelected = false;
			}
		}).error(function(data,status){
			// Error Code
		});
	}
	
	$scope.getDCList = function(){
		
		$scope.regionSelected = true;
		$scope.siteSelected = true;
		
		$http({
			method: "POST",
			url:url+"/customerOperation/getDCList",
			params:{siteId : $scope.selectedSite},
		}).success(function(data){
			if(angular.equals(data,'youCanNotAccessDCList')){
				$window.location.href="customerDashboard";
			}
			$scope.dcList = data[0];
			$scope.regionSelected = false;
			$scope.siteSelected = false;
		}).error(function(data,status){
			// Error Code
		});
	}
	
	$scope.getMessageList = function(){
		
		$http({
			method: "POST",
			url:url+"/customerOperation/getMessageList",
			params:{datacollectorId : $scope.selectedDc},
		}).success(function(data){
			
			$scope.messageQueueListShow = true;
			$scope.messageQueueList = data[0];
			
		}).error(function(data,status){
			// Error Code
		});
		
	}
	// Dhaval Code End
	
	
	//Battery replacement schedule Code Start
	
	$scope.showReplacementDueDiv = function(){
		var selectedReport = $scope.selectedReport;
	
		if(selectedReport == "Week" || selectedReport == "Month"){
			$scope.replacementDueDiv = true;
			$scope.isError = false;
			$scope.errorMessage = "";
		}
		
		if(selectedReport == "---Please Select Report---" || selectedReport == ""){
			$scope.replacementDueDiv = false;
			$scope.isError = false;
			$scope.errorMessage = "";
			$scope.reportPeriod="";
		}
	}
	
	$scope.searchEPForBatteryReplacement = function() {
		if($scope.selectedReport != undefined && $scope.reportPeriod != undefined){
			var installation = "";
			var siteId = "";
			var siteName = "";
			var zipcode = "";
			
			if($scope.installation != undefined){
				installation = $scope.installation;
			}
			if($scope.siteId != undefined){
				siteId = $scope.installation;
			}
			if($scope.siteName != undefined){
				siteName = $scope.siteName;
			}
			if($scope.zipcode != undefined){
				zipcode = $scope.zipcode;
			}
			$http({
				method: "POST",
				url:url+"/customerOperation/getBatteryReplacementData",
				params:{selectedReport : $scope.selectedReport,reportPeriod:$scope.reportPeriod,
					installation:installation,siteId:siteId,siteName:siteName,zipcode:zipcode},
			}).success(function(data){
				
			$scope.isError = false;
			$scope.errorMessage = "";
			if(data == 'NoBatteryData'){

				$scope.noConsumerMeterAlertsFoundDiv = true;
				$scope.consumerMeterAlertDiv = false;
			}else {
				$scope.meterData = data[0];
				$scope.noConsumerMeterAlertsFoundDiv = false;
				$scope.consumerMeterAlertDiv = true;
				// For pagination
				$scope.recordSize = 10;
				$scope.totalItems = $scope.meterData.length;
				$scope.currentPage = 1;
				$scope.itemsPerPage = $scope.recordSize;
				$scope.maxSize = 5; 
			}
				
			}).error(function(data,status){
				// Error Code
			});
		}else{
			$scope.isError = true;
			$scope.errorMessage = "Please select proper criteria";
		}
	}
	
	//Battery replacement schedule Code End
	
	
	
}]);