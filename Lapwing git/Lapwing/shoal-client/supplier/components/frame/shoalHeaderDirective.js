/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.frame')
        .directive('shoSupplierHeader', function () {
            return {
                restrict: 'A',
                templateUrl: '../supplier/components/frame/shoalHeaderView.html',
                replace: true
            };
        });
}());