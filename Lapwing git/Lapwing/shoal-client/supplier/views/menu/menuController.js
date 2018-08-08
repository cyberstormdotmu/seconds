/*global angular */
(function () {
    'use strict';
    angular.module('shoalSupplier.views.menu')
        .controller('shoalSupplier.views.menu.NavBarController', function () {
            this.menuItems = [
                {
                    name: 'Registrations',
                    state: 'registrations'
                }
            ];
            this.rightMenuItems = [
                {
                    name: 'Logout',
                    state: 'logout'
                }
            ];
        })
        .controller('shoalSupplier.views.menu.HeaderMenuController', function () {
            this.menuItems = [];
        });
}());