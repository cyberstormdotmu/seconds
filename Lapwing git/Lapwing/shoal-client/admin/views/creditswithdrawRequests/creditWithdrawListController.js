/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalAdmin.views.creditWithdrawRequests')
        .controller('shoalAdmin.views.creditWithdrawRequests.CreditWithdrawListController', function ($q, $http,  Notification, shoalAdmin_creditsWithdrawRequests_CreditsWithdrawRequestsService, $uibModal) {
            var vm = this,
                creditWithdrawService = shoalAdmin_creditsWithdrawRequests_CreditsWithdrawRequestsService,
                fetchUnconfirmedCreditWithdrawRequests = function () {
                    creditWithdrawService.getUnconfirmedWithdrawCredits()
                        .then(function (creditWithdrawRequests) {
                            vm.unconfirmedCreditWithdrawRequests = creditWithdrawRequests;
                        }, function () {
                            console.log("error reading unconfirmed Credit Withdraw Requests");
                        });
                },
                fetchConfirmedCreditWithdrawRequests = function () {
                    creditWithdrawService.getConfirmedWithdrawCredits()
                        .then(function (creditWithdrawRequests) {
                            vm.confirmedCreditWithdrawRequests = creditWithdrawRequests;
                        }, function () {
                            console.log("error reading confirmed Credit Withdraw Requests");
                        });
                },
                fetchCancelledCreditWithdrawRequests = function () {
                    creditWithdrawService.getCancelledWithdrawCredits()
                        .then(function (creditWithdrawRequests) {
                            vm.cancelledCreditWithdrawRequests = creditWithdrawRequests;
                            console.log(vm.unconfirmedCreditWithdrawRequests);
                        }, function () {
                            console.log("error reading cancelled Credit Withdraw Requests");
                        });
                },
                refresh = function () {
                    fetchUnconfirmedCreditWithdrawRequests();
                    fetchConfirmedCreditWithdrawRequests();
                    fetchCancelledCreditWithdrawRequests();
                };

            vm.cancelCreditWithdrawCredit = function (unconfirmedCreditWithdrawRequest) {
                creditWithdrawService.cancelWithdrawCredit(unconfirmedCreditWithdrawRequest.id)
                    .then(function () {
                        refresh();
                        Notification.success('Credit withdraw request cancelled successfully!', {
                            delay: 30000
                        });
                    }, function (failure) {
                        Notification.error({message: failure.reason, delay: 30000});
                    });
            };

            vm.recordPayment = function (creditWithdrawRequest) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'views/creditswithdrawRequests/creditWithdrawPaymentView.html',
                    controller: 'shoalAdmin.views.creditWithdrawRequests.CreditWithdrawPaymentController as vm',
                    size: 'lg',
                    resolve: {
                        requestedAmount: creditWithdrawRequest.requestedAmount,
                        buyerWithdrawCreditId: creditWithdrawRequest.id
                    }
                });
                modalInstance.result.then(function (creditWithdrawRequest) {
                    console.log("paymentDetails");
                    console.log(creditWithdrawRequest);
                    creditWithdrawService.recordPayment(creditWithdrawRequest)
                        .then(function () {
                            refresh();
                            Notification.success('Payment recorded successfully!', {
                                delay: 30000
                            });
                        }, function (failure) {
                            Notification.error({message: failure.reason, delay: 30000});
                        });
                });
            };
            refresh();
            vm.refresh = refresh;
            vm.unconfirmedCreditWithdrawRequests = [];
            vm.confirmedCreditWithdrawRequests = [];
            vm.cancelledCreditWithdrawRequests = [];
        });
}());