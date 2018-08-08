/*global angular */
(function () {
    'use strict';
    var REGISTRATION_NUMBER_REGEXP = /^[1-9]+(?:\d{3,7})|(?:[A-Za-z]{2}\d{6})$/;

    angular.module('shoalCommon.validator')
        .directive('shoCompanyReg', function () {
            return {
                require: 'ngModel',
                link: function (scope, elm, attrs, ctrl) {
                    ctrl.$validators.companyReg = function (modelValue, viewValue) {
                        if (ctrl.$isEmpty(modelValue)) {
                            // consider empty models to be valid
                            return true;
                        }
                        return (REGISTRATION_NUMBER_REGEXP.test(viewValue));
                    };
                }
            };
        });
}());
