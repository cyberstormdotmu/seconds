/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.profile')
        .factory('shoalApp_profile_ProfileService', function ($rootScope, $window, $q, shoalApp_profile_ProfileResource) {

            var my = {};

            function lockProperties(data) {
                return Object.seal(data);
            }
            function InvalidDataFormatError(message) {
                this.message = message;
            }

            InvalidDataFormatError.prototype = new Error();

            my.profileResource = shoalApp_profile_ProfileResource;
            my.profile = {};
            my.profile.form = {};
            my.saveProfileForm = function () {
                var that = this;
                if (!that.isCompleted) {
                    my.profileResource.save(that, function (response) {
                        console.log("profile form saved");
                        that.isSaved = true;
                        that.isError = false;
                        console.log(response);
                        // rebind form
                        my.profile.form = angular.merge(my.profile.form, response);

                        $rootScope.$broadcast('buyerProfileChange', my.profile);
                        $window.location.replace('/app');
                    }, function () {
                        console.log("profile form save failed");
                        that.isError = true;
                    });
                } else {
                    my.profileResource.save(that, function (response) {
                        console.log("profile form saved");
                        that.isSaved = true;
                        that.isError = false;
                        console.log(response);
                        // rebind form
                        my.profile.form = angular.merge(my.profile.form, response);

                        $rootScope.$broadcast('buyerProfileChange', my.profile);
                    }, function () {
                        console.log("profile form save failed");
                        that.isError = true;
                    });
                }
                console.log("saving profile form for " + that.organisation.name);
            };

            my.buildProfileForm = function () {
                my.profile.form = {
                    user: {
                        firstName: '',
                        surname: '',
                        emailAddress: '',
                        password: '',
                        mobileNumber: '',
                        westcoastAccountNumber: '',
                        lapwingAccountNumber: '',
                        buyerReferralCode: ''
                    },
                    organisation: {
                        name: '',
                        registrationNumber: '',
                        industry: '',
                        numberOfEmps: ''
                    },
                    contact: {
                        title: '',
                        firstName: '',
                        surname: '',
                        emailAddress: '',
                        phoneNumber: ''
                    },
                    deliveryAddress: {
                        departmentName: '',
                        buildingName: '',
                        streetAddress: '',
                        locality: '',
                        postTown: '',
                        postcode: ''
                    },
                    bankAccount: {
                        accountName: '',
                        sortCode: '',
                        accountNumber: '',
                        bankName: '',
                        buildingName: '',
                        streetAddress: '',
                        locality: '',
                        postTown: '',
                        postcode: ''
                    },
                    addresses: [],
                    save: my.saveProfileForm,
                    isSaved: false,
                    isError: false,
                    isCompleted: false,
                    errorMessage: '',
                    $promise: {},
                    $resolved: {}
                };
                console.log('building profile form');
                console.log(my.profile.form);
                lockProperties(my.profile.form);
                return my.profile;
            };

            my.fetchBuyerProfile = function (useSessionUser) {
                var defer = $q.defer();
                if (useSessionUser === false) {
                    my.profileResource.getLoginUser(
                        function (response) {
                            my.profile = my.buildProfileForm();
                            try {
                                my.profile.form = angular.merge(my.profile.form, response);
                            } catch (e) {
                                console.log("unable to process response " + JSON.stringify(response));
                                throw new InvalidDataFormatError("data received from server was not in valid format:" + e);
                            }

                            defer.resolve(my.profile);
                        },
                        function (response) {
                            if (response) {
                                defer.reject({
                                    reason : response.data.message
                                });
                            } else {
                                defer.reject({
                                    reason: 'the server returned an invalid response'
                                });
                            }
                        }
                    );
                } else {
                    my.profileResource.getSessionUser(
                        function (response) {
                            my.profile = my.buildProfileForm();
                            try {
                                console.log("profile----registrationToken");
                                console.log(response.user.registrationToken);
                                $window.sessionStorage.setItem('registrationToken', response.user.registrationToken);
                                my.profile.form = angular.merge(my.profile.form, response);
                            } catch (e) {
                                console.log("unable to process response " + JSON.stringify(response));
                                throw new InvalidDataFormatError("data received from server was not in valid format:" + e);
                            }

                            defer.resolve(my.profile);
                        },
                        function (response) {
                            if (response) {
                                defer.reject({
                                    reason : response.data.message
                                });
                            } else {
                                defer.reject({
                                    reason: 'the server returned an invalid response'
                                });
                            }
                        }
                    );
                }
                return defer.promise;
            };

            return Object.create({}, {
                buildProfileForm: {
                    value: function () {
                        return my.buildProfileForm();
                    }
                },
                fetchBuyerProfile: {
                    value: function (useSessionUser) {
                        return my.fetchBuyerProfile(useSessionUser);
                    }
                }
            });
        });
}());
