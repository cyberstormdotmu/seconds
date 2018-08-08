/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.registration')
        .factory('shoalPublic_registration_RegistrationResource', function ($resource, ENV) {
            var my = {};
            my.registrationWebServiceUrl = ENV.webServiceUrl + "/registration";
            return $resource(my.registrationWebServiceUrl, null,
                {
                    'save': {method: 'POST'}
                });
        });
}());
