(function () {
    'use strict';


    describe('shoalAdmin.manageProducts module -> shoalAdmin_manageProducts_ManageProductsService', function () {
        var manageProductsService,
            productsResource,
            $rootScope;

        function triggerPromise() {
            $rootScope.$digest();
        }

        beforeEach(function () {
            manageProductsService = quickmock({
                providerName: 'shoalAdmin_manageProducts_ManageProductsService',
                moduleName: 'shoalAdmin.manageProducts',
                mockModules: ['shoalAdmin.ShoalAdminMocks', 'shoalCommon.classes']
            });

            $rootScope = manageProductsService.$rootScope;
            productsResource = manageProductsService.$mocks.shoalAdmin_manageProducts_ProductsResource;
        });

        it('should be loaded', function () {
            //spec body

            expect(manageProductsService.isLoaded()).toBe(true, 'should be loaded');
        });

        describe("when the product is built", function () {
            var productOffer;

            beforeEach(function () {
                productOffer = manageProductsService.buildProduct();
            });

            it('should provide a product offer', function () {
                expect(productOffer).toBeDefined();
            });

            it('should have one product specification', function () {
                expect(productOffer.specifications.length).toBe(1);
            });

            it('should have one product category', function () {
                expect(productOffer.categories.length).toBe(1);
            });

            it('should have one product category', function () {
                expect(productOffer.images.length).toBe(1);
            });

            it('should have one price band', function () {
                expect(productOffer.priceBands.length).toBe(1);
            });

            it('should not be possible to change the product structure', function () {
                expect(Object.isSealed(productOffer)).toBe(true, 'product properties should not be changable');
            });

            describe("when the product offer is saved", function () {
                beforeEach(function () {
                    productOffer.code = 'HPELITE';
                    productOffer.save();
                });

                it("will send the product offer to the server", function () {
                    expect(productsResource.save).toHaveBeenCalled();
                });

                it("will send the product offer data to the server", function () {
                    expect(productsResource.save).toHaveBeenCalledWith(
                        jasmine.objectContaining({
                            code: 'HPELITE'
                        }),
                        jasmine.any(Function),
                        jasmine.any(Function)
                    );
                });

                describe("if the save is successful", function () {

                    beforeEach(function () {
                        triggerPromise();
                    });

                    it("will update the isSaved flag", function () {

                        expect(productOffer.status.isSaved).toBe(true, ' expected isSaved flag to be set');
                        expect(productOffer.status.isError).toBe(false, ' expected isError flag to be unset');
                    });
                });
            });

            describe("when the product offer save fails", function () {
                beforeEach(function () {
                    productsResource.reject = true;
                    productOffer.save();
                });

                it("will update the error flag", function () {
                    expect(productOffer.status.isError).toBe(true, 'expected isError flag to be set');
                    expect(productOffer.status.isSaved).toBe(false, 'expected isSaved flag to be unset');
                });

                describe("when the product offer is resubmitted successfully", function () {
                    beforeEach(function () {
                        productsResource.reject = false;
                        productOffer.save();
                    });

                    it("will update the isSaved flag", function () {
                        expect(productOffer.status.isSaved).toBe(true, 'expected isSaved flag to be set');
                        expect(productOffer.status.isError).toBe(false, 'expected isError flag to be unset');
                    });
                });
            });

        });
    });
}());