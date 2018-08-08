/*global angular */
(function () {
    'use strict';
    angular.module('shoalSupplier.frame', [])
        .config([function ($stateProvider) {
            $stateProvider
                .state('loggedIn', {
                    abstract: true,
                    templateUrl: 'components/frame/frame.html',
                    data: {
                        access: {
                            requiresAuthorisation: true
                            // specify permissions here
                            //requirePermissions: ['User']
                            //permissionType: 'AtLeastOne'
                        }
                    }
                });
        }]);
}());