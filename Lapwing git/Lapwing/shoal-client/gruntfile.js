/*global module, require, grunt */
module.exports = function (grunt) {
    'use strict';
    var buildParms = grunt.file.readJSON('buildParms.json'),
        environments = grunt.file.readJSON('environments.json');
    // Project configuration.
    grunt.config.init({
        hub: {
            modules: {
                src: ['**/*-gruntfile.js'],
                tasks: ['default'],
                options: {
                    concurrent: 1
                }
            },
            watch: {
                src: ['*gruntfile.js', '**/*-gruntfile.js', '!vendor-gruntfile.js'],
                tasks: ['watch'],
                options: {
                    allowSelf: true,
                    concurrent: 5 // this has to be kept in sync with the number of modules
                }
            }
        },
        copy: {
            dev: {
                expand: true,
                cwd: '<%= buildParms.buildPath %>',
                src: ['**/*'],
                dest: '<%= buildParms.distPath %>/dev'
            },
            ci: {
                expand: true,
                cwd: '<%= buildParms.buildPath %>',
                src: ['**/*', '!**/*.js', '!**/*.js.map', '**/*.min.js'],
                dest: '<%= buildParms.distPath %>/ci'
            },
            demo: {
                expand: true,
                cwd: '<%= buildParms.buildPath %>',
                src: ['**/*', '!**/*.js', '!**/*.js.map', '**/*.min.js'],
                dest: '<%= buildParms.distPath %>/demo'
            }
        },
        jslint: {
            src: {
                src: [
                    '**/*.js',
                    '!bower_components/**/*.js',
                    '!node_modules/**/*.js',
                    '!node/**/*.js',
                    '!test/**/*.js',
                    '!target/**/*.js',
                    '!birds/**/*.js',
                    '!app/js/*.js',
                    '!admin/js/*.js',
                    '!app/views/shopfront/shopFrontController.js',
                    '!app/views/account/orders/orderDetailController.js',
                    '!admin/views/manageProducts/createProductController.js'
                ],
                directives: {
                    browser: true,
                    unparam: true // do not flag unused parameters as syntax error
                }
            },
            test: {
                src: [
                    'test/**/*.js',
                    '!test/vendor/**/*.js',
                    '!test/selenium/**/*.js'
                ],
                directives: {
                    node: true,
                    unparam: true, // do not flag unused parameters as syntax error
                    predef: [
                        'protractor', 'angular', 'browser', 'element', 'require', 'console', 'inject',
                        '$', '$window',
                        'jasmine', 'describe', 'beforeEach', 'beforeAll', 'afterEach', 'afterAll', 'it', 'by', 'expect',
                        'spyOn',
                        'quickmock', 'SHOAL'
                    ]
                }
            }
        },
        ngconstant: environments.config,
        watch: {
            scripts: {
                files: ['<%= buildParms.buildPath %>/**/*', '*.json'],
                tasks: ['updateDev'],
                options: {
                    spawn: true,
                    livereload: false, // not implemented yet
                    debounceDelay: 1000
                }
            }
        },
        karma: {
            unit: {
                configFile: 'test/karma.conf.ci.js'
            }
        }
    });


    require('load-grunt-tasks')(grunt);
    grunt.loadNpmTasks('grunt-karma');
    grunt.config.merge(buildParms);
    grunt.config.merge(environments);

    grunt.registerTask('default', [
        'jslint',
        'hub:modules',
        'copy',
        'ngconstant',
        'karma'
    ]);

    grunt.registerTask('nolint', [
        'hub:modules',
        'copy',
        'ngconstant'
    ]);

    grunt.registerTask('updateDev', [
        'copy:dev',
        'ngconstant:dev'
    ]);

    grunt.registerTask('dev', [
        'hub:modules',
        'updateDev'
    ]);

    grunt.registerTask('watchAll', [
        'dev',
        'hub:watch'
    ]);
};