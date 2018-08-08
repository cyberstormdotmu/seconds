/*global angular */
(function () {
    'use strict';
    angular.module('shoalApp.views.checkout', ['ui.router.state', 'shoalApp.checkout', 'shoalApp.profile',
        'shoalCommon.addressPicker', 'shoalCommon.address', 'shoalCommon.form'])
        .config([function ($stateProvider) {
            $stateProvider
                .state('checkout', {
                    url: '/checkout',
                    templateUrl: 'views/checkout/checkoutView.html',
                    controller: 'shoalApp.views.checkout.CheckoutController',
                    controllerAs: 'vm',
                    resolve: {
                        buyerProfile: function (shoalApp_profile_ProfileService) {
                            return shoalApp_profile_ProfileService.fetchBuyerProfile();
                        },
                        userInfo: function (shoalCommon_security_AuthService) {
                            return shoalCommon_security_AuthService.getCurrentUser();
                        }
                    },
                    data: {
                        access: {
                            requiresAuthorisation: true
                            // specify permissions here
                            //requirePermissions: ['User']
                            //permissionType: 'AtLeastOne'
                        }
                    }
                })
                .state('checkout.profile', {
                    url: '/profile',
                    templateUrl: 'views/partials/profile/profilePartial.html'
                })
                .state('checkout.basket', {
                    url: '/basket',
                    templateUrl: 'views/checkout/basket/checkoutBasketPartial.html',
                    controller: 'shoalApp.views.checkout.CheckoutBasketController',
                    controllerAs: 'vm'
                })
                .state('checkout.termsandconditions', {
                    url: '/termsandconditions',
                    templateUrl: 'views/checkout/termsandconditions/checkoutBasketPartial.html',
                    controller: 'shoalApp.views.checkout.CheckoutTermsandconditionsController',
                    controllerAs: 'vm'
                })
                .state('checkout.addresses', {
                    url: '/addresses',
                    templateUrl: 'views/checkout/addresses/checkoutAddressesPartial.html',
                    controller: 'shoalApp.views.checkout.CheckoutAddressesController',
                    controllerAs: 'vm'
                })
                .state('checkout.payment', {
                    url: '/payment',
                    templateUrl: 'views/checkout/payment/checkoutPaymentPartial.html',
                    controller: 'shoalApp.views.checkout.CheckoutPaymentController',
                    controllerAs: 'vm'
                })
                .state('checkout.orderverification', {
                    url: '/orderverification',
                    templateUrl: 'views/checkout/orderverification/checkoutOrderVerificationPartial.html',
                    controller: 'shoalApp.views.checkout.CheckoutOrderVerificationController',
                    controllerAs: 'vm'
                })
                .state('checkout.review', {
                    url: '/review',
                    templateUrl: 'views/checkout/review/checkoutReviewPartial.html',
                    controller: 'shoalApp.views.checkout.CheckoutReviewController',
                    controllerAs: 'vm'
                })
                .state('checkout.finished', {
                    url: '/finished',
                    templateUrl: 'views/checkout/finished/checkoutFinishedPartial.html'
                });
        }]);
}());
