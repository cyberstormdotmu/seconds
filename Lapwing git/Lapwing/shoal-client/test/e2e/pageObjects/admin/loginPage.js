(function () {
    'use strict';
    var loginPage = function () {
        var self = {
            goto: function () {
                return browser.get('/admin/#/login');
            },
            loginWithDefaultUser: function () {
                self.setUser('oliver.squires@ishoal.com');
                self.setPassword('password');
                browser.waitForAngular();
                return self.login();
            },
            login: function () {
                return element(by.id("loginForm")).element(by.tagName("button")).click();
            },
            setUser: function (user) {
                element(by.id('email')).clear().sendKeys(user);
            },
            setPassword: function (password) {
                element(by.id('password')).clear().sendKeys(password);
            },
            waitForLoad: function () {
                browser.sleep(1000);
                browser.wait(function () {
                    return browser.isElementPresent(by.id("login-panel"));
                });
            }
        };
        return self;
    };

    module.exports = {
        login: loginPage()
    };

}());
