(function () {
    'use strict';
    describe('shoalCommon.products module -> Product Listing', function () {

        var $scope, $httpBackend,
            vm,
            productService,
            webServiceUrl = "https://domain.com/ws",
            productsUrl = webServiceUrl + "/products",
            productCode = 'HPELITE840',
            ProductListingController,
            initialiseController = function () {
                ProductListingController = quickmock({
                    providerName: 'shoalApp.views.product.ProductListingController',
                    providerAs: 'vm',
                    moduleName: 'shoalApp.views.product',
                    mockModules: ['shoalApp.ShoalAppMocks', 'shoalApp.classes']
                });

                productService = ProductListingController.$mocks.shoalCommon_products_ProductService;
                $scope = ProductListingController.$scope;
                $httpBackend = ProductListingController.$mocks.$httpBackend;
                vm = $scope.vm;
            };

        describe('-> ProductListingControllerSpec', function () {
            beforeEach(function () {
                module('shoalCommon.products');
                initialiseController();
            });

            describe('on load', function () {

                beforeEach(function () {
                    $scope.$digest();
                    $httpBackend.whenGET(productsUrl + "/" + productCode).respond(200, SHOAL.StaticData.buildDefaultProduct());
                    $httpBackend.whenGET(productsUrl + "?category=Products").respond(200, SHOAL.StaticData.buildDefaultProductListing());
                });

                it('should be loaded', function () {
                    expect(vm).toBeDefined();
                });


                it('should retrieve the product list for the topmost category', function () {
                    expect(productService.fetchByTopCategory).toHaveBeenCalledWith('Products');
                });
            });

            describe('product listing controller', function () {
                beforeEach(function () {
                    $scope.$digest();
                });

                it('should store the product data', function () {

                    expect(vm.products).toBeDefined('products not found on vm');
                    expect(vm.products).toEqual(SHOAL.StaticData.buildDefaultProductListing());
                });
            });
        });
    });
}());