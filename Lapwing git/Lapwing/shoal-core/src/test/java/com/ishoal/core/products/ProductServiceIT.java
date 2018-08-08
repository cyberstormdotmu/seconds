package com.ishoal.core.products;

import com.ishoal.core.config.ShoalCoreTestConfiguration;
import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.PriceBand;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.ProductSpec;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.ishoal.core.domain.ProductSpec.aProductSpec;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("/test.properties")
@ContextConfiguration(classes = { ShoalCoreTestConfiguration.class })
@Sql("/sql/clear-down-data.sql")
@Sql("/sql/categories-test-data.sql")
@Sql("/sql/vendor-test-data.sql")
@Sql("/sql/vat-test-data.sql")
@Sql("/sql/product-test-data.sql")
@Sql("/sql/offer-test-data.sql")
@Sql("/sql/expired-offer-test-data.sql")
@Transactional
public class ProductServiceIT {

    private static final ProductCode PRODUCT_CODE = ProductCode.from("HPELITE840");

    private static final ProductCode EXPIRED_PRODUCT_CODE = ProductCode.from("HPPAVILLIONDV7");

    @Autowired
    private ProductService productService;

    @Before
    public void setup() {
        DateTimeUtils.setCurrentMillisFixed(DateTime.parse("2015-09-18").getMillis());
    }

    @After
    public void teardown() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void aNonExistentProductCannotBeFound() {
        Product product = productService.getProduct(ProductCode.from("BANANA"));
        assertThat(product, is(nullValue()));
    }

    @Test
    public void anExistingProductCanBeFound() {
        Product product = productService.getProduct(PRODUCT_CODE);
        assertThat(product, is(not(nullValue())));
    }

    @Test
    public void theProductHasCorrectCode() {
        Product product = productService.getProduct(PRODUCT_CODE);
        assertThat(product.getCode(), is(PRODUCT_CODE));
    }

    @Test
    public void theProductHasCorrectName() {
        Product product = productService.getProduct(PRODUCT_CODE);
        assertThat(product.getName(), is("HP EliteBook 840 G2 Laptop"));
    }

    @Test
    public void theProductHasCorrectDescription() {
        Product product = productService.getProduct(PRODUCT_CODE);
        assertThat(product.getDescription(), is(
                "The HP EliteBook 840 thin and light notebook allows users to be ultra-productive in and out of the office. Work with confidence thanks to proven technologies, security, performance, and management features that will meet all your enterprise needs."));
    }


    @Test
    public void theProductHasCorrectStock() {
        Product product = productService.getProduct(PRODUCT_CODE);
        assertThat(product.getStock(), is(
                40000L));
    }

    @Test
    public void theProductHasCorrectTermsandConditions() {
        Product product = productService.getProduct(PRODUCT_CODE);
        assertThat(product.getTermsAndConditions(), is(
                "OFFER valid for 1 week only"));
    }
    @Test
    public void theProductHasSomeSpecs() {
        Product product = productService.getProduct(PRODUCT_CODE);
        assertThat(product.getProductSpecs(), hasItems(processorSpec(), memorySpec()));
    }

    private ProductSpec processorSpec() {
        return aProductSpec().type("Processor").value("Intel Core i5 processor").build();
    }

    private ProductSpec memorySpec() {
        return aProductSpec().type("Memory").value("4GB DDR3L-1600 SDRAM (1 x 4GB)").build();
    }

    @Test
    public void theProductHasACurrentOffer() {
        Product product = productService.getProduct(PRODUCT_CODE);
        assertTrue(product.isActive());
    }

    @Test
    public void theOfferHasAStartDate() {
        Offer offer = getOfferFromProduct();
        assertThat(offer.getStartDateTime(), is(DateTime.parse("2015-09-17T00:00:00")));
    }

    @Test
    public void theOfferHasAnEndDate() {
        Offer offer = getOfferFromProduct();
        assertThat(offer.getEndDateTime(), is(DateTime.parse("2020-03-17T23:59:59")));
    }

    @Test
    public void theOfferHasACurrentVolume() {
        Offer offer = getOfferFromProduct();
        assertThat(offer.getCurrentVolume(), is(1250L));
    }

    @Test
    public void theOfferHasSomePriceBands() {
        Offer offer = getOfferFromProduct();
        assertThat(offer.getPriceBands().size(), is(greaterThan(0)));
    }

    @Test
    public void theFirstPriceBandHasCorrectMinVolume() {
        Offer offer = getOfferFromProduct();
        PriceBand priceBand = offer.getPriceBands().get(0);
        assertThat(priceBand.getMinVolume(), is(0L));
    }

    @Test
    public void theFirstPriceBandHasCorrectMaxVolume() {
        Offer offer = getOfferFromProduct();
        PriceBand priceBand = offer.getPriceBands().get(0);
        assertThat(priceBand.getMaxVolume(), is(999L));
    }

    @Test
    public void theLastPriceBandHasNoMaxVolume() {
        Offer offer = getOfferFromProduct();
        PriceBand priceBand = offer.getPriceBands().get(offer.getPriceBands().size() - 1);
        assertThat(priceBand.getMaxVolume(), is(nullValue()));
    }

    @Test
    public void theFirstPriceBandHasCorrectBuyerPrice() {
        Offer offer = getOfferFromProduct();
        PriceBand priceBand = offer.getPriceBands().get(0);
        assertThat(priceBand.getBuyerPrice(), is(new BigDecimal("1030.00")));
    }

    @Test
    public void theFirstPriceBandHasCorrectVendorPrice() {
        Offer offer = getOfferFromProduct();
        PriceBand priceBand = offer.getPriceBands().get(0);
        assertThat(priceBand.getVendorPrice(), is(new BigDecimal("930.00")));
    }

    @Test
    public void theFirstPriceBandHasCorrectAgencyMargin() {
        Offer offer = getOfferFromProduct();
        PriceBand priceBand = offer.getPriceBands().get(0);
        assertThat(priceBand.getShoalMargin(), is(new BigDecimal("30.00")));
    }

    @Test
    public void theFirstPriceBandHasCorrectDistributorMargin() {
        Offer offer = getOfferFromProduct();
        PriceBand priceBand = offer.getPriceBands().get(0);
        assertThat(priceBand.getDistributorMargin(), is(new BigDecimal("20.00")));
    }

    @Test
    public void shouldReadAProductWithAnExpiredOffer() {
        Product product = productService.getProduct(EXPIRED_PRODUCT_CODE);

        assertThat(product.isActive(), is(false));
        assertThat(product.isExpired(), is(true));
    }


    private Offer getOfferFromProduct() {
        Product product = productService.getProduct(PRODUCT_CODE);
        return product.currentOffer();
    }

}
