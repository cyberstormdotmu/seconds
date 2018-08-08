package com.ishoal.email;

import java.util.HashMap;
import java.util.Map;

public class TemplateProperties {

    public static final String USER_FIRST_NAME = "forename";
    public static final String USER_SURNAME = "surname";
    public static final String EMAIL = "email";
    public static final String WEBSITE_ROOT_URL = "web_domain";
    public static final String MOBILENUMBER = "mobileNumber";
    public static final String USER_AS = "USER_ROLE";

    
    private Map<String, String> properties = new HashMap<>();

    // do not make this public, it should only be initialised by the factory class.
    TemplateProperties() {
    }

    public void addProperty(String key, String val) {

        properties.put(key, val);
    }

    public String getValue(String key) {
        return properties.get(key);
    }
}
