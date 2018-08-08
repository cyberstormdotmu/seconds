/*global angular */
(function () {
    'use strict';
    angular.module('shoalApp.views.logout', ['ui.router', 'shoalCommon.security'])
        .config([function ($stateProvider) {
            $stateProvider
                .state('logout', {
                    url: '/logout',
                    controller: 'shoalApp.views.logout.LogoutController',
                    templateUrl: 'views/logout/logoutView.html'
                });
        }]);
}());
