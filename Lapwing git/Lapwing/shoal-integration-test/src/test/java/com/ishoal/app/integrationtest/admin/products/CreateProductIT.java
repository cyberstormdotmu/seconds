package com.ishoal.app.integrationtest.admin.products;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import com.ishoal.app.integrationtest.DirtiesDb;
import com.ishoal.app.integrationtest.data.ProductTestData;
import com.ishoal.app.integrationtest.interactions.buyer.FetchProduct;
import org.apache.http.HttpStatus;
import org.junit.Test;

import java.math.BigDecimal;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidAdminAuthentication;
import static com.ishoal.app.integrationtest.admin.products.AdminProductTestData.buildAnAdminProductDto;
import static com.ishoal.app.integrationtest.admin.products.AdminProductTestData.buildAnAdminProductDtoWithInvalidOffer;
import static com.ishoal.app.integrationtest.interactions.admin.CreateAProduct.createAProduct;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

@DirtiesDb
public class CreateProductIT extends AbstractIntegrationTest {

    private static final String PRODUCT_CODE = "HP_SHOLITE_99";

    @Test
    public void shouldCreateAndReturnANewProduct() {
        usingValidAdminAuthentication();

        createAProduct(buildAnAdminProductDto(PRODUCT_CODE).build())
            .then()
            .statusCode(HttpStatus.SC_CREATED)
            .and()
            .body("code", is(PRODUCT_CODE))
            .body("name", is(ProductTestData.PRODUCT_NAME))
            .body("description", is(ProductTestData.PRODUCT_DESCRIPTION))
            .body("categories[0]", is(ProductTestData.ROOT_CATEGORY))
            .body("categories[1]", is(ProductTestData.MAIN_CATEGORY))
            .body("categories[2]", is(ProductTestData.SUB_CATEGORY))
            .body("vendorName", is(ProductTestData.VENDOR_NAME))
            .body("vatRate.code", is(ProductTestData.PRODUCT_VAT_CODE))
            .body("vatRate.rate", is(ProductTestData.PRODUCT_VAT_RATE))
            //.body("stock", is(50000L))
            //.body("termsAndConditions", is("OFFERsss"))
            .body("stock", is(40000))
            .body("termsAndConditions", is(ProductTestData.PRODUCT_TERMS_AND_CONDITIONS))
            .body("specifications", hasSize(3))
            .body("specifications[0].name", is("Processor"))
            .body("specifications[0].description", is("Intel Core i5"))
            .body("specifications[1].name", is("Memory"))
            .body("specifications[1].description", is("4GB DDR3"))
            .body("specifications[2].name", is("Drive"))
            .body("specifications[2].description", is("512GB Sata3 SSD"))
            .body("images", hasSize(2))
            .body("images[0].url", is(ProductTestData.IMAGE_URL_1))
            .body("images[0].description", is(ProductTestData.IMAGE_DESC_1))
            .body("images[1].url", is(ProductTestData.IMAGE_URL_2))
            .body("images[1].description", is(ProductTestData.IMAGE_DESC_2))
            .body("priceBands", hasSize(3))
            .body("priceBands[0].minVolume", is(0))
            .body("priceBands[0].maxVolume", is(99))
            .body("priceBands[0].buyerPrice", is(new BigDecimal("1000.00")))
            .body("priceBands[0].vendorPrice", is(new BigDecimal("900.00")))
            .body("priceBands[0].shoalMargin", is(new BigDecimal("5.00")))
            .body("priceBands[0].distributorMargin", is(new BigDecimal("2.00")))
            .body("priceBands[1].minVolume", is(100))
            .body("priceBands[1].maxVolume", is(999))
            .body("priceBands[1].buyerPrice", is(new BigDecimal("975.00")))
            .body("priceBands[1].vendorPrice", is(new BigDecimal("875.00")))
            .body("priceBands[1].shoalMargin", is(new BigDecimal("5.00")))
            .body("priceBands[1].distributorMargin", is(new BigDecimal("2.00")))
            .body("priceBands[2].minVolume", is(1000))
            .body("priceBands[2].maxVolume", is(nullValue()))
            .body("priceBands[2].buyerPrice", is(new BigDecimal("950.00")))
            .body("priceBands[2].vendorPrice", is(new BigDecimal("850.00")))
            .body("priceBands[2].shoalMargin", is(new BigDecimal("5.00")))
            .body("priceBands[2].distributorMargin", is(new BigDecimal("2.00")))
            ;

        shouldHideInternalPricingInfoFromBuyers();
    }

    @Test
    public void shouldRejectAProductWithAnInvalidOffer() {
        usingValidAdminAuthentication();

        createAProduct(buildAnAdminProductDtoWithInvalidOffer(PRODUCT_CODE).build())
            .then()
            .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    private void shouldHideInternalPricingInfoFromBuyers() {

        FetchProduct.fetchProduct(PRODUCT_CODE)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .and()
            .body("code", is(PRODUCT_CODE))
            .body("priceBands[0].minVolume", is(0))
            .body("priceBands[0].maxVolume", is(99))
            .body("priceBands[0].buyerPrice", is(new BigDecimal("1000.00")))
            .body("priceBands[0].vendorPrice", is(nullValue()))
            .body("priceBands[0].shoalMargin", is(nullValue()))
            .body("priceBands[0].distributorMargin", is(nullValue()))
            .body("priceBands[1].minVolume", is(100))
            .body("priceBands[1].maxVolume", is(999))
            .body("priceBands[1].buyerPrice", is(new BigDecimal("975.00")))
            .body("priceBands[1].vendorPrice", is(nullValue()))
            .body("priceBands[1].shoalMargin", is(nullValue()))
            .body("priceBands[1].distributorMargin", is(nullValue()))
            .body("priceBands[2].minVolume", is(1000))
            .body("priceBands[2].maxVolume", is(nullValue()))
            .body("priceBands[2].buyerPrice", is(new BigDecimal("950.00")))
            .body("priceBands[2].vendorPrice", is(nullValue()))
            .body("priceBands[2].shoalMargin", is(nullValue()))
            .body("priceBands[2].distributorMargin", is(nullValue()))
        ;
    }
}
