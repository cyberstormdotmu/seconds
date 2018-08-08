/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.menu')
        .directive('shoNavBar', function ($location) {
            return {
                restrict: 'A',
                templateUrl: '../shared/components/menu/navBarView.html',
                controllerAs: 'my',
                link: function (scope) {
                    scope.my.goto = function (item) {
                        scope.my.selected = item;
                        $location.path(item.link);
                    };
                }
            };
        });
}());