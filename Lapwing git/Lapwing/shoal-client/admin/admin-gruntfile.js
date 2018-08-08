/*globals module, require */
module.exports = function (grunt) {
    'use strict';
    grunt.file.setBase('../');

    var moduleTasks = grunt.file.readJSON('moduleTasks.json'),
        buildParms = grunt.file.readJSON('buildParms.json');

    // Project configuration.
    grunt.config.init({
        moduleName: "admin",
        buildPath: "<%= buildParms.buildPath %>/<%= moduleName %>"
    });

    require('load-grunt-tasks')(grunt);
    grunt.config.merge(moduleTasks.config);
    grunt.config.merge(buildParms);
    grunt.registerTask('default', moduleTasks.tasks);
};