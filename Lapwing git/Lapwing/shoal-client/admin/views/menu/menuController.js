/*global angular */
(function () {
    'use strict';
    angular.module('shoalAdmin.views.menu')
        .controller('shoalAdmin.views.menu.NavBarController', function () {
            this.menuItems = [
                {
                    name: 'Orders',
                    state: 'orders'
                },
                {
                    name: 'Active Offers',
                    state: 'activeOffers'
                },
                {
                    name: 'Registrations',
                    state: 'registrations'
                },
                {
                    name: 'Manage Products',
                    state: 'manageProducts'
                },
                {
                    name: 'Credit Withdraw Requests',
                    state: 'creditWithdrawRequests'
                },
                {
                    name: 'Buyer list',
                    state: 'buyerList'
                },
                {
                    name: 'Principal Dashboard',
                    state: 'principalDashboard'
                }
            ];
            this.rightMenuItems = [
                {
                    name: 'Logout',
                    state: 'logout'
                }
            ];
        })
        .controller('shoalApp.views.menu.HeaderMenuController', function () {
            this.menuItems = [];
        });
}());