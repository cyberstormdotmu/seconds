/*global angular */
(function () {
    'use strict';
    angular.module('shoalApp.views.pages', ['ui.router.state'])
        .config(function ($stateProvider) {
            $stateProvider
                .state('how-it-works', {
                    url: '/how-it-works',
                    templateUrl: 'views/pages/howitworksView.html'
                })
                .state('privacy-policy', {
                    url: '/privacy-policy',
                    templateUrl: 'views/pages/privacypolicyView.html'
                })
                .state('term-and-conditions', {
                    url: '/term-and-conditions',
                    templateUrl: 'views/pages/termsandconditionsView.html'
                })
                .state('contact-us', {
                    url: '/contact-us',
                    templateUrl: 'views/pages/contactUsView.html'
                })
                .state('report-a-problem', {
                    url: '/report-a-problem',
                    templateUrl: 'views/pages/reportProblem/reportProblemView.html',
                    controller: 'shoalApp.views.pages.ReportProblemController as vm'
                })
                .state('pilots-information', {
                    url: '/pilots-information',
                    templateUrl: 'views/pages/pilotsInformationView.html'
                })
                .state('how-it-work', {
                    url: '/how-it-work',
                    templateUrl: 'views/pages/howitworkView.html'
                })
                .state('welcome-to-silverwing', {
                    url: '/welcome-to-silverwing',
                    templateUrl: 'views/pages/welcomeToSilverwingView.html'
                })
                .state('faqs', {
                    url: '/faqs',
                    templateUrl: 'views/pages/faqs.html'
                })
                .state('paymentSuccessfulPage', {
                    url: '/paymentSuccessfulPage',
                    templateUrl: 'views/pages/paymentSuccessfulPage.html'
                });
        });
}());