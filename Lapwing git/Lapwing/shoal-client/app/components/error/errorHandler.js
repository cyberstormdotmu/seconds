/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.error')
        .factory('shoalApp_error_ErrorHandler',
            function ($location) {
                var isServerErrorOrTimeout = function (rejection) {
                        return rejection.status === 500 || rejection.status === -1;
                    },
                    redirectToErrorPageIfServerReportsError = function (rejection) {

                        if (isServerErrorOrTimeout(rejection)) {
                            console.log("error reason " + JSON.stringify(rejection));
                            $location.path("/error");
                            return true;
                        }

                        console.log("error was not a server error or timeout, status given : " + rejection.status);
                        return false;

                    };

                return {
                    handleServerError: redirectToErrorPageIfServerReportsError
                };
            });
}());
