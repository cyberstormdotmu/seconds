(function () {
    'use strict';

    var appPage = require('./../../pageObjects/app/appPage'),
        appLoginPage = require('./../../pageObjects/app/loginPage'),
        authenticateUser = function (credentials) {

            return appLoginPage.login.goto()
                .then(function () {
                    if (credentials) {
                        console.log("attempting login with " + JSON.stringify(credentials));
                        return appLoginPage.login.loginWithCredentials(credentials.userName, credentials.password);
                    }
                    return appLoginPage.login.loginWithDefaultUser();

                }).then(appPage.frame.waitForLoad);
        };

    module.exports = {
        authenticateUser: authenticateUser
    };

}());
