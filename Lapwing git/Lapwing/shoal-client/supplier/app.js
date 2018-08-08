/*global angular, console */
(function () {
    'use strict';

// Declare app level module which depends on views, and components
    angular.module('shoalSupplier', [
        'ui.router',
        'smoothScroll',
        'ngMessages',
        'shoal.config',
        'shoalCommon.menu',
        'shoalCommon.form',
        'shoalCommon.pageAnchor',
        'shoalCommon.filters',
        'shoalCommon.classes',
        'shoalCommon.validator',
        'ui.bootstrap',
        'shoalSupplier.views.login',
        'shoalSupplier.views.twoFactorAuthentication',
        'shoalSupplier.views.menu',
        'shoalSupplier.views.registrations',
        'shoalSupplier.views.principalDashboard',
        'shoalSupplier.views.orders',
        'shoalSupplier.views.supplierCreditIntegration',
        'shoalSupplier.views.buyerList',
        'shoalSupplier.twoFactorAuthentication',
        'shoalSupplier.views.activeOffers',
        'shoalSupplier.frame',
        'shoalSupplier.buyers',
        'shoalSupplier.classes',
        'shoalSupplier.orders',
        'shoalSupplier.offers',
        'shoalSupplier.supplierCreditIntegration'
    ])
        .config([function ($urlRouterProvider, $locationProvider, $httpProvider, shoalCommon_security_LoginRedirectServiceProvider) {

            $urlRouterProvider.otherwise('/login');
            $locationProvider.html5Mode(false);
            //$locationProvider.hashPrefix('!');
            shoalCommon_security_LoginRedirectServiceProvider.setLoginPageUri('/supplier/#/login');
        }])
        .run(function (shoalCommon_security_RouteChangeService) {
            shoalCommon_security_RouteChangeService.handleRouteChange();
        });
}());