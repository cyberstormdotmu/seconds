(function () {
    'use strict';
    describe('shoalAdmin.manageProducts module -> shoalAdmin_manageProducts_ProductsResource', function () {
        var ProductsResource,
            $httpBackend,
            manageProductsEndpoint = /https:\/\/[\w\.]*\/ws\/admin\/manageProducts/;

        beforeEach(function () {
            ProductsResource = quickmock({
                providerName: 'shoalAdmin_manageProducts_ProductsResource',
                moduleName: 'shoalAdmin.manageProducts',
                mockModules: ['shoalAdmin.ShoalAdminMocks']
            });
            $httpBackend = ProductsResource.$mocks.$httpBackend;
        });

        it("should be loaded", function () {
            //spec body

            expect(ProductsResource.isLoaded()).toBe(true, "should be loaded");
        });

        describe("when saving product", function () {

            afterEach(function () {
                ProductsResource.save(SHOAL.StaticData.PRODUCT_1, null);
                $httpBackend.flush();
                $httpBackend.verifyNoOutstandingExpectation();
                $httpBackend.verifyNoOutstandingRequest();
            });

            it("should PUT to server", function () {
                $httpBackend.expectPOST(manageProductsEndpoint).respond(200);
            });
        });
    });
}());