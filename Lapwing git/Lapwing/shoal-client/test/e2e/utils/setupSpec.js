(function () {
    'use strict';

    var setupSpec = function () {
        var self = {};
        console.log("loading abort spec function");

        self.isSpecAborted = false;
        self.reason = '';

        self.abort = function (reason) {
            self.isSpecAborted = true;
            console.error('ABORT SPEC :- ' + reason);
            self.reason = reason;
        };

        /**
         * Reset the abort status before triggering the set up function (if any)
         * Usage : use on beforeAll() before any other set up function
         */
        self.reset = function (fn) {

            return function () {
                self.isSpecAborted = false;
                self.reason = '';
                if (fn) {
                    fn();
                }
            };
        };

        /**
         * Reset the abort status before triggering the set up function (if any)
         * Usage : use on beforeAll() before any other set up function
         * Enable the jasmine done() function for syncronising setup functions.
         */
        self.resetDone = function (fn) {

            return function (done) {
                self.isSpecAborted = false;
                self.reason = '';
                if (fn) {
                    fn(done);
                }
            };
        };

        /**
         * Check if spec is ok to carry on.
         * Usage: use on every beforeEach().
         */
        self.continueIfOk = function (fn) {

            return function () {
                // if exceptions thrown then jasmine test will timeout because done() is not called

                try {
                    if (self.isSpecAborted) {
                        expect('ABORT CONDITION DURING SPEC SET UP :- ' + self.reason).toBe('');
                        throw "SKIPPING REST OF SPEC SET UP DUE TO ABORT CONDITION";
                    }
                    // continue if everything ok
                    if (fn) {
                        fn();
                    }
                } catch (e) {
                    console.log(e);
                }
            };
        };

        /**
         * Check if spec is ok to carry on.
         * Usage: use on every beforeEach().
         * Enable the jasmine done() function for syncronising setup functions.
         */
        self.continueIfDone = function (fn) {

            return function (done) {
                // if exceptions thrown then jasmine test will timeout because done() is not called

                try {
                    if (self.isSpecAborted) {
                        expect('ABORT CONDITION DURING SPEC SET UP :- ' + self.reason).toBe('');
                        throw "SKIPPING REST OF SPEC SET UP DUE TO ABORT CONDITION";
                    }
                    // continue if everything ok
                    if (fn) {
                        fn(done);
                    }
                } catch (e) {
                    console.log(e);
                }
            };
        };

        return Object.create(self);
    };

    module.exports = setupSpec();

}());
