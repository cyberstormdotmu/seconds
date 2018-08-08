(function () {
    'use strict';
    var EC = protractor.ExpectedConditions,
        header = function () {
            return Object.create({}, {
                logo: {
                    get: function () {
                        return element(by.id('logo'));
                    }
                }
            });
        },
        frame = function () {
            return Object.create({}, {
                isLoaded: {
                    get: function () {
                        return element(by.css('header.shoalFrame')).isPresent();
                    }
                },
                waitForLoad: {
                    value: function () {

                        browser.sleep(1000);
                        return browser.wait(EC.visibilityOf($('header.shoalFrame')), 10000);
                    }
                }
            });
        },
        menuBar = function () {
            return Object.create({}, {
                basketIconClick: {
                    value: function () {
                        return element(by.id("shoal-basket")).element(by.tagName("a"))
                            .click();
                    }
                }
            });
        };

    module.exports = {
        header: header(),
        frame: frame(),
        menuBar: menuBar()
    };

}());