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
    angular.module('shoalApp.ShoalAppMocks', [])
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
        .mockFactory('$log', function ($log) {
            return $log;
        })
        .mockFactory('$state', function ($state) {
            spyOn($state, ['go']).and.callThrough();
            return $state;
        })
        .mockFactory('smoothScroll', function (smoothScroll) {
            return smoothScroll;
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
        .mockFactory('shoalApp_credits_CreditService', function ($rootScope, $q) {
            var creditService = jasmine.createSpyObj('shoalApp_credits_CreditService', ['getCreditBalances', 'saveCreditWithdraw', 'saveCreditApplication']);

            creditService.reject = false;
            creditService.availableBalance = 120;
            creditService.pendingBalance = 30;

            creditService.getCreditBalances.and.callFake(function () {
                var defer = $q.defer();
                if (!creditService.reject) {
                    defer.resolve({
                        availableCreditBalance: creditService.availableBalance,
                        pendingCreditBalance: creditService.pendingBalance
                    });
                } else {
                    defer.reject();
                }
                return defer.promise;
            });

            creditService.saveCreditWithdraw.and.callFake(function () {
                var defer = $q.defer();
                if (!creditService.reject) {
                    defer.resolve(creditService);
                } else {
                    defer.reject();
                }
                return defer.promise;
            });

            creditService.saveCreditApplication.and.callFake(function () {
                var defer = $q.defer();
                if (!creditService.reject) {
                    defer.resolve(creditService);
                } else {
                    defer.reject();
                }
                return defer.promise;
            });

            creditService.returnFromPromise = function () {
                $rootScope.$digest();
            };

            return creditService;
        })
        .mockFactory('shoalApp_payment_PaymentService', function ($q, shoalApp_payment_PaymentService) {
            var paymentService = jasmine.createSpyObj('shoalApp_payment_PaymentService', ['init', 'createPaymentCardToken']);

            paymentService.createPaymentCardToken.and.callFake(function () {
                var defer = $q.defer();
                defer.resolve('tok_17enLICGgFMweSz5faRCz0BD');
                return defer.promise;
            });
            return paymentService;
        })
        .mockFactory('shoalApp_checkout_CheckoutService', function ($q, shoalApp_classes_Order) {
            var checkoutService = jasmine.createSpyObj('shoalApp_checkout_CheckoutService', ['init', 'fetchCreditBalances', 'order', 'placeOrder']);

            checkoutService.order = shoalApp_classes_Order();

            checkoutService.placeOrder.and.callFake(function () {
                var defer = $q.defer();
                defer.resolve();
                return defer.promise;
            });
            return checkoutService;
        })
        .mockFactory('shoalApp_basket_BasketService', function createMockBasketService(shoalApp_classes_Basket) {
            var basketService,
                basket;

            basket = shoalApp_classes_Basket({});

            basketService = jasmine.createSpyObj('shoalApp_basket_BasketService', ['getBasket', 'addItemToBasket', 'synchroniseBasket', 'removeItemFromBasket', 'removeAllFromBasket']);
            basketService.getBasket.and.returnValue(basket);

            return basketService;
        })
        .mockFactory('shoalApp_classes_Basket', function (shoalApp_classes_Basket) {
            return shoalApp_classes_Basket;
        })
        .mockFactory('shoalApp_views_basket_BasketViewModal', function () {
            var basketViewModal = jasmine.createSpyObj('basketViewModal', ['show']);
            return basketViewModal;
        })
        .mockService('shoalCommon_security_LoginRedirectService', function (shoalCommon_security_LoginRedirectService) {
            var redirectService = jasmine.createSpyObj('shoalCommon_security_LoginRedirectService',
                ['redirectToLogin', 'redirectFollowingLogout']);
            redirectService.redirectToLogin.and.callFake(function () {
                console.log("mock redirectToLogin");
            });
            redirectService.redirectFollowingLogout.and.callFake(function () {
                console.log("mock redirectFollowingLogout");
            });
            return redirectService;
        })
        .mockService('shoalCommon_security_AuthService', function ($q) {
            var auth = jasmine.createSpyObj('Auth', ['getCurrentUser', 'attemptLogin', 'attemptLogout', 'isAuthenticated', 'clearCurrentUser']);
            auth.getCurrentUser.and.returnValue(my.currentUser);
            auth.attemptLogin.and.callFake(function () {
                var defer = $q.defer();
                if (my.currentUser.authenticated) {
                    defer.resolve();
                } else {
                    defer.reject();
                }
                return defer.promise;
            });
            auth.isAuthenticated.and.callFake(function () {
                var defer = $q.defer();
                if (my.currentUser.authenticated) {
                    defer.resolve();
                } else {
                    defer.reject();
                }
                return defer.promise;
            });
            return auth;
        })
        .mockFactory('shoalCommon_products_ProductService', function ($q, shoalCommon_classes_ProductOffer) {
            var productService = jasmine.createSpyObj('shoalCommon_products_ProductService', ['findByCode', 'fetchByTopCategory', 'fetchCategory']);
            productService.findByCode.and.callFake(function (productCode) {
                if (!productCode) {
                    throw new Error('no product code supplied');
                }
                var allProducts = SHOAL.StaticData.buildAllProducts(),
                    deferred = $q.defer(),
                    product = shoalCommon_classes_ProductOffer(allProducts[productCode]);

                spyOn(product, 'calculatePricing').and.callThrough();
                deferred.resolve(product);
                return deferred.promise;
            });
            productService.fetchByTopCategory.and.callFake(function (category) {
                if (!category) {
                    throw new Error('no product category supplied');
                }
                var deferred = $q.defer();
                deferred.resolve(SHOAL.StaticData.buildDefaultProductListing());
                return deferred.promise;
            });
            productService.fetchCategory.and.callFake(function (category) {
                if (!category) {
                    throw new Error('no product category supplied');
                }
                var deferred = $q.defer();
                deferred.resolve(SHOAL.StaticData.buildDefaultCategory());
                return deferred.promise;
            });
            return productService;
        })
        .mockFactory('shoalApp_classes_Order', function (shoalApp_classes_Order) {
            return shoalApp_classes_Order;
        })
        .mockFactory('shoalApp_orders_OrderService', function ($rootScope, $q) {
            var orderService = jasmine.createSpyObj('shoalApp_orders_OrderService', ['create', 'submitOrder', 'getOrders', 'getOrder', 'getOrderBalance']),
                reject = false,
                orderReference = 'ABC-123';

            orderService.submitOrder.and.callFake(function () {
                var defer = $q.defer();
                if (!reject) {
                    defer.resolve({reference: orderReference});
                } else {
                    defer.reject();
                }
                return defer.promise;
            });

            orderService.getOrders.and.callFake(function () {
                var defer = $q.defer();
                defer.resolve([]);
                return defer.promise;
            });

            orderService.getOrder.and.callFake(function () {
                var defer = $q.defer();
                defer.resolve([]);
                return defer.promise;
            });

            orderService.getOrderBalance.and.callFake(function () {
                var defer = $q.defer();
                defer.resolve([]);
                return defer.promise;
            });

            orderService.returnFromPromise = function () {
                $rootScope.$digest();
            };

            return orderService;
        })
        .mockFactory('shoalApp_profile_ProfileResource', function () {
            var profileResource = jasmine.createSpyObj('shoalApp_profile_ProfileResource', ['get', 'save']),
                PUT = function (data, success, fail) {
                    if (!profileResource.reject && success) {
                        success(profileResource.responseData);
                    }
                    if (profileResource.reject && fail) {
                        fail({
                            data: profileResource.responseData
                        });
                    }
                },
                GET = function (success, fail) {
                    if (!profileResource.reject && success) {
                        success(profileResource.responseData);
                    }
                    if (profileResource.reject && fail) {
                        fail({
                            data: profileResource.responseData
                        });
                    }
                };
            profileResource.reject = false;
            profileResource.responseData = {};

            profileResource.get.and.callFake(GET);
            profileResource.save.and.callFake(PUT);
            return profileResource;
        })
        .mockFactory('shoalPublic_registration_RegistrationResource', function () {
            var registrationResource = jasmine.createSpyObj('shoalApp_profile_ProfileResource', ['get', 'save']),
                POST = function (data, success, fail) {
                    if (!registrationResource.reject && success) {
                        console.log("calling success");
                        success({
                            data: registrationResource.responseData
                        });
                    }
                    if (registrationResource.reject && fail) {
                        console.log("calling fail");

                        fail({
                            data: registrationResource.responseData
                        });
                    }
                };
            registrationResource.reject = false;
            registrationResource.responseData = {};

            registrationResource.save.and.callFake(POST);
            return registrationResource;
        })
        .mockFactory('buyerProfile', function ($q, shoalApp_profile_ProfileService) {
            var data = shoalApp_profile_ProfileService.buildProfileForm();
            spyOn(data.form, 'save').and.callFake(function () {
                console.log("mock saving profile form");
            });
            console.log("mocking the buyerProfile");
            data.form.isCompleted = true;
            return data;
        })
        .mockFactory('registrationForm', function (shoalPublic_registration_RegistrationService) {
            var form = shoalPublic_registration_RegistrationService.buildRegistrationForm();
            spyOn(form, 'save').and.callFake(function () {
                console.log("mock saving registration form");
            });
            console.log("mocking the registrationForm");
            return form;
        })
        .mockFactory('userInfo', function () {
            return my.currentUser;
        })
        .constant('ENV', {
            webServiceUrl: "https://domain.com/ws"
        })
        .value('$stateParams', {});
}());
