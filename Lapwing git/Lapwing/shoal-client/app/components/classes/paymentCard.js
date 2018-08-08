/*global angular */
(function () {
    'use strict';
    angular.module('shoalApp.classes').
        factory('shoalApp_classes_PaymentCard', function () {

            return function initClass(my) {
                var that;

                my = my || {};
                my.cardNumber = '';
                my.expiry = undefined;
                my.cvc = '';
                my.cardholderName = '';

                that = Object.create({}, {
                    cardNumber: {
                        get: function () {
                            return my.cardNumber;
                        },
                        set: function (val) {
                            my.cardNumber = val;
                        }
                    },
                    expiry: {
                        get: function () {
                            return my.expiry;
                        },
                        set: function (val) {
                            my.expiry = val;
                        }
                    },
                    cvc: {
                        get: function () {
                            return my.cvc;
                        },
                        set: function (val) {
                            my.cvc = val;
                        }
                    },
                    cardholderName: {
                        get: function () {
                            return my.cardholderName;
                        },
                        set: function (val) {
                            my.cardholderName = val;
                        }
                    }
                });

                return that;
            };
        });
}());
