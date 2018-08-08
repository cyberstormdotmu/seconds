/**
 *
 * @author TatvaSoft
 *
 */

//Defining Angular controller for request handling with Services
kenureApp.controller('logincontroller', ['$scope','$http','$location','$window','$filter','$cookies',function($scope,$http,$location,$window,$filter,$cookies){

	$scope.selectedConsumers=[];
	$scope.currentCustomerId;
	$scope.message;
	$scope.showMessage = false;
	$scope.errMessage;
	$scope.showErrorMessage = false;
	$scope.paginationList = [10,20,30,40,50];
	$scope.emailFormat = /^[a-zA-Z]+[a-zA-Z0-9._]+@[a-zA-Z]+\.[a-zA-Z.]{2,5}$/;
	
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

	var Base64 = {

			_keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

			encode : function (input) {
			    var output = "";
			    var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
			    var i = 0;

			    input = Base64._utf8_encode(input);

			    while (i < input.length) {

			        chr1 = input.charCodeAt(i++);
			        chr2 = input.charCodeAt(i++);
			        chr3 = input.charCodeAt(i++);

			        enc1 = chr1 >> 2;
			        enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
			        enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
			        enc4 = chr3 & 63;

			        if (isNaN(chr2)) {
			            enc3 = enc4 = 64;
			        } else if (isNaN(chr3)) {
			            enc4 = 64;
			        }

			        output = output +
			        this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) +
			        this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);

			    }

			    return output;
			},

			decode : function (input) {
			    var output = "";
			    var chr1, chr2, chr3;
			    var enc1, enc2, enc3, enc4;
			    var i = 0;

			  if (input != undefined && input.length > 0) {
			    	
			    input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

			    while (i < input.length) {

			        enc1 = this._keyStr.indexOf(input.charAt(i++));
			        enc2 = this._keyStr.indexOf(input.charAt(i++));
			        enc3 = this._keyStr.indexOf(input.charAt(i++));
			        enc4 = this._keyStr.indexOf(input.charAt(i++));

			        chr1 = (enc1 << 2) | (enc2 >> 4);
			        chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			        chr3 = ((enc3 & 3) << 6) | enc4;

			        if (chr1 !=0) {
				        output = output + String.fromCharCode(chr1);
			        }

			        if (enc3 != 64 & chr2 !=0) {
			            output = output + String.fromCharCode(chr2);
			        }
			        if (enc4 != 64 & chr3 !=0) {
			            output = output + String.fromCharCode(chr3);
			        }

			    }

			    output = Base64._utf8_decode(output);
			  }
			    
			    return output.trim();

			},

			_utf8_encode : function (string) {
			    string = string.replace(/\r\n/g,"\n");
			    var utftext = "";

			    for (var n = 0; n < string.length; n++) {

			        var c = string.charCodeAt(n);

			        if (c < 128) {
			            utftext += String.fromCharCode(c);
			        }
			        else if((c > 127) && (c < 2048)) {
			            utftext += String.fromCharCode((c >> 6) | 192);
			            utftext += String.fromCharCode((c & 63) | 128);
			        }
			        else {
			            utftext += String.fromCharCode((c >> 12) | 224);
			            utftext += String.fromCharCode(((c >> 6) & 63) | 128);
			            utftext += String.fromCharCode((c & 63) | 128);
			        }

			    }

			    return utftext;
			},

			_utf8_decode : function (utftext) {
			    var string = "";
			    var i = 0;
			    var c = c1 = c2 = 0;

			    while ( i < utftext.length ) {

			        c = utftext.charCodeAt(i);

			        if (c < 128) {
			            string += String.fromCharCode(c);
			            i++;
			        }
			        else if((c > 191) && (c < 224)) {
			            c2 = utftext.charCodeAt(i+1);
			            string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
			            i += 2;
			        }
			        else {
			            c2 = utftext.charCodeAt(i+1);
			            c3 = utftext.charCodeAt(i+2);
			            string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
			            i += 3;
			        }

			    }

			    return string;
			}

		}


	$scope.adminProfileInit = function(){
		$http({
			method: "POST",
			url: url + "/adminProfileInit",
			params:{},
		}).success(function(data){
			if(angular.equals(data,'nosuchuserfound')){
				// code for displaying error
			}else{
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

			}
		}).error(function(data,status){

		});
	}


	$scope.updateAdminProfile = function(){

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
				url:url+"/updateAdminProfile",
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
					email2:email2,
					email3:email3,
					email1:email1},

			}).success(function(data){

				$window.location.href="adminProfile";
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
	
	$scope.remember = Boolean($cookies.get('remember'));
	$scope.userName = Base64.decode($cookies.get('uname'));
	$scope.password = Base64.decode($cookies.get('psw'));

	// angular method for authentication and firstTime login check
	$scope.loginUser = function(){

		var userName = $scope.userName;
		var password = $scope.password;
		var login = {userName : $scope.userName,password : $scope.password};
		if( userName == null || password == null || userName == "" || password == "" ){

			$scope.error = true;
			setTimeout(function () 
					{
				$scope.$apply(function()
						{
					$scope.error = false;
						});
					}, 3000);
			$scope.authenticationError = false;
			$scope.success = false;
		} else {

			$http({
				method: "POST",
				url: url + "/authenticateUser",
				params :{userName : $scope.userName,password : $scope.password},

			}).success(function (data) {
				
				if ($scope.remember) {
					var now = new Date(),
				    
					// Set the expiration to 10 years
				    exp = new Date(now.getFullYear()+10, now.getMonth(), now.getDate());
					
					$cookies.put('uname', Base64.encode($scope.userName),{
						  expires: exp
					});
					$cookies.put('psw', Base64.encode($scope.password),{
						  expires: exp
					});
					$cookies.put('remember', $scope.remember,{
						  expires: exp
					});
		        } else {
		        	if (Base64.encode($scope.userName) == $cookies.get('uname')) {
		        		$cookies.remove('uname');
		        		$cookies.remove('psw');
		        		$cookies.remove('remember');
		        	}
		        }
				
				if(angular.equals(data,'authorizationfailed')){
					$scope.notAuthorizedError = true;
				}
				
				if(angular.equals(data,'inactiveDataPlan')){
					$scope.inactiveDataPlan = true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.inactiveDataPlan = false;
								});
							}, 3000);
				}
				
				if(angular.equals(data,'inactivePortalPlan')){
					$scope.inactivePortalPlan = true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.inactivePortalPlan = false;
								});
							}, 3000);
				}
				
				if(angular.equals(data,'inactiveuser')){
					$scope.inactiveUser = true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.inactiveUser = false;
								});
							}, 3000);
				}

				if(angular.equals(data,'authenticationfailed')){
					$scope.authenticationError = true;
					$scope.success = false;
					$scope.error = false;
				}

				if(angular.equals(data, 'isFirstTImeLoginTrue')){
					$window.location.href="resetPasswordForm";
				}
				if(angular.equals(data, 'loginAsCustomer')){
					$window.location.href="switchUser";
				}
				if(angular.equals(data, 'loginAsConsumer')){
					$window.location.href="switchUser";
				}
				if(angular.equals(data, 'isFirstTImeLoginFalse')){
					$window.location.href="loginForm";
				}
				/*if(angular.equals(data, 'customer')){
					$window.location.href="customerDashboard";
				}*/
				if(angular.equals(data, 'consumer')){
					$window.location.href="consumerDashboard";
				}
				if(angular.equals(data, 'installer')){
					$window.location.href="installerDashboard";
				}

			}).error(function (data, status) {

				$scope.response = data;
				$scope.postStatus = 'error: ' + status;
			});
		}
	};


	// angular method for Forget password functionality
	$scope.forgetpwd = function(){
		var email = $scope.email;
		var userName = $scope.userName;
		
		if( email == null || userName == null || email == "" || userName == "" ){

			$scope.error = true;
			setTimeout(function () 
					{
				$scope.$apply(function()
						{
					$scope.error = false;
						});
					}, 3000);

		}else{

			$http({
				method: "POST",
				url: url + "/sendMail",
				params :{email : email, userName : userName},

			}).success(function (data) {
				$scope.error = false;
				$scope.userNotAvailable = false;
				$scope.emailNotAvailable = false;
				$scope.sendEmailSuccess = false;
				$scope.sendmailEroor = false;
				$scope.mailSend= false;

				if(angular.equals(data, 'mailError')){
					$scope.sendmailEroor = true;
				}
				if(angular.equals(data, 'validationfailed')){
					$scope.validationfailed = true;
				}
				if(angular.equals(data, 'emailNotAvailable')){
					$scope.emailNotAvailable = true;
				}
				if(angular.equals(data, 'userNotAvailable')){
					$scope.userNotAvailable = true;
				}
				if(angular.equals(data, 'mailSend')){
					$scope.mailSend=true;
					$window.location.href="loginForm";

				}
				$scope.response = data;
			}).error(function (data, status) {
				$scope.response = data;
			});
		}
	};


	// Angular method for Reseting password
	$scope.resetpassword = function(){
		var newpwd = $scope.newpwd;
		var confirmnewpwd = $scope.confirmnewpwd;

		if(confirmnewpwd == newpwd){

			$scope.mismatchpassword = false;

			$http({
				method: "POST",
				url: url + "/resetpassword",
				params :{oldPassword : $scope.oldpwd, newPassword : $scope.newpwd, confirmnewpwd:$scope.confirmnewpwd},

			}).success(function (data) {
				if(angular.equals(data, 'adminpwdchangedSuccessfully')){
					$window.location.href="adminDashboard";

				}if(angular.equals(data, 'customerpwdchangedSuccessfully')){
					$window.location.href="customerDashboard";

				}if(angular.equals(data, 'consumerpwdchangedSuccessfully')){
					$window.location.href="consumerDashboard";

				}if(angular.equals(data, 'installerpwdchangedSuccessfully')){
					$window.location.href="installerDashboard";

				}if(angular.equals(data, 'passwordIsIncorrect')){
					$scope.passwordIsIncorrect = true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.passwordIsIncorrect = false;
								});
							}, 3000);


				}if(angular.equals(data,'passwordNotMatch')){
					$scope.passwordNotMatch = true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.passwordNotMatch = false;
								});
							}, 3000);

				}

				$scope.response = data;
			}).error(function (data, status) {
				$scope.response = data;
			});
		} else {
			$scope.mismatchpassword = true;
			setTimeout(function () 
					{
				$scope.$apply(function()
						{
					$scope.mismatchpassword = false;
						});
					}, 3000);
		}
	}

	$scope.backButton = function(){
		$window.location.href="loginForm";
	}


	// Angular method for getting dashboard data for all dashboard jsp on Initilization
	$scope.init = function(){

		// Setting initial pagination limit
		$scope.paginationList = [10,20,30,40,50];
		$scope.recordSize = 10;

		$scope.isError = false;
		$scope.errorMessage="";
		$scope.searchCompleted=false;
		$scope.noteUploadCompleted = false;

		$http({
			method: "POST",
			url: url + "/adminOperation/userDashboardData",
		}).success(function (data) {
			$scope.userListDiv = true;
			$scope.noUserFoundDiv = false;
			$scope.adminUser = data[0];
			$scope.customerList = data[1];
			$scope.dataPlanList = data[2];
			$scope.limit = 10;
			$scope.begin = 0;
			
			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.customerList.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show

			if(angular.equals(data,'userRoleNotMatch')){
				$window.location.href="loginForm";

			}
		}).error(function (data,status){

		});
	}

	//getting list of Data plan
	$scope.initDataplan = function(){
		$http({
			method: "GET",
			url: url + "/adminOperation/getDataPlan",
		}).success(function (data) {
			$scope.dataPlanListDiv = true;
			$scope.noDataPlanFoundDiv = false;
			$scope.addDataPlan = true;
			$scope.dataPlanList = data;
			$scope.limit = 10;
			$scope.begin = 0;
			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.dataPlanList.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show

			if(angular.equals(data,'youCanNotAccessDataPlanList')){
				$window.location.href="loginForm";
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

	$scope.open4 = function() {
		$scope.popup4.opened = true;
	};
	
	$scope.open5 = function() {
		$scope.popup5.opened = true;
	};
	
	$scope.open6 = function() {
		$scope.popup6.opened = true;
	};
	
	$scope.open7 = function() {
		$scope.popup7.opened = true;
	};

	$scope.openSearch1 = function() {
		$scope.popupSearch1.opened = true;
	};

	$scope.setDate = function(year, month, day) {
		$scope.activedate = new Date(year, month, day);
		$scope.expirydate = new Date(year, month, day);
	};

	$scope.formats = ['yyyy-MM-dd HH:mm:ss', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'MM-dd-yyyy'];
	$scope.format = $scope.formats[4];
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

	$scope.popup4 = {
			opened: false
	};
	
	$scope.popup5 = {
			opened: false
	};
	
	$scope.popup6 = {
			opened: false
	};
	
	$scope.popup7 = {
			opened: false
	};

	$scope.popupSearch1 = {
			opened: false
	};

	// Datepicker code ends

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


	// Angular method for Adding new dataPlan
	$scope.insertDataPlan = function(){

		$scope.isDataPlanExist = false;

		if($scope.mbPerMonth!= null){
			$http({
				method: "GET",
				url: url + "adminOperation/addDataPlan",
				params: {dataplan:$scope.mbPerMonth}
			}).success(function (data) {
				if(angular.equals(data,'dataPlanSuccessfullyadded')){

					$scope.isSuccess = true;
					$scope.success="Record added. Redirecting to main page."

						window.setTimeout(function() {
							$window.location.href="dataPlanManagement";
						}, 3000);
					

				}
				if(angular.equals(data,'dataplanalreadyexist')){
					$scope.isDataPlanExist=true;
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.isDataPlanExist = false;
								});
							}, 3000);
				}
				if(angular.equals(data,'youCanNotAccessDataPlanList')){
					$window.location.href="loginForm";
				}
				$scope.response = data;
			}).error(function (data,status){

			});
		}
	}


	// Angular method for Get Data Plan details for Edit
	$scope.editDataPlan = function(dataPlanId){

		$http({
			method: "GET",
			url: url + "/adminOperation/editDataPlan",
			params:{dataPlanId : dataPlanId},
		}).success(function(data) {
			//$scope.editDataPlanDiv = true;
			//$scope.addDataPlan = false;
			$window.location.href="editDataPlanForm";
			//$scope.dataPlan = JSON.parse(data[0]);

		}).error(function (data,status){

		});
	}

	$scope.getEditDataPlan = function(){
		//First make ajax call and get that sessioned user entity.
		$scope.isDataPlanExist=false;
		
		$http({
			method: "POST",
			url: url+"/adminOperation/getMyData",
		}).success(function(data){
			if(angular.equals(data,'nodataPlanFound')){
				$scope.addDataPlanDiv = true;
			}else{
				$scope.editDataPlanDiv = true;
				$scope.customerDivList = false;
				var dataPlan = data[0];
				$scope.mbPerMonth = dataPlan.mbPerMonth;
				$scope.dataPlanId = dataPlan.dataPlanId;

			}
		}).error(function(data,status){

		});
	}

	// Angular method for Updating Data Plan
	$scope.updateDataPlan = function(dataPlanId){

		var mbPerMonth = $scope.mbPerMonth ; 
		
		$http({
			method: "GET",
			url: url + "/updateDataPlan",
			params: {mbPerMonth : mbPerMonth,
				dataplanId: dataPlanId
			}
		}).success(function (data) {
			if(angular.equals(data,'dataPlanUpdatedSuccessfully')){

				$scope.isSuccess = true;
				$scope.success="Record updated. Redirecting to main page."

					window.setTimeout(function() {
						$window.location.href="dataPlanManagement";
					}, 3000);

			}
			if(angular.equals(data,'dataplanalreadyexist')){
				$scope.isDataPlanExist=true;
				setTimeout(function ()
						{
					$scope.$apply(function()
							{
						$scope.isDataPlanExist = false;
							});
						}, 3000);
			}
		}).error(function (data,status){

		});



	}

	$scope.searchDataPlan = function(){

		var searchInput = $scope.searchInput;
		
		if($scope.searchInput!= null){
			$http({
				method: "POST",
				url: url + "/searchDataPlan",
				params: {searchInput : searchInput},
	
			}).success(function (data) {
				if(angular.equals(data,'nosuchuserfound')){
					$scope.noDataPlanFoundDiv = true;
					$scope.dataPlanListDiv = false;
	
				} else if(angular.equals(data,'emptyValue')){
					
				}else{
					$scope.noDataPlanFoundDiv = false;
					$scope.dataPlanListDiv = true;
					$scope.dataPlanList = data;
					
					$scope.limit = 10;
					$scope.begin = 0;
					// For pagination
					var keys = Object.keys($scope.dataPlanList);
					var length = keys.length;
	
					// Max list calculations
					$scope.totalPage = Math.ceil(length/$scope.limit); //Here 10 is default records to be appeared
				}
			}).error(function (data,status){
	
			});
		}
	}

	// Angular method for init method for customer dashboard
	$scope.initCustomerDashboard = function(){
		$http({
			method: "POST",
			url: url + "/userDashboardData",
		}).success(function (data) {
			$scope.userListDiv = true;
			$scope.noUserFoundDiv = false;
			//$scope.customerUser = JSON.parse(data[0]);
			$scope.contactDetails = JSON.parse(data[0]);
			$scope.customer = JSON.parse(data[1]);
			if(angular.equals(data,'userRoleNotMatch')){
				$window.location.href="loginForm";
			}

		}).error(function (data,status){

		});
	}


	// Angular method for Updating Customer Profile by Customer
	$scope.updateProfile = function(){

		$http({
			method: "POST",
			url: url + "/updateProfile",

			params:{firstName : $scope.contactDetails.firstName,
				address1 : $scope.contactDetails.address1,
				address2 : $scope.contactDetails.address2,
				address3 : $scope.contactDetails.address3,
				streetname : $scope.contactDetails.streetName,
				cell_number1 : $scope.contactDetails.cell_number1,
				email1 : $scope.contactDetails.email1,
				email2 : $scope.contactDetails.email2,
				email3 : $scope.contactDetails.email3

			}
		}).success(function (data) {
			if(angular.equals(data, 'updatedSuccessfully')){
				$scope.updatedSuccessfully = true;
				$window.location.href="customerDashboard";
			}

		}).error(function (data,status){

		});
	}

	//  Angular method for confirm Delete User popup
	$scope.removeRow = function(id){

		swal({
			   title: 'Confirm',
			   text: 'Want to delete Customer with ID : '+ id+' ?',
			   type: 'warning',
			   showCancelButton: true,
			   confirmButtonText: 'Yes',
			   cancelButtonText: 'Cancel'
			}, function(){
				
			$http({
				method: "POST",
				url: url + "/adminOperation/deleteUser",
				params:{userId : id},
			}).success(function(data) {
				if(angular.equals(data,'userismapped')){
					$scope.isError=true;
					$scope.errorMessage = "User is mapped with other object. Can't delete";
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.isError = false;
								});
							}, 3000);
				}
				if(angular.equals(data,'userdeletedsuccessfully')){
					
					$scope.success="User successfully deleted... Reloading list...";
					$scope.isSuccess=true;

						window.setTimeout(function() {
							window.location.href = "userManagement";
						}, 3000);
				}

				// reload to reflect changes on UI side
			});
		});

	}

	// Angular method for Search Customer with different criteria
	$scope.searchCustomer = function(){

		var customerNameCriteria = $scope.customerNameCriteria;
		var dataplanActivedate = $filter('date')($scope.dataplanActivedate,"yyyy-MM-dd HH:mm:ss");

		/*var meterReadingDate = $filter('date')(new Date($scope.meterReadingDate),"yyyy-MM-dd HH:mm:ss");*/

		var dataplanExpirydate = $filter('date')($scope.dataplanExpirydate,"yyyy-MM-dd HH:mm:ss");
		var portalplanActiveDate = $filter('date')($scope.portalplanActivedate,"yyyy-MM-dd HH:mm:ss");
		var portalplanExpirydate = $filter('date')($scope.portalplanExpirydate,"yyyy-MM-dd HH:mm:ss");

		if(angular.isUndefined(customerNameCriteria) && angular.isUndefined(dataplanActivedate)
				&& angular.isUndefined(dataplanExpirydate) &&angular.isUndefined(portalplanActiveDate)
				&& angular.isUndefined(portalplanExpirydate)){
			//$scope.errorMessage="Enter Proper Search Criteria !!";
			$scope.noUserFoundDiv = true;

		}else if($scope.dataplanActivedate > $scope.dataplanExpirydate){
			$scope.errorMessage="Data Plan Expiry Date can't be small than Data Plan Active Date";
			$scope.isError = true;

			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isError = false;
						});
					}, 3000);


		}else if($scope.portalplanActiveDate > $scope.portalplanExpirydate){
			$scope.errorMessage="Portal Plan Expiry Date can't be small than Portal Plan Active Date";
			$scope.isError = true;
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isError = false;
						});
					}, 3000);
		}

		else{

			$http({
				method: "POST",
				url: url + "/searchCustomer",
				params: {customerNameCriteria:customerNameCriteria, dataPlanActiveDate:dataplanActivedate,
					dataPlanExpiryDate:dataplanExpirydate,portalPlanActiveDate:portalplanActiveDate,
					portalPlanExpiryDate:portalplanExpirydate},
			}).success(function (data) {

				if(angular.equals(data,'nosuchuserfound')){
					$scope.noUserFoundDiv = true;
					$scope.userListDiv = false;
				}else{
					$scope.noUserFoundDiv = false;
					$scope.userListDiv = true;

					$scope.customerList = data;

					$scope.limit = 10;
					$scope.begin = 0;
					// For pagination
					var keys = Object.keys($scope.customerList);
					var length = keys.length;

					// Max list calculations
					$scope.totalPage = Math.ceil(length/$scope.limit); //Here 10 is default records to be appeared
				}
			}).error(function (data,status){

			});
		}

	}


	// Angular method for Get Customer list in Login Selection drop down
	$scope.getCustomerList = function(){
		
		$http({
			method: "GET",
			url: url + "/getCustomerListData",
		}).success(function (data) {
			$scope.suggestedCustomerList = data;

		}).error(function (data,status){

		});
	}

	// Angular method for Admin login as customer
	$scope.loginAsCustomer = function(){
		if($scope.selected.customerName === undefined){
			$scope.error = true;
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.error = false;
						});
					}, 3000);
		}else{
			$http({
				method: "GET",
				url: url + "/loginAsCustomer",
				params: {userId : $scope.selected.customerName}
			}).success(function (data) {
				$scope.error = false;
				if(angular.equals(data, 'loginAsCustomer')){
					$window.location.href="customerDashboard";
				}
			}).error(function (data,status){

			});
		}
		
	}

	// Angular method for Customer login as consumer
	$scope.loginAsConsumer = function(){
		if($scope.selected.consumerAccNo === undefined){
			$scope.error = true;
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.error = false;
						});
					}, 3000);
		}else{
		$http({
			method: "GET",
			url: url + "/loginAsConsumer",
			params: {userId : $scope.selected.consumerAccNo}
		}).success(function (data) {
			$scope.error = false;
			if(angular.equals(data, 'loginAsConsumer')){
				$window.location.href="consumerDashboard";
			}
		}).error(function (data,status){

		});
		}
	}


	// Angular method for Search User by Username
	$scope.search = function(){
		var customerSearchCriteria = $scope.customerInput;

		$http({
			method: "POST",
			url: url + "/searchCustomer",
			params: {customerSearchCriteria:customerSearchCriteria},
		}).success(function (data) {
			if(angular.equals(data,'nosuchuserfound')){
				$scope.noUserFoundDiv = true;
				$scope.userListDiv = false;
			}else{
				$scope.noUserFoundDiv = false;
				$scope.userListDiv = true;
				$scope.userList = data;

				$scope.limit = 10;
				$scope.begin = 0;
				// For pagination
				var keys = Object.keys($scope.userList);
				var length = keys.length;

				// Max list calculations
				$scope.totalPage = Math.ceil(length/$scope.limit); //Here 10 is default records to be appeared
			}
		}).error(function (data,status){

		});

	}
	
	// Angular method for Get Consumer list in Login Selection drop down
	$scope.getConsumerUserList = function(){

		$http({
			method: "POST",
			url: url+"/customerOperation/consumerUserManagementData",
		}).success(function (data) {
			$scope.suggestedConsumerList=data[0];
			angular.forEach(data,function(value,index){
				$scope.suggestedConsumerList=value;
			});
		});
	}

	// Angular method for Adding New User
	$scope.addUser = function(){

		var customerCode = $scope.customerCode;
		var firstName= $scope.firstName;
		var lastName = $scope.lastName;
		var userName = $scope.userNameInput;
		var dataplan = $scope.dataplan;
		var addDataplanActivedate = new Date($scope.addDataplanActivedate);
		var dataPlanDuration = $scope.dataPlanDuration;
		var addPortalplanActiveDate = new Date($scope.addPortalplanActiveDate);
		var addPortalplanExpirydate = new Date($scope.addPortalplanExpirydate);
		var phone = $scope.phone;
		var email = $scope.email;
		var address = $scope.address;
		var zipcode = $scope.zipcode;
		var status = $scope.activeStatus;
		var countryId = $scope.selectedCountry;
		var currencyId = $scope.selectedCurrency;
		var selectedTimeZone = $scope.selectedTimeZone;

		if(addDataplanActivedate > addDataplanExpirydate){
			$scope.isActivePlanDateBigger = true;
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isActivePlanDateBigger = false;
						});
					}, 3000);
		}else{
			$scope.isActivePlanDateBigger = false;
		}

		if(dataPlanDuration > 31 && (dataPlanDuration < 48 || dataPlanDuration > 57)){
			return false;
		}
		
		if(addPortalplanActiveDate > addPortalplanExpirydate){
			$scope.isActivePortalDateBigger = true;
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isActivePortalDateBigger = false;
						});
					}, 3000);
		}else{
			$scope.isActivePortalDateBigger = false;
		}
		if($scope.addUserFrom.country.$error.required){
			$scope.isCountry = true;
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isCountry = false;
						});
					}, 3000);
		}else{
			$scope.isCountry = false;
		}
		if($scope.addUserFrom.dataplan.$error.required){
			$scope.isDataPlan = true;
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isDataPlan = false;
						});
					}, 3000);
		}else{
			$scope.isDataPlan = false;
		}
		if($scope.addUserFrom.currency.$error.required){
			$scope.isCurrency = true;
			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isCurrency = false;
						});
					}, 3000);
		}else{
			$scope.isCurrency = false;
		}
		/*if(angular.isUndefined($scope.countryId)){
			$scope.error="Provide Proper Country Address";
			$scope.isCountryUndefined = true;
		}else{

		}*/

		if(!($scope.isActivePlanDateBigger) && !($scope.isActivePortalDateBigger) && !($scope.isCountry) && !($scope.isCurrency)){

			var addDataplanActivedate = $filter('date')(new Date($scope.addDataplanActivedate),"dd/MM/yyyy");
			var addDataplanExpirydate = $filter('date')(new Date($scope.addDataplanExpirydate),"dd/MM/yyyy");
			var addPortalplanActiveDate   = $filter('date')(new Date($scope.addPortalplanActiveDate),"dd/MM/yyyy");
			var addPortalplanExpirydate   = $filter('date')(new Date($scope.addPortalplanExpirydate),"dd/MM/yyyy");

			$http({
				method: "POST",
				url: url + "/insertUser",
				params :{customerCode : customerCode, firstName : firstName, lastName : lastName,
					userName : userName, dataplanActivedate : addDataplanActivedate,
					dataPlanDuration : dataPlanDuration,portalplanActiveDate : addPortalplanActiveDate,portalplanExpirydate : addPortalplanExpirydate,
					phone : phone, email : email, address : address,
					zipcode : zipcode,status:status,dataplan:dataplan,
					countryId:countryId,currencyId:currencyId,selectedTimeZone:selectedTimeZone},
			}).success(function (data) {

				if(angular.equals(data,'customercodeerror')){
					// CustomerCode already exist.Display Error
					$scope.isCodeExist = false;
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.isCodeExist = true;
								});
							}, 3000);
				}else if(angular.equals(data,'usernameerror')){
					// CustomerName already exist.Show error
					$scope.isUserNameExist=false;
					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.isUserNameExist = true;
								});
							}, 3000);
				}else if(angular.equals(data,'added')){
					
					$scope.isSuccess = true;
					$scope.success="Record added. Redirecting To main page."

						window.setTimeout(function() {
							$window.location.href="userManagement";
						}, 3000);
					
				}else{
					$window.location.href="userManagement";
				}

			}).error(function (data, status) {
				$scope.response = data;
				$scope.postStatus = 'error: ' + status;
			});
		}

	};


	
	
	$scope.uploadNote = function(files) {
		
		var fd = new FormData();
	    
		//Take the first selected file
	    fd.append("file", files[0]);
		
	    $http.post( url+"/uploadNote", fd, {
            transformRequest: angular.identity,
            headers: {'Content-Type': undefined}
        })
        .success(function(response) {

            console.log("file uploaded sucessfully " + response);

        })
        .error(function(error) {

            console.log("Error while uploading file " + JSON.stringify(error));
        });

	};



	// Angular method for Redirect to angular route for redirection.
	$scope.editUser = function(customerId){
		$http({
			method: "POST",
			url: url + "/adminOperation/editUser",
			params:{customerId : customerId},
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


	// Angular method for Redirect to angular route for redirection.
	$scope.editConsumer = function(consumerMeterId){
		$http({
			method: "POST",
			url: url + "/editConsumer",
			params:{consumerMeterId:consumerMeterId},
		}).success(function(data){
			if(angular.equals(data,'nosuchuserfound')){
				// code for displaying error
			}else{
				//	$state.transitionTo('editUser');
				$window.location.href="editConsumerPageRedirect";
			}
		}).error(function(data,status){

		});
	}


	// Angular method for EditUser field will be set here
	$scope.getEditUserDetails = function(){

		// Srror is default false but again setting false for no error
		$scope.isError = false;
		$scope.errorMessage="";
		$scope.disableFields = true;
		
		//First make ajax call and get that sessioned user entity.
		$http({
			method: "POST",
			url:url+"/getEditUserEntity",
		}).success(function(data){
			if(angular.equals(data,'nosuchuserfound')){
				// code for displaying error
			}else{
				$scope.customerId = data[0].customerId;
				$scope.customerCode = data[0].customerCode;
				$scope.firstName = data[0].firstName;
				$scope.lastName = data[0].lastName;
				$scope.userName = data[0].userName;
				$scope.phone = data[0].phone;
				$scope.email = data[0].email;
				$scope.address = data[0].address;
				$scope.zipcode = data[0].zipcode;
				$scope.usersId = data[0].userId;
				$scope.activeStatus = data[0].status;
				// Setting 4 dates
				$scope.editDataPlanActiveDate = data[0].activeDate;
				$scope.editPortalActiveDate = data[0].portalStartDate;
				$scope.editPortalExpiryDate = data[0].portalEndDate;

				$scope.countryList = JSON.parse(data[0].countryList);
				$scope.currencyList = JSON.parse(data[0].currencyList);
				$scope.timeZoneList = JSON.parse(data[0].timeZoneList);

				$scope.selectedTimeZone = data[0].selectedTimeZone;
				$scope.selectedCountryId = data[0].selectedCountryId;
				$scope.selectedCurrencyId = data[0].selectedCurrencyId;
				$scope.customerId = data[0].customerId;
				$scope.currentDataPlanId = data[0].currentDataPlanId;
				$scope.dataPlanList = data[1];
				/*$scope.selectedCountry={};
				$scope.selectedCountry.id = angular.fromJson(data.selectedCountry).countryId;
				$scope.selectedCountry.name = angular.fromJson(data.selectedCountry).countryName;

				alert(angular.equals($scope.selectedCountry, $scope.countryList[1]));

				$scope.selectedCurrency = angular.formJson(data.selectedCurrency);*/

			}
		}).error(function(data,status){

		});
	}


	$scope.getInclude = function(){
		if(!$scope.isCustomer){
			return "consumerleftNavigationbar";
		} else {
			return "customerleftNavigationbar";
		}
		return "";
	}


	// Angular method for Update existing customer with new details
	$scope.updateCustomer = function(){

		var customerCode = $scope.customerCode ;
		var firstName = $scope.firstName ;
		var lastname = $scope.lastName;
		var userName = $scope.userName;
		var cell_number1 = $scope.phone;
		var email1 = $scope.email;
		var address1 = $scope.address;
		var zipcode = $scope.zipcode;
		var userId = $scope.usersId ;
		var status = $scope.activeStatus;

		var selectedCurrency = $scope.selectedCurrencyId;
		var selectedCountry = $scope.selectedCountryId;
		var currentDataPlanId = $scope.currentDataPlanId;
		var editDataPlanActiveDate = new Date($scope.editDataPlanActiveDate);
		var dataPlanDuration = $scope.dataPlanDuration;
		var editPortalActiveDate   = new Date($scope.editPortalActiveDate);
		var editPortalExpiryDate   = new Date($scope.editPortalExpiryDate);

		var selectedTimeZone = $scope.selectedTimeZone;
		var customerId = $scope.customerId;
		
		if(dataPlanDuration == "" || dataPlanDuration == 'undefined'){
			return false;
		}else if(editPortalActiveDate > editPortalExpiryDate){
			$scope.errorMessage="Portal Plan Expiry Date can't be small than Portal Plan Active Date";
			$scope.isError = true;

			setTimeout(function ()
					{
				$scope.$apply(function()
						{
					$scope.isError = false;
						});
					}, 3000);

		}else{

			var editDataPlanActiveDate = $filter('date')(new Date($scope.editDataPlanActiveDate),"dd/MM/yyyy");
			//var editDataPlanExpiryDate = $filter('date')(new Date($scope.editDataPlanExpiryDate),"dd/MM/yyyy");
			var editPortalActiveDate   = $filter('date')(new Date($scope.editPortalActiveDate),"dd/MM/yyyy");
			var editPortalExpiryDate   = $filter('date')(new Date($scope.editPortalExpiryDate),"dd/MM/yyyy");

			$http({
				method: "POST",
				url:url+"/updateCustomer",
				params:{customerCode:customerCode, firstName:firstName, lastname:lastname,
					userName:userName, cell_number1:cell_number1, email1:email1,
					address1:address1, zipcode:zipcode, userId:userId, status:status,
					editDataPlanActiveDate:editDataPlanActiveDate, dataPlanDuration:dataPlanDuration,
					editPortalActiveDate:editPortalActiveDate, editPortalExpiryDate:editPortalExpiryDate,
					selectedCountry:selectedCountry,selectedTimeZone:selectedTimeZone,selectedCurrency:selectedCurrency,customerId:customerId,currentDataPlanId:currentDataPlanId},
			}).success(function(data){
				
				if(angular.equals(data,'cannotSetTheSelectedDataplan')){
					$scope.invalidDataPlan = true;

					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.invalidDataPlan = false;
								});
							}, 2000);
				}
				
				if(angular.equals(data,'unvaliddataplanduration')){
					$scope.invalidDuration = true;

					setTimeout(function ()
							{
						$scope.$apply(function()
								{
							$scope.invalidDuration = false;
								});
							}, 2000);
				}
				
				if(angular.equals(data,'successfullyupdated')){
					
					$scope.isSuccess = true;
					$scope.success="Record updated. Redirecting to main page."

						window.setTimeout(function() {
							$window.location.href="adminOperation/userManagement";
						}, 3000);
				}
				
				//	$state.transitionTo('userManagement'); //back to same page again
			}).error(function(data,status){

			});
		}


	}

	
	// Angular method for VPN configuration
	$scope.portalConfig = function(){
		
		$scope.fieldDisable = true;
		$scope.saveEnable = false;
		$scope.updateEnable = true;
		
		$http({
			method: "GET",
			url: url + "/portalConfig",
		}).success(function (data) {
			$scope.vpnDetailsList = data[0];
			$scope.vpnServerName = $scope.vpnDetailsList[0].vpnServerName;
			$scope.vpnUserName = $scope.vpnDetailsList[0].vpnUserName;
			$scope.vpnPassword = $scope.vpnDetailsList[0].vpnPassword;
			$scope.vpnDomainName = $scope.vpnDetailsList[0].vpnDomainName;
			$scope.noOfBytesPerEndpointRead = $scope.vpnDetailsList[0].noOfBytesPerEndpointRead;
			$scope.noOfBytesPerPacket = $scope.vpnDetailsList[0].noOfBytesPerPacket;
			$scope.configId = $scope.vpnDetailsList[0].configId;
			$scope.abnormalThreshold = $scope.vpnDetailsList[0].abnormalThreshold;
		}).error(function (data,status){

		});
	}

	$scope.unableFields = function(){
		
		$scope.fieldDisable = false;
		$scope.saveEnable = true;
		$scope.updateEnable = false;
	}
	
	$scope.saveUpdatePortalConfig = function(){
		
		var configId = $scope.configId;
		var vpnServerName = $scope.vpnServerName ;
		var vpnUserName = $scope.vpnUserName ;
		var vpnPassword = $scope.vpnPassword ;
		var vpnDomainName = $scope.vpnDomainName ;
		var noOfBytesPerEndpointRead = $scope.noOfBytesPerEndpointRead ;
		var noOfBytesPerPacket = $scope.noOfBytesPerPacket ;
		var abnormalThreshold = $scope.abnormalThreshold;
		
		$http({
			method: "POST",
			url: url + "/saveUpdatePortalConfig",
			params:{vpnServerName : vpnServerName, vpnUserName : vpnUserName,
				vpnPassword : vpnPassword, vpnDomainName : vpnDomainName,
				noOfBytesPerEndpointRead : noOfBytesPerEndpointRead, configId : configId,
				noOfBytesPerPacket : noOfBytesPerPacket, abnormalThreshold : abnormalThreshold},
			
		}).success(function (data) {
			
			if(angular.equals(data,'success')){
				$scope.fieldDisable = true;
				$scope.updateEnable = true;
				$scope.saveEnable = false;
			}
		}).error(function (data,status){

		});
	}


	//Get Datplan list
	$scope.getDataPlanList = function(){

		$scope.isUserNameExist=true;
		$scope.isCodeExist=true;
		$scope.selectedTimeZone="-12:00";
		$scope.isCountry = false;
		$scope.isCurrency = false;
		// Initial Custome Active ?
		$scope.activeStatus = "ACTIVE";

		// Setting minimum date to block previous date
		$scope.mindateDataPlan = new Date();
		$http({
			method: "POST",
			url: url + "/dataPlanData",
		}).success(function (data) {

			$scope.isCodeExist=true;
			$scope.isUserNameExist=true;
			$scope.dataPlanList = $filter('orderBy')(data[0], "mbPerMonth");
			$scope.currencyList = data[1];
			$scope.countryList = data[2];
			$scope.timeZoneList = data[3];
		}).error(function (data,status){

		});
	}

	$scope.getSpareDataCollector = function(){
		var defaultPaging = 10;
		$scope.recordSize = defaultPaging.toString();

		$http({
			method: "GET",
			url: url + "/getSpareDataCollector",

		}).success(function (data) {
			$scope.dataCollectorListDiv = true;
			$scope.nodataCollectorFoundDiv = false;
			$scope.addDataCollector = true;
			$scope.dataCollectorList = data;
			$scope.limit = 10;
			$scope.begin = 0;
			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.dataCollectorList.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show

		}).error(function (data,status){

		});
	}

	$scope.editSpareDataCollector = function(datacollectorId){


		$http({
			method: "GET",
			url: url + "/editSpareDataCollector",
			params:{datacollectorId : datacollectorId},
		}).success(function(data) {
			if(angular.equals(data,'NoSuchDataCollectorFound')){
				// code for displaying error
			}else{
				$window.location.href="editSpareDataCollectorForm";
			}

		}).error(function (data,status){

		});


	}

	$scope.getEditdatacollector = function(){
		//First make ajax call and get that sessioned user entity.
		$http({
			method: "POST",
			url: url+ "/getEditdatacollector",
		}).success(function(data){
			if(angular.equals(data,'nodatacollectorFound')){
				// code for displaying error
			}else{
				//	$scope.customerDivList = true;

				$scope.customerDivList = false;
				$scope.customerDivListdisabled = true;

				var datacollector = data[0];
				$scope.datacollectorId = datacollector.datacollectorId;
				$scope.dcSerialNumber = datacollector.dcSerialNumber;
				$scope.dcIp = datacollector.dcIp;
				$scope.dcSimcardNo = datacollector.dcSimcardNo;
				$scope.totalEndpoints = datacollector.totalEndpoints;
				$scope.dcUserId = datacollector.dcUserId;
				$scope.dcUserPassword = datacollector.dcUserPassword;
				$scope.latitude = datacollector.latitude;
				$scope.longitude = datacollector.longitude;
				$scope.customerName = datacollector.customerName;
				var customerList = data;

				if(data[1] != null){

					$scope.customerDivList = true;
					$scope.customerDivListdisabled = false;
					$scope.customerList = customerList;

				}


			}
		}).error(function(data,status){

		});
	}

	$scope.updateDataCollector = function(datacollectorId){

		$scope.isDcSerialNumExists = false;
		$scope.isSimCardNumExists = false;
		
		$http({
			method: "GET",
			url: url + "/updateDataCollector",
			params: {dcSerialNumber:$scope.dcSerialNumber,
				dcIp:$scope.dcIp,
				dcSimcardNo:$scope.dcSimcardNo,
				dcUserId:$scope.dcUserId,
				dcUserPassword:$scope.dcUserPassword,
				latitude:$scope.latitude,
				longitude:$scope.longitude,
				datacollectorId: datacollectorId,
				customerName : $scope.customerName
			}
		}).success(function (data) {
			if(angular.equals(data,'dataCollectorUpdatedSuccessfully')){
				
				$scope.isSuccess = true;
				$scope.success="Record updated. Redirecting to main page."

					window.setTimeout(function() {
						$window.location.href="spareDataCollectorPanel";
					}, 3000);
			}
			if(angular.equals(data,'dcserialnumberalreadyexists')){

				$scope.isDcSerialNumExists=true;
				setTimeout(function () 
						{
					$scope.$apply(function()
							{
						$scope.isDcSerialNumExists = false;
							});
						}, 3000);
			}
			if(angular.equals(data,'simcardnumberalreadyexists')){

				$scope.isSimCardNumExists =true;

				setTimeout(function () 
						{
					$scope.$apply(function()
							{
						$scope.isSimCardNumExists = false;
							});
						}, 3000);
				
			}
		}).error(function (data,status){

		});
	}

	$scope.addDataCollectorByAdmin = function(){
		var dcSerialNumber = $scope.dcSerialNumber;
		var dcIp = $scope.dcIp;
		var dcSimcardNo = $scope.dcSimcardNo;
		var dcUserId = $scope.dcUserId;
		var dcUserPassword = $scope.dcUserPassword;
		//var latitude = $scope.latitude;
		//var longitude = $scope.longitude;
		var customerId = $scope.cust_id;
		$scope.isDcSerialNumExists = false;
		$scope.isSimCardNumExists = false;

		if(dcSerialNumber == null || dcSerialNumber == "" || dcIp == null || dcIp == "" ||
				dcSimcardNo == null || dcSimcardNo == "" || dcUserId == null || dcUserId == "" ||
				dcUserPassword == null || dcUserPassword == "" ){

		}
		else{

			$http({
				method: "POST",
				url: url + "/addDataCollector",
				params: {dcSerialNumber:dcSerialNumber,
					dcIp:dcIp,
					dcSimcardNo:dcSimcardNo,
					dcUserId:dcUserId,
					dcUserPassword:dcUserPassword,
					latitude:latitude,
					longitude:longitude,
					customerId : customerId
				}
			}).success(function (data) {

				if(angular.equals(data,'dataCollectorAddedSuccessfully')){

					$scope.isSuccess = true;
					$scope.success="Record added. Redirecting to main page."

						window.setTimeout(function() {
							$window.location.href="spareDataCollectorPanel";
						}, 3000);
					
				}
				if(angular.equals(data,'dcserialnumberalreadyexists')){

					$scope.isDcSerialNumExists=true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.isDcSerialNumExists = false;
								});
							}, 3000);
				}
				if(angular.equals(data,'simcardnumberalreadyexists')){

					$scope.isSimCardNumExists =true;

					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.isSimCardNumExists = false;
								});
							}, 3000);
					
				}
			}).error(function (data,status){

			});

		}
	}

	$scope.searchDatacollector = function(){

		var customerName = $scope.customerName;
		var dcSerialNumber = $scope.dcSerialNumber;

		$http({
			method: "POST",
			url: url + "/searchDataCollector",
			params: {
				customerName : customerName,
				dcSerialNumber : dcSerialNumber},

		}).success(function (data) {
			if(angular.equals(data,'nosuchuserfound')){
				$scope.noDataCollectorFoundDiv = true;
				$scope.dataCollectorListDiv = false;

			}else{
				$scope.noDataCollectorFoundDiv = false;
				$scope.dataCollectorListDiv = true;
				$scope.dataCollectorList = data;

				$scope.limit = 10;
				$scope.begin = 0;
				// For pagination
				var keys = Object.keys($scope.dataCollectorList);
				var length = keys.length;

				// Max list calculations
				$scope.totalPage = Math.ceil(length/$scope.limit); //Here 10 is default records to be appeared
			}
		}).error(function (data,status){

		});
	}



	// For pagination
	$scope.changedValue=function(recordSize){
		$scope.limit = recordSize;
		$scope.begin = 0;
		// Change Max Limit
		var keys = Object.keys($scope.customerList);
		var length = keys.length;
		// DropDown value fro boolean True and False
	}
	/*$scope.boolToStr = function(arg) {return arg ? 'True' : 'False'};
	
	$scope.abortEndPointDelete = function(){
		swal("Error !", "Selected EndPoint is already deleted !", "error")
	}*/
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

	$scope.refreshAdminUserPage = function(){
		window.location.reload();
	}
	// Assign Installer to consumer Methods starts
	//Search Consumer
	$scope.searchConsumerByStreetName = function(customerId){
		var selectedInstaller = $scope.selectedInstaller;
		if(selectedInstaller == '' || selectedInstaller == null || selectedInstaller === undefined){
			$scope.showErrorMessage = true;
			$scope.errMessage="Please select Installer"
				$scope.showMessage = false;
			$scope.message = '';
		}else if($scope.streetName){

			$http({
				method: "POST",
				url: url + "/getConsumerofCustomer",
				params :{customerId : $scope.currentCustomerId,streetName:$scope.streetName}
			}).success(function (data) {
				if(data[0] == undefined){
					$scope.showMessage = false;
					$scope.message = '';
					$scope.errMessage="No Endpoint found.";
					$scope.showErrorMessage = true;
					$scope.consumerWithoutInstaller = null;
					$scope.selectAll = false;
				}else{
					$scope.consumerWithoutInstaller = data;
					$scope.showMessage = false;
					$scope.message="";
					$scope.showErrorMessage = false;
					$scope.selectAll = false;
				}

			}).error(function (data,status){

			});
		}
	}

	$scope.getCustomerInstaller = function(customerId){
		$scope.currentCustomerId = customerId;
		$http({
			method: "POST",
			url: url + "/getCustomerInstaller",
			params :{customerId : $scope.currentCustomerId}
		}).success(function (data) {
			$scope.installerList = data;

		}).error(function (data,status){

		});
	}

	$scope.assignConsumertoInstaller = function(){

		var selectedConsumerArray = $scope.selectedConsumers;
		var selectedConsumerString="";
		//For selected consumer
		if(selectedConsumerArray.length > 0){
			var i = 0;

			angular.forEach($scope.selectedConsumers,function(value,index){
				selectedConsumerString = selectedConsumerString.concat(value+"/")
			});
			$http({
				method: "POST",
				url: url + "/assignSelectedConsumertoInstaller",
				params :{customerId :$scope.currentCustomerId,consumerIds :selectedConsumerString,installerId:$scope.selectedInstaller,streetName:$scope.streetName}
			}).success(function (data) {
				$scope.message= "Installer asigned successfully.";
				$scope.selectAll = false;
				$scope.showMessage = true;
				$scope.errMessage = '';
				$scope.showErrorMessage = false;
				$scope.removeConsumerRow();

			}).error(function (data,status){

			});
			//For All consumer
		}else if($scope.selectedInstaller == null || $scope.selectedInstaller == undefined){
			$scope.showMessage = false;
			$scope.message=""
			$scope.errMessage = 'Please select Endpoint and Installer.';
			$scope.showErrorMessage = true;
				
		}else if($scope.selectAll == true){

			$http({
				method: "POST",
				url: url + "/assignConsumertoInstaller",
				params :{customerId :$scope.currentCustomerId,installerId:$scope.selectedInstaller,streetName:$scope.streetName}
			}).success(function (data) {
				$scope.message= "Installer asigned successfully.";
				$scope.errMessage = '';
				$scope.showErrorMessage = false;
				$scope.showMessage = true;
				$scope.selectAll = false;
				$scope.addConsumerRow();
				$scope.consumerWithoutInstaller = '';
				//$scope.addRow();

			}).error(function (data,status){

			});
		}else{
			$scope.errMessage="Please enter proper criteria.";
			$scope.showMessage = false;
			$scope.message = '';
			$scope.showErrorMessage =true
		}


	}

	$scope.getInstallersConsumer = function(){

		$http({
			method: "POST",
			url: url + "/getInstallersConsumers",
			params :{installerId:$scope.selectedInstaller}
		}).success(function (data) {
			$scope.consumerWithInstaller = data;

		}).error(function (data,status){

		});
	}


	$scope.selectMe = function(accNo){
		//$scope.selectAll = false;
		$scope.selectedDcId = accNo;
		if($scope.selectedConsumers.includes(accNo)){ //If it is checked
			$scope.selectedConsumers.pop(accNo);
		}else{
			$scope.selectedConsumers.push(accNo);
		}

	}

	$scope.selectAllFun = function(){
		if($scope.selectedConsumers.length >0){
			$scope.selectedConsumers = '';
		}
		if($scope.consumerWithoutInstaller === undefined || $scope.consumerWithoutInstaller == ''){
			$scope.errMessage ="Please select Endpoint.";
			$scope.showErrorMessage = true;
			$scope.showMessage = false;
			$scope.message = '';
		}else{
			if($scope.selectAll == true){
				$scope.selectAll = false;
			}else if($scope.selectAll == false){
				$scope.selectAll = true;
			}

		}


	}

	$scope.removeConsumerRow = function(){

		var index = -1;

		var comArr = eval( $scope.selectedConsumers );
		for( var i = 0; i < comArr.length; i++ ) {
			var consumerWithoutInstallerArray = $scope.consumerWithoutInstaller;
			for(var j=0;j<consumerWithoutInstallerArray.length;j++){
				if(comArr[i] === consumerWithoutInstallerArray[j].meter_id){
					index = j;
					$scope.consumerWithInstaller.push(consumerWithoutInstallerArray[j]);
					$scope.consumerWithoutInstaller.splice(index, 1 );
					break;
				}
			}
		}
		$scope.selectedConsumers = [];
	}

	$scope.refreshAdminUserPage = function(){
		window.location.reload();
	}
	$scope.removeDCRow = function(){

		var index = -1;

		var comArr = eval( $scope.selectedConsumers );
		for( var i = 0; i < comArr.length; i++ ) {
			var DCWithoutInstallerArray = $scope.DCWithoutInstaller;
			for(var j=0;j<DCWithoutInstallerArray.length;j++){
				if(comArr[i] === DCWithoutInstallerArray[j].datacollectorId){
					index = j;
					$scope.DCWithInstaller.push(DCWithoutInstallerArray[j]);
					$scope.DCWithoutInstaller.splice(index, 1 );
					break;
				}
			}
		}
		$scope.selectedConsumers = [];
	}


	$scope.addConsumerRow = function(){
		var consumerWithoutInstallerArray = $scope.consumerWithoutInstaller;
		for(var j=0;j<consumerWithoutInstallerArray.length;j++){
			$scope.consumerWithInstaller.push(consumerWithoutInstallerArray[j]);

		}

	}
	// Assign Installer to consumer method ends

	//DC Assign to Installer Code Start

	// Get All DC of Installer
	$scope.getInstallersDC = function(){

		$http({
			method: "POST",
			url: url + "/getInstallersDC",
			params :{installerId:$scope.selectedInstaller}
		}).success(function (data) {
			$scope.DCWithInstaller = data;

		}).error(function (data,status){

		});
	}


	$scope.searchDataCollectorByIP = function(){

		var dcIp = $scope.dcIp;

		$http({
			method: "POST",
			url: url + "/searchDataCollectorByCustomerAndIp",
			params: {
				dcIp : dcIp,
				customerId:$scope.currentCustomerId,
			},

		}).success(function (data) {

			if(angular.equals(data,'nosuchDCfound')){
				$scope.message= "";
				$scope.errMessage = 'No such DataCollector found.';
				$scope.showErrorMessage = true;
				$scope.showMessage = false;
			}else {
				$scope.DCWithoutInstaller = data;
				$scope.message= "";
				$scope.errMessage = '';
				$scope.showErrorMessage = false;
				$scope.showMessage = false;
			}
		}).error(function (data,status){

		});
	}



	$scope.assignSelectedDCToInstaller = function(){

		$http({
			method: "POST",
			url: url + "/assignSelectedDCToInstaller",
			params :{customerId :$scope.currentCustomerId,installerId:$scope.selectedInstaller,selectedDcId:$scope.selectedDcId}
		}).success(function (data) {
			$scope.message= "Installer asigned successfully.";
			$scope.errMessage = '';
			$scope.showErrorMessage = false;
			$scope.showMessage = true;
			$scope.selectAll = false;
			$scope.consumerWithoutInstaller = '';
			$scope.removeDCRow();


		}).error(function (data,status){

		});


	}

	$scope.editopenDataPlan = function(){
		$scope.popup1.opened=true;
	}
	$scope.editopenExpiryDataPlan = function(){
		$scope.popup2.opened=true;
	}
	$scope.editopenPortalPlan = function(){
		$scope.popup3.opened=true;
	}
	$scope.editopenExpiryPortalPlan = function(){
		$scope.popup4.opened=true;
	}


	//***************************************************

	$scope.initCountry = function(){
		$http({
			method: "POST",
			url: url + "/getCountryInit",
		}).success(function (data) {
			$scope.countryListDiv = true;
			$scope.noCountryFoundDiv = false;
			$scope.countryList = data;
			$scope.limit = 10;
			$scope.begin = 0;
			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.countryList.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show

			if(angular.equals(data,'youCanNotAccessCountryList')){
				$window.location.href="loginForm";
			}
		}).error(function (data,status){

		});	

	}

	$scope.searchCountry = function(){

		var searchCountryName = $scope.searchCountryName;
		//var searchCountryCode = $scope.searchCountryCode;

		$http({
			method: "POST",
			url: url + "/searchCountry",
			params: {searchCountryName : searchCountryName},

		}).success(function (data) {

			if(angular.equals(data,'nosuchcountryfound')){

				$scope.noCountryFoundDiv = true;
				$scope.countryListDiv = false;

			} else {
				$scope.noCountryFoundDiv = false;
				$scope.countryListDiv = true;
				$scope.countryList = data;

				$scope.limit = 10;
				$scope.begin = 0;
				// For pagination
				var keys = Object.keys($scope.countryList);
				var length = keys.length;

				// Max list calculations
				$scope.totalPage = Math.ceil(length/$scope.limit); //Here 10 is default records to be appeared
			}
		}).error(function (data,status){

		});	
	}

	$scope.insertCountry = function(){

		$scope.isCountryNameExist = false;
		$scope.isCountryCodeExist = false;
		$scope.enterCountryName = false;
		$scope.enterCountryCode = false;
		
		var countryName = $scope.countryName;
		var countryCode = $scope.countryCode;

		if($scope.countryName != null && $scope.countryCode != null){

			$http({
				method: "POST",
				url: url + "/addCountry",
				params: {countryName:countryName, countryCode:countryCode}

			}).success(function (data) {

				if(angular.equals(data,'countrySuccessfullyadded')){

					$scope.isSuccess = true;
					$scope.success="Record added. Redirecting to main page."

						window.setTimeout(function() {
							$window.location.href="countryManagement";
						}, 3000);
					

				}
				if(angular.equals(data,'countrynamealreadyexist')){

					$scope.isCountryNameExist=true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.isCountryNameExist = false;
								});
							}, 3000);
				}
				if(angular.equals(data,'countrycodealreadyexist')){

					$scope.isCountryCodeExist=true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.isCountryCodeExist = false;
								});
							}, 3000);
				}
				if(angular.equals(data,'youCanNotAccessCountryList')){
					$window.location.href="loginForm";
				}
				if(angular.equals(data,'nullcountryname')){

					$scope.enterCountryName = true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.enterCountryName = false;
								});
							}, 3000);
					
				}
				if(angular.equals(data,'nullcountrycode')){

					$scope.enterCountryCode = true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.enterCountryCode = false;
								});
							}, 3000);
					
				}
				$scope.response = data;
			}).error(function (data,status){

			});	
		}
	}

	$scope.editCountry = function(countryId){

		$http({
			method: "POST",
			url: url + "/editCountry",
			params:{countryId : countryId},
		}).success(function(data) {
			$window.location.href="editCountryForm";
		}).error(function (data,status){

		});
	}

	$scope.getEditCountry = function(){
		//First make ajax call and get that sessioned user entity.
		
		$scope.isCountryNameExist = false;
		$scope.isCountryCodeExist = false;
		$scope.enterCountryName = false;
		$scope.enterCountryCode = false;
		
		$http({
			method: "POST",
			url: url+"/getCountryData",
		}).success(function(data){
			if(angular.equals(data,'noCountryFound')){
				$scope.addCountryDiv = true;
			}else{
				$scope.editCountryDiv = true;
				$scope.countryDivList = false;

				var country = data[0];
				$scope.countryId = country.countryId;
				$scope.countryName = country.countryName;
				$scope.countryCode = country.countryCode;

			}
		}).error(function(data,status){

		});
	}

	// Angular method for Updating Data Plan
	$scope.updateCountry = function(countryId){

		var countryName = $scope.countryName;
		var countryCode = $scope.countryCode;

		$http({
			method: "POST",
			url: url + "/updateCountry",
			params: {countryName : countryName,
				countryCode : countryCode,
				countryId : countryId
			}
		}).success(function (data) {
			if(angular.equals(data,'countryUpdatedSuccessfully')){

				$scope.isSuccess = true;
				$scope.success="Record updated. Redirecting to main page."

					window.setTimeout(function() {
						$window.location.href="countryManagement";
					}, 3000);
 
			}
			if(angular.equals(data,'countrynamealreadyexist')){

				$scope.isCountryNameExist=true;
				setTimeout(function () 
						{
					$scope.$apply(function()
							{
						$scope.isCountryNameExist = false;
							});
						}, 3000);
			}
			if(angular.equals(data,'countrycodealreadyexist')){

				$scope.isCountryCodeExist=true;
				setTimeout(function () 
						{
					$scope.$apply(function()
							{
						$scope.isCountryCodeExist = false;
							});
						}, 3000);
			}
			if(angular.equals(data,'nullcountryname')){

				$scope.enterCountryName = true;
				setTimeout(function () 
						{
					$scope.$apply(function()
							{
						$scope.enterCountryName = false;
							});
						}, 3000);
				
			}
			if(angular.equals(data,'nullcountrycode')){

				$scope.enterCountryCode = true;
				setTimeout(function () 
						{
					$scope.$apply(function()
							{
						$scope.enterCountryCode = false;
							});
						}, 3000);
				
			}
		}).error(function (data,status){

		});	



	}
	//**********************************************************

	$scope.initCurrency = function(){
		$http({
			method: "POST",
			url: url + "/getCurrencyInit",
		}).success(function (data) {
			$scope.currencyListDiv = true;
			$scope.noCurrencyFoundDiv = false;
			$scope.currencyList = data;
			$scope.limit = 10;
			$scope.begin = 0;
			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.currencyList.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show

			if(angular.equals(data,'youCanNotAccessCurrencyList')){
				$window.location.href="loginForm";
			}
		}).error(function (data,status){

		});	

	}

	$scope.searchCurrency = function(){

		var searchCurrencyName = $scope.searchCurrencyName;

		$http({
			method: "POST",
			url: url + "/searchCurrency",
			params: {searchCurrencyName : searchCurrencyName},

		}).success(function (data) {
			if(angular.equals(data,'nosuchuserfound')){
				$scope.noCurrencyFoundDiv = true;
				$scope.currencyListDiv = false;

			}else{
				$scope.noCurrencyFoundDiv = false;
				$scope.currencyListDiv = true;
				$scope.currencyList = data;

				$scope.limit = 10;
				$scope.begin = 0;
				// For pagination
				var keys = Object.keys($scope.currencyList);
				var length = keys.length;

				// Max list calculations
				$scope.totalPage = Math.ceil(length/$scope.limit); //Here 10 is default records to be appeared
			}
		}).error(function (data,status){

		});	
	}


	$scope.insertCurrency = function(){

		$scope.isCurrencyNameExist = false;

		var currencyName = $scope.currencyName;
		var currencySymbol = $scope.currencySymbol;
		$scope.enterCurrencyName = false;
		$scope.enterCurrencySymbol = false;

		if($scope.currencyName != null || $scope.currencySymbol != null){

			$http({
				method: "POST",
				url: url + "/addCurrency",
				params: {currencyName:currencyName,
					currencySymbol:currencySymbol}

			}).success(function (data) {

				if(angular.equals(data,'countrySuccessfullyadded')){

					$scope.isSuccess = true;
					$scope.success="Record added. Redirecting to main page."

						window.setTimeout(function() {
							$window.location.href="currencyManagement";
						}, 3000);
					

				}
				if(angular.equals(data,'currencynamealreadyexist')){

					$scope.isCurrencyNameExist=true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.isCurrencyNameExist = false;
								});
							}, 3000);
				}
				if(angular.equals(data,'nullcurrencyname')){

					$scope.enterCurrencyName = true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.enterCurrencyName = false;
								});
							}, 3000);
					
				}
				if(angular.equals(data,'nullcurrencysymbol')){

					$scope.enterCurrencySymbol = true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.enterCurrencySymbol = false;
								});
							}, 3000);
					
				}
				
				
				if(angular.equals(data,'youCanNotAccessCurrencyList')){
					$window.location.href="loginForm";
				}
				$scope.response = data;
			}).error(function (data,status){

			});	
		}
	}

	$scope.editCurrency = function(currencyId){

		$http({
			method: "POST",
			url: url + "/editCurrency",
			params:{currencyId : currencyId},
		}).success(function(data) {

			$window.location.href="editCurrencyForm";

		}).error(function (data,status){

		});
	}

	$scope.getEditCurrency = function(){
		//First make ajax call and get that sessioned user entity.
		$scope.isCurrencyNameExist = false;
		$scope.enterCurrencyName = false;
		$scope.enterCurrencySymbol = false;
		
		$http({
			method: "POST",
			url: url+"/getCurrencyData",
		}).success(function(data){
			if(angular.equals(data,'noCurrencyFound')){
				$scope.addCurrencyDiv = true;
			}else{
				$scope.editCurrencyDiv = true;
				$scope.currencyDivList = false;

				var currency = data[0];
				$scope.currencyId = currency.currencyId;
				$scope.currencyName = currency.currencyName;
				$scope.currencySymbol = currency.currencySymbol;

			}
		}).error(function(data,status){

		});
	}

