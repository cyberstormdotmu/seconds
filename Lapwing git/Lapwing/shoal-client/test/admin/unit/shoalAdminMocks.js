/*global angular, jasmine, spyOn, console, SHOAL */
(function () {
    'use strict';
    var my = {};
    my.currentUser = {
        name: 'rogerwatkins@codera.com',
        roles: ["BUYER"],
        authenticated: true,
        error: false
    };
    my.registeredUsers = {};
    angular.module('shoalAdmin.ShoalAdminMocks', [])
        .mockFactory('$window', function () {
            var location = jasmine.createSpyObj('location', ['replace']);
            return {
                location: location
            };
        })
        .mockFactory('$rootScope', function ($rootScope) {
            spyOn($rootScope, ['$broadcast']).and.callThrough();
            return $rootScope;
        })
        .mockFactory('$scope', function ($rootScope) {
            return $rootScope.$new();
        })
        .mockFactory('$q', function ($q) {
            return $q;
        })
        .mockFactory('$state', function ($state) {
            return $state;
        })
        .mockFactory('$routeParams', function ($routeParams) {
            return $routeParams;
        })
        .mockFactory('$resource', function ($q, $resource) {
            return $resource;
        })
        .mockFactory('$http', function ($http) {
            return $http;
        })
        .mockFactory('$q', function ($q) {
            return $q;
        })
        .mockFactory('$location', function () {
            return jasmine.createSpyObj('$location', ['path', 'hash']);
        })
        .mockFactory('$anchorScroll', function ($anchorScroll) {
            return $anchorScroll;
        })
        .mockFactory('$modalInstance', [function () {
            return jasmine.createSpyObj('$modalInstance', ['close']);
        }])
        .mockFactory('$uibModal', function ($rootScope, $q) {
            var uibModal = jasmine.createSpyObj('$uibModal', ['open']),
                defer,
                promise = function () {
                    // capture defer so we can trigger promise at will
                    defer = $q.defer();
                    return defer.promise;
                };

            uibModal.open.and.callFake(function () {
                return {
                    result: promise()
                };
            });
            uibModal.triggerClose = function () {
                if (defer) {
                    defer.resolve();
                    $rootScope.$digest();
                }
            };
            uibModal.triggerLoseFocusClose = function () {
                if (defer) {
                    defer.reject();
                    $rootScope.$digest();
                }
            };
            return uibModal;
        })
        .mockFactory('shoalAdmin_buyers_buyersResource', function () {
            var buyerResource = jasmine.createSpyObj('shoalAdmin_buyers_buyersResource',
                ['fetchInactiveBuyers', 'activateBuyer']),
                PUT = function (data, success, fail) {
                    if (!buyerResource.reject && success) {
                        success(buyerResource.responseData);
                    }
                    if (buyerResource.reject && fail) {
                        fail({
                            data: buyerResource.responseData
                        });
                    }
                },
                GET = function (success, fail) {
                    if (!buyerResource.reject && success) {
                        success(buyerResource.responseData);
                    }
                    if (buyerResource.reject && fail) {
                        fail({
                            data: buyerResource.responseData
                        });
                    }
                };
            buyerResource.reject = false;
            buyerResource.responseData = {};

            buyerResource.fetchInactiveBuyers.and.callFake(GET);
            buyerResource.activateBuyer.and.callFake(PUT);
            return buyerResource;
        })
        .mockFactory('shoalAdmin_manageProducts_ProductsResource', function () {
            var manageProductsResource = jasmine.createSpyObj('shoalAdmin_manageProducts_ProductsResource',
                ['save']),
                POST = function (data, success, fail) {
                    if (!manageProductsResource.reject && success) {
                        success(manageProductsResource.responseData);
                    }
                    if (manageProductsResource.reject && fail) {
                        fail({
                            data: manageProductsResource.responseData
                        });
                    }
                };
            manageProductsResource.reject = false;
            manageProductsResource.responseData = {};

            manageProductsResource.save.and.callFake(POST);
            return manageProductsResource;
        })
        .mockFactory('shoalCommon_classes_ProductOffer', function (shoalCommon_classes_ProductOffer) {
            return shoalCommon_classes_ProductOffer;
        })
        .mockFactory('shoalAdmin_classes_RegistrationCollection', function (shoalAdmin_classes_RegistrationCollection) {
            return shoalAdmin_classes_RegistrationCollection;
        })
        .mockFactory('productForm', function (shoalCommon_classes_ProductOffer) {
            var productOffer = shoalCommon_classes_ProductOffer({
                code: 'PRODUCTCODE'
            });
            console.log("mocking the productForm");
            return productOffer;
        })
        .mockFactory('productCategories', function () {
            console.log("mocking the productCategories");
            return [
                'Laptops'
            ];
        })
        .constant('ENV', {
            webServiceUrl: "https://domain.com/ws"
        });
}());
