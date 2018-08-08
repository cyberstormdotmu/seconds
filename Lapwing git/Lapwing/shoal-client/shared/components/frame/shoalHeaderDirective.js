/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.frame')
        .directive('shoHeader', function () {
            return {
                restrict: 'A',
                templateUrl: '../shared/components/frame/shoalHeaderView.html',
                replace: true,
                controllerAs: 'vm',
                controller: function ($scope, shoalApp_basket_BasketService, shoalApp_profile_ProfileService) {
                    var vm = this,
                        basketService = shoalApp_basket_BasketService,
                        buyerProfile = shoalApp_profile_ProfileService;
                    basketService.synchroniseBasket();
                    vm.fullName = '';

                    buyerProfile.fetchBuyerProfile().then(function (profile) {
                        vm.fullName = profile.form.contact.firstName + ' ' + profile.form.contact.surname;
                    });
                }
            };
        });
}());