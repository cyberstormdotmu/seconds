package com.ishoal.ws.admin.dto.adapter;

import com.ishoal.common.util.CustomCollectors;
import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.Products;
import com.ishoal.ws.admin.dto.OfferListingDto;
import java.util.List;
import java.util.stream.Collectors;

import static com.ishoal.ws.admin.dto.OfferListingDto.anOfferListing;

public class OfferListingDtoAdapter {

    public List<OfferListingDto> adapt(Products products) {
        return products.stream().map(product -> adapt(product)).collect(CustomCollectors.toMergedList());
    }

    private List<OfferListingDto> adapt(Product product) {
        return product.getOffers().stream()
                .map(offer -> adapt(product, offer))
                .collect(Collectors.toList());
    }

    private OfferListingDto adapt(Product product, Offer offer) {
        return anOfferListing()
            .productCode(product.getCode().toString())
            .productName(product.getName())
            .offerReference(offer.getOfferReference().asString())
            .offerStartDateTime(offer.getStartDateTime())
            .offerEndDateTime(offer.getEndDateTime())
            .build();
    }
}
