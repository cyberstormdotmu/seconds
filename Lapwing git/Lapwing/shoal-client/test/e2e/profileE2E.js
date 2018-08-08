(function () {
    'use strict';
    var accountPage = require('./pageObjects/app/accountPage'),
        auth = require('./interactions/util/authenticateUser'),
        buyer = require('./interactions/util/makeBuyer'),
        profile = require('./interactions/app/updateProfile'),
        continueIfOk = require('./utils/setupSpec').continueIfOk,
        continueIfDone = require('./utils/setupSpec').continueIfDone,
        resetDone = require('./utils/setupSpec').resetDone;

    describe('E2E Complete a buyer profile :', function () {

        var credentials;

        beforeAll(resetDone(function (done) {
            //console.log("beforeAll in profileE2E");

            buyer.makeBuyer(function (authorisedCredentials) {
                console.log("user authorised - capturing credentials");
                credentials = authorisedCredentials;
                done();
            });
        }));

        beforeEach(continueIfOk(function () {
            //console.log("beforeEach in profileE2E");

            browser.manage().deleteAllCookies();
        }));

        describe('when a new user logs in,', function () {

            beforeEach(continueIfDone(function (done) {

                auth.authenticateUser(credentials).then(function () {
                    return accountPage.profileForm.waitForLoad();
                }).then(done);
            }));

            it('should redirect them to the account profile page', function () {
                expect(browser.getCurrentUrl()).toContain('#/account');
                expect(accountPage.profileForm.isPresent).toBe(true, 'profile form not present');
            });

            describe("when the user completes their profile", function () {

                beforeEach(continueIfOk(function () {
                    profile.updateProfileWithDefaults(accountPage.profileForm);
                }));

                it("should notify them that it was accepted", function () {

                    expect(accountPage.profileForm.isProfileAccepted).toBe(true,
                        ' profile update was not accepted for some reason');
                });
            });
        });

        describe("when the user next logs in", function () {

            beforeEach(continueIfOk(function () {
                auth.authenticateUser(credentials);
            }));

            it("should put them on the main page", function () {
                expect(browser.getCurrentUrl()).toContain('#/shopfront');
            });
        });
    });
}());