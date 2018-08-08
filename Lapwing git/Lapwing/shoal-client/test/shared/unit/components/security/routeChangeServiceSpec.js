'use strict';

describe('shoalApp.security -> Route Change Service', function () {
    var RouteChangeService,
        $rootScope,
        Auth;

    beforeEach(function () {
        RouteChangeService = quickmock({
            providerName: 'shoalCommon_security_RouteChangeService',
            moduleName: 'shoalCommon.security',
            mockModules: ['shoalApp.ShoalAppMocks']
        });
        $rootScope = RouteChangeService.$rootScope;
        Auth = RouteChangeService.$mocks.shoalCommon_security_AuthService;
    });

    describe('Login redirect service', function () {

        describe('On load', function () {

            it('should be loaded', function () {
                //spec body

                expect(RouteChangeService).toBeDefined();
            });

            it("should do nothing when no scope passed", function () {

                RouteChangeService.handleRouteChange();
            });
        });

        describe('when the route changes', function () {
            var currentUser;

            beforeEach(function () {

                currentUser = Auth.getCurrentUser();
                currentUser.authenticated = false;

                RouteChangeService.handleRouteChange();

                var next = {
                    data: {
                        access: {
                            requiresAuthorisation: true
                        }
                    }
                };

                $rootScope.$broadcast('$stateChangeStart', next);

                $rootScope.$digest();
            });

            it('should check if the user is authenticated', function () {
                expect(Auth.isAuthenticated).toHaveBeenCalled();
            });

            it('should log the user out if not authenticated', function () {
                expect(Auth.attemptLogout).toHaveBeenCalled();
            });
        });

    });
});