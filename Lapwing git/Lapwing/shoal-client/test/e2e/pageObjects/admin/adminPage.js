(function () {
    'use strict';
    var menuBar = function () {
        return Object.create({}, {
            NewRegistrationsClick: {
                value: function () {
                    return element(by.id("nav-registrations")).click();
                }
            }
        });
    };

    module.exports = {
        menuBar: menuBar()
    };

}());