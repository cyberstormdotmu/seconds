/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalApp.views.account.addresses')
        .controller('shoalApp.views.account.addresses.AddressesController', function ($rootScope, $scope, Notification, shoalApp_address_AddressService, buyerProfile) {
            var vm = this,
                init = function () {
                    vm.addButtonText = "Add Address";
                    vm.isAdd = true;
                    vm.editIndex = 0;
                    vm.addresses = [];
                    var i,
                        length = buyerProfile.form.addresses.length;
                    for (i = 0; i < length; i += 1) {
                        vm.addresses[i] = buyerProfile.form.addresses[i];
                        vm.addresses[i].organisationName = buyerProfile.form.organisation.name;
                    }
                    vm.address = shoalApp_address_AddressService.buildAddressForm();
                };
            vm.errorMessage = '';
            vm.editAddress = function (index, address) {
                vm.editIndex = index;
                vm.addButtonText = "Edit Address";
                vm.isAdd = false;
                vm.address.form.address = angular.merge(vm.address.form.address, address);
                $scope.addressForm.delivery_department.$setValidity('serverError', true);
            };
            vm.deleteAddress = function (address) {
                vm.address.form.address = angular.merge(vm.address.form.address, address);
                vm.address.form.delete().then(function (id) {
                    var index = vm.addresses.indexOf(address);
                    vm.addresses.splice(index, 1);
                    vm.successMessage = 'Address Deleted Successfully';
                    vm.errorMessage = "";
                }, function (error) {
                    vm.errorMessage = error.data.message;
                    vm.successMessage = "";
                });
            };
            vm.departmentNameChange = function () {
                $scope.addressForm.delivery_department.$setValidity('serverError', true);
                vm.errorMessageReson = '';
            };
            vm.addressSave = function () {
                console.log(vm.isAdd);
                if (vm.isAdd) {
                    vm.address.form.save().then(function (address) {
                        vm.addresses.push(address);
                        vm.clear();
                        vm.successMessage = 'Address Saved Successfully';
                        vm.errorMessage = "";
                    }, function (error) {
                        console.log(error.data.message);
                        $scope.addressForm.delivery_department.$setValidity('serverError', false);
                        vm.errorMessageReson = error.data.message;
                        vm.errorMessage = 'Address can not save at time. please try again later';
                        vm.successMessage = "";
                    });
                } else {
                    vm.address.form.edit().then(function (address) {
                        console.log(address);
                        vm.addresses[vm.editIndex] = address;
                        vm.addButtonText = "Add Address";
                        vm.clear();
                        vm.successMessage = 'Address Edited Successfully';
                        vm.errorMessage = "";
                    }, function (error) {
                        $scope.addressForm.delivery_department.$setValidity('serverError', false);
                        vm.errorMessageReson = error.data.message;
                        vm.errorMessage = 'Address can not edit at time. please try again later';
                        vm.successMessage = "";
                    });
                }
            };
            vm.clear = function () {
                vm.address.form.address = {
                    id: '',
                    departmentName: '',
                    buildingName: '',
                    streetAddress: '',
                    locality: 'GB',
                    postTown: '',
                    postcode: ''
                };
                $scope.addressForm.$setPristine();
                $scope.addressForm.$setUntouched();
            };
            init();
        });
}());