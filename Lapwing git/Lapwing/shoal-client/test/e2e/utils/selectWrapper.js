(function () {
    "use strict";

    var selectWrapper = function () {

        return function (selector) {
            var my = {};

            my.webElement = element(selector);

            my.getOptions = function () {
                return my.webElement.all(by.tagName('option'));
            };
            my.getSelectedOptions = function () {
                return my.webElement.all(by.css('option[selected="selected"]'));
            };
            my.getSelectByValue = function (value) {
                return my.webElement.all(by.css('option[value="' + value + '"]')).click();
            };
            my.getSelectByPartialText = function (text) {
                return my.webElement.all(by.cssContainingText('option', text)).click();
            };
            my.selectByValue = function (text) {
                return my.webElement.all(by.css(('option[value="' + text + '"]'))).click();
            };
            my.selectByLabel = function (text) {
                return my.webElement.all(by.css(('option[label="' + text + '"]'))).click();
            };

            return Object.create(my);
        };
    };

    module.exports = selectWrapper();
}());