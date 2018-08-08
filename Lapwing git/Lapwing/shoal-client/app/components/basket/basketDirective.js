/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.basket')
        .directive('shoBasketIcon', function () {
            return {
                restrict: 'A',
                templateUrl: '../app/components/basket/basketIcon.html',
                scope: true,
                replace: true,
                controllerAs: 'vm',
                controller: function ($rootScope, shoalApp_views_basket_BasketViewModal, $window) {
                    var self = this,
                        updateIcon = function (event, newBasket) {
                            console.log('basket icon updated');
                            self.item.count = newBasket.itemCount;
                        };

                    self.item = {};
                    self.item.count = 0;
                    $rootScope.$on('basketUpdated', updateIcon);

                    self.viewBasket = function () {
                        shoalApp_views_basket_BasketViewModal.show();
                    };
                }
            };
        });
}());
