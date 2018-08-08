/*global angular, jasmine, spyOn, console, SHOAL */
(function () {
    'use strict';
    angular.module('shoalPublic.ShoalPublicMocks', [])
        .mockFactory('$uibModalInstance', function () {
            return jasmine.createSpyObj('$uibModalInstance', ['dismiss']);
        });
}());
