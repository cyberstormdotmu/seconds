/*global angular, console, Stripe */
(function () {
    'use strict';

    angular.module('shoalApp.payment')
        .factory('shoalApp_payment_PaymentService', function (shoalApp_classes_PaymentCard, shoalApp_classes_PaymentDetails, $q) {
            var paymentCard,
                init = function () {
                    paymentCard = shoalApp_classes_PaymentCard();
                },
                createPaymentCardToken = function () {
                    var defer = $q.defer();

                    console.log("calling stripe with:");
                    console.log(paymentCard);

                    Stripe.card.createToken({
                        number: paymentCard.cardNumber,
                        cvc: paymentCard.cvc,
                        exp: paymentCard.expiry,
                        name: paymentCard.cardholderName
                    }, function (status, response) {

                        if (response.error) {
                            console.log('failed to create token for payment');
                            defer.reject({
                                reason: response.error.message
                            });
                        } else {
                            console.log('got payment card token ' + response.id);
                            defer.resolve(response.id);
                        }
                    });

                    return defer.promise;
                };


            return Object.create({}, {
                init: {
                    value: init
                },
                createPaymentCardToken: {
                    value: createPaymentCardToken
                },
                paymentCard: {
                    get: function () {
                        return paymentCard;
                    }
                }
            });
        });
}());