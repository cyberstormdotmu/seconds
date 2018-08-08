/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalApp.views.account.orders')
        .controller('shoalApp.views.account.orders.OrderDetailController', function ($scope, $stateParams, shoalApp_orders_OrderService) {
            var vm = this,
                data,
                docDefinition,
                innerContents,
                popupWindow,
                orderReference = $stateParams.orderReference,
                ordersService = shoalApp_orders_OrderService,
                paymentsAtCheckout = [],
                paymentsRecorded = [],
                unpaidSupplierCreditDetail = [],

                sumSupplierCreditWhilePayments = function (items, filter) {
                    return items.filter(filter).reduce(function (previousTotal, current) {
                        return previousTotal + current.amount;
                    }, 0);
                },
                getOrder = function () {
                    ordersService.getOrder(orderReference)
                        .then(function (order) {
                            vm.order = order;
                            $scope.data = order;
                            order.payments.forEach(function (value) {
                                if (value.paymentRecordType === 'SUPPLIER_CREDIT_PAYMENT') {
                                    paymentsRecorded.push(value);
                                } else if (value.paymentRecordType === null || value.paymentRecordType === 'ORDER_CHECKOUT_PAYMENT') {
                                    paymentsAtCheckout.push(value);
                                }
                            });
                            vm.paymentsAtCheckout = paymentsAtCheckout;
                            vm.totalPaymentAtCheckout = vm.paymentsAtCheckout.reduce(function (previousTotal, current) {
                                return previousTotal + current.amount;
                            }, 0);
                            vm.paymentsRecorded = paymentsRecorded;
                            vm.availableSupplierCreditInPayment = sumSupplierCreditWhilePayments(vm.paymentsAtCheckout, function (payment) {
                                return payment.paymentType === 'Supplier Credits';
                            }, 0);
                            vm.paidSupplierCreditInPayment = sumSupplierCreditWhilePayments(vm.paymentsRecorded, function (payment) {
                                return payment.paymentRecordType === 'SUPPLIER_CREDIT_PAYMENT';
                            }, 0);
                            if ((vm.availableSupplierCreditInPayment - vm.paidSupplierCreditInPayment) > 0) {
                                unpaidSupplierCreditDetail.paymentType = 'Unpaid Supplier Credits';
                                unpaidSupplierCreditDetail.paymentRecordType = 'SUPPLIER_CREDIT_PAYMENT';
                                unpaidSupplierCreditDetail.amount = (vm.availableSupplierCreditInPayment - vm.paidSupplierCreditInPayment);
                                paymentsRecorded.push(unpaidSupplierCreditDetail);
                            }
                            vm.totalPaymentAtRecorded = vm.paymentsRecorded.reduce(function (previousTotal, current) {
                                return previousTotal + current.amount;
                            }, 0);
                        });
                },
                sumPayments = function () {
                    return vm.order.payments.reduce(function (previousTotal, current) {
                        return previousTotal + current.amount;
                    }, 0);
                },
                sumCredits = function (items, field, filter) {
                    return items.filter(filter).reduce(function (previousTotal, current) {
                        return previousTotal + current.amount[field];
                    }, 0);
                },
                sumCreditsEarned = function (items, field) {
                    return sumCredits(items, field, function (credit) {
                        return credit.creditMovementType === 'Earn';
                    });
                },
                sumCreditsSpent = function (items, field) {
                    return sumCredits(items, field, function (credit) {
                        return credit.creditMovementType !== 'Earn';
                    });
                },
                sumCreditsAvailable = function (items, field) {
                    return sumCredits(items, field, function () {
                        return true;
                    });
                };
            $scope.printToCart = function () {
                innerContents = document.getElementById('idOneGraph').innerHTML;
                popupWindow = window.open('', '_blank', 'width=1200,height=700,scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no');
                popupWindow.document.open();
                popupWindow.document.write('<html><head><link rel="stylesheet" href="./app.css"></head><body onload="window.print()">' + innerContents + '</html>');
                popupWindow.document.close();
            };
            $scope.export = function () {
                html2canvas(document.getElementById('idOneGraph'), {
                    onrendered: function (canvas) {
                        data = canvas.toDataURL();
                        docDefinition = {
                            content: [{
                                image: data,
                                width: 500
                            }]
                        };
                        pdfMake.createPdf(docDefinition).download("OrderSummary_" + orderReference + ".pdf");
                    }
                });
            };

            vm.sumCreditsAvailable = sumCreditsAvailable;
            vm.sumCreditsSpent = sumCreditsSpent;
            vm.sumCreditsEarned = sumCreditsEarned;
            vm.sumPayments = sumPayments;
            getOrder();
        });
}());