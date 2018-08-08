/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.product')
        .controller('shoalApp.views.product.ProductListingController', function ($rootScope, $window, $scope, $stateParams, shoalCommon_products_ProductService, shoalApp_basket_BasketService) {
            var vm = this,
                ProductsService = shoalCommon_products_ProductService,
                i,
                basketinfo,
                basketService = shoalApp_basket_BasketService,
                category = $stateParams.category || 'Products',
                fetchProducts = function () {
                    basketService.synchroniseBasket();
                    $scope.$on('basketUpdated', function (event, response) {
                        basketinfo = basketService.getBasket();
                        ProductsService.fetchByTopCategory(category)
                            .then(function (products) {
                                vm.products = products;
                                for (i = 0; i < vm.products.length; i += 1) {
                                    if (basketinfo !== null && basketinfo.items[vm.products[i].code] !== undefined) {
                                        vm.products[i].basket = basketinfo.items[vm.products[i].code];
                                    }
                                }
                            }, function () {
                                console.log("error retrieving product listing");
                            });
                    });
                    ProductsService.fetchByTopCategory(category)
                        .then(function (products) {
                            vm.products = products;
                        });
                },
                fetchCategory = function () {
                    ProductsService.fetchCategory(category)
                        .then(function (result) {
                            vm.currentCategory = result.name;
                            vm.parentCategories = result.parents;
                            console.log("$window.sessionStorage.getItem('pickBuyerByAdminForPlaceOrder')");
                            console.log($window.sessionStorage.getItem('pickBuyerByAdminForPlaceOrder'));
                        }, function () {
                            console.log("error retrieving category");
                        });
                };
            $rootScope.registrationTokenDisableBasketIcon = $window.sessionStorage.getItem('registrationToken');
            $rootScope.pickBuyerByAdminForPlaceOrder = $window.sessionStorage.getItem('pickBuyerByAdminForPlaceOrder');
            fetchCategory();
            fetchProducts();
        });
}());
