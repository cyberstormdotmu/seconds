package com.ishoal.ws.common.util;

import java.net.URI;
import java.net.URISyntaxException;

public class UriHelper {

    private UriHelper() {
        super();
    }

    public static URI uri(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Location URI not valid", e);
        }
    }
}
