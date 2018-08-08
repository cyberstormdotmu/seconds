/*global angular, console */
(function () {
    'use strict';

    angular.module('shoalAdmin.buyers')
        .factory('shoalAdmin_buyers_BuyerAdminService', function ($http, ENV, $q, shoalAdmin_buyers_buyersResource, shoalAdmin_classes_RegistrationCollection, Notification) {
            var my = {},
                that = {},
                buyerAdminServiceUrl = ENV.webServiceUrl + "/admin/buyers/",
                addAdminServiceUrl = ENV.webServiceUrl + "/admin/buyers/addAdmin",
                registrationCollectionConstructor = shoalAdmin_classes_RegistrationCollection,

                fetchBuyers = function () {
                    var defer = $q.defer();

                    $http.get(buyerAdminServiceUrl + 'ALL')
                        .then(function (response) {
                            var buyers = response.data;
                            defer.resolve(Object.freeze(buyers));
                        }, function (response) {
                            defer.reject(response);
                        });
                    return defer.promise;
                },
                fetchBuyerDetails = function (id) {
                    var defer = $q.defer();

                    $http.get(buyerAdminServiceUrl + id + '/details')
                        .then(function (response) {
                            var details = response.data;
                            defer.resolve(Object.freeze(details));
                        }, function (response) {
                            defer.reject({
                                reason : response.data.message
                            });
                        });
                    return defer.promise;
                },
                fetchBuyerSupplierDetails = function () {
                    var defer = $q.defer();

                    $http.get(buyerAdminServiceUrl + 'moneyOwedDetails')
                        .then(function (response) {
                            var details = response.data;
                            defer.resolve(Object.freeze(details));
                        }, function (response) {
                            defer.reject({
                                reason : response.data.message
                            });
                        });
                    return defer.promise;
                },
                addNewAdminRequest = function (adminForm) {
                    var defer = $q.defer();
                    console.log(adminForm);
                    $http.post(addAdminServiceUrl, adminForm)
                        .then(function (response) {
                            var details = response.data;
                            defer.resolve(Object.freeze(details));
                        }, function (response) {
                            defer.reject({
                                reason : response.data.message
                            });
                        });
                    return defer.promise;
                },
                updateVendorCredit = function (vendorCreditWithBuyerId) {
                    var defer = $q.defer();
                    console.log(vendorCreditWithBuyerId);
                    $http.put(buyerAdminServiceUrl, vendorCreditWithBuyerId)
                        .then(function (response) {
                            var buyers = response.data;
                            defer.resolve(Object.freeze(buyers));
                        }, function (response) {
                            defer.reject(response);
                        });
                    return defer.promise;
                };

            function setReadOnly(data) {
                return Object.freeze(data);
            }

            function InvalidDataFormatError(message) {
                this.message = message;
            }

            InvalidDataFormatError.prototype = new Error();

            my.profileResource = shoalAdmin_buyers_buyersResource;
            my.authorise = function (buyer, afterAuthorisation) {
                my.profileResource.activateBuyer({
                    id: buyer.emailAddress
                }, null, afterAuthorisation);
            };
            my.confirmaftercontractsigned = function (buyer, afterAuthorisation) {
                my.profileResource.confirmBuyer({
                    id: buyer.emailAddress
                }, null, afterAuthorisation);
            };
            my.cancelauthorise = function (buyer, afterAuthorisation) {
                my.profileResource.deactivateBuyer({
                    id: buyer.emailAddress
                }, null, afterAuthorisation);
            };
            my.resendemail = function (buyer, afterAuthorisation, afterAuthorisationFail) {
                my.profileResource.resendEmail({
                    id: buyer.emailAddress
                }, null, afterAuthorisation, afterAuthorisationFail);
            };
            my.refresh = function (data, afterRefresh) {
                my.profileResource.fetchInactiveBuyers(function (response) {
                    if (response.registrations) {
                        data.registrations = []; // wipe old data
                        try {
                            response.registrations.forEach(function (value) {
                                data.registrations.push(
                                    angular.merge(my.buildRegistration(), value)
                                );
                            });
                        } catch (e) {
                            console.log("unable to process response " + JSON.stringify(response));
                            throw new InvalidDataFormatError("data received from server was not in valid format:" + e);
                        }
                    }
                    if (afterRefresh) {
                        afterRefresh(data);
                    }
                });

                my.profileResource.fetchConfirmedBuyers(function (response) {

                    if (response.registrations) {
                        data.confirmregistrations = []; // wipe old data
                        try {
                            response.registrations.forEach(function (value) {
                                data.confirmregistrations.push(
                                    angular.merge(my.buildRegistration(), value)
                                );
                            });
                        } catch (e) {
                            console.log("unable to process response " + JSON.stringify(response));
                            throw new InvalidDataFormatError("data received from server was not in valid format:" + e);
                        }
                    }
                    if (afterRefresh) {
                        afterRefresh(data);
                    }
                });

                my.profileResource.fetchRejectedBuyers(function (response) {

                    if (response.registrations) {
                        data.rejectregistrations = []; // wipe old data
                        try {
                            response.registrations.forEach(function (value) {
                                data.rejectregistrations.push(
                                    angular.merge(my.buildRegistration(), value)
                                );
                            });
                        } catch (e) {
                            console.log("unable to process response " + JSON.stringify(response));
                            throw new InvalidDataFormatError("data received from server was not in valid format:" + e);
                        }
                    }
                    if (afterRefresh) {
                        afterRefresh(data);
                    }
                });

                my.profileResource.fetchContractSignPendingBuyers(function (response) {

                    if (response.registrations) {
                        data.contractSignPendingBuyersregistrations = []; // wipe old data
                        try {
                            response.registrations.forEach(function (value) {
                                data.contractSignPendingBuyersregistrations.push(
                                    angular.merge(my.buildRegistration(), value)
                                );
                            });
                        } catch (e) {
                            console.log("unable to process response " + JSON.stringify(response));
                            throw new InvalidDataFormatError("data received from server was not in valid format:" + e);
                        }
                    }
                    if (afterRefresh) {
                        afterRefresh(data);
                    }
                });

                my.profileResource.fetchAuthenticationPendingBuyers(function (response) {

                    if (response.registrations) {
                        data.authenticationPendingBuyersregistrations = []; // wipe old data
                        try {
                            response.registrations.forEach(function (value) {
                                data.authenticationPendingBuyersregistrations.push(
                                    angular.merge(my.buildRegistration(), value)
                                );
                            });
                        } catch (e) {
                            console.log("unable to process response " + JSON.stringify(response));
                            throw new InvalidDataFormatError("data received from server was not in valid format:" + e);
                        }
                    }
                    if (afterRefresh) {
                        afterRefresh(data);
                    }
                });
            };

            that.fetchUnauthorisedBuyers = function () {
                var defer = $q.defer(),
                    registrationCollection = registrationCollectionConstructor({}, my);

                registrationCollection.refresh(function afterCreated(data) {
                    setReadOnly(registrationCollection.registrations);
                    setReadOnly(registrationCollection.confirmregistrations);
                    setReadOnly(registrationCollection.rejectregistrations);
                    setReadOnly(registrationCollection.contractSignPendingBuyersregistrations);
                    setReadOnly(registrationCollection.authenticationPendingBuyersregistrations);
                    defer.resolve(registrationCollection);
                });
                return defer.promise;
            };

            return {
                that: that,
                addNewAdminRequest: addNewAdminRequest,
                fetchBuyers: fetchBuyers,
                fetchBuyerSupplierDetails: fetchBuyerSupplierDetails,
                fetchBuyerDetails: fetchBuyerDetails,
                updateVendorCredit: updateVendorCredit
            };
        });
}());
