/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.views.pages')
        .controller('shoalPublic.views.pages.registrationPrivacypolicy.PrivacyPolicyController', function ($scope, $location, $modalInstance) {
            var vm = this,
                closeModal = function () {
                    $modalInstance.close();
                };

            vm.close = closeModal;
        });
}());
