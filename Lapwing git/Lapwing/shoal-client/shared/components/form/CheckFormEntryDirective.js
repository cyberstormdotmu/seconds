/*global angular */
(function () {
    'use strict';
    angular.module('shoalCommon.form')
        .directive('shoCheckFormEntry', function () {
            return {
                restrict: 'A',
                templateUrl: '../shared/components/form/checkFormEntryTemplate.html',
                scope: {},
                bindToController: {
                    name: '='
                },
                controllerAs: 'vm',
                controller: function () {
                    return;
                },
                transclude: true
            };
        });

}());
