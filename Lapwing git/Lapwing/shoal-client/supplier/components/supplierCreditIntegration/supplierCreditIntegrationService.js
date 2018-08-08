/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalSupplier.supplierCreditIntegration')
        .factory('shoalSupplier_supplierCreditIntegration_supplierCreditIntegrationService', function ($rootScope, $q, $http, ENV) {

            var supplierCreditIntegrationList = ENV.webServiceUrl + "/admin/supplierCreditIntegrationList",
                CategoryDeleteUrl = ENV.webServiceUrl + "/admin/deleteCategory",
                addEditCategoryUrl = ENV.webServiceUrl + "/admin/addEditCategory",
                parentName,
                EditTermsAndCondition = function (termsAndCondition) {
                    console.log(termsAndCondition);
                    var defer = $q.defer();
                    $http({
                        method: 'PUT',
                        url: supplierCreditIntegrationList,
                        params: {'vendorId': termsAndCondition.id, 'vendorName': termsAndCondition.vendorName, 'termsAndCondition' : termsAndCondition.termsAndCondition}
                    }).then(function (response) {
                        var reason = response;
                        defer.resolve(Object.freeze(reason));
                    }, function (response) {
                        defer.reject(response.data.message);
                    });
                    return defer.promise;
                },
                getsupplierCreditIntegrationList = function () {
                    var defer = $q.defer();
                    $http({
                        method: 'GET',
                        url: supplierCreditIntegrationList
                    }).then(function (response) {
                        var reason = response.data;
                        defer.resolve(Object.freeze(reason));
                    }, function (response) {
                        defer.reject(response.data.message);
                    });
                    return defer.promise;
                },
                deleteCategory = function (categoryId) {
                    var defer = $q.defer(),
                        id = categoryId;
                    $http({
                        method: 'POST',
                        url: CategoryDeleteUrl,
                        params: {'id': id}
                    }).then(function (response) {
                        var reason = response.data;
                        defer.resolve(Object.freeze(reason));
                    }, function (response) {
                        defer.reject(response.data.message);
                    });
                    return defer.promise;
                },
                addOrEditProductCategory = function (addCategory) {
                    var defer = $q.defer();
                    parentName = addCategory.parent !== null ? addCategory.parent.name : '';
                    $http({
                        method: 'POST',
                        url: addEditCategoryUrl,
                        params: {'categoryId': addCategory.id, 'categoryName': addCategory.name, 'parentCategoryName' : parentName}
                    }).then(function (response) {
                        var reason = response.data;
                        defer.resolve(Object.freeze(reason));
                    }, function (response) {
                        defer.reject(response.data.message);
                    });
                    return defer.promise;
                };
            return {
                getsupplierCreditIntegrationList: getsupplierCreditIntegrationList,
                deleteCategory: deleteCategory,
                addOrEditProductCategory : addOrEditProductCategory,
                EditTermsAndCondition : EditTermsAndCondition
            };
        });
}());