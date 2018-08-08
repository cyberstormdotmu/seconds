package com.ishoal.app.integrationtest.buyer.products;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidBuyerAuthentication;
import static com.ishoal.app.integrationtest.interactions.buyer.FetchCategories.fetchCategories;
import static org.hamcrest.Matchers.is;

public class ProductCategoriesIT extends AbstractIntegrationTest {

    @Test
    public void canListProductCategoriesWhenAuthenticatedAsBuyer() {
        usingValidBuyerAuthentication();
        fetchCategories()
            .then()
            .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void productCategoriesShouldBeCorrect() {
        usingValidBuyerAuthentication();
        fetchCategories()
            .then()
            .body("[0]", is("Products"))
            .body("[1]", is("Laptops"))
            .body("[2]", is("Power User"))
            .body("[3]", is("Convertibles"));
    }
}
