package com.ishoal.ws.buyer.dto.adapter;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.Products;
import com.ishoal.ws.buyer.dto.ProductImageDto;
import com.ishoal.ws.buyer.dto.ProductSummaryDto;

public class ProductSummaryDtoAdapter {

    private ProductImageDtoAdapter imageAdapter = new ProductImageDtoAdapter();
    private PriceBandsDtoAdapter priceBandsAdapter = new PriceBandsDtoAdapter();
    public List<ProductSummaryDto> adapt(Products products) {
        return IterableUtils.mapToList(products, this::adapt);
    }

    private ProductSummaryDto adapt(Product product){
    	
    	 if (product == null) {
             return null;
         }
    	ProductSummaryDto.Builder builder = ProductSummaryDto.aProductSummary()
    			.productId(product.getId())
                .code(product.getCode().toString())
                .name(product.getName())
                .category(product.getCategory().getName())
                .offerEndDate(offerEndDate(product))
                .initialPrice(initialPrice(product))
                .currentPrice(currentPrice(product))
                .stock(product.getStock())
                .targetPrice(targetPrice(product))
                .currentVolume(product.getOffers().current().getCurrentVolume())
                .image(primaryImage(product));
    	Offer offer = product.currentOffer();
    	if(offer!=null)
    	{
    		builder.currentVolume(offer.getCurrentVolume())
    		.offerStartDate(offer.getStartDateTime())
    		.offerEndDate(offer.getEndDateTime())
    		.priceBands(priceBandsAdapter.adapt(offer.getPriceBands()));
    	}
    	
    	return builder.build();
    }

    private DateTime offerEndDate(Product product) {
        return product.currentOffer().getEndDateTime();
    }

    private BigDecimal initialPrice(Product product) {
        return product.currentOffer().getInitialPriceBand().getBuyerPrice();
    }

    private BigDecimal currentPrice(Product product) {
        return product.currentOffer().getCurrentPriceBand().getBuyerPrice();
    }

    private BigDecimal targetPrice(Product product) {
        return product.currentOffer().getTargetPriceBand().getBuyerPrice();
    }

    private ProductImageDto primaryImage(Product product) {
        return imageAdapter.adapt(product.getImages().primary());
    }
}
