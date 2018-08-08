/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalAdmin.views.supplierCreditIntegration')
        .controller('shoalAdmin.views.supplierCreditIntegration.supplierCreditIntegrationListController', function ($q, $http,  Notification, shoalAdmin_supplierCreditIntegration_supplierCreditIntegrationService, $uibModal) {
            var vm = this,
                supplierCreditIntegrationList = shoalAdmin_supplierCreditIntegration_supplierCreditIntegrationService,
                fetchsupplierCreditIntegrationList = function () {
                    supplierCreditIntegrationList.getsupplierCreditIntegrationList()
                        .then(function (vendorList) {
                            vm.vendorList = vendorList;
                            console.log(vm.vendorList);
                        }, function () {
                            console.log("error reading vendorList Requests");
                        });
                },
                saveTermsAndCondition = function (registrationForm) {
                    supplierCreditIntegrationList.EditTermsAndCondition(registrationForm)
                        .then(function (response) {
                            if (response.status === 200) {
                                vm.successMessage = "Vendor terms and conditions successfully updated.";
                            }
                        }, function () {
                            vm.errorMessage = "Error while saving vendor terms and conditions.";
                            console.log("Error while saving vendor terms and conditions.");
                        });
                },
                termsAndConditionfetch = function (vendorName) {
                    angular.forEach(vm.vendorList, function (value, key) {
                        if (value.name === vendorName) {
                            vm.termsAndCondition = value.termsAndCondition;
                            vm.id = value.id.id;
                        }
                    });
                };
            fetchsupplierCreditIntegrationList();
            vm.termsAndConditionfetch = termsAndConditionfetch;
            vm.saveTermsAndCondition = saveTermsAndCondition;
        });
}());