/*global angular */
(function () {
    'use strict';

    angular.module('shoalCommon.validator')
        .directive('shoPasswordMatch', function ($parse) {
            return {
                require: 'ngModel',
                link: function (scope, elm, attrs, ctrl) {
                    var passwordAccessor = $parse(attrs.shoPasswordMatch);

                    ctrl.$validators.passwordMatch = function (modelValue, viewValue) {
                        if (ctrl.$isEmpty(modelValue)) {
                            // consider empty models to be valid
                            return true;
                        }

                        var password = passwordAccessor(scope);
                        return password === viewValue;
                    };
                }
            };
        });
}());
