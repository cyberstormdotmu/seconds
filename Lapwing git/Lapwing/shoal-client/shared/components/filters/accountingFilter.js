/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.filters')
        .filter('accounting', function ($filter) {
            return function (amount) {
                var currencyFiltered = $filter('currency')(amount);

                if (currencyFiltered && currencyFiltered.charAt(0) === "-") {
                    return currencyFiltered.replace("-", "(") + ")";
                }
                return currencyFiltered;
            };
        });
}());