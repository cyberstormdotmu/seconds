/*global angular, quickmock, describe, module, beforeEach, afterEach, it, inject, spyOn, jasmine, element, expect, SHOAL */
(function () {
    'use strict';

    describe('shoalPublic.views.registration module', function () {

        var RegistrationController,
            registrationForm,
            vm;

        beforeEach(function () {
            RegistrationController = quickmock({
                providerName: 'shoalPublic.views.registration.RegistrationController',
                providerAs: 'vm',
                moduleName: 'shoalPublic.views.registration',
                mockModules: ['shoalApp.ShoalAppMocks']
            });
            registrationForm = RegistrationController.$mocks.registrationForm;
            vm = RegistrationController.$scope.vm;
        });

        describe('registration controller', function () {

            it('should be loaded', function () {
                //spec body

                expect(vm).toBeDefined();
            });

            it("should save the registration form", function () {

                expect(vm.registrationForm).toEqual(registrationForm);
            });
        });
    });
}());