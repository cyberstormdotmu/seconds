/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.views.registration').
        factory('shoalPublic_views_registration_RegistrationViewModal', function ($uibModal) {
            var modalConfig = {
                templateUrl: 'views/registration/registrationView.html',
                controller: 'shoalPublic.views.registration.RegistrationController as vm',
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
