/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalSupplier.views.activeOffers')
        .controller('shoalSupplier.views.activeOffers.SupplierOfferListController', function (shoalSupplier_offers_OfferService, Notification) {
            var vm = this,
                offerService = shoalSupplier_offers_OfferService,
                fetchOffers = function () {
                    offerService.fetchOffers()
                        .then(function (offers) {
                            vm.offers = offers;
                        }, function () {
                            console.log("error reading offers");
                        });
                },
                refresh = function () {
                    fetchOffers();
                };

            vm.refresh = refresh;
            vm.offers = [];

            refresh();
        });
}());