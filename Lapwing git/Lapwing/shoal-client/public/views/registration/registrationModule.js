/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.views.registration', ['ui.router.state', 'shoalPublic.registration', 'shoalCommon.validator'])
        .config([function ($stateProvider) {
            $stateProvider
                .state('registration', {
                    url: '/registration/confirm/:registrationToken',
                    templateUrl: 'views/registration/registrationConfirmationView.html',
                    controller: 'shoalPublic.views.registration.RegistrationConfirmationController',
                    controllerAs: 'vm'
                });

        }]);
}());
