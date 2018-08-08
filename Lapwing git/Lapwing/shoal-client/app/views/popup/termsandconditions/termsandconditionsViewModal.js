/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.popup').
        factory('shoalApp_views_popup_termsandconditions_TermsandconditionsViewModal', function ($uibModal) {
            var modalConfig = {
                    templateUrl: 'views/popup/termsandconditions/termsandconditionsView.html',
                    controller: 'shoalApp.views.popup.TermsandconditionsController as vm',
                    size: 'lg'
                };

            return {
                show: function () {
                    $uibModal.open(modalConfig);
                }
            };
        });
}());
