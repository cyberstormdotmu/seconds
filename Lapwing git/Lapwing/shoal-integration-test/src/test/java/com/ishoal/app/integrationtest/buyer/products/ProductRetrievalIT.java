package com.ishoal.app.integrationtest.buyer.products;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidBuyerAuthentication;
import static com.ishoal.app.integrationtest.interactions.buyer.FetchProduct.fetchProduct;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;


public class ProductRetrievalIT extends AbstractIntegrationTest {

    public static final String PRODUCT_CODE = "HPELITE840";

    @Test
    public void cannotRetrieveAProductWhenNotAuthenticated() {
        usingNoAuthentication();
        fetchProduct(PRODUCT_CODE)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void shouldRetrieveASingleProduct() {
        usingValidBuyerAuthentication();
        fetchProduct(PRODUCT_CODE)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void shouldHaveCorrectProductCode() {
        usingValidBuyerAuthentication();
        fetchProduct(PRODUCT_CODE)
                .then()
                .body("code", is(PRODUCT_CODE));
    }

    @Test
    public void shouldHaveCorrectProductName() {
        usingValidBuyerAuthentication();
        fetchProduct(PRODUCT_CODE)
                .then()
                .body("name", is("HP EliteBook 840 G2 Laptop"));
    }

    
    @Test
    public void shouldHaveProductSpecs() {
        usingValidBuyerAuthentication();
        fetchProduct(PRODUCT_CODE)
                .then()
                .body("specifications", hasSize(14));
    }

    @Test
    public void shouldHaveCategories() {
        usingValidBuyerAuthentication();
        fetchProduct(PRODUCT_CODE)
                .then()
                .body("categories[0]", is("Products"))
                .body("categories[1]", is("Laptops"))
                .body("categories[2]", is("Power User"));
    }
    
    @Test
    public void shouldHaveStock(){
    	 usingValidBuyerAuthentication();
    	 fetchProduct(PRODUCT_CODE)
    	 .then()
    	 .body("stock", is(40000));
    }

    @Test
    public void shouldHaveTermsandConditions(){
    	 usingValidBuyerAuthentication();
    	 fetchProduct(PRODUCT_CODE)
    	 .then()
    	 .body("termsAndConditions", is("OFFER valid for 1 week only"));
    }

}
