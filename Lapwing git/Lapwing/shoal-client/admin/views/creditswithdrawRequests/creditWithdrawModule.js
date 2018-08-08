/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.views.creditWithdrawRequests', ['shoalAdmin.creditWithdrawRequests', 'shoalCommon.address', 'ui-notification', 'ui.bootstrap'])
        .config([function ($stateProvider, NotificationProvider) {
            $stateProvider
                .state('loggedIn.creditWithdrawRequests', {
                    url: '/creditWithdrawRequests',
                    template: '<ui-view/>',
                    redirectTo: 'loggedIn.creditWithdrawRequests.list',
                    data: {
                        access: {
                            requiresAuthorisation: true
                            // specify permissions here
                            //requirePermissions: ['User']
                            //permissionType: 'AtLeastOne'
                        }
                    }
                })
                .state('loggedIn.creditWithdrawRequests.list', {
                    url: '',
                    templateUrl: 'views/creditsWithdrawRequests/creditWithdrawListView.html',
                    controller: 'shoalAdmin.views.creditWithdrawRequests.CreditWithdrawListController',
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
