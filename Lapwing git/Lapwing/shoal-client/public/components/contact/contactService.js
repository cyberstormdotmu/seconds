/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.contact')
        .factory('shoalPublic_contact_ContactService', function ($q, $http, ENV) {

            var contactWebServiceUrl = ENV.webServiceUrl + "/contact",
                submitContactRequest = function (contactRequest) {
                    var defer = $q.defer();
                    console.log(contactRequest);
                    $http.post(contactWebServiceUrl, contactRequest)
                        .then(function () {
                            defer.resolve();
                        }, function () {
                            defer.reject();
                        });

                    return defer.promise;
                };

            return {
                submitContactRequest: submitContactRequest
            };
        });
}());