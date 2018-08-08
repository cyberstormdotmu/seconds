/*global module, require */
'use strict';

var base = require('./karma.conf.js');
module.exports = function (config) {
    // import base config
    base(config);
    config.set({

        browsers: ['PhantomJS'],

        singleRun: true,

        logLevel: config.LOG_DEBUG
    });
};
