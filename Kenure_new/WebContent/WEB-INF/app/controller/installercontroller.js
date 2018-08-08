/**
 * 
 */


kenureApp.controller('installercontroller', ['$scope','$http','$location','$window','$filter',function($scope, $http,$location,$window,$filter){
	
	$scope.message;
	$scope.showMessage = false;
	$scope.errMessage;
	$scope.showErrorMessage = false;
	$scope.paginationList = [10,20,30,40,50];
	$scope.emailFormat = /^[a-zA-Z]+[a-zA-Z0-9._]+@[a-zA-Z]+\.[a-zA-Z.]{2,5}$/;
	
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

	
	$scope.installerProfileInit = function(){
		$http({
			method: "POST",
			url: url + "/installerProfileInit",
			params:{},
		}).success(function(data){
			if(angular.equals(data,'nosuchinstallerfound')){
				// code for displaying error
			}else{
				$scope.installerId = data[0].installerId;
				$scope.firstName = data[0].firstName;
				$scope.lastName = data[0].lastName;
				$scope.userName = data[0].userName;
				$scope.address1 = data[0].address1;
				$scope.address2 = data[0].address2;
				$scope.address3 = data[0].address3;
				$scope.streetName = data[0].streetName;
				$scope.zipcode = data[0].zipcode;
				$scope.cell_number1 = data[0].cell_number1;
				$scope.cell_number2 = data[0].cell_number2;
				$scope.cell_number3 = data[0].cell_number3;
				$scope.email1 = data[0].email1;
				$scope.email2 = data[0].email2;
				$scope.email3 = data[0].email3;
				
				/*if($scope.activeStatus){
					$scope.activeStatus = "ACTIVE";
				}else{
					$scope.activeStatus = "INACTIVE";
				}*/
				
			}
		}).error(function(data,status){

		});
	}


	$scope.updateInstallerProfile = function(){

		var installerId = $scope.installerId;
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
		
		var isEmail2Valid = false;
		var isEmail3Valid = false;
		
		var pattern = /^[a-zA-Z]+[a-zA-Z0-9._]+@[A-Za-z]+\.[a-zA-Z.]{2,5}$/;
		
		if(email2){
			
			if(pattern.test(email2)){
				isEmail3Valid = pattern.test(email3);
				isEmail2Valid = true;
			}
			
		}
		if(email3){
			
			if(pattern.test(email3)){
				isEmail2Valid = pattern.test(email2);
				isEmail3Valid = true;
			}
			
		}
		
		if(!email2){
			isEmail2Valid = true;
		}
		if(!email3){
			isEmail3Valid = true;
		}
		
		if(isEmail2Valid == true && isEmail3Valid == true){
		
			$http({
				method: "POST",
				url:url+"/updateInstallerProfile",
				params:{ installerId : installerId,
					firstName:firstName,
					lastName:lastName,
					userName:userName,
					address1:address1,
					address2:address2,
					address3:address3,
					streetName:streetName,
					zipcode:zipcode,
					cell_number1:cell_number1,
					cell_number2:cell_number2,
					cell_number3:cell_number3,
					email2:email2,
					email3:email3,
					email1:email1},
			
			}).success(function(data){
				
				$window.location.href="installerProfile";
				//	$state.transitionTo('userManagement'); //back to same page again
			}).error(function(data,status){
				$scope.isError=true;
				$scope.errorMessage="Error while fetching data.";
			});
			
		}else{
			
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
	
	
	
	
	
	
	
}]);