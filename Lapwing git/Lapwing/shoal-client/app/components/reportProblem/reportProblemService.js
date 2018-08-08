/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.reportProblem')
        .factory('shoalApp_reportProblem_ReportProblemService', function ($q, $http, ENV) {

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