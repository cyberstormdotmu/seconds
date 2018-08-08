/*global angular */
(function () {
    'use strict';
    angular.module('shoalAdmin.views.twoFactorAuthentication', ['shoalAdmin.twoFactorAuthentication', 'ui.router.state', 'shoalCommon.validator'])
        .config(function ($stateProvider) {
            $stateProvider
                .state('twoFactorAuthentication', {
                    url: '/twoFactorAuthentication',
                    templateUrl: 'views/twoFactorAuthentication/twoFactorAuthentication.html',
                    redirectTo: 'twoFactorAuthentication.confirm'
                })
                .state('twoFactorAuthentication.confirm', {
                    url: '/confirm',
                    templateUrl: 'views/twoFactorAuthentication/confirm/twoFactorAuthenticationConfirmView.html',
                    controller: 'shoalAdmin.views.twoFactorAuthentication.TwoFactorAuthenticationConfirmController',
                    controllerAs: 'vm'
                });
        });
}());