/*global angular */
(function () {
    'use strict';
    angular.module('shoalSupplier.views.login', ['shoalCommon.security'])
        .config([function ($stateProvider) {
            $stateProvider
                .state('login', {
                    url: '/login',
                    templateUrl: 'views/login/loginView.html',
                    controller: 'shoalSupplier.views.login.LoginController'
                })
                .state('logout', {
                    url: '/logout',
                    template: '',
                    controller: 'shoalSupplier.views.login.LogoutController'
                });
        }]);
}());
