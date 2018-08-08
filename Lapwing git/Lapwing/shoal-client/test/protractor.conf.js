/*global exports */
'use strict';
var HtmlScreenshotReporter = require('protractor-jasmine2-screenshot-reporter'),

    screenshotReporter = new HtmlScreenshotReporter({
        dest: 'target/screenshots',
        cleanDestination: true,
        filename: 'E2E-test-report.html',
        ignoreSkippedSpecs: true,
        captureOnlyFailedSpecs: true,
        showSummary: true
    });

exports.config = {
    allScriptsTimeout: 11000,
    directConnect: true,

    specs: [
        'e2e/**/*E2E.js'
    ],

    capabilities: {
        'browserName': 'chrome'
    },

    baseUrl: 'http://localhost:8443/app/',

    framework: 'jasmine2',

    /** how many concurrent brower session are allowed to run **/
    /** warning: more than one seems to cause intermittent protractor syncronisation issues **/
    maxSessions: 1,

    jasmineNodeOpts: {
        showColors: true,
        isVerbose: true,
        includeStackTrace: true,
        defaultTimeoutInterval: 30000
    },


    // Setup the report before any tests start
    beforeLaunch: function () {
        return new Promise(function (resolve) {
            screenshotReporter.beforeLaunch(resolve);
        });
    },

    // Assign the test reporter to each running instance
    onPrepare: function () {
        var width = 1280,
            height = 960;

        /** create selenium screenshots **/
        jasmine.getEnv().addReporter(screenshotReporter);

        /** set the browser window resolution **/
        browser.driver.manage().window().setSize(width, height);
    },

    // Close the report after all tests finish
    afterLaunch: function (exitCode) {
        return new Promise(function (resolve) {
            screenshotReporter.afterLaunch(resolve.bind(this, exitCode));
        });
    }
};
