/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.addressPicker')
        .directive('shoAddressPicker', function ($uibModal) {
            return {
                restrict: 'A',
                templateUrl: '../shared/components/addressPicker/addressPickerView.html',
                scope: {},
                bindToController: {
                    selectedAddressKey: '=',
                    selectedAddress: '=',
                    addresses: '='
                },
                controllerAs: 'vm',
                controller: function ($scope, $rootScope) {
                    var vm = this,
                        modalConfig = {
                            templateUrl: '../shared/components/addressPicker/newAddressView.html',
                            controller: 'shoalCommon.addressPicker.controller as vm',
                            size: 'lg'
                        };
                    vm.openAddressModal = function () {
                        $uibModal.open(modalConfig);
                    };
                    $scope.$watch(function () {
                        return vm.selectedAddressKey;
                    }, function () {
                        if (vm.selectedAddressKey) {
                            vm.selectedAddress = vm.addresses[vm.selectedAddressKey];
                        } else {
                            vm.selectedAddress = undefined;
                        }
                    });
                    $rootScope.$on('newAddressAdd', function (event, address) {
                        var newAddress = {};
                        newAddress.buildingName = address.buildingName;
                        newAddress.departmentName = address.departmentName;
                        newAddress.id = address.id;
                        newAddress.locality = address.locality;
                        newAddress.organisationName = address.organisationName;
                        newAddress.postTown = address.postTown;
                        newAddress.postcode = address.postcode;
                        newAddress.streetAddress = address.streetAddress;
                        vm.addresses[newAddress.departmentName] = newAddress;
                        vm.addresses[newAddress.departmentName].organisationName = newAddress.organisationName;
                        vm.selectedAddressKey = newAddress.departmentName;
                        vm.selectedAddress = vm.addresses[vm.selectedAddressKey];
                    });
                }
            };
        });
}());