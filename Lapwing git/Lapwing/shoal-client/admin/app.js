/*global angular, console */
(function () {
    'use strict';

// Declare app level module which depends on views, and components
    angular.module('shoalAdmin', [
        'ui.router',
        'smoothScroll',
        'ngMessages',
        'shoal.config',
        'shoalCommon.menu',
        'shoalCommon.form',
        'shoalCommon.pageAnchor',
        'shoalCommon.filters',
        'shoalCommon.classes',
        'shoalAdmin.classes',
        'shoalAdmin.frame',
        'shoalAdmin.orders',
        'shoalAdmin.creditWithdrawRequests',
        'shoalAdmin.offers',
        'shoalAdmin.buyers',
        'shoalAdmin.views.menu',
        'shoalAdmin.views.login',
        'shoalAdmin.views.orders',
        'shoalAdmin.views.activeOffers',
        'shoalAdmin.views.registrations',
        'shoalAdmin.views.manageProducts',
        'shoalAdmin.views.creditWithdrawRequests',
        'shoalAdmin.views.buyerList',
        'shoalAdmin.views.principalDashboard',
        'shoalCommon.validator',
        'shoalAdmin.views.twoFactorAuthentication',
        'shoalAdmin.twoFactorAuthentication',
        'ui.bootstrap',
        'shoalAdmin.manageProductCatagory',
        'shoalApp.views.manageProductCategory',
        'shoalAdmin.views.addAdmin',
        'shoalAdmin.views.supplierCreditIntegration',
        'shoalAdmin.supplierCreditIntegration'
    ])
        .config([function ($urlRouterProvider, $locationProvider, $httpProvider, shoalCommon_security_LoginRedirectServiceProvider) {

            $urlRouterProvider.otherwise('/login');
            $locationProvider.html5Mode(false);
            //$locationProvider.hashPrefix('!');

            shoalCommon_security_LoginRedirectServiceProvider.setLoginPageUri('/admin/#/login');
        }])
        .run(function (shoalCommon_security_RouteChangeService) {
            shoalCommon_security_RouteChangeService.handleRouteChange();
        });
}());