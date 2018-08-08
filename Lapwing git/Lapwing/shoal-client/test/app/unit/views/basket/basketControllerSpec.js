(function () {
    'use strict';
    describe('shoalApp.views.basket module', function () {
        var basketController,
            vm,
            basketService,
            $modalInstance,
            $scope,
            $rootScope,
            initialiseProvider = function () {
                basketController = quickmock({
                    providerName: 'shoalApp.views.basket.BasketController',
                    providerAs: 'vm',
                    moduleName: 'shoalApp.views.basket',
                    mockModules: ['shoalApp.ShoalAppMocks']
                });
                basketService = basketController.$mocks.shoalApp_basket_BasketService;
                $modalInstance = basketController.$mocks.$modalInstance;
                $scope = basketController.$scope;
                vm = $scope.vm;
                $rootScope = basketController.$rootScope;
            };

        describe('-> BasketController', function () {
            /*jslint nomen: true */
            beforeEach(function () {
                angular.module('ui.bootstrap', []); // not loaded by karma

                initialiseProvider();
            });

            describe('on load an empty basket', function () {

                beforeEach(function () {
                    // trigger digest to fire asynchronous calls such as promises
                    $scope.$digest();
                });

                it('should be available', function () {
                    //spec body

                    expect(vm).toBeDefined('BasketController did not load correctly');
                });

                it('should synchronise the existing basket', function () {

                    expect(basketService.synchroniseBasket).toHaveBeenCalled();
                });
            });

            describe('after load', function () {

                it('the basket should be copied into the scope', function () {
                    var newBasket = {
                        items: [],
                        forEach: function () { return; }
                    };
                    $rootScope.$broadcast('basketUpdated', newBasket);

                    $scope.$digest();

                    expect(vm.basket).toEqual(newBasket, 'basket is not empty');
                });

                describe('when user clicks close', function () {
                    it('should close the modal', function () {

                        vm.close();

                        expect($modalInstance.close).toHaveBeenCalled();
                    });
                });
            });
        });
    });
}());