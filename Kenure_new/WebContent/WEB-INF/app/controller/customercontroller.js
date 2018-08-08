
/*
var url = 'http://localhost:8080/Kenure'; */

kenureApp.controller('customercontroller', ['$scope','$http','$location','$window','$filter',function($scope, $http,$location,$window,$filter){

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

	$scope.emailFormat = /^[a-zA-Z]+[a-zA-Z0-9._]+@[a-zA-Z]+\.[a-zA-Z.]{2,5}$/;
	$scope.paginationList = [10,20,30,40,50];

	$scope.customerProfileInit = function(){
		$http({
			method: "POST",
			url: url + "/customerProfileInit",
			params:{},
		}).success(function(data){
			if(angular.equals(data,'nosuchuserfound')){
				// code for displaying error
			}else{
				$scope.firstName = data.firstName;
				$scope.lastName = data.lastName;
				$scope.userName = data.userName;
				$scope.address1 = data.address1;
				$scope.address2 = data.address2;
				$scope.address3 = data.address3;
				$scope.streetName = data.streetName;
				$scope.zipcode = data.zipcode;
				$scope.cell_number1 = data.cell_number1;
				$scope.cell_number2 = data.cell_number2;
				$scope.cell_number3 = data.cell_number3;
				$scope.email1 = data.email1;
				$scope.email2 = data.email2;
				$scope.email3 = data.email3;
				if(angular.equals(data.mbPerMonth,"-")){
					$scope.isNormal = true;
				}else{
					$scope.mbPerMonth = data.mbPerMonth;
					$scope.dataPlanActivatedDate = data.dataPlanActivatedDate;
					$scope.dataPlanExpiryDate = data.dataPlanExpiryDate;
				}

			}
		}).error(function(data,status){

		});
	}


	$scope.updateCustomerProfile = function(){

		var firstName = $scope.firstName;
		var lastName = $scope.lastName;
		var userName = $scope.userName;
		var address1 = $scope.address1;
		var address2 = $scope.address2;
		var address3 = $scope.address3;
		var streetName = $scope.streetName;
		var zipcode = $scope.zipcode;
		var cell_number1 = $scope.cell_number1;
		var cell_number2 = $scope.cell_number2;
		var cell_number3 = $scope.cell_number3;
		var email1 = $scope.email1;
		var email2 = $scope.email2;
		var email3 = $scope.email3;
		var mbPerMonth = $scope.mbPerMonth;
		var dataPlanActivatedDate = $scope.dataPlanActivatedDate;
		var dataPlanExpiryDate = $scope.dataPlanExpiryDate;


		var isEmail2Valid = false;
		var isEmail3Valid = false;

		var pattern = /^[a-zA-Z]+[a-zA-Z0-9._]+@[A-Za-z]+\.[a-zA-Z.]{2,5}$/;

		if(!angular.isUndefined(email2) && email2.trim()){

			if(pattern.test(email2)){
				isEmail3Valid = pattern.test(email3);
				isEmail2Valid = true;
			}

		}
		if(!angular.isUndefined(email3) && email3.trim()){

			if(pattern.test(email3)){
				isEmail2Valid = pattern.test(email2);
				isEmail3Valid = true;
			}

		}

		if(angular.isUndefined(email2) || !email2.trim()){
			isEmail2Valid = true;
		}
		if(angular.isUndefined(email3) || !email3.trim()){
			isEmail3Valid = true;
		}

		if(isEmail2Valid == true && isEmail3Valid == true){

			$http({
				method: "POST",
				url:url+"/updateCustomerProfile",
				params:{
					firstName:firstName,
					lastName:lastName,
					userName:userName,
					address1:address1,
					streetName:streetName,
					zipcode:zipcode,
					cell_number1:cell_number1,
					address2:address2,
					address3:address3,
					streetName:streetName,
					cell_number2:cell_number2,
					cell_number3:cell_number3,
					email2:email2, email3:email3,
					email1:email1
				},

			}).success(function(data){
				$scope.isSuccess = true;
				$scope.success = "Profile updated.";

				setTimeout(function ()
						{
					$scope.$apply(function()
							{
						$scope.isSuccess = false;
						$scope.success = "";
							});
						}, 3000);

				//	$state.transitionTo('userManagement'); //back to same page again
			}).error(function(data,status){
				$scope.isError=true;
				$scope.errorMessage="Error while fetching data.";
			});

		}else {
			if(isEmail2Valid == false){
				$scope.invalidemail2 = true;
			}
			if(isEmail3Valid == false){
				$scope.invalidemail3 = true;
			}

			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.invalidemail2 = false;
					$scope.invalidemail3 = false;
						});
					}, 3000);
		}

	}


	//************************************

	$scope.initCustomerSiteData = function(){

		$scope.selectedRegion = "==== Select Value ===";

		$http({
			method:"POST",
			url:url+"/customerOperation/customersitedata",
		}).success(function(data){
			if(data != null){
				$scope.regionNames = data[0];
				$scope.boundryCollector = data[1];
				$scope.assignCollector = data[2];
				$scope.installerName = data[3];
				$scope.districtUMList = data[4];
			}

		}).error(function(data,status){

		});

	}

	//getting list of Region
	$scope.initRegion = function(){

		$http({
			method: "POST",
			url: url + "/customerOperation/getRegion",
		}).success(function (data) {
			$scope.regionListDiv = true;
			$scope.noregionFoundDiv = false;
			$scope.addregion = true;
			$scope.regionList = data;
			$scope.limit = 10;
			$scope.begin = 0;
			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.regionList.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show

			if(angular.equals(data,'youCanNotAccessRegionList')){
				$window.location.href="customerDashboard";
			}

		}).error(function (data,status){

		});	

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

	$scope.open2 = function() {
		$scope.popup2.opened = true;
	};

	$scope.open3 = function() {
		$scope.popup3.opened = true;
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
	
	$scope.popup3 = {
			opened: false
	};
	// Datepicker code ends

	$scope.addNewSite =  function() {

		if($scope.addSite.isChecked){
			if(angular.isUndefined($scope.addSite.districtMSN)){
				$scope.isUndefined = true;
			}else{
				$scope.isUndefined = false;
				$http({
					method:"POST",
					url:url+"/customerOperation/addNewSite",
					params:{"siteName":$scope.addSite.siteName,
						"selectedRegion":$scope.addSite.selectedRegion,"boundryDC":$scope.addSite.boundryDC,
						"assignDC":$scope.addSite.assignDC,"installerName":$scope.addSite.installerName,"dmsn":$scope.addSite.districtMSN},
				}).success(function(data){

					$scope.isSuccess = true;
					$scope.success="Record added. Redirecting to main page."

						window.setTimeout(function() {
							$window.location.href="siteManagement";
						}, 3000);

				}).error(function(data,status){
				});
			}

		}else{
			if(angular.isUndefined($scope.addSite.selectedRegion)
					|| angular.isUndefined($scope.addSite.siteName)){
				$scope.isUndefined = true;
			}else{
				$scope.isUndefined = false;
				$http({
					method:"POST",
					url:url+"/customerOperation/addNewSite",
					params:{"siteName":$scope.addSite.siteName,
						"selectedRegion":$scope.addSite.selectedRegion,"boundryDC":$scope.addSite.boundryDC,
						"assignDC":$scope.addSite.assignDC,"installerName":$scope.addSite.installerName},
				}).success(function(data){

					$scope.isSuccess = true;
					$scope.success="Record added. Redirecting to main page."

						window.setTimeout(function() {
							$window.location.href="siteManagement";
						}, 3000);

				}).error(function(data,status){
				});
			}
		}



	}
	
	// Angular method for Adding new Region
	$scope.insertRegion = function(){
		$scope.isRegionExist=false;
		if($scope.regionName === undefined){
			$scope.errorStatus = true;
		}else{

			$http({
				method: "POST",
				url: url + "/customerOperation/addRegion",
				params: {region:$scope.regionName, selectedCountryId:$scope.selectedCountryId, selectedCurrencyId:$scope.selectedCurrencyId,selectedTimeZone:$scope.selectedTimeZone}
			}).success(function (data) {
				if(angular.equals(data,'regionSuccessfullyadded')){

					$scope.isSuccess = true;
					$scope.success="Record added. Redirecting to main page."

						window.setTimeout(function() {
							$window.location.href="regionManagement";
						}, 3000);

				}
				if(angular.equals(data,'regionalreadyexist')){
					$scope.isRegionExist=true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.isRegionExist = false;
								});
							}, 3000);
				}
				if(angular.equals(data,'youCanNotAccessRegionList')){
					$window.location.href="login";
				}
				$scope.response = data;
			}).error(function (data,status){

			});	

		}


	}

	// Angular method for Get Data Plan details for Edit 
	$scope.editRegion = function(regionId){

		$http({
			method: "GET",
			url: url + "/customerOperation/editRegion",
			params:{regionId : regionId},
		}).success(function(data) {
			if(angular.equals(data,'NoSuchRegionFound')){
				$window.location.href="editRegionForm";


			}

			$window.location.href="editRegionForm";

		}).error(function (data,status){

		});
	}


	$scope.getEditRegion = function(){
		//First make ajax call and get that sessioned Region entity.

		$http({
			method: "GET",
			url: url+ "/customerOperation/getEditRegion",
		}).success(function(data){
			if(data[0].addEditFlag == "add"){
				$scope.addRegionDiv = true;
				$scope.countryList = data[1];
				$scope.currencyList = data[2];
				$scope.timeZoneList = JSON.parse(data[3]);
			}else{
				$scope.editRegionDiv = true;
				$scope.regionDivList = false;
				var region = data[0];
				$scope.regionName = region.regionName;
				$scope.regionId = region.regionId;
				$scope.savedCountryId = region.savedCountryId;
				$scope.savedCurrencyId = region.savedCurrencyId;
				$scope.savedTimeZone = region.savedTimeZone;
				$scope.countryList = data[1];
				$scope.currencyList = data[2];
				$scope.timeZoneList = JSON.parse(data[3]);
			}
		}).error(function(data,status){

		});
	}

	// Angular method for Updating Region
	$scope.updateRegion = function(regionId){
		
		$scope.isRegionExist=false;
		
		if($scope.regionName === undefined){
			$scope.errorStatus = true;
			setTimeout(function () 
					{
				$scope.$apply(function()
						{
					$scope.errorStatus = false;
						});
					}, 3000);
		}else{
			$http({
				method: "POST",
				url: url + "/customerOperation/updateRegion",
				params: {regionName:$scope.regionName,regionId: regionId,savedCountryId:$scope.savedCountryId,savedCurrencyId:$scope.savedCurrencyId,savedTimeZone:$scope.savedTimeZone}
			}).success(function (data) {
				if(angular.equals(data,'regionUpdatedSuccessfully')){

					$scope.isSuccess = true;
					$scope.success="Record updated. Redirecting to main page."

						window.setTimeout(function() {
							$window.location.href="regionManagement";
						}, 3000);

				}
				if(angular.equals(data,'regionalreadyexist')){

					$scope.isRegionExist=true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.isRegionExist = false;
								});
							}, 3000);

				}
				
			}).error(function (data,status){

			});	

		}
	}

	$scope.searchRegion = function(){

		var regionName = $scope.regionName;
		$http({
			method: "POST",
			url: url + "/customerOperation/searchRegion",
			params: {regionName : regionName},

		}).success(function (data) {
			if(angular.equals(data,'nosuchregionfound')){
				$scope.searchCompleted = false;
				$scope.noRegionFoundDiv = true;
				$scope.regionListDiv = false;

			}else{
				$scope.noRegionFoundDiv = false;
				$scope.regionListDiv = true;
				$scope.regionList = data;

				$scope.limit = 10;
				$scope.begin = 0;
				// For pagination
				var keys = Object.keys($scope.regionList);
				var length = keys.length;
				// Max list calculations
				$scope.totalPage = Math.ceil(length/$scope.limit); //Here 10 is default records to be appeared
			}
		}).error(function (data,status){

		});	
	}

	$scope.initSiteData = function(){
		// Initialize variables
		$scope.searchError = false;
		var x = 10;
		$scope.recordSize = x.toString()+"";
		$http({
			method:"POST",
			url:url+"/customerOperation/customerSiteInitialData"
		}).success(function(data){
			$scope.siteListDiv = true;
			$scope.siteList = data[0];
			$scope.isSuperCustomer = data[1];
			$scope.limit = 10;
			$scope.begin = 0;
			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.siteList.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show

		}).error(function(data,status){

		});
	}

	$scope.getDcDetailsforCustomer = function(){
		var defaultSize = 10;
		$scope.recordSize= defaultSize.toString();
		$http({
			method:"POST",
			url:url+"/customerOperation/getCustomerDataCollector"
		}).success(function(data){

			if(angular.equals(data,'nodatacollectorfound')){
				$scope.noUserFoundDiv = true;
			}
			$scope.dataCollectorListDiv = true;
			$scope.dataCollector = data[0];
			$scope.isSuperCustomer = data[1];

			$scope.limit = 10;
			$scope.begin = 0;
			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.dataCollector.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show
		}).error(function(data,status){

		});
	}

	// Angular method for Search Site with different criteria
	$scope.searchSite = function(){

		var siteSearchName = $scope.siteInput;
		var siteSearchRegon = $scope.regionNameSearch;

		$http({
			method: "POST",
			url: url + "/customerOperation/searchSiteByNameOrRegionName",
			params: {siteSearchCriteria:siteSearchName,siteSearchRegon:siteSearchRegon},
		}).success(function (data) {
			if(angular.equals(data,'nosuchsitefound')){
				$scope.noSiteFoundDiv = true;
				$scope.siteListDiv = false;
			}else{
				$scope.noSiteFoundDiv = false;
				$scope.siteListDiv = true;
				$scope.siteList = data;

				$scope.limit = 10;
				$scope.begin = 0;
				// For pagination
				var keys = Object.keys($scope.siteList);
				var length = keys.length;

				// Max list calculations
				$scope.totalPage = Math.ceil(length/$scope.limit); //Here 10 is default records to be appeared
			}
		}).error(function (data,status){

		});	

	}

	$scope.editDataCollectorForCustomer = function(datacollectorId){

		$http({
			method: "GET",
			url: url + "/customerOperation/editDataCollectorForCustomer",
			params:{datacollectorId : datacollectorId},
		}).success(function(data) {
			if(angular.equals(data,'NoSuchDataCollectorFound')){
				// code for displaying error
			}else{
				$window.location.href="editDataCollectorFormForCustomer";
			}

		}).error(function (data,status){

		});


	}

	$scope.getEditdatacollectorByCustomer = function(){

		// Some UI stuff before initializing page
		$scope.toggle = true;
		$scope.nullSite = false;
		$scope.nullSiteToggle = true;
		$scope.isSuperCustomer = false;
		$scope.$watch('toggle', function(){
			$scope.toggleText = $scope.toggle ? 'Change DC Serial Number!' : 'Update DC Serial Number';
			if(angular.equals($scope.toggleText,"Change DC Serial Number!") && !angular.isUndefined($scope.datacollectorId)){
				$http({
					method: "POST",
					url: url+ "/updateDcSerialNumber",
					params:{
						id:$scope.datacollectorId,
						serialNumber:$scope.dcSerialNumber,
					}
				}).success(function(data){
					if(angular.equals(data,'error')){
						// code for displaying error
					}
					else if(angular.equals('exist',data)){
						$scope.isError = true;
						$scope.error="Serial number "+ $scope.dcSerialNumber +" already exists !";
						$scope.dcSerialNumber = $scope.originalDcSerialNumber
						setTimeout(function ()
								{
							$scope.$apply(function()
									{
								$scope.isError = false;
									});
								}, 3000);

					}else{
						$scope.customerDivList = false;
						var datacollector = data[0];
						$scope.datacollectorId = datacollector.datacollectorId;
						$scope.dcSerialNumber = datacollector.dcSerialNumber;
						$scope.dcIp = datacollector.dcIp;
						$scope.totalEndpoints = datacollector.totalEndpoints;
						$scope.iscommissioned = datacollector.iscommissioned;
						$scope.meterReadingInterval = datacollector.meterReadingInterval;
						$scope.networkStatusInterval = datacollector.networkStatusInterval;

						$scope.site = datacollector.site;
						$scope.latitude = datacollector.latitude;
						$scope.longitude = datacollector.longitude;
						$scope.simcardNo = datacollector.simcardNo;

						if(angular.equals($scope.simcardNo,"-")){
							$scope.simcardNo="";
							$scope.nullSite = true;
						}else{
							$scope.nullSite = false;
						}
						$scope.isSuccess = true;
						$scope.success = "Changes applied !";

						setTimeout(function ()
								{
							$scope.$apply(function()
									{
								$scope.isSuccess = false;
									});
								}, 3000);
					}
				}).error(function(data,status){

				});
			}
		})
		$scope.$watch('nullSiteToggle', function(){
			$scope.nullSiteToggleText = $scope.nullSiteToggle ? 'Change DC SIM Number!' : 'Update DC SIM Number';

			if(angular.equals("Change DC SIM Number!",$scope.nullSiteToggleText) && !angular.isUndefined($scope.datacollectorId)){

				if(!angular.isUndefined($scope.dcSimNo) &&  !angular.equals($scope.dcSimNo.toString().trim(),"")){

					$http({
						method: "POST",
						url: url+ "/updateDcSimNumber",
						params:{
							id:$scope.datacollectorId,
							simNo:$scope.dcSimNo,
						}
					}).success(function(data){
						if(angular.equals(data,'error')){

						}else{
							$scope.customerDivList = false;
							var datacollector = data[0];
							$scope.datacollectorId = datacollector.datacollectorId;
							$scope.dcSerialNumber = datacollector.dcSerialNumber;
							$scope.dcIp = datacollector.dcIp;
							$scope.totalEndpoints = datacollector.totalEndpoints;
							$scope.iscommissioned = datacollector.iscommissioned;
							$scope.meterReadingInterval = datacollector.meterReadingInterval;
							$scope.networkStatusInterval = datacollector.networkStatusInterval;

							$scope.site = datacollector.site;
							$scope.latitude = datacollector.latitude;
							$scope.longitude = datacollector.longitude;
							$scope.simcardNo = datacollector.simcardNo;

							if(angular.equals($scope.simcardNo,"-")){
								$scope.simcardNo="";
								$scope.nullSite = true;
							}else{
								$scope.nullSite = false;
							}

							$scope.isSuccess = true;
							$scope.success = "Changes applied !";

							setTimeout(function ()
									{
								$scope.$apply(function()
										{
									$scope.isSuccess = false;
										});
									}, 3000);

						}
					}).error(function(data,status){

					});
				}else{
					$scope.isErrorDiv = true;
					$scope.errorMsg = "Provide Proper SIM Number !"
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.isErrorDiv = false;
								});
							}, 3000);
				}
			}

		})

		//First make ajax call and get that sessioned user entity.

		$http({
			method: "POST",
			url: url+ "/customerOperation/getEditdatacollectorByCustomer",
		}).success(function(data){
			if(angular.equals(data,'nodatacollectorFound')){
				// code for displaying error
			}else{
				//	$scope.customerDivList = true;
				$scope.customerDivList = false;
				var datacollector = data[0];
				$scope.datacollectorId = datacollector.datacollectorId;
				$scope.dcSerialNumber = datacollector.dcSerialNumber;
				$scope.dcIp = datacollector.dcIp;
				$scope.totalEndpoints = datacollector.totalEndpoints;
				$scope.iscommissioned = datacollector.iscommissioned;
				$scope.meterReadingInterval = datacollector.meterReadingInterval;
				$scope.networkStatusInterval = datacollector.networkStatusInterval;

				$scope.originalDcSerialNumber = $scope.dcSerialNumber; // Needed to change to original if changed already exist !
				$scope.site = datacollector.site;
				$scope.latitude = datacollector.latitude;
				$scope.longitude = datacollector.longitude;
				/*$scope.simcardNo = datacollector.simcardNo;*/
				$scope.dcSimNo = datacollector.simcardNo;

				if(datacollector.isSuperCustomer === 'true'){
					$scope.isSuperCustomer = true;
				}else{
					$scope.isSuperCustomer = false;
				}
				if(angular.equals($scope.dcSimNo,"-")){
					$scope.dcSimNo="";
					$scope.nullSite = true;
				}else{
					$scope.nullSite = false;
				}

			}
		}).error(function(data,status){

		});
	}

	$scope.updateDataCollectorByCustomer = function(datacollectorId){

		if($scope.dcSerialNumber == ""){
			$scope.isErrorDiv = true;
			$scope.errorMsg = "Provide Proper Serial Number !"
			setTimeout(function () 
					{
				$scope.$apply(function()
						{
					$scope.isErrorDiv = false;
						});
					}, 3000);
		}else{
			$http({
				method: "GET",
				url: url + "/customerOperation/updateDataCollectorByCustomer",
				params: {
					latitude:$scope.latitude,
					longitude:$scope.longitude,
					datacollectorId: datacollectorId,
					dcSerialNumber:$scope.dcSerialNumber
				}
			}).success(function (data) {
				if(angular.equals(data,'dataCollectorUpdatedSuccessfully')){

					$scope.isSuccess = true;
					$scope.success="Record updated. Redirecting to main page."

						window.setTimeout(function() {
							$window.location.href="dcManagement";
						}, 3000);

				}
			}).error(function (data,status){

			});
		}

	}

	$scope.searchDataCollectorByCustomer = function(){

		var dcIp = $scope.dcIp;
		var dcSerialNumber = $scope.dcSerialNumber;

		$http({
			method: "POST",
			url: url + "/customerOperation/searchDataCollectorByCustomer",
			params: {
				dcIp : dcIp, 
				dcSerialNumber : dcSerialNumber},

		}).success(function (data) {
			if(angular.equals(data,'nosuchuserfound')){
				$scope.noDataCollectorFoundDiv = true;
				$scope.dataCollectorListDiv = false;

			}else{
				$scope.noDataCollectorFoundDiv = false;
				$scope.dataCollectorListDiv = true;
				$scope.dataCollector = data;
				$scope.limit = 10;
				$scope.begin = 0;
				// For pagination
				var keys = Object.keys($scope.dataCollector);
				var length = keys.length;

				// Max list calculations
				$scope.totalPage = Math.ceil(length/$scope.limit); //Here 10 is default records to be appeared
			}
		}).error(function (data,status){

		});	
	}


