/*global angular */
(function () {
    'use strict';
    angular.module('shoalApp.error', ['shoalCommon.security'])
        .config([function ($stateProvider) {
            $stateProvider
                .state('error', {
                    url: '/error',
                    templateUrl: 'views/error/errorView.html',
                    controller: 'shoalApp.views.error.ErrorController',
                    controllerAs: 'vm'
                });
        }]);
}());
