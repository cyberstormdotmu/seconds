(function () {
    'use strict';
    var appPage = require('./pageObjects/app/appPage'),
        productListingPage = require('./pageObjects/app/productListingPage'),
        productDetailsPage = require('./pageObjects/app/productDetailsPage'),
        basketPage = require('./pageObjects/app/basketPage'),
        checkoutPage = require('./pageObjects/app/checkoutPage'),
        auth = require('./interactions/util/authenticateUser'),
        buyer = require('./interactions/util/makeBuyer'),
        profile = require('./interactions/app/updateProfile'),
        continueIfDone = require('./utils/setupSpec').continueIfDone,
        resetDone = require('./utils/setupSpec').resetDone;

    describe('E2E Checkout a buyer order:', function () {

        var credentials;

        beforeAll(resetDone(function (done) {
            buyer.makeBuyer(function (authorisedCredentials) {
                console.log("user authorised - capturing credentials");
                credentials = authorisedCredentials;
                done();
            });
        }));

        function startANewSession() {
            return resetDone(function (done) {
                console.log("starting a new session");
                browser.manage().deleteAllCookies()
                    .then(function () {
                        return auth.authenticateUser(credentials);
                    })
                    .then(appPage.frame.waitForLoad)
                    .then(done);
            });
        }

        function addAProductToTheBasket(done) {
            productListingPage.listing.goto()
                .then(function () {
                    return productListingPage.listing.selectProductLink("HPSPECTRE").click();
                })
                .then(function () {
                    productDetailsPage.details.orderQuantity = 2;
                })
                .then(function () {
                    return productDetailsPage.details.addProductButton.click();
                })
                .then(done);
        }

        describe("when the buyer adds an item to the basket,", function () {

            beforeAll(startANewSession());

            beforeAll(continueIfDone(addAProductToTheBasket));

            describe("and proceeds to checkout", function () {

                beforeAll(continueIfDone(function (done) {
                    basketPage.basket.basketCheckoutButtonClick().then(done);
                }));

                describe("without completing their profile first,", function () {

                    it("will present them with the profile form", function () {

                        expect(checkoutPage.profileForm.isPresent).toBeTruthy();
                    });

                    describe("after completing the form,", function () {

                        beforeEach(continueIfDone(function (done) {
                            /**
                             * Disabling this will break later tests which expect the profile to be completed.
                             */
                            profile.updateProfileWithDefaults(checkoutPage.profileForm)
                                .then(checkoutPage.profileForm.waitForProfileFormToDisappear)
                                .then(done);
                        }));

                        it("will allow the buyer to edit the basket", function () {

                            expect(checkoutPage.checkout.currentStep.getText()).toBe("Edit Basket");
                        });
                    });
                });

                describe("when the buyer has already completed their profile,", function () {

                    beforeAll(continueIfDone(function (done) {
                        appPage.menuBar.basketIconClick()
                            .then(basketPage.basket.basketCheckoutButtonClick)
                            .then(done);
                    }));

                    it("will allow the buyer to edit the basket", function () {

                        expect(checkoutPage.checkout.currentStep.getText()).toBe("Edit Basket");
                    });

                    it("should show the order price information", function () {
                        expect(checkoutPage.basket.checkoutBasketSubtotal.getText()).toBe('£4,060.00', ' basket subtotal was missing or incorrect');
                        expect(checkoutPage.basket.checkoutBasketVatTotal.getText()).toBe('£812.00', ' basket vat total was missing or incorrect');
                        expect(checkoutPage.basket.checkoutBasketGrossTotal.getText()).toBe('£4,872.00', ' basket gross total was missing or incorrect');
                    });

                    describe("after the buyer confirms the basket,", function () {

                        beforeAll(continueIfDone(function (done) {
                            checkoutPage.checkout.nextStepButtonClick().then(done);
                        }));

                        it("will allow them to confirm addresses", function () {
                            expect(checkoutPage.checkout.currentStep.getText()).toBe("Confirm Addresses");
                        });

                        describe("after the buyer confirms the order addresses,", function () {

                            function selectFirstOption(comboBox) {
                                return function () {
                                    return comboBox.element(by.cssContainingText("option", "HP Inc.")).click();
                                };
                            }

                            beforeAll(continueIfDone(function (done) {
                                selectFirstOption(checkoutPage.checkout.invoiceAddress)()
                                    .then(selectFirstOption(checkoutPage.checkout.deliveryAddress))
                                    .then(checkoutPage.checkout.nextStepButtonClick)
                                    .then(done);
                            }));

                            it("will allow them to provide payment details", function () {
                                expect(checkoutPage.checkout.currentStep.getText()).toBe("Payment");
                            });

                            describe("after the buyer fills in payment details,", function () {

                                beforeAll(continueIfDone(function (done) {
                                    checkoutPage.paymentForm.paymentOption('Card Payment')
                                        .then(function () {
                                            checkoutPage.paymentForm.cardholderName = "Mr Endtoend Test";
                                            checkoutPage.paymentForm.cardNumber = "4242424242424242";
                                            checkoutPage.paymentForm.expiryMonth = new Date().getMonth() + 1;
                                            checkoutPage.paymentForm.expiryYear = new Date().getFullYear().toString();
                                            checkoutPage.paymentForm.cvc = "123";
                                            return checkoutPage.checkout.nextStepButtonClick();
                                        })
                                        .then(done);
                                }));

                                it("will allow them to review the order", function () {
                                    expect(checkoutPage.checkout.currentStep.getText()).toBe("Review Order");
                                });

                                describe("after the buyer reviews the order,", function () {

                                    it("should show the order total information", function () {
                                        expect(checkoutPage.review.reviewOrderSubtotal.getText()).toBe('£4,060.00', ' order subtotal was missing or incorrect');
                                        expect(checkoutPage.review.reviewOrderVatTotal.getText()).toBe('£812.00', ' order vat total was missing or incorrect');
                                        expect(checkoutPage.review.reviewOrderGrossTotal.getText()).toBe('£4,872.00', ' order gross total was missing or incorrect');
                                        expect(checkoutPage.review.reviewOrderBalancePayable.getText()).toBe('£4,872.00', ' order balance payable was missing or incorrect');
                                    });

                                    describe("after the buyer places the order", function () {

                                        beforeAll(continueIfDone(function (done) {
                                            checkoutPage.checkout.acceptTCsClick()
                                                .then(checkoutPage.checkout.placeOrderButtonClick)
                                                .then(done);
                                        }));

                                        it("will show an order confirmation", function () {

                                            expect(checkoutPage.checkout.checkoutOrderConfirmation.isDisplayed())
                                                .toBe(true, ' checkout order confirmation is not displayed');

                                            expect(checkoutPage.checkout.currentStep.getText())
                                                .toBe("Order Placed", ' wrong checkout step displayed');
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });

        describe("when the buyer adds an item to the basket,", function () {

            beforeAll(startANewSession());

            beforeAll(continueIfDone(addAProductToTheBasket));

            describe("and proceeds to checkout", function () {

                beforeAll(continueIfDone(function (done) {
                    basketPage.basket.basketCheckoutButtonClick().then(done);
                }));

                describe("when the user amends the basket", function () {

                    beforeAll(continueIfDone(function (done) {
                        checkoutPage.basket.quantity = 3;
                        done();
                    }));

                    it("should update the order price information", function () {
                        expect(checkoutPage.basket.checkoutBasketSubtotal.getText()).toBe('£6,090.00', ' basket subtotal was missing or incorrect');
                        expect(checkoutPage.basket.checkoutBasketVatTotal.getText()).toBe('£1,218.00', ' basket vat total was missing or incorrect');
                        expect(checkoutPage.basket.checkoutBasketGrossTotal.getText()).toBe('£7,308.00', ' basket gross total was missing or incorrect');
                    });

                    describe("after the buyer confirms the basket,", function () {

                        beforeAll(continueIfDone(function (done) {
                            checkoutPage.checkout.nextStepButtonClick().then(done);
                        }));

                        describe("after the buyer confirms the order addresses,", function () {

                            function selectFirstOption(comboBox) {
                                return function () {
                                    return comboBox.element(by.cssContainingText("option", "HP Inc.")).click();
                                };
                            }

                            beforeAll(continueIfDone(function (done) {
                                selectFirstOption(checkoutPage.checkout.invoiceAddress)()
                                    .then(selectFirstOption(checkoutPage.checkout.deliveryAddress))
                                    .then(checkoutPage.checkout.nextStepButtonClick)
                                    .then(done);
                            }));

                            describe("after the buyer fills in payment details,", function () {

                                beforeAll(continueIfDone(function (done) {
                                    checkoutPage.paymentForm.paymentOption('Card Payment')
                                        .then(function () {
                                            checkoutPage.paymentForm.cardholderName = "Mr Endtoend Test";
                                            checkoutPage.paymentForm.cardNumber = "4242424242424242";
                                            checkoutPage.paymentForm.expiryMonth = new Date().getMonth() + 1;
                                            checkoutPage.paymentForm.expiryYear = new Date().getFullYear().toString();
                                            checkoutPage.paymentForm.cvc = "123";
                                            return checkoutPage.checkout.nextStepButtonClick();
                                        })
                                        .then(done);
                                }));

                                describe("after the buyer reviews the order,", function () {

                                    it("should show the order total information", function () {
                                        expect(checkoutPage.review.reviewOrderSubtotal.getText()).toBe('£6,090.00', ' order subtotal was missing or incorrect');
                                        expect(checkoutPage.review.reviewOrderVatTotal.getText()).toBe('£1,218.00', ' order vat total was missing or incorrect');
                                        expect(checkoutPage.review.reviewOrderGrossTotal.getText()).toBe('£7,308.00', ' order gross total was missing or incorrect');
                                        expect(checkoutPage.review.reviewOrderBalancePayable.getText()).toBe('£7,308.00', ' order balance payable was missing or incorrect');
                                    });

                                    describe("after the buyer places the order", function () {

                                        beforeAll(continueIfDone(function (done) {
                                            checkoutPage.checkout.acceptTCsClick()
                                                .then(checkoutPage.checkout.placeOrderButtonClick)
                                                .then(done);
                                        }));

                                        it("will show an order confirmation", function () {

                                            expect(checkoutPage.checkout.checkoutOrderConfirmation.isDisplayed())
                                                .toBe(true, ' checkout order confirmation is not displayed');

                                            expect(checkoutPage.checkout.currentStep.getText())
                                                .toBe("Order Placed", ' wrong checkout step displayed');
                                        });
                                    });

                                });
                            });
                        });
                    });
                });
            });
        });

        describe("when the buyer adds an item to the basket,", function () {

            beforeAll(startANewSession());

            beforeAll(continueIfDone(addAProductToTheBasket));

            describe("and proceeds to checkout", function () {

                beforeAll(continueIfDone(function (done) {
                    basketPage.basket.basketCheckoutButtonClick().then(done);
                }));

                describe("after the buyer confirms the basket,", function () {

                    beforeAll(continueIfDone(function (done) {
                        checkoutPage.checkout.nextStepButtonClick().then(done);
                    }));

                    describe("after the buyer confirms the order addresses,", function () {

                        function selectFirstOption(comboBox) {
                            return function () {
                                return comboBox.element(by.cssContainingText("option", "HP Inc.")).click();
                            };
                        }

                        beforeAll(continueIfDone(function (done) {
                            selectFirstOption(checkoutPage.checkout.invoiceAddress)()
                                .then(selectFirstOption(checkoutPage.checkout.deliveryAddress))
                                .then(checkoutPage.checkout.nextStepButtonClick)
                                .then(done);
                        }));

                        describe("after the buyer chooses to pay on invoice,", function () {

                            beforeAll(continueIfDone(function (done) {
                                checkoutPage.paymentForm.paymentOption('On Invoice')
                                    .then(checkoutPage.checkout.nextStepButtonClick)
                                    .then(done);
                            }));

                            describe("after the buyer reviews the order,", function () {

                                describe("after the buyer places the order", function () {

                                    beforeAll(continueIfDone(function (done) {
                                        checkoutPage.checkout.acceptTCsClick()
                                            .then(checkoutPage.checkout.placeOrderButtonClick)
                                            .then(done);
                                    }));

                                    it("will show an order confirmation", function () {

                                        expect(checkoutPage.checkout.checkoutOrderConfirmation.isDisplayed())
                                            .toBe(true, ' checkout order confirmation is not displayed');

                                        expect(checkoutPage.checkout.currentStep.getText())
                                            .toBe("Order Placed", ' wrong checkout step displayed');
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });
    });
}());