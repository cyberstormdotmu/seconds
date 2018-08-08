/*global exports, require, browser, jasmine, console */
'use strict';
var HtmlScreenshotReporter = require('protractor-jasmine2-screenshot-reporter'),
    JasmineReporters = require('jasmine-reporters'),

    screenshotReporter = new HtmlScreenshotReporter({
        dest: 'target/screenshots/',
        filename: 'E2E-test-report.html',
        ignoreSkippedSpecs: true,
        captureOnlyFailedSpecs: false,
        showSummary: true
    });

exports.config = {
    allScriptsTimeout: 11000,

    specs: [
        'e2e/**/*E2E.js'
    ],

    multiCapabilities: [
        {
            "browserName": "chrome",
            "seleniumAddress": 'http://localhost:4444/wd/hub'
        },
        {
            "browserName": "firefox",
            "seleniumAddress": 'http://localhost:4444/wd/hub'
        }
    ],

    /** how many concurrent brower session are allowed to run **/
    /** warning: more than one seems to cause intermittent protractor syncronisation issues **/
    maxSessions: 1,

    /*
     This is where the staging proxy lives.
     */
    baseUrl: 'https://localhost/app/',

    framework: 'jasmine2',

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

        return browser.getProcessedConfig().then(function (config) {
            var browserName = config.capabilities.browserName,
                junitReporter = new JasmineReporters.JUnitXmlReporter({
                    savePath: 'target/surefire-reports/E2E',
                    consolidateAll: true,
                    filePrefix: 'TESTS-' + browserName
                });

            console.log(config.capabilities);

            /** create junit test report **/
            jasmine.getEnv().addReporter(junitReporter);

            /** create selenium screenshots **/
            jasmine.getEnv().addReporter(screenshotReporter);

            /** set the browser window resolution **/
            browser.driver.manage().window().setSize(width, height);
        });
    },

    // Close the report after all tests finish
    afterLaunch: function (exitCode) {
        return new Promise(function (resolve) {
            screenshotReporter.afterLaunch(resolve.bind(this, exitCode));
        });
    }
};
