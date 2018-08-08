/*global angular */
(function () {
    'use strict';

    angular.module('shoalCommon.form')
        .directive('shoPreventDoubleClick', function ($timeout) {
            var delay = 2000,   // min milliseconds between clicks
                DISABLED = "disabled";

            return {
                restrict: 'A',
                priority: -1,   // cause our postLink function to execute before native `ngClick`'s
                                // ensuring that we can stop the propagation of the 'click' event
                                // before it reaches `ngClick`'s listener
                link: function (scope, elem) {
                    var disabled = false;

                    function handleClick(evt) {
                        if (disabled) {
                            evt.preventDefault();
                            evt.stopImmediatePropagation();
                        } else {
                            disabled = true;
                            elem.attr(DISABLED, true);
                            $timeout(function () {
                                disabled = false;
                                elem.attr(DISABLED, false);
                            }, delay, false);
                        }
                    }

                    scope.$on('$destroy', function () {
                        elem.off('click', handleClick);
                    });
                    elem.on('click', handleClick);
                }
            };
        });
}());