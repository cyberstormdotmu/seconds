/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.vendors')
        .directive('shoSelectVendors', function ($rootScope) {
            return {
                restrict: 'A',
                templateUrl: '../shared/templates/arrayBasedComboBoxTemplate.html',
                scope: {},
                replace: true,
                bindToController: {
                    bind: '='
                },
                controllerAs: 'vm',
                controller: function (shoalCommon_vendors_VendorsResource) {
                    var that = this;
                    shoalCommon_vendors_VendorsResource.fetchAllVendors(function (vendors) {
                        that.options = vendors;
                    });
                    $rootScope.$on('newVendorAdd', function (event, vendor) {
                        that.options.push(vendor.name);
                    });
                }
            };
        });
}());