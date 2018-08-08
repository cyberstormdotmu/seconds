package com.ishoal.core.domain;

public class ProductImage {
	private final Long id;
    private final int order;
    private final String url;
    private final String description;

    private ProductImage(Builder builder) {
        order = builder.order;
        url = builder.url;
        description = builder.description;
        id=builder.id;
    }

    public static Builder aProductImage() {
        return new Builder();
    }

    public int getOrder() {
        return order;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
		return id;
	}

	public static final class Builder {
    	private  Long id;
        private int order;
        private String url;
        private String description;

        private Builder() {
        }

        public Builder order(int val) {
            order = val;
            return this;
        }

        public Builder url(String val) {
            url = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }
        public Builder id(Long val) {
            id = val;
            return this;
        }

        public ProductImage build() {
            return new ProductImage(this);
        }
    }
}
