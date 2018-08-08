/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.basket')
        .controller('shoalApp.views.basket.BasketController', function ($rootScope, $window, $scope, $location, $modalInstance, shoalApp_basket_BasketService) {
            var vm = this,
                basketService = shoalApp_basket_BasketService,
                updateScopeWithBasket = function (event, newBasket) {
                    console.log('BasketController -> handling a basket update');
                    if (newBasket) {
                        vm.basket = newBasket;
                    }
                },
                init = function () {
                    $scope.$on('basketUpdated', updateScopeWithBasket);
                    basketService.synchroniseBasket();
                },
                closeModal = function () {
                    $modalInstance.close();
                },
                proceedWithOrder = function () {
                    console.log("user clicked proceed - redirect to checkout page");
                    basketService.synchroniseBasket();
                    $modalInstance.close();
                    $location.path('/checkout');
                };

            init();
            console.log("----------------registrationToken basket--------------");
            console.log($window.sessionStorage.getItem('registrationToken'));
            $rootScope.registrationTokenDisableBasketIcon = $window.sessionStorage.getItem('registrationToken');
            $rootScope.pickBuyerByAdminForPlaceOrder = $window.sessionStorage.getItem('pickBuyerByAdminForPlaceOrder');
            vm.close = closeModal;
            vm.proceed = proceedWithOrder;
        });
}());
