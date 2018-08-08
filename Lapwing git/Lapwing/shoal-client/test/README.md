# Shoal Test Framework

## Unit Tests

Unit tests are written using the [Jasmine](http://jasmine.github.io/2.0/introduction.html) framework, and run using [Karma](https://karma-runner.github.io/0.13/index.html).

Each 'Spec' initialises it's subject module using a custom [Quickmock](https://github.com/tennisgent/quickmock) script which can be found in the vendor directory.

Mocks for the tests are provided in *Mock.js files, which are initialised by Karma and injected into the Spec by
Quickmock.

The karma test runner can be used in CI mode, however due to the pre-processing that goes on in this project you will also
need to run the build in watch mode.

The default browser for the Karma tests is [PhantomJSv1](http://phantomjs.org/).


> ### Shell Commands
> ```
  $ npm run test                 # run the tests in ci mode - continuous.
  ```
  
> ```
  $ npm run test-single-run                # run the tests through once, and exit.
  ```
  
> ```
$ npm run start-watch                #, or 'npm run sw' - build the code and watch for changes - you must do this for continuous test mode to work correctly.
```

> ```
$ npm run start-watch-quick                #, or 'npm run swq' - same as above but don't clear out the build dir first.
```


> ### IntelliJ

* Firstly install the Karma plugin for IntelliJ.

* Ensure Node.js is installed.

* Create a new Run configuration for Karma.

* Select the 'karma.conf.js' file as your configuration file.

* Find node_modules/karma and select that as your karma package.

* Run the Karma test runner.

> #### Configuration :

> * You can trigger the watcher in Karma by selecting the 'auto-test' button on the IntelliJ test runner panel. Note that
you will need to run the 'start-watch' command in order for this to work correctly.

> * You can configure the amount of time Karma will wait for changes to complete before triggering the tests again. Select the gear
icon in the IntelliJ test runner and choose 'Set autotest delay'. 5 seconds is a reasonable setting.
 
> * You can change the browser from PhantomJS to Chrome or Firefox by opening the karma.conf.js and changing the 
browsers option.
 
### Tips:
If you change a lot of files at once, rename them, or introduce new ones. The Karma ci runner might get confused
or not be watching certain files. In this case, simply stop the Karma server and restart it.

If Karma gets really confused. Do the above, but first run 'npm start-watch' to clear out and rebuild the 
code. This clears up the majority of strange issues that occur while unit testing the code.

If you get an error stating that a dependency could not be injected, try looking in the *Mock files.
Is the required component in there ? Be especially careful with value and constant types - these use .value and .constant
in the mock file.

If you are wondering what the $scope.$digest() in many of the Specs does, it triggers a digest cycle in Angular which will
resolve any promises that are lingering in the runtime. If your expectations are not working
as expected, and you are relying on a promise to resolve them, try using $scope.$digest();

Likewise, with $http calls, try using $httpBackend.flush() to resolve them. Or better yet, use $resource in your 
services rather than $http and avoid the whole mess of $httpBackend in the first place.

Notice that your can retrieve various things from the test subject when using Quickmock, such as
$mocks, $scope, $rootScope etc. This is way easier than the default Angular way of injecting these objects
in to your test.



## End to End Tests

End to End tests are written using the [Jasmine](http://jasmine.github.io/2.0/introduction.html) framework, and run using [Protractor](https://angular.github.io/protractor/#/).

Protractor itself is an Angular aware [Selenium](http://www.seleniumhq.org/) wrapper.

The E2E Specs are quite complicated and contain a lot of Asyncronous code. Be aware that commands in this code are subject
to page loads, redirects and angular bootstrapping, and if any of this changes half way through a test, some very strange
error messages can result.

The Protractor tests run against the real code, by default the shell commands will take care of building the code and 
starting the server for you. However the IntelliJ plugin won't and you will need to start the server yourself.

To aid you in debugging errors, take a look at the E2E-test-report.html in the screenshots directory. This provides a useful
summary and will take a screenshot for any failed tests. Use this, and the protractor output to lock down the problem.


### A note about layout / design :

Other than the tests themselves, the test framework splits the E2E code into 3 sections: pageObjects, interactions and utils.

PageObjects are Javascript modules that encompass any layout or page specific code such as element selectors. PageObjects expose
a friendly API for the tests to use which simplifies and eliminates a lot of messy, fragile code.
 
Interactions provide some resuable unit of functionality such as registering a new user. They typically invoke PageObjects
to do their work.

Utils provide non page or test specific units of code that help simplify the tests, such as a wrapper for
selecting from a drop down list.

### A note about Asyncronicity

Generally, Protractor and Selenium will guarantee the ordering of commands sent to the browser and synchronously return control to the test when this is finished.

However in some circumstances, this contract is broken and control is returned to the tests before the element being tested
is available. It is for this reason that the promises have 'leaked' into the tests which is why you'll see a lot of
`element.doSomething().then(function () {}).then(function () {})` like code.

This code ensures that Selenium really has finished processing before attempting to execute the next step in the chain.

For example where you click a button and expect a panel to be visible on screen, you would write :

'button.click().then(function () { // do expectation here }';

The Jasmine2 API has some built in protections for asyncronous behaviour in the form of the done() callback. You
will see this used throghout the E2E tests, where the final promise in the chain invoked the Jasmine done(). This
ensures that the assertions do not try and execute while the setup is still taking place.

> #### Configuration :
> * You can enable or disable browsers in the protractor.conf.js file by modifying the `multiCapabilities` section.
> * By default the E2E tests are configured to run one browser session at a time, to improve reliability. If you require more concurrent performance
you can increase this by modifying the `maxSessions` option.
> * You can configure the browser window size by modifying the `width` and `height` options in the config file.
> * You can configure Protractor to use a selenium server or use the browser drivers directly by changing the `directConnect` option.



> ### Shell Commands
> ```
  $ npm run protractor                 # run the e2e tests once
  ```
  
> ```
  $ npm run start-watch                #, or 'npm run sw' - build the code and watch for changes - you must do this if you are running protractor in your IDE.
  ```
  
> ```
  $ npm run start-watch-quick                #, or 'npm run swq' - same as above but don't clear out the build dir first.
  ```
  
> ### IntelliJ
  
* The first thing to note is that there is no specific Protractor plugin for IntelliJ.

* Ensure you have Node.js installed.

* Install the Node plugin for IntelliJ.

* Create a new Run configuration for Node.js.

> * Choose your node interpreter.
> * Set your node parameters to `node_modules/protractor/bin/protractor`.
> * Set your working directory to `shoal-client` - you will need the absolute path.
> * Set your Javascript file to `test/protractor.conf.js`.
> * Run the Protractor test runner.



### Tips:

If you want to run a specific test rather than the entire suit, use `--specs test/e2e/manageProductE2E.js` in the Node.js
application parameters.

If you get an `Error while waiting for Protractor to sync with the page: "window.angular is undefined` it could be because your
server is not running. Try running `$ npm run start-watch`.

Be careful when doing page redirects between different Angular apps, or doing anything that could cause Angular
to reload, as this causes havoc with Protractor and Selenium when a command or test is currently executing. Sometimes a carefully
placed browser.sleep() can assist in these scenarios.

Notice the difference between the beforeEach and beforeAll syntax in the tests, this is Jasmine's syntax and makes a huge difference to
the performance of the tests. For example, in the unit tests, the beforeEach syntax is used to set up data because it is cheap and
provides good isolation by setting up new data for every test. However in Protractor, taking this route quickly leads
to a very slow test because it may need to do a lot of work to set up each test, in this case using beforeAll is more appropriate
as we can significantly improve the test performance at the cost of less isolation for each test, a classic trade-off.