//	Angular method for confirm Delete User popup
	$scope.removeSite = function(id){
		
		if(!$scope.isSuperCustomer){
			
			swal.withForm({
				title: "Authentication required !",
				text: "Please enter your Super Customer credentials to delete selected consumer.",
				showCancelButton: true,
				closeOnConfirm: true,
								
				formFields: [
				   { id: 'Username', placeholder:'Username', required:true },
				   { id: 'Password',type:'password' , placeholder:'Password', required:true }
				]
			
			},function(isConfirm){
				
				//alert("Username : " + Username.value + "\n Password : " + Password.value);
				
				var username = Username.value;
				var password = Password.value;
				
				$http({
					method: "POST",
					url: url + "/authenticateSuperCustomer",
					params:{username : username, password : password},
				}).success(function(data) {
					
					if(angular.equals(data,'success')){
						$http({
							method: "POST",
							url: url + "/customerOperation/deleteSite",
							params:{siteId : id},
						}).success(function(data) {
							$window.location.reload(); // reload to reflect changes on UI side
						});
						
					}
					if(angular.equals(data,'error')){
						
						setTimeout(function () 
								{
							$scope.$apply(function()
									{
						swal("Authentication Failed !", "Please enter valid credentials !", "warning")
									});
						}, 1000);
					}

				});
			});
		
			
		}else{
			
			swal({
				   title: 'Confirm',
				   text: 'Are you sure you want to delete this Site with ID : '+ id + '?',
				   type: 'warning',
				   showCancelButton: true,
				   confirmButtonText: 'Yes',
				   cancelButtonText: 'Cancel'
				}, function(){
				$http({
					method: "POST",
					url: url + "/customerOperation/deleteSite",
					params:{siteId : id},
				}).success(function(data) {
					$window.location.reload(); // reload to reflect changes on UI side
				});
			});
		}

	}

	// Angular Generic method for pagination in all pages
	$scope.changedValue=function(recordSize, list){
		$scope.limit = recordSize;
		$scope.begin = 0;
		// Change Max Limit
		var keys = Object.keys(list);
		var length = keys.length;

		// Max list calculations
		$scope.totalPage = Math.ceil(length/$scope.limit);
	}

	$scope.getNumber = function(number){
		return new Array(number);
	}

	// Detecting page change event
	$scope.fetchRecord = function(number){
		var currentRecordSize = $scope.recordSize; // current record size
		$scope.begin = parseInt((number-1)*currentRecordSize);
		$scope.limit = parseInt($scope.begin) + parseInt(currentRecordSize);
	}

	// Editing Site

	$scope.editSite = function(site){
		$http({
			method:"POST",
			url:url+"/customerOperation/genereateCustomerData",
			params:{siteId:site},
		}).success(function(data){
			if(angular.equals('datafetched',data))
				window.location.href="editSiteRedirect";
			/*else
				error status code*/
		}).error(function(data,status){

		});
	}

	$scope.editSiteData = function(){
		$scope.isUndefined=false;
		$http({
			method:"GET",
			url:url+"/customerOperation/setGenerateCustomerEditData",
		}).success(function(data){
			$scope.siteId= parseInt(data.siteId);
			$scope.siteName= data.siteName;
			//$scope.status= data.status;
			if(data.status == true)	
				$scope.status = "Active";
			else
				$scope.status = "Inactive";
				
			$scope.regionNames= JSON.parse(data.customerRegion);
			$scope.boundryCollector= JSON.parse(data.dcList);
			$scope.installerName= JSON.parse(data.installerName);
			if(!angular.equals({},data.assignedList))
				$scope.assignCollector= JSON.parse(data.assignCollector),
				$scope.boundryDC=[];	
			$scope.selectedInstallerName = JSON.parse(data.selectedInstallerName);
			$scope.selectedRegion = JSON.parse(data.selectedRegion);
			$scope.selectedInstaller = JSON.parse(data.selectedInstallerName);
			$scope.isInstaller = JSON.parse(data.isInstallerActive);
			var selectedBDC = JSON.parse(data.selectedBdc);
			for(var i=0;i<selectedBDC.length;i++){
				$scope.boundryDC.push(selectedBDC[i]);
			}

		}).error(function(data,status){

		});
	}

	$scope.refreshSitePage = function(){
		window.location.href="siteManagement";
	}

	$scope.refPage = function(){
		window.location.href="dcManagement";
	}

	$scope.editCurrentSite = function(siteId){

		if(angular.isUndefined($scope.selectedInstaller) || angular.isUndefined($scope.selectedRegion)){
			$scope.isUndefined=true;
		}else{
			$http({
				method:"POST",
				url:url+"/customerOperation/editCurrentSite",
				params:{"siteId":$scope.siteId,"siteName":$scope.siteName,
					"assignDC":$scope.assignDC,"region":$scope.selectedRegion,
					"boundryDC":$scope.boundryDC,"installer":$scope.selectedInstaller, status : $scope.status},
			}).success(function(data){

				$scope.isSuccess = true;
				$scope.success="Record updated. Redirecting to main page."

					window.setTimeout(function() {
						$window.location.href="siteManagement";
					}, 3000);

			}).error(function(data,status){

			});
		}

	}

	//getting list of Region
	$scope.initDUMeterPage = function(){

		$http({
			method: "POST",
			url: url + "/customerOperation/getDUMeterData",
		}).success(function (data) {
			$scope.DUMeterListDiv = true;
			$scope.noDUMeterFoundDiv = false;
			//$scope.addregion = true;
			$scope.DUMeterList = data;
			$scope.limit = 10;
			$scope.begin = 0;
			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.DUMeterList.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show

			if(angular.equals(data,'youCanNotAccessDUMeterList')){
				$window.location.href="customerDashboard";
			}

		}).error(function (data,status){

		});	

	}

	// Angular method for Adding new DUMeter
	$scope.insertDUMeter = function(){

		$scope.enterSerialNumber = false;
		$scope.isDUMeterExist=false;
		$scope.mustSelectDate=false;
		$scope.startBilling = false;
		$scope.endBilling = false;
		
		var DUMeterSerialNumber = $scope.DUMeterSerialNumber;
		var DUMeterReading = $scope.DUMeterReading;
		//var DUMeterReadingDate = $scope.DUMeterReadingDate;
		var DUMeterReadingDate = $filter('date')($scope.DUMeterReadingDate,"dd/MM/yyyy");
		var startBillingDate = $filter('date')($scope.startBillingDate,"dd/MM/yyyy");
		var endBillingDate = $filter('date')($scope.endBillingDate,"dd/MM/yyyy");

		if(startBillingDate == null || startBillingDate == ""){

			$scope.startBilling=true;
			setTimeout(function () 
					{
				$scope.$apply(function()
						{
					$scope.startBilling = false;
						});
					}, 3000);

		} else if(endBillingDate == null || endBillingDate == ""){

			$scope.endBilling=true;
			setTimeout(function () 
					{
				$scope.$apply(function()
						{
					$scope.endBilling = false;
						});
					}, 3000);

		} else {

			$http({
				method: "POST",
				url: url + "/customerOperation/addDUMeter",
				params: {DUMeterSerialNumber : DUMeterSerialNumber, DUMeterReading : DUMeterReading, 
						DUMeterReadingDate : DUMeterReadingDate, startBillingDate : startBillingDate, 
						endBillingDate : endBillingDate}
			}).success(function (data) {
				if(angular.equals(data,'dumeterSuccessfullyadded')){

					$scope.isSuccess = true;
					$scope.success="Record added. Redirecting to main page."

						window.setTimeout(function() {
							$window.location.href="districtUtilityMeter";
						}, 3000);

				}
				if(angular.equals(data,'dumeteralreadyexist')){
					$scope.isDUMeterExist=true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.isDUMeterExist = false;
								});
							}, 3000);
				}
				if(angular.equals(data,'nullserialnumber')){

					$scope.enterSerialNumber = true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.enterSerialNumber = false;
								});
							}, 3000);

				}
				if(angular.equals(data,'youCanNotAccessDUMeterList')){
					$window.location.href="districtUtilityMeter";
				}
				$scope.response = data;
			}).error(function (data,status){

			});	
		}
	}

	//**************
	$scope.editDUMeter = function(districtMeterTransactionId){

		$http({
			method: "GET",
			url: url + "/customerOperation/editDUMeter",
			params:{districtMeterTransactionId : districtMeterTransactionId},
		}).success(function(data) {

			if(angular.equals(data,"NoSuchDUMeterFound")){
				$window.location.href="editDUMeterForm";
			}

			$window.location.href="editDUMeterForm";

		}).error(function (data,status){

		});
	}


	$scope.getEditDUMeter = function(){
		//First make ajax call and get that sessioned Region entity.

		$scope.isDUMeterExist=false;
		$scope.enterSerialNumber = false;

		$http({
			method: "GET",
			url: url+ "/customerOperation/getEditDUMeter",
		}).success(function(data){
			if(angular.equals(data,'noDUMeterFound')){
				$scope.addDUMeterDiv = true;
			}else{
				$scope.editDUMeterDiv = true;
				$scope.DUMeterListDiv = false;
				var DUMeter = data[0];
				$scope.districtMeterTransactionId = DUMeter.districtMeterTransactionId;
				$scope.districtUtilityMeterSerialNumber = DUMeter.districtUtilityMeterSerialNumber;
				$scope.currentReading = DUMeter.currentReading;
				$scope.startBillingDate = DUMeter.startBillingDate;
				$scope.endBillingDate = DUMeter.endBillingDate;

			}
		}).error(function(data,status){

		});
	}

	// Angular method for Updating Region
	$scope.updateDUMeter = function(districtMeterTransactionId){

		var districtMeterTransactionId = $scope.districtMeterTransactionId;
		var DUMeterSerialNumber = $scope.districtUtilityMeterSerialNumber;
		var DUMeterReading = $scope.currentReading;
		var DUMeterReadingDate = $filter('date')($scope.readingDate,"dd/MM/yyyy");
		var startBillingDate = $filter('date')($scope.startBillingDate,"dd/MM/yyyy");
		var endBillingDate = $filter('date')($scope.endBillingDate,"dd/MM/yyyy");

		$http({
			method: "POST",
			url: url + "/customerOperation/updateDUMeter",
			params: {districtMeterTransactionId : districtMeterTransactionId, DUMeterSerialNumber : DUMeterSerialNumber,
					DUMeterReading : DUMeterReading, DUMeterReadingDate : DUMeterReadingDate, 
					startBillingDate : startBillingDate, endBillingDate : endBillingDate}
		}).success(function (data) {
			if(angular.equals(data,'DUMeterUpdatedSuccessfully')){

				$scope.isSuccess = true;
				$scope.success="Record updated. Redirecting to main page."

					window.setTimeout(function() {
						$window.location.href="districtUtilityMeter";
					}, 3000);

			}
			if(angular.equals(data,'dumeteralreadyexist')){
				$scope.isDUMeterExist=true;
				setTimeout(function () 
						{
					$scope.$apply(function()
							{
						$scope.isDUMeterExist = false;
							});
						}, 3000);
			}
			if(angular.equals(data,'nullserialnumber')){

				$scope.enterSerialNumber = true;
				setTimeout(function () 
						{
					$scope.$apply(function()
							{
						$scope.enterSerialNumber = false;
							});
						}, 3000);

			}
		}).error(function (data,status){

		});	

	}

	$scope.searchDUMeter = function(){

		var DUMeterSerialNumber = $scope.DUMeterSerialNumber;

		$http({
			method: "POST",
			url: url + "/customerOperation/searchDUMeter",
			params: {DUMeterSerialNumber:DUMeterSerialNumber},

		}).success(function (data) {

			if(angular.equals(data,'emptryList')){
				$scope.searchCompleted = false;
				$scope.noDUMeterFoundDiv = true;
				$scope.DUMeterListDiv = false;

			}else{
				$scope.noDUMeterFoundDiv = false;
				$scope.DUMeterListDiv = true;
				$scope.DUMeterList = data;

				$scope.limit = 10;
				$scope.begin = 0;
				// For pagination
				var keys = Object.keys($scope.DUMeterList);
				var length = keys.length;

				// Max list calculations
				$scope.totalPage = Math.ceil(length/$scope.limit); //Here 10 is default records to be appeared
			}
		}).error(function (data,status){

		});	
	}

	//Angular method for confirm Delete Meter popup
	$scope.removeDUMeter = function(districtUtilityMeterId){
		
		swal({
			   title: 'Confirm',
			   text: 'Want to delete District Meter with ID : '+ districtUtilityMeterId+' ?',
			   type: 'warning',
			   showCancelButton: true,
			   confirmButtonText: 'Yes',
			   cancelButtonText: 'Cancel'
			}, function(){
				
			$http({
				method: "POST",
				url: url + "/customerOperation/deleteDUMeter",
				params:{districtUtilityMeterId : districtUtilityMeterId},
			}).success(function(data) {

				if(angular.equals(data,'meterismapped')){
					$scope.isMapped=true;

					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.isMapped = false;
								});
							}, 3000);
				}
				if(angular.equals(data,'Meter successfully deleted')){

					$scope.success="District utility meter successfully deleted... Reloading list...";
					$scope.isSuccess=true;

					window.setTimeout(function() {
						$window.location.href="districtUtilityMeter";
					}, 3000);

				}

				// reload to reflect changes on UI side
			});
		});

	}


	$scope.getAddConsumerData = function(){

		$scope.isUserNameExist=true;
		$scope.isCodeExist=true;

		$scope.activeStatus = "Active";

		$http({
			method: "POST",
			url: url + "/customerOperation/tariffPlanData",
		}).success(function (data) {

			$scope.isCodeExist=true;
			$scope.isUserNameExist=true;
			$scope.tariffPlanList = data[0];


		}).error(function (data,status){

		});	
	}


	// Tariff Management

	$scope.isTariffAdded = false;
	$scope.isTariffEdit = false;

	$scope.initAddTeriffVariable = function(){
		$scope.isTariffAdded = true;
		$scope.isTariffEdit = false;
		$scope.error="";
	}

	$scope.tariffAdded = function(model,row){
		if(angular.equals(row,2) && model.check){
			$scope.trans2.start =  parseInt($scope.trans1.end)+1;
			$scope.trans2.startValue = parseInt($scope.trans1.end)+1;
		}else if(angular.equals(row,3) && model.check){
			$scope.trans3.start =  parseInt($scope.trans2.end)+1;
			$scope.trans3.startValue = parseInt($scope.trans2.end)+1;
		}else if(angular.equals(row,4) && model.check){
			$scope.trans4.start =  parseInt($scope.trans3.end)+1;
			$scope.trans4.startValue = parseInt($scope.trans3.end)+1;
		}else if(angular.equals(row,5) && model.check){
			$scope.trans5.start =  parseInt($scope.trans4.end)+1;
			$scope.trans5.startValue = parseInt($scope.trans4.end)+1;
		}else if(angular.equals(row,6) && model.check){
			$scope.trans6.start =  parseInt($scope.trans5.end)+1;
			$scope.trans6.startValue = parseInt($scope.trans5.end)+1;
		}else if(angular.equals(row,7) && model.check){
			$scope.trans7.start =  parseInt($scope.trans6.end)+1;
			$scope.trans7.startValue = parseInt($scope.trans6.end)+1;
		}else if(angular.equals(row,8) && model.check){
			$scope.trans8.start =  parseInt($scope.trans7.end)+1;
			$scope.trans8.startValue = parseInt($scope.trans7.end)+1;
		}else if(angular.equals(row,9) && model.check){
			$scope.trans9.start =  parseInt($scope.trans8.end)+1;
			$scope.trans9.startValue = parseInt($scope.trans8.end)+1;
		}else if(angular.equals(row,10) && model.check){
			$scope.trans10.start =  parseInt($scope.trans9.end)+1;
			$scope.trans10.startValue = parseInt($scope.trans9.end)+1;
		}
	}

	$scope.tariffData=[];

	$scope.addTariff = function(){

		$scope.isError = false;

		if(angular.isUndefined($scope.tariffName) || $scope.tariffName === ""){
			$scope.isError = true;
			$scope.error="Provide proper Tariff Name.";

		}else{

			if(!angular.isUndefined($scope.trans1)){
				if($scope.trans1.check){
					// First row is selected.So check for basic validation
					if(checkForRowUndefinedData($scope.trans1)){
						// Row 1's basic validation passed .. Let add it into array
						pushElementToTariffData($scope.trans1);
						// If row 2 is seleced or not ?
						if(!angular.isUndefined($scope.trans2) && $scope.trans2.check){
							//Second row basic validation ?
							if(checkForRowUndefinedData($scope.trans2)){
								// Row 2's data added
								pushElementToTariffData($scope.trans2);
								if(!angular.isUndefined($scope.trans3) && $scope.trans3.check){
									if(checkForRowUndefinedData($scope.trans3)){
										pushElementToTariffData($scope.trans3);
										if(!angular.isUndefined($scope.trans4) && $scope.trans4.check){
											if(checkForRowUndefinedData($scope.trans4)){
												pushElementToTariffData($scope.trans4);
												if(!angular.isUndefined($scope.trans5) && $scope.trans5.check){
													if(checkForRowUndefinedData($scope.trans5)){
														pushElementToTariffData($scope.trans5);
														if(!angular.isUndefined($scope.trans6) && $scope.trans6.check){
															if(checkForRowUndefinedData($scope.trans6)){
																pushElementToTariffData($scope.trans6);
																if(!angular.isUndefined($scope.trans7) && $scope.trans7.check){
																	if(checkForRowUndefinedData($scope.trans7) && $scope.trans7.check){
																		pushElementToTariffData($scope.trans7);
																		if(!angular.isUndefined($scope.trans8)){
																			if(checkForRowUndefinedData($scope.trans8) && $scope.trans8.check){
																				pushElementToTariffData($scope.trans8);
																				if(!angular.isUndefined($scope.trans9) && $scope.trans9.check){
																					if(checkForRowUndefinedData($scope.trans9)){
																						pushElementToTariffData($scope.trans9);
																						if(!angular.isUndefined($scope.trans10) && $scope.trans10.check){
																							if(checkForRowUndefinedData($scope.trans10)){
																								pushElementToTariffData($scope.trans10);
																							}else{
																								$scope.isError = true;
																							}
																						}
																					}else{
																						$scope.isError = true;
																					}
																				}
																			}else{
																				$scope.isError = true;
																			}
																		}
																	}else{
																		$scope.isError = true;
																	}
																}
															}else{
																$scope.isError = true;
															}
														}

													}else{
														$scope.isError = true;
													}
												}	

											}else{
												$scope.isError = true;
											}
										}
									}else{
										$scope.isError = true;
									}
								}
							}else{
								$scope.isError = true;
							}
						}
					}else{
						$scope.isError = true;
					}

				}else{
					$scope.isError = true;
				}
			}else{
				$scope.isError = true;
			}

		}

		if($scope.isError){

			if(angular.isUndefined($scope.tariffName)|| $scope.tariffName === ""){
				$scope.error="Provide proper Tariff Name.";
			}else{
				$scope.error="Not properly filled checked data.";
			}
			$scope.isNotProperlyFilled = true;
			$scope.tariffData=[];

			setTimeout(function () 
					{
				$scope.$apply(function()
						{
					$scope.isNotProperlyFilled = false;
					$scope.isError = false;
						});
					}, 3000);
		}else{
			// Everything looks fine for data. Tring for backend call

			if($scope.isTariffAdded){
				$scope.isTariffAdded = false;
				var response = $http.post('tarrifManagementOperation/addNewTariff', {tariffArray:$scope.tariffData,tariffName:$scope.tariffName,tariffId:$scope.tariffId});
				response.success(function(data, status, headers, config) {
					if(angular.equals(data,"nameexist")){
						$scope.isTariffAdded = true;
						$scope.error="Tariff name already exists.";
						$scope.isNotProperlyFilled = true;
						setTimeout(function () 
								{
							$scope.$apply(function()
									{
								$scope.error="";
								$scope.isNotProperlyFilled = false;
								$scope.isError = false;
									});
								}, 3000);
					}else if(angular.equals(data,"added")){
						window.location.href="tariffManagement";
					}else{
						window.location.href="tariffManagement";
					}

				});
			}else if($scope.isTariffEdit){
				$scope.isTariffEdit=false;
				var tarrifId = $scope.tariffId;
				var response = $http.post('tarrifManagementOperation/editCurrentTariff', {tariffArray:$scope.tariffData,tariffName:$scope.tariffName,tariffId:tarrifId}); 
				response.success(function(data, status, headers, config) {

					if(angular.equals(data,"nameexist")){
						$scope.error="Name already exists.Try with different one.";
						$scope.isTariffEdit=true;
						$scope.isNotProperlyFilled = true;
						setTimeout(function () 
								{
							$scope.$apply(function()
									{
								$scope.error="";
								$scope.isNotProperlyFilled = false;
								$scope.isError = false;
									});
								}, 3000);
					}else{
						window.location.href="tariffManagement";
					}

				});
				response.error(function(data, status, headers, config) {
				});

			}

			$scope.tariffData=[]; // Again setting to its default value
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
				'start':model.start,
				'end':model.end,
				'cost':model.cost,
			});
		}


	}

	var checkForRowUndefinedData = function(model) {

		if(!angular.isUndefined(model.start)){
			if(model.check && !angular.isUndefined(model.end) &&
					!angular.isUndefined(model.cost) && parseInt(model.start) < parseInt(model.end)){
				return true;
			}
			return false;
		}else{
			if(model.check && !angular.isUndefined(model.end) &&
					!angular.isUndefined(model.cost) && parseInt(model.startValue) < parseInt(model.end)){
				return true;
			}
			return false;
		}

	};

	$scope.getTariffData = function(){

		$scope.noTariffFoundDiv = false;

		$scope.isError = false;
		$scope.isSuccess = false;

		$scope.success="";
		$scope.error="";

		$http({
			method:"POST",
			url:url+"/customerOperation/tarrifManagementOperation/getTariffData",
		}).success(function(data){
			$scope.tariffList = data[0];
			$scope.isSuperCustomer = data[1];
			$scope.limit = 10;
			$scope.begin = 0;
			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.tariffList.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show
		}).error(function(data,status){

		});

	}

	$scope.searchTarrifByName = function(){

		if(angular.isUndefined($scope.tarrifSearch)|| $scope.tarrifSearch === ""){
			$scope.error="Provide proper search criteria.";
			$scope.isError = true;

			setTimeout(function () 
					{
				$scope.$apply(function()
						{
					$scope.isError = false;
					$scope.error=""
						});
					}, 3000);

		}else{
			$http({
				method:"POST",
				url:url+"/customerOperation/tarrifManagementOperation/tarrifSearch",
				params:{name:$scope.tarrifSearch},
			}).success(function(data){

				if(angular.equals(data,"nodata")){
					$scope.noTariffFoundDiv=true;
				}else{
					$scope.noTariffFoundDiv=false;
					$scope.tariffList = data;
				}


			}).error(function(data,status){

			});
		}

	}

	$scope.refreshPage = function(){
		window.location.reload();
	}

	$scope.removeTariffRow = function(x){

		if(!$scope.isSuperCustomer){
			
			swal.withForm({
				title: "Authentication required !",
				text: "Please enter your Super Customer credentials to delete selected consumer.",
				showCancelButton: true,
				closeOnConfirm: true,
								
				formFields: [
				   { id: 'Username', placeholder:'Username', required:true },
				   { id: 'Password',type:'password' , placeholder:'Password', required:true }
				]
			
			},function(isConfirm){
				
				//alert("Username : " + Username.value + "\n Password : " + Password.value);
				
				var username = Username.value;
				var password = Password.value;
				
				$http({
					method: "POST",
					url: url + "/authenticateSuperCustomer",
					params:{username : username, password : password},
				}).success(function(data){
					
					if(angular.equals(data,'success')){
						$http({
							method:"POST",
							url:url+"/customerOperation/tarrifManagementOperation/tariffDelete",
							params:{id:x.id,name:x.name},
						}).success(function(data){
							if(angular.equals(data,'deleted')){
								$scope.success="Tariff successfully deleted... Reloading list...";
								$scope.isSuccess=true;

								window.setTimeout(function() {
									window.location.href = "tariffManagement";
								}, 3000);

							}else{
								$scope.error="Tariff is mapped. Can't delete";
								$scope.isError=true;

								setTimeout(function () 
										{
									$scope.$apply(function()
											{
										$scope.isError = false;
										$scope.error="";
											});
										}, 3000);

							}

							$scope.tariffList = data;
						}).error(function(data,status){

						});
					}
					
					if(angular.equals(data,'error')){
						
						setTimeout(function () 
								{
							$scope.$apply(function()
									{
						swal("Authentication Failed !", "Please enter valid credentials !", "warning")
									});
						}, 1000);
					}
					
				});
			
			});
			
		
			
		}else{
			
			swal({
				   title: 'Confirm',
				   text: 'Want to delete tariff with name :'+ x.name +' ?',
				   type: 'warning',
				   showCancelButton: true,
				   confirmButtonText: 'Yes',
				   cancelButtonText: 'Cancel'
				}, function(){
			
				$http({
					method:"POST",
					url:url+"/customerOperation/tarrifManagementOperation/tariffDelete",
					params:{id:x.id,name:x.name},
				}).success(function(data){
					if(angular.equals(data,'deleted')){
						$scope.success="Tariff successfully deleted... Reloading list...";
						$scope.isSuccess=true;

						window.setTimeout(function() {
							window.location.href = "tariffManagement";
						}, 3000);

					}else{
						$scope.error="Tariff is mapped. Can't delete";
						$scope.isError=true;

						setTimeout(function () 
								{
							$scope.$apply(function()
									{
								$scope.isError = false;
								$scope.error="";
									});
								}, 3000);

					}

					$scope.tariffList = data;
				}).error(function(data,status){

				});
			});
		}

	}

	$scope.editTariff = function(x){

		if(!$scope.isSuperCustomer){
			
			swal.withForm({
				title: "Authentication required !",
				text: "Please enter your Super Customer credentials to delete selected consumer.",
				showCancelButton: true,
				closeOnConfirm: true,
								
				formFields: [
				   { id: 'Username', placeholder:'Username', required:true },
				   { id: 'Password',type:'password' , placeholder:'Password', required:true }
				]
			
			},function(isConfirm){
				
				//alert("Username : " + Username.value + "\n Password : " + Password.value);
				
				var username = Username.value;
				var password = Password.value;
				
				$http({
					method: "POST",
					url: url + "/authenticateSuperCustomer",
					params:{username : username, password : password},
				}).success(function(data){
					
					if(angular.equals(data,'success')){
						window.location.href="editTariff";
					}
					
					if(angular.equals(data,'error')){
						
						setTimeout(function () 
								{
							$scope.$apply(function()
									{
						swal("Authentication Failed !", "Please enter valid credentials !", "warning")
									});
						}, 1000);
					}
					
				});
			
			});
			
		
		}else{
			$http({
				method:"POST",
				url:url+"/customerOperation/tarrifManagementOperation/tariffEdit",
				params:{id:x.id,name:x.name},
			}).success(function(data){
				if(angular.equals('error',data)){
					$scope.error="Error while fetching transaction.";
					$scope.isError=true;

					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.isError = false;
							$scope.error="";
								});
							}, 3000);
				}else{
					window.location.href="editTariff";
				}

			}).error(function(data,status){

			});
		}

	}

	$scope.editTariffInit = function(){

		$scope.isTariffAdded = false;
		$scope.isTariffEdit = true;

		$http({
			method:"POST",
			url:url+"/customerOperation/tarrifManagementOperation/getEditTariffData",
		}).success(function(data){
			$scope.tariffId = data[0];
			$scope.tariffName = data[1].tariffName;
			var temp = data[1].tariffArray; 
			for( var i = 0; i < temp.length; i++ ) {
				if(angular.equals(parseInt(i)+1,1)){
					$scope.trans1 = temp[i];
					$scope.trans1.check=true;
				}else if(angular.equals(parseInt(i)+1,2)){
					$scope.trans2 = temp[i];
					$scope.trans2.check=true;
				}else if(angular.equals(parseInt(i)+1,3)){
					$scope.trans3 = temp[i];
					$scope.trans3.check=true;
				}else if(angular.equals(parseInt(i)+1,4)){
					$scope.trans4 = temp[i];
					$scope.trans4.check=true;
				}else if(angular.equals(parseInt(i)+1,5)){
					$scope.trans5 = temp[i];
					$scope.trans5.check=true;
				}else if(angular.equals(parseInt(i)+1,6)){
					$scope.trans6 = temp[i];
					$scope.trans6.check=true;
				}else if(angular.equals(parseInt(i)+1,7)){
					$scope.trans7 = temp[i];
					$scope.trans7.check=true;
				}else if(angular.equals(parseInt(i)+1,8)){
					$scope.trans8 = temp[i];
					$scope.trans8.check=true;
				}else if(angular.equals(parseInt(i)+1,9)){
					$scope.trans9 = temp[i];
					$scope.trans9.check=true;
				}else if(angular.equals(parseInt(i)+1,10)){
					$scope.trans10 = temp[i];
					$scope.trans10.check=true;
				}else{
				}

			}

		}).error(function(data,status){
		})
	}

	$scope.getConsumerUserList = function(){

		$scope.paginationList = [10,20,30,40,50];

		$http({
			method: "POST",
			url: url+"/customerOperation/consumerUserManagementData",
		}).success(function (data) {

			$scope.noConsumerFoundDiv = false;
			$scope.consumerListDiv = true;

			//$scope.tariffPlanList = data[0];
			$scope.consumerUserList = data[0];
			$scope.isSuperCustomer  = data[1];
			$scope.limit = 10;
			$scope.begin = 0;

			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.consumerUserList.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show

			if(angular.equals(data,'userRoleNotMatch')){
				$window.location.href="login";

			}
		}).error(function (data,status){

		});	
	}

	$scope.addConsumerUser = function(){

		var consumerAccountNumber = $scope.consumerAccountNumber;
		var firstName= $scope.firstName;
		var lastName = $scope.lastName;
		var userName = $scope.userName;
		var cell_number1 = $scope.cell_number1;
		var email1 = $scope.email1;
		var address1 = $scope.address1;
		var streetName = $scope.streetName;
		var tariffPlan = $scope.tariffPlan;
		var zipcode = $scope.zipcode;
		var activeStatus = $scope.activeStatus;

		$http({
			method: "POST",
			url: url + "/customerOperation/insertConsumerUser",
			params :{consumerAccountNumber : consumerAccountNumber, firstName : firstName, lastName : lastName, 
				userName : userName, cell_number1 : cell_number1, email1 : email1, address1 : address1,
				streetName : streetName, tariffPlan : tariffPlan, zipcode : zipcode, activeStatus : activeStatus},

		}).success(function (data) {

			if(angular.equals(data,'consumeraccnumbererror')){

				$scope.isCodeExist=false;

			}else if(angular.equals(data,'usernameerror')){

				$scope.isUserNameExist=false;

			}else if(angular.equals(data,'added')){

				$scope.isSuccess = true;
				$scope.success="Record added. Redirecting to main page."

					window.setTimeout(function() {
						$window.location.href="consumerUserManagement";
					}, 3000);

			}else{

				$window.location.href="consumerUserManagement";

			}

		}).error(function (data, status) {
			$scope.response = data;
			$scope.postStatus = 'error: ' + status;
		});


	}

	$scope.searchConsumerByAccNum = function(){

		var consumerAccNumInput = $scope.consumerAccNumInput;

		$http({
			method: "POST",
			url: url + "/customerOperation/searchConsumerByAccNum",
			params: {consumerAccNumInput : consumerAccNumInput},

		}).success(function (data) {
			if(angular.equals(data,'nosuchconsumeruserfound')){
				$scope.searchCompleted = false;
				$scope.noConsumerFoundDiv = true;
				$scope.consumerListDiv = false;

			}else{
				$scope.noConsumerFoundDiv = false;
				$scope.consumerListDiv = true;
				$scope.consumerUserList = data;

				$scope.limit = 10;
				$scope.begin = 0;
				// For pagination
				var keys = Object.keys($scope.consumerUserList);
				var length = keys.length;

				// Max list calculations
				$scope.totalPage = Math.ceil(length/$scope.limit); //Here 10 is default records to be appeared
			}
		}).error(function (data,status){

		});	
	}

	$scope.removeConsumerUser = function(consumerId){

		if(!$scope.isSuperCustomer){
			
			//swal("Access Denied!", "You don't have access rights to delete account ! Please contact with admin !", "warning")
			
			swal.withForm({
				title: "Authentication required !",
				text: "Please enter your Super Customer credentials to delete selected consumer.",
				showCancelButton: true,
				closeOnConfirm: true,
								
				formFields: [
				   { id: 'Username', placeholder:'Username', required:true },
				   { id: 'Password',type:'password' , placeholder:'Password', required:true }
				]
			
			},function(isConfirm){
				
				//alert("Username : " + Username.value + "\n Password : " + Password.value);
				
				var username = Username.value;
				var password = Password.value;
				
				$http({
					method: "POST",
					url: url + "/authenticateSuperCustomer",
					params:{username : username, password : password},
				}).success(function(data) {
					
					/*if(angular.equals(data,'usernamecannotbenull')){
						swal("Invalid Username", "Please enter valid Username!", "warning")
					}
					if(angular.equals(data,'passwordcannotbenull')){
						swal("Invalid Password", "Please enter valid Password!", "warning")
					}*/
					
					if(angular.equals(data,'success')){
						$http({

							method: "POST",
							url: url + "/customerOperation/deleteConsumerUser",
							params:{consumerId : consumerId},
						}).success(function(data) {
							if(angular.equals(data,'consumerismapped')){
								$scope.isError=true;
								$scope.errorMessage = "Consumer is mapped with consumer meter. Can't Delete";
								setTimeout(function () 
										{
									$scope.$apply(function()
											{
										$scope.isError = false;
											});
										}, 3000);
							}else if(angular.equals(data,'Consumer successfully deleted')){
								$scope.success="Consumer successfully deleted... Reloading list...";
								$scope.isSuccess=true;

								window.setTimeout(function() {
									$window.location.href="consumerUserManagement";
								}, 3000);

							}else{
								$window.location.reload();
							}

						});
						
					}
					if(angular.equals(data,'error')){
						
						setTimeout(function () 
								{
							$scope.$apply(function()
									{
						swal("Authentication Failed !", "Please enter valid credentials !", "warning")
									});
						}, 1000);
					}

				});
			});
		}else{
			
			swal({
				   title: 'Confirm',
				   text: 'Are you sure you want to delete this Customer with ID : '+ consumerId + '?',
				   type: 'warning',
				   showCancelButton: true,
				   confirmButtonText: 'Yes',
				   cancelButtonText: 'Cancel'
				}, function(){
					$http({

						method: "POST",
						url: url + "/customerOperation/deleteConsumerUser",
						params:{consumerId : consumerId},
					}).success(function(data) {
						if(angular.equals(data,'consumerismapped')){
							$scope.isError=true;
							$scope.errorMessage = "Consumer is mapped with consumer meter. Can't Delete";
							setTimeout(function () 
									{
								$scope.$apply(function()
										{
									$scope.isError = false;
										});
									}, 3000);
						}else if(angular.equals(data,'Consumer successfully deleted')){
							$scope.success="Consumer successfully deleted... Reloading list...";
							$scope.isSuccess=true;

							window.setTimeout(function() {
								$window.location.href="consumerUserManagement";
							}, 3000);

						}else{
							$window.location.reload();
						}

					});
			});
		
			}
	}

	$scope.abortDelete = function(){
		swal("Error !", "Selected Consumer is already deleted !", "error")
	}
	
	$scope.abortDeleteInstaller = function(){
		swal("Error !", "Selected Installer is already deleted !", "error")
	}
	
	$scope.abortDeleteTechnician = function(){
		swal("Error !", "Selected Technician is already deleted !", "error")
	}
	
	$scope.abortSiteDelete = function(){
		swal("Error !", "You can't modify inactive site !", "error")
	}
	
	$scope.editConsumerUser = function(consumer){
		$http({
			method: "POST",
			url: url + "/customerOperation/editConsumerUser",
			params:{consumerId:consumer.consumerId},
		}).success(function(data){
			if(angular.equals(data,'nosuchuserfound')){

			}else{
				$window.location.href="editConsumerUserPageRedirect";
			}
		}).error(function(data,status){

		});
	}

	$scope.getEditConsumerUserDetails = function(){

		//First make ajax call and get that sessioned user entity.

		$scope.activeStatus = "Active";

		$http({
			method: "POST",
			url:url+"/customerOperation/getEditConsumerUserEntity",
		}).success(function(data){
			if(angular.equals(data,'nosuchuserfound')){
				// code for displaying error
			}else{
				$scope.consumerId = data[0].consumerId;
				$scope.consumerAccountNumber = data[0].consumerAccountNumber;
				$scope.firstName = data[0].firstName;
				$scope.lastName = data[0].lastName;
				$scope.userName = data[0].userName;
				$scope.address1 = data[0].address1;
				$scope.streetName = data[0].streetName;
				$scope.zipcode = data[0].zipcode;
				$scope.cell_number1 = data[0].cell_number1;
				$scope.email1 = data[0].email1;
				$scope.tariffId = data[0].tariffPlanId;
				$scope.activeStatus = data[0].activeStatus;
				$scope.tariffList = data[1];

			}
		}).error(function(data,status){

		});
	}


	$scope.updateConsumerUser = function(){

		var consumerId = $scope.consumerId;
		var consumerAccountNumber = $scope.consumerAccountNumber;
		var firstName = $scope.firstName;
		var lastName = $scope.lastName;
		var userName = $scope.userName;
		var address1 = $scope.address1;
		var streetName = $scope.streetName;
		var zipcode = $scope.zipcode;
		var cell_number1 = $scope.cell_number1;
		var email1 = $scope.email1;
		var activeStatus = $scope.activeStatus;
		var tariffid = $scope.tariffId;

		$http({
			method: "POST",
			url:url+"/customerOperation/updateConsumerUserDetails",
			params:{ consumerId : consumerId,
				consumerAccountNumber:consumerAccountNumber,
				firstName:firstName,
				lastName:lastName,
				userName:userName,
				address1:address1,
				streetName:streetName,
				zipcode:zipcode,
				cell_number1:cell_number1,
				streetName:streetName,
				email1:email1,
				activeStatus:activeStatus,
				tariffid:tariffid},

		}).success(function(data){

			if(angular.equals(data,'successfullyupdated')){
				$scope.isSuccess = true;
				$scope.success="Record updated. Redirecting to main page."

					window.setTimeout(function() {
						$window.location.href="consumerUserManagement";
					}, 3000);
			}
		}).error(function(data,status){
			$scope.isError=true;
			$scope.errorMessage="Error while fetching data.";
		});

	}


	$scope.editTariffData = function(){
	}

	$scope.addTariffFunction = function(x,status){
		if(!$scope.isSuperCustomer){
			//swal("Access Denied!", "You don't have access rights to modify tariff ! Please contact with admin !", "warning")
			
			swal.withForm({
				title: "Authentication required !",
				text: "Please enter your Super Customer credentials to delete selected consumer.",
				showCancelButton: true,
				closeOnConfirm: true,
								
				formFields: [
				   { id: 'Username', placeholder:'Username', required:true },
				   { id: 'Password',type:'password' , placeholder:'Password', required:true }
				]
			
			},function(isConfirm){
				
				//alert("Username : " + Username.value + "\n Password : " + Password.value);
				
				var username = Username.value;
				var password = Password.value;
				
				$http({
					method: "POST",
					url: url + "/authenticateSuperCustomer",
					params:{username : username, password : password},
				}).success(function(data){
					
					if(angular.equals(data,'success')){
						window.location.href="addNewTariff";
					}
					
					if(angular.equals(data,'error')){
						
						setTimeout(function () 
								{
							$scope.$apply(function()
									{
						swal("Authentication Failed !", "Please enter valid credentials !", "warning")
									});
						}, 1000);
					}
					
				});
			
			});
			
		}else{
			window.location.href="addNewTariff";
		}

	}

	$scope.isDisabled = function(){
		if(angular.equals('true',$scope.isSuperCustomer)){
			return false;
		}else{
			return true;
		}
	}

	// Get all consumer meter details
	$scope.getConsumerList = function(){

		$http({
			method: "POST",
			url: url+"/customerOperation/consumerManagementData",
		}).success(function (data) {

			$scope.noConsumerFoundDiv = false;
			$scope.consumerListDiv = true;

			//$scope.tariffPlanList = data[0];
			$scope.consumerList = data[1];
			$scope.isCustomer = data[0].isCustomer;

			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.consumerList.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show

			if(angular.equals(data,'userRoleNotMatch')){
				$window.location.href="loginForm";

			}
		}).error(function (data,status){

		});
	}
	
	// Angular method for Search Consumer User by Username
	$scope.searchConsumer = function(){
		var searchRegisterId = $scope.searchRegisterId;
		var searchConsumerAccNo = $scope.searchConsumerAccNo;

		$http({
			method: "POST",
			url: url + "/customerOperation/searchConsumer",
			params: {searchRegisterId:searchRegisterId, searchConsumerAccNo:searchConsumerAccNo},
		}).success(function (data) {
			if(angular.equals(data,'nosuchuserfound')){
				$scope.noConsumerFoundDiv = true;
				$scope.consumerListDiv = false;
			}else{

				$scope.noConsumerFoundDiv = false;
				$scope.consumerListDiv = true;
				$scope.consumerList = data[0];

				$scope.limit = 10;
				$scope.begin = 0;

				// For pagination
				var keys = Object.keys($scope.consumerList);
				var length = keys.length;

				// Max list calculations
				$scope.totalPage = Math.ceil(length/$scope.limit); //Here 10 is default records to be appeared

				$scope.searchCompleted = true;
				setTimeout(function ()
						{
					$scope.$apply(function()
							{
						$scope.searchCompleted = false;
							});
						}, 3000);
			}
		}).error(function (data,status){

		});

	}
	
	$scope.dcSerialBtnClicked = function(){
		
		if(!$scope.isSuperCustomer){

			swal.withForm({
				title: "Authentication required !",
				text: "Please enter your Super Customer credentials to delete selected consumer.",
				showCancelButton: true,
				closeOnConfirm: true,
								
				formFields: [
				   { id: 'Username', placeholder:'Username', required:true },
				   { id: 'Password',type:'password' , placeholder:'Password', required:true }
				]
			
			},function(isConfirm){
				
				//alert("Username : " + Username.value + "\n Password : " + Password.value);
				
				var username = Username.value;
				var password = Password.value;
				
				$http({
					method: "POST",
					url: url + "/authenticateSuperCustomer",
					params:{username : username, password : password},
				}).success(function(data){
					
					if(angular.equals(data,'success')){
						$scope.nullSiteToggle = !$scope.nullSiteToggle;
					}
					
					if(angular.equals(data,'error')){
						
						setTimeout(function () 
								{
							$scope.$apply(function()
									{
						swal("Authentication Failed !", "Please enter valid credentials !", "warning")
									});
						}, 1000);
					}
					
				});
			
			});
			
		
		}else{
			$scope.nullSiteToggle = !$scope.nullSiteToggle;
		}
		
	}

	$scope.getTariffPlanAndBillingFrequencyAndSiteList = function(){

		$http({
			method: "POST",
			url: url+"/customerOperation/tariffPlanAndBillingFrequencyDataAndSite",
		}).success(function (data) {

			$scope.tariffPlanList = data[0];
			$scope.billingFrequencyNames = data[1];
			$scope.siteList = data[2];

			$scope.limit = 10;
			$scope.begin = 0;

			/*// For pagination
			var keys = Object.keys($scope.consumerList);
			var length = keys.length;

			// Max list calculations
			$scope.totalPage = Math.ceil(length/$scope.limit); //Here 10 is default records to be appeared
			 */
			if(angular.equals(data,'userRoleNotMatch')){
				$window.location.href="loginForm";

			}
		}).error(function (data,status){

		});
	}
	
	// Angular method for Adding New Consumer User
	$scope.addConsumer = function(){

		/*var consumerAccNo = $scope.consumerAccNo;*/
		var registerId = $scope.registerId;
		var endpointSerialNumber = $scope.endpointSerialNumber;
		var latitude = $scope.latitude;
		var longitude = $scope.longitude;
		var endpointIntegrity = $scope.endpointIntegrity;
		var isRepeater = $scope.isRepeater;
		var address1= $scope.address1;
		var address2 = $scope.address2;
		var address3 = $scope.address3;
		var streetName = $scope.streetName;
		var zipcode = $scope.zipcode;
		var selectedBillingFrequency = $scope.selectedBillingFrequency;
		var lastMeterReading = $scope.lastMeterReading;
		var latsMeterReadingDate = new Date($scope.latsMeterReadingDate);
		var meterReadingDate = new Date($scope.meterReadingDate);
		var selectedTariffId = $scope.selectedTariffId;
		var selectedSiteId = $scope.selectedSiteId;
		var billingStartDate = $scope.billingStartDate;
		var unitOfMeasure = $scope.unitOfMeasure;
		var noOfOccupants = $scope.noOfOccupants;
		$scope.isRegisterIdExist=false;
		$scope.isSerialNumberExist=false;
		$scope.unKnownErrorDiv = false;
		/*var email1 = $scope.email1;*/

		var latsMeterReadingDate = $filter('date')(new Date($scope.latsMeterReadingDate),"yyyy-MM-dd HH:mm:ss");
		var meterReadingDate = $filter('date')(new Date($scope.meterReadingDate),"yyyy-MM-dd HH:mm:ss");
		var billingStartDate = $filter('date')(new Date($scope.billingStartDate),"yyyy-MM-dd HH:mm:ss");

		$http({
			method: "POST",
			url: url + "/customerOperation/insertConsumer",
			params: {registerId:registerId, endpointSerialNumber:endpointSerialNumber, latitude:latitude, longitude:longitude, endpointIntegrity:endpointIntegrity, isRepeater:isRepeater, 
				streetName:streetName, address1:address1, address2:address2, address3:address3, zipcode:zipcode, 
				selectedBillingFrequency:selectedBillingFrequency, lastMeterReading:lastMeterReading, latsMeterReadingDate:latsMeterReadingDate, 
				meterReadingDate:meterReadingDate, selectedTariffId:selectedTariffId, 
				selectedSiteId:selectedSiteId, billingStartDate:billingStartDate,unitOfMeasure:unitOfMeasure,noOfOccupants:noOfOccupants},
		}).success(function (data) {
			if(angular.equals(data,'consumercodeerror')){
				$scope.isRegisterIdExist=true;
			}if (angular.equals(data,'serialnumbererror')) {
				$scope.isSerialNumberExist=true;
			}if (angular.equals(data,'unknownerror')) {
				$scope.unKnownErrorDiv = true;
			}
			if(angular.equals(data,'added')){
				
				$scope.isSuccess = true;
				$scope.success="Record added. Redirecting to main page."

					window.setTimeout(function() {
						$window.location.href="consumerManagement";
					}, 3000);
				
			}
		}).error(function (data, status) {
			$scope.response = data;
			$scope.postStatus = 'error: ' + status;
		});

	};
	
	$scope.resetErrors = function() {
		$scope.isRegisterIdExist=false;
		$scope.isSerialNumberExist=false;
		$scope.unKnownErrorDiv = false;
		
		$scope.registerId = null;
		$scope.streetName = null;
		$scope.address1 = null;
		$scope.address2 = null;
		$scope.address3 = null;
		$scope.zipcode = null;
		$scope.endpointSerialNumber = null;
		$scope.latitude = null;
		$scope.longitude = null;
		$scope.endpointIntegrity = null;
		$scope.isRepeater = null;
		$scope.selectedBillingFrequency = null;
		$scope.selectedTariffId = null;
		$scope.selectedSiteId = null;
		$scope.lastMeterReading = null;
		$scope.latsMeterReadingDate = null;
		$scope.meterReadingDate = null;
		$scope.billingStartDate = null;
		
	}
	
	// Angular method for Redirect to angular route for redirection.
	$scope.editCustomer = function(consumerMeterId){
		$http({
			method: "POST",
			url: url + "/customerOperation/editConsumer",
			params:{consumerMeterId:consumerMeterId},
		}).success(function(data){
			if(angular.equals(data,'nosuchuserfound')){
				// code for displaying error
			}else{
				//	$state.transitionTo('editUser');
				$window.location.href="editCustomerPageRedirect";
			}
		}).error(function(data,status){

		});
	}
	
	$scope.boolToStr = function(arg) {return arg ? 'True' : 'False'};
	
	$scope.abortEndPointDelete = function(){
		swal("Error !", "Selected EndPoint is already deleted !", "error")
	}
	// Angular method for Edit Consumer field will be set here
	$scope.getEditConsumerDetails = function(){

		//First make ajax call and get that sessioned user entity.
		$http({
			method: "POST",
			url:url+"/customerOperation/getEditConsumerEntity",
		}).success(function(data){
			if(angular.equals(data,'nosuchuserfound')){
				// code for displaying error
			}else{

				$scope.isCustomer = data.isCustomer;

				$scope.consumerMeterId = data.consumerMeterId;
				$scope.conAccNumber = data.conAccNumber;
				$scope.registerId = data.registerId;

				$scope.endpointSerialNumber = data.endpointSerialNumber;
				$scope.latitude = data.latitude;
				$scope.longitude = data.longitude;
				$scope.endpointIntegrity = data.endpointIntegrity;
				$scope.isRepeater = data.isRepeater;
				$scope.address1 = data.address1;
				$scope.address2 = data.address2;
				$scope.address3 = data.address3;
				$scope.streetName = data.streetName;
				$scope.zipcode = data.zipcode;

				$scope.savedBillingFrequency = data.savedBillingFrequency;
				$scope.savedTariffId = data.savedTariffId;
				$scope.savedSiteId = data.savedSiteId;
				
				$scope.totalEndpointAttached = data.totalEndpointAttached;
				$scope.batteryVoltage = data.batteryVoltage;
				$scope.currentReadingDate = data.currentReadingDate;
				$scope.installDate = data.installDate;
				$scope.batteryReplacedDate = data.batteryReplacedDate;
				$scope.assetInspectionDate = data.assetInspectionDate;
				$scope.districtMeterSerialNo = data.districtMeterSerialNo;
				
				$scope.lastMeterReading = data.lastMeterReading;
				$scope.latsMeterReadingDate = data.latsMeterReadingDate;
				$scope.meterReadingDate = data.meterReadingDate;
				$scope.billingStartDate = data.billingStartDate;
				
				$scope.textNote = data.textNote;
				$scope.filePath = data.filePath;
				$scope.originalFileName = data.originalFileName;
				$scope.unitOfMeasure = data.unitOfMeasure;
				$scope.noOfOccupants = data.noOfOccupants;
				
				$scope.isActive = data.isActive;
				$scope.tariffList = JSON.parse(data.tariffList);
				$scope.siteList = JSON.parse(data.siteList);
				$scope.billingFrequencyList = JSON.parse(data.billingFrequencyList);

				if (!$scope.isCustomer) {
					$scope.hosepipe = {
							value: data.hosepipe
					};
					$scope.irrigationSystem = {
							value: data.irrigationSystem
					};
					$scope.swimmingPool = {
							value: data.swimmingPool
					};
					$scope.hotTub = {
							value: data.hotTub
					}; 
					$scope.pond = {
							value: data.pond
					};
				}
			}
		}).error(function(data,status){

		});
	}
	
	// Angular method for Update existing consumer with new details
	$scope.updateConsumer = function(){

		var consumerMeterId = $scope.consumerMeterId;
		//var conAccNumber = $scope.conAccNumber;
		var registerId = $scope.registerId;

		var endpointSerialNumber = $scope.endpointSerialNumber;
		var latitude = $scope.latitude;
		var longitude = $scope.longitude;
		var endpointIntegrity = $scope.endpointIntegrity;
		var isRepeater = $scope.isRepeater;
		var address1= $scope.address1;
		var address2 = $scope.address2;
		var address3 = $scope.address3;
		var streetName = $scope.streetName;
		var zipcode = $scope.zipcode;

		var savedBillingFrequency = $scope.savedBillingFrequency;
		var savedTariffId = $scope.savedTariffId;
		var savedSiteId = $scope.savedSiteId;
		
		/*var totalEndpointAttached = $scope.totalEndpointAttached;
		var batteryVoltage = $scope.batteryVoltage;
		var currentReadingDate = $scope.currentReadingDate;
		var installDate = $scope.installDate;
		var batteryReplacedDate = $scope.batteryReplacedDate;
		var assetInspectionDate = $scope.assetInspectionDate;
		var districtMeterSerialNo = $scope.districtMeterSerialNo;*/
		
		var lastMeterReading = $scope.lastMeterReading;
		var latsMeterReadingDate = new Date($scope.latsMeterReadingDate);

		var meterReadingDate = new Date($scope.meterReadingDate);
		var billingStartDate = new Date($scope.billingStartDate);
		/*var email1 = $scope.email1;*/
		var isActive = $scope.isActive;

		var latsMeterReadingDate = $filter('date')($scope.latsMeterReadingDate,"yyyy-MM-dd HH:mm:ss");
		var meterReadingDate = $filter('date')($scope.meterReadingDate,"yyyy-MM-dd HH:mm:ss");
		var billingStartDate = $filter('date')($scope.billingStartDate,"yyyy-MM-dd HH:mm:ss");
		var unitOfMeasure = $scope.unitOfMeasure;
		var noOfOccupants = $scope.noOfOccupants;

		if (!$scope.isCustomer) {
			var hosepipe = $scope.hosepipe.value;
			var irrigationSystem = $scope.irrigationSystem.value;
			var swimmingPool = $scope.swimmingPool.value;
			var hotTub = $scope.hotTub.value;
			var pond = $scope.pond.value;

			$http({
				method: "POST",
				url:url+"/customerOperation/updateConsumer",
				params:{ consumerMeterId:consumerMeterId,
					registerId:registerId,

					endpointSerialNumber:endpointSerialNumber,
					latitude:latitude,
					longitude:longitude,
					endpointIntegrity:endpointIntegrity,
					isRepeater:isRepeater,
					address1:address1,
					address2:address2,
					address3:address3,
					streetName:streetName,
					zipcode:zipcode,

					savedBillingFrequency:savedBillingFrequency,
					savedTariffId:savedTariffId,
					savedSiteId:savedSiteId,
					lastMeterReading:lastMeterReading,
					latsMeterReadingDate:latsMeterReadingDate,
					hosepipe:hosepipe,
					irrigationSystem:irrigationSystem,
					swimmingPool:swimmingPool,
					hotTub:hotTub,
					pond:pond,
					meterReadingDate:meterReadingDate,
					billingStartDate:billingStartDate,
					unitOfMeasure:unitOfMeasure,
					noOfOccupants:noOfOccupants,
					/*email1:email1,*/
					isActive:isActive},
			}).success(function(data){
				
				$scope.isSuccess = true;
				$scope.success="Record added. Redirecting to main page."

					window.setTimeout(function() {
						$window.location.href="consumerEPManagement";
					}, 3000);
				
				//	$state.transitionTo('userManagement'); //back to same page again
			}).error(function(data,status){

			});
		} else {
			$http({
				method: "POST",
				url:url+"/customerOperation/updateConsumer",
				params:{ consumerMeterId:consumerMeterId,
					registerId:registerId,

					endpointSerialNumber:endpointSerialNumber,
					latitude:latitude,
					longitude:longitude,
					endpointIntegrity:endpointIntegrity,
					isRepeater:isRepeater,
					address1:address1,
					address2:address2,
					address3:address3,
					streetName:streetName,
					zipcode:zipcode,

					savedBillingFrequency:savedBillingFrequency,
					savedTariffId:savedTariffId,
					savedSiteId:savedSiteId,
					lastMeterReading:lastMeterReading,
					latsMeterReadingDate:latsMeterReadingDate,
					meterReadingDate:meterReadingDate,
					billingStartDate:billingStartDate,
					unitOfMeasure:unitOfMeasure,
					noOfOccupants:noOfOccupants,
					/*email1:email1,*/
					isActive:isActive},
			}).success(function(data){
				$scope.isSuccess = true;
				$scope.success="Record added. Redirecting to main page."

					window.setTimeout(function() {
						$window.location.href="customerEPManagement";
					}, 3000);
				//	$state.transitionTo('userManagement'); //back to same page again
			}).error(function(data,status){

			});
		}
	}
	
	$scope.dcSimBtnClicked = function(){

		if(!$scope.isSuperCustomer){
			swal.withForm({
				title: "Authentication required !",
				text: "Please enter your Super Customer credentials to delete selected consumer.",
				showCancelButton: true,
				closeOnConfirm: true,
								
				formFields: [
				   { id: 'Username', placeholder:'Username', required:true },
				   { id: 'Password',type:'password' , placeholder:'Password', required:true }
				]
			
			},function(isConfirm){
				
				//alert("Username : " + Username.value + "\n Password : " + Password.value);
				
				var username = Username.value;
				var password = Password.value;
				
				$http({
					method: "POST",
					url: url + "/authenticateSuperCustomer",
					params:{username : username, password : password},
				}).success(function(data){
					
					if(angular.equals(data,'success')){
						$scope.nullSiteToggle = !$scope.nullSiteToggle;
					}
					
					if(angular.equals(data,'error')){
						
						setTimeout(function () 
								{
							$scope.$apply(function()
									{
						swal("Authentication Failed !", "Please enter valid credentials !", "warning")
									});
						}, 1000);
					}
				});
			});
		}else{
			$scope.nullSiteToggle = !$scope.nullSiteToggle;
		}
	}

	$scope.toggleInstaller = function(){
		if($scope.assignDC.length>0 ){
			$scope.isInstaller=true;

		}else{
			$scope.isInstaller=false;
			$scope.selectedInstaller="";
		}
	}


	$scope.resetPageForAddConsumer = function(){

		$scope.consumerAccountNumber = null;
		$scope.firstName = null;
		$scope.lastName = null;
		$scope.userName = null;
		$scope.cell_number1 = null;
		$scope.email1 = null;
		$scope.address1 = null;
		$scope.streetName = null;
		$scope.zipcode = null;
		$scope.tariffPlan = null;

	}
	
	$scope.resetPageForEditConsumer = function(){

		$scope.firstName = null;
		$scope.lastName = null;
		$scope.cell_number1 = null;
		$scope.email1 = null;
		$scope.address1 = null;
		$scope.streetName = null;
		$scope.zipcode = null;

	}
	
	$scope.initdcConnectionMap = function(){
		$http({
			method: "POST",
			url:url+"/customerOperation/initdcConnectionMap",
		}).success(function(data){
			$scope.dclist = data;
		}).error(function(data,status){

		});
	}
	
	$scope.getConnectionMap = function(){
		$http({
			method: "POST",
			url:url+"/customerOperation/getConnectionMap",
			params:{dcId:$scope.seletedDc.dcId}
		}).success(function(response){
			drawChart(response);
		}).error(function(data,status){

		});
	}

	function drawChart(datas){
	        var table = new google.visualization.DataTable();
	        table.addColumn('string', 'Name');
	        table.addColumn('string', 'Manager');
	        table.addColumn('string', 'ToolTip');
	        
	        table.addRows(JSON.parse(datas));
	        
	        var options = {
	        		size: 'medium',
	                allowHtml:true,
	            };
	 
	        var chart = new google.visualization.OrgChart(document.getElementById('chart_div'));
	        chart.draw(table, options);
	        google.visualization.events.addListener(chart, 'select', selectHandler);
	        
	        function selectHandler() {
	        	var selectedItem = chart.getSelection()[0];
	    		if (selectedItem) {
	    		    var consumerMeterId = table.getValue(selectedItem.row, 2);
	    		    editEndPoint(consumerMeterId)
	    		}
	    		  
	    	}
	}
	
	
	// Angular method for Redirect to angular route for redirection.
	function editEndPoint(consumerMeterId){
		$http({
			method: "POST",
			url: url + "/editConsumer",
			params:{consumerMeterId:consumerMeterId},
		}).success(function(data){
			if(angular.equals(data,'datafetched')){
				$window.open('editConsumerPageRedirect', '_blank');
			}
		}).error(function(data,status){
		});
	}
	
