package com.ishoal.core.repository;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ishoal.core.domain.OfferId;
import com.ishoal.core.persistence.entity.OfferEntity;
import com.ishoal.core.persistence.repository.OfferEntityRepository;

@RunWith(MockitoJUnitRunner.class)
public class OfferRepositoryTest {

    @Mock
    private OfferEntityRepository offerEntityRepository;
    
    @Captor
    private ArgumentCaptor<OfferEntity> offerCaptor;
    
    private OfferRepository offerRepository;
    
    @Before
    public void before() {
        this.offerRepository = new OfferRepository(this.offerEntityRepository);
    }
    
    @Test
    public void increaseCurrentVolumeShouldSetNewVolumeCorrectly() {
        when(this.offerEntityRepository.findOne(94L)).thenReturn(offerWithCurrentVolume(75L));
        this.offerRepository.increaseCurrentVolume(OfferId.from(94L), 1450);
        assertThat(persistedOffer().getCurrentVolume(), is(1525L));
    }
    
    @Test
    public void increaseCurrentVolumeShouldReturnNewVolume() {
        when(this.offerEntityRepository.findOne(94L)).thenReturn(offerWithCurrentVolume(75L));
        assertThat(this.offerRepository.increaseCurrentVolume(OfferId.from(94L), 1400), is(1475L));
    }
    
    private OfferEntity persistedOffer() {
        verify(this.offerEntityRepository).save(offerCaptor.capture());
        return this.offerCaptor.getValue();
    }
    
    private OfferEntity offerWithCurrentVolume(long currentVolume) {
        OfferEntity offer = new OfferEntity();
        offer.setCurrentVolume(currentVolume);
        return offer;
    }
}