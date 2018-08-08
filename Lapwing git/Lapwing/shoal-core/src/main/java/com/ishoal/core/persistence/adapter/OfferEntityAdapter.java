package com.ishoal.core.persistence.adapter;

import static com.ishoal.common.util.IterableUtils.mapToCollection;
import static com.ishoal.common.util.IterableUtils.mapToList;
import static com.ishoal.core.domain.Offer.anOffer;

import java.util.List;

import org.joda.time.DateTime;

import com.ishoal.core.domain.Offer;
import com.ishoal.core.domain.OfferId;
import com.ishoal.core.domain.OfferReference;
import com.ishoal.core.domain.Offers;
import com.ishoal.core.domain.PriceBands;
import com.ishoal.core.persistence.entity.OfferEntity;
import com.ishoal.core.persistence.entity.ProductEntity;

public class OfferEntityAdapter {

    private final PriceBandsEntityAdapter priceBandsEntityAdapter = new PriceBandsEntityAdapter();

    public Offers adapt(List<OfferEntity> offerEntities) {
        if (offerEntities == null) {
            return Offers.emptyOffers();
        }
        return mapToCollection(offerEntities, this::adapt, Offers::over);
    }

    public Offer adapt(OfferEntity offerEntity) {
        if(offerEntity == null) {
            return null;
        }
        return anOffer().id(OfferId.from(offerEntity.getId()))
                .offerReference(OfferReference.from(offerEntity.getOfferReference()))
                .startDateTime(offerEntity.getStartDateTime())
                .endDateTime(offerEntity.getEndDateTime())
                .currentVolume(offerEntity.getCurrentVolume())
                .priceBands(adaptPriceBands(offerEntity))
                .offerId(offerEntity.getId())
                .build();
    }

    private PriceBands adaptPriceBands(OfferEntity offerEntity) {
        return priceBandsEntityAdapter.adapt(offerEntity.getPriceBands());
    }

    public List<OfferEntity> adapt(Offers offers, ProductEntity product) {
        return mapToList(offers, o -> adapt(o, product));
    }

    private OfferEntity adapt(Offer offer, ProductEntity productEntity) {
        OfferEntity entity = new OfferEntity();

        entity.setOfferReference(offer.getOfferReference().asString());
        entity.setId(offer.getOfferId());
        entity.setProduct(productEntity);
        entity.setStartDateTime(offer.getStartDateTime());
        entity.setEndDateTime(offer.getEndDateTime());
        entity.setPriceBands(priceBandsEntityAdapter.adapt(offer.getPriceBands(), entity));
        return entity;
    }

    public List<OfferEntity> adapt(List<OfferEntity> offersEntity, ProductEntity productEntity) {
        List<OfferEntity> offerEntities = null;
        for (OfferEntity offerEntity : offersEntity) {
            DateTime startDateTime =offerEntity.getStartDateTime();
            DateTime endDateTime =offerEntity.getEndDateTime();
            offerEntities = mapToList(productEntity.getOffers(), o -> adapt(o, productEntity,startDateTime, endDateTime));
        }
        return offerEntities;
    }

    private OfferEntity adapt(OfferEntity offerEntity, ProductEntity productEntity, DateTime startDateTime, DateTime endDateTime) {
        offerEntity.setStartDateTime(startDateTime);
        offerEntity.setEndDateTime(endDateTime);
        return offerEntity;
    }
}
