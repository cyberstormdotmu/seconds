(function () {
    'use strict';


    describe('shoalAdmin.buyers module -> shoalAdmin_buyers_BuyerAdminService', function () {
        var buyerAdminService,
            $rootScope,
            profileResource,
            buyerResource = {
                "registrations": [
                    {
                        "buyer": {
                            "firstName": null,
                            "surname": null,
                            "emailAddress": "afngusduh"
                        }
                    },
                    {
                        "buyer": {
                            "firstName": null,
                            "surname": null,
                            "emailAddress": "roger.watkins11@gnomesoft.co.uk"
                        }
                    },
                    {
                        "buyer": {
                            "firstName": null,
                            "surname": null,
                            "emailAddress": "roger.watkins15@gmail.com"
                        }
                    }
                ]
            };

        beforeEach(function () {
            buyerAdminService = quickmock({
                providerName: 'shoalAdmin_buyers_BuyerAdminService',
                moduleName: 'shoalAdmin.buyers',
                mockModules: ['shoalAdmin.ShoalAdminMocks']
            });

            $rootScope = buyerAdminService.$rootScope;
            profileResource = buyerAdminService.$mocks.shoalAdmin_buyers_buyersResource;
        });

        function triggerPromise() {
            $rootScope.$digest();
        }

        it('should be loaded', function () {
            //spec body

            expect(buyerAdminService.isLoaded()).toBe(true, 'should be loaded');
        });

        describe("when the registered buyers are fetched successfully", function () {
            var unauthorised;
            beforeEach(function () {
                profileResource.responseData = buyerResource;
                buyerAdminService.fetchUnauthorisedBuyers().then(function (response) {
                    unauthorised = response;
                });

                triggerPromise();
            });

            it("should fetch the form from the server", function () {
                expect(profileResource.fetchInactiveBuyers).toHaveBeenCalled();
            });

            it("should return the result to the caller", function () {
                expect(unauthorised.registrations).toBeDefined('no registered users defined');
            });

            it('should not be possible to change the profile form structure', function () {
                expect(Object.isFrozen(unauthorised.registrations)).toBe(true, 'registered users properties should not be changable');
            });
        });
    });
}());