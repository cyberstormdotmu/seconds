/*global angular, quickmock,  describe, module, beforeEach, afterEach, it, inject, spyOn, jasmine, element, expect, SHOAL */
(function () {
    'use strict';
    describe('shoalCommon.pageAnchor module -> shoPageAnchor', function () {
        var shoPageAnchor;

        beforeEach(function () {
            shoPageAnchor = quickmock({
                providerName: 'shoPageAnchor',
                moduleName: 'shoalCommon.pageAnchor',
                mockModules: ['shoalApp.ShoalAppMocks'],
                html: '<a sho-page-anchor location="an-id" >link text</a>'
            });
            shoPageAnchor.$compile();
        });

        it('should be loaded', function () {
            //spec body

            expect(shoPageAnchor).toBeDefined();
        });
    });
}());