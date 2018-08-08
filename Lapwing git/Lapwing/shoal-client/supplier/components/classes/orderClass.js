/*global angular */
(function () {
    'use strict';
    angular.module('shoalSupplier.classes').
        factory('shoalSupplier_classes_Order', function () {

            return function initOrderClass(spec, my) {
                var that,
                    getPayments = function (spec) {
                        var i,
                            items = [],
                            creditType,
                            item,
                            creditPaymentType;

                        for (i = 0; i < spec.payments.length; i += 1) {
                            item = spec.payments[i];

                            items.push({
                                internalReference: item.paymentReference,
                                date: item.dateReceived,
                                type: item.paymentType,
                                amount: item.amount,
                                paymentGatewayCharge: item.paymentGatewayCharges,
                                userReference: item.userReference,
                                paymentRecordType: item.paymentRecordType
                            });
                        }

                        for (i = 0; i < spec.appliedCredits.length; i += 1) {
                            item = spec.appliedCredits[i];
                            creditType = item.spendType === 'Spend' ? 'Silverwing Credits' : 'Supplier Credits';
                            creditPaymentType = 'ORDER_CHECKOUT_PAYMENT';
                            items.push({
                                date: item.created,
                                type: creditType,
                                amount: item.amount.gross,
                                originalOrderReference: item.originalOrderReference,
                                originalOrderLineProductCode: item.originalOrderLineProductCode,
                                paymentRecordType: creditPaymentType
                            });
                        }

                        return items.sort(function (a, b) {
                            if (a.date > b.date) {
                                return 1;
                            }
                            if (a.date < b.date) {
                                return -1;
                            }
                            return 0;
                        });
                    },
                    calculatePaymentTotal = function () {
                        return my.order.payments.reduce(function (previousTotal, currentPayment) {
                            return previousTotal + currentPayment.amount;
                        }, 0);
                    },
                    setOrder = function (spec) {
                        var order = {
                            summary: spec.summary,
                            version: spec.version,
                            buyer: spec.buyer,
                            invoiceAddress: spec.invoiceAddress,
                            deliveryAddress: spec.deliveryAddress,
                            lines: spec.lines,
                            payments: getPayments(spec)
                        };

                        my.order = order;
                    };

                my = my || {};
                if (spec) {
                    setOrder(spec);
                }

                that = Object.create({}, {
                    summary: {
                        get: function () {
                            return my.order.summary;
                        }
                    },
                    version: {
                        get: function () {
                            return my.order.version;
                        }
                    },
                    buyer: {
                        get: function () {
                            return my.order.buyer;
                        }
                    },
                    invoiceAddress: {
                        get: function () {
                            return my.order.invoiceAddress;
                        }
                    },
                    deliveryAddress: {
                        get: function () {
                            return my.order.deliveryAddress;
                        }
                    },
                    lines: {
                        get: function () {
                            return my.order.lines;
                        }
                    },
                    payments: {
                        get: function () {
                            return my.order.payments;
                        }
                    },
                    paymentTotal: {
                        get: function () {
                            return calculatePaymentTotal();
                        }
                    }
                });

                return that;
            };
        });
}());
