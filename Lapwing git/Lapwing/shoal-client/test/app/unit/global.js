/* the only purpose of this file is to set up a single global variable to next all other globals under, rather than
 polluting the entire global namepsace with our variables
 */
'use strict';
var SHOAL = {};

var Stripe = {
    setPublishableKey: function (key) {
        return;
    },
    card: {
        createToken: function (card, callback) {
            callback(200, {id: 'MZPIY22451'});
        }
    }
};