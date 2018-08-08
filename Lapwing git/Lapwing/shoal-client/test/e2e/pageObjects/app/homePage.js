(function () {
    'use strict';
    var landingPage = function () {
        return {
            goto: function () {
                return browser.get('/public');
            },
            registerButton: function () {
                return element(by.id("register-button"));
            },
            registerClick: function () {
                return this.registerButton().click();
            }
        };
    };

    module.exports = {
        home: landingPage()
    };

}());
