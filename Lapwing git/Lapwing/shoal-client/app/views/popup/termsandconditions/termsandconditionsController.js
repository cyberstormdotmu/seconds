/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.popup')
        .controller('shoalApp.views.popup.TermsandconditionsController', function ($scope, $location, $modalInstance) {
            var vm = this,
                closeModal = function () {
                    $modalInstance.close();
                };

            vm.close = closeModal;
        });
}());
