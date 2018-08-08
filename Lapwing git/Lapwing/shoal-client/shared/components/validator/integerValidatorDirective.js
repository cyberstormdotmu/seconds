/*global angular */
(function () {
    'use strict';
    var INTEGER_REGEXP = /^\-?\d+$/;

    angular.module('shoalCommon.validator')
        // REW this name needs changing to shoInteger to avoid future issues
        .directive('integer', function () {
            return {
                require: 'ngModel',
                link: function (scope, elm, attrs, ctrl) {
                    ctrl.$validators.integer = function (modelValue, viewValue) {
                        if (ctrl.$isEmpty(modelValue)) {
                            // consider empty models to be valid
                            return true;
                        }

                        return (INTEGER_REGEXP.test(viewValue));
                    };
                }
            };
        });
}());
