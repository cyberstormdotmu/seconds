/*global angular */
(function () {
    'use strict';

    angular.module('shoalApp.orderverification')
        .factory('shoalApp_orderverification_OrderVerificationService', function ($http, $q, ENV) {
            var initiatePasswordResetUrl = ENV.webServiceUrl + "/resetpassword/send",
                initiateOTPConfirmUrl = ENV.webServiceUrl + "/resetpassword",

                initiatePasswordReset = function () {
                    var defer = $q.defer();
                    $http({
                        method: 'POST',
                        url: initiatePasswordResetUrl
                    }).then(function () {
                        defer.resolve();
                    }, function (response) {
                        defer.reject();
                    });

                    return defer.promise;
                },

                initiateOTPConfirm = function (verificationCode) {
                    var defer = $q.defer();
                    $http({
                        method: 'POST',
                        url: initiateOTPConfirmUrl + "/" + verificationCode
                    }).then(function () {
                        defer.resolve();
                    }, function (response) {
                        defer.reject();
                    });

                    return defer.promise;
                };

            return {
                initiatePasswordReset: initiatePasswordReset,
                initiateOTPConfirm: initiateOTPConfirm
            };
        });
}());