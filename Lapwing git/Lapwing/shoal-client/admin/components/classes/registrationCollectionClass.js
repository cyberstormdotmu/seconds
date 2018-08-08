/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.classes').factory('shoalAdmin_classes_RegistrationCollection', function (Notification) {

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
                        appliedFor: '',
                        westcoastNumber: ''
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
            that.confirmaftercontractsigned = function (registration) {
                my.confirmaftercontractsigned(registration.buyer, function afterAuthorisation() {
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
            that.resendemail = function (registration) {
                my.resendemail(registration.buyer, function afterAuthorisation() {
                    Notification.success('Mail sent successfully!', {delay: 30000 });
                    console.log("Mail is resent");
                    that.refresh();
                }, function afterAuthorisationFail() {
                    Notification.error({message: 'Mail not sent,Please try after sometime.', delay: 30000});
                    console.log("Mail not sent,Please try after sometime.");
                    that.refresh();
                });
            };
            that.copiedClipboard = function (isSucces) {
                if (isSucces) {
                    Notification.success('URL copied to clipboard!', {delay: 30000 });
                } else if (!isSucces) {
                    Notification.error({message: 'URL copying to clipboard failed!', delay: 30000});
                }
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
            Object.defineProperty(that, "authenticationPendingBuyersregistrations", {
                get: function () {
                    return my.authenticationPendingBuyersregistrations;
                }
            });
            Object.defineProperty(that, "contractSignPendingBuyersregistrations", {
                get: function () {
                    return my.contractSignPendingBuyersregistrations;
                }
            });
            return that;

        };
    });
}());
