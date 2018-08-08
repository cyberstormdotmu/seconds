package com.ishoal.core.buyer;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import com.ishoal.common.PayloadResult;
import com.ishoal.common.util.ErrorType;
import com.ishoal.core.domain.Product;

public class ProductDatabaseExceptionHandler {

    public static final String UNIQUE_PRODUCT_CODE = "UQ_PRODUCTS_PRODUCT_CODE";
    
    public ProductDatabaseExceptionHandler(){
    }
    
    public PayloadResult<Product> handleDatabaseException(Product product, DataIntegrityViolationException dbException)
    {
         PayloadResult<Product> result;
         Throwable cause = dbException.getCause();
         if (cause instanceof ConstraintViolationException) {
                ConstraintViolationException exception = (ConstraintViolationException) dbException.getCause();

                result = checkUniqueDepartmentnameViolation(product, exception);
            } else {
                throw dbException;
            }
            return result;
    }
    
    PayloadResult<Product> checkUniqueDepartmentnameViolation(Product product,
            ConstraintViolationException exception) {

            PayloadResult<Product> result = null;
            if (UNIQUE_PRODUCT_CODE.equalsIgnoreCase(exception.getConstraintName())) {
                result = PayloadResult.error(ErrorType.CONFLICT, String.format("PRODUCT '%s' already existing",
                        product.getCode()));
            }
            return result;
        }
}
