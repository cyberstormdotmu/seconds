/*global angular, console */
(function () {
    'use strict';
    /* Improvements : We can add a spinner on the login screen if a response takes longer than 250ms
     We can register an event here with Auth.onAuthorised(fn {})
     */
    angular.module('shoalApp.views.pages')
        .controller('shoalApp.views.pages.ReportProblemController', function (shoalApp_reportProblem_ReportProblemService) {
            var vm = this,
                reportProblemService = shoalApp_reportProblem_ReportProblemService,
                submitReportProblemForm = function () {
                    vm.submitAttempted = true;

                    if (vm.reportProblemForm.$valid && !vm.submitting) {
                        vm.submitting = true;
                        vm.errorMessage = undefined;
                        vm.successMessage = undefined;

                        reportProblemService.submitContactRequest({
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
            vm.messageType = 'Report_Problem';
            vm.submitReportProblemForm = submitReportProblemForm;
        });
}());