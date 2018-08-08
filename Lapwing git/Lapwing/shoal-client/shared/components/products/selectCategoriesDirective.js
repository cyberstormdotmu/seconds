/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.products')
        .directive('shoSelectCategories', function () {
            return {
                restrict: 'A',
                templateUrl: '../shared/templates/arrayBasedComboBoxTemplate.html',
                scope: {},
                replace: true,
                bindToController: {
                    bind: '='
                },
                controllerAs: 'vm',
                controller: function (shoalCommon_products_ProductsResource) {
                    var that = this;
                    shoalCommon_products_ProductsResource.fetchAllCategories(function (categories) {
                        that.options = categories;
                    });
                }
            };
        });
}());