/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.views.addAdmin', ['ui-notification', 'ui.bootstrap'])
        .config([function ($stateProvider) {
            $stateProvider
                .state('loggedIn.admin', {
                    url: '/addAdmin',
                    template: '<ui-view/>',
                    redirectTo: 'loggedIn.admin.add',
                    data: {
                        access: {
                            requiresAuthorisation: true
                            // specify permissions here
                            //requirePermissions: ['User']
                            //permissionType: 'AtLeastOne'
                        }
                    }
                })
                .state('loggedIn.admin.add', {
                    url: '',
                    templateUrl: 'views/addAdmin/addAdminView.html',
                    controller: 'shoalAdmin.views.addAdmin.AddAdminController',
                    controllerAs: 'vm'
                });
        }]);
}());
