/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalPublic.views.registration')
        .controller('shoalPublic.views.registration.RegistrationController', function ($scope, $http, $state, $location, registrationForm, $modalInstance, shoalPublic_views_pages_RegistrationTermsandconditionModal, shoalPublic_views_pages_PrivacyPolicyModal) {
            var vm = this,
                registrationTermsandconditionModal = shoalPublic_views_pages_RegistrationTermsandconditionModal,
                openRegistrationTermsandconditionModal = function () {
                    registrationTermsandconditionModal.show();
                },
                privacyPolicyModal = shoalPublic_views_pages_PrivacyPolicyModal,
                openPrivacyPolicyModal = function () {
                    privacyPolicyModal.show();
                };
            $scope.mobilemaxlength = 10;
            vm.registrationForm = registrationForm;
            vm.saving = false;
            $scope.confirmPassword = '';
            $scope.Codelist = [];
            $http.get('https://restcountries.eu/rest/v2/all')
                   .then(function (response) {
                    angular.forEach(response.data, function (data) {
                        $scope.Codelist.push(data.callingCodes[0]);
                    });
                    $scope.mobilemaxlength = 10;
                });
            vm.save = function () {
                if (!vm.saving) {
                    $scope.mobilemaxlength = 15;
                    vm.saving = true;
                   // vm.registrationForm.organisation.mobileNumber = $scope.countryCode + vm.registrationForm.organisation.mobileNumber;
                    this.registrationForm.save($scope.countryCode)
                        .then(function () {
                            $scope.mobilemaxlength = 10;
                            vm.saving = false;
                            vm.registrationForm.isSaved = true;
                        }, function () {
                            vm.saving = false;
                        });
                }
            };
            vm.close = function () {
                $modalInstance.dismiss();
            };
            vm.forgottenPassword = function () {
                $modalInstance.dismiss();
                $state.go('passwordreset');
            };

            vm.openRegistrationTermsandconditionModal = openRegistrationTermsandconditionModal;
            vm.openPrivacyPolicyModal = openPrivacyPolicyModal;
        });
}());