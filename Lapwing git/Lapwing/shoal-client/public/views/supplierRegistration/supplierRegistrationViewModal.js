/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.views.supplierregistration').
        factory('shoalPublic_views_supplierregistration_RegistrationViewModal', function ($uibModal) {
            var modalConfig = {
                templateUrl: 'views/supplierRegistration/supplierRegistrationView.html',
                controller: 'shoalPublic.views.supplierregistration.RegistrationController as vm',
                size: 'lg',
                resolve: {
                    registrationForm: function (shoalPublic_registration_RegistrationService) {
                        return shoalPublic_registration_RegistrationService.buildRegistrationForm();
                    }
                }
            };

            return {
                show: function () {
                    $uibModal.open(modalConfig);
                }
            };
        });
}());
