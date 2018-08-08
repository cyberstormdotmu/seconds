/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.checkout')
        .directive('shoPaymentCardExpiry', function () {
            return {
                restrict: 'E',
                templateUrl: 'views/checkout/payment/checkoutPaymentCardExpiryView.html',
                scope: {},
                require: '?ngModel',
                link: function (scope, element, attrs, ngModel) {
                    var i,
                        currentDate = new Date(),
                        currentYear = currentDate.getFullYear(),
                        currentMonth = currentDate.getMonth() + 1;

                    scope.expiry = {};
                    ngModel.$render = function () {
                        if (ngModel.$viewValue) {
                            var split = ngModel.$viewValue.split('/');
                            if (split.length > 0) {
                                scope.expiry.month = split[0];
                            }
                            if (split.length > 1) {
                                scope.expiry.year = split[1];
                            }
                        }
                    };

                    scope.years = function () {
                        var years = [];
                        for (i = currentYear; i < currentYear + 25; i += 1) {
                            years.push(i.toString());
                        }
                        return years;
                    };

                    element.on('focusout', function () {
                        scope.$apply(function () {
                            ngModel.$setTouched();
                        });
                    });

                    scope.$watch('expiry', function (expiry, oldValue) {
                        var haveMonth = !!expiry.month,
                            haveYear = !!expiry.year,
                            validExpiry;

                        if (haveMonth && haveYear) {
                            validExpiry = Number(expiry.year) > currentYear
                                || (Number(expiry.year) === currentYear && Number(expiry.month) >= currentMonth);

                            console.log('validExpiry ' + validExpiry);
                            ngModel.$setValidity('cardExpired', validExpiry);

                            if (validExpiry) {
                                ngModel.$setViewValue(expiry.month + '/' + expiry.year);
                            }
                        } else {
                            ngModel.$setValidity('cardExpired', true);
                        }

                        ngModel.$setValidity('required', haveMonth && haveYear);
                    }, true);
                }
            };
        });
}());