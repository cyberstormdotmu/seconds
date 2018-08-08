/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.views.pages').
        factory('shoalPublic_views_pages_PrivacyPolicyModal', function ($uibModal) {
            var modalConfig = {
                templateUrl: 'views/pages/registrationPrivacypolicyView.html',
                controller: 'shoalPublic.views.pages.registrationPrivacypolicy.PrivacyPolicyController as vm',
                size: 'lg'
            };

            return {
                show: function () {
                    $uibModal.open(modalConfig);
                }
            };
        });
}());
