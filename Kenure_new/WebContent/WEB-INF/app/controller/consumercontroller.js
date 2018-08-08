/**
 * 
 */


kenureApp.controller('consumercontroller', ['$scope','$http','$location','$window','$filter',function($scope, $http,$location,$window,$filter){
	
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

	$scope.backButton = function(){
		$window.location.href="loginForm";
	}
	
	$scope.consumerProfileInit = function(){
		$http({
			method: "POST",
			url: url + "/consumerProfileInit",
			params:{},
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
				$scope.tariffId = data[0].tariffPlanId;
				$scope.activeStatus = data[0].activeStatus;
				
				if($scope.activeStatus){
					$scope.activeStatus = "ACTIVE";
				}else{
					$scope.activeStatus = "INACTIVE";
				}
				
				$scope.tariffList = data[1];
				
			}
		}).error(function(data,status){

		});
	}


	$scope.updateConsumerProfile = function(){

		var consumerId = $scope.consumerId;
		var consumerAccountNumber = $scope.consumerAccountNumber;
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
		var activeStatus = $scope.activeStatus;
		var tariffid = $scope.tariffId;
		
		var isEmail2Valid = false;
		var isEmail3Valid = false;
		
		var pattern = /^[a-zA-Z]+[a-zA-Z0-9._]+@[A-Za-z]+\.[a-zA-Z.]{2,5}$/;
		
		if(email2.trim()){
			
			if(pattern.test(email2)){
				isEmail3Valid = pattern.test(email3);
				isEmail2Valid = true;
			}
			
		}
		if(email3.trim()){
			
			if(pattern.test(email3)){
				isEmail2Valid = pattern.test(email2);
				isEmail3Valid = true;
			}
			
		}
		
		if(!email2.trim()){
			isEmail2Valid = true;
		}
		if(!email3.trim()){
			isEmail3Valid = true;
		}
		
		
		if(isEmail2Valid == true && isEmail3Valid == true){
		
			$http({
				method: "POST",
				url:url+"/updateConsumerProfileDetails",
				params:{ consumerId : consumerId,
					consumerAccountNumber:consumerAccountNumber,
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
					email2:email2,
					email3:email3,
					email1:email1,
					activeStatus:activeStatus,
					tariffid:tariffid},
			
			}).success(function(data){
				
				$window.location.href="consumerProfile";
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
	
	//******************************
	
	
	$scope.initConsumerRegistration = function(){
		
		$scope.errorAlert = false;
		$scope.custCodeError = false;
		$scope.alreadyRegistered = false;
		$scope.zipcodeError = false;
		
		$http({
		
			method: "POST",
			url: url + "/initCustomerCodeData",
		
		}).success(function(data){
			
			$scope.customerCodeList = data[0];
			
		}).error(function (data,status){
	
		});
	
	}

	$scope.validateConsumer = function(){
		
		var customerCode = $scope.customerCode;
		var consumerAccountNumber = $scope.consumerAccountNumber;
		var zipcode = $scope.zipcode;

		if(customerCode != null){	
			$http({
				method: "POST",
				url: url + "/validateConsumerRegistration",
				params:{
					customerCode : customerCode,
					consumerAccountNumber : consumerAccountNumber,
					zipcode : zipcode
				},
			}).success(function(data){
				
				if(angular.equals(data,'nosuchconsumerfound')){
				
					$scope.errorAlert = true;
					
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.errorAlert = false;
								});
							}, 3000);
				
				}
				
				if(angular.equals(data,'consumeralreadyregistered')){
					
					$scope.alreadyRegistered = true;
					
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.alreadyRegistered = false;
								});
							}, 3000);
				
				}
				
				if(angular.equals(data,'nosuchzipcodefound')){
					
					$scope.zipcodeError = true;
					
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.zipcodeError = false;
								});
							}, 3000);
				
				}
				
				
				if(angular.equals(data,'validconsumer')){
					
					$window.location.href="consumerDetailsRegistration";
					
				}
				
			}).error(function(data,status){
	
			});

		} else {
			$scope.custCodeError = true;
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.custCodeError = false;
						});
					}, 3000);
		}
	}
	
	
	$scope.initConsumerRegiPage = function(){
		
		$scope.isUserNameExist = false;
		$scope.isUserNameEmpty = false;
		
		$http({
		
			method: "POST",
			url: url + "/initConsumerRegiPage",
		
		}).success(function(data){

			$scope.consumerAccountNumber = data[0].consumerAccountNumber;
			$scope.customerCode = data[0].customerCode;
			$scope.address1 = data[0].address1;
			$scope.address2 = data[0].address2;
			$scope.address3 = data[0].address3;
			$scope.streetName = data[0].streetName;
			
		}).error(function (data,status){
	
		});
	
	}
	
	
	$scope.registerConsumerUser = function(){

		var customerCode = $scope.customerCode;
		var consumerAccountNumber = $scope.consumerAccountNumber;
		var firstName= $scope.firstName;
		var lastName = $scope.lastName;
		var userName = $scope.userName;
		var cell_number1 = $scope.cell_number1;
		var cell_number2 = $scope.cell_number2;
		var cell_number3 = $scope.cell_number3;
		var email1 = $scope.email1;
		var email2 = $scope.email2;
		var email3 = $scope.email3;
		var address1 = $scope.address1;
		var address2 = $scope.address2;
		var address3 = $scope.address3;
		var streetName = $scope.streetName;
		var zipcode = $scope.zipcode;

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
				url: url + "/registerNewConsumer",
				params :{
					consumerAccountNumber : consumerAccountNumber,
					firstName : firstName,
					lastName : lastName, 
					userName : userName,
					cell_number1 : cell_number1,
					cell_number2:cell_number2,
					cell_number3:cell_number3,
					address1 : address1,
					address2:address2,
					address3:address3,
					streetName : streetName,
					zipcode : zipcode,
					customerCode: customerCode,
					email1 : email1,
					email2:email2,
					email3:email3},
	
			}).success(function (data) {
	
				if(angular.equals(data,'nullusername')){
					$scope.isUserNameEmpty=true;
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.isUserNameEmpty = false;
								});
							}, 3000);
				}
				if(angular.equals(data,'usernameerror')){
	
					$scope.isUserNameExist=true;
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.isUserNameExist = false;
								});
							}, 3000);
					
	
				}else if(angular.equals(data,'added')){
	
					$window.location.href="loginForm";
	
				}
			}).error(function (data, status) {
				$scope.response = data;
				$scope.postStatus = 'error: ' + status;
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
	
	$scope.resetPage = function(){
		
		$scope.firstName = null;
		$scope.lastName = null;
		$scope.cell_number1 = null;
		$scope.email1 = null;
		$scope.address1 = null;
		$scope.streetName = null;
		$scope.zipcode = null;
		
	}

	// Dhaval Code Start
	
	$scope.initConsumerDashboardData = function(){
		
		$scope.endPointDropDown = false;
		
		$http({
			method: "POST",
			url: url + "/initConsumerDashboardData",
		}).success(function(data){
			
			var endPointList = data[0];
			var count = Object.keys(endPointList).length;
			
			$scope.notificationData = data[1];
			not(data[1]);
			$scope.notificationDataCount = 0;
			angular.forEach(data,function(value,index){
				$scope.notificationDataCount = $scope.notificationDataCount + 1;
			})
			
			if(count > 6){
				$scope.endPointList = endPointList;
				$scope.endPointDropDown = true;
			}
			
			if(count == 6){
				
				$scope.totalConsumption = data[0].totalConsumption ;
				$scope.estimatedBill = data[0].estimatedBill ;
				$scope.lastBillData = data[0].lastBillData ;
				$scope.currentRate = data[0].currentRate ;
				$scope.futureRate = data[0].futureRate ;
				$scope.nextBillingDate = data[0].nextBillingDate ;
				$scope.endPointDropDown = false;
			
				var currentRate = $scope.currentRate;
				var futureRate = $scope.futureRate;
				var nextBillingDate = $scope.nextBillingDate;
				
				if(currentRate == futureRate){
					$scope.tariffStatus = "Reporting : You are currently in Tariff "+ currentRate +". If your maintain average daily consumption until "+ nextBillingDate +".";
				} else if(currentRate < futureRate){
					$scope.tariffStatus = "Reporting : You are currently in Tariff "+ currentRate +" and you will move to Tariff "+ futureRate +" on "+ nextBillingDate +". If your current average daily consumption continues.";
				}
			}
		}).error(function (data,status){
	
		});
		
	}
	
	$scope.showDashboardData = function(){
		
		var registerId = $scope.selectedEndpoint;
		
		$http({
			method: "POST",
			url: url + "/showDashboardData",
			params :{registerId : registerId},
		}).success(function(data){
			
			$scope.totalConsumption = data[0].totalConsumption;
			$scope.estimatedBill = data[0].estimatedBill;
			$scope.lastBillData = data[0].lastBillData;
			$scope.currentRate = data[0].currentRate ;
			$scope.futureRate = data[0].futureRate ;
			$scope.nextBillingDate = data[0].nextBillingDate ;
			
			var currentRate = $scope.currentRate;
			var futureRate = $scope.futureRate;
			var nextBillingDate = $scope.nextBillingDate;
			
			if(currentRate == futureRate){
				$scope.tariffStatus = "Reporting : You are currently in Tariff "+ currentRate +". If your maintain average daily consumption until "+ nextBillingDate +".";
			} else if(currentRate < futureRate){
				$scope.tariffStatus = "Reporting : You are currently in Tariff "+ currentRate +" and you will move to Tariff "+ futureRate +" on "+ nextBillingDate +". If your current average daily consumption continues.";
			}
			
		}).error(function (data,status){
	
		});
		
	}
	
	// Dhaval Code end
	
}]);