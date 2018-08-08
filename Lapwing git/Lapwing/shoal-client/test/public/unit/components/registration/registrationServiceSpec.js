/*global angular, quickmock,  describe, module, beforeEach, afterEach, it, inject, spyOn, jasmine, element, expect, SHOAL */
(function () {
    'use strict';
    describe('shoalPublic.registration module -> registrationService', function () {
        var registrationService,
            registrationForm,
            registrationResource;

        beforeEach(function () {
            registrationService = quickmock({
                providerName: 'shoalPublic_registration_RegistrationService',
                moduleName: 'shoalPublic.registration',
                mockModules: ['shoalApp.ShoalAppMocks']
            });
            registrationResource = registrationService.$mocks.shoalPublic_registration_RegistrationResource;
            registrationForm = registrationService.buildRegistrationForm();
        });

        it('should be loaded', function () {
            //spec body

            expect(registrationService.isLoaded()).toBe(true, 'should be loaded');
        });

        it('should provide a registration form', function () {
            expect(registrationForm).toBeDefined();
        });

        describe("when the registration form is saved", function () {
            beforeEach(function () {
                registrationForm.save();
            });

            it("will send the form to the server", function () {
                expect(registrationResource.save).toHaveBeenCalled();
            });

            describe("if the save is successful", function () {

                it("will update the isSaved flag", function () {
                    expect(registrationForm.isSaved).toBe(true, 'expected isSaved flag to be set');
                    expect(registrationForm.isError).toBe(false, 'expected isError flag to be unset');
                });
            });
        });

        describe("when the registration form save fails", function () {

            beforeEach(function () {
                registrationResource.reject = true;
                registrationResource.responseData = {
                    "error": "Internal Server Error",
                    "exception": "org.springframework.dao.InvalidDataAccessResourceUsageException",
                    "message": "some error message"
                };
                registrationForm.save();
            });

            it("will update the error flag", function () {
                expect(registrationForm.isError).toBe(true, 'expected isError flag to be set');
                expect(registrationForm.isSaved).toBe(false, 'expected isSaved flag to be unset');
            });

            describe("when the form is resubmitted successfully", function () {
                beforeEach(function () {
                    registrationResource.reject = false;
                    registrationForm.save();
                });

                it("will update the isSaved flag", function () {
                    expect(registrationForm.isSaved).toBe(true, 'expected isSaved flag to be set');
                    expect(registrationForm.isError).toBe(false, 'expected isError flag to be unset');
                });
            });
        });
    });
}());