//	Angular method for Updating Data Plan
	$scope.updateCurrency = function(currencyId){

		var currencyName = $scope.currencyName;
		var currencySymbol = $scope.currencySymbol;

		$http({
			method: "POST",
			url: url + "/updateCurrency",
			params: {currencyName : currencyName,
				currencySymbol : currencySymbol,
				currencyId : currencyId
			}
		}).success(function (data) {
			if(angular.equals(data,'currencyUpdatedSuccessfully')){

				$scope.isSuccess = true;
				$scope.success="Record updated. Redirecting to main page."

					window.setTimeout(function() {
						$window.location.href="currencyManagement";
					}, 3000);
 
			}
			if(angular.equals(data,'currencynamealreadyexist')){

				$scope.isCurrencyNameExist=true;
				setTimeout(function () 
						{
					$scope.$apply(function()
							{
						$scope.isCurrencyNameExist = false;
							});
						}, 3000);
			}
			if(angular.equals(data,'nullcurrencyname')){

				$scope.enterCurrencyName = true;
				setTimeout(function () 
						{
					$scope.$apply(function()
							{
						$scope.enterCurrencyName = false;
							});
						}, 3000);
				
			}
			if(angular.equals(data,'nullcurrencysymbol')){

				$scope.enterCurrencySymbol = true;
				setTimeout(function () 
						{
					$scope.$apply(function()
							{
						$scope.enterCurrencySymbol = false;
							});
						}, 3000);
				
			}
		}).error(function (data,status){

		});	



	}


	$scope.removeError = function(errorTag){
		if(angular.equals(errorTag,'currency')){
			$scope.isCurrency = false;
		}else if(angular.equals(errorTag,'country')){
			$scope.isCountry = false;
		}else if(angular.equals(errorTag,'dataPlan')){
			$scope.isDataPlan = false;
		}

	}

