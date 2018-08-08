/*global angular */
(function () {
    'use strict';
    angular.module('shoalApp.credits')
        .factory('shoalApp_credits_CreditService', function ($http, $q, ENV, $rootScope) {
            var creditWebServiceUrl = ENV.webServiceUrl + "/credits",
                creditApplicationWebServiceUrl = ENV.webServiceUrl + "/creditapplicationform",
                saveSupplierCreditFacilityWebServiceUrl = ENV.webServiceUrl + "/creditapplicationform/saveSupplierCreditFacility",
                creditWithdrawWebServiceUrl = ENV.webServiceUrl + "/creditwithdrawform",
                getCreditBalances = function () {

                    var defer = $q.defer();

                    $http.get(creditWebServiceUrl)
                        .then(function (response) {
                            var creditBalances = response.data;
                            defer.resolve(Object.freeze(creditBalances));
                        }, function (response) {
                            defer.reject(response);
                        });
                    return defer.promise;
                },
                getCreditApplication = function () {

                    var defer = $q.defer();

                    $http.get(creditApplicationWebServiceUrl)
                        .then(function (response) {
                            var creditApplication = response.data;
                            defer.resolve(Object.freeze(creditApplication));
                        }, function (response) {
                            defer.reject(response);
                        });
                    return defer.promise;
                },
                saveCreditApplication = function (creditApplicationForm) {
                    var defer = $q.defer();

                    $http.post(creditApplicationWebServiceUrl, creditApplicationForm)
                        .then(function (response) {
                            var creditApplication = response.data;
                            defer.resolve(Object.freeze(creditApplication));
                        }, function (response) {
                            defer.reject(response);
                        });
                    return defer.promise;
                },
                saveSupplierCreditFacility = function () {
                    var defer = $q.defer();
                    $http.get(saveSupplierCreditFacilityWebServiceUrl)
                        .then(function (response) {
                            defer.resolve(Object.freeze(response));
                        }, function (response) {
                            defer.reject(response);
                        });
                    return defer.promise;
                },
                saveCreditWithdraw = function (creditWithdrawForm) {

                    var defer = $q.defer();

                    $http.get(creditWithdrawWebServiceUrl + "/" + creditWithdrawForm.creditToWithdraw)
                        .then(function (response) {
                            var creditWithdraw = response.data;
                            defer.resolve(Object.freeze(creditWithdraw));
                        }, function (response) {
                            defer.reject(response);
                        });
                    return defer.promise;
                };

            return {
                getCreditBalances: getCreditBalances,
                getCreditApplication: getCreditApplication,
                saveCreditApplication: saveCreditApplication,
                saveCreditWithdraw: saveCreditWithdraw,
                saveSupplierCreditFacility: saveSupplierCreditFacility
            };
        });
}());