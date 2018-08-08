/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.creditWithdrawRequests')
        .directive('shoCreditWithdrawList', function () {
            return {
                restrict: 'E',
                templateUrl: '../admin/components/creditswithdrawRequests/creditWithdrawListDirectiveView.html',
                scope: {},
                bindToController: {
                    creditWithdrawRequests: '='
                },
                controllerAs: 'vm',
                controller: function () {
                    return;
                }
            };
        });
}());