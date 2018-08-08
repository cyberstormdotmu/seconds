(function () {
    'use strict';
    var productDetailsPage = function () {
        return Object.create({}, {
            gotoHPELITE840: {
                value: function () {
                    return browser.get('/app/#/products/HPELITE840');
                }
            },
            productMainImage: {
                get: function () {
                    return element(by.id('product_view')).all(by.tagName('img')).get(0);
                }
            },
            detailPanel: {
                get: function () {
                    return element(by.id('detail-tabs'));
                }
            },
            specificationButton: {
                get: function () {
                    return this.detailPanel.element(by.partialLinkText('Specification'));
                }
            },
            activeDetailTab: {
                get: function () {
                    return this.detailPanel.all(by.css('.active')).first().getText();
                }
            },
            detailTabs: {
                get: function () {
                    return this.detailPanel.all(by.tagName('li'));
                }
            },
            detailTabNames: {
                get: function () {
                    return this.detailTabs.getText();
                }
            },
            allSpecifications: {
                get: function () {
                    // liable to break if binding name changes
                    return this.detailPanel.all(by.repeater('spec in vm.product.specifications'));
                }
            },
            firstSpecification: {
                get: function () {
                    return this.allSpecifications.first().getText();
                }
            },
            productDescription: {
                get: function () {
                    // liable to break if binding name changes
                    return element(by.id('product-header')).element(by.binding(' vm.product.name ')).getText();
                }
            },
            productPricing: {
                get: function () {
                    return element(by.id('product-pricing'));
                }
            },
            orderQuantity: {
                get: function () {
                    return this.productPricing.element(by.id('order-quantity'));
                },
                set: function (amount) {
                    this.orderQuantity.clear().sendKeys(amount);
                    browser.waitForAngular();
                }
            },
            unitPrice: {
                get: function () {
                    return this.productPricing.element(by.id('unit-price')).getText();
                }
            },
            unitDiscount: {
                get: function () {
                    return this.productPricing.element(by.id('unit-discount')).getText();
                }
            },
            addProductButton: {
                get: function () {
                    var ele = this.productPricing.element(by.id('add-product'));
                    expect(ele.isDisplayed()).toBe(true);
                    return ele;
                }
            },
            orderNotifier: {
                get: function () {
                    return element(by.css('.ui-notification'));
                }
            }
        });
    };

    module.exports = {
        details: productDetailsPage()
    };

}());
