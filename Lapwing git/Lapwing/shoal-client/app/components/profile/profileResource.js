/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.profile')
        .factory('shoalApp_profile_ProfileResource', function ($resource, ENV) {

            var my = {};
            my.profileWebServiceUrl = ENV.webServiceUrl + "/profile";
            return $resource(my.profileWebServiceUrl, null,
                {
                    'save': {method: 'PUT'},
                    'getSessionUser': {url: ENV.webServiceUrl + "/profile", method: 'GET', params: {useSessionUser: true}},
                    'getLoginUser': {url: ENV.webServiceUrl + "/profile", method: 'GET', params: {useSessionUser: false}}
                });
        });
}());
