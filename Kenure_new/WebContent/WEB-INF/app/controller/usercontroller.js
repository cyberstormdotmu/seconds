
/*var url = 'http://localhost:8080/Kenure'*/
	
kenureApp.controller('usercontroller',function($scope,$http){


	$scope.onKeyDown = function(myE){
		//$scope.onKeyDownResult = "a";
		//alert($scope.customerCode);
	}

	/*$scope.addUser = function(){
		
		$http({
			method: "GET",
			url: url + "/insertUser",
			params:{customerCode:$scope.customerCode,firstName:$scope.firstName,lastName:$scope.lastName,userName:$scope.userName,dataplan:$scope.dataplan, 
				activedate:$scope.activedate,expirydate:$scope.expirydate,phone1:$scope.phone1,email:$scope.email,address:$scope.address,zipcode:$scope.zipcode},
			params:{customerCode:$scope.customerCode,firstName:$scope.firstName,lastName:$scope.lastName,userName:$scope.userName,dataplan:$scope.dataplan},

		}).success(function(data){
			alert(">>");
				
			
		}).error(function(data,async){

		})
	}*/
	
});