/*global angular, console */
(function () {
    'use strict';
    /* Improvements : We can add a spinner on the login screen if a response takes longer than 250ms
     We can register an event here with Auth.onAuthorised(fn {})
     */
    angular.module('shoalPublic.views.contact')
        .controller('shoalPublic.views.contact.ContactController', function ($window, $scope, $state, shoalPublic_views_supplierregistration_RegistrationViewModal, $uibModalInstance, shoalPublic_contact_ContactService) {
            var vm = this,
                service = shoalPublic_contact_ContactService,
                registrationModal = shoalPublic_views_supplierregistration_RegistrationViewModal,
                close = function () {
                    $uibModalInstance.dismiss();
                },
                openSupplierRegistrationModal = function () {
                    $uibModalInstance.dismiss();
                    registrationModal.show();
                },
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
                            message: vm.message,
                            messageType: vm.messageType
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
            vm.close = close;
            vm.messageType = 'Contact_Us';
            vm.submitContactForm = submitContactForm;
            vm.openSupplierRegistrationModal = openSupplierRegistrationModal;
        });
}());