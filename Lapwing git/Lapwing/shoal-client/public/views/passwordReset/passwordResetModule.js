/*global angular */
(function () {
    'use strict';
    angular.module('shoalPublic.views.passwordReset', ['ui.router.state', 'shoalPublic.registration', 'shoalCommon.validator', 'shoalPublic.passwordReset'])
        .config(function ($stateProvider) {
            $stateProvider
                .state('passwordreset', {
                    url: '/passwordreset',
                    templateUrl: 'views/passwordReset/passwordResetView.html',
                    redirectTo: 'passwordreset.initiate'
                })
                .state('passwordreset.initiate', {
                    url: '',
                    templateUrl: 'views/passwordReset/initiate/passwordResetInitiateView.html',
                    controller: 'shoalPublic.views.passwordReset.PasswordResetInitiateController',
                    controllerAs: 'vm'
                })
                .state('passwordreset.confirm', {
                    url: 'confirm',
                    templateUrl: 'views/passwordReset/confirm/passwordResetConfirmView.html',
                    controller: 'shoalPublic.views.passwordReset.PasswordResetConfirmController',
                    controllerAs: 'vm'
                })
                .state('passwordreset.complete', {
                    url: 'complete',
                    templateUrl: 'views/passwordReset/complete/passwordResetCompleteView.html'
                });
        });
}());
