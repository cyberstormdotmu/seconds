/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.template')
            .directive('shoalHeader', function () {
            var headerDirective = {
                restrict: 'A',
                replace: true,
                templateUrl: "/public/components/template/header/header.html",
                scope: {},
                controllerAs: 'vm',
                controller: function ($scope, shoalPublic_views_login_LoginViewModal, shoalPublic_views_contact_ContactViewModal) {
                    var vm = this,
                        loginModal = shoalPublic_views_login_LoginViewModal,
                        contactViewModal = shoalPublic_views_contact_ContactViewModal;

                    vm.openLoginModal = function () {
                        loginModal.show();
                    };
                    vm.openContactModal = function () {
                        contactViewModal.show();
                    };
                }
            };

            return headerDirective;
        });
}());