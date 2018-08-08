/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin')
        .config(function configureHttpProvider($httpProvider) {
            console.log("configuring http provider");
            //$httpProvider.useLegacyPromiseExtensions(false);
            $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
            $httpProvider.interceptors.push(function timeoutInterceptor() {
                return {
                    'request': function (config) {
                        config.timeout = 30000;
                        return config;
                    }
                };
            });
            $httpProvider.interceptors.push(function configureResponseErrorInterceptor($q, shoalCommon_security_AuthFailHandler) {
                return {
                    'responseError': function (rejection) {
                        if (rejection.config.url !== '/ws/login' && rejection.config.url !== '/ws/logout') {

                            if (shoalCommon_security_AuthFailHandler.handleAuthFailure(rejection)) {
                                console.log("response has authentication error - intercepted");
                                $q.reject(rejection);
                                return;
                            }
                        }

                        // response error wasn't handled by anything
                        //console.log("response error wasn't handled by anything : " + JSON.stringify(rejection));

                        return $q.reject(rejection);
                    }
                };
            });
        });
}());
