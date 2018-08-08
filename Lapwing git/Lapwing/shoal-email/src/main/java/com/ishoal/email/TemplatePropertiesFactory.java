package com.ishoal.email;

public final class TemplatePropertiesFactory {

    private String webRootUrl;

    public TemplatePropertiesFactory(String webRootUrl) {

        this.webRootUrl = webRootUrl;
    }

    public TemplateProperties createProperties() {

        TemplateProperties properties = new TemplateProperties();

        populateGlobalProperties(properties);
        return properties;
    }

    private void populateGlobalProperties(TemplateProperties properties) {

        properties.addProperty(TemplateProperties.WEBSITE_ROOT_URL, webRootUrl);
    }
}
