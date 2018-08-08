/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.frame')
        .directive('shoAdminHeader', function () {
            return {
                restrict: 'A',
                templateUrl: '../admin/components/frame/shoalHeaderView.html',
                replace: true
            };
        });
}());