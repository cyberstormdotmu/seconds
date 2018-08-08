(function () {
    'use strict';

    describe('shoalApp.profile module -> shoalApp_profile_ProfileService', function () {
        var profileService,
            buyerProfile,
            profileResource,
            $rootScope;

        beforeEach(function () {
            profileService = quickmock({
                providerName: 'shoalApp_profile_ProfileService',
                moduleName: 'shoalApp.profile',
                mockModules: ['shoalApp.ShoalAppMocks']
            });

            $rootScope = profileService.$rootScope;
            profileResource = profileService.$mocks.shoalApp_profile_ProfileResource;
            buyerProfile = profileService.buildProfileForm().form;
        });

        function triggerPromise() {
            $rootScope.$digest();
        }

        it('should be loaded', function () {
            //spec body

            expect(profileService.isLoaded()).toBe(true, 'should be loaded');
        });

        it('should provide a profile form', function () {
            expect(buyerProfile).toBeDefined();
        });

        it('should not be possible to change the profile form structure', function () {
            expect(Object.isSealed(buyerProfile)).toBe(true, 'profile form properties should not be changable');
        });

        describe("when the profile data is fetched successfully", function () {
            var fetchedBuyerProfile;
            beforeEach(function () {
                profileResource.responseData = SHOAL.StaticData.buildBuyerProfile();
                profileService.fetchBuyerProfile().then(function (response) {
                    fetchedBuyerProfile = response;
                });

                triggerPromise();
            });

            it("should fetch the form from the server", function () {
                expect(profileResource.get).toHaveBeenCalled();
            });

            it("should return the result to the caller", function () {
                expect(fetchedBuyerProfile).toBeDefined('no buyerProfile defined');
            });

            it('should not be possible to change the profile form structure', function () {
                expect(Object.isSealed(buyerProfile)).toBe(true, 'profile form properties should not be changable');
            });
        });

        describe("when the profile data is fetched unsuccessfully", function () {
            var errorResult;
            beforeEach(function () {
                profileResource.reject = true;
                profileResource.responseData = SHOAL.StaticData.buildASpringFormattedServerException();
                profileService.fetchBuyerProfile().catch(function (response) {
                    errorResult = response;
                });

                triggerPromise();
            });

            it("will return an error", function () {
                expect(errorResult).toBeDefined('no error result given');
            });
        });

        describe("when the profile form is saved", function () {
            beforeEach(function () {
                buyerProfile.save();
            });

            it("will send the form to the server", function () {
                expect(profileResource.save).toHaveBeenCalled();
            });

            describe("if the save is successful", function () {

                it("will update the isSaved flag", function () {

                    expect(buyerProfile.isSaved).toBe(true, 'expected isSaved flag to be set');
                    expect(buyerProfile.isError).toBe(false, 'expected isError flag to be unset');
                });
            });
        });

        describe("when the profile form save fails", function () {
            beforeEach(function () {
                profileResource.reject = true;
                buyerProfile.save();
            });

            it("will update the error flag", function () {
                expect(buyerProfile.isError).toBe(true, 'expected isError flag to be set');
                expect(buyerProfile.isSaved).toBe(false, 'expected isSaved flag to be unset');
            });

            describe("when the form is resubmitted successfully", function () {
                beforeEach(function () {
                    profileResource.reject = false;
                    buyerProfile.save();
                });

                it("will update the isSaved flag", function () {
                    expect(buyerProfile.isSaved).toBe(true, 'expected isSaved flag to be set');
                    expect(buyerProfile.isError).toBe(false, 'expected isError flag to be unset');
                });
            });
        });
    });
}());