(function () {
    'use strict';

    var jQueryId = function jq(myid) {
        return "#" + myid.replace(/(:|\.|\[|\]|,|@|\+|\/)/g, "\\$1");
    };

    module.exports = jQueryId;

}());


