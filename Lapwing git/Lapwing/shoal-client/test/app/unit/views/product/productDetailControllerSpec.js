(function () {
    'use strict';
    describe('shoalApp.views.products module -> ProductDetailController', function () {

        var productDetailsController,
            vm,
            $scope,
            $stateParams,
            $rootScope,
            productService,
            basketService,
            viewingProduct = SHOAL.StaticData.buildDefaultProduct(),
            initialiseController = function () {
                // this needs to be separate from the configuration step to allow individual tests
                // more control over configuring mocks.

                productDetailsController = quickmock({
                    providerName: 'shoalApp.views.product.ProductDetailsController',
                    providerAs: 'vm',
                    moduleName: 'shoalApp.views.product',
                    mockModules: ['shoalApp.ShoalAppMocks', 'shoalApp.classes']
                });


                $stateParams = productDetailsController.$mocks.$stateParams;
                productService = productDetailsController.$mocks.shoalCommon_products_ProductService;
                basketService = productDetailsController.$mocks.shoalApp_basket_BasketService;
                $stateParams.code = viewingProduct.code;

                productDetailsController = productDetailsController.$initialize();

                $rootScope = productDetailsController.$rootScope;
                $scope = productDetailsController.$scope;
                vm = $scope.vm;
            };

        describe('-> ProductDetailControllerSpec', function () {

            beforeEach(function () {
                module('shoalCommon.products');
                initialiseController();
            });

            describe('on load', function () {

                beforeEach(function () {
                    $scope.$digest();
                });

                it('should be loaded', function () {
                    expect(vm).toBeDefined();
                });

                it('should retrieve the product data by code', function () {

                    expect(productService.findByCode).toHaveBeenCalledWith(viewingProduct.code);
                });

                it('should synchronise the basket', function () {
                    expect(basketService.synchroniseBasket).toHaveBeenCalled();
                });
            });

            describe('on loading with a basket containing the current product', function () {

                beforeEach(function () {
                    $scope.$digest();
                });

                it('should know that the visible product is already in the basket', function () {
                    $rootScope.$broadcast('basketUpdated', SHOAL.StaticData.DUMMY_BASKET);

                    $scope.$digest();

                    expect(vm.isProductInBasket).toBe(true, ' did not register that the visible product is in the basket');
                });
            });

            describe("after loading", function () {

                beforeEach(function () {
                    $scope.$digest();
                });

                it('should save the product data', function () {
                    expect(vm.product).toBeDefined('vm.product');
                    expect(vm.product.name).toEqual("HP EliteBook 840 G2 Laptop");
                });

                it('should remember the current volume of products sold', function () {
                    expect(vm.product.currentVolume).toBe(36, ' did not store the product quantity');
                });

                it('should initialise the quantity to one', function () {
                    expect(vm.order.quantity).toBe(1);
                });

                describe('when adding item to basket', function () {

                    var order = {
                        quantity: 1,
                        pricing: {
                            unitPrice: 930.00,
                            unitDiscount: 0.00
                        }
                    };

                    beforeEach(function () {
                        vm.addToBasket(viewingProduct, order);
                    });

                    it('should get the current basket and add an item to it ', function () {

                        expect(basketService.addItemToBasket).toHaveBeenCalled();
                    });

                    it('should pass product and order information', function () {
                        expect(basketService.addItemToBasket).toHaveBeenCalledWith(viewingProduct, order);
                    });
                });

                describe('when calculating prices', function () {

                    function updateQuantityField(value) {
                        vm.order.quantity = value;
                    }

                    it('should set the pricing fields', function () {
                        expect(vm.order.pricing.unitPrice).toEqual(1030.00, ' band price was not set');
                        expect(vm.order.pricing.unitDiscount).toEqual(0.00, ' unit discount was not set');
                    });

                    it('should trigger a pricing update when the quantity changes', function () {
                        updateQuantityField(2);

                        expect(vm.product.calculatePricing).toHaveBeenCalled();
                    });
                });
            });
        });
    });
}());