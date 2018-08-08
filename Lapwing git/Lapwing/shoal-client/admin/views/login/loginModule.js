/*global angular */
(function () {
    'use strict';
    angular.module('shoalAdmin.views.login', ['shoalCommon.security'])
        .config([function ($stateProvider) {
            $stateProvider
                .state('login', {
                    url: '/login',
                    templateUrl: 'views/login/loginView.html',
                    controller: 'shoalAdmin.views.login.LoginController'
                })
                .state('logout', {
                    url: '/logout',
                    template: '',
                    controller: 'shoalAdmin.views.login.LogoutController'
                });
        }]);
}());
