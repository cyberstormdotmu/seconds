/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalCommon.validator')
        .directive('shoMinDate', function () {

            return {
                require: 'ngModel',
                link: function (scope, elm, attrs, ctrl) {

                    var minDateBinding = attrs.shoMinDate,
                        minDateValue;

                    scope.$watch(minDateBinding, function (newValue, oldValue) {
                        console.log("minDate binding has changed");
                        minDateValue = newValue;
                    });

                    ctrl.$validators.minDate = function (modelValue, viewValue) {
                        if (ctrl.$isEmpty(viewValue)) {
                            // consider empty models to be valid
                            return true;
                        }
                        if (!minDateBinding) {
                            // consider no min date provided to be valid
                            return true;
                        }

                        console.log("minDate validate : " + modelValue + " > " + minDateValue + " = " + (modelValue >= minDateValue));
                        return modelValue >= minDateValue;
                    };
                }
            };
        });
}());
