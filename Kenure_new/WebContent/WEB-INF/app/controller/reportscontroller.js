kenureApp.controller('reportscontroller', ['$scope','$http','$location','$window','$filter',function($scope, $http,$location,$window,$filter){
	$scope.paginationList = [10,20,30,40,50];

	//Map
	function Map() {
		this.keys = new Array();
		this.data = new Object();

		this.put = function (key, value) {
			if (this.data[key] == null) {
				this.keys.push(key);
			}
			this.data[key] = value;
		};

		this.get = function (key) {
			return this.data[key];
		};

		this.each = function (fn) {
			if (typeof fn != 'function') {
				return;
			}
			var len = this.keys.length;
			for (var i = 0; i < len; i++) {
				var k = this.keys[i];
				fn(k, this.data[k], i);
			}
		};

		this.entrys = function () {
			var len = this.keys.length;
			var entrys = new Array(len);
			for (var i = 0; i < len; i++) {
				entrys[i] = {
						key: this.keys[i],
						value: this.data[i]
				};
			}
			return entrys;
		};

		this.remove = function (key) {
			delete this.data[key];
		}

		this.isEmpty = function () {
			return this.keys.length == 0;
		};

		this.size = function () {
			return this.keys.length;
		};
	}

	// For Pagination - Start
	$scope.setPage = function (pageNo) {
		$scope.currentPage = pageNo;
	};

	$scope.pageChanged = function() {
		console.log('Page changed to: ' + $scope.currentPage);
	};

	$scope.pageChangedForNwAlert = function() {
		console.log('Page changed to: ' + $scope.currentPageForNwAlert);
	};

	$scope.setItemsPerPage = function(num) {
		$scope.itemsPerPage = num;
		$scope.currentPage = 1; //reset to first page
	}

	$scope.setItemsPerPageForNwAlert = function(num) {
		$scope.itemsPerPageForNwAlert = num;
		$scope.currentPageForNwAlert = 1; //reset to first page
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


	$scope.searchNetworkWaterLoss = function(){

		var startDate = $filter('date')($scope.startDate,"dd/MM/yyyy");
		var endDate = $filter('date')($scope.endDate,"dd/MM/yyyy");
		var acceptableLoss = $scope.acceptableLoss;

		$http({
			method: "POST",
			url: url + "/customerOperation/reportsOperation/searchNetworkWaterLoss",
			params: {startDate:startDate,endDate:endDate,acceptableLoss:acceptableLoss},
		}).success(function (data) {

			if(angular.equals(data,'noNetworkWaterLossFound')){
				$scope.noNetworkWaterLossFound = true;
				$scope.networkWaterLossDiv = false;
			}else{
				$scope.noNetworkWaterLossFound = false;
				$scope.networkWaterLossDiv = true;

				$scope.networkWaterLossList = data;
				// For pagination
				$scope.recordSize = 10;
				$scope.totalItems = $scope.networkWaterLossList.length;
				$scope.currentPage = 1;
				$scope.itemsPerPage = $scope.recordSize;
				$scope.maxSize = 5; //Number of pager buttons to show
			}
		}).error(function (data,status){

		});
	}

	$scope.exportToCsvForWaterLoss = function(){
		var result = angular.toJson($scope.networkWaterLossList);
		var header = [['District Meter Serial Number','District Meter Reading','Billing Start Date','Billing End Date','Total Consumer Consumption','Water Loss','Percentage']];
		ConvertExportToCSV(result,header,'network_water_loss_report.csv');
	}

	$scope.refreshNetworkWaterLossPage = function(){
		window.location.href="networkWaterLossReport";
	}

	$scope.refreshAlertReportPage = function(){
		window.location.href="alertReport";
	}

	$scope.refreshAggregateConsumptionReportPage = function(){
		/*window.location.href="aggregateConsumptionReport";*/
		$scope.selectedReport = "default";
		$scope.installationName = "";
		$scope.siteName = "";
		$scope.dcSerialNumber = "";
		$scope.startDateDiv = false;
		$scope.endDateDiv = false;
		$scope.reportPeriodDiv = false;
	}

	$scope.showReportPeriodDiv = function(){
		var selectedReport = $scope.selectedReport;

		if(selectedReport == "Week" || selectedReport == "Month"){
			$scope.reportPeriodDiv = true;
			$scope.startDateDiv = false;
			$scope.endDateDiv = false;
		}
		if(selectedReport == "Select Dates"){
			$scope.reportPeriodDiv = false;
			$scope.startDateDiv = true;
			$scope.endDateDiv = true;
		}
		if(selectedReport == "---Please Select Report---" || selectedReport == "" || selectedReport == undefined){
			$scope.reportPeriodDiv = false;
			$scope.startDateDiv = false;
			$scope.endDateDiv = false;
		}
	}

	$scope.showdiv = function(){

		var selectedAlertType = $scope.selectedAlertType;
		if(selectedAlertType == "Consumer Alerts" || selectedAlertType == "All"){
			$scope.zipcodediv = true;
			$scope.consumerAccNodiv = true;
			$scope.address1div = true;
		}
		if(selectedAlertType == "Network Alerts"){
			$scope.zipcodediv = false;
			$scope.consumerAccNodiv = false;
			$scope.address1div = false;
		}
	}

	$scope.generateAlertReports = function(){

		var selectedReport = $scope.selectedReport;
		var reportPeriod = $scope.reportPeriod;
		var startDate = $filter('date')($scope.startDate,"dd/MM/yyyy");
		var endDate = $filter('date')($scope.endDate,"dd/MM/yyyy");
		var selectedAlertType = $scope.selectedAlertType;
		var alerts = $scope.alerts;
		var installationName = $scope.installationName;
		var siteId = $scope.siteId;
		var siteName = $scope.siteName;
		var zipCode = $scope.zipCode;
		var consumerAccNo = $scope.consumerAccNo;
		var address1 = $scope.address1;


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
		if(selectedAlertType == "Consumer Alerts" || selectedAlertType == "All"){

			$http({
				method: "POST",
				url: url + "/customerOperation/reportsOperation/generateAlertReportForConsumerMeter",
				params: {selectedReport:selectedReport,reportPeriod:reportPeriod,startDate:startDate,endDate:endDate,
					alerts:alerts,installationName:installationName,siteId:siteId,siteName:siteName,
					zipCode:zipCode,consumerAccNo:consumerAccNo,address1:address1},
			}).success(function (data) {
				if(angular.equals(data,'noConsumerMeterAlertsFound')){
					$scope.consumerMeterAlertDiv = false;
					$scope.noConsumerMeterAlertsFoundDiv = true;
					if(selectedAlertType == "Consumer Alerts"){
						$scope.noNetwrokAlertsFoundDiv = false;
					}
				}else{
					if(selectedAlertType == "Consumer Alerts"){
						$scope.networkAlertDiv = false;
						$scope.noNetwrokAlertsFoundDiv = false;
					}
					$scope.noConsumerMeterAlertsFoundDiv = false;
					$scope.consumerMeterAlertDiv = true;
					$scope.consumerMeterAlertsList = data;

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

		if(selectedAlertType == "Network Alerts" || selectedAlertType == "All"){

			$http({
				method: "POST",
				url: url + "/customerOperation/reportsOperation/generateAlertReportForNetwork",
				params: {selectedReport:selectedReport,reportPeriod:reportPeriod,startDate:startDate,endDate:endDate,
					alerts:alerts,installationName:installationName,siteId:siteId,siteName:siteName,
					zipCode:zipCode,consumerAccNo:consumerAccNo,address1:address1},
			}).success(function (data) {
				if(angular.equals(data,'noNetworkAlertsFound')){
					$scope.noNetwrokAlertsFoundDiv = true;
					$scope.networkAlertDiv = false;
					if(selectedAlertType == "Network Alerts"){
						$scope.noConsumerMeterAlertsFoundDiv = false;
					}
				}else{
					if(selectedAlertType == "Network Alerts"){
						$scope.noConsumerMeterAlertsFoundDiv = false;
						$scope.consumerMeterAlertDiv = false;
					}
					$scope.noNetwrokAlertsFoundDiv = false;
					$scope.networkAlertDiv = true;
					$scope.networkAlertsList = data;
					// For pagination
					$scope.recordSizeForNwAlert = 10;
					$scope.totalItemsForNwAlert = $scope.networkAlertsList.length;
					$scope.currentPageForNwAlert = 1;
					$scope.itemsPerPageForNwAlert = $scope.recordSizeForNwAlert;
					$scope.maxSize = 5; //Number of pager buttons to show
				}
			}).error(function (data,status){

			});
		}
	}

	$scope.exportToCsvForConsumerAlert = function(){
		var result = angular.toJson($scope.consumerMeterAlertsList);
		var header = [['Consumer Account Number','Site Id','Zipcode','Alert','Date Flagged','Date Cleared']];
		ConvertExportToCSV(result,header,'consumer_meter_alert_report.csv');
	}

	$scope.exportToCsvForNetworkAlert = function(){
		var result = angular.toJson($scope.networkAlertsList);
		var header = [['Site Id','DC Serial No','Alert','Date Flagged','Date Cleared']];
		ConvertExportToCSV(result,header,'network_alert_report.csv');
	}

	$scope.generateAggregateConsumptionReport = function(){

		var selectedReport = $scope.selectedReport;
		var reportPeriod = $scope.reportPeriod;
		var startDate = $filter('date')($scope.startDate,"dd/MM/yyyy");
		var endDate = $filter('date')($scope.endDate,"dd/MM/yyyy");
		var installationName = $scope.installationName;
		var siteId = $scope.siteId;
		var siteName = $scope.siteName;
		var zipCode = $scope.zipCode;
		var consumerAccNo = $scope.consumerAccNo;
		var address1 = $scope.address1;
		var noOfOccupants = $scope.noOfOccupants;

		$http({
			method: "POST",
			url: url + "/customerOperation/reportsOperation/generateAggregateConsumptionReport",
			params: {selectedReport:selectedReport,reportPeriod:reportPeriod,startDate:startDate,endDate:endDate,
				installationName:installationName,siteId:siteId,siteName:siteName,
				zipCode:zipCode,consumerAccNo:consumerAccNo,address1:address1,noOfOccupants:noOfOccupants},
		}).success(function (data) {
			if(angular.equals(data,'noSuchRecordsFound')){
				$scope.aggrgateConsumptionReportDiv = false;
				$scope.noSuchRecordsFoundDiv = true;
			}else{
				$scope.noSuchRecordsFoundDiv = false;
				$scope.aggrgateConsumptionReportDiv = true;
				$scope.aggregateConsumptionDiv = true;
				$scope.aggregateConsumptionList = data;

				var sum = 0;
				for(var i=0;i<data.length;i++)
				{
					sum += parseInt(data[i].totalConsumption, 10);
				}
				var average = (sum/data.length).toFixed(2);
				$scope.average = average;
				// For pagination
				$scope.recordSize = 10;
				$scope.totalItems = $scope.aggregateConsumptionList.length;
				$scope.currentPage = 1;
				$scope.itemsPerPage = $scope.recordSize;
				$scope.maxSize = 5; //Number of pager buttons to show
			}	
		}).error(function (data,status){

		});
	}

	$scope.exportToCsvForAggrgateConsumptionReport = function(){
		var result = angular.toJson($scope.aggregateConsumptionList);
		var header = [['Consumer Account No','Total Consumption']];
		ConvertExportToCSV(result,header,'aggregate_consumption_report.csv');
	}

	// Dhaval Code Start

	$scope.initDataUsageReportData = function(){
		$scope.dcDataUsageListFound = false;
		$scope.noDcDataUsageListFoundDiv = false;

		$http({
			method: "POST",
			url: url + "/customerOperation/reportsOperation/initDataUsageReportData",
		}).success(function (data) {

			if(angular.equals(data,'dataPlanExpired')){
				$scope.totalUsage = '-';
				$scope.usagePer = '-';
				$scope.dataPlan = '-';
				
				swal("Warning !", "Your Data Plan is expired. Please contact Admin.", "warning");
			}else{
				$scope.totalUsage = $filter('number')(data.totalUsage, 2);
				$scope.usagePer = data.usagePer;
				$scope.dataPlan = data.dataPlan;
			}
			
			
		}).error(function (data,status){

		});
	}

	$scope.resetSearch = function(){
		$scope.dcDataUsageListFound = false;
		$scope.noDcDataUsageListFoundDiv = false;
		$scope.siteId = null;
		$scope.siteName = null;
		$scope.installationName = null;
		$scope.dcSerial = null;
	}

	$scope.searchDataUsageReport = function(){

		var siteId = $scope.siteId;
		var siteName = $scope.siteName;
		var installationName = $scope.installationName;
		var dcSerial = $scope.dcSerial;

		if (!isNaN(siteId) || siteId < 0 || siteId == null || siteId == 'undefined') {
			$http({
				method: "POST",
				url: url + "/searchDataUsageReport",
				params: {siteId : siteId, siteName : siteName, installationName : installationName, dcSerial : dcSerial},
			}).success(function (data) {

				if(angular.equals(data,'nosuchdcusagefound')){
					$scope.noDcDataUsageListFoundDiv = true;
					$scope.dcDataUsageListFound = false;
				}

				if(!angular.equals(data,'nosuchdcusagefound')){
					$scope.dcDataUsageList = data[0];
					$scope.dcDataUsageListFound = true;
					$scope.noDcDataUsageListFoundDiv = false;

					$scope.recordSize = 10;
					$scope.totalItems = $scope.dcDataUsageList.length;
					$scope.currentPage = 1;
					$scope.itemsPerPage = $scope.recordSize;
					$scope.maxSize = 5; //Number of pager buttons to show
				}

			}).error(function (data,status){

			});
		}
	}

	$scope.downloadResultCSVfile = function(){
		var result = angular.toJson($scope.dcDataUsageList);
		var header = [['DC Serial Number','Data Usage']];
		ConvertExportToCSV(result,header,'data_usage_report.csv');
	}

	var map = new Map();
	var billingDataHeaderConversion = new Map();
	$scope.initBillingDataReport = function(){

		$scope.showBillingDataListDiv = false;
		$scope.noBillingDataFoundDiv = false;

		$http({
			method: "POST",
			url: url + "/customerOperation/reportsOperation/initBillingDataReport",
		}).success(function (data) {
			map.put("col1", "consumerAccNum");
			map.put("col2", "");
			map.put("col3", "");
			map.put("col4", "");
			map.put("col5", "");
			map.put("col6", "");
			map.put("col7", "");
			map.put("col8", "");
			map.put("col9", "");
			map.put("col10", "");
			map.put("col11", "isEstimated");
			$scope.columnDataMap = data;
			
			billingDataHeaderConversion.put("consumerAccNum","Consumer Account Number");
			billingDataHeaderConversion.put("billingStartDate","Billing Start Date");
			billingDataHeaderConversion.put("currentReading","Current Reading");
			billingDataHeaderConversion.put("billDate","Bill Date");
			billingDataHeaderConversion.put("registerId","Register ID");
			billingDataHeaderConversion.put("lastReading","Last Reading");
			billingDataHeaderConversion.put("totalAmount","Total Amount");
			billingDataHeaderConversion.put("billingFrequency","Billing Frequency");
			billingDataHeaderConversion.put("billingEndDate","Billing End Date");
			billingDataHeaderConversion.put("consumedUnit","Consumed Unit");
			billingDataHeaderConversion.put("isEstimated","is-Estimated ?");
			
		}).error(function (data,status){

		});

	}
	
	$scope.getBillingReportHeaderBasedOnKey = function(key){
		return billingDataHeaderConversion.get(key);
	}
	
	$scope.searchBillingDataReport = function(){

		var billingStartDate = $filter('date')($scope.billingStartDate,"dd/MM/yyyy");
		var billingEndDate = $filter('date')($scope.billingEndDate,"dd/MM/yyyy");
		var onlyEstimatedReadAcc = $scope.onlyEstimatedReadAcc;

		if(billingStartDate != null && billingStartDate != "" && billingEndDate != null && billingEndDate != ""){
			$http({
				method: "POST",
				url: url + "/customerOperation/reportsOperation/searchBillingDataReport",
				params: {billingStartDate : billingStartDate,billingEndDate : billingEndDate, onlyEstimatedReadAcc : onlyEstimatedReadAcc},
			}).success(function (data) {

				if(data[0]){
					$scope.finalArray = [];
					var tableHeaderMap = new Map();
					angular.forEach(data[0],function(value,index){
						var tempMap = new Map();
						tempMap.data = value;
						var finalMap = new Map();
						for(var i=1;i<12;i++){
							var col = 'col'+i;
							if(map.get(col)!=""){
								finalMap.put(col,tempMap.get(map.get(col)));
								if(index==0){
									tableHeaderMap.put(col,map.get(col));
								}
							}
						}
						if(index==0){
							$scope.tableHeaderArray=[];
							$scope.tableHeaderArray.push(tableHeaderMap.data);
						}
						$scope.finalArray.push(finalMap.data);
					});

					$scope.billingDataList = data[0];
					$scope.showBillingDataListDiv = true;
					$scope.noBillingDataFoundDiv = false;

					$scope.recordSize = 10;
					$scope.totalItems = $scope.billingDataList.length;
					$scope.currentPage = 1;
					$scope.itemsPerPage = $scope.recordSize;
					$scope.maxSize = 5; //Number of pager buttons to show
				}
				if(angular.equals(data,'error')){
					$scope.noBillingDataFoundDiv = true;
					$scope.showBillingDataListDiv = false;
				}
			}).error(function (data,status){
			});

		}
	}

	$scope.funCalled = function(columnId,value){
		map.put(columnId,value);
	}
	
	var hashMapCSVBillingHeaderData = new Map();
	var tempArrayForBilling = new Array(); 
	$scope.billingDataCSVReportDownload = function(finalArray,tableHeaderArray){

		var result = angular.toJson(finalArray);
		
		hashMapCSVBillingHeaderData = new Map();
		var header = angular.toJson(tableHeaderArray);
		for (var i in tableHeaderArray[0]){
			hashMapCSVBillingHeaderData.put(i,billingDataHeaderConversion.get(tableHeaderArray[0][i]));
		}
		tempArrayForBilling[0] = (hashMapCSVBillingHeaderData.data);
		
		//var header = [['DC Serial Number','Data Usage']];
		header = angular.toJson(tempArrayForBilling);
		ConvertExportToCSV(result,header,'billing_data_report.csv');
	}

	// Dhaval Code End

	$scope.initNetworkConsumption = function(){
		$http({
			method: "POST",
			url:url+"/customerOperation/reportsOperation/initNetworkConsumption",
		}).success(function(data){
			$scope.duMeterList = data;
		}).error(function(data,status){
		});
	} 

	$scope.setPeriodTime = function() {
		var selectedPeriod = $scope.selectedPeriodType;

		if (!angular.equals(selectedPeriod,'hour') && !angular.equals(selectedPeriod,'day') && !angular.equals(selectedPeriod,'month') && !angular.equals(selectedPeriod,'year')) {
			$scope.periodTimeList = [];
			return;
		} else {
			$scope.message = "";
			$scope.nosuchSiteFoundDiv = false;

			if(angular.equals(selectedPeriod,'hour')) {
				$scope.periodTimeList = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24];
				$scope.selectedPeriodTime = 12;
			} else if(angular.equals(selectedPeriod,'day')) {
				$scope.periodTimeList = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30];
				$scope.selectedPeriodTime = 7;
			} else if(angular.equals(selectedPeriod,'month')) {
				$scope.periodTimeList = [1,2,3,4,5,6,7,8,9,10,11,12];
				$scope.selectedPeriodTime = 12;
			} else if(angular.equals(selectedPeriod,'year')) {
				$scope.periodTimeList = [1,2,3,4,5];
				$scope.selectedPeriodTime = 3;
			}
		}
	}

	$scope.getNWConsumptionGraph = function(){

		var selectedDUMeter = $scope.selectedDUMeter;
		var selectedPeriod = $scope.selectedPeriodType;
		var selectedPeriodTime = $scope.selectedPeriodTime;
		var showDataTbl = $scope.showDataTbl;

		if(angular.isUndefined(selectedDUMeter)) {
			$scope.nosuchSiteFoundDiv = true;
			$scope.message = "Please select any DistrictUtilityMeter";
			return;
		} else if(selectedPeriod == null || selectedPeriod == "" || angular.isUndefined(selectedPeriod)) {
			$scope.nosuchSiteFoundDiv = true;
			$scope.message = "Please select any Period";
			return;
		} else if(selectedPeriodTime == null || selectedPeriodTime == "" || angular.isUndefined(selectedPeriodTime)) {
			$scope.nosuchSiteFoundDiv = true;
			$scope.message = "Please select no of periods";
			return;
		} else {
			$http({
				method: "POST",
				url:url+"/customerOperation/reportsOperation/getNWConsumptionGraph",
				params:{
					duMeterId:selectedDUMeter.duMeterId,
					periodType:selectedPeriod,
					periodTime:selectedPeriodTime
				}
			}).success(function(response){
				$scope.chartData = response;
				drawChart(response);

				//chart_data
				if(showDataTbl) {
					showDataTableForConsumptionGraph(response);
				} else {
					$scope.tblShow = false;
				}

				$scope.message = "";
				$scope.nosuchSiteFoundDiv = false;
			}).error(function(data,status){
			});
		}
	}

	$scope.reset = function() { 
		$scope.selectedDUMeter = "";
		$scope.selectedPeriodType = "";
		$scope.selectedPeriodTime = "";

		$scope.chartShow = false;
		$scope.tblShow = false;
		$scope.showDataTbl = false;
	}

	function drawChart(datas) {
		$scope.chartShow = true;

		var data = new google.visualization.DataTable();
		data.addColumn('string', 'Period');
		data.addColumn('number', 'Network Consumption');

		data.addRows(JSON.parse(datas));

		var options = {
				title: 'Network Consumption Chart',
				width: 1600,
				height: 500,
				hAxis: {title: 'Period'},
				vAxis: {title: 'Network Consumption'},
				legend: { position: 'top', maxLines: 3, textStyle: {color: 'black', fontSize: 12 } },
				isStacked: true,
		};

		var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
		chart.draw(data, options);

	}

	$scope.exportToCSV = function() {
		var data = $scope.chartData;

		var header = [['Period','Network Consumption']];

		ConvertExportToCSV(data, header, "network_consumption.csv");         

	}


	// objDataArray = data , objHeaderArray = headers, filename
	function ConvertExportToCSV(objDataArray, objHeaderArray, filename) {
		var dataArray = typeof objDataArray != 'object' ? JSON.parse(objDataArray) : objDataArray;
		var headerArray = typeof objHeaderArray != 'object' ? JSON.parse(objHeaderArray) : objHeaderArray;

		var str = '';

		// printing header  
		for (var i = 0; i < headerArray.length; i++) {
			var line = '';
			for (var index in headerArray[i]) {
				if (line != '') line += ','

					line += headerArray[i][index];
			}
			str += line + '\r\n';
		}

		// printin data
		for (var i = 0; i < dataArray.length; i++) {
			var line = '';
			for (var index in dataArray[i]) {
				if (line != '') line += ','

					line += dataArray[i][index];
			}

			str += line + '\r\n';
		}

		var csv = str;

		var blob = new Blob([csv],{type: "text/csv;charset=utf-8;"});

		if (navigator.msSaveBlob) { // IE 10+
			navigator.msSaveBlob(blob, filename)
		} else {
			var link = document.createElement("a");
			if (link.download !== undefined) { // feature detection
				// Browsers that support HTML5 download attribute
				var url = URL.createObjectURL(blob);
				link.setAttribute("href", url);
				link.setAttribute("download", filename);
				link.style = "visibility:hidden";
				document.body.appendChild(link);
				link.click();
				document.body.removeChild(link);
			}           
		}
	}


	$scope.abnormalConsumptionExportData = "";
	$scope.thresholdDefaultValue ;

	$scope.initAbnormalConsumptionInitData = function(){

		$http({
			method: "POST",
			url:url+"/customerOperation/reportsOperation/initAbnormalConsumptionInitData",
		}).success(function(data){
			$scope.consumptionList = data[0];
			$scope.abnormalConsumptionExportData = data[1];
			$scope.thresholdFactor = data[2];
			$scope.thresholdDefaultValue = data[2] ;

			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.consumptionList.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show

		}).error(function(data){
		});
	}

	$scope.setConsumptionStyle = function(x){
		if(!(angular.isUndefined(x.last24hrUsage) && angular.isUndefined($scope.thresholdFactor))){
			if(x.last24hrUsage > (parseInt(x.averageConsumption) + parseInt(($scope.thresholdFactor*parseInt(x.averageConsumption)))/100)){
				return {color: "red"}
			}else{
				return {color: "green"}
			}
		}
	}

	$scope.valiateMaxLimit = function(){
		if($scope.thresholdFactor > 99 || angular.isUndefined($scope.thresholdFactor)){
			//alert("Max limit is 20 ... Setting up default value");
			//swal("Warning !", "Max limit is 20 .Setting up default value", "warning")

			swal({
				title: 'Warning !',
				text: 'Out of Max-Limit .Setting up default value',
				type: 'warning',
				confirmButtonText: "Ok",
			}, function(){
				$scope.thresholdFactor = $scope.thresholdDefaultValue;
			});

		}
	}

	$scope.downloadAbnormalConsumptionCSVFile = function(){
		var header = [['Consumer Account No','Average Consumption','24hr Consumption']];
		ConvertExportToCSV($scope.abnormalConsumptionExportData,header,'abnormal_consumption_report_data.csv');
	}

	// pct24 code ends here


	$scope.setBaseTypeIds = function() {
		var selectedBaseType = $scope.selectedBaseType;

		if (!angular.equals(selectedBaseType,'installation') && !angular.equals(selectedBaseType,'districtWaterMeter') && !angular.equals(selectedBaseType,'consumerAccNo') && !angular.equals(selectedBaseType,'registerId')) {
			$scope.seletedBaseType = "";
			$scope.installaionType = false;
			$scope.districtWaterMeterType = false;
			$scope.consumerAccNoType = false;
			$scope.registerIdType = false;
			return;
		} else {
			$scope.message = "";
			$scope.nosuchSiteFoundDiv = false;

			//baseTypeIdList
			if(angular.equals(selectedBaseType,'installation')) {
				$http({
					method: "POST",
					url:url+"/customerOperation/reportsOperation/getInstallationIds",
				}).success(function(data){
					$scope.installaionType = true;
					$scope.districtWaterMeterType = false;
					$scope.consumerAccNoType = false;
					$scope.registerIdType = false;

					$scope.baseTypeInstallationList = data;
				}).error(function(data,status){
				});
			} else if(angular.equals(selectedBaseType,'districtWaterMeter')) {
				$http({
					method: "POST",
					url:url+"/customerOperation/reportsOperation/initNetworkConsumption",
				}).success(function(data){
					$scope.districtWaterMeterType = true;
					$scope.installaionType = false;
					$scope.consumerAccNoType = false;
					$scope.registerIdType = false;

					$scope.baseTypeDWMList = data;
				}).error(function(data,status){
				});
			} else if(angular.equals(selectedBaseType,'consumerAccNo')) {
				$http({
					method: "POST",
					url:url+"/customerOperation/reportsOperation/getConsumerAccNo",
				}).success(function(data){
					$scope.consumerAccNoType = true;
					$scope.installaionType = false;
					$scope.districtWaterMeterType = false;
					$scope.registerIdType = false;

					$scope.baseTypeConsumerList = data;
				}).error(function(data,status){
				});
			} else if(angular.equals(selectedBaseType,'registerId')) {
				$http({
					method: "POST",
					url:url+"/customerOperation/reportsOperation/getRegisterIds",
				}).success(function(data){
					$scope.registerIdType = true;
					$scope.installaionType = false;
					$scope.districtWaterMeterType = false;
					$scope.consumerAccNoType = false;

					$scope.baseTypeRegisterList = data;
				}).error(function(data,status){
				});
			}
		}

	}


	$scope.getConsumptionGraph = function(){ 
		var flag = false;
		var id = null;

		var selectedBaseType = $scope.selectedBaseType;
		var selectedInstallation = $scope.selectedInstallation;
		var selectedDWT = $scope.selectedDWT;
		var selectedConsumerAccNo = $scope.selectedConsumerAccNo;
		var selectedRegisterId = $scope.selectedRegisterId;
		var selectedPeriodType = $scope.selectedPeriodType;
		var selectedPeriodTime = $scope.selectedPeriodTime;
		var showDataTbl = $scope.showDataTbl;

		if(angular.isUndefined(selectedBaseType) || selectedBaseType == "" || selectedBaseType == null) {
			$scope.nosuchSiteFoundDiv = true;
			$scope.message = "Please select any graph base type";
			return;
		} else {
			if(angular.equals(selectedBaseType,'installation')) {
				if(angular.isUndefined(selectedInstallation) || selectedInstallation == "" || selectedInstallation == null) {
					$scope.nosuchSiteFoundDiv = true;
					$scope.message = "Please select any installation";
					return;
				} else {
					flag = true;
					id = selectedInstallation.regionId;
				}
			} else if(angular.equals(selectedBaseType,'districtWaterMeter')) {
				if(angular.isUndefined(selectedDWT) || selectedDWT == "" || selectedDWT == null) {
					$scope.nosuchSiteFoundDiv = true;
					$scope.message = "Please select any district water meter";
					return;
				} else {
					flag = true;
					id = selectedDWT.duMeterId;
				}
			} else if(angular.equals(selectedBaseType,'consumerAccNo')) {
				if(angular.isUndefined(selectedConsumerAccNo) || selectedConsumerAccNo == "" || selectedConsumerAccNo == null) {
					$scope.nosuchSiteFoundDiv = true;
					$scope.message = "Please select any consumer acc no";
					return;
				} else {
					flag = true;
					id = selectedConsumerAccNo.consumerId;
				}
			} else if(angular.equals(selectedBaseType,'registerId')) {
				if(angular.isUndefined(selectedRegisterId) || selectedRegisterId == "" || selectedRegisterId == null) {
					$scope.nosuchSiteFoundDiv = true;
					$scope.message = "Please select any register id";
					return;
				} else {
					flag = true;
					id = selectedRegisterId.registerId;
				}
			}
		} 

		if(flag) {
			if(selectedPeriodType == null || selectedPeriodType == "" || angular.isUndefined(selectedPeriodType)) {
				$scope.nosuchSiteFoundDiv = true;
				$scope.message = "Please select any Period";
				return;
			} else if(selectedPeriodTime == null || selectedPeriodTime == "" || angular.isUndefined(selectedPeriodTime)) {
				$scope.nosuchSiteFoundDiv = true;
				$scope.message = "Please select no of periods";
				return;
			} else {
				$http({
					method: "POST",
					url:url+"/customerOperation/reportsOperation/getConsumptionGraph",
					params:{
						id:id,
						baseType:selectedBaseType,
						periodType:selectedPeriodType,
						periodTime:selectedPeriodTime
					}
				}).success(function(response){
					if(response != null) {
						// response has 2 array -> 0 for regular data, 1 for data with avg , 2 for data with tariff change
						$scope.chartData = response;

						// setting graph types 
						$scope.graphTypeShow = true;
						if(angular.equals(selectedBaseType,'registerId')) {
							$scope.graphTypeList = ["Show compartive", "Show trend line", "Show average", "Show tariff change"];
							$scope.selectedGraphType = "Show compartive";
						} else {
							$scope.graphTypeList = ["Show compartive", "Show trend line", "Show average"];
							$scope.selectedGraphType = "Show compartive";
						}

						// drawing chart 
						drawChartConsumption(response[0]);

						//chart_data
						if(showDataTbl) {
							showDataTableForConsumptionGraph(response[0]);
						} else {
							$scope.tblShow = false;
						}

						$scope.message = "";
						$scope.nosuchSiteFoundDiv = false;
					} else {
						$scope.nosuchSiteFoundDiv = true;
						$scope.message = "No data found";
					}

				}).error(function(data,status){
				});
			}
		}
	}

	$scope.consumptionGraphReset = function() {
		$scope.selectedBaseType = "";
		$scope.selectedPeriodType = "";
		$scope.selectedPeriodTime = "";

		$scope.registerIdType = false;
		$scope.installaionType = false;
		$scope.districtWaterMeterType = false;
		$scope.consumerAccNoType = false;

		$scope.chartShow = false;
		$scope.tblShow = false;
		$scope.graphTypeShow = false;
		$scope.showDataTbl = false;
	}

	function showDataTableForConsumptionGraph(datas) {
		$scope.tblShow = true;

		if( $.fn.dataTable.isDataTable( '#chart_data' ) ) {
			var table = $('#chart_data').DataTable();
			table.destroy();
		}

		$('#chart_data').DataTable( {
			data: JSON.parse(datas),
			columns: [
			          { title: "Period" },
			          { title: "Unit Consumption" },
			          ],
			          order: []
		} );
	}

	$scope.showTypeBasedGraph = function(){
		var selectedGraphType = $scope.selectedGraphType;
		var datas = $scope.chartData;

		if(angular.equals(selectedGraphType,'Show trend line')) {
			drawChartWithTrendLine(datas[1]);
		} else if(angular.equals(selectedGraphType,'Show compartive')) {
			drawChart(datas[0]);
		} else if(angular.equals(selectedGraphType,'Show average')) {
			drawChartWithAVG(datas[1]);
		} else if(angular.equals(selectedGraphType,'Show tariff change')) {
			drawChartWithTariffChange(datas[2]);
		}
	}

	function drawChartWithAVG(datas) {
		$scope.chartShow = true;

		var data = new google.visualization.DataTable();
		data.addColumn('string', 'Period');
		data.addColumn('number', 'Unit Consumption');
		data.addColumn('number', 'Average');

		data.addRows(JSON.parse(datas));

		var options = {
				title: 'Unit Consumption Chart',
				width: 1600,
				height: 500,
				hAxis: {title: 'Period'},
				vAxis: {title: 'Unit Consumption'},
				legend: { position: 'top', maxLines: 3, textStyle: {color: 'black', fontSize: 12 } },
		};

		var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
		chart.draw(data, options);

	}

	function drawChartWithTrendLine(datas) {
		var data = new google.visualization.DataTable();
		data.addColumn('string', 'Period');
		data.addColumn('number', 'Unit Consumption');
		data.addColumn('number', 'Average');

		data.addRows(JSON.parse(datas));

		var options = {
				title: 'Unit Consumption Chart',
				hAxis: {title: 'Period'},
				vAxis: {title: 'Unit Consumption'},
				legend: { position: 'top', maxLines: 3, textStyle: {color: 'black', fontSize: 12 } },
				width: 1200,
				height: 400,
		};

		var chart = new google.charts.Line(document.getElementById('chart_div'));
		chart.draw(data, options);
	}

	// FreeReport Starts from here
	// Init method of free report 
	var freeSearchMap = new Map();
	$scope.searchedData;
	var freeReportHeaderArrayConversion = new Map();
	$scope.initFreeReportData = function(){
		$http({
			method: "POST",
			url:url+"/customerOperation/reportsOperation/initFreeReportData",
		}).success(function(data){
			$scope.dcList = data[0];
			$scope.consumerEPList = data[1];
			$scope.siteList = data[2];
			$scope.installerList = data[3];
			$scope.consumerList = data[4];

			// Map related to search
			freeSearchMap.put("col1", "");
			freeSearchMap.put("col2", "");
			freeSearchMap.put("col3", "");
			freeSearchMap.put("col4", "");
			freeSearchMap.put("col5", "");
			freeSearchMap.put("col6", "");

			$scope.column1 = $scope.column2 = $scope.column3 = $scope.column4 = $scope.column5 = $scope.column6 = "";

			// initialize freereport header array
			
			//DC fields
			freeReportHeaderArrayConversion.put("simNumber","Simcard Number");
			freeReportHeaderArrayConversion.put("installerName","Installer Name");
			freeReportHeaderArrayConversion.put("location","Location");
			freeReportHeaderArrayConversion.put("siteName","Site Name");
			freeReportHeaderArrayConversion.put("dcSerialNumber","DataCollector Serial Number");
			//Consumer EP Fields
			freeReportHeaderArrayConversion.put("tariffName","Tariff Name");
			freeReportHeaderArrayConversion.put("consumerName","Consumer Name");
			freeReportHeaderArrayConversion.put("epSerailnumber","EndPoint Serial Number");
			//Site Fields
			freeReportHeaderArrayConversion.put("status","Site Status");
			freeReportHeaderArrayConversion.put("region","Region");
			freeReportHeaderArrayConversion.put("commissioningType","Commissioning Type");
			freeReportHeaderArrayConversion.put("name","Name");
			// Installer Fields
			freeReportHeaderArrayConversion.put("installerName","Installer Name");
			//Consumer Fields
			freeReportHeaderArrayConversion.put("consumerAccountNumber","Consumer Account Number");
			freeReportHeaderArrayConversion.put("address","Address");
			freeReportHeaderArrayConversion.put("streetName","Street Name");
			freeReportHeaderArrayConversion.put("firstName","First Name");
			freeReportHeaderArrayConversion.put("zip","Zip-Code");
			freeReportHeaderArrayConversion.put("lastName","LastName");
			
		}).error({

		})
	}

	$scope.freeReportfunCalled = function(columnId,value){
		freeSearchMap.put(columnId,value);
	}

	$scope.selectionChanged = function(){
		$scope.finalArray = [];
		$scope.tableHeaderArray=[];
		freeSearchMap.put("col1", "");
		freeSearchMap.put("col2", "");
		freeSearchMap.put("col3", "");
		freeSearchMap.put("col4", "");
		freeSearchMap.put("col5", "");
		freeSearchMap.put("col6", "");
		if(angular.equals($scope.selectedField,'dcCheckBox')){
			$scope.displayList = $scope.dcList;
		}else if(angular.equals($scope.selectedField,'consumerEPCheckBox')){
			$scope.displayList = $scope.consumerEPList;
		}else if(angular.equals($scope.selectedField,'siteCheckBox')){
			$scope.displayList = $scope.siteList;	
		}else if(angular.equals($scope.selectedField,'installerCheckBox')){
			$scope.displayList = $scope.installerList;
		}else if(angular.equals($scope.selectedField,'consumerCheckBox')){
			$scope.displayList = $scope.consumerList;
		}
		// now lets clear their model
		$scope.column1 = $scope.column2 = $scope.column3 = $scope.column4 = $scope.column5 = $scope.column6 = "";
	}

	$scope.searchFreeReportData = function(){
		if(angular.isUndefined($scope.selectedField)){
			alert("Select any field to search by "); 
		}else{
			//alert($scope.selectedField+">>"+$scope.selectedReport);
			var startDate = $filter('date')($scope.startDate,"yyyy-MM-dd");
			var endDate = $filter('date')($scope.endDate,"yyyy-MM-dd");
			// http call for searching fields
			$http({
				method: "POST",
				url: url + "/customerOperation/reportsOperation/searchFreeReportData",
				params: {selectedField:$scope.selectedField,
					reportBy:$scope.selectedReport,
					reportPeriod:$scope.reportPeriod,
					startDate:startDate,
					endDate:endDate,
					installatioName:$scope.installationName,
					siteName:$scope.siteName,
					dcSerialNumber:$scope.dcSerialNumber},
			}).success(function(data) {
				//alert(data);
				$scope.searchedData = data;
				$scope.recordSize = 10;
				$scope.totalItems = $scope.searchedData.length;
				$scope.currentPage = 1;
				$scope.itemsPerPage = $scope.recordSize;
				$scope.maxSize = 5;

				if(data){
					$scope.finalArray = [];
					var tableHeaderMap = new Map();
					angular.forEach(data,function(value,index){
						var tempMap = new Map();
						tempMap.data = value;
						var finalMap = new Map();
						for(var i=1;i<7;i++){
							var col = 'col'+i;
							if(freeSearchMap.get(col)!=""){
								finalMap.put(col,tempMap.get(freeSearchMap.get(col)));
								if(index==0){
									tableHeaderMap.put(col,freeSearchMap.get(col));
								}
							}
						}
						if(index==0){
							$scope.tableHeaderArray=[];
							$scope.tableHeaderArray.push(tableHeaderMap.data);
						}
						$scope.finalArray.push(finalMap.data);
					});

					$scope.billingDataList = data[0];
					$scope.showBillingDataListDiv = true;
					$scope.noBillingDataFoundDiv = false;
				}
				if(angular.equals(data,'error')){
					$scope.noBillingDataFoundDiv = true;
					$scope.showBillingDataListDiv = false;
				}


			}).error(function (data,status){

			});
		}
	}

	var hashMapCSVHeaderData = new Map();
	var tempArray = new Array();
	$scope.downloadFreeReportCSVFile = function(tableHeaderArray){
		hashMapCSVHeaderData = new Map();
		var header = angular.toJson(tableHeaderArray);
		for (var i in tableHeaderArray[0]){
			hashMapCSVHeaderData.put(i,freeReportHeaderArrayConversion.get(tableHeaderArray[0][i]));
		}
		tempArray[0] = (hashMapCSVHeaderData.data);
		header = angular.toJson(tempArray);
		ConvertExportToCSV(angular.toJson($scope.finalArray),header,'free_report_data.csv');
	}

	$scope.getHeaderValueBasedOnKey = function(key){
		return freeReportHeaderArrayConversion.get(key);
	}

	// pct24 code ends here
	function drawChartWithTariffChange(datas) {
		var data = new google.visualization.DataTable();
		data.addColumn('string', 'Period');
		data.addColumn('number', 'Unit Consumption');
		data.addColumn({type: 'string', role: 'annotation'});

		data.addRows(JSON.parse(datas));

		var view = new google.visualization.DataView(data);
		view.setColumns([0, 1, 1, 2]);

		var options = {
				title: 'Unit Consumption Chart [ TC = Tariff Change ]',
				width: 1600,
				height: 500,
				hAxis: {title: 'Period'},
				vAxis: {title: 'Unit Consumption'},
				legend: { position: 'top', maxLines: 3, textStyle: {color: 'black', fontSize: 12 } },
				series: {
					0: {
						type: 'bars'
					},
					1: {
						type: 'line',
						color: 'red',
						lineWidth: 0,
						pointSize: 0,
						visibleInLegend: false
					}
				},
		};

		var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
		chart.draw(view, options);
	}

	$scope.consumptionExportToCSV = function() {
		var data = $scope.chartData;
		var header = [['Period','Unit Consumption']];
		ConvertExportToCSV(data[0], header, "unit_consumption.csv");         

	}

	function drawChartConsumption(datas) {
		$scope.chartShow = true;

		var data = new google.visualization.DataTable();
		data.addColumn('string', 'Period');
		data.addColumn('number', 'Unit Consumption');

		data.addRows(JSON.parse(datas));

		var options = {
				title: 'Unit Consumption Chart',
				width: 1600,
				height: 500,
				hAxis: {title: 'Period'},
				vAxis: {title: 'Unit Consumption'},
				legend: { position: 'top', maxLines: 3, textStyle: {color: 'black', fontSize: 12 } },
				isStacked: false,
		};

		var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
		chart.draw(data, options);
	}

	// Financial What If starts from here
	$scope.initFinanicalWhatIfData = function(){

		//default value to selection time model
		$scope.timeSelection = "default";
		$scope.percentage = 0;
		$http({
			method: "POST",
			url: url + "/customerOperation/reportsOperation/initFinancialWhatIfData"
		}).success(function(data){
			$scope.status = data[0];
			if(angular.equals($scope.status,'noUniqueTariff')){
				$scope.isError = true;
				$scope.errorMessage = "No unique tariff plan under Customer. Filter it by region wise !";
				setTimeout(function ()
						{
					$scope.$apply(function()
							{
						$scope.isError = false;
						$scope.installerList = data[1];
							});
						}, 1500);
			}else if(angular.equals($scope.status,'sameTariff')){
				var temp = data[1].tariffArray; 
				$scope.totalConsumerMeters = parseInt(data[2]);
				for( var i = 0; i < temp.length; i++ ) {
					if(angular.equals(parseInt(i)+1,1)){
						$scope.trans1 = temp[i];
						$scope.iftrans1 = temp[i];
					}else if(angular.equals(parseInt(i)+1,2)){
						$scope.trans2 = temp[i];
						$scope.iftrans2 = temp[i];
					}else if(angular.equals(parseInt(i)+1,3)){
						$scope.trans3 = temp[i];
						$scope.iftrans3 = temp[i];
					}else if(angular.equals(parseInt(i)+1,4)){
						$scope.trans4 = temp[i];
						$scope.iftrans4 = temp[i];
					}else if(angular.equals(parseInt(i)+1,5)){
						$scope.trans5 = temp[i];
						$scope.iftrans5 = temp[i];
					}else if(angular.equals(parseInt(i)+1,6)){
						$scope.trans6 = temp[i];
						$scope.iftrans6 = temp[i];
					}else if(angular.equals(parseInt(i)+1,7)){
						$scope.trans7 = temp[i];
						$scope.iftrans7 = temp[i];
					}else if(angular.equals(parseInt(i)+1,8)){
						$scope.trans8 = temp[i];
						$scope.iftrans8 = temp[i];
					}else if(angular.equals(parseInt(i)+1,9)){
						$scope.trans9 = temp[i];
						$scope.iftrans9 = temp[i];
					}else if(angular.equals(parseInt(i)+1,10)){
						$scope.trans10 = temp[i];
						$scope.iftrans10 = temp[i];
					}else{
					}
				}
				$scope.displayTariffDiv = true;
				/*$scope.installerList = data[1];*/
			}
		}).error(function(data,status){
			alert("Error ");
		})
	}

	$scope.initConsumerUsageReport = function(){
		$http({
			method: "POST",
			url:url+"/customerOperation/reportsOperation/initConsumerUsageReport",
		}).success(function(data){
			$scope.consumer = data;
		}).error(function(data,status){
		});
	} 


	$scope.setBaseTypeIdsForConsumer = function() {
		var selectedBaseType = $scope.selectedBaseType;

		if (!angular.equals(selectedBaseType,'consumerAccNo') && !angular.equals(selectedBaseType,'registerId')) {
			$scope.seletedBaseType = "";
			$scope.consumerAccNoType = false;
			$scope.registerIdType = false;
			return;
		} else {
			$scope.message = "";
			$scope.nosuchSiteFoundDiv = false;

			if(angular.equals(selectedBaseType,'consumerAccNo')) {
				$scope.consumerAccNoType = true;
				$scope.registerIdType = false;

			} else if(angular.equals(selectedBaseType,'registerId')) {
				$http({
					method: "POST",
					url:url+"/customerOperation/reportsOperation/getRegisterIdsForConsumer",
				}).success(function(data){
					$scope.registerIdType = true;
					$scope.consumerAccNoType = false;

					$scope.baseTypeRegisterList = data;
				}).error(function(data,status){
				});
			}
		}

	}

	$scope.getConsumerUsageGraph = function(){ 
		var flag = false;
		var id = null;

		var selectedBaseType = $scope.selectedBaseType;
		var selectedConsumer = $scope.consumer;
		var selectedRegisterId = $scope.selectedRegisterId;
		var selectedPeriodType = $scope.selectedPeriodType;
		var selectedPeriodTime = $scope.selectedPeriodTime;
		var showDataTbl = $scope.showDataTbl;

		if(angular.isUndefined(selectedBaseType) || selectedBaseType == "" || selectedBaseType == null) {
			$scope.nosuchSiteFoundDiv = true;
			$scope.message = "Please select any graph base type";
			return;
		} else {
			if(angular.equals(selectedBaseType,'consumerAccNo')) {
				if(angular.isUndefined(selectedConsumer) || selectedConsumer == "" || selectedConsumer == null) {
					$scope.nosuchSiteFoundDiv = true;
					$scope.message = "Consumer not found";
					return;
				} else {
					flag = true;
					id = selectedConsumer.consumerId;
				}
			} else if(angular.equals(selectedBaseType,'registerId')) {
				if(angular.isUndefined(selectedRegisterId) || selectedRegisterId == "" || selectedRegisterId == null) {
					$scope.nosuchSiteFoundDiv = true;
					$scope.message = "Please select any register id";
					return;
				} else {
					flag = true;
					id = selectedRegisterId.registerId;
				}
			}
		} 

		if(flag) {
			if(selectedPeriodType == null || selectedPeriodType == "" || angular.isUndefined(selectedPeriodType)) {
				$scope.nosuchSiteFoundDiv = true;
				$scope.message = "Please select any Period";
				return;
			} else if(selectedPeriodTime == null || selectedPeriodTime == "" || angular.isUndefined(selectedPeriodTime)) {
				$scope.nosuchSiteFoundDiv = true;
				$scope.message = "Please select no of periods";
				return;
			} else {
				$http({
					method: "POST",
					url:url+"/customerOperation/reportsOperation/getConsumptionGraph",
					params:{
						id:id,
						baseType:selectedBaseType,
						periodType:selectedPeriodType,
						periodTime:selectedPeriodTime
					}
				}).success(function(response){
					if(response != null) {
						// response has 3 array -> 0 for regular data, 1 for data with avg , 2 for data with tariff change
						$scope.chartData = response;

						// setting graph types 
						$scope.graphTypeShow = true;
						if(angular.equals(selectedBaseType,'registerId')) {
							$scope.graphTypeList = ["Show compartive", "Show trend line", "Show average", "Show tariff change"];
							$scope.selectedGraphType = "Show compartive";
						} else {
							$scope.graphTypeList = ["Show compartive", "Show trend line", "Show average"];
							$scope.selectedGraphType = "Show compartive";
						}

						// drawing chart 
						drawChartConsumption(response[0]);

						//chart_data
						if(showDataTbl) {
							showDataTableForConsumptionGraph(response[0]);
						} else {
							$scope.tblShow = false;
						}

						$scope.message = "";
						$scope.nosuchSiteFoundDiv = false;
					} else {
						$scope.nosuchSiteFoundDiv = true;
						$scope.message = "No data found";
					}

				}).error(function(data,status){
				});
			}
		}
	}


	$scope.initBillingReport = function(){
		$http({
			method: "POST",
			url:url+"/customerOperation/reportsOperation/initBillingReport",
		}).success(function(data){
			$scope.registerIdList = data;

			if(data.length == 1) {
				$scope.selectedRegister = data[0];
			}

			$scope.amountTypeList = ["Consumed Unit", "Total Amount"];
		}).error(function(data,status){
		});
	} 


	$scope.getBillingReportGraph = function() {
		var selectedRegister = $scope.selectedRegister;
		var selectedAmountType = $scope.selectedAmountType;
		var showIsEstimated = $scope.showIsEstimated;
		var showDataTbl = $scope.showDataTbl;

		if(selectedRegister == null || selectedRegister == "" || angular.isUndefined(selectedRegister)) {
			$scope.nosuchSiteFoundDiv = true;
			$scope.message = "Please select any register id";
			return;
		} else if(selectedAmountType == null || selectedAmountType == "" || angular.isUndefined(selectedAmountType)) {
			$scope.nosuchSiteFoundDiv = true;
			$scope.message = "Please select any type";
			return;
		} else {
			$http({
				method: "POST",
				url:url+"/customerOperation/reportsOperation/getBillingReportGraph",
				params:{
					registerId:selectedRegister.registerId,
					amountType:selectedAmountType
				}
			}).success(function(response){
				if(response != null) {
					// response has 2 array -> 0 for regular data, 1 for data with estimated
					$scope.chartData = response;
					var header;

					if(angular.equals(selectedAmountType,'Consumed Unit')) {
						header = "Consumed Unit";
					} else {
						header = "Total Amount";
					}
					$scope.tempHeader = header;

					// drawing chart 
					if(!showIsEstimated) {
						drawChartBillingHistory(response[0], header);
					} else {
						drawChartBillingWithIsEstimated(response[1], header);
					}

					//chart_data
					if(showDataTbl) {
						showDataTableForBillingGraph(response, header);
					} else {
						$scope.tblShow = false;
					}

					$scope.message = "";
					$scope.nosuchSiteFoundDiv = false;
				} else {
					$scope.nosuchSiteFoundDiv = true;
					$scope.message = "No data found";
				}

			}).error(function(data,status){
			});
		}
	}

	function drawChartBillingHistory(datas, header) {
		$scope.chartShow = true;

		var data = new google.visualization.DataTable();
		data.addColumn('string', 'Time');
		data.addColumn('number', header);

		data.addRows(JSON.parse(datas));

		var options = {
				title: 'Billing History Chart',
				width: 1600,
				height: 500,
				hAxis: {title: 'Time'},
				vAxis: {title: header},
				legend: { position: 'top', maxLines: 3, textStyle: {color: 'black', fontSize: 12 } },
				isStacked: false,
		};

		var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
		chart.draw(data, options);

	}

	function drawChartBillingWithIsEstimated(datas, header) {
		$scope.chartShow = true;

		var data = new google.visualization.DataTable();
		data.addColumn('string', 'Time');
		data.addColumn('number', header);
		data.addColumn({type: 'string', role: 'annotation'});

		data.addRows(JSON.parse(datas));

		var view = new google.visualization.DataView(data);
		view.setColumns([0, 1, 1, 2]);

		var options = {
				title: 'Billing History Chart [ Est = IsEstimated ]',
				width: 1600,
				height: 500,
				hAxis: {title: 'Time'},
				vAxis: {title: header},
				legend: { position: 'top', maxLines: 3, textStyle: {color: 'black', fontSize: 12 } },
				series: {
					0: {
						type: 'bars'
					},
					1: {
						type: 'line',
						color: 'red',
						lineWidth: 0,
						pointSize: 0,
						visibleInLegend: false
					}
				},
		};

		var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
		chart.draw(view, options);
	}

	function showDataTableForBillingGraph(datas, header) {
		$scope.tblShow = true;

		if( $.fn.dataTable.isDataTable( '#chart_data' ) ) {
			var table = $('#chart_data').DataTable();
			table.destroy();
		}

		$('#chart_data').DataTable({
			data: JSON.parse(datas[1]),
			columns: [
			          { title: "Time" },
			          { title: header },
			          { title: "Is Estimated?" },
			          ],
			          order: []
		});
	}

	$scope.billingExportToCSV = function() {
		var data = $scope.chartData;

		var header = [['Time',$scope.tempHeader, 'Is Estimated?']];

		ConvertExportToCSV(data[1], header, "billing_report.csv");         

	}


	$scope.billingReportReset = function() {
		$scope.selectedRegister = "";
		$scope.selectedAmountType = "";

		$scope.chartShow = false;
		$scope.tblShow = false;
		$scope.showIsEstimated = false;
		$scope.showDataTbl = false;
	}



	$scope.whatIfRegionSelectionChanged = function(){
		$scope.selectsiteModel = "default";
		if(!(!$scope.selectregionModel || 0 === $scope.selectregionModel.length)){
			$http({
				method: "POST",
				url: url + "/customerOperation/reportsOperation/checkForConsumerWithSameTariffUnderRegion",
				params:{region:$scope.selectregionModel},
			}).success(function(data){
				$scope.status = data[0];
				$scope.selectsiteModel = "default";
				if(angular.equals($scope.status,'noUniqueTariff')){
					$scope.isError = true;
					$scope.errorMessage = "No unique tariff plan under region . Filter it by Site wise !";
					$scope.selectsiteModel = "default";
					$scope.displayTariffDiv = false;
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.isError = false;
							$scope.siteNameList = data[1];
								});
							}, 1500);
				}else if(angular.equals($scope.status,'noSiteUnderRegion')){
					$scope.isError = true;
					$scope.errorMessage = "No site allocated under region "+$scope.selectregionModel+".";
					$scope.displayTariffDiv = false;
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.isError = false;
							$scope.siteNameList = data[1];
								});
							}, 1500);
				}else if($scope.status.indexOf(":") !== -1){
					var temp = data[1].tariffArray; 
					$scope.siteNameList = null;
					$scope.totalConsumerMeters = data[2];
					for( var i = 0; i < temp.length; i++ ) {
						if(angular.equals(parseInt(i)+1,1)){
							$scope.trans1 = temp[i];
							$scope.iftrans1 = {};
							$scope.iftrans1.ifstart1=temp[i].start;
							$scope.iftrans1.ifend1=temp[i].end;
							$scope.iftrans1.ifcost1=temp[i].cost;
							/*$scope.iftrans1 = temp[i];*/
						}else if(angular.equals(parseInt(i)+1,2)){
							$scope.trans2 = temp[i];
							$scope.iftrans2 = {};
							$scope.iftrans2.ifstart2=temp[i].start;
							$scope.iftrans2.ifend2=temp[i].end;
							$scope.iftrans2.ifcost2=temp[i].cost;
							/*$scope.iftrans2 = temp[i];*/
						}else if(angular.equals(parseInt(i)+1,3)){
							$scope.trans3 = temp[i];
							$scope.iftrans3 = {};
							$scope.iftrans3.ifstart3=temp[i].start;
							$scope.iftrans3.ifend3=temp[i].end;
							$scope.iftrans3.ifcost3=temp[i].cost;
							/*$scope.iftrans3 = temp[i];*/
						}else if(angular.equals(parseInt(i)+1,4)){
							$scope.trans4 = temp[i];
							$scope.iftrans4 = {};
							$scope.iftrans4.ifstart4=temp[i].start;
							$scope.iftrans4.ifend4=temp[i].end;
							$scope.iftrans4.ifcost4=temp[i].cost;
							/*$scope.iftrans4 = temp[i];*/
						}else if(angular.equals(parseInt(i)+1,5)){
							$scope.trans5 = temp[i];
							$scope.iftrans5 = {};
							$scope.iftrans5.ifstart5=temp[i].start;
							$scope.iftrans5.ifend5=temp[i].end;
							$scope.iftrans5.ifcost5=temp[i].cost;
							/*$scope.iftrans5 = temp[i];*/
						}else if(angular.equals(parseInt(i)+1,6)){
							$scope.trans6 = temp[i];
							$scope.iftrans6 = {};
							$scope.iftrans6.ifstart6=temp[i].start;
							$scope.iftrans6.ifend6=temp[i].end;
							$scope.iftrans6.ifcost6=temp[i].cost;
							/*$scope.iftrans6 = temp[i];*/
						}else if(angular.equals(parseInt(i)+1,7)){
							$scope.trans7 = temp[i];
							$scope.iftrans7 = {};
							$scope.iftrans7.ifstart7=temp[i].start;
							$scope.iftrans7.ifend7=temp[i].end;
							$scope.iftrans7.ifcost7=temp[i].cost;
							/*$scope.iftrans7 = temp[i];*/
						}else if(angular.equals(parseInt(i)+1,8)){
							$scope.trans8 = temp[i];
							$scope.iftrans8 = {};
							$scope.iftrans8.ifstart8=temp[i].start;
							$scope.iftrans8.ifend8=temp[i].end;
							$scope.iftrans8.ifcost8=temp[i].cost;
							/*$scope.iftrans8 = temp[i];*/
						}else if(angular.equals(parseInt(i)+1,9)){
							$scope.trans9 = temp[i];
							$scope.iftrans9 = {};
							$scope.iftrans9.ifstart9=temp[i].start;
							$scope.iftrans9.ifend9=temp[i].end;
							$scope.iftrans9.ifcost9=temp[i].cost;
							/*$scope.iftrans9 = temp[i];*/
						}else if(angular.equals(parseInt(i)+1,10)){
							$scope.trans10 = temp[i];
							$scope.iftrans10 = {};
							$scope.iftrans10.ifstart10=temp[i].start;
							$scope.iftrans10.ifend10=temp[i].end;
							$scope.iftrans10.ifcost10=temp[i].cost;
							/*$scope.iftrans10 = temp[i];*/
						}else{
						}
					}
					$scope.displayTariffDiv = true;
				}
				else{
					$scope.siteNameList = data[1];
					$scope.displayTariffDiv = false;
				}
			}).error(function(data,status){

			})
		}else{
			$scope.siteNameList = null;
		}
	}

	$scope.whatIfSiteSelectionChanged = function(){
		if(!(!$scope.selectsiteModel || 0 === $scope.selectsiteModel.length)){
			$http({
				method: "POST",
				url: url + "/customerOperation/reportsOperation/checkForConsumerWithSameTariffUnderSite",
				params:{site:$scope.selectsiteModel},
			}).success(function(data){
				$scope.status = data[0];
				if(angular.equals($scope.status,'noUniqueTariff')){
					$scope.isError = true;
					$scope.errorMessage = "No unique tariff plan under Site ";
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.isError = false;
								});
							}, 1500);
				}else if(angular.equals($scope.status,'notAnyConsumerAllocated')){
					$scope.isError = true;
					$scope.errorMessage = "No consumer allocated under site ";
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.isError = false;
								});
							}, 1500);
				}else if($scope.status.indexOf(":") !== -1){
					var temp = data[1].tariffArray; 
					$scope.totalConsumerMeters = data[2];
					for( var i = 0; i < temp.length; i++ ) {
						if(angular.equals(parseInt(i)+1,1)){
							$scope.trans1 = temp[i];
							$scope.iftrans1 = temp[i];
						}else if(angular.equals(parseInt(i)+1,2)){
							$scope.trans2 = temp[i];
							$scope.iftrans2 = temp[i];
						}else if(angular.equals(parseInt(i)+1,3)){
							$scope.trans3 = temp[i];
							$scope.iftrans3 = temp[i];
						}else if(angular.equals(parseInt(i)+1,4)){
							$scope.trans4 = temp[i];
							$scope.iftrans4 = temp[i];
						}else if(angular.equals(parseInt(i)+1,5)){
							$scope.trans5 = temp[i];
							$scope.iftrans5 = temp[i];
						}else if(angular.equals(parseInt(i)+1,6)){
							$scope.trans6 = temp[i];
							$scope.iftrans6 = temp[i];
						}else if(angular.equals(parseInt(i)+1,7)){
							$scope.trans7 = temp[i];
							$scope.iftrans7 = temp[i];
						}else if(angular.equals(parseInt(i)+1,8)){
							$scope.trans8 = temp[i];
							$scope.iftrans8 = temp[i];
						}else if(angular.equals(parseInt(i)+1,9)){
							$scope.trans9 = temp[i];
							$scope.iftrans9 = temp[i];
						}else if(angular.equals(parseInt(i)+1,10)){
							$scope.trans10 = temp[i];
							$scope.iftrans10 = temp[i];
						}else{
						}
					}
					$scope.displayTariffDiv = true;
				}
			}).error(function(data,status){

			})
		}else{
			$scope.siteNameList = null;
		}
	}

	$scope.calculateTimeData = function(){
		if(!angular.equals($scope.timeSelection,"default")){
			if(angular.equals($scope.timeSelection,"lastYear")){
				$scope.selectedMonth = 12;
			}else if(angular.equals($scope.timeSelection,"lastQuarter")){
				$scope.selectedMonth = 3;
			}else {
				$scope.selectedMonth = 1;
			} 
			$http({
				method: "POST",
				url: url+"/customerOperation/reportsOperation/generateFinancialTimePeriodData",
				params: {monthDuration : parseInt($scope.selectedMonth),
					regionName : $scope.selectregionModel,
					siteName : $scope.selectsiteModel},
			}).success(function(data){
				$scope.revenue = data;
				if($scope.whatIfRevenue == 0 || angular.isUndefined($scope.whatIfRevenue)){
					$scope.whatIfRevenue = $scope.revenue;
					$scope.initialRevenue = $scope.whatIfRevenue; 
				}	
				$scope.calculateProfitOrLoss();
			}).error(function(data,status){

			})
		}else{
			alert("Select Proper Time Period");
		}
	}

	/*$scope.calculateRevenue = function(){
		var value = $scope.checkForRange($scope.revenue);
		$scope.whatIfRevenue =  ($scope.consumptionVar*$scope.revenue*value)/100;
	}*/

	$scope.calculateMeterGrowth = function(){
		if(!angular.isUndefined($scope.totalConsumerMeters)){
			$scope.whatIfRevenue = ((($scope.totalConsumerMeters + ($scope.meterGrowthVar/100))*$scope.initialRevenue)/$scope.totalConsumerMeters);
			$scope.calculateProfitOrLoss();
		}
	}

	$scope.checkForRange = function(value,state){
		if(!angular.isUndefined($scope.revenue)){

			if(betweenTwoValue($scope.revenue,$scope.trans1.start,$scope.trans1.end)){
				return $scope.trans1.cost;
			}
			else if(betweenTwoValue($scope.revenue,$scope.trans2.start,$scope.trans2.end)){
				return $scope.trans2.cost;
			}
			else if(betweenTwoValue($scope.revenue,$scope.trans3.start,$scope.trans3.end)){
				return $scope.trans3.cost;
			}
			else if(betweenTwoValue($scope.revenue,$scope.trans4.start,$scope.trans4.end)){
				return $scope.trans4.cost;
			}
			else if(betweenTwoValue($scope.revenue,$scope.trans5.start,$scope.trans5.end)){
				return $scope.trans5.cost;
			}
			else if(betweenTwoValue($scope.revenue,$scope.trans6.start,$scope.trans6.end)){
				return $scope.trans6.cost;
			}
			else if(betweenTwoValue($scope.revenue,$scope.trans7.start,$scope.trans7.end)){
				return $scope.trans7.cost;
			}
			else if(betweenTwoValue($scope.revenue,$scope.trans8.start,$scope.trans8.end)){
				return $scope.trans8.cost;
			}
			else if(betweenTwoValue($scope.revenue,$scope.trans9.start,$scope.trans9.end)){
				return $scope.trans9.cost;
			}
			else if(betweenTwoValue($scope.revenue,$scope.trans10.start,$scope.trans10.end)){
				return $scope.trans10.cost;
			}
		}
	}
	function betweenTwoValue(value,lower,upper){
		return lower <= value && value <= upper;
	}

	$scope.findrevenueBasedOnNewTariff = function(consumptionVar){
		$scope.tariffData=[];
		/* var isValidationPassed = $scope.checkForValidTransaction();*/
		if(checkForRowUndefinedData($scope.iftrans1)){
			pushElementToTariffDataWithElement($scope.iftrans1.ifstart1,$scope.iftrans1.ifend1,$scope.iftrans1.ifcost1);
		}if(checkForRowUndefinedData($scope.iftrans2)){
			pushElementToTariffDataWithElement($scope.iftrans2.ifstart2,$scope.iftrans2.ifend2,$scope.iftrans2.ifcost2);
		}if(checkForRowUndefinedData($scope.iftrans3)){
			pushElementToTariffDataWithElement($scope.iftrans3.ifstart3,$scope.iftrans3.ifend3,$scope.iftrans3.ifcost3);
		}if(checkForRowUndefinedData($scope.iftrans4)){
			pushElementToTariffDataWithElement($scope.iftrans4.ifstart4,$scope.iftrans4.ifend4,$scope.iftrans4.ifcost4);
		}if(checkForRowUndefinedData($scope.iftrans5)){
			pushElementToTariffDataWithElement($scope.iftrans5.ifstart5,$scope.iftrans5.ifend5,$scope.iftrans5.ifcost5);
		}if(checkForRowUndefinedData($scope.iftrans6)){
			pushElementToTariffDataWithElement($scope.iftrans6.ifstart6,$scope.iftrans6.ifend6,$scope.iftrans6.ifcost6);
		}if(checkForRowUndefinedData($scope.iftrans7)){
			pushElementToTariffDataWithElement($scope.iftrans7.ifstart7,$scope.iftrans7.ifend7,$scope.iftrans7.ifcost7);
		}if(checkForRowUndefinedData($scope.iftrans8)){
			pushElementToTariffDataWithElement($scope.iftrans8.ifstart8,$scope.iftrans8.ifend8,$scope.iftrans8.ifcost8);
		}if(checkForRowUndefinedData($scope.iftrans9)){
			pushElementToTariffDataWithElement($scope.iftrans9.ifstart9,$scope.iftrans9.ifend9,$scope.iftrans9.ifcost9);
		}if(checkForRowUndefinedData($scope.iftrans10)){
			pushElementToTariffDataWithElement($scope.iftrans10.ifstart10,$scope.iftrans10.ifend10,$scope.iftrans10.ifcost10);
		}

		if($scope.tariffData.length > 0){
			var response = $http.post("applyNewTariff",{tariffModel:$scope.tariffData,regionName:$scope.selectregionModel,month:$scope.timeSelection,consumptionVar:consumptionVar,siteName:$scope.selectsiteModel});
			response.success(function(data){
				$scope.whatIfRevenue = data;
				$scope.initialRevenue = $scope.whatIfRevenue;
				$scope.calculateProfitOrLoss();
			})
		}
	}

	var checkForRowUndefinedData = function(model) {

		if(angular.isUndefined(model))
			return false;

		if((!angular.isUndefined(model.ifstart1)|| !angular.isUndefined(model.ifstart2) || !angular.isUndefined(model.ifstart3) || 
				!angular.isUndefined(model.ifstart4)|| !angular.isUndefined(model.ifstart5) || !angular.isUndefined(model.ifstart6) ||
				!angular.isUndefined(model.ifstart7)|| !angular.isUndefined(model.ifstart8) || !angular.isUndefined(model.ifstart9) || !angular.isUndefined(model.ifstart10)) && 

				(!angular.isUndefined(model.ifend1) || !angular.isUndefined(model.ifend2) || !angular.isUndefined(model.ifend3) ||
						!angular.isUndefined(model.ifend4) || !angular.isUndefined(model.ifend5) || !angular.isUndefined(model.ifend6) ||		
						!angular.isUndefined(model.ifend7) || !angular.isUndefined(model.ifend8) || !angular.isUndefined(model.ifend8) || !angular.isUndefined(model.ifend10)) && 

						(!angular.isUndefined(model.ifcost1)|| !angular.isUndefined(model.ifcost2)|| !angular.isUndefined(model.ifcost3)||
								!angular.isUndefined(model.ifcost4)|| !angular.isUndefined(model.ifcost5)|| !angular.isUndefined(model.ifcost6)||
								!angular.isUndefined(model.ifcost7)|| !angular.isUndefined(model.ifcost8)|| !angular.isUndefined(model.ifcost9)|| !angular.isUndefined(model.ifcost10))
		)
		{
			return true;
		}else{
			return false;
		}
	}

	var pushElementToTariffData = function(model){

		if(!angular.isUndefined(model.startValue)){
			$scope.tariffData.push({
				'start':model.startValue,
				'end':model.end,
				'cost':model.cost,
			});
		}else{
			$scope.tariffData.push({
				'start':model.ifstart,
				'end':model.end,
				'cost':model.cost,
			});
		}
	}

	var pushElementToTariffDataWithElement = function(start,end,cost){

		$scope.tariffData.push({
			'start':start,
			'end':end,
			'cost':cost,
		});
	}

	$scope.calculateProfitOrLoss = function(){
		$scope.profitLossStatus = "";
		$scope.percentage = 100 * Math.abs($scope.revenue - $scope.whatIfRevenue)/$scope.revenue;
		$scope.percentage = $filter('number')($scope.percentage, 2);
		if($scope.revenue > $scope.whatIfRevenue){
			$scope.profitLossStatus = "Loss";
			$scope.statusColor = {color: "red"};
		}else if($scope.revenue < $scope.whatIfRevenue){
			$scope.profitLossStatus = "Profit";
			$scope.statusColor = {color: "green"};
		}else{
			$scope.profitLossStatus = "";
		}
		$scope.whatIfRevenue  = $filter('number')($scope.whatIfRevenue, 2);
	}

	$scope.whatIfEndChanged = function(rowNumber){
		if(rowNumber == 1){
			if(angular.isUndefined($scope.iftrans2))
				$scope.iftrans2 = {};
			$scope.iftrans2.ifstart2 = $scope.iftrans1.ifend1 + 1;

			if($scope.iftrans1.ifend1 < $scope.iftrans1.ifstart1)
				$scope.iftrans1.ifend1 = $scope.iftrans1.ifstart1 + 1;
		}else if(rowNumber == 2){
			if(angular.isUndefined($scope.iftrans3))
				$scope.iftrans3 = {};
			$scope.iftrans3.ifstart3 = $scope.iftrans2.ifend2 + 1;

			if($scope.iftrans2.ifend2 < $scope.iftrans2.ifstart2)
				$scope.iftrans2.ifend2 = $scope.iftrans2.ifstart2 + 1;
		}else if(rowNumber == 3){
			if(angular.isUndefined($scope.iftrans4))
				$scope.iftrans4 = {};
			$scope.iftrans4.ifstart4 = $scope.iftrans3.ifend3 + 1;

			if($scope.iftrans3.ifend3 < $scope.iftrans3.ifstart3)
				$scope.iftrans3.ifend3 = $scope.iftrans3.ifstart3 + 1;
		}else if(rowNumber == 4){
			if(angular.isUndefined($scope.iftrans5))
				$scope.iftrans5 = {};
			$scope.iftrans5.ifstart5 = $scope.iftrans4.ifend4 + 1;

			if($scope.iftrans4.ifend4 < $scope.iftrans4.ifstart4)
				$scope.iftrans4.ifend4 = $scope.iftrans4.ifstart4 + 1;
		}else if(rowNumber == 5){
			if(angular.isUndefined($scope.iftrans6))
				$scope.iftrans6 = {};
			$scope.iftrans6.ifstart6 = $scope.iftrans5.ifend5 + 1;

			if($scope.iftrans5.ifend5 < $scope.iftrans5.ifstart5)
				$scope.iftrans5.ifend5 = $scope.iftrans5.ifstart5 + 1;
		}else if(rowNumber == 6){
			if(angular.isUndefined($scope.iftrans7))
				$scope.iftrans7 = {};
			$scope.iftrans7.ifstart7 = $scope.iftrans6.ifend6 + 1;

			if($scope.iftrans6.ifend6 < $scope.iftrans6.ifstart6)
				$scope.iftrans6.ifend6 = $scope.iftrans6.ifstart6 + 1;
		}else if(rowNumber == 7){
			if(angular.isUndefined($scope.iftrans8))
				$scope.iftrans8 = {};
			$scope.iftrans8.ifstart8 = $scope.iftrans7.ifend7 + 1;

			if($scope.iftrans7.ifend7 < $scope.iftrans7.ifstart7)
				$scope.iftrans7.ifend7 = $scope.iftrans7.ifstart7 + 1;
		}else if(rowNumber == 8){
			if(angular.isUndefined($scope.iftrans9))
				$scope.iftrans9 = {};
			$scope.iftrans9.ifstart9 = $scope.iftrans8.ifend8 + 1;

			if($scope.iftrans8.ifend8 < $scope.iftrans8.ifstart8)
				$scope.iftrans8.ifend8 = $scope.iftrans8.ifstart8 + 1;
		}else if(rowNumber == 9){
			if(angular.isUndefined($scope.iftrans10))
				$scope.iftrans10 = {};
			$scope.iftrans10.ifstart10 = $scope.iftrans9.ifend9 + 1;

			if($scope.iftrans9.ifend9 < $scope.iftrans9.ifstart9)
				$scope.iftrans9.ifend9 = $scope.iftrans9.ifstart9 + 1;
		}
	}

}]);