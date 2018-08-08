/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.product', ['ui.router.state', 'shoalApp.charts', 'shoalCommon.products', 'shoalApp.orders',
            'shoalCommon.gallery'])
        .config(function ($stateProvider) {
            $stateProvider
                .state('products', {
                    url: '/products',
                    template: '<ui-view/>',
                    redirectTo: 'products.list',
                    data: {
                        access: {
                            requiresAuthorisation: true
                            // specify permissions here
                            //requirePermissions: ['User']
                            //permissionType: 'AtLeastOne'
                        }
                    }
                })
                .state('products.list', {
                    url: '/productcategory/:category',
                    templateUrl: 'views/product/productListingView.html',
                    controller: 'shoalApp.views.product.ProductListingController',
                    controllerAs: 'vm',
                    params: {
                        category: {
                            value: 'Products',
                            squash: true
                        }
                    }
                })
                .state('products.detail', {
                    url: '/:code',
                    templateUrl: 'views/product/productDetailsView.html',
                    controller: 'shoalApp.views.product.ProductDetailsController',
                    controllerAs: 'vm'
                });
        });
}());
