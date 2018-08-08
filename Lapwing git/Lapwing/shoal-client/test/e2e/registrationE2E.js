(function () {
    'use strict';
    var appPage = require('./pageObjects/app/appPage'),
        appLoginPage = require('./pageObjects/app/loginPage'),
        homePage = require('./pageObjects/app/homePage'),
        register = require('./interactions/app/register'),
        buyer = require('./interactions/admin/activate'),
        auth = require('./interactions/util/authenticateUser'),
        continueIfOk = require('./utils/setupSpec').continueIfOk,
        continueIfDone = require('./utils/setupSpec').continueIfDone,
        reset = require('./utils/setupSpec').reset;

    describe('E2E Register a new user :', function () {

        beforeAll(reset());

        beforeEach(continueIfOk(function () {
            //console.log("beforeEach in registrationE2E");

            browser.manage().deleteAllCookies();
        }));

        describe('when a user visits the shopfront page,', function () {

            beforeEach(continueIfDone(function (done) {
                homePage.home.goto().then(done);
            }));

            it("should provide a registration link", function () {

                expect(homePage.home.registerButton().isPresent()).toBe(true, ' registration button not present');
            });
        });

        describe('when a user visits the registration page,', function () {

            var credentials;

            it('should allow them to submit their details', function () {

                credentials = register.registerBuyer();
            });

            describe('after the user submits their details,', function () {

                describe("if the user tries to log in", function () {

                    function loginAndExpectDelayDueToMultiplePageRedirects() {
                        appLoginPage.login.goto();
                        appLoginPage.login.loginWithCredentials(credentials.userName, credentials.password);
                    }

                    beforeEach(continueIfOk(function () {
                        loginAndExpectDelayDueToMultiplePageRedirects();
                    }));

                    it('should show an error because the account is not yet active', function () {
                        expect(appLoginPage.login.errorMessage()).toBe('Your user account has not yet been activated');
                    });
                });

                describe('when the user is activated,', function () {

                    beforeEach(continueIfOk(function () {

                        buyer.activate(credentials.userName);
                        auth.authenticateUser(credentials);
                    }));

                    it('should allow the user to login,', function () {

                        expect(appPage.frame.isLoaded).toBe(true, 'user was not able to login');
                    });
                });
            });
        });
    });
}());