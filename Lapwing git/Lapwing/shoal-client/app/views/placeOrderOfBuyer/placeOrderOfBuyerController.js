/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.placeOrderOfBuyer')
        .controller('shoalApp.views.placeOrderOfBuyer.PlaceOrderOfBuyerController', function ($rootScope, $window, $modalInstance, shoalApp_placeOrder_BuyerService) {
            var vm = this,
                service = shoalApp_placeOrder_BuyerService,
                submitSerchForm = function () {
                    vm.submitAttempted = true;
                    if ((vm.firstName === null || vm.firstName === "") && (vm.lastname === null || vm.lastname === "") && (vm.emailAddress === null || vm.emailAddress === "")) {
                        vm.errorMessage = 'Please enter atleast one search criteria to get search result.';
                        vm.submitting = false;
                        vm.submitAttempted = false;
                    } else {
                        vm.submitting = true;
                        service.submitSerchBuyerRequest({
                            firstName: vm.firstName,
                            lastName: vm.lastname,
                            emailAddress: vm.emailAddress
                        }).then(function (searchedBuyerList) {
                            vm.searchedBuyerList = searchedBuyerList;
                        }, function () {
                            console.log("error fetching searchedBuyerList");
                        });
                    }
                },
                pickUser = function (emailAddress) {
                    service.pickSerchBuyerRequest({
                        username: emailAddress
                    }).then(function (userPicked) {
                        vm.userPicked = userPicked;
                        $window.sessionStorage.setItem("pickBuyerByAdminForPlaceOrder", userPicked.username);
                        console.log("$window.sessionStorage.getItem('pickBuyerByAdminForPlaceOrder')");
                        console.log($window.sessionStorage.getItem("pickBuyerByAdminForPlaceOrder"));
                    }, function () {
                        console.log("error fetching searchedBuyerList");
                    });
                },
                closeModal = function () {
                    $modalInstance.close();
                };
            vm.close = closeModal;
            vm.firstName = "";
            vm.lastname = "";
            vm.emailAddress = "";
            vm.serchForm = submitSerchForm;
            vm.pickUser = pickUser;
        });
}());/*if ((vm.firstName === null || vm.firstName === undefined || vm.firstName === "") && (vm.lastname === null || vm.lastname === undefined || vm.lastname === "") && (vm.emailAddress === null || vm.emailAddress === undefined || vm.emailAddress === "")) {*/