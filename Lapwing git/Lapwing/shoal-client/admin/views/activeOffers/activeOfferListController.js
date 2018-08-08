/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalAdmin.views.activeOffers')
        .controller('shoalAdmin.views.activeOffers.AdminOfferListController', function (shoalAdmin_offers_OfferService, Notification) {
            var vm = this,
                offerService = shoalAdmin_offers_OfferService,
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