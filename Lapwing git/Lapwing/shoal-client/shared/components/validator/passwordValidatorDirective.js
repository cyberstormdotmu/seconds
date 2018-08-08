/*global angular */
(function () {
    'use strict';
    /*jslint regexp: true */
    var PASSWORD_REGEXP = /^(?=.*[A-Z])(?=.*[^A-Za-z0-9])(?=.*[0-9])(?=.*[a-z]).{8,20}$/;

    angular.module('shoalCommon.validator')
        .directive('shoPassword', function () {
            return {
                require: 'ngModel',
                link: function (scope, elm, attrs, ctrl) {
                    ctrl.$validators.password = function (modelValue, viewValue) {
                        if (ctrl.$isEmpty(modelValue)) {
                            // consider empty models to be valid
                            return true;
                        }

                        return (PASSWORD_REGEXP.test(viewValue));
                    };
                }
            };
        });
}());
