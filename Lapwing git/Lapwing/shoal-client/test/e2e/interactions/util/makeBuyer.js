(function () {
    'use strict';
    var register = require('./../app/register'),
        buyer = require('./../admin/activate'),
        abort = require('../../utils/setupSpec').abort,
        makeBuyer = function (afterBuyerAuthorisedFn) {
            var userDetails = register.registerBuyer();
            buyer.activate(userDetails.userName).then(function () {
                if (afterBuyerAuthorisedFn) {
                    console.log("after activation");
                    afterBuyerAuthorisedFn(userDetails);
                }
            }, function (reason) {
                reason = reason || '<none given>';
                console.log("activation failed, reason: " + reason);
                abort('could not find new registration on page, unable to authorise user.');
            });

            return userDetails;
        };

    module.exports = {
        makeBuyer: makeBuyer
    };
}());
