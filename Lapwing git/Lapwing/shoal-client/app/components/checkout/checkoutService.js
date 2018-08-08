/*global angular, console */
(function () {
    'use strict';
    function checkoutService($rootScope, $q, shoalApp_classes_Order, shoalApp_orders_OrderService, shoalApp_basket_BasketService, shoalApp_credits_CreditService, shoalApp_payment_PaymentService, shoalApp_orderVerification_OrderVerificationService) {
        var orderService = shoalApp_orders_OrderService,
            basketService = shoalApp_basket_BasketService,
            creditService = shoalApp_credits_CreditService,
            paymentService = shoalApp_payment_PaymentService,
            order,
            paymentCard,
            availableCreditBalance,
            allVendorCredits = [],
            vendorCreditBalance = [],
            maximumVendorUsableCredit,
            i,
            item,
            found,
            paymentMethodName,
            paymentChargesPercentage,
            paymentExtraCharge,
            availableCreditsForVendor,
            init = function () {
                console.log("checkoutService init");
                paymentService.init();
                paymentCard = paymentService.paymentCard;
                order = shoalApp_classes_Order();
                $rootScope.$on('basketUpdated', function (event, newBasket) {
                    console.log("basketUpdated : " + JSON.stringify(newBasket));
                    if (newBasket) {
                        order.basket = newBasket;
                    }
                });
            },
            fetchCreditBalances = function () {
                creditService.getCreditBalances()
                    .then(function (creditBalances) {
                        availableCreditBalance = creditBalances.lapwingCredits.availableCreditBalance;
                        paymentMethodName = creditBalances.paymentCharges.name;
                        paymentChargesPercentage = creditBalances.paymentCharges.paymentChargesPercentage;
                        paymentExtraCharge = creditBalances.paymentCharges.paymentExtraCharge;
                        for (i = 0; i < creditBalances.vendorCredits.length; i += 1) {
                            allVendorCredits[creditBalances.vendorCredits[i].vendorName] = creditBalances.vendorCredits[i].availableCredits;
                        }
                        angular.forEach(order.basket.items, function (value, key) {
                            if (order.basket.items.hasOwnProperty(value.productCode)) {
                                item = order.basket.items[value.productCode];
                                found = vendorCreditBalance.some(function (vc) {
                                    return vc.vendorName === item.vendorName;
                                });
                                if (!found) {
                                    if (allVendorCredits[item.vendorName] < order.basket.grossTotal) {
                                        maximumVendorUsableCredit = allVendorCredits[item.vendorName];
                                    } else {
                                        maximumVendorUsableCredit = order.basket.grossTotal;
                                    }
                                    availableCreditsForVendor = allVendorCredits[item.vendorName] === undefined ? 0 : allVendorCredits[item.vendorName];
                                    vendorCreditBalance.push({
                                        vendorName: item.vendorName,
                                        availableVendorCredits: availableCreditsForVendor,
                                        maximumVendorCreditSpend: maximumVendorUsableCredit
                                    });
                                }
                            }
                        });
                    }, function () {
                        console.log("error reading credit balances");
                    });
            },
            calculateMaximumCreditSpend = function () {
                if (availableCreditBalance < order.basket.grossTotal) {
                    return availableCreditBalance;
                }
                return order.basket.grossTotal;
            },
            submitOrder = function () {
                var defer = $q.defer();

                orderService.submitOrder(order).then(
                    function (orderSummary) {
                        console.log('order success');
                        basketService.removeAllFromBasket();
                        order.completed = true;
                        defer.resolve(orderSummary.reference);
                    },
                    function (error) {
                        console.log('order failed');
                        defer.reject(error);
                    }
                );

                return defer.promise;
            },
            obtainPaymentToken = function () {
                var defer = $q.defer();

                paymentService.createPaymentCardToken().then(
                    function (paymentCardToken) {
                        defer.resolve(paymentCardToken);
                    },
                    function (error) {
                        defer.reject(error);
                    }
                );

                return defer.promise;
            },
            placeOrder = function () {
                var defer = $q.defer();

                if (order.paymentMethod === "Card Payment") {

                    obtainPaymentToken().then(
                        function (paymentCardToken) {

                            //order.paymentMethod = "Card Payment";
                            order.paymentCardToken = paymentCardToken;
                            defer.resolve(submitOrder());
                        },
                        function (error) {
                            defer.reject(error);
                        }
                    );
                } else {
                    defer.resolve(submitOrder());
                }

                return defer.promise;
            };


        return Object.create({}, {
            init: {
                value: init
            },
            fetchCreditBalances: {
                value: fetchCreditBalances
            },
            order: {
                get: function () {
                    return order;
                }
            },
            paymentCard: {
                get: function () {
                    return paymentCard;
                }
            },
            availableCreditBalance: {
                get: function () {
                    return availableCreditBalance;
                }
            },
            paymentMethodName: {
                get: function () {
                    return paymentMethodName;
                }
            },
            paymentChargesPercentage: {
                get: function () {
                    return paymentChargesPercentage;
                }
            },
            paymentExtraCharge: {
                get: function () {
                    return paymentExtraCharge;
                }
            },
            maximumCreditSpend: {
                get: function () {
                    return calculateMaximumCreditSpend();
                }
            },
            vendorCreditBalance : {
                get : function () {
                    return vendorCreditBalance;
                }
            },

            placeOrder: {
                value: placeOrder
            }
        });
    }

    angular.module('shoalApp.checkout')
        .factory('shoalApp_checkout_CheckoutService', ['$rootScope', '$q', 'shoalApp_classes_Order',
            'shoalApp_orders_OrderService', 'shoalApp_basket_BasketService',
            'shoalApp_credits_CreditService', 'shoalApp_payment_PaymentService', checkoutService]);
}());