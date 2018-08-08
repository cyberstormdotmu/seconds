/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalApp.views.account.credits')
        .controller('shoalApp.views.account.credits.CreditApplicationFormController', function (shoalApp_credits_CreditService, $rootScope, $window) {
            var vm = this,
                creditService = shoalApp_credits_CreditService,
                submitSupplierCrefitFacility = function () {
                    vm.errorMessage = undefined;
                    vm.successMessage = undefined;
                    creditService.saveSupplierCreditFacility().
                        then(function (response) {
                            console.log("sucess");
                            vm.successMessage = 'Your application has been sent to your registered email. It should be with you within 5 minutes.';
                        }, function (response) {
                            console.log("fail");
                            vm.errorMessage = 'Your request could not be sent at this time, please try again later.';
                        });
                };
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
            $rootScope.disableSupplierCredits = $window.sessionStorage.getItem('registrationToken');
            vm.fetchCreditBalances();
            vm.submitSupplierCrefitFacility = submitSupplierCrefitFacility;
        });
}());