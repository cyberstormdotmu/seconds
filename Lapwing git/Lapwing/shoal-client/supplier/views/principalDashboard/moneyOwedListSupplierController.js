/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.views.principalDashboard')
        .controller('shoalSupplier.views.principalDashboard.moneyOwedListSupplierController', function (shoalSupplier_buyers_BuyerAdminService) {
            var vm = this,
                buyerService = shoalSupplier_buyers_BuyerAdminService,
                fetchBuyerSupplierDetails = function () {
                    buyerService.fetchVendorSupplierDetails()
                        .then(function (buyers) {
                            vm.supplierDetails = buyers;
                        }, function () {
                            console.log("error reading buyers");
                        });
                };
            fetchBuyerSupplierDetails();
            vm.supplierDetails = [];
        });
}());