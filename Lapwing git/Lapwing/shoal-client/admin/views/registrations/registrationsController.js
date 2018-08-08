/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalAdmin.views.registrations')
        .controller('shoalAdmin.views.registrations.RegistrationsController', function (newRegistrations) {
            var vm = this;
            console.log(vm.newRegistrations);
            vm.newRegistrations = newRegistrations;
            console.log(vm.newRegistrations);
        });
}());