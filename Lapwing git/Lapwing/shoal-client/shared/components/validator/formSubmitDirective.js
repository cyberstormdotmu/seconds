/*global angular */
(function () {
    'use strict';

    angular.module('shoalCommon.validator')
        .directive('shoFormSubmit', function ($parse) {
            return {
                restrict: 'A',
                controller: function () {
                    this.hasAttemptedSubmit = false;

                    this.submitAttempted = function () {
                        this.hasAttemptedSubmit = true;
                    };

                    this.hasError = function (fieldModelController) {
                        return fieldModelController.$invalid &&
                            (fieldModelController.$touched || this.hasAttemptedSubmit);
                    };
                },
                link: function (scope, element, attributes, controller) {
                    var modelAccessor = $parse(attributes.name);
                    angular.extend(modelAccessor(scope), controller);
                }
            };
        });
}());
