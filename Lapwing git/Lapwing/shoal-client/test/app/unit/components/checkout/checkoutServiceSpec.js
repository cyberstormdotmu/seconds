(function () {
    'use strict';

    describe('shoalApp.checkout module -> CheckoutService', function () {
        var subject,
            orderService,
            basketService,
            creditService,
            paymentService,
            initialiseProvider = function () {
                subject = quickmock({
                    providerName: 'shoalApp_checkout_CheckoutService',
                    moduleName: 'shoalApp.checkout',
                    mockModules: ['shoalApp.ShoalAppMocks', 'shoalApp.classes']
                });
                orderService = subject.$mocks.shoalApp_orders_OrderService;
                basketService = subject.$mocks.shoalApp_basket_BasketService;
                paymentService = subject.$mocks.shoalApp_payment_PaymentService;
                creditService = subject.$mocks.shoalApp_credits_CreditService;
            };

        beforeEach(function () {
            initialiseProvider();
            subject.init();
        });

        describe('on initialisation', function () {
            it('should be loaded', function () {
                expect(subject.isLoaded()).toBe(true, 'should be loaded');
            });
        });

        describe('on fetch credit balances', function () {
            beforeEach(function () {
                creditService.availableBalance = 100;
                subject.fetchCreditBalances();
                creditService.returnFromPromise();
            });

            it('should call the credit service to fetch the balances', function () {
                expect(creditService.getCreditBalances).toHaveBeenCalled();
            });

            it('should set the available credit balance', function () {
                expect(subject.availableCreditBalance).toBe(100);
            });
        });

        describe('on calculate maximum credit spend', function () {
            describe('when the credit balance is greater than the basket gross total', function () {
                beforeEach(function () {
                    creditService.availableBalance = 120;
                    subject.fetchCreditBalances();
                    creditService.returnFromPromise();
                    subject.order.basket = { grossTotal : 100 };
                });

                it('should calculate the maximum credit spend as the gross total', function () {
                    expect(subject.maximumCreditSpend).toBe(100);
                });
            });

            describe('when the credit balance is lower than the basket gross total', function () {
                beforeEach(function () {
                    creditService.availableBalance = 90;
                    subject.fetchCreditBalances();
                    creditService.returnFromPromise();
                    subject.order.basket = { grossTotal : 100 };
                });

                it('should calculate the maximum credit spend as the credit balance', function () {
                    expect(subject.maximumCreditSpend).toBe(90);
                });
            });
        });

        describe('on place order with payment card option', function () {
            beforeEach(function () {
                subject.order.paymentMethod = 'Card Payment';
                subject.placeOrder();
                orderService.returnFromPromise();
            });

            it('should call the order service to submit the order', function () {
                expect(orderService.submitOrder).toHaveBeenCalled();
            });

            it('should call the basket service to empty the basket', function () {
                expect(basketService.removeAllFromBasket).toHaveBeenCalled();
            });

            it("should obtain a payment token", function () {
                expect(paymentService.createPaymentCardToken).toHaveBeenCalled();
            });
        });

        describe('on place order with invoice option', function () {
            beforeEach(function () {
                subject.order.paymentMethod = 'On Invoice';
                subject.placeOrder();
                orderService.returnFromPromise();
            });

            it('should call the order service to submit the order', function () {
                expect(orderService.submitOrder).toHaveBeenCalled();
            });

            it('should call the basket service to empty the basket', function () {
                expect(basketService.removeAllFromBasket).toHaveBeenCalled();
            });

            it("should obtain a payment token", function () {
                expect(paymentService.createPaymentCardToken).not.toHaveBeenCalled();
            });
        });
    });
}());