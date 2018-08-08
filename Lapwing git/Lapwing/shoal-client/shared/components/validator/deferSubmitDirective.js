/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalCommon.validator')
        .directive('shoValidateOnClick', function ($parse) {
            return {
                restrict: 'A',
                require: '^form',
                link: function (scope, elem, attr, formCtl) {
                    var formController,
                        action,
                        callback;

                    if (!formCtl) {
                        console.log("form not found - aborting shoValidateOnSubmit link");
                        return;
                    }
                    formController = formCtl;
                    action = $parse(attr.shoValidateOnClick);

                    console.log("action= " + attr.shoValidateOnClick);
                    if (!action) {
                        console.log("no action, not registering click");
                        return;
                    }

                    elem.bind('click', function (event) {
                        console.log("click");
                        formController.$setSubmitted();
                        scope.$broadcast('submitValidate', formController);
                        scope.$apply();

                        if (formController.$invalid) {
                            console.log("form is invalid - block action");
                            return false;
                        }

                        callback = function () {
                            console.log("trigger event");
                            formController.$submitted = false;
                            action(scope, {$event: event});
                        };

                        scope.$apply(callback);
                    });
                }
            };
        });
}());
