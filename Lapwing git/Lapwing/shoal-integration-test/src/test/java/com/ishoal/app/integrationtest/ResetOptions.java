package com.ishoal.app.integrationtest;

import com.jayway.restassured.config.RestAssuredConfig;

public class ResetOptions {
    private int port;
    private RestAssuredConfig config;

    public int getPort() {

        return port;
    }

    public void setPort(int port) {

        this.port = port;
    }

    public void setConfig(RestAssuredConfig config) {

        this.config = config;
    }

    public RestAssuredConfig getConfig() {

        return config;
    }
}
