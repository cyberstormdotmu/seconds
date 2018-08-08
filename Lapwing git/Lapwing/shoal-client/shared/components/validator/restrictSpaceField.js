/*global angular */
(function () {
    'use strict';
    var SPACE_NOT_ALLOW_REGEXP = /^\S*$/; // a string consisting only of non-whitespaces

    angular.module('shoalCommon.validator')
        .directive('shoNotAllowedSpaceField', function () {
            return {
                require: 'ngModel',
                link: function (scope, elm, attrs, ctrl) {
                    ctrl.$validators.spacenotallow = function (modelValue, viewValue) {
                        if (ctrl.$isEmpty(modelValue)) {
                            // consider empty models to be valid
                            return true;
                        }
                        return (SPACE_NOT_ALLOW_REGEXP.test(viewValue));
                    };
                }
            };
        });
}());
