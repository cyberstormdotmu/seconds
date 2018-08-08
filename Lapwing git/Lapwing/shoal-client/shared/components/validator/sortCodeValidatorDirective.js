/*global angular,console */
(function () {
    'use strict';
    var SORTCODE_REGEXP = /^\d{2}-\d{2}-\d{2}$/;

    angular.module('shoalCommon.validator')
        .directive('shoSortcode', function () {
            return {
                require: 'ngModel',
                link: function (scope, elm, attrs, ctrl) {
                    ctrl.$validators.sortcode = function (modelValue, viewValue) {
                        if (ctrl.$isEmpty(modelValue)) {
                            // consider empty models to be valid
                            return true;
                        }
                        return (SORTCODE_REGEXP.test(viewValue));
                    };
                }
            };
        });
}());