(function () {
    'use strict';
    var randomCodeGenerator = function () {

        return function (charsRequired) {
            var characters = new Array(charsRequired),
                i;
            for (i = 0; i < characters.length; i += 1) {
                characters[i] = Math.floor(Math.random() * 63);
            }
            return new Buffer(characters).toString('base64').replace(new RegExp('=', 'g'), '');
        };
    };

    module.exports = randomCodeGenerator();

}());
