/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.views.home')
        .controller('shoalPublic.views.home.HomeController', function ($state, shoalPublic_views_login_LoginViewModal, shoalPublic_views_registration_RegistrationViewModal) {
            var vm = this,
                loginModal = shoalPublic_views_login_LoginViewModal,
                registrationModal = shoalPublic_views_registration_RegistrationViewModal,
                openLoginModal = function () {
                    loginModal.show();
                },
                openRegistrationModal = function () {
                    registrationModal.show();
                };

            vm.openLoginModal = openLoginModal;
            vm.openRegistrationModal = openRegistrationModal;
        });
}());