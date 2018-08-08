/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.views.contact').
        factory('shoalPublic_views_contact_ContactViewModal', function ($uibModal) {
            var modalConfig = {
                templateUrl: 'views/contact/contactView.html',
                controller: 'shoalPublic.views.contact.ContactController as vm',
                size: 'lg'
            };

            return {
                show: function () {
                    $uibModal.open(modalConfig);
                }
            };
        });
}());
