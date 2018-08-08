/*global angular, console, document */
(function () {
    'use strict';
    angular.module('shoalCommon.pageAnchor')
        .directive('shoPageAnchor', function (smoothScroll) {
            return {
                restrict: 'A',
                link: function (scope, element, attrs) {
                    var targetId = attrs.shoPageAnchor,
                        scrollTo = function () {
                            var targetElement = document.getElementById(targetId);
                            smoothScroll(targetElement);
                        };

                    element.bind('click', scrollTo);
                }
            };
        });
}());




