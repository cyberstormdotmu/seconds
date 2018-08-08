/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.manageProducts')
        .factory('shoalAdmin_manageProducts_ManageProductsService', function ($rootScope, $q, shoalAdmin_manageProducts_ProductsResource,
            shoalCommon_classes_ProductOffer, $http, ENV) {

            var my = {},
                priceBandLength,
                productFormEditTimeSave,
                adminManageProductsWebServiceUrl = ENV.webServiceUrl + "/admin/manageProducts",
                adminManageProductsUploadFileWebServiceUrl = ENV.webServiceUrl + "/admin/manageProducts/uploadFile",
                vendorSave = function (vendorForm) {
                    var that = this,
                        deferred = $q.defer();
                    my.productResource.vendorSave(vendorForm.vendorName, function (response) {
                        that.isSaved = true;
                        that.isError = false;
                        my.vendor.form.vendor = angular.merge(my.vendor.form.vendor, response);
                        $rootScope.$broadcast('newVendorAdd', my.vendor.form.vendor);
                        deferred.resolve(my.vendor.form.vendor);
                    }, function (response) {
                        that.isError = true;
                        deferred.reject(response);
                    });
                    return deferred.promise;
                },
                deleteProduct = function (productCode) {
                    var that = this,
                        deferred = $q.defer(),
                        code = productCode;
                    my.productResource.delete({code: code}, function (response) {
                        deferred.resolve(code);
                    }, function (response) {
                        that.isError = true;
                        deferred.reject(response);
                    });
                    return deferred.promise;
                },
                fetchAllProduct = function () {
                    var defer = $q.defer();
                    $http({
                        method: 'GET',
                        url: adminManageProductsWebServiceUrl
                    }).then(function (response) {
                        var reason = response.data;
                        defer.resolve(Object.freeze(reason));
                    }, function (response) {
                        defer.reject(response.data.message);
                    });
                    return defer.promise;
                },
                uploadBulkUploadProductsFile = function (file, uploadUrl) {
                    console.log("Service Call File");
                    console.log(file);
                    var defer = $q.defer(),
                        fd = new FormData();
                    fd.append('file', file);
                    $http.post(adminManageProductsUploadFileWebServiceUrl, fd, {
                        transformRequest: angular.identity,
                        headers: {'Content-Type': undefined}
                    }).then(function (response) {
                        console.log("response");
                        console.log(response);
                        defer.resolve(response);
                    }, function (response) {
                        console.log("response");
                        console.log(response);
                        if (response.status === 500) {
                            response.data.message = "Error while uploading bulk data.Please try after sometime or please check that file contains data in proper form.";
                        }
                        defer.reject({
                            reason: response.data.message
                        });
                    });
                    return defer.promise;
                };

            function lockProperties(data) {
                return Object.seal(data);
            }
            function InvalidDataFormatError(message) {
                this.message = message;
            }
            InvalidDataFormatError.prototype = new Error();

            my.productResource = shoalAdmin_manageProducts_ProductsResource;
            my.productClass = shoalCommon_classes_ProductOffer;
            my.product = {};
            my.saveProduct = function () {
                my.product.getProduct().vatRate = {
                    code: my.product.getProduct().vatRate,
                    rate: 0.00
                };
                console.log("saving product form");
                priceBandLength = my.product.getProduct().priceBands.length - 1;
                if (!my.product.getProduct().priceBands[priceBandLength].unbounded) {
                    my.product.status.isError = true;
                    my.product.status.errorMessage = "Please check the checkbox of Unbounded in last Price bracket information.";
                } else {
                    my.productResource.save(my.product.getProduct(), function (response) {
                        console.log("product form saved");
                        my.product.status.isSaved = true;
                        my.product.status.isError = false;

                        // rebind form
                        //my.product = angular.merge(my.product, response);
                    }, function (error) {
                        console.log("product form save failed : " + JSON.stringify(error));
                        my.product.status.isSaved = false;
                        my.product.status.isError = true;
                        if (error.data) {
                            my.product.status.errorMessage = error.data.message;
                        }
                    });
                }
            };
            my.editProduct = function (productForm) {
                var defer = $q.defer();
                console.log("editing product form");
                priceBandLength = productForm.getProduct().priceBands.length - 1;
                if (!productForm.getProduct().priceBands[priceBandLength].unbounded) {
                    productForm.status.isError = true;
                    productForm.status.errorMessage = "Please check the checkbox of Unbounded in last Price bracket information.";
                } else {
                    productFormEditTimeSave = productForm.getProduct();
                    productFormEditTimeSave.vatRate = {
                        code: productFormEditTimeSave.vatRate,
                        rate: 0.00
                    };
                    my.productResource.edit(productFormEditTimeSave, function (response) {
                        console.log("product form saved");
                        productForm.status.isSaved = true;
                        productForm.status.isError = false;
                        //productForm = angular.merge(my.product, response);
                        defer.resolve(productForm);
                    }, function (error) {
                        console.log("product form edit failed : " + JSON.stringify(error));
                        productForm.status.isSaved = false;
                        productForm.status.isError = true;
                        if (error.data) {
                            productForm.status.errorMessage = error.data.message;
                        }
                        defer.resolve(productForm);
                    });
                    return defer.promise;
                }
            };
            my.buildProduct = function () {
                my.product = my.productClass();
                my.product.specifications.addItem();
                my.product.categories.push('');
                my.product.images.addItem();
                my.product.priceBands.addItem();
                my.product.save = my.saveProduct;
                my.product.edit = my.editProduct;
                my.product.status = {
                    isSaved: false,
                    isError: false,
                    errorMessage: ''
                };
                console.log('building product form');
                lockProperties(my.product);
                return my.product;
            };
            my.vendor = {};
            my.vendor.form = {};
            my.VendorForm = function () {
                my.vendor.form = {
                    vendor: {
                        vendorName: ''
                    },
                    isSaved: false,
                    isError: false,
                    errorMessage: '',
                    $promise: {},
                    $resolved: {}
                };
                my.vendor.save = my.vendorSave;
                return my.vendor;
            };

            return Object.create({}, {
                buildProduct: {
                    value: my.buildProduct
                },
                fetchProductCategories: {
                    value: my.fetchProductCategories
                },
                VendorForm: {
                    value: my.VendorForm
                },
                vendorSave: {
                    value: vendorSave
                },
                fetchAllProduct: {
                    value: fetchAllProduct
                },
                uploadBulkUploadProductsFile: {
                    value: uploadBulkUploadProductsFile
                },
                deleteProduct: {
                    value: deleteProduct
                }
            });
        });
}());