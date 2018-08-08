(function () {
    'use strict';

    var adminLoginPage = require('./../../pageObjects/admin/loginPage'),
        adminPage = require('./../../pageObjects/admin/adminPage'),
        adminViewRegistrationsPage = require('./../../pageObjects/admin/adminViewRegistrationsPage'),
        jQueryId = require('./../../utils/jqueryId'),
        Promise = require('promise'),
        REGISTRATION_WAIT_TIMEOUT = 30000,
        my = {},
        EC = protractor.ExpectedConditions;

    function authorise(userName, resolve, reject) {
        var elementId = adminViewRegistrationsPage.newRegistrations.makeAuthElementId(userName);

        console.log("testing visibility of :" + jQueryId(elementId) + " at " + new Date().toUTCString());

        if ((Date.now() - my.startTime) > REGISTRATION_WAIT_TIMEOUT) {
            if (reject) {
                reject("authorise timed out at + " + new Date().toUTCString());
            }
            return;
        }

        EC.visibilityOf($(jQueryId(elementId)))()
            .then(function (isVisible) {
                if (isVisible) {
                    console.log(jQueryId(elementId) + " is visible");
                    adminViewRegistrationsPage.newRegistrations.authoriseAndConfirm(userName)
                        .then(resolve);
                } else {
                    if ((Date.now() - my.refreshTimeMillis) > 1000) {
                        console.log(jQueryId(elementId) + " is not visible - refreshing list");
                        adminViewRegistrationsPage.newRegistrations.refresh().then(function () {
                            console.log("recurse call authorise until element is visible");
                            my.refreshTimeMillis = Date.now();
                            authorise(userName, resolve, reject);
                        });
                    } else {
                        console.log("millis elapsed since last refresh " + (Date.now() - my.refreshTimeMillis));
                        authorise(userName, resolve, reject);
                    }
                }
            });
    }

    my.activate = function (userName) {

        return new Promise(function (resolve, reject) {
            // reset the timers
            my.refreshTimeMillis = my.startTime = Date.now();
            adminLoginPage.login.goto()
                .then(adminLoginPage.login.loginWithDefaultUser)
                .then(adminPage.menuBar.NewRegistrationsClick)
                .then(function () {
                    // will invoke resolve when action is completed
                    authorise(userName, resolve, reject);
                });
        });
    };

    module.exports = Object.create(my);
}());