//**********************************************
	
	$scope.initBattery = function(){
		$http({
			method: "POST",
			url: url + "/getBatteryInit",
		}).success(function (data) {
			$scope.batteryListDiv = true;
			$scope.noBatteryFoundDiv = false;
			$scope.batteryList = data;
			$scope.limit = 10;
			$scope.begin = 0;
			// For pagination
			$scope.recordSize = 10;
			$scope.totalItems = $scope.batteryList.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show
			if(angular.equals(data,'youCanNotAccessBatteryList')){
				$window.location.href="loginForm";
			}
		}).error(function (data,status){

		});	

	}

	$scope.searchBattery = function(){

		var childNodeInput = $scope.childNodeInput;

		$http({
			method: "POST",
			url: url + "/searchBattery",
			params: {childNodeInput : childNodeInput},

		}).success(function (data) {

			if(angular.equals(data,'nosuchbatteryfound')){

				$scope.noBatteryFoundDiv = true;
				$scope.batteryListDiv = false;

			}else if(angular.equals(data,'emptyfield')){
				
			}else {
				$scope.noBatteryFoundDiv = false;
				$scope.batteryListDiv = true;
				$scope.batteryList = data;

				$scope.limit = 10;
				$scope.begin = 0;
				// For pagination
				var keys = Object.keys($scope.batteryList);
				var length = keys.length;

				// Max list calculations
				$scope.totalPage = Math.ceil(length/$scope.limit); //Here 10 is default records to be appeared
			}
		}).error(function (data,status){

		});	
	}

	$scope.insertBatteryLife = function(){

		$scope.isnumOfChildNodeExist = false;
		$scope.inValidChildNodes = false;
		$scope.inValidBatteryLife = false;
		$scope.invalidvalue = false;
		
		var numberOfChildNodes = $scope.numberOfChildNodes;
		var estimatedBatteryLifeInYears = $scope.estimatedBatteryLifeInYears;

		if($scope.numberOfChildNodes != null && $scope.estimatedBatteryLifeInYears != null){

			$http({
				method: "POST",
				url: url + "/addBatteryLife",
				params: {numberOfChildNodes:numberOfChildNodes,
					estimatedBatteryLifeInYears:estimatedBatteryLifeInYears}

			}).success(function (data) {

				if(angular.equals(data,'batterySuccessfullyadded')){
					
					$scope.isSuccess = true;
					$scope.success="Record added. Redirecting to main page."

						window.setTimeout(function() {
							$window.location.href="batteryManagement";
						}, 3000);

				}
				if(angular.equals(data,'numofchildnodesalreadyexist')){

					$scope.isnumOfChildNodeExist=true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.isnumOfChildNodeExist = false;
								});
							}, 3000);
				}
				if(angular.equals(data,'invalidvalue')){

					$scope.invalidvalue=true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.invalidvalue = false;
								});
							}, 3000);
				}
				if(angular.equals(data,'numberofchildnodescantbenull')){

					$scope.inValidChildNodes =true;

					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.inValidChildNodes = false;
								});
							}, 3000);
					
				}
				if(angular.equals(data,'batterylifecantbenull')){

					$scope.inValidBatteryLife =true;

					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.inValidBatteryLife = false;
								});
							}, 3000);
					
				}
				if(angular.equals(data,'youCanNotAccessBatteryList')){
					$window.location.href="loginForm";
				}
				
				$scope.response = data;
			}).error(function (data,status){

			});	
		}
	}

	$scope.removeBattery = function(batteryLifeId){

		swal({
			   title: 'Confirm',
			   text: 'Want to delete battery with ID : '+ batteryLifeId+' ?',
			   type: 'warning',
			   showCancelButton: true,
			   confirmButtonText: 'Yes',
			   cancelButtonText: 'Cancel'
			}, function(){
				
				$http({
					method: "POST",
					url: url + "/deleteBattery",
					params:{batteryLifeId : batteryLifeId},
				}).success(function(data) {
					
					if(angular.equals(data,'batteryismapped')){
						$scope.isMapped=true;

						setTimeout(function () 
								{
							$scope.$apply(function()
									{
								$scope.isMapped = false;
									});
								}, 3000);
					}
					if(angular.equals(data,'batterydeletedsuccessfully')){
						
						$scope.success="Battery successfully deleted... Reloading list...";
						$scope.isSuccess=true;

						window.setTimeout(function() {
							$window.location.href="batteryManagement";
						}, 3000);
					}
				});
				
			});
	}
	
	$scope.editBattery = function(batteryLifeId){

		$http({
			method: "POST",
			url: url + "/editBattery",
			params:{batteryLifeId : batteryLifeId},
		}).success(function(data) {
			$window.location.href="editBatteryForm";
		}).error(function (data,status){

		});
	}

	$scope.updateBatteryByPercent = function(){
		
		var percentageUpDown = $scope.percentageUpDown;
		//var percentageUp = $scope.percentageUp;
		//var percentageDown = $scope.percentageDown;
		var batteryPercentage = $scope.batteryPercentage;
		
			$http({
				method: "POST",
				url: url + "/updateBatteryByPercent",
				params:{batteryPercentage : batteryPercentage, /*percentageUp : percentageUp,
					percentageDown : percentageDown*/ percentageUpDown : percentageUpDown},
			}).success(function(data) {
				$scope.success="Battery life updated successfully. Reloading list.";
				$scope.isSuccess=true;
	
				window.setTimeout(function() {
					$window.location.href="batteryManagement";
				}, 3000);
			}).error(function (data,status){
	
			});
	}
	
	$scope.getEditBattery = function(){
		//First make ajax call and get that sessioned user entity.
		
		$scope.inValidChildNodes = false;
		$scope.inValidBatteryLife = false;
		$scope.isnumOfChildNodeExist = false;
		$scope.invalidvalue = false;
		
		$http({
			method: "POST",
			url: url+"/getBatteryData",
		}).success(function(data){
			if(angular.equals(data,'noBatteryFound')){
				$scope.addBatteryDiv = true;
			}else{
				$scope.editBatteryDiv = true;
				$scope.batteryDivList = false;

				var battery = data[0];
				$scope.batteryLifeId = battery.batteryLifeId;
				$scope.numberOfChildNodes = battery.numberOfChildNodes;
				$scope.estimatedBatteryLifeInYears = battery.estimatedBatteryLifeInYears;

			}
		}).error(function(data,status){

		});
	}

	// Angular method for Updating Battery
	$scope.updateBattery = function(batteryLifeId){

		var numberOfChildNodes = $scope.numberOfChildNodes;
		var estimatedBatteryLifeInYears = $scope.estimatedBatteryLifeInYears;

		if(numberOfChildNodes != null && estimatedBatteryLifeInYears != null){
			$http({
				method: "POST",
				url: url + "/updateBattery",
				params: {numberOfChildNodes : numberOfChildNodes,
					estimatedBatteryLifeInYears : estimatedBatteryLifeInYears,
					batteryLifeId : batteryLifeId
				}
			}).success(function (data) {
				if(angular.equals(data,'batteryUpdatedSuccessfully')){
					
					$scope.isSuccess = true;
					$scope.success="Record updated. Redirecting to main page."

						window.setTimeout(function() {
							$window.location.href="batteryManagement";
						}, 3000);
				}
				if(angular.equals(data,'numberofchildnodescantbenull')){

					$scope.inValidChildNodes =true;

					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.inValidChildNodes = false;
								});
							}, 3000);
					
				}
				if(angular.equals(data,'invalidvalue')){

					$scope.invalidvalue=true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.invalidvalue = false;
								});
							}, 3000);
				}
				if(angular.equals(data,'batterylifecantbenull')){

					$scope.inValidBatteryLife =true;

					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.inValidBatteryLife = false;
								});
							}, 3000);
					
				}
				if(angular.equals(data,'numofchildnodesalreadyexist')){

					$scope.isnumOfChildNodeExist=true;
					setTimeout(function () 
							{
						$scope.$apply(function()
								{
							$scope.isnumOfChildNodeExist = false;
								});
							}, 3000);
				}
			}).error(function (data,status){
	
			});	
		}else{
			
		}


	}
	
	// Get customer all notification
	$scope.getCustomerNotifications = function(customerId){
		
		$http({
			method: "POST",
			url: url + "/customerOperation/getCustomerNotifications",
			params: {customerId:customerId}
		}).success(function (data) {
			if(data == 'nonotification'){
				
			}else{
				$scope.notificationData = data[0];
				not(data[0]);
				$scope.notificationDataCount = 0;
				angular.forEach(data,function(value,index){
					$scope.notificationDataCount = $scope.notificationDataCount + 1;
				})
				
				$scope.totalAlert = data[1].totalAlert;
				$scope.perCapita = data[1].perCapita;
				$scope.totalEP = data[1].totalEP;
				$scope.totalDataUsage = data[1].totalDataUsage;
				$scope.usagePer = data[1].usagePer;
				$scope.totalBillData = data[1].totalBillData;
				$scope.billDate = data[1].billDate;
				$scope.abnormalUsers = data[1].abnormalUsers;
				$scope.dailyConsumption = data[1].dailyConsumption;
				$scope.totalRevenue = data[1].totalRevenue;
			}
			
		}).error(function (data,status){

		});	
	}
	
	$scope.showNotification = function(){
		if($scope.notificationData === undefined){
			
		}else{
			not($scope.notificationData);
		}
		
	}

	$scope.resetPageForAddCustomer = function(){
		
		$scope.customerCode = null;
		$scope.userName = null;
		$scope.firstName = null;
		$scope.lastName = null;
		$scope.phone = null;
		$scope.email = null;
		$scope.address = null;
		$scope.zipcode = null;
		$scope.dataplan = null;
		$scope.selectedCountry = null;
		$scope.selectedCurrency = null;
		$scope.selectedTimeZone = null;
		$scope.activeStatus = null;
		
	}
	
	$scope.resetPageForEditCustomer = function(){
		
		/*$scope.firstName = null;
		$scope.lastName = null;
		$scope.phone = null;
		$scope.email = null;
		$scope.address = null;
		$scope.zipcode = null;
		$scope.dataplan = null;
		$scope.selectedCountryId = null;
		$scope.selectedCurrencyId = null;
		$scope.selectedTimeZone = null;
		$scope.activeStatus = null;*/
		window.location.reload();
		
	}
	
	$scope.setBorderColor = function(isAdmin){
		if(isAdmin == null || angular.isUndefined(isAdmin)){
			return "customerborderclass";
		}else{
			return "adminborderclass";
		}
	}
	
}]);
