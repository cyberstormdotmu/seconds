(function () {
    'use strict';
    describe('shoalApp.views.checkout module -> CheckoutController', function () {
        var checkoutPaymentController,
            checkoutService,
            $scope,
            vm;

        beforeEach(function () {
            checkoutPaymentController = quickmock({
                providerName: 'shoalApp.views.checkout.CheckoutPaymentController',
                providerAs: 'vm',
                moduleName: 'shoalApp.views.checkout',
                mockModules: ['shoalApp.ShoalAppMocks']
            });

            checkoutService = checkoutPaymentController.$mocks.shoalApp_checkout_CheckoutService;
            $scope = checkoutPaymentController.$scope;
            vm = $scope.vm;
        });

        describe('on initialisation', function () {

            it('should be loaded', function () {
                expect(checkoutPaymentController).toBeDefined();
            });

            it('should initialise the checkout service', function () {
                expect(checkoutService.fetchCreditBalances).toHaveBeenCalled();
            });

            it("should select card payment by default", function () {
                expect(vm.order.paymentMethod).toBe('Card Payment');
            });
        });
    });
}());