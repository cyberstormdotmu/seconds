package com.ishoal.core.products;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;

import com.ishoal.common.PayloadResult;
import com.ishoal.core.buyer.ProductDatabaseExceptionHandler;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.repository.ProductRepository;

public class CreateProductService {

    private ProductRepository productRepository;
    private final ProductDatabaseExceptionHandler productDatabaseExceptionHandler = new ProductDatabaseExceptionHandler();

    public CreateProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public Product createProduct(Product product) {
        if (product.isValidForSave()) {
            return productRepository.save(product);
        }
        throw new InvalidProductException(product.getCode());
    }

    @Transactional
    public PayloadResult<Product> updateProduct(Product product) {
        PayloadResult<Product> payload = null;
        Product updateProduct = null;
        if (product.isValidForUpdate()) {
            try {
                updateProduct = productRepository.save(product);
            } catch (DataIntegrityViolationException e) {
                payload = handleDatabaseException(product, e);
            }

        }else {
            throw new InvalidProductException(product.getCode()); 
        }
        if(updateProduct !=null){
            payload = PayloadResult.success(updateProduct);
        }
        return payload != null ? payload : PayloadResult.error("Unexpected error");
    }

    private PayloadResult<Product> handleDatabaseException(Product product,
            DataIntegrityViolationException dbException) {

        return productDatabaseExceptionHandler.handleDatabaseException(product, dbException);
    }

    public boolean getProduct(ProductCode code) {

        boolean isProductExists = false;
        Product product = productRepository.findByCode(code);
        if(product != null){
            isProductExists = true;
        }
        return isProductExists;
    }

    public boolean getProductById(long productId) {
        boolean isProductExists = false;
        Product product = productRepository.findByProductId(productId);
        if(product != null){
            isProductExists = true;
        }
        return isProductExists;
    }

}
