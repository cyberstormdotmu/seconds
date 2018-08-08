/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.views.manageProducts')
        .controller('shoalAdmin.views.manageProducts.CreateVendorController', function (Notification, $scope, $rootScope,  $modalInstance, shoalAdmin_manageProducts_ManageProductsService) {
            var vm = this,
                manageProductsService = shoalAdmin_manageProducts_ManageProductsService;
            vm.errorMessage = '';
            vm.vendor = shoalAdmin_manageProducts_ManageProductsService.VendorForm();
            vm.close = function () {
                $modalInstance.close();
            };
            vm.newVendorSave = function () {
                manageProductsService.vendorSave(vm.VendorForm).then(function (vendorForm) {
                    Notification.success('Principal created successfully!', {
                        delay: 30000
                    });
                    vm.close();
                }, function (error) {
                    $scope.VendorForm.vendorName.$setValidity('serverError', false);
                    vm.errorMessage = error.data.message;
                });
            };
        });
}());