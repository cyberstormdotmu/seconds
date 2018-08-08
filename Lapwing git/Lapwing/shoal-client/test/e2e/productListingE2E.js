(function () {
    'use strict';

    var appPage = require('./pageObjects/app/appPage'),
        productListingPage = require('./pageObjects/app/productListingPage'),
        productDetailsPage = require('./pageObjects/app/productDetailsPage'),
        auth = require('./interactions/util/authenticateUser'),
        buyer = require('./interactions/util/makeBuyer'),
        continueIfDone = require('./utils/setupSpec').continueIfDone,
        resetDone = require('./utils/setupSpec').resetDone;

    describe('E2E Product listing :', function () {

        var credentials;

        beforeAll(resetDone(function (done) {
            //console.log("beforeAll in productListingE2E");

            buyer.makeBuyer(function (authorisedCredentials) {
                console.log("user authorised - capturing credentials");
                credentials = authorisedCredentials;
                done();
            });
        }));

        beforeEach(continueIfDone(function (done) {
            browser.manage().deleteAllCookies();
           // console.log("beforeEach in productListingE2E");

            auth.authenticateUser(credentials)
                .then(function () {
                    return appPage.frame.waitForLoad();
                }).then(function () {
                    return productListingPage.listing.goto();
                }).then(done);
        }));

        describe('in the product listing view', function () {

            it("should show three products", function () {
                expect(productListingPage.listing.productListing.count()).toBeGreaterThan(2, 'incorrect number of products shown');
            });

            it("should link first view offer to the correct product detail page", function () {
                productListingPage.listing.selectProductLink("HPELITE840").click();

                expect(productDetailsPage.details.productDescription).toBe("HP EliteBook 840 G2 Laptop");
            });

        });
    });
}());