/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.views.login').
        factory('shoalPublic_views_login_LoginViewModal', function ($uibModal) {
            var modalConfig = {
                templateUrl: 'views/login/loginView.html',
                controller: 'shoalPublic.views.login.LoginController as vm',
                size: 'lg'
            };

            return {
                show: function () {
                    $uibModal.open(modalConfig);
                }
            };
        });
}());
