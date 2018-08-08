(function () {
    'use strict';
    describe('shoalCommon.products module -> shoalCommon_products_ProductsResource', function () {
        var ProductResource,
            $httpBackend,
            findProductByCodeEndpointMatch = /\w*\/ws\/products\/HPELITE840/,
            fetchProductsByTopCategoryEndpointMatch = /\w*\/ws\/products\?category=Laptops/,
            fetchProductCategoriesEndpointMatch = /\w*\/ws\/products\/categories/;


        beforeEach(function () {
            ProductResource = quickmock({
                providerName: 'shoalCommon_products_ProductsResource',
                moduleName: 'shoalCommon.products',
                mockModules: ['shoalCommon.SharedMocks']
            });
            $httpBackend = ProductResource.$mocks.$httpBackend;
        });

        it("should be loaded", function () {
            //spec body

            expect(ProductResource.isLoaded()).toBe(true, "should be loaded");
        });

        describe("when find a single product by it's code", function () {

            afterEach(function () {
                ProductResource.findByCode({
                    code: 'HPELITE840'
                });
                $httpBackend.flush();
                $httpBackend.verifyNoOutstandingExpectation();
                $httpBackend.verifyNoOutstandingRequest();
            });

            it("should GET from server", function () {
                $httpBackend.expectGET(findProductByCodeEndpointMatch).respond(200);
            });
        });

        describe("when fetching products by their top category", function () {

            afterEach(function () {
                ProductResource.fetchByTopCategory({
                    category: 'Laptops'
                });
                $httpBackend.flush();
                $httpBackend.verifyNoOutstandingExpectation();
                $httpBackend.verifyNoOutstandingRequest();
            });

            it("should GET from server", function () {
                $httpBackend.expectGET(fetchProductsByTopCategoryEndpointMatch).respond(200);
            });
        });

        describe("when fetching product categories", function () {

            afterEach(function () {
                ProductResource.fetchAllCategories();
                $httpBackend.flush();
                $httpBackend.verifyNoOutstandingExpectation();
                $httpBackend.verifyNoOutstandingRequest();
            });

            it("should GET from server", function () {
                $httpBackend.expectGET(fetchProductCategoriesEndpointMatch).respond(200);
            });
        });

    });
}());