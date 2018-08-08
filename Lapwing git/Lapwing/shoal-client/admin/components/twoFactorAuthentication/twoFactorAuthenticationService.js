/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.twoFactorAuthentication')
        .factory('shoalAdmin_twoFactorAuthentication_TwoFactorAuthenticationService', function ($q, $http, ENV) {
            var initiateTwoFactorAuthenticationUrl = ENV.webServiceUrl + "/resetpassword/send",
                confirmTwoFactorAuthenticationUrl = ENV.webServiceUrl + "/resetpassword/twoFactorAuthentication",
                initiateTwoFactorAuthentication = function (username) {
                    var defer = $q.defer();
                    $http({
                        method: 'POST',
                        url: initiateTwoFactorAuthenticationUrl
                    }).then(function () {
                        defer.resolve();
                    }, function (response) {
                        defer.reject({
                            reason: response.data.message
                        });
                    });
                    return defer.promise;
                },
                confirmTwoFactorAuthentication = function (verificationCode) {
                    var defer = $q.defer();
                    $http({
                        method: 'POST',
                        url: confirmTwoFactorAuthenticationUrl,
                        params: {'verificationCode': verificationCode}
                    }).then(function () {
                        defer.resolve();
                    }, function (response) {
                        defer.reject({
                            reason: response.data.message
                        });
                    });

                    return defer.promise;
                };

            return {
                initiateTwoFactorAuthentication: initiateTwoFactorAuthentication,
                confirmTwoFactorAuthentication: confirmTwoFactorAuthentication
            };
        });
}());