/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.registration')
        .factory('shoalPublic_registration_RegistrationService', function ($q, $http, ENV, shoalPublic_registration_RegistrationResource) {
            var my = {},
                verifyCaptchaUrl = ENV.webServiceUrl + "/registration/verifyCaptcha",
                confirmTwoFactorAuthenticationUrl = ENV.webServiceUrl + "/registrationconfirm/registrationOtpconfirm",
                registrationConfirmServiceUrl = ENV.webServiceUrl + "/registrationconfirm/",
                verifyRegistrationForBuyers = function (registrationToken) {
                    var defer = $q.defer();
                    $http.get(registrationConfirmServiceUrl + registrationToken)
                        .then(function (response) {
                            defer.resolve(Object.freeze(response));
                        }, function (response) {
                            defer.reject(response);
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
            my.verifyCaptcha = function (key) {
                var defer = $q.defer();
                $http({
                    method: 'POST',
                    url: verifyCaptchaUrl,
                    params: {'key': key }
                }).then(function (response) {
                    var reason = response.data;
                    defer.resolve(Object.freeze(reason));
                }, function (response) {
                    defer.reject(response.data.message);
                });
                return defer.promise;
            };
            my.registrationResource = shoalPublic_registration_RegistrationResource;
            my.saveRegistrationForm = function (country) {
                var that = this,
                    defer = $q.defer();
                that.buyer.westcoastAccountNumber = that.buyer.westcoastAccountNumber === '' ? null : that.buyer.westcoastAccountNumber;
                that.buyer.lapwingAccountNumber = that.buyer.lapwingAccountNumber === '' ? null : that.buyer.lapwingAccountNumber;
                that.buyer.buyerReferralCode = that.buyer.buyerReferralCode === '' ? null : that.buyer.buyerReferralCode;
                if (that.organisation.mobileNumber.indexOf('07') === 0) {
                    that.organisation.mobileNumber = that.organisation.mobileNumber.indexOf('07') === 0 ? that.organisation.mobileNumber.replace('07', '00447') : that.organisation.mobileNumber;
                    my.registrationResource.save(that, function () {
                        that.isSaved = true;
                        that.isError = false;
                        defer.resolve();
                    }, function (response) {
                        that.isError = true;
                        that.errorMessage = response.data.message;
                        defer.reject();
                    });
                } else {
                    that.isError = true;
                    that.errorMessage = "Mobile number should start with '07'.";
                    defer.reject();
                }
                return defer.promise;
            };
            my.buildRegistrationForm = function () {
                var form = {
                    buyer: {
                        firstName: '',
                        surname: '',
                        emailAddress: '',
                        password: '',
                        lapwingAccountNumber: '',
                        westcoastAccountNumber: '',
                        buyerReferralCode: '',
                        appliedFor: ''
                    },
                    organisation: {
                        name: '',
                        registrationNumber: '',
                        mobileNumber: ''
                    },
                    siteDateKey: '',
                    save: my.saveRegistrationForm,
                    verifyCaptcha: my.verifyCaptcha,
                    isSaved: false,
                    isError: false,
                    errorMessage: ''
                };
                console.log('building registration form');
                return Object.seal(form);
            };

            return Object.create({}, {
                buildRegistrationForm: {
                    value: function () {
                        return my.buildRegistrationForm();
                    }
                },
                verifyRegistrationForBuyers: {
                    value: verifyRegistrationForBuyers
                },
                confirmTwoFactorAuthentication: {
                    value: confirmTwoFactorAuthentication
                }
            });
        });
}());