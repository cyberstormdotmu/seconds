/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.frame')
        .directive('shoAdminFooter', function () {
            return {
                restrict: 'A',
                templateUrl: '../admin/components/frame/shoalFooterView.html',
                replace: true
            };
        });
}());