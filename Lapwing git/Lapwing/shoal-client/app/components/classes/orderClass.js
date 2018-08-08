/*global angular */
(function () {
    'use strict';
    angular.module('shoalApp.classes').
        factory('shoalApp_classes_Order', function (shoalApp_classes_Basket) {

            // using douglas crockford's functional constructor pattern
            return function initOrderClass(my) {
                var that,
                    basketConstructor = shoalApp_classes_Basket,
                    getLines = function () {
                        var lines = [];
                        my.basket.forEach(function (item) {
                            var line = {
                                productCode: item.productCode,
                                quantity: item.quantity,
                                unitPrice: item.unitPrice
                            };
                            lines.push(line);
                        });
                        return lines;
                    };

                my = my || {};
                my.basket = basketConstructor();
                my.creditToBeApplied = 0;
                my.appliedVendorCredits = [
                    {
                        vendorName: '',
                        creditsApplied: 0
                    }
                ];
                my.invoiceAddress = {};
                my.deliveryAddress = {};
                my.paymentMethod = undefined;
                my.paymentCardToken = undefined;
                my.orderReference = undefined;

                that = Object.create({}, {
                    basket: {
                        get: function () {
                            return my.basket;
                        },
                        set: function (val) {
                            my.basket = val;
                        }
                    },
                    lines: {
                        get: function () {
                            return getLines();
                        }
                    },
                    creditToBeApplied: {
                        get: function () {
                            return my.creditToBeApplied;
                        },
                        set: function (val) {
                            my.creditToBeApplied = val;
                        }
                    },
                    appliedVendorCredits: {
                        get: function () {
                            return my.appliedVendorCredits;
                        },
                        set: function (val) {
                            my.appliedVendorCredits = val;
                        }
                    },
                    balanceToPay: {
                        get: function () {
                            return my.basket.grossTotal - my.creditToBeApplied - my.appliedVendorCredits[0].creditsApplied;
                        }
                    },
                    invoiceAddress: {
                        get: function () {
                            return my.invoiceAddress;
                        },
                        set: function (val) {
                            my.invoiceAddress = val;
                        }
                    },
                    deliveryAddress: {
                        get: function () {
                            return my.deliveryAddress;
                        },
                        set: function (val) {
                            my.deliveryAddress = val;
                        }
                    },
                    paymentMethod: {
                        get: function () {
                            return my.paymentMethod;
                        },
                        set: function (val) {
                            my.paymentMethod = val;
                        }
                    },
                    paymentCardToken: {
                        get: function () {
                            return my.paymentCardToken;
                        },
                        set: function (val) {
                            my.paymentCardToken = val;
                        }
                    },
                    orderReference: {
                        get: function () {
                            return my.orderReference;
                        },
                        set: function (val) {
                            my.orderReference = val;
                        }
                    }
                });

                return that;
            };
        });
}());
