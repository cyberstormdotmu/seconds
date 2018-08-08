/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.views.buyerList')
        .controller('shoalSupplier.views.buyerList.EditVendorCreditController', function ($scope, $uibModalInstance, vendorCreditWithBuyerId, shoalSupplier_buyers_BuyerAdminService) {
            var vm = this,
                buyerService = shoalSupplier_buyers_BuyerAdminService,
                ok = function () {
                    $uibModalInstance.close();
                },
                cancel = function () {
                    $uibModalInstance.dismiss();
                },
                changeVendorCreditSave = function () {
                    vendorCreditWithBuyerId.availableCredits = vendorCreditWithBuyerId.totalCredits - vendorCreditWithBuyerId.usedCredits;
                    buyerService.updateVendorCredit(vendorCreditWithBuyerId)
                        .then(function () {
                            vm.cancel();
                        }, function () {
                            console.log("error in update vendor credit");
                        });
                };
            Object.defineProperty(vm, 'vendorCreditWithBuyerId', {
                get: function () {
                    return vendorCreditWithBuyerId;
                }
            });
            vm.changeVendorCreditSave = changeVendorCreditSave;
            vm.ok = ok;
            vm.cancel = cancel;
        });
}());