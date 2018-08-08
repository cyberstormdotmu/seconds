/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.shopFront')
        .controller('shoalApp.views.shopFront.ShopFrontController', function (buyerProfile, $rootScope, $window) {
            var vm = this;
            $rootScope.registrationTokenDisableBasketIcon = $window.sessionStorage.getItem('registrationToken');
            vm.contact = buyerProfile.form.contact;
            // Carousel
            function owlCarouselLoad() {$(".slider .owl-carousel").owlCarousel({
                loop: true,
                items: 1,
                autoplay: true,
                smartSpeed: 1000
            });
                }
            function owlCarouselImagesLoad() {$(".product-images").owlCarousel({
                loop: true,
                items: 1,
                autoplay: true,
                thumbs: true,
                thumbImage:  true,
                thumbContainerClass: 'owl-thumbs clearfix',
                thumbItemClass: 'owl-thumb-item',
                smartSpeed: 1000
            });
                }

            owlCarouselLoad();
            owlCarouselImagesLoad();
        });
}());
