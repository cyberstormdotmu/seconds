/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.address')
        .factory('shoalApp_address_AddressService', function ($rootScope, $q, shoalApp_address_AddressResource, shoalApp_profile_ProfileService) {

            var my = {},
                buyerProfile = shoalApp_profile_ProfileService.fetchBuyerProfile();
            my.addressResource = shoalApp_address_AddressResource;
            my.address = {};
            my.address.form = {};
            my.saveAddressForm = function () {
                var that = this,
                    deferred = $q.defer();
                my.addressResource.save(that.address, function (response) {
                    that.isSaved = true;
                    that.isError = false;
                    my.address.form.address = angular.merge(my.address.form.address, response);
                    my.address.form.address.organisationName = buyerProfile.$$state.value.form.organisation.name;
                    $rootScope.$broadcast('newAddressAdd', my.address.form.address);
                    deferred.resolve(my.address.form.address);
                }, function (response) {
                    that.isError = true;
                    deferred.reject(response);
                });
                return deferred.promise;
            };
            my.editAddressForm = function () {
                var that = this,
                    deferred = $q.defer();
                my.addressResource.edit(that.address, function (response) {
                    that.isSaved = true;
                    that.isError = false;
                    my.address.form.address = angular.merge(my.address.form.address, response);
                    my.address.form.address.organisationName = buyerProfile.$$state.value.form.organisation.name;
                    deferred.resolve(my.address.form.address);
                }, function (response) {
                    that.isError = true;
                    deferred.reject(response);
                });
                return deferred.promise;
            };
            my.deleteAddressForm = function (address) {
                var that = this,
                    deferred = $q.defer(),
                    id = that.address.id;
                that.address = {};
                that.address.form = {};
                my.addressResource.delete({id: id}, function (response) {
                    that.isSaved = true;
                    that.isError = false;
                    deferred.resolve(id);
                    console.log(response);
                }, function (response) {
                    that.isError = true;
                    deferred.reject(response);
                });
                return deferred.promise;
            };
            my.buildAddressForm = function () {
                my.address.form = {
                    address: {
                        id: '',
                        departmentName: '',
                        buildingName: '',
                        streetAddress: '',
                        locality: 'GB',
                        postTown: '',
                        postcode: ''
                    },
                    organisationName: '',
                    save: my.saveAddressForm,
                    edit: my.editAddressForm,
                    delete: my.deleteAddressForm,
                    isSaved: false,
                    isError: false,
                    errorMessage: '',
                    $promise: {},
                    $resolved: {}
                };
                return my.address;
            };

            return Object.create({}, {
                buildAddressForm: {
                    value: function () {
                        return my.buildAddressForm();
                    }
                }
            });
        });
}());
