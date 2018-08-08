/*global angular, Stripe */
(function () {
    'use strict';
    angular.module('shoalApp.payment', ['shoalApp.classes'])
        .config(function (ENV) {
            Stripe.setPublishableKey(ENV.stripeApiKey);
        });
}());
