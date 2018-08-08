/*global angular */
(function () {
    'use strict';
    angular.module('shoalApp.classes').
        factory('shoalApp_classes_PaymentDetails', function () {

            return function initClass(my) {
                var that;

                my = my || {};
                my.paymentMethod = 'Card Payment';
                my.paymentToken = undefined;
                my.cardNumberLastFourDigits = undefined;
                my.cardExpiryMonth = undefined;
                my.cardExpiryYear = undefined;
                my.cardholderName = undefined;
                my.cardType = undefined;
                my.tokenCreatedDateTime = undefined;

                that = Object.create({}, {
                    paymentMethod: {
                        get: function () {
                            return my.paymentMethod;
                        }
                    },
                    paymentToken: {
                        get: function () {
                            return my.paymentToken;
                        },
                        set: function (val) {
                            my.paymentToken = val;
                        }
                    },
                    cardNumberLastFourDigits: {
                        get: function () {
                            return my.cardNumberLastFourDigits;
                        },
                        set: function (val) {
                            my.cardNumberLastFourDigits = val;
                        }
                    },
                    cardExpiryMonth: {
                        get: function () {
                            return my.cardExpiryMonth;
                        },
                        set: function (val) {
                            my.cardExpiryMonth = val;
                        }
                    },
                    cardExpiryYear: {
                        get: function () {
                            return my.cardExpiryYear;
                        },
                        set: function (val) {
                            my.cardExpiryYear = val;
                        }
                    },
                    cardholderName: {
                        get: function () {
                            return my.cardholderName;
                        },
                        set: function (val) {
                            my.cardholderName = val;
                        }
                    },
                    cardType: {
                        get: function () {
                            return my.cardType;
                        },
                        set: function (val) {
                            my.cardType = val;
                        }
                    },
                    tokenCreatedDateTime: {
                        get: function () {
                            return my.tokenCreatedDateTime;
                        },
                        set: function (val) {
                            my.tokenCreatedDateTime = val;
                        }
                    }
                });

                return that;
            };
        });
}());
