/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.template')
            .directive('shoalFooter', function () {
            var footerDirective = {
                restrict: 'A',
                replace: true,
                templateUrl: "/public/components/template/footer/footer.html",
                scope: {},
                controllerAs: 'vm',
                controller: function ($scope, shoalPublic_views_contact_ContactViewModal, shoalPublic_views_pages_RegistrationTermsandconditionModal, shoalPublic_views_pages_PrivacyPolicyModal) {
                    var vm = this,
                        contactViewModal = shoalPublic_views_contact_ContactViewModal,
                        openTermsAndConditionModal = shoalPublic_views_pages_RegistrationTermsandconditionModal,
                        openPrivacyPolicyModal = shoalPublic_views_pages_PrivacyPolicyModal;
                    vm.openContactModal = function () {
                        contactViewModal.show();
                    };
                    vm.openTermsAndConditionModal = function () {
                        openTermsAndConditionModal.show();
                    };
                    vm.openPrivacyPolicyModal = function () {
                        openPrivacyPolicyModal.show();
                    };
                }
            };

            return footerDirective;
        });
}());