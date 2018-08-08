package com.ishoal.core.products;

import com.ishoal.core.config.ShoalCoreTestConfiguration;
import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.OfferTestData;
import com.ishoal.core.domain.Offers;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCategory;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.Vendor;
import org.joda.time.DateTime;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.ishoal.core.domain.OfferTestData.aFutureOffer;
import static com.ishoal.core.domain.OfferTestData.aNewOfferToday;
import static com.ishoal.core.domain.OfferTestData.aPastOffer;
import static com.ishoal.core.domain.ProductTestData.productBuilder;
import static com.ishoal.core.domain.ProductTestData.productImageBuilder;
import static com.ishoal.core.domain.ProductTestData.productSpecBuilder;
import static com.ishoal.core.domain.ProductVatRateTestData.vatRatesWith20PercentRate;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("/test.properties")
@ContextConfiguration(classes = {ShoalCoreTestConfiguration.class})
@Sql("/sql/clear-down-data.sql")
@Sql("/sql/categories-test-data.sql")
@Sql("/sql/vendor-test-data.sql")
@Transactional
public class CreateProductServiceIT {

    private static final ProductCode PRODUCT_CODE = ProductCode.from(UUID.randomUUID().toString());
    private static final String PRODUCT_NAME = "HP Sholite";
    private static final String VENDOR = "HP";
    private static final String CATEGORY = "Power User";

    private static final DateTime OFFER_START_DATE = OfferTestData.NEW_OFFER_START_DATE;
    private static final DateTime OFFER_END_DATE = OfferTestData.NEW_OFFER_END_DATE.withTime(23, 59, 59, 0);

    private static final DateTime FUTURE_OFFER_START_DATE = OfferTestData.FUTURE_OFFER_START_DATE;
    private static final DateTime FUTURE_OFFER_END_DATE = OfferTestData.FUTURE_OFFER_END_DATE.withTime(23, 59, 59, 0);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private CreateProductService createProductService;

    @Autowired
    private ProductService productService;

    @Test
    public void shouldCreateANewProductOffer() {

        Product persistedProductOffer = createProductService.createProduct(
            productBuilder(PRODUCT_CODE)
                .name(PRODUCT_NAME)
                .category(ProductCategory.aProductCategory().name(CATEGORY).build())
                .productSpecs(productSpecBuilder())
                .images(productImageBuilder())
                .vendor(Vendor.aVendor().name(VENDOR).build())
                .vatRates(vatRatesWith20PercentRate())
                .offers(aNewOffer())
                .build());

        validateProduct(persistedProductOffer);
        validateCurrentOffers(persistedProductOffer.getOffers());

        validateReadProduct(persistedProductOffer);
        validateReadOffers(persistedProductOffer);
    }

    @Test
    public void shouldCreateAProductWithOfferInTheFuture() {
        Product persistedProductOffer = createProductService.createProduct(
            productBuilder(PRODUCT_CODE)
                .name(PRODUCT_NAME)
                .category(ProductCategory.aProductCategory().name(CATEGORY).build())
                .productSpecs(productSpecBuilder())
                .images(productImageBuilder())
                .vendor(Vendor.aVendor().name(VENDOR).build())
                .vatRates(vatRatesWith20PercentRate())
                .offers(aNewFutureOffer())
                .build());

        validateProduct(persistedProductOffer);
        validateFutureOffers(persistedProductOffer.getOffers());
    }

    @Test
    public void shouldNotAllowProductReadWithOfferInTheFuture() {

        expectedException.expect(InvalidProductException.class);

        Product persistedProductOffer = createProductService.createProduct(
            productBuilder(PRODUCT_CODE)
                .name(PRODUCT_NAME)
                .category(ProductCategory.aProductCategory().name(CATEGORY).build())
                .productSpecs(productSpecBuilder())
                .images(productImageBuilder())
                .vendor(Vendor.aVendor().name(VENDOR).build())
                .vatRates(vatRatesWith20PercentRate())
                .offers(aNewFutureOffer())
                .build());

        validateReadProduct(persistedProductOffer);
    }

    @Test
    public void shouldRejectAProductWithOfferInThePast() {
        expectedException.expect(InvalidProductException.class);

        createProductService.createProduct(
            productBuilder(PRODUCT_CODE)
                .name(PRODUCT_NAME)
                .category(ProductCategory.aProductCategory().name(CATEGORY).build())
                .productSpecs(productSpecBuilder())
                .images(productImageBuilder())
                .vendor(Vendor.aVendor().name(VENDOR).build())
                .vatRates(vatRatesWith20PercentRate())
                .offers(aNewPastOffer())
                .build());
    }

    private Offers aNewOffer() {

        return Offers.over(aNewOfferToday().build());
    }

    private Offers aNewFutureOffer() {
        return Offers.over(aFutureOffer().build());
    }

    private Offers aNewPastOffer() {
        return Offers.over(aPastOffer().build());
    }

    private void validateProduct(Product productOffer) {

        assertThat(productOffer.getCode(), is(PRODUCT_CODE));
        assertThat(productOffer.getName(), is(PRODUCT_NAME));
        assertThat(productOffer.getCategory().getName(), is(CATEGORY));
        assertThat(productOffer.getVendor().getName(), is(VENDOR));
        assertThat(productOffer.getProductSpecs().size(), is(3));
        assertThat(productOffer.getImages().size(), is(2));
        assertThat(productOffer.getVatRates().size(), is(1));
    }

    private void validateCurrentOffers(Offers offers) {
        Offer offer = offers.current();

        assertThat(offer, is(notNullValue()));
        assertThat(offer.getOfferReference(), is(notNullValue()));
        assertThat(offer.getStartDateTime(), is(OFFER_START_DATE));
        assertThat(offer.getEndDateTime(), is(OFFER_END_DATE));
        assertThat(offer.getPriceBands().size(), is(2));
    }

    private void validateFutureOffers(Offers offers) {
        offers.stream().forEach(offer -> {
            assertThat(offer, is(notNullValue()));
            assertThat(offer.getOfferReference(), is(notNullValue()));
            assertThat(offer.getStartDateTime(), is(FUTURE_OFFER_START_DATE));
            assertThat(offer.getEndDateTime(), is(FUTURE_OFFER_END_DATE));
            assertThat(offer.getPriceBands().size(), is(2));
        });
    }

    private void validateReadProduct(Product productOffer) {
        Product readProduct = productService.getProduct(productOffer.getCode());
        validateProduct(readProduct);
    }

    private void validateReadOffers(Product productOffer) {
        Product readProduct = productService.getProduct(productOffer.getCode());
        validateCurrentOffers(readProduct.getOffers());
    }
}