/*global angular */
(function () {
    'use strict';

// Declare app level module which depends on views, and components
    angular.module('shoalPublic', [
        'ui.router',
        'ngMessages',
        'smoothScroll',
        'shoal.config',
        'shoalCommon.security',
        'shoalCommon.pageAnchor',
        'shoalPublic.contact',
        'shoalPublic.views.home',
        'shoalPublic.views.login',
        'shoalPublic.views.contact',
        'shoalPublic.views.passwordReset',
        'shoalPublic.views.registration',
        'shoalPublic.views.supplierregistration',
        'shoalCommon.validator',
        'shoalCommon.form',
        'shoalPublic.template',
        'shoalPublic.views.pages',
        'ngCookies'
    ])
        .config([function ($urlRouterProvider, $locationProvider, $httpProvider) {
            $urlRouterProvider.otherwise('/');
            $locationProvider.html5Mode(false);
            //$locationProvider.hashPrefix('!');

            $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
        }])
        .run(function ($rootScope, $state, shoalCommon_security_RouteChangeService) {
            $rootScope.$state = $state;
            shoalCommon_security_RouteChangeService.handleRouteChange();
        });
}());