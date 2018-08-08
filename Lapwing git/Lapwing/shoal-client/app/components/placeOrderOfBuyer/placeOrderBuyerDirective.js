/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.placeOrderBuyer')
        .directive('shoPlaceorderIcon', function ($rootScope, $window) {
            return {
                restrict: 'A',
                templateUrl: '../app/components/placeOrderOfBuyer/placeOrderBuyerIcon.html',
                scope: true,
                replace: true,
                controllerAs: 'vm',
                controller: function (shoalApp_views_placeOrderOfBuyer_PlaceOrderOfBuyerViewModal) {
                    var vm = this;
                    $rootScope.isLoginUserAdmin = $window.sessionStorage.getItem("isLoginUserAdmin");
                    vm.viewPlaceOrderBuyer = function () {
                        shoalApp_views_placeOrderOfBuyer_PlaceOrderOfBuyerViewModal.show();
                    };
                }
            };
        });
}());