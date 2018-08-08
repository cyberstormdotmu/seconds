package com.ishoal.core.domain;

public class ProductCode {
    public static final ProductCode EMPTY_PRODUCT_CODE = new ProductCode(null);

    private String code;

    public ProductCode(String code) {
        this.code = code;
    }

    public static ProductCode from(String code) {
        if(code == null) {
            return EMPTY_PRODUCT_CODE;
        }
        return new ProductCode(code);
    }

    @Override
    public String toString() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductCode)) {
            return false;
        }
        ProductCode productCode = (ProductCode) o;
        return code.equals(productCode.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}