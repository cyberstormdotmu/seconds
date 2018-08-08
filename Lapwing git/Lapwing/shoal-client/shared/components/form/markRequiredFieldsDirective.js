/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.form')
        .directive('shoMarkRequiredFields', function ($timeout) {

            var REQUIRE_ATTRB = 'required',
                REQUIRED_FIELD_ICON_CLASS = 'sho-required-field-icon';

            return {
                restrict: 'A',
                require: '^form',
                link: function (scope, elem, formCtl) {

                    function isEmpty(value) {
                        return !value || value.length === 0;
                    }

                    function findLabel(inputEle) {
                        var parentEle = inputEle.parent();
                        // keep stepping up until form group found or we find the form element.
                        while (!isEmpty(parentEle) && parentEle !== formCtl) {
                            if (parentEle.hasClass('form-group')) {
                                //$log.debug("found form group for " + inputEle.attr('name'));
                                // find first label in form group only - others are ignored
                                return parentEle.find('label');
                            }

                            parentEle = parentEle.parent();
                        }
                    }

                    function appendIconToLabel(labelElement) {
                        var j,
                            childLen = labelElement.children().length,
                            childNgEle,
                            found = false;

                        // is the icon already on the label ?
                        for (j = 0; j < childLen; j += 1) {
                            childNgEle = angular.element(labelElement.children()[j]);
                            if (childNgEle.hasClass(REQUIRED_FIELD_ICON_CLASS)) {
                                found = true;
                               // $log.debug("This label already has required field icon");
                                break;
                            }
                        }

                        if (!found) {
                            labelElement.append('<span class="sho-required-field-icon">&nbsp;&nbsp;&nbsp;*</span>');
                        }
                    }

                    function applyRequireAttributeToAffectedLabels() {
                        var inputEles = elem[0].querySelectorAll("[name]"),
                            i,
                            len = inputEles.length,
                            ngEle,
                            labelEle;

                        for (i = 0; i < len; i += 1) {
                            ngEle = angular.element(inputEles[i]);
                            if (!isEmpty(ngEle.attr(REQUIRE_ATTRB))) {
                                //$log.debug("found required field " + ngEle.attr('name'));
                                labelEle = findLabel(ngEle);
                                if (!labelEle) {
                                    return;
                                }

                                //$log.debug("found label " + labelEle.text());
                                appendIconToLabel(labelEle);
                            }
                        }

                        //$log.debug("marking all required fields " + inputEles.length);
                    }

                    // wait for other directives to finish linking, otherwise
                    // we might miss them.
                    $timeout(applyRequireAttributeToAffectedLabels, 25);
                }
            };
        });
}());
