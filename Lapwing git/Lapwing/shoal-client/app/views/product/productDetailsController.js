/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalApp.views.product')
        .controller('shoalApp.views.product.ProductDetailsController', function initProductDetailsController($rootScope, $window, $scope, $stateParams,
                                                                                                             shoalCommon_products_ProductService,
                                                                                                             shoalApp_basket_BasketService,
                                                                                                             shoalApp_views_basket_BasketViewModal) {

            var productService = shoalCommon_products_ProductService,
                basketService = shoalApp_basket_BasketService,
                basketView = shoalApp_views_basket_BasketViewModal,
                productCode = $stateParams.code,
                basketItem,
                vm = this,
                handleBasketChange = function (event, updatedBasket) {
                    console.log('product detail page -> handle basket change');
                    if (vm.product) {
                        vm.isProductInBasket = updatedBasket.containsItem({
                            productCode: vm.product.code,
                            stock: vm.product.stock
                        });
                    } else {
                        vm.isProductInBasket = false;
                    }
                    if (!vm.loadedBasket) {
                        vm.loadedBasket = true;
                    }

                    if (vm.isProductInBasket) {
                        // update basket related info for this product
                        basketItem = updatedBasket.items[productCode];

                        vm.order.quantity = basketItem.quantity;
                    } else {
                        vm.order.quantity = 1;
                    }
                },
                fetchProduct = function () {
                    productService.findByCode(productCode)
                        .then(function (product) {
                            console.log('product ' + productCode + ' retrieved from server');
                            console.log("-------------product desc-------------");
                            console.log(product.maximumPurchaseLimit);
                            $rootScope.maximumProductOrderLimit = product.stock > product.maximumPurchaseLimit ? product.maximumPurchaseLimit : product.stock;
                            vm.product = product;
                            vm.order.product = product;
                        }, function () {
                            console.log("error reading products");
                        });
                },
                listenToBasket = function () {
                    $scope.$on('basketUpdated', handleBasketChange);
                    basketService.synchroniseBasket();
                },
                handleAddItemToBasket = function () {
                    vm.addToBasket = function (product, order) {
                        basketService.addItemToBasket(product, order);
                        basketView.show();
                    };
                },
                handleUpdateQuantity = function () {
                    vm.updateQuantity = function () {
                        basketItem.bindQuantity = vm.order.quantity;
                        basketService.synchroniseBasket();
                    };
                };

            vm.loadedBasket = false;
            $rootScope.registrationTokenProductDisable = $window.sessionStorage.getItem('registrationToken');
            if (productCode !== undefined) {
                vm.order = Object.create({}, {
                    quantity: {
                        get: function () {
                            return this.qty;
                        },
                        set: function (qty) {
                            this.qty = qty;
                            if (this.prod) {
                                this.price = this.prod.calculatePricing(this.qty);
                            }
                        }
                    },
                    pricing: {
                        get: function () {
                            return this.price;
                        }
                    },
                    product: {
                        get: function () {
                            return this.prod;
                        },
                        set: function (prod) {
                            if (this.qty) {
                                this.price = prod.calculatePricing(this.qty);
                            }
                            this.prod = prod;
                        }
                    }
                });

                $rootScope.pickBuyerByAdminForPlaceOrder = $window.sessionStorage.getItem('pickBuyerByAdminForPlaceOrder');
                vm.order.quantity = 1;

                fetchProduct();

                listenToBasket();

                handleAddItemToBasket();

                handleUpdateQuantity();
            }
        });
}());
