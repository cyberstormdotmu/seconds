(function () {
    'use strict';
    describe('shoalCommon.classes module -> shoalCommon_classes_ProductOfferClass', function () {
        var productClass,
            subject,
            product;

        beforeEach(function () {
            product = SHOAL.StaticData.PRODUCT_1;
            productClass = quickmock({
                providerName: 'shoalCommon_classes_ProductOffer',
                moduleName: 'shoalCommon.classes',
                mockModules: []
            });
            subject = productClass(product);
        });

        it('should be initialised', function () {
            //spec body

            expect(subject).toBeDefined();
        });

        it('should contain all the raw product data', function () {
            expect(subject.code).toEqual(product.code);
            expect(subject.description).toEqual(product.description);
            expect(subject.specifications.length).toEqual(product.specifications.length);
            expect(subject.categories).toEqual(product.categories);
            expect(subject.currentVolume).toEqual(product.currentVolume);
            expect(subject.vendorName).toEqual(product.vendorName);
            expect(subject.vatRate).toEqual(product.vatRate);
            expect(subject.offerEndDate).toEqual(product.offerEndDate);
            expect(subject.priceBands.length).toEqual(product.priceBands.length);
            expect(subject.images.length).toEqual(product.images.length);
        });

        describe("when setting product category", function () {
            beforeEach(function () {
                subject.categories = ['Products', 'Laptops'];
            });

            it("should determine the main category", function () {
                expect(subject.mainCategory).toBe('Products');
            });

            describe("when the main category is set", function () {
                beforeEach(function () {
                    subject.mainCategory = 'Power User';
                });

                it("should override the categories", function () {
                    expect(subject.categories[0]).toEqual('Power User');
                });
            });
        });

        describe('should calculate product pricing', function () {

            it('should return price for current volume when the quantity is not set', function () {
                var pricing = subject.calculatePricing();

                expect(pricing.unitPrice).toEqual(1030.00, ' unitPrice was not as expected');
                expect(pricing.unitDiscount).toEqual(0.00, ' unit discount was not as expected');
            });

            it('should raise an error if the quantity is negative', function () {
                try {
                    subject.calculatePricing(-1);

                    expect('no exception thrown from product.calculatePricing()').toBe('an exception is thrown');
                } catch (ignore) {
                }
            });

            it('should calculate the pricing for the supplied item quantity', function () {
                var pricing = subject.calculatePricing(1);

                expect(pricing.unitPrice).toEqual(1030.00, ' unitPrice was not as expected');
                expect(pricing.unitDiscount).toEqual(0.00, ' unit discount was not as expected');

                pricing = subject.calculatePricing(15);
                expect(pricing.unitPrice).toEqual(930.00, ' unitPrice was not as expected');
                expect(pricing.unitDiscount).toEqual(100.00, ' unit discount was not as expected');

                pricing = subject.calculatePricing(150);
                expect(pricing.unitPrice).toEqual(830.00, ' unitPrice was not as expected');
                expect(pricing.unitDiscount).toEqual(200.00, ' unit discount was not as expected');
            });

            it('should handle order quantities at the max of the price band', function () {

                var pricing = subject.calculatePricing(4);
                expect(pricing.unitPrice).toEqual(1030.00, ' unitPrice was not as expected');
                expect(pricing.unitDiscount).toEqual(0.00, ' unit discount was not as expected');

            });

            it('should handle order quantities in the highest price band', function () {

                var pricing = subject.calculatePricing(250);
                expect(pricing.unitPrice).toEqual(730.00);
                expect(pricing.unitDiscount).toEqual(300.00);
            });
        });

        describe("when creating a new product specification", function () {

            beforeEach(function () {
                subject = productClass();
            });

            it("should have no specification set", function () {
                expect(subject.specifications.length).toBe(0);
            });

            describe("should allow adding an empty product specification", function () {
                beforeEach(function () {
                    subject.specifications.addItem();
                });

                it("should have added a new empty specification", function () {
                    expect(subject.specifications.length).toBe(1);
                    expect(subject.specifications[0].name).toBe('');
                    expect(subject.specifications[0].description).toBe('');
                });
            });

            describe("should allow removing a specification", function () {
                beforeEach(function () {
                    var specToDelete;
                    subject = productClass(product);
                    specToDelete = subject.specifications[1];
                    subject.specifications.removeItem(specToDelete);
                });

                it("should have reduced the number of specifications", function () {
                    expect(subject.specifications.length).toBe(1);
                });

                it("should contain the remaining specification", function () {
                    expect(subject.specifications[0].name).toBe("Processor");
                });
            });
        });

        describe("when creating a new product image", function () {

            beforeEach(function () {
                subject = productClass();
            });

            it("should have no images set", function () {
                expect(subject.images.length).toBe(0);
            });

            describe("should allow adding an empty product image", function () {
                beforeEach(function () {
                    subject.images.addItem();
                });

                it("should have added a new empty image", function () {
                    expect(subject.images.length).toBe(1);
                    expect(subject.images[0].url).toBe('');
                    expect(subject.images[0].description).toBe('');
                });
            });

            describe("should allow removing an image", function () {
                beforeEach(function () {
                    var imageToDelete;
                    subject = productClass(product);
                    imageToDelete = subject.images[1];
                    subject.images.removeItem(imageToDelete);
                });

                it("should have reduced the number of images", function () {
                    expect(subject.images.length).toBe(1);
                });

                it("should contain the remaining image", function () {
                    console.log(JSON.stringify(subject.images[0]));
                    expect(subject.images[0].url).toBe(SHOAL.StaticData.IMAGE_1.url);
                });
            });
        });

        describe("when setting the vat rate", function () {
            var rate = {
                code: 'STANDARD',
                rate: 20.00
            };
            beforeEach(function () {
                subject.vatRate = rate;
            });

            it("should record the rate", function () {
                expect(subject.vatRate).toBe(rate);
            });
        });

        describe("when adding a new price band", function () {
            beforeEach(function () {
                subject = productClass();
                subject.priceBands.addItem();
            });

            it("should contain a new price band", function () {
                expect(subject.priceBands.length).toBe(1);
            });

            it("should start with zero volume", function () {
                expect(subject.priceBands[0].minVolume).toBe(0);
            });

            describe("after completing a price band", function () {
                beforeEach(function () {
                    subject.priceBands[0].maxVolume = 99;
                    subject.priceBands[0].buyerPrice = 1000.00;
                });

                describe("when creating another price band", function () {
                    beforeEach(function () {
                        subject.priceBands.addItem();
                    });

                    it("should create a new band with a seamless min volume", function () {
                        expect(subject.priceBands[1].minVolume).toBe(100);
                    });
                });
            });

            describe("when the unbounded flag is set", function () {

                beforeEach(function () {
                    subject.priceBands[0].unbounded = true;
                    subject.priceBands[0].maxVolume = 999;
                });

                it("should force the maxVolume to be null", function () {
                    expect(subject.priceBands[0].maxVolume).toBe(null);
                });
            });
        });
    });
}());
