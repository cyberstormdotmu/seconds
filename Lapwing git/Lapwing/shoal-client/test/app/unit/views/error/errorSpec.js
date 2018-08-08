(function () {
    'use strict';
    describe('shoalApp.views.checkout module -> CheckoutController', function () {
        var errorController,
            vm,
            $scope,
            initialise = function () {
                errorController = errorController.$initialize();
                $scope = errorController.$scope;
                vm = $scope.vm;
            };

        beforeEach(function () {
            errorController = quickmock({
                providerName: 'shoalApp.views.error.ErrorController',
                providerAs: 'vm',
                moduleName: 'shoalApp.views.error',
                mockModules: ['shoalApp.ShoalAppMocks']
            });

            initialise();
        });

        describe('on initialisation', function () {

            it('should be loaded', function () {
                expect(errorController).toBeDefined();
            });

            it('should provide a logout method', function () {
                expect(vm.logout).toBeDefined();
            });
        });
    });
}());