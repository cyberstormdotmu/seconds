/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.passwordReset')
        .factory('shoalPublic_passwordReset_PasswordResetService', function ($q, $http, ENV) {
            var initiatePasswordResetUrl = ENV.webServiceUrl + "/resetpassword/initiate",
                confirmPasswordResetUrl = ENV.webServiceUrl + "/resetpassword",

                initiatePasswordReset = function (username) {
                    var defer = $q.defer();

                    $http({
                        method: 'POST',
                        url: initiatePasswordResetUrl,
                        params: {'username': username}
                    }).then(function () {
                        defer.resolve();
                    }, function (response) {
                        defer.reject({
                            reason: response.data.message
                        });
                    });

                    return defer.promise;
                },
                confirmPasswordReset = function (newPassword, verificationCode) {
                    var defer = $q.defer();

                    $http({
                        method: 'POST',
                        url: confirmPasswordResetUrl,
                        data: {
                            newPassword: newPassword,
                            verificationCode: verificationCode
                        }
                    }).then(function () {
                        defer.resolve();
                    }, function (response) {
                        defer.reject(response.data);
                    });

                    return defer.promise;
                };

            return {
                initiatePasswordReset: initiatePasswordReset,
                confirmPasswordReset: confirmPasswordReset
            };
        });
}());