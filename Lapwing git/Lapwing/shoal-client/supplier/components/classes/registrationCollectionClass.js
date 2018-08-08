/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.classes').factory('shoalSupplier_classes_RegistrationCollection', function () {

        // using douglas crockford's functional constructor pattern
        return function initRegistrationCollectionClass(spec, my) {
            console.log("initialising registration collection class");
            var that;
            my = my || {};

            my.registrations = [];
            my.confirmregistrations = [];
            my.rejectregistrations = [];
            my.buildRegistration = function () {
                return {
                    buyer: {
                        firstName: '',
                        surname: '',
                        emailAddress: '',
                        appliedFor: ''
                    },
                    organisation: {
                        name: '',
                        registrationNumber: '',
                        mobileNumber: ''
                    },
                    registeredDate: ''
                };
            };

            that = Object.create({});
            that.refresh = function (afterRefresh) {
                my.refresh(my, afterRefresh);
            };
            that.authorise = function (registration) {
                my.authorise(registration.buyer, function afterAuthorisation() {
                    console.log("buyer was activated");
                    that.refresh();
                });
            };
            that.cancelauthorise = function (registration) {
                my.cancelauthorise(registration.buyer, function afterAuthorisation() {
                    console.log("buyer was rejected");
                    that.refresh();
                });
            };
            // bindable
            Object.defineProperty(that, "registrations", {
                get: function () {
                    return my.registrations;
                }
            });
            Object.defineProperty(that, "confirmregistrations", {
                get: function () {
                    return my.confirmregistrations;
                }
            });
            Object.defineProperty(that, "rejectregistrations", {
                get: function () {
                    return my.rejectregistrations;
                }
            });
            return that;

        };
    });
}());
