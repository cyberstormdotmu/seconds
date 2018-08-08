(function () {
    'use strict';
    describe('shoalApp.profile module -> shoalApp_profile_ProfileResource', function () {
        var ProfileResource,
            $httpBackend,
            profileEndpointMatch = /\w*\/ws\/profile/;


        beforeEach(function () {
            ProfileResource = quickmock({
                providerName: 'shoalApp_profile_ProfileResource',
                moduleName: 'shoalApp.profile',
                mockModules: ['shoalApp.ShoalAppMocks']
            });
            $httpBackend = ProfileResource.$mocks.$httpBackend;
        });

        it("should be loaded", function () {
            //spec body

            expect(ProfileResource.isLoaded()).toBe(true, "should be loaded");
        });

        describe("when getting resource", function () {

            afterEach(function () {
                ProfileResource.get();
                $httpBackend.flush();
                $httpBackend.verifyNoOutstandingExpectation();
                $httpBackend.verifyNoOutstandingRequest();
            });

            it("should GET from server", function () {
                $httpBackend.expectGET(profileEndpointMatch).respond(200);
            });
        });

        describe("when saving resource", function () {

            afterEach(function () {
                ProfileResource.save();
                $httpBackend.flush();
                $httpBackend.verifyNoOutstandingExpectation();
                $httpBackend.verifyNoOutstandingRequest();
            });

            it("should PUT to server", function () {
                $httpBackend.expectPUT(profileEndpointMatch).respond(200);
            });
        });

    });
}());