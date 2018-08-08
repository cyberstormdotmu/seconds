/*global angular, console */
(function () {
    'use strict';

// Declare app level module which depends on views, and components
    angular.module('shoalApp', [
        'ui.router',
        'ngMessages',
        'smoothScroll',
        'shoal.config',
        'shoalCommon.menu',
        'shoalCommon.frame',
        'shoalCommon.pageAnchor',
        'shoalCommon.security',
        'shoalCommon.form',
        'shoalCommon.filters',
        'shoalCommon.formatter',
        'shoalCommon.validator',
        'shoalCommon.classes',
        'shoalApp.views.shopFront',
        'shoalApp.views.pages',
        'shoalApp.views.product',
        'shoalApp.views.account',
        'shoalApp.views.checkout',
        'shoalApp.views.basket',
        'shoalApp.views.popup',
        'shoalApp.views.error',
        'shoalApp.views.logout',
        'shoalApp.views.placeOrderOfBuyer',
        'shoalApp.error',
        'shoalApp.orderverification',
        'shoalApp.reportProblem'
    ])
        .config([function ($urlRouterProvider, $stateProvider, $locationProvider, shoalCommon_security_LoginRedirectServiceProvider) {
            $locationProvider.html5Mode(false);
            //$locationProvider.hashPrefix('!');

            $stateProvider.state('home', {
                url: '',
                resolve: {
                    buyerProfile: function (shoalApp_profile_ProfileService) {
                        return shoalApp_profile_ProfileService.fetchBuyerProfile();
                    }
                },
                controller: function ($state, buyerProfile, $scope, $location) {
                    if (!buyerProfile.form.isCompleted) {
                        $state.go('account.profile');
                    } else {
                        $state.go('shopFront');
                    }
                }
            });

            shoalCommon_security_LoginRedirectServiceProvider.setLoginPageUri('/public/#/login');
        }])
        .run(function redirectBuyerToAccountPageIfTheirProfileIsNotCompleted($location, shoalCommon_security_RouteChangeService) {
            shoalCommon_security_RouteChangeService.handleRouteChange();
        });
}());