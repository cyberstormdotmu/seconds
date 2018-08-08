(function () {
    'use strict';
    describe('shoalApp.views.checkout module -> CheckoutController', function () {
        var checkoutController,
            checkoutService,
            basketService,
            buyerProfile,
            $state,
            initialise = function () {
                checkoutController = checkoutController.$initialize();
            };
        beforeEach(function () {
            checkoutController = quickmock({
                providerName: 'shoalApp.views.checkout.CheckoutController',
                providerAs: 'vm',
                moduleName: 'shoalApp.views.checkout',
                mockModules: ['shoalApp.ShoalAppMocks']
            });

            checkoutService = checkoutController.$mocks.shoalApp_checkout_CheckoutService;
            basketService = checkoutController.$mocks.shoalApp_basket_BasketService;
            buyerProfile = checkoutController.$mocks.buyerProfile;
            $state = checkoutController.$mocks.$state;
        });

        describe('on initialisation', function () {

            it('should be loaded', function () {
                expect(checkoutController).toBeDefined();
            });

            it('should initialise the checkout service', function () {
                expect(checkoutService.init).toHaveBeenCalled();
            });

            it('should synchronise the basket', function () {
                expect(basketService.synchroniseBasket).toHaveBeenCalled();
            });

            describe("when the user profile is completed", function () {
                beforeEach(function () {
                    buyerProfile.form.isCompleted = true;
                    initialise();
                });

                it('should set the initial state to be the review basket step', function () {
                    expect($state.go).toHaveBeenCalledWith('checkout.basket');
                });
            });


            describe("when the user profile is incomplete", function () {
                beforeEach(function () {
                    buyerProfile.form.isCompleted = false;
                    initialise();
                });

                it('should set the initial state to be the profile step', function () {
                    expect($state.go).toHaveBeenCalledWith('checkout.profile');
                });
            });
        });
    });
}());