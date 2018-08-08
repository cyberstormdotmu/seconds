package com.ishoal.app.integrationtest.buyer.products;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidBuyerAuthentication;
import static com.ishoal.app.integrationtest.interactions.buyer.ListProducts.listProducts;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

public class ProductListingIT extends AbstractIntegrationTest {

    public static final String LAPTOPS = "Laptops";
    public static final String CONVERTIBLES = "Convertibles";

    @Test
    public void cannotListProductsWhenNotAuthenticated() {
        usingNoAuthentication();
        listProducts(LAPTOPS)
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);
    }

    @Test
    public void canListProductsWhenAuthenticatedAsBuyer() {
        usingValidBuyerAuthentication();
        listProducts(LAPTOPS)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void correctNumberOfProductsAreReturnedWhenSearchingForLaptops() {
        usingValidBuyerAuthentication();
        listProducts(LAPTOPS)
                .then()
                .body("", hasSize(3));
    }

    @Test
    public void productShouldHaveTheCorrectCode() {
        usingValidBuyerAuthentication();
        listProducts(CONVERTIBLES)
                .then()
                .body("[0].code", is("HPSPIRIT"));
    }
}
