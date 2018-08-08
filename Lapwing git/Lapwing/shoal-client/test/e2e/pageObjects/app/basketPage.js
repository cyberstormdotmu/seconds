(function () {
    'use strict';

    var EC = protractor.ExpectedConditions,

        basketPage = function () {
            return Object.create({}, {
                basketCheckoutButtonClick: {
                    value: function () {
                        browser.wait(EC.visibilityOf($('#basket-proceed-checkout-button')), 10000);
                        return element(by.id("basket-proceed-checkout-button")).click();
                    }
                }
            });
        };

    module.exports = {
        basket: basketPage()
    };

}());
