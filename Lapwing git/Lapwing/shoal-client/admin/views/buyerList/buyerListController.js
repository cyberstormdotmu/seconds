/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalAdmin.views.buyerList')
        .controller('shoalAdmin.views.buyerList.BuyerListController', function (shoalAdmin_buyers_BuyerAdminService, Notification) {
            var vm = this,
                buyerService = shoalAdmin_buyers_BuyerAdminService,
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