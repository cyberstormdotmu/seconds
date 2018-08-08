/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.views.principalDashboard', ['shoalAdmin.buyers', 'ui-notification', 'ui.bootstrap'])
        .config([function ($stateProvider, NotificationProvider) {
            $stateProvider
                .state('loggedIn.principalDashboard', {
                    url: '/principalDashboard/',
                    template: '<ui-view/>',
                    redirectTo: 'loggedIn.principalDashboard.detail',
                    data: {
                        access: {
                            requiresAuthorisation: true
                            // specify permissions here
                            //requirePermissions: ['User']
                            //permissionType: 'AtLeastOne'
                        }
                    }
                })
                .state('loggedIn.principalDashboard.detail', {
                    url: '',
                    templateUrl: 'views/principalDashboard/moneyOwedListSupplierView.html',
                    controller: 'shoalAdmin.views.principalDashboard.moneyOwedListSupplierController',
                    controllerAs: 'vm'
                });

            NotificationProvider.setOptions({
                delay: 5000,
                startTop: 20,
                startRight: 10,
                verticalSpacing: 20,
                horizontalSpacing: 20,
                positionX: 'center',
                positionY: 'top',
                templateUrl: '../shared/components/ui-notification/ui-notification.html'
            });

        }]);
}());
