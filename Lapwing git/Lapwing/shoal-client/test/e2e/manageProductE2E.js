(function () {
    'use strict';
    var adminLoginPage = require('./pageObjects/admin/loginPage'),
        manageProductPage = require('./pageObjects/admin/manageProductPage'),
        randomCodeGenerator = require('./utils/randomCodeGenerator'),
        continueIfDone = require('./utils/setupSpec').continueIfDone,
        reset = require('./utils/setupSpec').reset,
        resetDone = require('./utils/setupSpec').resetDone;

    describe('E2E Create a new product :', function () {

        var randomProductCode;

        beforeAll(reset());

        describe("When the administrator accesses the manage product page", function () {

            beforeEach(continueIfDone(function (done) {
                //console.log("beforeEach in manageProductE2E");

                browser.manage().deleteAllCookies();
                adminLoginPage.login.goto()
                    .then(function () {
                        adminLoginPage.login.loginWithDefaultUser();
                        return browser.sleep(2000);
                    })
                    .then(manageProductPage.manageProduct.goto)
                    .then(done);
            }));

            it("should show the create product form", function () {
                expect(manageProductPage.createProductForm.form.isDisplayed()).toBe(true, ' create product form not displayed');
            });

            describe("when the administrator submits the create product form", function () {
                var completeProductForm = function () {
                    var day = new Date().getDate();

                    manageProductPage.createProductForm.productName = "HP ShoLite 99";
                    manageProductPage.createProductForm.productCode = randomProductCode;

                    manageProductPage.createProductForm.productCategory = "Power User";

                    manageProductPage.createProductForm.vendorName = "HP";
                    manageProductPage.createProductForm.vatRate = "STANDARD [20%]";
                    manageProductPage.createProductForm.description = "A high performance laptop for today's power user";
                    manageProductPage.createProductForm.stock = 0;
                    manageProductPage.createProductForm.termsAndConditions = "Terms And Conditions";

                    manageProductPage.createProductForm.specifications(0).name = "Processor";
                    manageProductPage.createProductForm.specifications(0).description = "Intel Core i7";

                    manageProductPage.createProductForm.addSpecification();

                    manageProductPage.createProductForm.specifications(1).name = "Memory";
                    manageProductPage.createProductForm.specifications(1).description = "8GB Kingston DDR3";

                    manageProductPage.createProductForm.addSpecification();

                    manageProductPage.createProductForm.specifications(2).name = "Drives";
                    manageProductPage.createProductForm.specifications(2).description = "512GB Sata3 SSD";

                    manageProductPage.createProductForm.images(0).url = "http://www.www8-hp.com/h22175/UKStore/Html/Merch/Images/c03889408_390x286.jpg";
                    manageProductPage.createProductForm.images(0).description = "ShoLite Laptop";

                    manageProductPage.createProductForm.addImage();

                    manageProductPage.createProductForm.images(1).url = "http://www.www8-hp.com/h22175/UKStore/Html/Merch/Images/c03889529_390x286.jpg";
                    manageProductPage.createProductForm.images(1).description = "ShoLite Laptop";

                    manageProductPage.createProductForm.pickOfferStartDayInCurrentMonth(day);
                    manageProductPage.createProductForm.pickOfferEndDayInCurrentMonth(day);
                    manageProductPage.createProductForm.priceBands(0).maxVolume = "99";
                    manageProductPage.createProductForm.priceBands(0).buyerPrice = "1000.00";
                    manageProductPage.createProductForm.priceBands(0).vendorPrice = "900.00";
                    manageProductPage.createProductForm.priceBands(0).shoalMargin = "5.00";
                    manageProductPage.createProductForm.priceBands(0).distributorMargin = "2.00";

                    manageProductPage.createProductForm.addPriceBand();

                    manageProductPage.createProductForm.priceBands(1).maxVolume = "999";
                    manageProductPage.createProductForm.priceBands(1).buyerPrice = "950.00";
                    manageProductPage.createProductForm.priceBands(1).vendorPrice = "850.00";
                    manageProductPage.createProductForm.priceBands(1).shoalMargin = "5.00";
                    manageProductPage.createProductForm.priceBands(1).distributorMargin = "2.00";

                    manageProductPage.createProductForm.addPriceBand();

                    manageProductPage.createProductForm.priceBands(2).setUnboundedCheckBox();
                    manageProductPage.createProductForm.priceBands(2).buyerPrice = "925.00";
                    manageProductPage.createProductForm.priceBands(2).vendorPrice = "825.00";
                    manageProductPage.createProductForm.priceBands(2).shoalMargin = "5.00";
                    manageProductPage.createProductForm.priceBands(2).distributorMargin = "2.00";
                };


                beforeEach(continueIfDone(function (done) {

                    randomProductCode = randomCodeGenerator(8);

                    completeProductForm();
                    manageProductPage.createProductForm.submit.click().then(done);
                }));

                it("should give a notification to indicate the product was created", function () {
                    expect(element(by.id("product-saved-block")).isDisplayed()).toBe(true, 'product saved block was not shown');
                });
            });
        });

        describe("after the product was created", function () {

            var appPage = require('./pageObjects/app/appPage'),
                productListingPage = require('./pageObjects/app/productListingPage'),
                auth = require('./interactions/util/authenticateUser'),
                buyer = require('./interactions/util/makeBuyer'),
                credentials;

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

                auth.authenticateUser(credentials)
                    .then(appPage.frame.waitForLoad)
                    .then(productListingPage.listing.goto)
                    .then(done);
            }));

            it("should show the new product in the product listings", function () {

                productListingPage.listing.hasProductInList(randomProductCode);
            });
        });

    });
}());
