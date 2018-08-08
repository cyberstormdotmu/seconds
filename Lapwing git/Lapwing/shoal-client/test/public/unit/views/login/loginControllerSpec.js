(function () {
    'use strict';

    describe('shoalPublic.views.login module', function () {

        var currentUser = {},
            LoginController,
            $rootScope,
            $window,
            Auth;

        beforeEach(function () {
            LoginController = quickmock({
                providerName: 'shoalPublic.views.login.LoginController',
                moduleName: 'shoalPublic.views.login',
                mockModules: ['shoalPublic.ShoalPublicMocks', 'shoalApp.ShoalAppMocks']
            });
            $window = LoginController.$mocks.$window;
            Auth = LoginController.$mocks.shoalCommon_security_AuthService;
            $rootScope = LoginController.$rootScope;
            currentUser = Auth.getCurrentUser();
        });

        describe('login controller', function () {

            it('should be loaded', function () {
                //spec body

                expect(LoginController).toBeDefined();
            });

            it("should clear the current user", function () {

                expect(Auth.clearCurrentUser).toHaveBeenCalled();
            });

            it("should save the current user", function () {

                expect(LoginController.currentUser).toBeDefined();
                expect(LoginController.currentUser).toEqual(currentUser);
            });

            describe("when the login button is clicked", function () {
                beforeEach(function () {
                    LoginController.login();

                    $rootScope.$digest();
                });

                it('should invoke the Auth service', function () {

                    expect(Auth.attemptLogin).toHaveBeenCalled();
                });

                it('should redirect the user if authentication successful', function () {
                    expect($window.location.replace).toHaveBeenCalled();
                });
            });
        });
    });
}());