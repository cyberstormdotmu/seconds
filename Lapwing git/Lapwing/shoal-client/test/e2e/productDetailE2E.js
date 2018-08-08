(function () {
    'use strict';

    var appPage = require('./pageObjects/app/appPage'),
        productDetailsPage = require('./pageObjects/app/productDetailsPage'),
        auth = require('./interactions/util/authenticateUser'),
        buyer = require('./interactions/util/makeBuyer'),
        continueIfDone = require('./utils/setupSpec').continueIfDone,
        resetDone = require('./utils/setupSpec').resetDone;

    describe('E2E Product details :', function () {

        var credentials;

        beforeAll(resetDone(function (done) {
            //console.log("beforeAll in productDetailE2E");

            buyer.makeBuyer(function (authorisedCredentials) {
                console.log("user authorised - capturing credentials");
                credentials = authorisedCredentials;
                done();
            });
        }));

        beforeEach(continueIfDone(function (done) {
            browser.manage().deleteAllCookies();
            //console.log("beforeEach in productDetailE2E");
            auth.authenticateUser(credentials)
                .then(function () {
                    return appPage.frame.waitForLoad();
                }).then(function () {
                    return productDetailsPage.details.gotoHPELITE840();
                }).then(done);
        }));

        describe('in the product detail view', function () {

            it("should show the product image", function () {
                expect(productDetailsPage.details.productMainImage.isPresent()).toBeTruthy(' could not see main product image');
            });

            it("should show the Add to Basket button", function () {
                expect(productDetailsPage.details.addProductButton).toBeDefined('could not find buy button');
            });

            it("should show the detail tabs", function () {
                expect(productDetailsPage.details.detailTabNames).toEqual(['Description', 'Specification', 'Reviews', 'Suitability']);
            });

            it('should show the description of the product by default', function () {
                expect(productDetailsPage.details.activeDetailTab).toMatch(/Description/);
            });

            it("should display technical specs when button clicked", function () {
                productDetailsPage.details.specificationButton.click();

                expect(productDetailsPage.details.firstSpecification).toMatch(/Processor/);
            });

            it('should update the price and unit discount when the quantity changes', function () {

                expect(productDetailsPage.details.unitPrice).toEqual('£1,030.00');
                expect(productDetailsPage.details.unitDiscount).toEqual('£0.00');

                productDetailsPage.details.orderQuantity = 2000;

                expect(productDetailsPage.details.unitPrice).toEqual('£830.00');
                expect(productDetailsPage.details.unitDiscount).toEqual('£200.00');

            });

            it('should add an item to the basket', function () {
                productDetailsPage.details.orderQuantity = 2000;

                productDetailsPage.details.addProductButton.click();
            });

        });
    });
}());