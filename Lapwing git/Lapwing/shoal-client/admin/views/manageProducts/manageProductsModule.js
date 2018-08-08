/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.views.manageProducts', ['ui.router',
            'ui.bootstrap',
            'shoalAdmin.manageProducts',
            'shoalCommon.products',
            'shoalCommon.vendors',
            'shoalCommon.vatRate',
            'shoalAdmin.manageProductCatagory',
            'ui-notification',
            'shoalCommon.validator'
        ])
        .config([function ($stateProvider, NotificationProvider) {
            $stateProvider
                .state('loggedIn.manageProducts', {
                    url: '/manageProducts/',
                    template: '<ui-view/>',
                    redirectTo: 'loggedIn.manageProducts.create',
                    data: {
                        access: {
                            requiresAuthorisation: true
                            // specify permissions here
                            //requirePermissions: ['User']
                            //permissionType: 'AtLeastOne'
                        }
                    }
                })
                .state('loggedIn.manageProducts.create', {
                    url: '',
                    templateUrl: 'views/manageProducts/createProductView.html',
                    controller: 'shoalAdmin.views.manageProducts.CreateProductController',
                    controllerAs: 'vm',
                    resolve: {
                        productForm: function (shoalAdmin_manageProducts_ManageProductsService) {
                            return shoalAdmin_manageProducts_ManageProductsService.buildProduct();
                        }
                    }
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
