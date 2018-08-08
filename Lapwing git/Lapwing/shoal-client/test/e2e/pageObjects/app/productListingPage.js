(function () {
    'use strict';
    var productListingPage = function () {
        return Object.create({}, {
            goto: {
                value: function () {
                    return browser.get('/app/#/products');
                }
            },
            productListing: {
                get: function () {
                    return element.all(by.css('.product-listing-wrapper'));
                }
            },
            allProductLinks: {
                get: function () {
                    return this.productListing.element.all(by.css('.product-pricing')).all(by.tagName('a'));
                }
            },
            hasProductInList: {
                value: function (code) {
                    return element(by.id("product-info-" + code)).isDisplayed();
                }
            },
            selectProductLink: {
                value: function (code) {
                    return element(by.id("product-detail-link-" + code));
                }
            }
        });
    };

    module.exports = {
        listing: productListingPage()
    };

}());
