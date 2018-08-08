(function () {
    'use strict';
    describe('shoalApp.views.account.orders module -> OrderSummaryController', function () {
        var orderSummaryController,
            vm,
            $scope,
            orderService,
            initialise = function () {
                orderSummaryController = orderSummaryController.$initialize();
                $scope = orderSummaryController.$scope;
                vm = $scope.vm;
            };

        beforeEach(function () {
            orderSummaryController = quickmock({
                providerName: 'shoalApp.views.account.orders.OrderSummaryController',
                providerAs: 'vm',
                moduleName: 'shoalApp.views.account.orders',
                mockModules: ['shoalApp.ShoalAppMocks']
            });

            orderService = orderSummaryController.$mocks.shoalApp_orders_OrderService;
        });

        describe('on initialisation', function () {
            beforeEach(function () {
                initialise();
            });

            it('should be loaded', function () {
                expect(orderSummaryController).toBeDefined();
            });

            it('should have vm defined', function () {
                expect(vm).toBeDefined();
            });

            it('should call the order service to fetch the orders', function () {
                expect(orderService.getOrders).toHaveBeenCalled();
            });
        });

        describe('when the orders have been returned', function () {
            beforeEach(function () {
                initialise();
                orderService.returnFromPromise();
            });

            it('should have the orders', function () {
                expect(vm.orders).toBeDefined();
            });
        });
    });
}());