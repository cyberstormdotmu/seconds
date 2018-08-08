/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.views.buyerList')
        .controller('shoalSupplier.views.buyerList.BuyerListDetailsController', function (shoalSupplier_orders_OrderService, $uibModal, $scope, $stateParams, shoalSupplier_buyers_BuyerAdminService) {
            var vm = this,
                id = $stateParams.id,
                vendorCreditWithBuyerId = {},
                orderAdminService = shoalSupplier_orders_OrderService,
                buyerService = shoalSupplier_buyers_BuyerAdminService,
                fetchBuyersDetails = function () {
                    buyerService.fetchBuyerDetails(id)
                        .then(function (buyerDetails) {
                            vm.buyerDelails = buyerDetails;
                            orderAdminService.getUnconfirmedOrdersOfBuyer(id)
                                .then(function (orders) {
                                    vm.unconfirmedOrders = orders;
                                }, function () {
                                    console.log("error reading unconfirmed orders");
                                });
                            orderAdminService.getConfirmedOrdersOfBuyer(id)
                                .then(function (orders) {
                                    vm.confirmedOrders = orders;
                                }, function () {
                                    console.log("error reading confirmed orders");
                                });
                            orderAdminService.getCancelledOrdersOfBuyer(id)
                                .then(function (orders) {
                                    vm.cancelledOrders = orders;
                                }, function () {
                                    console.log("error reading cancelled orders");
                                });
                        }, function (failure) {
                            console.log("error reading buyers");
                        });
                },
                editVendorCredit = function (vendorCredit) {
                    console.log(vendorCredit);
                    vendorCreditWithBuyerId = Object.assign({ id: id, usedCredits: vendorCredit.totalCredits - vendorCredit.availableCredits}, vendorCredit);
                    var modalInstance = $uibModal.open({
                        templateUrl: 'views/buyerList/VendorCredit/vendorCreditView.html',
                        controller: 'shoalAdmin.views.buyerList.EditVendorCreditController as vm',
                        size: 'lg',
                        resolve: {
                            vendorCreditWithBuyerId: vendorCreditWithBuyerId
                        }
                    });
                    modalInstance.result.then(function () {
                        fetchBuyersDetails();
                    }, function () {
                        fetchBuyersDetails();
                    });
                };
            fetchBuyersDetails();
            vm.editVendorCredit = editVendorCredit;
        });
}());