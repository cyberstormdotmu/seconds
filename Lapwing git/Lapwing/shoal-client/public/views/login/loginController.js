/*global angular, console */
(function () {
    'use strict';
    /* Improvements : We can add a spinner on the login screen if a response takes longer than 250ms
     We can register an event here with Auth.onAuthorised(fn {})
     */
    angular.module('shoalPublic.views.login')
        .controller('shoalPublic.views.login.LoginController', function ($window, $rootScope, $scope, $state, $stateParams, shoalCommon_security_AuthService, $uibModalInstance, $cookies) {
            var vm = this,
                Auth = shoalCommon_security_AuthService,
                login = function () {
                    Auth.attemptLogin()
                        .then(function () {
                            vm.error = undefined;
                            $window.location.replace('/app');
                            console.log("Credit application Cookie remove START");
                           /* $cookies.remove('creditFormCompanyName');
                            $cookies.remove('creditFormTradingAs');
                            $cookies.remove('creditFormInvoiceAddress');
                            $cookies.remove('creditFormDeliveryAddress');
                            $cookies.remove('creditFormRegisteredAddress');
                            $cookies.remove('creditFormLandlineNumber');
                            $cookies.remove('creditFormWebsite');
                            $cookies.remove('creditFormVatRegistration');
                            $cookies.remove('creditFormPurchasingManager');
                            $cookies.remove('creditFormContactName');
                            $cookies.remove('creditFormTelephoneNumber');
                            $cookies.remove('creditFormEmailAddress');
                            $cookies.remove('creditFormIsCreditAccount');
                            $cookies.remove('creditFormPrintName');
                            $cookies.remove('creditFormRegistrationDate');
                            $cookies.remove('creditFormFullCompanyName');
                            $cookies.remove('creditFormTradingName');
                            $cookies.remove('creditFormExpectedCredit');
                            console.log("Credit application Cookie remove END");*/
                        }, function (error) {
                            vm.error = error.reason;
                        });
                },
                close = function () {
                    $uibModalInstance.dismiss();
                },
                register = function () {
                    $uibModalInstance.dismiss();
                    $state.go('registration');
                },
                SetCookies = function () {
                    $cookies.put("email", vm.currentUser.email);
                    $cookies.put("password", vm.currentUser.password);
                },
                forgottenPassword = function () {
                    $uibModalInstance.dismiss();
                    $state.go('passwordreset');
                };
            Auth.clearCurrentUser();

            vm.currentUser = Auth.getCurrentUser();
            $scope.cookieemail = $cookies.get('email');
            $scope.cookiepassword = $cookies.get('password');
            vm.login = login;
            vm.close = close;
            vm.register = register;
            vm.forgottenPassword = forgottenPassword;
            vm.loggedOut = !!$stateParams.logout;
            vm.SetCookies = SetCookies;
        });
}());