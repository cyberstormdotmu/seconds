(function () {
    'use strict';
    describe('shoalApp.views.basket module -> BasketViewModal', function () {
        var BasketViewModal,
            $uibModal,
            basketService;

        beforeEach(function () {
            BasketViewModal = quickmock({
                providerName: 'shoalApp_views_basket_BasketViewModal',
                moduleName: 'shoalApp.views.basket',
                mockModules: ['shoalApp.ShoalAppMocks']
            });
            $uibModal = BasketViewModal.$mocks.$uibModal;
            basketService = BasketViewModal.$mocks.shoalApp_basket_BasketService;
        });

        it('should be loaded', function () {
            //spec body

            expect(BasketViewModal).toBeDefined();
        });

        describe('when the client shows the modal view', function () {

            beforeEach(function () {
                BasketViewModal.show();
            });

            it('will open the bootstrap modal box', function () {
                expect($uibModal.open).toHaveBeenCalled();
            });

            describe('when the client closes the modal explicitly', function () {

                beforeEach(function () {
                    $uibModal.triggerClose();
                });

                it('will synchronise the basket', function () {
                    expect(basketService.synchroniseBasket).toHaveBeenCalled();
                });
            });

            describe('when the modal is closed by any other means', function () {

                beforeEach(function () {
                    $uibModal.triggerLoseFocusClose();
                });

                it('will synchronise the basket', function () {
                    expect(basketService.synchroniseBasket).toHaveBeenCalled();
                });
            });
        });
    });
}());