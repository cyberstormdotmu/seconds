/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalAdmin.views.addAdmin')
        .controller('shoalAdmin.views.addAdmin.AddAdminController', function (shoalAdmin_buyers_BuyerAdminService) {
            var vm = this,
                service = shoalAdmin_buyers_BuyerAdminService,
                submitAddAdminForm = function () {
                    vm.submitAttempted = true;
                    if (vm.addAdminForm.$valid && !vm.submitting) {
                        vm.submitting = true;
                        vm.errorMessage = undefined;
                        vm.successMessage = undefined;
                        service.addNewAdminRequest({
                            firstname: vm.firstName,
                            surname: vm.surname,
                            emailAddress: vm.emailAddress,
                            password: vm.password,
                            mobileNumber: vm.mobileNumber
                        })
                            .then(function (details) {
                                vm.successMessage = 'New admin added successfully. Now you will login with this username : ' + details.payload.username;
                                vm.submitting = false;
                                vm.submitAttempted = false;
                            }, function (reason) {
                                vm.errorMessage = 'New admin could not added at this time, please try again later.';
                                vm.submitting = false;
                                vm.submitAttempted = false;
                            });
                    }
                };
            vm.submitAddAdminForm = submitAddAdminForm;
        });
}());