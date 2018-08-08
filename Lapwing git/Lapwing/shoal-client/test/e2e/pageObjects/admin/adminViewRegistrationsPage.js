(function () {
    'use strict';
    var newRegistrations = function () {

        var my =  Object.create({}, {
            authoriseAndConfirm: {
                value: function (userName) {
                    var elementId = my.makeAuthElementId(userName);
                    return element(by.id(elementId)).click()
                        .then(function () {
                            console.log("clicking registration button: #" + "registrations-confirm-" + userName);
                            return element(by.id("registrations-confirm-" + userName)).click();
                        });
                }
            },
            makeAuthElementId: {
                value: function (userName) {
                    return "registrations-authorise-" + userName;
                }
            },
            refresh: {
                value: function () {
                    console.log("refreshing registration view...");
                    return element(by.id("refresh")).click();
                }
            }
        });
        return my;
    };

    module.exports = {
        newRegistrations: newRegistrations()
    };

}());
