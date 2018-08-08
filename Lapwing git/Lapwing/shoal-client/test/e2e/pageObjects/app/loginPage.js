(function () {
    'use strict';
    var EC = protractor.ExpectedConditions,
        loginPage = function () {
            return {
                goto: function () {
                    browser.get('/public');
                    browser.waitForAngular();
                    return element(by.id("login-button")).click();
                },
                loginForm: function () {
                    return element(by.id("loginForm"));
                },
                loginWithDefaultUser: function () {
                    this.setUser('oliver.squires@ishoal.com');
                    this.setPassword('password');
                    return this.login();
                },
                loginWithCredentials: function (userName, password) {
                    this.setUser(userName);
                    this.setPassword(password);
                    return this.login();
                },
                login: function () {
                    return this.loginForm().element(by.tagName("button")).click();
                },
                setUser: function (user) {
                    element(by.id('email')).clear().sendKeys(user);
                },
                setPassword: function (password) {
                    element(by.id('password')).clear().sendKeys(password);
                },
                errorMessage: function () {
                    return element(by.id('login-error-message')).getText();
                },
                waitForLoad: function (afterLoad) {
                    browser.sleep(1000);
                    browser.wait(EC.visibilityOf($('#login-panel')), 10000);
                    expect(element(by.id("login-panel")).isDisplayed()).toBe(true, 'LoginPage was not loaded');
                    if (afterLoad) {
                        return afterLoad();
                    }
                }
            };
        };

    module.exports = {
        login: loginPage()
    };

}());