//  Angular method for confirm Delete User popup
	$scope.removeConsumer = function(id, registerId){

		swal({
			   title: 'Confirm',
			   text: 'Are you sure you want to delete this Endpoint ?',
			   type: 'warning',
			   showCancelButton: true,
			   confirmButtonText: 'Yes',
			   cancelButtonText: 'Cancel'
			}, function(){
		
			$http({
				method: "POST",
				url: url + "/customerOperation/deleteConsumer",
				params:{consumerMeterId : id},
			}).success(function(data) {
				if(angular.equals(data,'endpointismapped')){
					$scope.isError=true;
					$scope.errorMessage = "Endpoint is mapped with other data. Can't delete";
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.isError = false;
								});
							}, 3000);
				}
				if(angular.equals(data,'Endpoint successfully deleted')){
					
					$scope.success="Endpoint successfully deleted... Reloading list...";
					$scope.isSuccess=true;

					window.setTimeout(function() {
						$window.location.href="customerEPManagement";
					}, 3000);
					
				} // reload to reflect changes on UI side
			});
		});

	}
	
	// Angular method for Redirect to angular route for redirection.
	$scope.showBillingData = function(consumerMeterId,registerId){
		$http({
			method: "POST",
			url: url + "/customerOperation/billingData",
			params:{consumerMeterId:consumerMeterId,registerId:registerId},
		}).success(function(data){
			$window.open("showBillingDataPageRedirect",'_blank');
		}).error(function(data,status){

		});
	}
	
	$scope.getBillingDataByConsumerId = function(){
		
		$http({
			method: "POST",
			url: url + "/customerOperation/getbillingHistoryData",
		}).success(function(data){
			if(angular.equals(data,"nosuchdatafound")){
				$scope.noDataFoundDiv = true;
				$scope.billingHistoryDiv = false;
			}else{
				$scope.noDataFoundDiv = false;
				$scope.billingHistoryDiv = true;
				$scope.billingDataList = data[1];
				$scope.isCustomer = data[0].isCustomer;
				$scope.registerId = data[1][0].registerId;
				// For pagination
				$scope.recordSize = 10;
				$scope.totalItems = $scope.billingDataList.length;
				$scope.currentPage = 1;
				$scope.itemsPerPage = $scope.recordSize;
				$scope.maxSize = 5; //Number of pager buttons to show
			}
		}).error(function(data,status){

		});
	}
	
	//Installer CRUD start

	$scope.getInstallerByCustomerId = function(customerId){
		$http({
			method: "POST",
			url: url + "/customerOperation/getInstallerByCustomerId",
			params :{customerId :customerId}
		}).success(function (data) {
			if(data == 'noSuchInstallerFound'){
				$scope.noInstallerFoundDiv=true;
				$scope.installerFoundDiv=false;
			}else {
				$scope.installerFoundDiv = true;
				$scope.noInstallerFoundDiv=false;
				$scope.installers = data;
			}

			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.installers.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show
		}).error(function (data,status){

		});
	}

	$scope.addInstaller = function(customerId){


		var firstname = $scope.installer.firstname;
		var lastname = $scope.installer.lastname;
		var username = $scope.installer.username;
		var phone = $scope.installer.phone;
		var email1 = $scope.installer.email1;
		var zipcode = $scope.installer.zipcode;
		var address1 = $scope.installer.address1;
		var streetname= $scope.installer.streetname;
		if(	$scope.userNameExist){
			$scope.userNameMessage ="Please change Username.";
			return false;
		}else{
			$http({
				method: "POST",
				url: url + "/customerOperation/addInstaller",
				params :{firstname :firstname,lastname:lastname,username:username,phone:phone,streetname:streetname,
					email1:email1,address1:address1,zipcode:zipcode,customerId:customerId}
			}).success(function (data) {

				if(data == "installerAddedSuccessfully"){
					$window.location.href="installerManagement?message=success";
				}

				$scope.installers = data;
			}).error(function (data,status){

			});
		}
	}




	$scope.removeInstaller = function(id){

		swal({
			   title: 'Confirm',
			   text: 'Want to delete Installer with ID : '+id+' ?',
			   type: 'warning',
			   showCancelButton: true,
			   confirmButtonText: 'Yes',
			   cancelButtonText: 'Cancel'
			}, function(){
		
			$http({
				method: "POST",
				url: url + "/customerOperation/deleteInstaller",
				params:{userId : id},
			}).success(function(data) {
				if(data == 'userismapped'){
					$scope.isError=true;
					$scope.errorMessage = "Installer is mapped with other object. Can't delete";
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.isError = false;
								});
							}, 3000);
				}
				if(data == 'installerdeletedsuccessfully'){
					$window.location.href="installerManagement";
				}

				// reload to reflect changes on UI side
			});
		});

	}


	$scope.searchInstaller = function(){
		var criteria = $scope.installer.name;
		$scope.installerFoundDiv=false;
		$http({
			method: "POST",
			url: url + "/customerOperation/searchInstaller",
			params:{criteria : criteria},
		}).success(function(data) {
			if(data == 'noSuchInstallerFound'){
				$scope.installerFoundDiv=false;
				$scope.noInstallerFoundDiv=true;
				$scope.installers = "";

			}else {
				$scope.installerFoundDiv = true;
				$scope.noInstallerFoundDiv=false;
				$scope.installers = data;
			}

			// reload to reflect changes on UI side
		}).error(function (data,status){

		});
	}

	$scope.checkUserName = function(){
		var userName = $scope.installer.username;

		$http({
			method: "POST",
			url: url + "/customerOperation/checkUserName",
			params:{userName : userName},
		}).success(function(data) {
			if(data == 'usernameerror'){
				$scope.userNameExist=true;
				$scope.userNameMessage ="Username already exists.";
				return false;
			}else if(data == 'success'){
				$scope.userNameExist=false;
				return true;
			}
		}).error(function (data,status){

		});
	}
	//Installer CRUD Finish

	// Technician CRUD Start

	$scope.getTechnicianByCustomerId = function(){
		$http({
			method: "POST",
			url: url + "/customerOperation/getTechnicianByCustomerId"
		}).success(function (data) {
			if(data == 'noSuchTechnicianFound'){
				$scope.noTechnicianFoundDiv=true;
				$scope.technicianFoundDiv=false;
			}else {
				$scope.technicianFoundDiv = true;
				$scope.noTechnicianFoundDiv=false;
				$scope.technicians = data;
			}
			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.technicians.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show

		}).error(function (data,status){

		});
	}


	$scope.addTechnician = function(customerId){
		var firstname = $scope.technician.firstname;
		var lastname = $scope.technician.lastname;
		var phone = $scope.technician.phone;
		var email1 = $scope.technician.email1;
		var zipcode = $scope.technician.zipcode;
		var address1 = $scope.technician.address1;
		var streetname= $scope.technician.streetname;

		$http({
			method: "POST",
			url: url + "/customerOperation/addTechnician",
			params :{firstname :firstname,lastname:lastname,phone:phone,streetname:streetname,
				email1:email1,address1:address1,zipcode:zipcode,customerId:customerId}
		}).success(function (data) {

			if(data == "technicianAddedSuccessfully"){
				
				$scope.isSuccess = true;
				$scope.success="Record added. Redirecting to main page."

					window.setTimeout(function() {
						//$window.location.href="technicianManagement?message=success";
						$window.location.href="technicianManagement";
					}, 3000);
			}

		}).error(function (data,status){

		});

	}

	$scope.editTechnicianMethod = function(id){
		$scope.submit();
	}

	$scope.updateInstaller = function(){
		var firstname = $scope.installer.firstname;
		var lastname = $scope.installer.lastname;
		var phone = $scope.installer.phone;
		var email1 = $scope.installer.email1;
		var zipcode = $scope.installer.zipcode;
		var address1 = $scope.installer.address1;
		var streetname= $scope.installer.streetname;
		var installerUserId= $scope.installer.installerUserId;
		var status = $scope.installer.activeStatus;
		$http({
			method: "POST",
			url: url + "/customerOperation/updateInstaller",
			params :{firstname :firstname,lastname:lastname,phone:phone,streetname:streetname,
				email1:email1,address1:address1,zipcode:zipcode,installerUserId:installerUserId,status:status}
		}).success(function (data) {

			if(data == "installerUpdatedSuccessfully"){
				$window.location.href="installerManagement?message=update";
			}

			$scope.installers = data;
		}).error(function (data,status){

		});
	}
	$scope.updateTechnician = function(){
		var firstname = $scope.technician.firstname;
		var lastname = $scope.technician.lastname;
		var phone = $scope.technician.phone;
		var email1 = $scope.technician.email1;
		var zipcode = $scope.technician.zipcode;
		var address1 = $scope.technician.address1;
		var streetname= $scope.technician.streetname;
		var contactDetailsId = $scope.technician.contactDetailsId;
		var status = $scope.technician.activeStatus;
		$http({
			method: "POST",
			url: url + "/customerOperation/updateTechnician",
			params :{firstname :firstname,lastname:lastname,phone:phone,streetname:streetname,
				email1:email1,address1:address1,zipcode:zipcode,detailId:contactDetailsId,status:status}
		}).success(function (data) {

			if(data == "technicianUpdatedSuccessfully"){
				
				$scope.isSuccess = true;
				$scope.success="Record updated. Redirecting to main page."

					window.setTimeout(function() {
						//$window.location.href="technicianManagement?message=update";
						$window.location.href="technicianManagement";
					}, 3000);
			}

			$scope.installers = data;
		}).error(function (data,status){

		});
	}

	$scope.searchTechnician = function(){
		var criteria = $scope.technician.name;
		$scope.noTechnicianFoundDiv=false;
		$http({
			method: "POST",
			url: url + "/customerOperation/searchTechnician",
			params:{criteria : criteria},
		}).success(function(data) {
			if(data == 'noSuchTechnicianFound'){
				$scope.technicianFoundDiv=false;
				$scope.noTechnicianFoundDiv=true;
				$scope.technicians = "";

			}else {
				$scope.technicianFoundDiv = true;
				$scope.noTechnicianFoundDiv=false;
				$scope.technicians = data;
			}

			// reload to reflect changes on UI side
		}).error(function (data,status){

		});
	}


	$scope.removeTechnician = function(id){

		swal({
			   title: 'Confirm',
			   text: 'Want to delete technician with ID : '+ id +' ?',
			   type: 'warning',
			   showCancelButton: true,
			   confirmButtonText: 'Yes',
			   cancelButtonText: 'Cancel'
			}, function(){
			$http({
				method: "POST",
				url: url + "/customerOperation/deleteTechnician",
				params:{userId : id},
			}).success(function(data) {
				if(angular.equals(data,'userismapped')){
					$scope.isError=true;
					$scope.errorMessage = "Technician is mapped with other object. Can't delete";
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.isError = false;
								});
							}, 3000);
				}
				if(angular.equals(data,'Technician successfully deleted')){
					$scope.success="Technician successfully deleted... Reloading list...";
					$scope.isSuccess=true;

					window.setTimeout(function() {
						$window.location.href="technicianManagement";
					}, 3000);
				}

				// reload to reflect changes on UI side
			});
		});

	}
	//Installer CRUD FINISH
	
}]);

