/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalSupplier.views.buyerList')
        .controller('shoalSupplier.views.buyerList.BuyerListController', function (shoalSupplier_buyers_BuyerAdminService, Notification) {
            var vm = this,
                buyerService = shoalSupplier_buyers_BuyerAdminService,
                fetchBuyers = function () {
                    buyerService.fetchBuyers()
                        .then(function (buyers) {
                            vm.buyers = buyers;
                        }, function () {
                            console.log("error reading buyers");
                        });
                },
                refresh = function () {
                    fetchBuyers();
                };

            vm.refresh = refresh;
            vm.buyers = [];

            refresh();
        });
}());