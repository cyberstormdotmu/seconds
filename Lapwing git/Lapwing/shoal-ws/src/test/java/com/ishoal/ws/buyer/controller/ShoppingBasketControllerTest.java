package com.ishoal.ws.buyer.controller;

import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.Offers;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.User;
import com.ishoal.core.orders.VatCalculator;
import com.ishoal.core.products.ProductService;
import com.ishoal.ws.buyer.dto.ShoppingBasketDto;
import com.ishoal.ws.buyer.dto.ShoppingBasketItemDto;
import com.ishoal.ws.session.FakeBuyerSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static com.ishoal.core.domain.OfferTestData.anExistingOpenOffer;
import static com.ishoal.core.domain.ProductTestData.productBuilder;
import static com.ishoal.ws.buyer.dto.ShoppingBasketDto.aShoppingBasket;
import static com.ishoal.ws.buyer.dto.ShoppingBasketItemDto.aShoppingBasketItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingBasketControllerTest {

    public static final ProductCode HPELITE840 = ProductCode.from("HPELITE840");
    public static final long CURRENT_VOLUME = 50L;

    @Mock
    private ProductService productService;

    @Mock
    private User user;

    @Spy
    private FakeBuyerSession buyerSession;

    private ShoppingBasketController controller;

    @Before
    public void before() {
        controller = new ShoppingBasketController(productService, new VatCalculator());

        theProductServiceWillReturnAProduct();
        theBuyerSessionHasABasket();
    }

    @Test
    public void getBasketReturnsTheBasket() {
        assertThat(getBasket(), is(not(nullValue())));
    }

    @Test
    public void getBasketUpdatesTheBasketInTheSession() {
        getBasket();
        verify(buyerSession).updateBasket(any(ShoppingBasketDto.class));
    }

    @Test
    public void getBasketReturnsTheBasketWithUnitPrice() {
        ShoppingBasketItemDto item = getBasketAndReturnItem();
        assertThat(item.getUnitPrice(), is(new BigDecimal("900.00")));
    }

    @Test
    public void getBasketReturnsTheBasketWithCurrentVolume() {
        ShoppingBasketItemDto item = getBasketAndReturnItem();
        assertThat(item.getCurrentVolume(), is(CURRENT_VOLUME));
    }

    @Test
    public void setBasketReturnsTheBasket() {
        assertThat(setBasket(), is(not(nullValue())));
    }

    @Test
    public void setBasketUpdatesTheBasketInTheSession() {
        setBasket();
        verify(buyerSession).updateBasket(any(ShoppingBasketDto.class));
    }

    @Test
    public void setBasketReturnsTheBasketWithUnitPrice() {
        ShoppingBasketItemDto item = setBasketAndReturnItem();
        assertThat(item.getUnitPrice(), is(new BigDecimal("900.00")));
    }

    @Test
    public void setBasketReturnsTheBasketWithUnitPriceVat() {
        ShoppingBasketItemDto item = setBasketAndReturnItem();
        assertThat(item.getUnitPriceVat(), is(new BigDecimal("180.00")));
    }

    @Test
    public void setBasketReturnsTheBasketWithCurrentVolume() {
        ShoppingBasketItemDto item = setBasketAndReturnItem();
        assertThat(item.getCurrentVolume(), is(CURRENT_VOLUME));
    }

    private ShoppingBasketDto setBasket() {
        return controller.setBasket(basketWithOneItem(), buyerSession, user);
    }

    private ShoppingBasketItemDto setBasketAndReturnItem() {
        ShoppingBasketDto basket = setBasket();
        return basket.getItems().get(0);
    }

    private ShoppingBasketDto getBasket() {
        return controller.getBasket(buyerSession, user);
    }

    private ShoppingBasketItemDto getBasketAndReturnItem() {
        ShoppingBasketDto basket = getBasket();
        return basket.getItems().get(0);
    }

    private void theBuyerSessionHasABasket() {
        buyerSession.initializeBasket(basketWithOneItem());
    }

    private ShoppingBasketDto basketWithOneItem() {
        return aShoppingBasket()
                .item(item())
                .build();
    }

    private ShoppingBasketItemDto item() {
        return aShoppingBasketItem()
                .productCode(HPELITE840.toString())
                .quantity(1000)
                
                .unitPrice(new BigDecimal("1000.00"))
                .currentVolume(0L)
                .build();
    }

    private void theProductServiceWillReturnAProduct() {
        Product product = productBuilder(HPELITE840)
                .offers(Offers.over(offer()))
                .build();

        when(productService.getProduct(HPELITE840)).thenReturn(product);
    }

    private Offer offer() {
        return anExistingOpenOffer().currentVolume(CURRENT_VOLUME).build();
    }
}