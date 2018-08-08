/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.frame')
        .directive('shoSupplierFooter', function () {
            return {
                restrict: 'A',
                templateUrl: '../supplier/components/frame/shoalFooterView.html',
                replace: true
            };
        });
}());