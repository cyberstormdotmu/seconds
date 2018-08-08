(function () {

    'use strict';
    var profilePartial = require('./../common/profilePartial'),
        EC = protractor.ExpectedConditions,
        checkoutPage = function () {
            return Object.create({}, {
                currentStep: {
                    get: function () {
                        browser.wait(EC.visibilityOf($('li.current-step')), 10000);
                        return element(by.css('li.current-step'));
                    }
                },
                nextStepButtonClick: {
                    value: function () {
                        browser.waitForAngular();
                        browser.wait(EC.visibilityOf($('#checkout-next-step-button')), 10000);
                        return element(by.id("checkout-next-step-button")).click();
                    }
                },
                invoiceAddress: {
                    get: function () {
                        return element(by.xpath("(//select[@name='selectedAddressKey'])[1]"));
                    }
                },
                deliveryAddress: {
                    get: function () {
                        return element(by.xpath("(//select[@name='selectedAddressKey'])[2]"));
                    }
                },
                acceptTCsClick: {
                    value: function () {
                        return element(by.id("acceptTCs")).click();
                    }
                },
                placeOrderButtonClick: {
                    value: function () {
                        browser.wait(EC.visibilityOf($('#checkout-place-order-button')), 25000);
                        return element(by.id("checkout-place-order-button")).click();
                    }
                },
                checkoutOrderConfirmation: {
                    get: function () {
                        browser.wait(EC.visibilityOf($('#checkout-order-confirmation')), 25000);
                        return element(by.id('checkout-order-confirmation'));
                    }
                }
            });
        },
        paymentForm = function () {
            return Object.create({}, {
                paymentOption: {
                    value: function (paymentType) {
                        if (paymentType === 'Card Payment') {

                            return element(by.id('pay_by_card')).click();
                        }
                        if (paymentType === 'On Invoice') {

                            return element(by.id('pay_on_invoice')).click();
                        }
                        throw 'Invalid Payment Type';
                    }
                },
                cardholderName: {
                    get: function () {
                        return element(by.id("cardholderName"));
                    },
                    set: function (val) {
                        this.cardholderName.clear().sendKeys(val);
                    }
                },
                cardNumber: {
                    get: function () {
                        return element(by.id("cardNumber"));
                    },
                    set: function (val) {
                        this.cardNumber.clear().sendKeys(val);
                    }
                },
                expiryMonth: {
                    get: function () {
                        return element(by.id("expiryMonth"));
                    },
                    set: function (val) {
                        this.expiryMonth.element(by.cssContainingText("option", val)).click();
                    }
                },
                expiryYear: {
                    get: function () {
                        return element(by.id("expiryYear"));
                    },
                    set: function (val) {
                        this.expiryYear.element(by.cssContainingText("option", val)).click();
                    }
                },
                cvc: {
                    get: function () {
                        return element(by.id("cvc"));
                    },
                    set: function (val) {
                        this.cvc.clear().sendKeys(val);
                    }
                }
            });
        },
        basketConfirm = function () {
            return Object.create({}, {
                quantity: {
                    get: function () {
                        return element(by.name("item_quantity"));
                    },
                    set: function (val) {
                        this.quantity.clear().sendKeys(val);
                    }
                },
                checkoutBasketSubtotal: {
                    get: function () {
                        return element(by.id("checkout-basket-subtotal"));
                    }
                },
                checkoutBasketVatTotal: {
                    get: function () {
                        return element(by.id("checkout-basket-vat-total"));
                    }
                },
                checkoutBasketGrossTotal: {
                    get: function () {
                        return element(by.id("checkout-basket-gross-total"));
                    }
                }
            });
        },
        basketReview = function () {
            return Object.create({}, {
                reviewOrderSubtotal: {
                    get: function () {
                        return element(by.id("review-order-subtotal"));
                    }
                },
                reviewOrderVatTotal: {
                    get: function () {
                        return element(by.id("review-order-vat-total"));
                    }
                },
                reviewOrderGrossTotal: {
                    get: function () {
                        return element(by.id("review-order-gross-total"));
                    }
                },
                reviewOrderBalancePayable: {
                    get: function () {
                        return element(by.id("review-order-balance-payable"));
                    }
                }
            });
        };

    module.exports = {
        checkout: checkoutPage(),
        paymentForm: paymentForm(),
        basket: basketConfirm(),
        review: basketReview(),
        profileForm: profilePartial.profileForm
    };

}());
