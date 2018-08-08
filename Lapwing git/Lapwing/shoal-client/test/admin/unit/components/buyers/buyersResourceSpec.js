(function () {
    'use strict';
    describe('shoalAdmin.buyers module -> shoalAdmin_buyers_buyersResource', function () {
        var BuyerResource,
            $httpBackend,
            activateBuyerEndpointMatch = /https:\/\/[\w\.]*\/ws\/admin\/buyers\/[\w@\.]+\/activateBuyer/,
            fetchAllUsersEndpointMatch = /https:\/\/[\w\.]*\/ws\/admin\/buyers\?role=INACTIVE/;


        beforeEach(function () {
            BuyerResource = quickmock({
                providerName: 'shoalAdmin_buyers_buyersResource',
                moduleName: 'shoalAdmin.buyers',
                mockModules: ['shoalAdmin.ShoalAdminMocks']
            });
            $httpBackend = BuyerResource.$mocks.$httpBackend;
        });

        it("should be loaded", function () {
            //spec body

            expect(BuyerResource.isLoaded()).toBe(true, "should be loaded");
        });

        describe("when making buyer", function () {

            afterEach(function () {
                BuyerResource.activateBuyer({
                    id: "rogerwatkins@gmail.com"
                }, null);
                $httpBackend.flush();
                $httpBackend.verifyNoOutstandingExpectation();
                $httpBackend.verifyNoOutstandingRequest();
            });

            it("should PUT to server", function () {
                $httpBackend.expectPUT(activateBuyerEndpointMatch).respond(200);
            });
        });

        describe("when fetching users with no role", function () {

            afterEach(function () {
                BuyerResource.fetchInactiveBuyers();
                $httpBackend.flush();
                $httpBackend.verifyNoOutstandingExpectation();
                $httpBackend.verifyNoOutstandingRequest();
            });

            it("should GET from the server", function () {
                $httpBackend.expectGET(fetchAllUsersEndpointMatch).respond(200);
            });

        });

    });
}());