package com.ishoal.ws.buyer.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.PriceBand;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.domain.User;
import com.ishoal.core.orders.VatCalculator;
import com.ishoal.core.products.ProductService;
import com.ishoal.ws.buyer.dto.ProductDto;
import com.ishoal.ws.buyer.dto.ShoppingBasketDto;
import com.ishoal.ws.buyer.dto.ShoppingBasketItemDto;
import com.ishoal.ws.buyer.dto.adapter.ProductDtoAdapter;
import com.ishoal.ws.session.BuyerSession;

@RestController
@RequestMapping("/ws/basket")
public class ShoppingBasketController {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingBasketController.class);
    private final ProductDtoAdapter productAdapter = new ProductDtoAdapter();
    private final ProductService productService;
    private final VatCalculator vatCalculator;
    
    public ShoppingBasketController(ProductService productService, VatCalculator vatCalculator) {
        this.productService = productService;
        this.vatCalculator = vatCalculator;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ShoppingBasketDto getBasket(BuyerSession session, User user) {
        logger.info("Get basket for user {}", user);

        ShoppingBasketDto basket = session.getBasket();

        ShoppingBasketDto response = updatePricesInBasketAndStoreToSession(basket, session);
        logger.info("Get basket for user returned {}", response);
        return response;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ShoppingBasketDto setBasket(@RequestBody ShoppingBasketDto basket, BuyerSession session, User user) {
        logger.info("Set basket for user {} with basket contents {}", user, basket);

        return updatePricesInBasketAndStoreToSession(basket, session);
    }

    private ShoppingBasketDto updatePricesInBasketAndStoreToSession(ShoppingBasketDto basket, BuyerSession session) {
        basket.getItems().forEach(item -> updatePrice(item));

        session.updateBasket(basket);

        return basket;
    }

    private void updatePrice(ShoppingBasketItemDto item) {
        ProductCode productCode = ProductCode.from(item.getProductCode());
        
        Product product = productService.getProduct(productCode);
        Offer offer = product.currentOffer();
        item.setCurrentVolume(offer.getCurrentVolume());
        PriceBand priceBandForRequestedQuantity = offer.getPriceBandForRequestedQuantity(item.getQuantity());
        BigDecimal unitPriceNet = priceBandForRequestedQuantity.getBuyerPrice();
        BigDecimal unitPriceVat = vatCalculator.calculateVatAmount(unitPriceNet, product.currentVatRate());
        item.setUnitPrice(unitPriceNet);
        item.setUnitPriceVat(unitPriceVat);
        item.setVendorName(product.getVendor().getName());
  
        
    }
}
