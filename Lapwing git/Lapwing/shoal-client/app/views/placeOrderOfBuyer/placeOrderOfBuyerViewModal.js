/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.placeOrderOfBuyer').
        factory('shoalApp_views_placeOrderOfBuyer_PlaceOrderOfBuyerViewModal', function ($uibModal) {
            var modalConfig = {
                    templateUrl: 'views/placeOrderOfBuyer/placeOrderOfBuyerView.html',
                    controller: 'shoalApp.views.placeOrderOfBuyer.PlaceOrderOfBuyerController as vm',
                    size: 'lg'
                };

            return {
                show: function () {
                    $uibModal.open(modalConfig);
                }
            };
        });
}());
