package com.ishoal.core.products;

import static com.ishoal.core.domain.Offer.anOffer;
import static com.ishoal.core.domain.PriceBand.aPriceBand;
import static com.ishoal.core.domain.PriceBands.somePriceBands;
import static com.ishoal.core.domain.Product.aProduct;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.OfferId;
import com.ishoal.core.domain.Offers;
import com.ishoal.core.domain.PriceBand;
import com.ishoal.core.domain.PriceBands;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.repository.OfferRepository;
import com.ishoal.core.repository.ProductRepository;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private static final ProductCode PRODUCT_CODE = ProductCode.from("HPELITE840");
    private static final ProductCode PRODUCT_CODE_INVALID_PRODUCT = ProductCode.from("ANINVALIDPRODUCT");
    private static final ProductCode PRODUCT_CODE_EXPIRED_OFFER = ProductCode.from("EXPIRED_PRODUCT_OFFER");

    private static final long OFFER_ID = new Random().nextLong();
    private static final long OFFER_CURRENT_VOLUME = 5L;
    private static final DateTime OFFER_START_DATE_TIME = DateTime.now().minusDays(1);
    private static final DateTime OFFER_END_DATE_TIME = DateTime.now().plusMonths(1);

    private static final PriceBand PRICE_BAND_1 = aPriceBand().minVolume(0L).maxVolume(10L).build();
    private static final PriceBand PRICE_BAND_2 = aPriceBand().minVolume(11L).maxVolume(20L).build();
    private static final PriceBand PRICE_BAND_3 = aPriceBand().minVolume(21L).build();

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OfferRepository offerRepository;
   

    private Product product;
    private Product expired;
    private Offer offer;

    private ProductService service;

    @Before
    public void setUp() {
        this.service = new ProductService(this.productRepository, this.offerRepository);

        PriceBands priceBands = somePriceBands().priceBand(PRICE_BAND_1).priceBand(PRICE_BAND_2).priceBand(PRICE_BAND_3)
                .build();
        this.offer = anOffer().id(OfferId.from(OFFER_ID)).startDateTime(OFFER_START_DATE_TIME)
                .endDateTime(OFFER_END_DATE_TIME).currentVolume(OFFER_CURRENT_VOLUME).priceBands(priceBands).build();

        product = buildAFakeProduct();
        when(productRepository.findByCode(PRODUCT_CODE)).thenReturn(product);
        when(productRepository.findByCode(PRODUCT_CODE_INVALID_PRODUCT)).thenReturn(Product.aProduct().build());
        expired = buildAnExpiredProductOffer();
        when(productRepository.findByCode(PRODUCT_CODE_EXPIRED_OFFER)).thenReturn(expired);

    }

    private Product buildAFakeProduct() {

        Product product = spy(aProduct().code(PRODUCT_CODE).offers(Offers.over(offer)).build());
        when(product.isActive()).thenReturn(true);
        return product;
    }

    private Product buildAnExpiredProductOffer() {

        Product product = spy(aProduct().code(PRODUCT_CODE_EXPIRED_OFFER).offers(Offers.over(offer)).build());
        when(product.isExpired()).thenReturn(true);
        return product;
    }

    @Test
    public void findByCategoryDelegatesToTheRepository() {
        service.findByCategory("Laptops");
        verify(productRepository).findByCategory("Laptops");
    }

    @Test
    public void increaseCurrentVolumeShouldNotifyRepositoryToIncreaseQuantityByCorrectAmount() {
        this.service.increaseCurrentVolume(this.offer, 17);
        verify(this.offerRepository).increaseCurrentVolume(OfferId.from(OFFER_ID), 17);
    }
    
    @Test
    public void increaseCurrentVolumeShouldReturnNewVolumeForOffer() {
        when(this.offerRepository.increaseCurrentVolume(any(), anyLong())).thenReturn(1533L);
        assertThat(this.service.increaseCurrentVolume(this.offer, 5), is(1533L));
    }

    @Test
    public void shouldReturnTheProductFromTheRepository() {
        assertThat(service.getProduct(PRODUCT_CODE), is(this.product));
    }

    @Test
    public void shouldReturnTheCorrectPriceBandForTheProductAtGivenQuantityWhenInMiddleOfBand() {
        assertThat(this.service.getPriceBandForOrder(PRODUCT_CODE, 10), is(PRICE_BAND_2));
    }

    @Test
    public void shouldReturnTheCorrectPriceBandForTheProductAtGivenQuantityWhenAtMinimumOfBand() {
        assertThat(this.service.getPriceBandForOrder(PRODUCT_CODE, 6), is(PRICE_BAND_2));
    }

    @Test
    public void shouldReturnTheCorrectPriceBandForTheProductAtGivenQuantityWhenAtMaximumOfBand() {
        assertThat(this.service.getPriceBandForOrder(PRODUCT_CODE, 15), is(PRICE_BAND_2));
    }

    @Test
    public void shouldReturnTheCorrectPriceBandForTheProductAtGivenQuantityWhenAtTopBand() {
        assertThat(this.service.getPriceBandForOrder(PRODUCT_CODE, 40), is(PRICE_BAND_3));
    }

    @Test
    public void shouldReturnNullIfAProductIsInvalid() {
        expectedException.expect(InvalidProductException.class);
        assertThat(service.getProduct(PRODUCT_CODE_INVALID_PRODUCT), is(nullValue()));
    }

    @Test
    public void shouldReturnProductIfExpired() {
        assertThat(service.getProduct(PRODUCT_CODE_EXPIRED_OFFER), is(expired));
    }
}