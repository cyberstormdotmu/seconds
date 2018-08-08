package com.ishoal.core.repository;

import com.ishoal.core.domain.OfferId;
import com.ishoal.core.persistence.entity.OfferEntity;
import com.ishoal.core.persistence.repository.OfferEntityRepository;

public class OfferRepository {

    private final OfferEntityRepository offerEntityRepository;
    
    public OfferRepository(OfferEntityRepository offerEntityRepository) {
        this.offerEntityRepository = offerEntityRepository;
    }
    
    public long increaseCurrentVolume(OfferId offerId, long quantityDelta) {
        OfferEntity offerEntity = find(offerId);
        long newVolume = calculateNewVolume(offerEntity, quantityDelta);
        offerEntity.setCurrentVolume(newVolume);
        this.offerEntityRepository.save(offerEntity);
        return newVolume;
    }

    private long calculateNewVolume(OfferEntity offerEntity, long quantityDelta) {
        return offerEntity.getCurrentVolume() + quantityDelta;
    }

    private OfferEntity find(OfferId offerId) {
        return this.offerEntityRepository.findOne(offerId.asLong());
    }
}