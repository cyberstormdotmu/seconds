(function () {
    'use strict';
    describe('shoalApp.views.partial.profile module -> ProfileController', function () {
        var ProfileController,
            userInfo,
            buyerProfile,
            vm;

        beforeEach(function () {
            ProfileController = quickmock({
                providerName: 'shoalApp.views.partial.profile.ProfileController',
                providerAs: 'vm',
                moduleName: 'shoalApp.views.partial.profile',
                mockModules: ['shoalApp.ShoalAppMocks']
            });
            userInfo = ProfileController.$mocks.userInfo;
            buyerProfile = ProfileController.$mocks.buyerProfile;
            vm = ProfileController.$scope.vm;
        });

        it("should be loaded", function () {
            //spec body

            expect(ProfileController.isLoaded()).toBe(true, "should be loaded");
        });

        describe("when the controller is loaded", function () {

            it("should save the user info", function () {
                expect(vm.userInfo).toBeDefined();
                expect(vm.userInfo).toEqual(userInfo);
            });

            it("should save the profile form", function () {
                expect(vm.profile).toBeDefined();
                expect(vm.profile).toEqual(buyerProfile);
            });
        });


    });
}());