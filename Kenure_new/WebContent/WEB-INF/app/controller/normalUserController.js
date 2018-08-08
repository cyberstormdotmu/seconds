/**
 * TatvaSoft
 */


kenureApp.controller('normalUserController', ['$scope','$http','$location','$window','$filter','$timeout',function($scope, $http,$location,$window,$filter,$timeout){

	$scope.emailFormat = /^[a-zA-Z]+[a-zA-Z0-9._]+@[a-zA-Z]+\.[a-zA-Z.]{2,5}$/;
	$scope.paginationList = [10,20,30,40,50];
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
	
	// Added default page index to 10
	var defaultPageIndex = 10;
	$scope.recordSize = defaultPageIndex.toString();

	$scope.normalUserInitData = function(){

		$http({
			method: "POST",
			url:url+"/customerOperation/normalCustomerOperation/getNormalCustomerOfSuperCustomer",
		}).success(function(data){
			$scope.normalCustomerList = data;
			$scope.limit = 10;
			$scope.begin = 0;
			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.normalCustomerList.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show
		}).error(function(data,status){

		})

	}

	/*$scope.getNormalCustomerDetails = function(){

		$http({
			method: "POST",
			url: url+"/getNormalCust",
		}).success(function(data){

		}).error(function(data,status){

		});

		$scope.countryList = data.countryList;
	}*/

	$scope.addNormalCustomer = function(){

		var customerCode = $scope.customerCode;
		var firstName = $scope.firstName;
		var lastName = $scope.lastName;
		var userName = $scope.userName;
		var phone = $scope.phone;
		var email = $scope.email;
		var address = $scope.address;
		var zip = $scope.zipcode;

		/*var country = $scope.selectedCountry;
		var currency = $scope.selectedCurrency;
		var status = $scope.activeStatus;

		if(angular.isUndefined($scope.selectedCountry)){
			$scope.isError = true;
			$scope.error = "Provide Proper Country.";
		}else if(angular.isUndefined($scope.selectedCurrency)){
			$scope.isError = true;
			$scope.error = "Provide Proper Currency.";
		}else if(angular.isUndefined($scope.activeStatus)){
			$scope.isError = true;
			$scope.error = "Provide Proper Status.";
		}

		if($scope.isError){
			var errorMessage = $scope.error;
			if(errorMessage.indexOf("Country") > 0){
				$scope.isCountry = true;
			}else if(errorMessage.indexOf("Currency") > 0){
				$scope.isCurrency = true;
			}else if(errorMessage.indexOf("Status") > 0){
				$scope.isStatus = true;
			}

			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isCountry = false;
					$scope.isCurrency = false;
					$scope.isStatus = false;
					$scope.error = ""
						});
					}, 3000);

		}*/

		$http({
			method: "POST",
			url: url+"/customerOperation/normalCustomerOperation/addNormalCustomer",
			params:{customerCode:customerCode,firstName:firstName,lastName:lastName
				,userName:userName,phone:phone,email:email,address:address,zip:zip},
		}).success(function(data){

			if(angular.equals(data, 'success')){
				$scope.isSuccess = true;
				$scope.success="Record added. Redirecting To main page."

					window.setTimeout(function() {
						window.location.href = "normalUserManagement";
					}, 3000);
			}else{
				$scope.isError = true;
				if(angular.equals(data,'codeExist')){
					$scope.error="Customer Code already exists.";
					$scope.isCode = true;
				}else if(angular.equals(data,'userExist')){
					$scope.error="Username already exists.";
					$scope.isUsername = true;
				}else{
					$scope.error="Error while inserting record."
						$scope.isGeneralError = true;
				}
				setTimeout(function ()
						{
					$scope.$apply(function()
							{
						$scope.isError = false;
						$scope.isCode = false;
						$scope.isUsername = false;
						$scope.isGeneralError = false;
						$scope.error = "";
							});
						}, 3000);
			}

		}).error(function(data,status){
			$scope.isError = true;
			$scope.error="Error while inserting record.";
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isError = false;
					$scope.error = ""
						});
					}, 3000);
		})

	}

	$scope.editUser = function(rowObject){
		$http({
			method: "POST",
			url: url+"/customerOperation/normalCustomerOperation/editNormalCustomer",
			params:{rowObject:rowObject.userId},
		}).success(function(data){
			window.location.href="editNormalCustomer";
		}).error(function(data,status) {
		})
	}

	$scope.editNormalCustomerDetails = function(){
		$http({
			method: "POST",
			url: url+"/customerOperation/normalCustomerOperation/getEditNormalCustomerDetails",
		}).success(function(data){
			$scope.firstName = data.firstName;
			$scope.lastName = data.lastName;
			$scope.userName = data.userName;
			$scope.phone = data.phone;
			$scope.email = data.email;
			$scope.address = data.address;
			$scope.zipcode = data.zip;
			$scope.activeStatus = data.activeStatus;
			$scope.userId = data.userId;
		}).error(function(data,status) {
		})
	}

	$scope.abortNormalCustomerDelete = function(){
		swal("Error !", "Selected Customer is already deleted !", "error")
	}
	
	$scope.updateNormalCustomer = function(){
		$http({
			method: "POST",
			url: url+"/customerOperation/normalCustomerOperation/updateNormalCustomer",
			params:{userId:$scope.userId,firstName:$scope.firstName,lastName:$scope.lastName
				,userName:$scope.userName,phone:$scope.phone,email:$scope.email,address:$scope.address,zip:$scope.zipcode, activeStatus : $scope.activeStatus},
		}).success(function(data, status, headers, config) {
			$scope.isSuccess = true;
			$scope.success="Record updated. Redirecting to main page."

				window.setTimeout(function() {
					window.location.href = "normalUserManagement";
				}, 2000);

		}).error(function(data){
		})
	}

	$scope.removeRow = function(id){

		swal({
			   title: 'Confirm',
			   text: 'Want to delete Normal User with ID : '+ id +' ?',
			   type: 'warning',
			   showCancelButton: true,
			   confirmButtonText: 'Yes',
			   cancelButtonText: 'Cancel'
			}, function(){
				
			$http({
				method: "POST",
				url: url+"/customerOperation/normalCustomerOperation/deleteNormalCustomer",
				params:{id:id},
			}).success(function(data) {

				if(angular.equals(data,'success')){
					$scope.success="Customer successfully deleted... Reloading list...";
					$scope.isSuccess=true;

					window.setTimeout(function() {
						$window.location.href="normalUserManagement";
					}, 3000);
				}
				if(angular.equals(data,'error')){
					$scope.isError = true;
					$scope.error="Customer is mapped ! Can't delete.";
				
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.isError = false;
							});
						}, 3000);
				}
				

			}).error(function(data){
			});
		});

	}

	$scope.searchNormalCustomer = function(){
		if(!angular.isUndefined($scope.customerNameCriteria) && $scope.customerNameCriteria){

			$http({
				method: "POST",
				url: url+"/customerOperation/normalCustomerOperation/searchNormalCustomer",
				params: {searchCriteria:$scope.customerNameCriteria},
			}).success(function(data){
				$scope.isSuccess = true;
				$scope.success = "Search Completed !";
				
				if(angular.equals(data, 'empty')){
					$scope.noUserFoundDiv = true;
				}else{
					$scope.isSuccess = true;
					$scope.success = "Search completed !";
					$scope.normalCustomerList = data;
					$scope.noUserFoundDiv = false;
				}
				setTimeout(function ()
						{
					$scope.$apply(function()
							{
						$scope.isSuccess = false;
						$scope.success = "";
							});
						}, 3000);

			}).error(function(data,status) {

			})

		}
	}

	$scope.refreshPage = function(){
		window.location.reload();
	}

	$scope.resetPageForAddUser = function(){
		
		$scope.firstName = null;
		$scope.lastName = null;
		$scope.userName = null;
		$scope.phone = null;
		$scope.email = null;
		$scope.address = null;
		$scope.zipcode = null;
		
	}
	
	$scope.resetPageForEditUser = function(){
		
		$scope.firstName = null;
		$scope.lastName = null;
		$scope.phone = null;
		$scope.email = null;
		$scope.address = null;
		$scope.zipcode = null;
		
	}
	
}]);