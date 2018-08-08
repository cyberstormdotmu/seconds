/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.views.principalDashboard')
        .controller('shoalAdmin.views.principalDashboard.moneyOwedListSupplierController', function ($uibModal, $scope, $stateParams, shoalAdmin_buyers_BuyerAdminService) {
            var vm = this,
                buyerService = shoalAdmin_buyers_BuyerAdminService,
                fetchBuyerSupplierDetails = function () {
                    buyerService.fetchBuyerSupplierDetails()
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