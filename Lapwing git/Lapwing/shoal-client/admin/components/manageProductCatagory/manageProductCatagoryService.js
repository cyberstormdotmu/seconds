/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.manageProductCatagory')
        .factory('shoalAdmin_manageProductCatagory_manageProductCatagoryService', function ($rootScope, $q, $http, ENV) {

            var orderCategoryListUrl = ENV.webServiceUrl + "/admin/productCategoryList",
                CategoryDeleteUrl = ENV.webServiceUrl + "/admin/deleteCategory",
                addEditCategoryUrl = ENV.webServiceUrl + "/admin/addEditCategory",
                parentName,
                getProductCategoryList = function (id) {
                    var defer = $q.defer();
                    $http({
                        method: 'GET',
                        url: orderCategoryListUrl,
                        params: {'id': id}
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
                getproductCategoryList: getProductCategoryList,
                deleteCategory: deleteCategory,
                addOrEditProductCategory : addOrEditProductCategory
            };
        });
}());