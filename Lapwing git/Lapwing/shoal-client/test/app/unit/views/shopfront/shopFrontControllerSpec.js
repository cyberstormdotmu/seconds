(function () {
    'use strict';
    describe('shoalApp.views.shopFront module -> ShopFrontController', function () {
        var ShopFrontController,
            buyerProfile,
            vm;

        beforeEach(function () {
            ShopFrontController = quickmock({
                providerName: 'shoalApp.views.shopFront.ShopFrontController',
                providerAs: 'vm',
                moduleName: 'shoalApp.views.shopFront',
                mockModules: ['shoalApp.ShoalAppMocks']
            });
            buyerProfile = ShopFrontController.$mocks.buyerProfile;
            vm = ShopFrontController.$scope.vm;
        });

        it("should be loaded", function () {
            //spec body

            expect(ShopFrontController.isLoaded()).toBe(true, "should be loaded");
        });

        describe("when the controller is loaded", function () {

            it("should store the contact info from the profile", function () {
                expect(vm.contact).toBeDefined();
                expect(vm.contact).toEqual(buyerProfile.form.contact);
            });
        });


    });
}());