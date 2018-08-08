/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalCommon.validator')
        .directive('shoHighlightErrors', function ($timeout, $parse) {

            var HAS_ERROR_ATTRB = 'has-error',
                NAME_ATTRB = 'name';

            return {
                restrict: 'A',
                require: '^form',
                link: function (scope, elem, attr, formCtl) {
                    var inputEle,
                        inputNgEle,
                        inputName;

                    if (!formCtl) {
                        return;
                    }
                    //$log.debug('post-linking shoHighlightErrors');

                    function parseInputName(name) {
                        var expr = name.replace('{{', '').replace('}}', '');
                        if (name === expr) {
                            // no evaluation required
                            return name;
                        }
                        //$log.debug("parsing input name " + name + " into " + expr);
                        return $parse(expr)(scope);
                    }


                    function isModelInError() {
                        elem.toggleClass(HAS_ERROR_ATTRB, formCtl[inputName].$invalid &&
                            (formCtl[inputName].$touched || formCtl.$submitted));
                    }

                    function isEmpty(value) {
                        return !value || value.length === 0;
                    }

                    function setUpFormFieldWatcher(attempts) {
                        if (attempts > 10) {
                            //$log.debug("exceeded max attempts to find the form entry - error highlighting disabled on this element");
                            // safety feature so we don't end up in an infinite recursive loop.
                            return;
                        }
                        //$log.debug("testing shoHighlightErrors timeout - must be after linking phase!!");

                        inputEle = elem[0].querySelector("[" + NAME_ATTRB + "]");

                        if (isEmpty(inputEle)) {
                            //$log.debug("no element found with attribute name, so quitting right here.");
                            return;
                        }

                        inputNgEle = angular.element(inputEle);
                        inputName = parseInputName(inputNgEle.attr(NAME_ATTRB));

                        if (isEmpty(formCtl[inputName])) {
                            // if we have found the element but it is not included in the form yet, then
                            // we need to quit this attempt and reschedule another try.
                            //$log.debug("form field " + inputName + " not found in attempt " + attempts + " - try again in next cycle");
                            $timeout(setUpFormFieldWatcher.bind(this, attempts + 1), 0);
                            return;
                        }

                        // trigger an update whenever the user clicks away from the input.
                        inputNgEle.bind('blur', isModelInError);

                        // trigger a check whenever the input validity changes.
                        scope.$watch(function () {
                            return formCtl[inputName].$valid;
                        }, isModelInError);
                    }

                    // we must initialise watch AFTER all linking complete because child directives are not
                    // guaranteed to have completed in the parent post-link phase.
                    $timeout(setUpFormFieldWatcher.bind(this, 0), 0);

                    // trigger an update when there is a form submission on the same form.
                    scope.$on('submitValidate', function (event, controller) {
                        if (controller === formCtl) {
                            //$log.debug("submit has triggered an error highlight update");

                            $timeout(isModelInError);
                        }
                    });

                    scope.$on('resetValidation', function () {
                        $timeout(function () {
                            elem.removeClass(HAS_ERROR_ATTRB);
                        }, 0, false);
                    });
                }
            };
        });
}());
