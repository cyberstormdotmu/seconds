/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.shopFront', ['ui.router.state', 'shoalApp.profile'])
        .config(function ($stateProvider) {
            $stateProvider
                .state('shopFront', {
                    url: '/shopfront',
                    templateUrl: 'views/shopfront/shopFrontView.html',
                    controller: 'shoalApp.views.shopFront.ShopFrontController',
                    controllerAs: 'vm',
                    data: {
                        access: {
                            requiresAuthorisation: true
                            // specify permissions here
                            //requirePermissions: ['User']
                            //permissionType: 'AtLeastOne'
                        }
                    },
                    resolve: {
                        buyerProfile: function (shoalApp_profile_ProfileService) {
                            return shoalApp_profile_ProfileService.fetchBuyerProfile();
                        }
                    }
                });
        });
}());
