/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalPublic.contact')
        .directive('shoContactForm', function () {
            return {
                restrict: 'E',
                templateUrl: 'components/contact/contactFormView.html',
                scope: {},
                controllerAs: 'vm',
                controller: function (shoalPublic_contact_ContactService) {
                    var vm = this,
                        service = shoalPublic_contact_ContactService,
                        submitContactForm = function () {
                            vm.submitAttempted = true;

                            if (vm.contactForm.$valid && !vm.submitting) {
                                vm.submitting = true;
                                vm.errorMessage = undefined;
                                vm.successMessage = undefined;

                                service.submitContactRequest({
                                    name: vm.name,
                                    companyName: vm.companyName,
                                    phoneNumber: vm.phoneNumber,
                                    emailAddress: vm.emailAddress,
                                    message: vm.message
                                })
                                    .then(function () {
                                        vm.successMessage = 'Your message has been sent and a member of the team will reply soon.';
                                        vm.submitting = false;
                                        vm.submitAttempted = false;
                                    }, function () {
                                        vm.errorMessage = 'Your message could not be sent at this time, please try again later.';
                                        vm.submitting = false;
                                        vm.submitAttempted = false;
                                    });
                            }
                        };

                    vm.submitContactForm = submitContactForm;
                }
            };
        });
}());