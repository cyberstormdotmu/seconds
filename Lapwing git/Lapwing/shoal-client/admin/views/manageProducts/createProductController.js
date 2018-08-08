/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalAdmin.views.manageProducts')
        .controller('shoalAdmin.views.manageProducts.CreateProductController', function ($scope, $rootScope, productForm, $uibModal, shoalAdmin_manageProducts_ManageProductsService, shoalAdmin_manageProductCatagory_manageProductCatagoryService, shoalCommon_classes_ProductOffer) {
            var vm = this,
                addNewCategory,
                i,
                len,
                endDate,
                startDate,
                start_date,
                end_date,
                start_month,
                end_month,
                start_year,
                end_year,
                productsService = shoalAdmin_manageProducts_ManageProductsService,
                productCatagoryService = shoalAdmin_manageProductCatagory_manageProductCatagoryService,
                productInitClass = shoalCommon_classes_ProductOffer,
                modalConfig = {
                    templateUrl: '../admin/views/manageProducts/createVendorView.html',
                    controller: 'shoalAdmin.views.manageProducts.CreateVendorController as vm',
                    size: 'lg'
                },
                modalConfigForBulkProducts = {
                    templateUrl: '../admin/views/manageProducts/BulkUploadProducts/bulkUploadProductsView.html',
                    controller: 'shoalAdmin.views.manageProducts.BulkUploadProductsController as vm',
                    size: 'lg'
                },
                startDate = new Date(),
                endDate = new Date(),
                datePickerOptions = {
                    formatYear: 'yy',
                    startingDay: 1,
                    showWeeks: false,
                    closeOnDateSelection: true
                },
                fetchProductCategoryList = function (id) {
                    console.log("fetchProductCategoryList");
                    productCatagoryService.getproductCategoryList(id)
                        .then(function (response) {
                            console.log(response);
                            vm.productCategoryList = response;
                            $rootScope.showTabOne = true;
                            $rootScope.showTabTwo = false;
                        }, function () {
                            console.log("error reading unconfirmed Credit Withdraw Requests");
                        });
                },
                fetchProducts = function () {
                    console.log("fetch Products List");
                    productsService.fetchAllProduct()
                        .then(function (products) {
                            vm.products = products;
                            $rootScope.showTabOne = true;
                            $rootScope.showTabTwo = false;
                        });
                };
            vm.deleteCategory = function (categoryId) {
                console.log(categoryId);
                productCatagoryService.deleteCategory(categoryId).then(function (response) {
                    fetchProductCategoryList(0);
                    vm.categoryForm = [];
                    vm.categoryForm.isSuccess = true;
                    vm.categoryForm.isError = false;
                    vm.categoryForm.successMessage = 'Product category deleted successfuly.';
                    console.log(response);
                }, function (error) {
                    fetchProductCategoryList(0);
                    vm.categoryForm = [];
                    vm.categoryForm.isSuccess = false;
                    vm.categoryForm.isError = true;
                    if (error === null || error === '') {
                        vm.categoryForm.errorMessage = 'Error while deleting product category. Please try after sometime.';
                    } else {
                        vm.categoryForm.errorMessage = error;
                    }
                    console.log(error);
                });
            };
            vm.editProduct = function (product) {
                vm.addButtonText = 'Edit product';
                vm.productForm = productForm;
                vm.setValues(product);                
                vm.productForm.edit = productForm.edit;
                vm.productForm.save = productForm.save;
                vm.isAdd = false;
                $rootScope.showTabOne = false;
                $rootScope.showTabTwo = true;
            };
            vm.addNewProduct = function () {
                vm.addButtonText = 'Create product';
                vm.productForm = productForm;
                vm.productForm.specifications[0].name = '';
                vm.productForm.specifications[0].description = '';
                vm.productForm.images[0].url = '';
                vm.productForm.images[0].description = '';
                vm.productForm.priceBands[0].maxVolume = undefined;
                vm.productForm.priceBands[0].oldMaxVolume = undefined;
                vm.productForm.priceBands[0].minVolume = 0;
                vm.productForm.priceBands[0].buyerPrice = undefined;
                vm.productForm.priceBands[0].vendorPrice = undefined;
                vm.productForm.priceBands[0].shoalMargin = 0;
                vm.productForm.priceBands[0].distributorMargin = 0;
                vm.productForm.priceBands[0].unbounded = false;
                vm.isAdd = true;
                $rootScope.showTabOne = false;
                $rootScope.showTabTwo = true;
            }
            vm.deleteProduct = function (product) {
                console.log(product);
                productsService.deleteProduct(product.code).then(function (response) {
                    console.log(response);
                }, function (error) {
                    console.log(error.data.message);
                });
            };
            vm.backToMainPage = function () {
                vm.clear();
                vm.isAdd = true;
                $rootScope.showTabOne = true;
                $rootScope.showTabTwo = false;
            };
            vm.clear = function () {
                vm.productForm.id = '';
                vm.productForm.name = '';
                vm.productForm.code = '';
                vm.productForm.vatRate = '';
                vm.productForm.mainCategory = '';
                vm.productForm.vendorName = '';
                vm.productForm.description = '';
                vm.productForm.review = '';
                vm.productForm.processNotification = false;
                vm.productForm.submitNotification = false;
                vm.productForm.suitability = '';
                vm.productForm.stock = 0;
                vm.productForm.offerStartDate = '';
                vm.productForm.offerEndDate = '';
                vm.productForm.termsAndConditions = '';
                
                if (vm.productForm.specifications.length > 0) {
                    for (i = vm.productForm.specifications.length; i > 0; i -= 1) {
                        vm.productForm.specifications.splice(i, 1);
                    }
                }
                if (vm.productForm.images.length > 0) {
                    for (i = vm.productForm.images.length; i > 0; i -= 1) {
                        vm.productForm.images.splice(i, 1);
                    }
                }
                if (vm.productForm.priceBands.length > 0) {
                    for (i = vm.productForm.priceBands.length; i > 0; i -= 1) {
                        vm.productForm.priceBands.splice(i, 1);
                    }
                }
                if (vm.productForm.categories.length > 0) {
                    for (i = vm.productForm.categories.length; i >= 0; i -= 1) {
                        vm.productForm.categories.splice(i, 1);
                    }
                }

                vm.productForm.maximumPurchaseLimit = 0;
                vm.productForm.status = {
                    isSaved: false,
                    isError: false,
                    errorMessage: ''
                };
                $scope.vm.createProductForm.$setPristine();
                $scope.vm.createProductForm.$setUntouched();
            };
            vm.setValues = function (product) {
                vm.productForm.id = product.id;
                vm.productForm.name = product.name;
                vm.productForm.code = product.code;
                vm.productForm.vatRate = product.vatRate.code;
                vm.productForm.mainCategory = product.categories[product.categories.length-1];
                vm.productForm.vendorName = product.vendorName;
                vm.productForm.description = product.description;
                vm.productForm.review = product.review;
                vm.productForm.processNotification = product.processNotification;
                vm.productForm.submitNotification = product.submitNotification;
                vm.productForm.suitability = product.suitability;
                vm.productForm.stock = product.stock;

                startDate = new Date(product.offerStartDate);
                start_date = startDate.getDate();
                start_date = start_date.toString().length === 1 ? '0' + start_date : start_date;
                start_month = startDate.getMonth();
                start_month = start_month.toString().length === 1 ? '0' + start_month : start_month;
                start_year = startDate.getFullYear();
                vm.productForm.offerStartDate = new Date(start_year, start_month, start_date);
                
                endDate = new Date(product.offerEndDate);
                end_date = endDate.getDate();
                end_date = end_date.toString().length === 1 ? '0' + end_date : end_date;
                end_month = endDate.getMonth();
                end_month = end_month.toString().length === 1 ? '0' + end_month : end_month;
                end_year = endDate.getFullYear();
                vm.productForm.offerEndDate = new Date(end_year, end_month, end_date);

                vm.productForm.termsAndConditions = product.termsAndConditions;

                //// Product Specification Add
                if (vm.productForm.specifications.length > 1) {
                    for (i = vm.productForm.specifications.length; i > 0; i -= 1) {
                        vm.productForm.specifications.splice(i, 1);
                    }
                }
                (product.specifications || []).forEach(function (s) {
                    vm.productForm.specifications.push(s);
                });
                vm.productForm.specifications.splice(0, 1);

                //// Product Images Add
                if (vm.productForm.images.length > 1) {
                    for (i = vm.productForm.images.length; i > 0; i -= 1) {
                        vm.productForm.images.splice(i, 1);
                    }
                }
                (product.images || []).forEach(function (i) {
                    vm.productForm.images.push(i);
                });
                vm.productForm.images.splice(0, 1);

                //// Product Price Bands Add
                if (vm.productForm.priceBands.length > 1) {
                    for (i = vm.productForm.priceBands.length; i > 0; i -= 1) {
                        vm.productForm.priceBands.splice(i, 1);
                    }
                }
                (product.priceBands || []).forEach(function (p) {                    
                    vm.productForm.priceBands.push(p);
                });
                vm.productForm.priceBands.splice(0, 1);
                vm.productForm.priceBands[vm.productForm.priceBands.length-1].unbounded = true;

                //// Product Categories Add
                if (vm.productForm.categories.length > 1) {
                    for (i = vm.productForm.categories.length; i > 0; i -= 1) {
                        vm.productForm.categories.splice(i, 1);
                    }
                }
                (product.categories || []).forEach(function (c) {
                    vm.productForm.categories.push(c);
                });
                vm.productForm.categories.splice(0, 1);

                vm.productForm.maximumPurchaseLimit = product.maximumPurchaseLimit;
            };
            startDate.setHours(0, 0, 0, 0);
            vm.hideAddImageButton = false;
            vm.productForm = productForm;
            vm.isAdd = true;
            vm.addButtonText = 'Create product';
            vm.startDatePicker = {
                options: datePickerOptions,
                minDate: startDate,
                opened: false
            };
            vm.endDatePicker = {
                options: datePickerOptions,
                minDate: endDate,
                opened: false
            };
            vm.openVendorModal = function () {
                $uibModal.open(modalConfig);
            };
            vm.openUploadProductsModal = function () {
                vm.isAdd = true;
                $rootScope.showTabOne = false;
                $rootScope.showTabTwo = true;
                $uibModal.open(modalConfigForBulkProducts);
            };
            vm.openStartDatePicker = function () {
                vm.startDatePicker.opened = !vm.startDatePicker.opened;
            };
            vm.openEndDatePicker = function () {
                vm.endDatePicker.opened = !vm.endDatePicker.opened;
            };
            // update the min allowed date when the start date changes
            $scope.$watch(function () {
                return vm.productForm.offerStartDate;
            }, function (newValue, oldValue) {
                if (newValue) {
                    console.log("offer start date changed");
                    vm.endDatePicker.minDate = newValue;
                }
            });
            fetchProductCategoryList(0);
            vm.editCategory = function (category) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'views/manageProducts/EditProductCategory.html',
                    controller: 'shoalApp.views.manageProductCategory.manageProductCategoryController as vm',
                    size: 'lg',
                    resolve: {
                        category: category
                    }
                });

                modalInstance.result.then(function () {
                    fetchProductCategoryList(0);
                });
            };
            vm.productFormSave = function () {
                console.log(vm.isAdd);
                if (vm.isAdd) {
                    vm.productForm.save().then(function (product) {
                        vm.products.push(product);
                        vm.clear();
                        vm.successMessage = 'Product Saved Successfully';
                        vm.errorMessage = "";
                    }, function (error) {
                        console.log(error.data.message);
                        vm.errorMessageReson = error.data.message;
                        vm.errorMessage = 'Product can not save at time. please try again later';
                        vm.successMessage = "";
                    });
                } else {
                    vm.productForm.edit(vm.productForm).then(function (product) {
                        vm.products[vm.editIndex] = product;
                        vm.addButtonText = "Add Address";
                        vm.clear();
                        vm.successMessage = 'Product Edited Successfully';
                        vm.errorMessage = "";
                    }, function (error) {
                        vm.setValues(product);
                        $scope.addressForm.delivery_department.$setValidity('serverError', false);
                        vm.errorMessageReson = error.data.message;
                        vm.errorMessage = 'Product can not edit at time. please try again later';
                        vm.successMessage = "";
                    });
                }
            };
            vm.addCategory = function () {
                addNewCategory = {
                    id : 0,
                    name : null,
                    parent : {
                        id : 0,
                        name : null,
                        parent : null
                    }
                };
                var modalInstance = $uibModal.open({
                    templateUrl: 'views/manageProducts/EditProductCategory.html',
                    controller: 'shoalApp.views.manageProductCategory.manageProductCategoryController as vm',
                    size: 'lg',
                    resolve: {
                        category: addNewCategory
                    }
                });
                modalInstance.result.then(function () {
                    fetchProductCategoryList(0);
                });
            };
            fetchProducts();
        });
}());