/*global angular, quickmock,  describe, module, beforeEach, afterEach, it, inject, spyOn, jasmine, element, expect, SHOAL */
(function () {
    'use strict';
    describe('shoalPublic.registration module -> shoalPublic_registration_RegistrationService', function () {
        var RegistrationResource,
            $httpBackend,
            registrationEndpointMatch = /\w*\/ws\/registration/;


        beforeEach(function () {
            RegistrationResource = quickmock({
                providerName: 'shoalPublic_registration_RegistrationResource',
                moduleName: 'shoalPublic.registration',
                mockModules: ['shoalApp.ShoalAppMocks']
            });
            $httpBackend = RegistrationResource.$mocks.$httpBackend;
        });

        it("should be loaded", function () {
            //spec body

            expect(RegistrationResource.isLoaded()).toBe(true, "should be loaded");
        });

        describe("when saving resource", function () {

            afterEach(function () {
                RegistrationResource.save();
                $httpBackend.flush();
                $httpBackend.verifyNoOutstandingExpectation();
                $httpBackend.verifyNoOutstandingRequest();
            });

            it("should PUT to server", function () {
                $httpBackend.expectPOST(registrationEndpointMatch).respond(200);
            });
        });

    });
}());