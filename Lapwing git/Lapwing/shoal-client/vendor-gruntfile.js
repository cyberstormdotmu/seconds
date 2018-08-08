/*globals module, require */
module.exports = function (grunt) {
    'use strict';
    var buildParms = grunt.file.readJSON('buildParms.json'),
        config = {
            moduleName: "vendor",
            buildPath: "<%= buildParms.buildPath %>/<%= moduleName %>",
            copy: {
                fontawesome: {
                    expand: true,
                    cwd: 'bower_components/fontawesome',
                    src: ['fonts/*.*'],
                    dest: 'target/build'
                }
            },
            bower_concat: {
                // compile all vendor scripts into one file
                all: {
                    dest: {
                        js: 'target/build/vendor/vendor.js',
                        css: 'target/build/vendor/vendor.css'
                    },
                    mainFiles: {
                        'angular-i18n': 'angular-locale_en-gb.js',
                        'bootstrap': ['dist/css/bootstrap.min.css', 'dist/css/bootstrap-theme.min.css'],
                        'fontawesome': 'css/font-awesome.min.css'
                    },
                    exclude: [
                        'jquery',
                        'html5-boilerplate'
                    ],
                    bowerOptions: {
                        relative: false
                    }
                }
            },
            uglify: {
                vendor: {
                    options: {
                        sourceMap: true,
                        sourceMapName: 'target/build/vendor/vendor.min.js.map'
                    },
                    src: ['target/build/vendor/vendor.js'],
                    dest: 'target/build/vendor/vendor.min.js'
                }
            }
        };

    // Project configuration.
    grunt.config.init(config);
    grunt.config.merge(buildParms);

    require('load-grunt-tasks')(grunt);
    grunt.registerTask('default', ['copy', 'bower_concat', 'uglify']);
};