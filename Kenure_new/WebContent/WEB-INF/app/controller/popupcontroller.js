kenureApp.controller('popupcontroller', ['$scope','$http','$location','$window','$uibModal','$filter',function($scope, $http,$location,$window,$modal,$filter){
	$scope.siteIdsList = [];
	$scope.paginationList = [10,20,30,40,50,60];
	//Init For Scheduler Page
	$scope.initSiteDataForScheduler = function(){
		$scope.siteListDiv = true;
		$http({
			method:"POST",
			url:url+"/customerOperation/customerSiteInitialDataForScheduler"
		}).success(function(data){
			$scope.selectAll = false;
			$scope.siteList = data[0];
			$scope.isSuperCustomer = data[1];
			// For pagination
			var defaultPaging = 10;
			$scope.recordSize = defaultPaging;
			$scope.totalItems = $scope.siteList.length;
			$scope.currentPage = 1;
			$scope.itemsPerPage = $scope.recordSize;
			$scope.maxSize = 5; //Number of pager buttons to show

		}).error(function(data,status){

		});
	}

	//Search Functionality for Scheduler Page
	$scope.searchSite = function(){

		// Check for both null 
		$http({
			method:"POST",
			url:url+"/customerOperation/searchBysiteIdOrRegionName",
			params:{sitId:$scope.sitIdSearch,regionName:$scope.regionNameSearch},
		}).success(function(data){

			if(angular.equals(data,'nosuchsitefound')){
				$scope.nosuchSiteFoundDiv = true;
				$scope.siteListDiv = false;
			}else{
				$scope.nosuchSiteFoundDiv = false;
				$scope.siteListDiv = true;
				$scope.siteList = data;
			}

			$scope.limit = 10;
			$scope.begin = 0;
			// For pagination
			var keys = Object.keys($scope.siteList);
			var length = keys.length;

			// Max list calculations
			$scope.totalPage = Math.ceil(length/$scope.limit); //Here 10 is default records to be appeared

		}).error(function(data){

		});
	}

	// Popup Open and close and Update MRI and NRI Functionality for particular SitId
	$scope.open = function (Id) {

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
						

						var ModalInstance = $modal.open({
							controller: function($uibModalInstance ,$scope,siteId){
								$scope.siteId = siteId;
								$scope.close = function () {
									$uibModalInstance.dismiss('cancel');
								};
								$scope.update = function () {
									if(!$scope.mins && !$scope.hours && !$scope.days){
										
										swal("Warning!", "Please select atleast one value !", "warning")
									}else{
										var time = null;
										if($scope.hours)
											time = $scope.hours*60;
										if($scope.mins)
											time = $scope.mins;
										if($scope.days)
											time = $scope.days * 1440;
										$http({
											method:"POST",
											url:url+"/updateMeterReadingInterval",
											params :{sitId:$scope.siteId,mriInMins:time},
										}).success(function(data){
											if(angular.equals(data,'success')){
												$scope.successMessage = "MRI Successfully Set!";
												$scope.isSuccess = true;
												window.setTimeout(function() {
													window.location.reload();
												}, 2000);
											}else{
												$scope.errorMessage = "Cannot set selected Value as MRI because its exceeding your Data Plan!";
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
										}).error(function(data,status){

										});
									}
								}
							},
							resolve: {
								siteId: function () {
									return Id;
								}
							},
							templateUrl: url+'/popup',
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
			var ModalInstance = $modal.open({
				controller: function($uibModalInstance ,$scope,siteId){
					$scope.daysList = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31];
					$scope.hoursList = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23];
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

					$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
					$scope.format = $scope.formats[0];
					$scope.altInputFormats = ['M!/d!/yyyy'];

					$scope.popup1 = {
							opened: false
					};
					// Datepicker code ends
					
					$scope.siteId = siteId;
					$scope.close = function () {
						$uibModalInstance.dismiss('cancel');
					};
					$scope.update = function () {
						if(!$scope.mins && !$scope.hours && !$scope.days && !$scope.startDate){
							swal("Warning!", "Please select atleast one value !", "warning")
						}else{
							var time = null;
							var startDate = null;
							if($scope.hours)
								time = $scope.hours*60;
							if($scope.mins)
								time = $scope.mins;
							if($scope.days)
								time = $scope.days * 1440;
							if($scope.startDate)
								startDate = $filter('date')($scope.startDate,"yyyy-MM-dd");
							$http({
								method:"POST",
								url:url+"/updateMeterReadingInterval",
								params :{sitId:$scope.siteId,mriInMins:time,startDate:startDate},
							}).success(function(data){
								if(angular.equals(data,'success')){
									$scope.successMessage = "MRI Successfully Set!";
									$scope.isSuccess = true;
									window.setTimeout(function() {
										window.location.reload();
									}, 2000);
								}else{
									$scope.errorMessage = "Cannot set selected Value as MRI because its exceeding your Data Plan!";
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
							}).error(function(data,status){

							});
						}
					}
				},
				resolve: {
					siteId: function () {
						return Id;
					}
				},
				templateUrl: url+'/popup',
			});
		}

	}


	// Add/Upload notes
	$scope.viewUploadNotes = function (consumerMeterId, textNote, filePath, originalFileName) {

		var ModalInstance = $modal.open({
			controller: function($uibModalInstance, $scope, consumerMeterId, textNote, filePath, originalFileName){

				$scope.consumerMeterId = consumerMeterId;
				$scope.textNote = textNote;
				$scope.filePath = filePath;
				$scope.originalFileName = originalFileName;
				$scope.removeUpload = false;

				$scope.close = function () {
					$uibModalInstance.dismiss('cancel');
				};
				$scope.uploadFile = function () {

					var file = $scope.myFile;

					var uploadUrl = url+"/customerOperation/uploadNote";
					//fileUpload.uploadFileToUrl(file, uploadUrl, $scope.consumerMeterId, $scope.textNote);

					if (file == undefined && $scope.textNote == undefined && !$scope.removeUpload) {
						swal("Warning!", "Nothing to upload ! Please add text AND/OR file.", "warning")
					} else {
						var fd = new FormData();

						if (file == undefined) {
							fd.append('file', new File([""], "noFile", {type: "noType"}));
						} else {
							fd.append('file', file);
						}

						fd.append('consumerMeterId', $scope.consumerMeterId);
						fd.append('textNote', $scope.textNote);
						fd.append('removeUpload',$scope.removeUpload);

						$http.post(uploadUrl, fd, {
							transformRequest: angular.identity,
							headers: {'Content-Type': undefined}
						})
						.success(function(data){

							if (angular.equals(data,'successfullyupdated')) {
								$scope.noteUploadCompleted = true;
								setTimeout(function ()
										{
									$scope.$apply(function()
											{
										$scope.noteUploadCompleted = false;
										$uibModalInstance.dismiss('cancel');
											});
										}, 1100);
								$http({
									method: "POST",
									url: url + "/customerOperation/editConsumer",
									params:{consumerMeterId:$scope.consumerMeterId},
								}).success(function(data){
									if(angular.equals(data,'nosuchuserfound')){
										$uibModalInstance.dismiss('cancel');
									}else{
										$window.location.href="editCustomerPageRedirect";
									}
								}).error(function(data,status){

								});
							} else if (angular.equals(data,'unsupportedfiletype')) {
								swal("Warning!", "Unsupported file ! Please upload valid (text/image) files.", "warning")
							}else {
								$uibModalInstance.dismiss('cancel');
							}
						})

						.error(function(){
						});
					}
				}
			},
			resolve: {
				consumerMeterId: function() {
					return $scope.consumerMeterId;
				},textNote: function () {
					return $scope.textNote;
				},filePath: function () {
					return $scope.filePath;
				},originalFileName: function () {
					return $scope.originalFileName;
				}
			},
			templateUrl: url+'/customerOperation/notes',
		});
	}



	// Check alerts for end points
	$scope.checkAlerts = function (consumerMeterId) {
		$http({
			method: "GET",
			url: url + "/customerOperation/checkAlerts",
			params:{consumerMeterId:consumerMeterId},
		}).success(function(data) {

			if(angular.equals(data,'noalerts')){
				$scope.alerts = 'No alert(s) found!';
				$scope.isAlertAcknowledged = true;
			} else {
				$scope.alerts = data.alerts;
				// Commented out below known code
				/*$scope.leak = data.leak;
				$scope.tamper = data.tamper;
				$scope.backFlow = data.backFlow;
				$scope.battery = data.battery;*/
				$scope.isAlertAcknowledged = data.isAlertAcknowledged;
			}

			$scope.meterTransactionId = data.meterTransactionId;

			var ModalInstance = $modal.open({
				controller: function($uibModalInstance, $scope, alerts, meterTransactionId, isAlertAcknowledged){

					$scope.meterTransactionId = meterTransactionId;
					$scope.alerts = alerts;
					// Commented out below known code
					/*$scope.leak = leak;
				$scope.tamper = tamper;
				$scope.backFlow = backFlow;
				$scope.battery = battery;*/
					$scope.isAlertAcknowledged = isAlertAcknowledged;

					$scope.close = function () {
						$uibModalInstance.dismiss('cancel');
					};
					$scope.acknowledge = function () {

						$http({
							method:"POST",
							url:url+"/customerOperation/alertAcknowledgement",
							params :{meterTransactionId:$scope.meterTransactionId},
						}).success(function(data){
							$uibModalInstance.dismiss('cancel');
						}).error(function(data,status){

						});
					}
				},
				resolve: {
					meterTransactionId: function() {
						return $scope.meterTransactionId;
					},alerts: function () {
						return $scope.alerts;
					},isAlertAcknowledged: function () {
						return $scope.isAlertAcknowledged;
					}
				},
				templateUrl: url+'/customerOperation/alerts',
			});
		}); 
	}


	// Reset alerts for end points
	$scope.resetAlerts = function (consumerMeterId) {
		swal("Warning!", "Implementation of this functionality is remaining.", "warning")
		/*		$http({
			method: "POST",
			url: url + "/resetAlerts",
			params:{consumerMeterId:consumerMeterId},
		}).success(function(data) {
			$scope.resetCompleted = true;
		     setTimeout(function ()
		       {
		      $scope.$apply(function()
		        {
		       $scope.resetCompleted = false;
		        });
		       }, 3000);
		}).error(function(data,status){

		});*/
	}


	/*// For Pagination Start
	$scope.changedValue=function(recordSize, list){
		$scope.limit = Pagination.changedValue(recordSize, list)[0];
		$scope.begin = Pagination.changedValue(recordSize, list)[1];
		$scope.totalPage = Pagination.changedValue(recordSize, list)[2];
	}

	$scope.getNumber = function(number){
		return Pagination.getNumber(number);
	}

	$scope.fetchRecord = function(number,recordSize){
		$scope.limit = Pagination.fetchRecord(number,recordSize)[0];
		$scope.begin = Pagination.fetchRecord(number,recordSize)[1];
	}
	// Pagination End
*/	
	//New Pagination Start
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
	//New pagination End

	//Page Refresh functionality
	$scope.refreshPage = function () {
		window.location.reload();
	}

	$scope.pushOrPop = function(siteId) {
		//If it is already checked then on unCheck pop else push
		if($scope.siteIdsList.indexOf(siteId) === -1) {
			$scope.siteIdsList.push(siteId);
		}else{
			var index  = $scope.siteIdsList.indexOf(siteId);
			$scope.siteIdsList.splice(index, 1);
		}
	}

	// Set Default MRI and NRI for All Sites 
	$scope.setDefaultMRIForAllSites = function () {

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
						if($scope.siteIdsList.length > 0){
							var allSelectedSiteIdsList = [];
							allSelectedSiteIdsList = $scope.siteIdsList;
							var ModalInstance = $modal.open({
								controller: function($uibModalInstance ,$scope,allSelectedSiteIdsList){
									
									$scope.daysList = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31];
									$scope.hoursList = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23];
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

									$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
									$scope.format = $scope.formats[0];
									$scope.altInputFormats = ['M!/d!/yyyy'];

									$scope.popup1 = {
											opened: false
									};
									// Datepicker code ends
									
									$scope.close = function () {
										$uibModalInstance.dismiss('cancel');
									};
									$scope.update = function () {
										if(!$scope.mins && !$scope.hours && !$scope.days && !$scope.startDate){
											swal("Warning!", "Please select any one value !", "warning")
										}
										else{
											var time = null;
											var startDate = null;
											if($scope.hours)
												time = $scope.hours*60;
											if($scope.mins)
												time = $scope.mins;
											if($scope.days)
												time = $scope.days * 1440;
											if($scope.startDate)
												startDate = $scope.startDate;
											$http({
												method:"POST",
												url:url+"/customerOperation/setDefaultMeterReadingInterval",
												params :{siteList:allSelectedSiteIdsList,mriInMins:time,startDate:startDate},
											}).success(function(data){
												$scope.siteIdsList = [];
												if(angular.equals(data,'success')){
													$scope.successMessage = "MRI Successfully Set!";
													$scope.isSuccess = true;
													window.setTimeout(function() {
														window.location.reload();
													}, 2000);
												}else{
													$scope.errorMessage = "Cannot set selected Value as MRI because its exceeding your Data Plan!";
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
											}).error(function(data,status){

											});
										}
									}
								},
								resolve: {
									allSelectedSiteIdsList: function () {
										return allSelectedSiteIdsList;
									}
								},
								templateUrl: url+'/popup',
								windowClass: 'app-modal-window'
							});
						}else{
							swal("Warning!", "Please select atleast one checkbox !", "warning")
						}
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
			if($scope.siteIdsList.length > 0){
				var allSelectedSiteIdsList = [];
				allSelectedSiteIdsList = $scope.siteIdsList;
				var ModalInstance = $modal.open({
					controller: function($uibModalInstance ,$scope,allSelectedSiteIdsList){
						$scope.daysList = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31];
						$scope.hoursList = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23];
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

						$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
						$scope.format = $scope.formats[0];
						$scope.altInputFormats = ['M!/d!/yyyy'];

						$scope.popup1 = {
								opened: false
						};
						// Datepicker code ends
						
						$scope.close = function () {
							$uibModalInstance.dismiss('cancel');
						};
						$scope.update = function () {
							if(!$scope.mins && !$scope.hours && !$scope.days && !$scope.startDate){
								swal("Warning!", "Please select any one value !", "warning")
							}
							else{
								var time = null;
								var startDate = null;
								if($scope.hours)
									time = $scope.hours*60;
								if($scope.mins)
									time = $scope.mins;
								if($scope.days)
									time = $scope.days * 1440;
								if($scope.startDate)
									startDate = $filter('date')($scope.startDate,"yyyy-MM-dd");
								$http({
									method:"POST",
									url:url+"/customerOperation/setDefaultMeterReadingInterval",
									params :{siteList:allSelectedSiteIdsList,mriInMins:time,startDate:startDate},
								}).success(function(data){
									$scope.siteIdsList = [];
									if(angular.equals(data,'success')){
										$scope.successMessage = "MRI Successfully Set!";
										$scope.isSuccess = true;
										window.setTimeout(function() {
											window.location.reload();
										}, 2000);
									}else{
										$scope.errorMessage = "Cannot set selected Value as MRI because its exceeding your Data Plan!";
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
								}).error(function(data,status){

								});
							}
						}
					},
					resolve: {
						allSelectedSiteIdsList: function () {
							return allSelectedSiteIdsList;
						}
					},
					templateUrl: url+'/popup',
					windowClass: 'app-modal-window'
				});
			}else{
				swal("Warning!", "Please select atleast one checkbox !", "warning")
			}
		}

	}

	$scope.superUserRequire = function(consumerId){
		$http({
			method: "GET",
			url: url + "/superUserRequire",
		}).success(function(data){
			if(angular.equals(data, 'true')){
				$scope.superUserRequireAuthentication();
			}else{
				if(confirm('Want to delete customer with id '+consumerId+' ?')){
					alert("delete");
				}else{
					alert("not delete");
				}
			}
		}).error(function(data){
			alert(data);
		})
	}

	$scope.function1 = function(){
		alert("called");
	}

	$scope.superUserRequireAuthentication = function(){
		var ModalInstance = $modal.open({
			controller: function($uibModalInstance ,$scope){
				$scope.close = function () {
					$uibModalInstance.dismiss('cancel');
				};
			},
			templateUrl: url+'/superUserAuthenticationRequire',
		});
	}

	$scope.selectAllFun = function(){
		if($scope.siteIdsList.length >0){
			$scope.siteIdsList = [];
		}
		if($scope.selectAll == true){
			$scope.selectAll = false;
			$scope.siteIdsList = [];
		}else if($scope.selectAll == false){
			$scope.selectAll = true;
			for(count=0;count<$scope.siteList.length;count++){
				$scope.siteIdsList.push($scope.siteList[count].siteId);
			}
		}
	}

	$scope.openAbnormalActionGraph = function (registerId,consumerAccountNo) {

		var ModalInstance = $modal.open({
			size: 'lg',
			controller: function($uibModalInstance,$scope,registerId,consumerAccountNo){
				$scope.init = function(){
					$scope.consumerAccountNo = consumerAccountNo;
						$http({
							method: "POST",
							url: url + "/abnormalConsumptionReportActionGraph",
							params: {registerId:registerId},
						}).success(function(data){
							$scope.graphActionData = data;
							var todayDate = new Date();
							var previousDay = new Date(todayDate);
							previousDay.setDate(todayDate.getDate()-1);
							$scope.displayDate = previousDay.getDate()+"-"+previousDay.getMonth()+"-"+previousDay.getFullYear();
							drawChart($scope.graphActionData);
						}).error(function(data){
							alert(data);
						})
				}

				$scope.close = function () {
					$uibModalInstance.dismiss('cancel');
				};
				$scope.update = function () {

				}
				function drawChart(datas) {
					$scope.chartShow = true;

					var data = new google.visualization.DataTable();
					data.addColumn('string', 'Time(24-Hrs)');
					data.addColumn('number', 'Data-Usage');

					console.log(datas);
					console.log(JSON.parse(datas));
					data.addRows(JSON.parse(datas));


					var options = {
							width: 850,
							height: 500,
							legend: { position: 'top', maxLines: 3, textStyle: {color: 'black', fontSize: 12 } },
							isStacked: true,


							chart: {
								title: 'Network Consumption Chart',
								subtitle: 'distance on the left, brightness on the right'
							},
					};

					var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
					chart.draw(data, options);

				}

			},
			resolve: {
				registerId: function () {
					return registerId;
				},
				consumerAccountNo: function(){
					return consumerAccountNo;
				}
			},
			templateUrl: url+'/abnormalConsumptionActionGraph',
		});

	}

}]);