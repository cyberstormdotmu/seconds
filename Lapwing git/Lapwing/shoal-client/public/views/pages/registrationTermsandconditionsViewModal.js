/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.views.pages').
        factory('shoalPublic_views_pages_RegistrationTermsandconditionModal', function ($uibModal) {
            var modalConfig = {
                templateUrl: 'views/pages/registrationTermsandconditionsView.html',
                controller: 'shoalPublic.views.pages.registrationTermsandCondition.TermsandconditionsController as vm',
                size: 'lg'
            };

            return {
                show: function () {
                    $uibModal.open(modalConfig);
                }
            };
        });
}());
