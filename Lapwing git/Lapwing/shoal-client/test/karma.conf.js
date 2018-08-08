/*global module, require */
'use strict';

module.exports = function (config) {
    config.set({

        basePath: '../',

        frameworks: ['jasmine'],

        // deliberately using the source rather than dist vendor scripts here
        // to keep performance up as vendor.js can grow huge !
        files: [
            'bower_components/angular/angular.js',
            'bower_components/angular-resource/angular-resource.js',
            'bower_components/angular-ui-router/release/angular-ui-router.js',
            'bower_components/angular-mocks/angular-mocks.js',
            'bower_components/angular-bootstrap/ui-bootstrap.js',
            'bower_components/ngSmoothScroll/lib/angular-smooth-scroll.js',
            '**/*Module.js',
            'app/**/*.js',
            'app/*.js',
            'app/*.js',
            'admin/*.js',
            'admin/**/*.js',
            'shared/*.js',
            'shared/**/*.js',
            'public/*.js',
            'public/**/*.js',
            'test/vendor/*.js', // test helper scripts
            'test/**/unit/global.js', // global namespace
            'test/**/unit/**/*.js',
            'test/**/unit/*.js'

        ],

        exclude: [
            'target/build/**/*.min.js',
            'app/*gruntfile.js',
            'admin/*gruntfile.js',
            'shared/*gruntfile.js',
            'public/*gruntfile.js',
            'node/**/*Module.js'
        ],

        autoWatch: true,

        //browsers : ['Chrome'],

        browsers: ['Chrome'],

        customLaunchers: {
            Chrome_small: {
                base: 'Chrome',
                flags: [
                    '--window-size=600,200',
                    '--window-position=0,0'
                ]
            }
        },

        plugins: [
            'karma-phantomjs-launcher',
            'karma-chrome-launcher',
            'karma-firefox-launcher',
            'karma-jasmine',
            'karma-junit-reporter',
            'karma-ng-html2js-preprocessor',
            'karma-coverage'
        ],

        reporters: ['progress', 'junit', 'coverage'],

        junitReporter: {
            outputDir: 'target/surefire-reports',
            suite: 'unit'
        },
        preprocessors: {
            'app/*/**/*.js': ['coverage'],
            'admin/*/**/*.js': ['coverage'],
            'shared/*/**/*.js': ['coverage'],
            'public/*/**/*.js': ['coverage']
        },
        coverageReporter: {
            reporters: [
                { type: 'html', dir: 'target/coverage' },
                { type: 'lcov', dir: 'target/coverage' }
            ]
        },
        // configure directive html template preprocessor to rewrite the path used by the cache.
        ngHtml2JsPreprocessor: {
            moduleName: 'templates',
            stripPrefix: 'app/'
        }

    });
};
