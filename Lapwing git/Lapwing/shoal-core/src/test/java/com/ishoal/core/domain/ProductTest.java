package com.ishoal.core.domain;

import org.junit.Test;

import static com.ishoal.core.domain.OfferTestData.aFutureOffer;
import static com.ishoal.core.domain.OfferTestData.aNewOfferToday;
import static com.ishoal.core.domain.OfferTestData.aPastOffer;
import static com.ishoal.core.domain.OfferTestData.anExistingOpenOffer;
import static com.ishoal.core.domain.OfferTestData.anOfferWithInvalidPriceBand;
import static com.ishoal.core.domain.OfferTestData.anOfferWithNoUpperBound;
import static com.ishoal.core.domain.OfferTestData.anOfferWithOverlappedDateRange;
import static com.ishoal.core.domain.OfferTestData.anOfferWithOverlappingPriceBand;
import static com.ishoal.core.domain.OfferTestData.anOfferWithPriceBandGap;
import static com.ishoal.core.domain.OfferTestData.anOfferWithSameDayRange;
import static com.ishoal.core.domain.PriceBands.emptyPriceBands;
import static com.ishoal.core.domain.Product.aProduct;
import static com.ishoal.core.domain.ProductTestData.productBuilder;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class ProductTest {

    @Test
    public void shouldBeEqualIfSameInstance() {
        Product product = productWithCode("LAPTOP");
        assertThat(product, is(equalTo(product)));
    }

    @Test
    public void shouldBeEqualIfSameValueButDifferentInstance() {
        assertThat(productWithCode("LAPTOP"), is(equalTo(productWithCode("LAPTOP"))));
    }

    @Test
    public void shouldNotBeEqualIfNotSame() {
        assertThat(productWithCode("LAPTOP"), is(not(equalTo(productWithCode("PHONE")))));
    }
    
    @Test
    public void shouldNotBeEqualToNull() {
        assertThat(productWithCode("LAPTOP"), is(not(equalTo(null))));
    }
    
    @Test
    public void shouldNotBeEqualToProductWithNullCode() {
        assertThat(productWithCode("LAPTOP"), is(not(equalTo(productWithCode((ProductCode)null)))));
    }
    
    @Test
    public void shouldNotBeEqualIfToIncorrectObjectType() {
        assertThat(productWithCode("LAPTOP"), is(not(equalTo("hello"))));
    }

    @Test
    public void shouldTestForProductActivation() {

        testProductIsActive(productBuilder("LAPTOP"));

        testProductInactive(productBuilder("LAPTOP").offers(Offers.emptyOffers()));

        testProductIsActive(productBuilder("LAPTOP").offers(Offers.over(aNewOfferToday().build())));

        testProductInactive(productBuilder("LAPTOP").offers(Offers.over(aFutureOffer().build())));

        testProductInactive(productBuilder("LAPTOP").offers(Offers.over(anOfferWithSameDayRange().build())));

        testProductInactive(productBuilder("LAPTOP").offers(Offers.over(aPastOffer().build())));
    }

    @Test
    public void shouldTestForProductValidity() {

        testProductInvalid(productBuilder("LAPTOP").offers(Offers.emptyOffers()));

        testProductInvalid(productBuilder("LAPTOP").productSpecs(ProductSpecs.emptyProductSpecs()));

        testProductInvalid(productBuilder("LAPTOP").images(ProductImages.emptyProductImages()));

        testProductInvalid(productBuilder("LAPTOP").offers(Offers.over(anExistingOpenOffer().priceBands(emptyPriceBands()).build())));

        testProductInvalid(productBuilder("LAPTOP").vatRates(ProductVatRates.emptyVatRates()));

        testProductIsValid(productBuilder("LAPTOP").offers(Offers.over(aNewOfferToday().build())));

        testProductIsValid(productBuilder("LAPTOP").offers(Offers.over(aFutureOffer().build())));

        testProductIsValid(productBuilder("LAPTOP").offers(Offers.over(anOfferWithSameDayRange().build())));

        testProductInvalid(productBuilder("LAPTOP").offers(Offers.over(aPastOffer().build())));

        testProductInvalid(productBuilder("LAPTOP").offers(Offers.over(anOfferWithOverlappedDateRange().build())));

        testProductInvalid(productBuilder("LAPTOP").offers(Offers.over(anOfferWithInvalidPriceBand().build())));

        testProductInvalid(productBuilder("LAPTOP").offers(Offers.over(anOfferWithOverlappingPriceBand().build())));

        testProductInvalid(productBuilder("LAPTOP").offers(Offers.over(anOfferWithNoUpperBound().build())));

        testProductInvalid(productBuilder("LAPTOP").offers(Offers.over(anOfferWithPriceBandGap().build())));
    }

    @Test
    public void shouldTestForExpiry() {
        testProductNotExpired(productBuilder("LAPTOP").offers(Offers.emptyOffers()));

        testProductNotExpired(productBuilder("LAPTOP").offers(Offers.over(aNewOfferToday().build())));

        testProductNotExpired(productBuilder("LAPTOP").offers(Offers.over(aFutureOffer().build())));

        testProductNotExpired(productBuilder("LAPTOP").offers(Offers.over(anOfferWithSameDayRange().build())));

        testProductNotExpired(productBuilder("LAPTOP").offers(Offers.over(anOfferWithOverlappedDateRange().build())));

        testProductExpired(productBuilder("LAPTOP").offers(Offers.over(aPastOffer().build())));
    }

    private void testProductInactive(Product.Builder product) {

        assertThat(product.build().isActive(), is(false));
    }

    private void testProductIsActive(Product.Builder product) {

        assertThat(product.build().isActive(), is(true));
    }

    private void testProductIsValid(Product.Builder product) {

        assertThat(product.build().isValidForSave(), is(true));
    }

    private void testProductInvalid(Product.Builder product) {

        assertThat(product.build().isValidForSave(), is(false));
    }

    private void testProductExpired(Product.Builder product) {

        assertThat(product.build().isExpired(), is(true));
    }

    private void testProductNotExpired(Product.Builder product) {

        assertThat(product.build().isExpired(), is(false));
    }

    private Product productWithCode(String code) {
        return productWithCode(ProductCode.from(code));
    }
    
    private Product productWithCode(ProductCode code) {
        return aProduct().code(code).build();
    }
}