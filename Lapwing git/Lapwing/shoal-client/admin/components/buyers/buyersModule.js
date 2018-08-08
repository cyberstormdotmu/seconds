/*global angular */
(function () {
    'use strict';
    angular.module('shoalAdmin.buyers', ['ngResource', 'ui-notification', 'shoalAdmin.classes'])
        .config([function (NotificationProvider) {
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
