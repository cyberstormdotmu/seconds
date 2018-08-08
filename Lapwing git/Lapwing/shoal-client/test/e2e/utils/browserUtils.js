(function () {
    "use strict";
    var EC = protractor.ExpectedConditions,
        jQueryId = require('./jqueryId'),
        TIMEOUT = 10000,
        self = {};

    self.waitFor = function (elementId) {
        return browser.wait(EC.visibilityOf($(jQueryId(elementId))), TIMEOUT);
    };

    module.exports = Object.create(self);
}());