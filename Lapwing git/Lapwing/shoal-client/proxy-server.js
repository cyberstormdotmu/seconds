/*global require, console */
(function () {
    'use strict';

    var port = 8443,
        http = require('http'),
        httpProxy = require('http-proxy'),
        url = require('url'),
        proxy = httpProxy.createProxyServer({});

    proxy.on('error', function (err, req, res) {
        console.log('handled error' + err);
    });

    http.createServer(function (req, res) {

        var path = url.parse(req.url).pathname,
            target = 'http://localhost:8000',
            newPath = path;
        if (path !== undefined && path.lastIndexOf('/ws', 0) === 0) {
            target = 'http://localhost:9090';
        }
        // we want to use /login and /public interchangably for now
        if (path.indexOf('/login') === 0) {
            newPath = path.replace('/login', '/public');
        }
        req.url = url.parse(req.url).path.replace(path, newPath);
        console.log(path + "-> " + target + newPath);

        proxy.web(req, res, {
            target: target
        });

    }).listen(port);

    console.log("proxy initialised on port " + port);

}());