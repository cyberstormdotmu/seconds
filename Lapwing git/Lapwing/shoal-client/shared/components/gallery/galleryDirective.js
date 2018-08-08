/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalCommon.gallery')
        .directive('shoGallery', function () {
            return {
                restrict: 'A',
                templateUrl: '../shared/components/gallery/galleryView.html',
                scope: {},
                bindToController: {
                    main: '=',
                    thumbnails: '='
                },
                controllerAs: 'gallery',
                controller: function ($scope) {
                    var that = this;
                    that.mainImage = {};

                    // watch the bound variable and copy to a working variable
                    $scope.$watch(function () {
                        return that.main;
                    }, function (newValue) {
                        if (newValue) {
                            console.log('mainimage has updated to ' + JSON.stringify(newValue));
                            that.mainImage = newValue;
                        }
                    });

                    that.changeImage = function (thumb) {
                        if (that.mainImage !== thumb) {
                            that.mainImage = thumb;
                        }
                    };
                }
            };
        });
}());
