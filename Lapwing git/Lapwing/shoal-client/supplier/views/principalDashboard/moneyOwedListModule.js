/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.views.principalDashboard', ['shoalSupplier.buyers', 'ui.bootstrap'])
        .config([function ($stateProvider) {
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
                    controller: 'shoalSupplier.views.principalDashboard.moneyOwedListSupplierController',
                    controllerAs: 'vm'
                });
        }]);
}());
