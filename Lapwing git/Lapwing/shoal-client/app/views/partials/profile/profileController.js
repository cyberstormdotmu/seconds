/*global angular, console */
(function () {
    'use strict';
    angular.module('shoalApp.views.partial.profile')
        .controller('shoalApp.views.partial.profile.ProfileController', function ($http, $scope, userInfo, buyerProfile) {
            var vm = this;
            vm.userInfo = userInfo;
            vm.profile = buyerProfile;
            /*$scope.mobilemaxlength = 15;
            $scope.Codelist = [];
            $http.get('https://restcountries.eu/rest/v2/all')
                .then(function (response) {
                    angular.forEach(response.data, function (data) {
                        $scope.Codelist.push(data.callingCodes[0]);
                    });*/
                    /*console.log(vm.profile.form.user.mobileNumber);
                    $scope.countryCode = buyerProfile.form.user.mobileNumber.substring(0, buyerProfile.form.user.mobileNumber.length - 10);
                    buyerProfile.form.user.mobileNumber = buyerProfile.form.user.mobileNumber.substring(vm.profile.form.user.mobileNumber.length - 10, buyerProfile.form.user.mobileNumber.length);
                    $scope.mobilemaxlength = 10;*/
                /*});*/
        });
}());