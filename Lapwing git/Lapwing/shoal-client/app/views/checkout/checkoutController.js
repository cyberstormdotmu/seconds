/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.checkout')
        .controller('shoalApp.views.checkout.CheckoutController', function ($rootScope, $window, $state, $scope, $log, shoalApp_checkout_CheckoutService,
                                                                            shoalApp_basket_BasketService,
                                                                            buyerProfile, userInfo) {

            var vm = this,
                checkoutService = shoalApp_checkout_CheckoutService,
                basketService = shoalApp_basket_BasketService,
                init = function () {
                    basketService.synchroniseBasket();
                    checkoutService.init();
                    vm.order = checkoutService.order;

                    $log.debug("buyerProfile.form.isCompleted=" + buyerProfile.form.isCompleted);
                    if (buyerProfile.form.isCompleted) {
                        $state.go('checkout.basket');
                    } else {
                        vm.userInfo = userInfo;
                        vm.profile = buyerProfile;
                        $state.go('checkout.profile');
                    }

                    $scope.$on('$stateChangeStart',
                        function resumeCurrentStep(event, toState, toParams, fromState, fromParams) {
                            if (toState.name === 'checkout' && vm.currentState) {
                                event.preventDefault();
                                $state.go(vm.currentState);
                            }
                        });
                    $scope.$on('$stateChangeSuccess',
                        function updateCurrentState(event, toState, toParams, fromState, fromParams) {
                            console.log("current step " + toState.name);
                            vm.currentState = toState.name;
                        });

                    $scope.$on('$stateChangeSuccess',
                        function updateBasketOnLeavingBasketEditPage(event, toState, toParams, fromState, fromParams) {
                            if (fromState.name === 'checkout.basket') {
                                basketService.synchroniseBasket();
                            }
                        });


                    $scope.$on('buyerProfileChange', function (ignore, buyerProfile) {
                        if (buyerProfile.form.isCompleted) {
                            $state.go('checkout.basket');
                        }
                    });
                };

            $rootScope.pickBuyerByAdminForPlaceOrder = $window.sessionStorage.getItem('pickBuyerByAdminForPlaceOrder');
            init();
        });
}());