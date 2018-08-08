package com.ishoal.core.persistence.adapter;

import com.ishoal.core.domain.Offers;
import com.ishoal.core.persistence.entity.OfferEntity;
import com.ishoal.core.persistence.entity.ProductEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.ishoal.core.domain.OfferTestData.anExistingOpenOffer;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class OfferEntityAdapterTest {

    private OfferEntityAdapter adapter;

    @Before
    public void before() {
        this.adapter = new OfferEntityAdapter();
    }

    @Test
    public void adaptNullOfferEntityShouldReturnNull() {
        assertThat(this.adapter.adapt((OfferEntity)null), is(nullValue()));
    }

    @Test
    public void adaptNullOfferEntitiesShouldReturnEmptyOffers() {
        assertThat(this.adapter.adapt((List<OfferEntity>)null).iterator().hasNext(), is(false));
    }

    @Test
    public void shouldAdaptAnOffer() {

        List<OfferEntity> offerEntityList = adapter.adapt(Offers.over(anExistingOpenOffer().build()), new ProductEntity());

        assertThat(offerEntityList, is(notNullValue()));
        assertThat(offerEntityList.size(), is(1));
        assertThat(offerEntityList.get(0).getStartDateTime(), is(notNullValue()));
        assertThat(offerEntityList.get(0).getEndDateTime(), is(notNullValue()));
        assertThat(offerEntityList.get(0).getOfferReference(), is(notNullValue()));
    }

}