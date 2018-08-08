/*global angular */
(function () {
    'use strict';
    var POSTCODE_REGEXP = /^[a-z]{1,2}[0-9][a-z0-9]?\s?[0-9][a-z]{2}$/i;

    angular.module('shoalCommon.validator')
        .directive('shoPostcode', function () {
            return {
                require: 'ngModel',
                link: function (scope, elm, attrs, ctrl) {
                    ctrl.$validators.postcode = function (modelValue, viewValue) {
                        if (ctrl.$isEmpty(modelValue)) {
                            // consider empty models to be valid
                            return true;
                        }

                        return (POSTCODE_REGEXP.test(viewValue));
                    };
                }
            };
        });
}());