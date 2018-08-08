/**
 * 
 */
kenureApp.controller('commissioningcontroller', ['$scope','$http','$location','$window','$uibModal','$filter','RouteFileLine',function($scope, $http,$location,$window,$modal,$filter,RouteFileLine){
	$scope.emailFormat = /^[a-zA-Z]+[a-zA-Z0-9._]+@[a-zA-Z]+\.[a-zA-Z.]{2,5}$/;
	$scope.paginationList = [50,60,70,80,90,100];
	$scope.isHeader = false;
	$scope.rowIndexList = [];
	$scope.rowBDCList = [];
	$scope.selectAll = false;
	$scope.selectAllBDC = false;
	$scope.dcIdsList = [];
	$scope.scheduledFlag = false;
	$scope.scheduledDate = null;
	$scope.selectedRegionid = -1;
	$scope.selectedSiteid = -1;
	$scope.assignedEndpoints = 0;
	$scope.installerName;
	$scope.dcLocations = [];
	$scope.assignedDCs = 0;
	$scope.repeatersLocations = [];
	$scope.assignedRepeaters = 0;
	$scope.totalNoOfDCsToBeInstalled = null;
	$scope.totalNoOfRepeatersToBeInstalled = null;
	var map = new Map();
	$scope.dcSerialNoList = [];
	/*// style change
	$scope.changeBorder = function(eventName){
		if(angular.equals(eventName,'assign'))
			$scope.borderColor = { 'border-color': '#030303' };
		else if(angular.equals(eventName,'discard'))
			$scope.discardBorderColor = {'border-color': '#030303'}

	}
	$scope.recoverBorder = function(eventName){
		if(angular.equals(eventName,'assign'))
			$scope.borderColor = {'border-color': '#F3F3F3'}
		else if(angular.equals(eventName,'discard'))
			$scope.discardBorderColor = {'border-color': '#F3F3F3'}
	}*/

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

//	Installation and Commissioning 

	$scope.initInstallationAndCommissioningData = function(){

		$scope.setOperatingMode = true;
		$scope.checkDCConnectivity = true;
		$http({
			method: "POST",
			url:url+"/initInstallationAndCommissioningData",
			params:{},
		}).success(function(data){
			$scope.dcList = data[0];
			$scope.regionList = data[1];
			$scope.installationList = $filter('orderBy')(data[2], "siteId")
			$scope.totalSpareDC = data[3];
		}).error(function(data,status){
			// Error Code
		});
	}

	$scope.showHideSites = function(regionId){
		if($scope.selectedRegionid == regionId){
			$scope.selectedRegionid = -1;
			$scope.selectedSiteid = -1;
		}else{
			$scope.selectedRegionid = regionId;
			$scope.selectedSiteid = -1;
		}
	}

	$scope.showHideDc = function(siteId,dc){
		if($scope.selectedSiteid == siteId){
			$scope.selectedSiteid = -1;
			$scope.selectAllDc = false;
			$scope.dcSerialNoList = [];
		}else{
			$scope.selectedSiteid = siteId;
			$scope.selectAllDc = true;
			$scope.dcSerialNoList = [];
			for(count=0;count<dc.length;count++){
				$scope.dcSerialNoList.push(dc[count].dcSerialNumber);
			}
			if($scope.dcSerialNoList.length == 1){
				$scope.viewNetworkMap = true;
			}else{
				$scope.viewNetworkMap = false;
			}
		}
	}

	$scope.pushOrPopDcSerialNo = function(dcSerialNumber){
		//If it is already checked then on unCheck pop else push
		if($scope.dcSerialNoList.indexOf(dcSerialNumber) === -1) {
			$scope.dcSerialNoList.push(dcSerialNumber);
		}else{
			var index  = $scope.dcSerialNoList.indexOf(dcSerialNumber);
			$scope.dcSerialNoList.splice(index, 1);
		}
		if($scope.dcSerialNoList.length == 1){
			$scope.viewNetworkMap = true;
		}else{
			$scope.viewNetworkMap = false;
		}
		if($scope.dcSerialNoList.length == 0){
			$scope.setOperatingMode = false;
			$scope.checkDCConnectivity = false;
		}else{
			$scope.setOperatingMode = true;
			$scope.checkDCConnectivity = true;
		}
	}
	
	$scope.redirectToNetworkMap = function(){
		if($scope.dcSerialNoList.length == 1){
			$window.open(url+"/dcConnectionMap?dcserialnumber="+$scope.dcSerialNoList[0],'_blank');
		}
	}
	
	$scope.selectAllCheckBox = function(){
		if($scope.dcIdsList.length >0){
			$scope.dcIdsList = [];
		}
		if($scope.selectAll == true){
			$scope.selectAll = false;
			$scope.dcIdsList = [];
		}else if($scope.selectAll == false){
			$scope.selectAll = true;
			for(count=0;count<$scope.dcList.length;count++){
				$scope.dcIdsList.push($scope.dcList[count].datacollectorId);
			}
		}
	}

	$scope.pushOrPopDc = function(datacollectorId) {
		//If it is already checked then on unCheck pop else push
		if($scope.dcIdsList.indexOf(datacollectorId) === -1) {
			$scope.dcIdsList.push(datacollectorId);
		}else{
			var index  = $scope.dcIdsList.indexOf(datacollectorId);
			$scope.dcIdsList.splice(index, 1);
		}
	}

	$scope.dataCollectorFileNames = [];
	$scope.diableCheckConnectionBtn = true;
	$scope.filesVerifedFlag = false;
	
	$scope.initdcEpConfigTest = function(siteId){
		$scope.selectedSiteid = siteId;
		$http({
			method: "POST",
			url:url+"/initdcEpConfigTest",
			params:{siteId:siteId},
		}).success(function(data){
			$scope.dcFileList = data[0];
			$scope.custCode = data[1].custCode;
			$scope.siteName = data[1].siteName;
			$scope.siteID = data[1].siteID;
			$scope.regionName = data[1].regionName;
			$scope.status = data[1].status;
			$scope.totalDC = data[1].totalDC;
			$scope.updatedDateTime = data[1].updatedDateTime;
			$scope.updatedBy = data[1].updatedBy;
			$scope.datacollectorsList = data[2];
			$scope.tag = data[3];
			if($scope.tag>=7 && $scope.tag<=8)
				$scope.filesVerifedFlag = true;
			else
				$scope.filesVerifedFlag = false;
			
			angular.forEach($scope.dcFileList, function(dcFile, index) {
				if($scope.filesVerifedFlag){
					if(dcFile.noOfDatacollectors > -1 && (dcFile.isFileUploaded!='Yes' || dcFile.isFileVerified!='Yes')){
						$scope.filesVerifedFlag=false;
					}
					if(dcFile.noOfDatacollectors > -1  && (dcFile.isFileUploaded == 'Yes' || dcFile.isFileVerified == 'Yes')){
						$scope.dataCollectorFileNames.push(dcFile.fileName);
					}
				}
			});
			if($scope.filesVerifedFlag && $scope.dcFileList.length>0){
				$scope.diableCheckConnectionBtn = false;
				$scope.tag=8;
			}
		}).error(function(data,status){
			// Error Code
		});
	}
	
	/*Saurabh Code Start*/
	$scope.configureAndTestDataCollectors = function(){
		if($scope.dataCollectorFileNames.length>0 && $scope.filesVerifedFlag && $scope.tag==8){
			$scope.diableCheckConnectionBtn = true;
			$http({
				method: "POST",
				url:url+"/configureAndTestDataCollectors",
				params:{fileNameList:$scope.dataCollectorFileNames,siteId:$scope.selectedSiteid}
			}).success(function(data){
				if(data!="error"){
					$scope.datacollectorsList = data[0];
					$scope.tag = data[1];
					$scope.waitForConfigurationMsg = data[2];
				}
			}).error(function(data,status){
			});
		}else{
			alert("All uploaded files must be properly verified before moving further!");
		}
	}
	
	$scope.startContinueInstallation = function(){
		if($scope.selectedSiteid != -1){
			var form = document.createElement("form");
			input = document.createElement("input");
			form.action = url+'/customerOperation/startContinueInstallation';
			form.method = "POST"
				input.type = "hidden";
			input.name = "siteID";
			input.value = $scope.selectedSiteid;
			form.appendChild(input);
			document.body.appendChild(form);
			form.submit();
		}else{
			swal("Warning!", "Please select any one site !", "warning")
		}
	}

	$scope.initStartContinueinstallation = function(siteID){
		$scope.selectedSiteid = siteID;
		$http({
			method: "POST",
			url:url+"/customerOperation/initStartContinueinstallation",
			params:{siteId:siteID},
		}).success(function(response){
			if(response!="error"){
				$scope.tag = response[0];
				if(angular.equals(response,'filenotfound')){
					swal("Warning!", "File not found !", "warning")
				}
				if(response[1]){
					$scope.custCode = response[1].custCode;
					$scope.siteName = response[1].siteName;
					$scope.siteID = response[1].siteID;
					$scope.regionName = response[1].regionName;
					$scope.status = response[1].status;
					$scope.totalDC = response[1].totalDC;
					$scope.updatedDateTime = response[1].updatedDateTime;
					$scope.updatedBy = response[1].updatedBy;
				}

				if(response[2]){
					$scope.displayUploadedRouteFile = true;
					$scope.selectAll = false;
					$scope.routeFiledata = response[2];
					if(response[2][0][19]){
						$scope.scheduledDate = response[2][0][19];
					}
					$scope.originalFileLength = $scope.routeFiledata.length;
					$scope.routeFilelength=$scope.originalFileLength;
					// For pagination
					var defaultPaging = 50;
					$scope.recordSize = defaultPaging.toString();
					$scope.totalItems = $scope.originalFileLength;
					$scope.currentPage = 1;
					$scope.itemsPerPage = $scope.recordSize;
					$scope.maxSize = 5; //Number of pager buttons to show

					if($scope.tag > 2 && $scope.tag < 5){
						$scope.phase2data();
					}
				}
			}else if(response=="error"){
				swal("Warning!", "File not found on the server or may be it is deleted from the server !", "warning")
			}else{
				swal("Warning!", "Some unexpected error occured !", "warning")
			}
		}).error(function(response,status){
			// Error Code
		});
	}

	$scope.setflag = function(flag){
		if(flag=="scheduled"){
			$scope.scheduledFlag = true;
			if(!angular.isUndefined($scope.scheduledDate) && $scope.scheduledDate!=null)
				$scope.tag = 2;
			else{
				$scope.displayUploadedRouteFile = false;
				$("#browseBtn").val("");
				$scope.tag = 1;
			}
		}else{
			$scope.displayUploadedRouteFile = false;
			$("#browseBtn").val("");
			$scope.scheduledFlag = false;
			$scope.tag = 2;
			$scope.scheduledDate = null;
		}
	}

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

	$scope.setDate = function(year, month, day) {
		$scope.activedate = new Date(year, month, day);
		$scope.expirydate = new Date(year, month, day);
	};

	$scope.formats = ['dd-MM-yyyy HH:mm:ss', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
	$scope.format = $scope.formats[0];
	$scope.altInputFormats = ['M!/d!/yyyy'];

	$scope.popup1 = {
			opened: false
	};
	// Datepicker code ends

	//On Browse Click event
	$scope.onBrowseClick = function(){
		$scope.displayUploadedRouteFile = false;
	}

	//Upload Route File
	$scope.uploadRouteFile = function () {
		var file = $scope.routeFile;
		var uploadUrl = url+"/uploadRouteFile";
		if (file == undefined) {
			swal("Warning!", "Nothing to upload ! Please add file.", "warning")
		} else {
			$scope.displayUploadedRouteFile = false;
			var fd = new FormData();
			fd.append('file', file);
			fd.append('isHeader',$scope.isHeader);
			fd.append('scheduledDate',$scope.scheduledDate)
			$http.post(uploadUrl, fd, {
				transformRequest: angular.identity,
				headers: {'Content-Type': undefined}
			})
			.success(function(result){
				if(result!="error"){
					$scope.displayUploadedRouteFile = true;
					$scope.selectAll = false;
					$scope.routeFiledata = result;
					$scope.originalFileLength = $scope.routeFiledata.length;
					$scope.routeFilelength=$scope.originalFileLength;
					// For pagination
					$scope.recordSize = 50;
					$scope.totalItems = $scope.originalFileLength;
					$scope.currentPage = 1;
					$scope.itemsPerPage = $scope.recordSize;
					$scope.maxSize = 5; //Number of pager buttons to show
				}else{
					//alert(result);
				}
			})
			.error(function(){
			});
		}
	}

	$scope.pushOrPop = function(index) {
		//If it is already checked then on unCheck pop else push
		if($scope.rowIndexList.indexOf(index) === -1) {
			$scope.rowIndexList.push(index);
		}else{
			var listIndex  = $scope.rowIndexList.indexOf(index);
			$scope.rowIndexList.splice(listIndex, 1);
		}
	}

	$scope.selectAllFun = function(){
		if($scope.rowIndexList.length >0){
			$scope.rowIndexList = [];
		}
		if($scope.selectAll == true){
			$scope.selectAll = false;
			$scope.rowIndexList = [];
		}else{
			$scope.selectAll = true;
			for(count=0;count<$scope.routeFiledata.length;count++){
				$scope.rowIndexList.push(count);
			}
		}
	}

	//Update Selected Endpoints parameters
	$scope.editSelectedLines = function(){
		if($scope.rowIndexList.length > 0){
			var allSelectedRowIndexList = [];
			allSelectedRowIndexList = $scope.rowIndexList;

			var ModalInstance = $modal.open({
				size: 'lg',
				controller: function($uibModalInstance ,$scope,allSelectedRowIndexList,RouteFileLine){
					$scope.fileLine = RouteFileLine.getFileLine();
					$scope.close = function () {
						$uibModalInstance.dismiss('cancel');
					};
					$scope.update = function () {
						RouteFileLine.setFileLine($scope.fileLine);
						$uibModalInstance.close(RouteFileLine.getFileLine());
					}
				},
				resolve: {
					allSelectedRowIndexList: function () {
						return allSelectedRowIndexList;
					}
				},
				templateUrl: url+'/selectedLinesPopup',
			});
		}else{
			swal("Warning!", "Please select atleast one checkBox !", "warning")
		}
		ModalInstance.result.then(function (result) {
			angular.forEach(allSelectedRowIndexList,function(value,index){
				$scope.routeFiledata[value][9]=result.col9;
				$scope.routeFiledata[value][10]=result.col10;
				$scope.routeFiledata[value][11]=result.col11;
				$scope.routeFiledata[value][12]=result.col12;
				$scope.routeFiledata[value][13]=result.col13;
				$scope.routeFiledata[value][14]=result.col14;
				$scope.routeFiledata[value][15]=result.col15;
				$scope.routeFiledata[value][16]=result.col16;
				$scope.routeFiledata[value][17]=result.col17;
				$scope.routeFiledata[value][18]=result.col18;
			});
		}, function () {
		});
	}

	//Add New Line to Uploaded Route file
	$scope.addLine = function (){
		var ModalInstance = $modal.open({
			size: 'lg',
			controller: function($uibModalInstance ,$scope,RouteFileLine){
				//$scope.fileLine = RouteFileLine.getWholeFileLine(); (For reference replaced by below line)
				$scope.fileLine = {
						"col0":'',"col1":'',"col2":'',"col3":'',"col4":'',"col5":'',"col6":'',"col7":'',"col8":'',"col9":''
							,"col10":'',"col11":'',"col12":'',"col13":'',"col14":'',"col15":'',"col16":'',"col17":'',"col18":''
				};
				$scope.close = function () {
					$uibModalInstance.dismiss('cancel');
				};
				$scope.add = function () {
					RouteFileLine.setWholeFileLine($scope.fileLine);
					$uibModalInstance.close(RouteFileLine.getWholeFileLine());
				}
			},
			templateUrl: url+'/addNewLinePopup',
		});

		ModalInstance.result.then(function (result) {
			var myArray =[];
			angular.forEach(result,function(value,index){
				myArray.push(value);
			});
			if($scope.scheduledDate){
				myArray.push($scope.routeFiledata[0][19]);
			}
			$scope.routeFiledata.push(myArray);
			$scope.routeFilelength=$scope.routeFiledata.length;
		}, function () {
		});
	}

	/*//For Date Conversion
	$scope.convertDate = function(str) {
	    var date = new Date(str),
	        mnth = ("0" + (date.getMonth()+1)).slice(-2),
	        day  = ("0" + date.getDate()).slice(-2),
	    	time = date.toString().split(' ')[4];
	    	var outputdate = [ day, mnth, date.getFullYear()].join("-");
	    	outputdate = outputdate.concat(" ").concat(time);
	    return outputdate;
	}*/

	//Remove last Line from Uploaded Route file
	$scope.removeLine = function (){
		if($scope.routeFiledata.length > $scope.originalFileLength){
			$scope.routeFiledata.pop();
			$scope.routeFilelength=$scope.routeFiledata.length;
		}
	}

	//Update Route File
	$scope.generateMasterFile = function (){
		var updateUrl = url+"/generateMasterFile";
		$http.post(updateUrl,$scope.routeFiledata)
		.success(function(result){
			if(result!="Error"){
				$scope.updatedDateTime = result.updatedDate;
				$scope.updatedBy = result.updatedBy;
				$scope.tag = result.tag;
				$scope.phase2data();
			}else{
				alert(result);
			}
		}).error(function(data){
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
	/*Saurabh Code End*/

	//	darshit's code start
	$scope.phase2data = function(){
		$http({
			method: "POST",
			url:url+"/generatePhase2InitData",
			params:{siteId:$scope.selectedSiteid},
		}).success(function(data){
			$scope.hashData = data[0];
			$scope.installerName = data[1];
			//Total Number of Endpoints to be installed will generated here
			$scope.totalNoOfEndpointsToBeInstalled =  data[2];
			$scope.tag = data[3];
			$scope.fileNameList = data[4];
		}).error(function(data,status){

		})
	}

	$scope.update = function(x,street,endpoint){
		$scope.removeCurrentElement(map,street,endpoint);
		if(map.keys.indexOf(x) == -1){
			var value = new Array();
			var pushObj = new Object();
			pushObj.street = street;
			pushObj.endpoint = endpoint;
			value.push(pushObj);
			map.put(x, value);
		}else{
			var value = map.data[x];
			var pushObj = new Object();
			pushObj.street = street;
			pushObj.endpoint = endpoint;
			value.push(pushObj);
			map.put(x, value);
		}
		$scope.assignedEndpoints = $scope.assignedEndpoints + endpoint;
		$scope.angularMap = map;
	}

	$scope.getNumberOfEndPoints = function(value){
		var endPoint = 0;
		for(var i=0;i<value.length;i++){
			endPoint = endPoint + value[i].endpoint;
		}
		return endPoint;
	}

	$scope.getAssignedStreetNames = function(value){
		var streetName = "";
		for(var i=0;i<value.length;i++){
			streetName = streetName + value[i].street +", ";
		}
		return streetName;
	}

	$scope.removeCurrentElement = function(map,street,endpoint){
		for(var i=0;i<$scope.installerName.length;i++){
			var value = map.get($scope.installerName[i]);
			if(!(value === undefined) ){
				for(var j=0;j<value.length;j++){
					if(value[j].endpoint == endpoint && value[j].street == street){
						$scope.assignedEndpoints = $scope.assignedEndpoints - endpoint;
						value.splice(j,1);
					}

				}
			}

		}
	}

	$scope.insFilesGenerated = function(){
		
		swal({
			   title: 'Confirm',
			   text: 'After generating Installation files phase will be locked. You will not able to change data. Want to continue ?',
			   type: 'warning',
			   showCancelButton: true,
			   confirmButtonText: 'Yes',
			   cancelButtonText: 'Cancel'
			}, function(){
				
				var responseArray = new Array();
				var groupArray = new Array();
	
				for(var i=0;i<map.keys.length;i++){
					var obj = new Object();
					var keyValue = map.keys[i];
					var value = map.get(keyValue);
					obj.installerName = keyValue;
					obj.groupValue = value;
					responseArray.push(obj);
				}	
				$scope.rArray = responseArray;
				var response = $http.post('generateInstFiles', {responseArray:responseArray,siteId:$scope.selectedSiteid});
				response.success(function(data, status, headers, config) {
					$scope.fileNameList = data[0];
					$scope.tag = data[1];
				}).error(function(data, status, headers, config) {
					swal("Warning!", "Unexpected error !", "warning")
				})
				
			});
		
	}

	$scope.updateFile = function(){
		$http({
			method: "POST",
			url:url+"/generateFileDataToDisplay",
			params:{fileName:$scope.fileName},
		}).success(function(data){
			$scope.insData = data;
		}).error(function(data,status){

		})
	}

	$scope.checkForTag = function() {
		if(parseInt($scope.tag) > 3)
			return true;
		return false;
	}

	$scope.checkForEnable = function(){
		if(parseInt($scope.tag) >= 4)
			return true;
		return false;
	}

	$scope.checkForGenerateInsDisable = function(){
		if($scope.assignedEndpoints != $scope.totalNoOfEndpointsToBeInstalled || $scope.tag > 3){
			return true;
		}else{
			return false;
		}
	}

	$scope.assignToIns = function(){
		$http({
			method: "POST",
			url:url+"/assignToInstaller",
			params:{list:$scope.fileNameList,siteId:$scope.selectedSiteid},
		}).success(function(data){
			if(angular.equals(data,'success')){
				$window.location.href = url+"/dataCollectorInstallation";
			}else{
				swal("Warning!", "Unexpected error!", "warning")
			}
		}).error(function(data,status){
		});
	}

	$scope.discardChanges = function(){
		// Here we have to pass siteId which should be discarded !
		$http({
			method: "POST",
			url:url+"/discardChanges",
			params:{siteId:$scope.selectedSiteid},
		}).success(function(data){
			if(!angular.equals(data,'error')){
				$scope.tag = data;
				$scope.insData = [];
				var map = new Map();
				$scope.angularMap = map;
			}else{
				swal("Error!", "Some unexpected error occured...Please try again!", "error");
			}

		}).error(function(data,status){
		});
	}

	/*saurabh code for install Dc page*/
	$scope.initDcLocaionInstallation = function(siteId){
		$scope.selectedSiteid = siteId;
		$http({
			method: "POST",
			url:url+"/initDcLocaionInstallation",
			params:{siteId:siteId},
		}).success(function(data){
			if(data != "error"){
				
				$scope.tag = data[0]
				$scope.installerName = data[1];
				
				$scope.custCode = data[2].custCode;
				$scope.siteName = data[2].siteName;
				$scope.siteID = data[2].siteID;
				$scope.regionName = data[2].regionName;
				$scope.status = data[2].status;
				$scope.totalDC = data[2].totalDC;
				$scope.updatedDateTime = data[2].updatedDateTime;
				$scope.updatedBy = data[2].updatedBy;
				
				// For pagination
				$scope.recordSize = 50;
				$scope.totalItems = $scope.dcLocations.length;
				$scope.currentPage = 1;
				$scope.itemsPerPage = $scope.recordSize;
				$scope.maxSize = 5; //Number of pager buttons to show
				$scope.bdcList = data[3];
				$scope.sitebdc = data[4];

				if($scope.tag===6){
					$scope.fileNameList = data[5];
				}
			}else{
				alert(data);
			}
		}).error(function(data,status){

		})
	}

	$scope.addNewDclocation = function(){
		$scope.dcLocations.push([null,null,null,null]);
		$scope.totalItems = $scope.dcLocations.length;
		$scope.totalNoOfDCsToBeInstalled = $scope.totalNoOfDCsToBeInstalled+1;
	}

	$scope.removeDcLocation = function(){
		var lastRowIndex = $scope.dcLocations.length - 1;
		if(lastRowIndex!=-1){
			for(var i=0;i<map.keys.length;i++){
				var keyValue = map.keys[i];
				var value = map.get(keyValue);
				for(var j=0;j<value.length;j++){
					if(value[j].rowIndex == lastRowIndex){
						value.splice(j,1);
						$scope.assignedDCs = $scope.assignedDCs - 1;
						if(value.length===0){
							map.keys.splice(i,1);
							map.remove(keyValue);
						}
					}
				}
			}
			$scope.angularMap = map;
			$scope.dcLocations.pop();
			$scope.totalItems = $scope.dcLocations.length;
			$scope.totalNoOfDCsToBeInstalled = $scope.totalNoOfDCsToBeInstalled-1;
			if($scope.totalNoOfDCsToBeInstalled===0)
				$scope.totalNoOfDCsToBeInstalled = null;
		}
	}

	$scope.generateDcInstallationFiles = function(){
		if($scope.assignedDCs === $scope.totalNoOfDCsToBeInstalled){
			
			swal({
				   title: 'Confirm',
				   text: 'After generating Installation files phase will be locked. You will not able to change data. Want to continue ?',
				   type: 'warning',
				   showCancelButton: true,
				   confirmButtonText: 'Yes',
				   cancelButtonText: 'Cancel'
				}, function(){
					var responseArray = new Array();
					for(var i=0;i<$scope.angularMap.keys.length;i++){
						var obj = new Object();
						var keyValue = $scope.angularMap.keys[i];
						var value = $scope.angularMap.get(keyValue);
						obj.installerName = keyValue;
						obj.groupValue = value;
						responseArray.push(obj);
					}
					var response = $http.post('generateDcInstallationFiles', {siteId:$scope.selectedSiteid,responseArray:responseArray});
					response.success(function(data, status, headers, config) {
						$scope.fileNameList = data[0];
						$scope.tag = data[1];
					}).error(function(data, status, headers, config) {
						swal("Warning!", "Unexpected error !", "warning")
					})
				});
			
		}
		else
			swal("Information", "Few DataCollector Locations are yet to be assigned.", "warning")
	}
	
	$scope.updateRightSideTable = function(name,index){
		if(name){
			$scope.removeElementIfAlreadyExist(map,name,index);
			if(map.keys.indexOf(name) === -1){
				var value = new Array();
				var pushObj = new Object();
				pushObj.rowIndex = index;
				pushObj.dcLocation = $scope.dcLocations[index][0];
				pushObj.networkType = $scope.dcLocations[index][1];
				pushObj.latitude = $scope.dcLocations[index][2];
				pushObj.longitude = $scope.dcLocations[index][3];
				value.push(pushObj);
				map.put(name, value);
			}else{
				var value = map.data[name];
				var pushObj = new Object();
				pushObj.rowIndex = index;
				pushObj.dcLocation = $scope.dcLocations[index][0];
				pushObj.networkType = $scope.dcLocations[index][1];
				pushObj.latitude = $scope.dcLocations[index][2];
				pushObj.longitude = $scope.dcLocations[index][3];
				value.push(pushObj);
				map.put(name, value);
			}
			$scope.assignedDCs = $scope.assignedDCs + 1;
			$scope.angularMap = map;
		}
	}

	$scope.removeElementIfAlreadyExist = function(map,name,index){
		for(var i=0;i<$scope.installerName.length;i++){
			var value = map.get($scope.installerName[i]);
			if(!(value === undefined)){
				for(var j=0;j<value.length;j++){
					if(value[j].rowIndex === index){
						value.splice(j,1);
						if(value.length===0){
							var indexOfKey = map.keys.indexOf($scope.installerName[i]);
							map.keys.splice(indexOfKey,1);
							map.remove($scope.installerName[i]);
						}
						if($scope.assignedDCs!=0){
							$scope.assignedDCs = $scope.assignedDCs - 1;
						}
					}
				}
			}
		}
	}

	$scope.enableDisableGenreateBtn = function(){
		if($scope.assignedDCs != $scope.totalNoOfDCsToBeInstalled || $scope.tag > 5)
			return true;
		else
			return false;
	}

	$scope.viewDCFilesData = function(filename){
		$http({
			method: "POST",
			url:url+"/generateDCFileData",
			params:{fileName:filename},
		}).success(function(data){
			$scope.dcData = data;
		}).error(function(data){
			alert(data);
		})
	}

	$scope.assignDcToInstaller = function(){
		$http({
			method: "POST",
			url:url+"/assignDcToInstaller",
			params:{list:$scope.fileNameList,siteId:$scope.selectedSiteid},
		}).success(function(data){
			if(angular.equals(data,'success')){
				$window.location.href = url+"/dcEpConfigurationAndTest";//redirect to dcEpConfigurationAndTest page
			}else{
				swal("Warning!", "Unexpected error !", "warning")
			}
		}).error(function(data,status){
		});
	}

	$scope.discardInstallationFiles = function(){
		// Here we have to pass siteId which should be discarded !
		$http({
			method: "POST",
			url:url+"/discardInstallationFiles",
			params:{siteId:$scope.selectedSiteid},
		}).success(function(data){
			if(!angular.equals(data,'error')){
				$scope.tag = data;
				$scope.dcData = [];
				var map = new Map();
				$scope.angularMap = map;
			}else{
				swal("Error!", "Some unexpected error occured...Please try again!", "error");
			}
		}).error(function(data,status){
		});
	}
	/*saurabh code for install Dc page END*/

	/*Darshit code for install Dc page START*/
	$scope.addBDC = function(){
		if($scope.rowIndexList.length > 0){
			$http({
				method: "POST",
				url: url+"/addBDCForDCPlanning",
				params:{siteId:$scope.selectedSiteid,list:$scope.rowIndexList}
			}).success(function(data){
				$scope.rowIndexList = [];
				$scope.rowBDCList = [];
				$scope.selectAll = false;
				$scope.bdcList = data[0];
				$scope.sitebdc = data[1];
			}).error(function(data,status){
				alert(data);
			})
		}else{
			swal("Warning!", "Select atleast one DC !", "warning")
		}

	}

	$scope.removeBDC = function(){
		if($scope.rowBDCList.length > 0){
			$http({
				method: "POST",
				url: url+"/removeBDCForDCPlanning",
				params:{siteId:$scope.selectedSiteid,list:$scope.rowBDCList} 
			}).success(function(data){
				$scope.rowIndexList = [];
				$scope.rowBDCList = [];
				$scope.selectAllBDC = false;
				$scope.bdcList = data[0];
				$scope.sitebdc = data[1];
			}).error(function(data,status){
				alert(data);
			})
		}else{
			swal("Warning!", "Select atleast one DC !", "warning")
		}
	}

	$scope.pushOrPopDCSerialNo = function(serialNo) {
		//If it is already checked then on unCheck pop else push
		if($scope.rowIndexList.indexOf(serialNo) === -1) {
			$scope.rowIndexList.push(serialNo);
		}else{
			var listserialNo  = $scope.rowIndexList.indexOf(serialNo);
			$scope.rowIndexList.splice(listserialNo, 1);
		}
	}

	$scope.selectAllDcSerialNo = function(){
		if($scope.rowIndexList.length >0){
			$scope.rowIndexList = [];
		}
		if($scope.selectAll == true){
			$scope.selectAll = false;
			$scope.rowIndexList = [];
		}else{
			$scope.selectAll = true;
			for(count=0;count<$scope.bdcList.length;count++){
				$scope.rowIndexList.push($scope.bdcList[count].dcSerialNumber);
			}
		}
	}

	$scope.pushOrPopSiteBDCSerialNo = function(serialNo) {
		//If it is already checked then on unCheck pop else push
		if($scope.rowBDCList.indexOf(serialNo) === -1) {
			$scope.rowBDCList.push(serialNo);
		}else{
			var bdcserialNo  = $scope.rowBDCList.indexOf(serialNo);
			$scope.rowBDCList.splice(bdcserialNo, 1);
		}
	}

	$scope.selectAllSiteBDcSerialNo = function(){
		if($scope.rowBDCList.length >0){
			$scope.rowBDCList = [];
		}
		if($scope.selectAllBDC == true){
			$scope.selectAllBDC = false;
			$scope.rowBDCList = [];
		}else{
			$scope.selectAllBDC = true;
			for(count=0;count<$scope.sitebdc.length;count++){
				$scope.rowBDCList.push($scope.sitebdc[count].dcSerialNumber);
			}
		}
	}
	//Darshit's coe ends
	
	//Saurabh Code starts
	$scope.redirectToCommissioningPage = function(){
		$window.location.href = url+"/commissioning";
	}
	
	$scope.redirectToVerficationPage = function(){
		$window.location.href = url+"/verification";
	}
	
	$scope.redirectToRepeaterPage = function(){
		$window.location.href = url+"/repeatersPlanning";
	}
	
	$scope.initRepeaterLocaionInstallation = function(siteId){
		$scope.selectedSiteid = siteId;
		$http({
			method: "POST",
			url:url+"/initRepeaterLocaionInstallation",
			params:{siteId:siteId},
		}).success(function(data){
			if(data != "error"){
				$scope.tag = data[0]
				$scope.installerName = data[1];
				// For pagination
				$scope.recordSize = 50;
				$scope.totalItems = $scope.repeatersLocations.length;
				$scope.currentPage = 1;
				$scope.itemsPerPage = $scope.recordSize;
				$scope.maxSize = 5; //Number of pager buttons to show
				if($scope.tag === 8){
					$scope.fileNameList = data[2];
				}
			}else{
				alert(data);
			}
		}).error(function(data,status){

		})
	}
	
	$scope.addNewRepeaterLocation = function(){
		$scope.repeatersLocations.push([null,null,null,null,null]);
		$scope.totalItems = $scope.repeatersLocations.length;
		$scope.totalNoOfRepeatersToBeInstalled = $scope.totalNoOfRepeatersToBeInstalled+1;
	}

	$scope.removeRepeaterLocation = function(){
		var lastRowIndex = $scope.repeatersLocations.length - 1;
		if(lastRowIndex!=-1){
			for(var i=0;i<map.keys.length;i++){
				var keyValue = map.keys[i];
				var value = map.get(keyValue);
				for(var j=0;j<value.length;j++){
					if(value[j].rowIndex == lastRowIndex){
						value.splice(j,1);
						$scope.assignedRepeaters = $scope.assignedRepeaters - 1;
						if(value.length===0){
							map.keys.splice(i,1);
							map.remove(keyValue);
						}
					}
				}
			}
			$scope.angularMap = map;
			$scope.repeatersLocations.pop();
			$scope.totalItems = $scope.repeatersLocations.length;
			$scope.totalNoOfRepeatersToBeInstalled = $scope.totalNoOfRepeatersToBeInstalled-1;
			if($scope.totalNoOfRepeatersToBeInstalled===0)
				$scope.totalNoOfRepeatersToBeInstalled = null;
		}
	}
	
	$scope.updateRepeaterRightSideTable = function(name,index){
		if(name){
			$scope.removeRepeaterElementIfAlreadyExist(map,name,index);
			if(map.keys.indexOf(name) === -1){
				var value = new Array();
				var pushObj = new Object();
				pushObj.rowIndex = index;
				pushObj.repeaterLocation = $scope.repeatersLocations[index][0];
				pushObj.latitude = $scope.repeatersLocations[index][1];
				pushObj.longitude = $scope.repeatersLocations[index][2];
				pushObj.slots = $scope.repeatersLocations[index][3];
				pushObj.levels = $scope.repeatersLocations[index][4];
				value.push(pushObj);
				map.put(name, value);
			}else{
				var value = map.data[name];
				var pushObj = new Object();
				pushObj.rowIndex = index;
				pushObj.repeaterLocation = $scope.repeatersLocations[index][0];
				pushObj.latitude = $scope.repeatersLocations[index][1];
				pushObj.longitude = $scope.repeatersLocations[index][2];
				pushObj.slots = $scope.repeatersLocations[index][3];
				pushObj.levels = $scope.repeatersLocations[index][4];
				value.push(pushObj);
				map.put(name, value);
			}
			$scope.assignedRepeaters = $scope.assignedRepeaters + 1;
			$scope.angularMap = map;
		}
	}

	$scope.removeRepeaterElementIfAlreadyExist = function(map,name,index){
		for(var i=0;i<$scope.installerName.length;i++){
			var value = map.get($scope.installerName[i]);
			if(!(value === undefined)){
				for(var j=0;j<value.length;j++){
					if(value[j].rowIndex === index){
						value.splice(j,1);
						if(value.length===0){
							var indexOfKey = map.keys.indexOf($scope.installerName[i]);
							map.keys.splice(indexOfKey,1);
							map.remove($scope.installerName[i]);
						}
						if($scope.assignedRepeaters!=0){
							$scope.assignedRepeaters = $scope.assignedRepeaters - 1;
						}
					}
				}
			}
		}
	}
	
	$scope.generateRepeaterInstallationFiles = function(){
		if($scope.assignedRepeaters === $scope.totalNoOfRepeatersToBeInstalled){
			
			swal({
				   title: 'Confirm',
				   text: 'After generating Installation files phase will be locked. You will not able to change data. Want to continue ?',
				   type: 'warning',
				   showCancelButton: true,
				   confirmButtonText: 'Yes',
				   cancelButtonText: 'Cancel'
				}, function(){
					var responseArray = new Array();
					for(var i=0;i<$scope.angularMap.keys.length;i++){
						var obj = new Object();
						var keyValue = $scope.angularMap.keys[i];
						var value = $scope.angularMap.get(keyValue);
						obj.installerName = keyValue;
						obj.groupValue = value;
						responseArray.push(obj);
					}	
					var response = $http.post('generateRepeaterInstallationFiles', {siteId:$scope.selectedSiteid,responseArray:responseArray});
					response.success(function(data, status, headers, config) {
						$scope.fileNameList = data[0];
						$scope.tag = data[1];
					}).error(function(data, status, headers, config) {
						swal("Warning!", "Unexpected error !", "warning")
					})
				});
		}else{
			swal("Information", "Few Repeater Locations are yet to be assigned.", "warning")
		}
	}
	
	$scope.enableDisableRepeaterGenerateBtn = function(){
		if($scope.assignedRepeaters != $scope.totalNoOfRepeatersToBeInstalled || $scope.tag > 7)
			return true;
		else
			return false;
	}
	
	$scope.viewRepeaterFilesData = function(filename){
		$http({
			method: "POST",
			url:url+"/generateRepeatersFileData",
			params:{fileName:filename},
		}).success(function(data){
			$scope.dcData = data;
		}).error(function(data){
			alert(data);
		})
	}
	
	$scope.assignRepeatersToInstaller = function(){
		$http({
			method: "POST",
			url:url+"/assignRepeatersToInstaller",
			params:{list:$scope.fileNameList,siteId:$scope.selectedSiteid},
		}).success(function(data){
			if(angular.equals(data,'success')){
				$window.location.href = url+"/installationAndCommissioning";//redirect to installationAndCommissioning page
			}else{
				swal("Warning!", "Unexpected error !", "warning")
			}
		}).error(function(data,status){
		});
	}

	$scope.discardRepeaterInstallationFiles = function(){
		// Here we have to pass siteId which should be discarded !
		$http({
			method: "POST",
			url:url+"/discardRepeaterInstallationFiles",
			params:{siteId:$scope.selectedSiteid},
		}).success(function(data){
			if(!angular.equals(data,'error')){
				$scope.tag = data;
				$scope.dcData = [];
				var map = new Map();
				$scope.angularMap = map;
			}else{
				swal("Error!", "Some unexpected error occured...Please try again!", "error");
			}

		}).error(function(data,status){
		});
	}
	//Saurabh Code End
	
	// Dhaval Code Start
	
	$scope.initCommissioningPage = function(siteId){
		$scope.selectedSiteid = siteId;
		$http({
			method: "POST",
			url:url+"/initCommissioningPage",
			params:{siteId : siteId},
		}).success(function(data){
			$scope.custCode = data[0].custCode;
			$scope.siteName = data[0].siteName;
			$scope.siteID = data[0].siteID;
			$scope.regionName = data[0].regionName;
			$scope.status = data[0].status;
			$scope.totalDC = data[0].totalDC;
			$scope.updatedDateTime = data[0].updatedDateTime;
			$scope.updatedBy = data[0].updatedBy;
			$scope.tag = data[1];
			$scope.commissioningType = data[2];
			if(angular.equals($scope.commissioningType,'manual')){
				$scope.toggleCommissioningType = true;
				$scope.commCheckboxDisable = true;
				if($scope.tag>10 && $scope.tag<=13){
					$scope.level1DatacollectorsList = data[3];
				}
				if($scope.tag>11 && $scope.tag<=13){
					$scope.isCommCheckboxSelected = true;
				}
				if($scope.tag==11){
					$scope.commFlag = true;
					var i;
					for(i=0;i<$scope.level1DatacollectorsList.length;i++){
						if($scope.commFlag){
							if($scope.level1DatacollectorsList[i].isLevel1CommStarted=="No"){
								$scope.commFlag = false;
							}
						}
					}
					if($scope.commFlag){
						$scope.commCheckboxDisable = false;
					}
				}
				if($scope.tag==13){
					$scope.levelNDatacollectorsList = data[4];
				}
			}else{
				$scope.toggleCommissioningType = false;
			}
		}).error(function(data,status){
			// Error Code
		});
		
	}
	// Dhaval Code end
	
	//Saurabh's Code Start
	$scope.startLevel1Commissioning = function(){
		$http({
			method: "POST",
			url:url+"/startLevel1Commissioning",
			params:{siteId : $scope.selectedSiteid},
		}).success(function(data){
			if(data!="error"){
				$scope.level1DatacollectorsList = data[0];
				$scope.tag = data[1];
				$scope.isCommCheckboxSelected = false;
				$scope.commCheckboxDisable = true;
				//$scope.waitForConfigurationMsg = data[2];
			}
		}).error(function(data,status){
			// Error Code
		});
	}
	
	$scope.startLevelNCommissioning = function(){
		$http({
			method: "POST",
			url:url+"/startLevelNCommissioning",
			params:{siteId : $scope.selectedSiteid},
		}).success(function(data){
			if(data!="error"){
				$scope.levelNDatacollectorsList = data[0];
				$scope.tag = data[1];
				//$scope.waitForConfigurationMsg = data[2];
			}
		}).error(function(data,status){
			// Error Code
		});
	}
	
	$scope.changeTag = function(){
		if($scope.tag==11 && !$scope.isCommCheckboxSelected){
			$http({
				method: "POST",
				url:url+"/setTagValue",
				params:{siteId : $scope.selectedSiteid},
			}).success(function(data){
				if(data!="error"){
					$scope.tag = data;
					$scope.isCommCheckboxSelected = true;
				}
			}).error(function(data,status){
				// Error Code
			});
		}else{
			$scope.commCheckboxDisable = true;
		}
		
	}
	
	$scope.initVerification = function(siteId){
		$scope.selectedSiteid = siteId;
		$http({
			method: "POST",
			url:url+"/initVerification",
			params:{siteId : $scope.selectedSiteid},
		}).success(function(data){
			if(data!="error"){
				$scope.tag = data;
				$scope.isCommCheckboxSelected = true;
			}
		}).error(function(data,status){
			// Error Code
		});
	}
	//Saurabh's Code End
	
}]);