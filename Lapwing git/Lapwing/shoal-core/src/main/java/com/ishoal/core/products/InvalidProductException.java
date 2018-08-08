package com.ishoal.core.products;

import com.ishoal.core.domain.ProductCode;

public class InvalidProductException extends RuntimeException {

    public InvalidProductException(ProductCode code) {

        super("Product " + code + " is invalid");
    }
}
