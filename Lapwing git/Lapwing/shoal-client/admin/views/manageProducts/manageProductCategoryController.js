/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.manageProductCategory')
        .controller('shoalApp.views.manageProductCategory.manageProductCategoryController', function ($http, $scope, $modalInstance, category, shoalAdmin_manageProductCatagory_manageProductCatagoryService) {
            var vm = this,
                productCatagoryService = shoalAdmin_manageProductCatagory_manageProductCatagoryService,
                parentCategoryName,
                isAdd,
                fetchProductCategoryList = function (id) {
                    console.log("fetchProductCategoryList");
                    productCatagoryService.getproductCategoryList(id)
                        .then(function (response) {
                            console.log(response);
                            vm.productCategoryList = response;
                        }, function () {
                            console.log("error reading unconfirmed Credit Withdraw Requests");
                        });
                };
            vm.productCategoryEdit = function (category) {
                console.log(vm.manageCategory.checked);
                if (vm.manageCategory.checked) {
                    if (vm.manageCategory.parent !== null) {
                        vm.manageCategory.parent.name = "";
                        vm.manageCategory.parent.id = 0;
                    }
                }

                if (category.id === 0) {
                    parentCategoryName = category.parent.name;
                    category.parent = {};
                    category.parent.name = parentCategoryName;
                    isAdd = true;
                } else {
                    isAdd = false;
                }

                productCatagoryService.addOrEditProductCategory(category)
                    .then(function (response) {
                        vm.EditCategory = response;
                        vm.categoryFormAddEdit = [];
                        vm.categoryFormAddEdit.isSuccess = true;
                        vm.categoryFormAddEdit.isError = false;
                        if (isAdd) {
                            vm.categoryFormAddEdit.successMessage = 'Product category added successfuly.';
                            productCatagoryService.getproductCategoryList(0)
                                .then(function (response) {
                                    response.forEach(function (value) {
                                        if (value.name === category.name) {
                                            category.id = value.id;
                                            vm.manageCategory.id = value.id;
                                        }
                                    });
                                }, function () {
                                    console.log("error reading categories data.");
                                });
                        } else {
                            vm.categoryFormAddEdit.successMessage = 'Product category edited successfuly.';
                        }
                    }, function (response) {
                        vm.categoryFormAddEdit = [];
                        vm.categoryFormAddEdit.isSuccess = false;
                        vm.categoryFormAddEdit.isError = true;
                        if (response !== null || response !== '') {
                            vm.categoryFormAddEdit.errorMessage = response;
                        } else {
                            if (isAdd) {
                                vm.categoryFormAddEdit.errorMessage = 'Error while adding product category. Please try after sometime.';
                            } else {
                                vm.categoryFormAddEdit.errorMessage = 'Error while editing product category. Please try after sometime.';
                            }
                        }
                    });
            };
            console.log(category.id);
            vm.close = function () {
                $modalInstance.close();
            };
            vm.manageCategory = category;
            vm.isParent = false;
            if (category.parent === null) {
                vm.isParent = true;
            } else {
                vm.isParent = false;
                vm.manageCategory.parent = category.parent;
                console.log(vm.manageCategory.parent.name);
            }
            if (category.id !== 0) {
                fetchProductCategoryList(category.id);
            } else {
                fetchProductCategoryList(0);
            }
        });
}());