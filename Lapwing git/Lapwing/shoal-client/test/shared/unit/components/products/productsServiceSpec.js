'use strict';

describe('shoalCommon.products', function () {

    var productService,
        productsResource;

    beforeEach(function () {
        productService = quickmock({
            providerName: 'shoalCommon_products_ProductService',
            moduleName: 'shoalCommon.products',
            mockModules: ['shoalCommon.SharedMocks', 'shoalApp.classes']
        });

        productsResource = productService.$mocks.shoalCommon_products_ProductsResource;
    });

    describe('Products service', function () {

        it('should be loaded', function () {
            expect(productService.isLoaded()).toBe(true, 'should be loaded');
        });

        describe('After load', function () {
            var code = "HPELITE840",
                category = "Products";

            describe('When a product is requested by code', function () {

                beforeEach(function () {
                    productsResource.responseData = SHOAL.StaticData.buildDefaultProduct();
                });

                it('should find product from server', function () {

                    productService.findByCode(code);

                    expect(productsResource.findByCode).toHaveBeenCalledWith(jasmine.objectContaining(
                        {
                            code: code
                        }
                    ), jasmine.any(Function),
                        jasmine.any(Function));
                });

                it('should not be possible to amend the product data', function () {
                    productService.findByCode(code).then(function (product) {
                        try {
                            product.someValue = '';
                            expect('changing product data did not raise an error').toBe('expected an error');
                        } catch (ignore) {
                            // we expect this error
                        }
                    });
                });

                describe("when the product code is null", function () {

                    it('should raise an error', function () {
                        try {
                            productService.findByCode();
                            expect('no exception thrown from findByCode').toBe('an exception is thrown');
                        } catch (ignore) {
                            // expected
                        }
                    });
                });
            });

            describe("When product listing is requested by category", function () {

                beforeEach(function () {
                    productsResource.responseData = SHOAL.StaticData.buildDefaultProductListing();

                    productService.fetchByTopCategory(category);
                });

                it('should fetch product listing from server', function () {

                    expect(productsResource.fetchByTopCategory).toHaveBeenCalledWith(jasmine.objectContaining({
                        category: category
                    }),
                        jasmine.any(Function),
                        jasmine.any(Function));
                });

                describe("when the product category is null", function () {

                    it('should raise an error', function () {
                        try {
                            productService.fetchByTopCategory();
                            expect('no exception thrown from fetchByTopCategory').toBe('an exception is thrown');
                        } catch (ignore) {
                            // expected
                        }
                    });
                });
            });
        });
    });
});