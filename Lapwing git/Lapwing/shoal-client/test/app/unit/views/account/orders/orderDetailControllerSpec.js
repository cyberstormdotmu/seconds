(function () {
    'use strict';
    describe('shoalApp.views.account.orders module -> OrderDetailController', function () {
        var orderDetailController,
            vm,
            $scope,
            $stateParams,
            orderService,
            initialise = function () {
                orderDetailController = orderDetailController.$initialize();
                $scope = orderDetailController.$scope;
                vm = $scope.vm;
            };

        beforeEach(function () {
            orderDetailController = quickmock({
                providerName: 'shoalApp.views.account.orders.OrderDetailController',
                providerAs: 'vm',
                moduleName: 'shoalApp.views.account.orders',
                mockModules: ['shoalApp.ShoalAppMocks']
            });

            orderService = orderDetailController.$mocks.shoalApp_orders_OrderService;
            $stateParams = orderDetailController.$mocks.$stateParams;
            $stateParams.orderReference = 'P22Q-8HG-MBA-M76A';
        });

        describe('on initialisation', function () {
            beforeEach(function () {
                initialise();
            });

            it('should be loaded', function () {
                expect(orderDetailController).toBeDefined();
            });

            it('should have vm defined', function () {
                expect(vm).toBeDefined();
            });

            it('should call the order service to fetch the order', function () {
                expect(orderService.getOrder).toHaveBeenCalledWith('P22Q-8HG-MBA-M76A');
            });
        });

        describe('when the order has been returned', function () {
            beforeEach(function () {
                initialise();
                orderService.returnFromPromise();
            });

            it('should have the order', function () {
                expect(vm.order).toBeDefined();
            });
        });

    });
}());