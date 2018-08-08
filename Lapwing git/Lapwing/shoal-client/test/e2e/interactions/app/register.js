(function () {
    'use strict';

    var userRegistrationPage = require('./../../pageObjects/app/userRegistrationPage'),
        randomCodeGenerator = require('../../utils/randomCodeGenerator'),
        abort = require('../../utils/setupSpec').abort,
        waitFor = require('../../utils/browserUtils').waitFor,

        registerBuyer = function () {
            var userName = randomCodeGenerator(10) + "@gmail.com",
                password = "Password1$";
            userRegistrationPage.registrationForm.goto();

            userRegistrationPage.registrationForm.emailAddress = userName;
            userRegistrationPage.registrationForm.forename = "Roger";
            userRegistrationPage.registrationForm.surname = "Watkins";
            userRegistrationPage.registrationForm.organisationName = "GnomeSoft Limited";
            userRegistrationPage.registrationForm.organisationRegNumber = "8669720";
            userRegistrationPage.registrationForm.password = password;
            userRegistrationPage.registrationForm.contactMobile = "12345678911";

            console.log("attempting registration with " + JSON.stringify({
                userName: userName,
                password: password
            }));

            userRegistrationPage.registrationForm.submit();

            // wait for list of users to load
            browser.waitForAngular();

            waitFor('savedRegistration').thenCatch(function saveFailed(e) {
                abort('saving user registration failed - check the browser and server logs. \nCause:\n' +
                    e.message || '<no error given>');
            });

            return {
                userName: userName,
                password: password
            };
        };

    module.exports = {
        registerBuyer: registerBuyer
    };
}());
