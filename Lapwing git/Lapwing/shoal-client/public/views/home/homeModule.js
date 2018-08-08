/*global angular */
(function () {
    'use strict';
    angular.module('shoalPublic.views.home', [])
        .config(function ($stateProvider) {
            $stateProvider
                .state('home', {
                    url: '/',
                    templateUrl: '/public/views/home/homeView.html',
                    controller: 'shoalPublic.views.home.HomeController',
                    controllerAs: 'vm'
                });
        });
}());
