/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalApp.views.account.credits')
        .controller('shoalApp.views.account.credits.CreditSummaryController', function ($scope, $http, ENV, shoalApp_credits_CreditService) {
            var vm = this,
                creditService = shoalApp_credits_CreditService;

            vm.fetchCreditBalances = function () {
                creditService.getCreditBalances()
                    .then(function (creditBalances) {
                        vm.creditBalances = creditBalances;
                        vm.creditWithdrawForm = {};
                        vm.creditWithdrawForm.creditToWithdraw = 0;
                        vm.creditWithdrawForm.isCompleted = false;
                        vm.creditWithdrawForm.isSaved = false;
                        vm.creditWithdrawForm.isError = false;
                    }, function () {
                        console.log("error reading credit balances");
                    });
            };
            vm.saveCreditWithdrawInformation = function () {
                console.log("Function fired!");
                creditService.saveCreditWithdraw(vm.creditWithdrawForm)
                    .then(function (creditWithdrawForm) {
                        vm.creditWithdrawForm.isCompleted = true;
                        vm.creditWithdrawForm.isSaved = true;
                        var withdrawAmount = vm.creditWithdrawForm.creditToWithdraw,
                            availableCreditBalance = vm.creditBalances.lapwingCredits.availableCreditBalance;
                        vm.creditBalances.lapwingCredits.availableCreditBalance = availableCreditBalance - withdrawAmount;
                    }, function () {
                        vm.creditWithdrawForm.isError = true;
                        console.log("error saving Credit Withdraw.");
                    });
            };

            vm.fetchCreditBalances();
        });
}());