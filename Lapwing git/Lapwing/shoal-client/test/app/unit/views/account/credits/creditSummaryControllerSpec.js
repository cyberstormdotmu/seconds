(function () {
    'use strict';
    describe('shoalApp.views.account.credits module -> CreditSummaryController', function () {
        var creditSummaryController,
            vm,
            $scope,
            creditService,
            initialise = function () {
                creditSummaryController = creditSummaryController.$initialize();
                $scope = creditSummaryController.$scope;
                vm = $scope.vm;
            };

        beforeEach(function () {
            creditSummaryController = quickmock({
                providerName: 'shoalApp.views.account.credits.CreditSummaryController',
                providerAs: 'vm',
                moduleName: 'shoalApp.views.account.credits',
                mockModules: ['shoalApp.ShoalAppMocks']
            });

            creditService = creditSummaryController.$mocks.shoalApp_credits_CreditService;
        });

        describe('on initialisation', function () {
            beforeEach(function () {
                initialise();
            });

            it('should be loaded', function () {
                expect(creditSummaryController).toBeDefined();
            });

            it('should have vm defined', function () {
                expect(vm).toBeDefined();
            });

            it('should call the credit service to fetch the credit balances', function () {
                expect(creditService.getCreditBalances).toHaveBeenCalled();
            });
        });

        describe('when the credit balances have been returned', function () {
            beforeEach(function () {
                creditService.availableBalance = 120;
                creditService.pendingBalance = 30;
                initialise();
                creditService.returnFromPromise();
            });

            it('should have the expected available balance', function () {
                expect(vm.creditBalances.availableCreditBalance).toBe(120);
            });

            it('should have the expected pending balance', function () {
                expect(vm.creditBalances.pendingCreditBalance).toBe(30);
            });
        });

    });
}());