/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.views.activeOffers')
        .controller('shoalSupplier.views.activeOffers.SupplierOfferDetailsController', function ($scope, Notification, shoalSupplier_offers_OfferService, $uibModal, $stateParams) {
            var vm = this,
                offerService = shoalSupplier_offers_OfferService,
                offerReference = $stateParams.reference,
                fetchOfferReport = function () {
                    offerService.fetchOfferReport(offerReference)
                        .then(function (report) {
                            vm.report = report;
                            vm.allOutstandingBalanceSheet = [];
                            vm.report.outstandingBalanceOfADay.forEach(function (value) {
                                if (value.balanceToBePaidToLapwing - value.balanceToBeTransferredToSupplier > 0 || value.balanceToBeTransferredToSupplier - value.balanceToBePaidToLapwing > 0) {
                                    vm.allOutstandingBalanceSheet.push({
                                        paymentRecordDate : value.date,
                                        from : value.balanceToBePaidToLapwing > value.balanceToBeTransferredToSupplier ? vm.report.vendorName : 'Silverwing',
                                        to: value.balanceToBePaidToLapwing > value.balanceToBeTransferredToSupplier ? 'Silverwing' : vm.report.vendorName,
                                        userReference: '',
                                        receivedDate: '',
                                        received: 'No',
                                        recordPaymentStatus: '',
                                        amount: value.balanceToBeTransferredToSupplier > value.balanceToBePaidToLapwing ? (value.balanceToBeTransferredToSupplier - value.balanceToBePaidToLapwing) : (value.balanceToBePaidToLapwing - value.balanceToBeTransferredToSupplier),
                                        offerReference: offerReference
                                    });
                                }
                            });

                            vm.report.supplierPayments.forEach(function (value) {
                                console.log(value);
                                vm.allOutstandingBalanceSheet.push({
                                    paymentRecordDate : value.paymentRecordDate,
                                    from : value.from,
                                    to: value.to,
                                    userReference: value.userReference,
                                    receivedDate: value.dateReceived,
                                    received: 'Yes',
                                    recordPaymentStatus: value.recordPaymentStatus,
                                    amount: value.amount,
                                    offerReference: offerReference
                                });
                                vm.allOutstandingBalanceSheet.forEach(function (innervalue) {
                                    if (value.paymentRecordDate === innervalue.paymentRecordDate && innervalue.received === 'No') {
                                        innervalue.amount = innervalue.amount - value.amount;
                                    }
                                });
                            });
                        }, function (failure) {
                            Notification.error({message: failure.reason, delay: 30000});
                        });
                },
                recordPayment = function (outstandingBalanceOfDay) {
                    var modalInstance = $uibModal.open({
                        templateUrl: 'views/activeOffers/SupplierOfferPaymentView.html',
                        controller: 'shoalSupplier.views.activeOffers.SupplierOfferPaymentController as vm',
                        size: 'lg',
                        resolve: {
                            report: vm.report,
                            outstandingBalanceOfDay: outstandingBalanceOfDay
                        }
                    });

                    modalInstance.result.then(function (paymentDetails) {
                        offerService.recordPayment(paymentDetails)
                            .then(function () {
                                Notification.success('Payment recorded successfully!', {
                                    delay: 30000
                                });
                                fetchOfferReport();
                            }, function (failure) {
                                Notification.error({message: failure.reason, delay: 30000});
                            });
                    });
                };

            fetchOfferReport();
            vm.recordPayment = recordPayment;
        });
}());