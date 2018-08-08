/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.basket').
        factory('shoalApp_views_basket_BasketViewModal', function ($uibModal, shoalApp_basket_BasketService) {
            var modalConfig = {
                    templateUrl: 'views/basket/basketView.html',
                    controller: 'shoalApp.views.basket.BasketController as vm',
                    size: 'lg'
                },
                basketService = shoalApp_basket_BasketService,
                handleResult = function () {
                    console.log('basket modal closed');
                    basketService.synchroniseBasket();
                };

            return {
                show: function () {
                    $uibModal.open(modalConfig).
                        result.then(handleResult, handleResult);
                }
            };
        });
}());
