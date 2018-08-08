/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalAdmin.views.manageProducts')
        .controller('shoalAdmin.views.manageProducts.BulkUploadProductsController', function ($parse, $scope, $rootScope, $modalInstance, shoalAdmin_manageProducts_ManageProductsService) {
            var vm = this,
                file,
                uploadUrl = "/fileUpload",
                manageProductsService = shoalAdmin_manageProducts_ManageProductsService;
            vm.close = function () {
                $modalInstance.close();
            };
            vm.bulkUploadProducts = function () {
                file = $scope.bulkUploadProductsFile;
                console.dir(file);
                manageProductsService.uploadBulkUploadProductsFile(file, uploadUrl).then(function (response) {
                    console.log("Bulk product upload success response in 'BulkUploadProductsController.js'.");
                    vm.successMessage = response;
                    vm.close();
                }, function (error) {
                    vm.errorMessage = error.reason;
                });
            };
        });
}());