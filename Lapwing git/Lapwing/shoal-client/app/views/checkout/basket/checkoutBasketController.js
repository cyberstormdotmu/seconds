/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.checkout')
        .controller('shoalApp.views.checkout.CheckoutBasketController', function (shoalApp_checkout_CheckoutService,
                                                                                  shoalApp_basket_BasketService) {

            var vm = this,
                checkoutService = shoalApp_checkout_CheckoutService,
                basketService = shoalApp_basket_BasketService,
                init = function () {
                    vm.order = checkoutService.order;
                },
                removeItem = function (item) {
                    basketService.removeItemFromBasket(item);
                };

            init();

            vm.removeItem = removeItem;
        });
}());