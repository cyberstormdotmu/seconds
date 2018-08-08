/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.views.orders')
        .controller('shoalSupplier.views.orders.SupplierOrderDetailsController', function (Notification, shoalSupplier_classes_Order, shoalSupplier_orders_OrderService, $uibModal, $stateParams) {
            var vm = this,
                orderAdminService = shoalSupplier_orders_OrderService,
                orderReference = $stateParams.reference,
                paymentsAtCheckout = [],
                paymentsRecorded = [],
                unpaidSupplierCreditDetail = [],
                sumSupplierCreditWhilePayments = function (items, filter) {
                    return items.filter(filter).reduce(function (previousTotal, current) {
                        return previousTotal + current.amount;
                    }, 0);
                },
                fetchOrder = function () {
                    orderAdminService.fetchOrder(orderReference)
                        .then(function (order) {
                            paymentsAtCheckout = [];
                            paymentsRecorded = [];
                            unpaidSupplierCreditDetail = [];
                            vm.order = shoalSupplier_classes_Order(order);
                            vm.order.payments.forEach(function (value) {
                                if (value.paymentRecordType === 'SUPPLIER_CREDIT_PAYMENT') {
                                    paymentsRecorded.push(value);
                                } else if (value.paymentRecordType === null || value.paymentRecordType === 'ORDER_CHECKOUT_PAYMENT') {
                                    paymentsAtCheckout.push(value);
                                }
                            });

                            vm.order.paymentsAtCheckout = paymentsAtCheckout;
                            vm.order.totalPaymentAtCheckout = vm.order.paymentsAtCheckout.reduce(function (previousTotal, current) {
                                return previousTotal + current.amount;
                            }, 0);

                            vm.order.paymentsRecorded = paymentsRecorded;
                            vm.order.availableSupplierCreditInPayment = sumSupplierCreditWhilePayments(vm.order.paymentsAtCheckout, function (payment) {
                                return payment.type === 'Supplier Credits';
                            }, 0);

                            vm.order.paidSupplierCreditInPayment = sumSupplierCreditWhilePayments(vm.order.paymentsRecorded, function (payment) {
                                return payment.paymentRecordType === 'SUPPLIER_CREDIT_PAYMENT';
                            }, 0);

                            if ((vm.order.availableSupplierCreditInPayment - vm.order.paidSupplierCreditInPayment) > 0) {
                                unpaidSupplierCreditDetail.type = 'Unpaid Supplier Credits';
                                unpaidSupplierCreditDetail.paymentRecordType = 'SUPPLIER_CREDIT_PAYMENT';
                                unpaidSupplierCreditDetail.amount = (vm.order.availableSupplierCreditInPayment - vm.order.paidSupplierCreditInPayment);
                                paymentsRecorded.push(unpaidSupplierCreditDetail);
                            }
                            vm.order.totalPaymentAtRecorded = vm.order.paymentsRecorded.reduce(function (previousTotal, current) {
                                return previousTotal + current.amount;
                            }, 0);

                        }, function (failure) {
                            Notification.error({message: failure.reason, delay: 30000});
                        });
                },
                confirmOrder = function () {
                    console.log("confirm order requested for order reference " + orderReference);
                    orderAdminService.confirmOrder(orderReference, vm.order.version)
                        .then(function () {
                            Notification.success('Order confirmed successfully!', {
                                delay: 30000
                            });
                            fetchOrder();
                        }, function (failure) {
                            Notification.error({message: failure.reason, delay: 30000});
                        });
                },
                cancelOrder = function () {
                    console.log("cancel order requested for order reference " + orderReference);
                    orderAdminService.cancelOrder(orderReference, vm.order.version)
                        .then(function () {
                            Notification.success('Order cancelled successfully!', {
                                delay: 30000
                            });
                            fetchOrder();
                        }, function (failure) {
                            Notification.error({message: failure.reason, delay: 30000});
                        });
                },
                recordPayment = function () {
                    var modalInstance = $uibModal.open({
                        templateUrl: 'views/orders/supplierOrderPaymentView.html',
                        controller: 'shoalSupplier.views.orders.SupplierOrderPaymentController as vm',
                        size: 'lg',
                        resolve: {
                            unpaidAmount: vm.order.summary.unpaidAmount
                        }
                    });

                    modalInstance.result.then(function (paymentDetails) {
                        orderAdminService.recordPayment(orderReference, vm.order.version, paymentDetails)
                            .then(function () {
                                Notification.success('Payment recorded successfully!', {
                                    delay: 30000
                                });
                                fetchOrder();
                            }, function (failure) {
                                Notification.error({message: failure.reason, delay: 30000});
                            });
                    });
                },
                deletePayment = function (paymentReference) {
                    console.log("delete payment requested for order reference and payment reference " + orderReference, paymentReference);
                    orderAdminService.deletePayment(orderReference, vm.order.version, paymentReference)
                        .then(function () {
                            Notification.success('Order payment deleted successfully!', {
                                delay: 30000
                            });
                            fetchOrder();
                        }, function (failure) {
                            Notification.error({message: failure.reason, delay: 30000});
                        });
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

            vm.panels = {
                buyerPanelOpen: true,
                linesPanelOpen: true,
                paymentsPanelOpen: true
            };
            vm.sumCreditsAvailable = sumCreditsAvailable;
            vm.sumCreditsSpent = sumCreditsSpent;
            vm.sumCreditsEarned = sumCreditsEarned;
            vm.deletePayment = deletePayment;
            vm.recordPayment = recordPayment;
            vm.cancelOrder = cancelOrder;
            vm.confirmOrder = confirmOrder;

            fetchOrder();
        });
}());