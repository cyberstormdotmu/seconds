/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.account', ['ui.router', 'shoalApp.views.partial.profile',
            'shoalApp.views.account.orders',
            'shoalApp.views.account.credits',
            'shoalApp.views.account.addresses'
        ])
        .config([function ($stateProvider) {
            $stateProvider
                .state('account', {
                    url: '/account',
                    templateUrl: 'views/account/accountView.html',
                    redirectTo: 'account.profile',
                    data: {
                        access: {
                            requiresAuthorisation: true
                            // specify permissions here
                            //requirePermissions: ['User']
                            //permissionType: 'AtLeastOne'
                        }
                    }
                })
                .state('account.profile', {
                    url: '',
                    templateUrl: 'views/partials/profile/profilePartial.html',
                    controller: 'shoalApp.views.partial.profile.ProfileController as vm',
                    resolve: {
                        buyerProfile: function (shoalApp_profile_ProfileService) {
                            return shoalApp_profile_ProfileService.fetchBuyerProfile(false);
                        },
                        userInfo: function (shoalCommon_security_AuthService) {
                            return shoalCommon_security_AuthService.getCurrentUser();
                        }
                    }
                })
                .state('account.orders', {
                    url: '/orders',
                    templateUrl: 'views/account/orders/orderSummaryPartial.html',
                    controller: 'shoalApp.views.account.orders.OrderSummaryController as vm'
                })
                .state('account.order', {
                    url: '/orders/:orderReference',
                    templateUrl: 'views/account/orders/orderDetailPartial.html',
                    controller: 'shoalApp.views.account.orders.OrderDetailController as vm'
                })
                .state('account.credits', {
                    url: '/credits',
                    templateUrl: 'views/account/credits/creditSummaryPartial.html',
                    controller: 'shoalApp.views.account.credits.CreditSummaryController as vm'
                })
                .state('account.addresses', {
                    url: '/addresses',
                    templateUrl: 'views/account/addresses/addressesPartial.html',
                    controller: 'shoalApp.views.account.addresses.AddressesController as vm',
                    resolve: {
                        buyerProfile: function (shoalApp_profile_ProfileService) {
                            return shoalApp_profile_ProfileService.fetchBuyerProfile(false);
                        }
                    }
                })
                .state('account.creditapplicationform', {
                    url: '/creditapplicationform',
                    templateUrl: 'views/account/credits/creditApplicationFormPartial.html',
                    controller: 'shoalApp.views.account.credits.CreditApplicationFormController as vm',
                    resolve: {
                        buyerProfile: function (shoalApp_profile_ProfileService) {
                            return shoalApp_profile_ProfileService.fetchBuyerProfile(false);
                        }
                    }
                })
                .state('account.suppliercreditfacility', {
                    url: '/suppliercreditfacility',
                    templateUrl: 'views/account/credits/creditApplicationFormPartial.html',
                    controller: 'shoalApp.views.account.credits.CreditApplicationFormController as vm',
                    resolve: {
                        buyerProfile: function (shoalApp_profile_ProfileService) {
                            return shoalApp_profile_ProfileService.fetchBuyerProfile(false);
                        },
                        supplierCreditFacility: function () {
                            return true;
                        }
                    }
                });
        }]);
}());
