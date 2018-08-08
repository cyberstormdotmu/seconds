(function () {
    'use strict';
    var appPage = require('./pageObjects/app/appPage'),
        loginPage = require('./pageObjects/app/loginPage'),
        continueIfDone = require('./utils/setupSpec').continueIfDone,
        reset = require('./utils/setupSpec').reset;

    describe('E2E: Login', function () {

        beforeAll(reset());

        beforeEach(continueIfDone(function (done) {
           // console.log("beforeEach in loginE2E");

            browser.manage().deleteAllCookies()
                .then(loginPage.login.goto)
                .then(function () {
                    return browser.waitForAngular();
                })
                .then(done);
        }));

        it("should show the login form", function () {
            expect(loginPage.login.loginForm().isDisplayed()).toBeTruthy('login form should be displayed');
        });

        it("should authorise a valid user's credentials", function () {

            loginPage.login.loginWithDefaultUser();
            appPage.frame.waitForLoad();

            expect(appPage.frame.isLoaded).toBeTruthy('shoal app not loaded');
        });
    });
}());