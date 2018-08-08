/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.address')
        .directive('shoAddress', function () {
            return {
                restrict: 'A',
                templateUrl: '../shared/components/address/addressView.html',
                scope: {},
                bindToController: {
                    address: '='
                },
                controllerAs: 'vm',
                controller: function () {
                    return;
                }
            };
        });
}());