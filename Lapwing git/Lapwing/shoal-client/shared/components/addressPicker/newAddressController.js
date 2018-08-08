/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.addressPicker')
        .controller('shoalCommon.addressPicker.controller', function ($scope, $modalInstance, shoalApp_address_AddressService) {
            var vm = this;
            vm.errorMessage = '';
            vm.address = shoalApp_address_AddressService.buildAddressForm();
            vm.close = function () {
                $modalInstance.close();
            };
            vm.departmentNameChange = function () {
                $scope.newAddressForm.delivery_department.$setValidity('serverError', true);
                vm.errorMessage = '';
            };
            vm.newAddressSave = function () {
                vm.address.form.save().then(function (address) {
                    $modalInstance.close();
                }, function (error) {
                    $scope.newAddressForm.delivery_department.$setValidity('serverError', false);
                    vm.errorMessage = error.data.message;
                });
            };
        });
}());