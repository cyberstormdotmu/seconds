/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.frame')
        .directive('shoFooter', function () {
            return {
                restrict: 'A',
                templateUrl: '../shared/components/frame/shoalFooterView.html',
                replace: true
            };
        });
}());