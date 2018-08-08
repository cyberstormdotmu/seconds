/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.vatRate')
        .directive('shoSelectVatRate', function () {
            return {
                restrict: 'A',
                template: '<select class="form-control" ng-model="vm.bind" ng-init="vm.selected = selected"' +
                    ' ng-options="option.code as (option.code + \' [\' + option.rate + \'%]\') for option in vm.options" required>' +
                    '</select>',
                scope: {},
                replace: true,
                bindToController: {
                    bind: '='
                },
                controllerAs: 'vm',
                controller: function (shoalCommon_vatRate_VatRateResource) {
                    var that = this;
                    shoalCommon_vatRate_VatRateResource.fetchAllVatRates(function (rates) {
                        that.options = rates;
                        // if we don't do this then angular default it to zero (array index)
                        // this causes require validation to pass -> user is not forced to select a value.
                        ////that.bind = null;
                    });
                }
            };
        });
}());