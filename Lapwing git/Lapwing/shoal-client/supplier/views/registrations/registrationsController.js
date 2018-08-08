/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalSupplier.views.registrations')
        .controller('shoalSupplier.views.registrations.RegistrationsController', function (newRegistrations) {
            var vm = this;
            vm.newRegistrations = newRegistrations;
        });
}());