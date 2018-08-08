package com.ishoal.ws.buyer.dto.adapter;

import com.ishoal.core.domain.Product;
import com.ishoal.ws.buyer.dto.ProductDto;
import com.ishoal.ws.buyer.dto.ProductVatRateDto;
import org.joda.time.DateTime;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static com.ishoal.ws.buyer.dto.ProductDto.aProduct;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;


public class ProductDtoAdapterTest {

    private ProductDtoAdapter adapter = new ProductDtoAdapter();

    @Test
    public void shouldAdaptAProductDto() {

        Product product = adapter.adapt(createProductDto().build());

        assertThat(product.getCategory(), is(notNullValue()));
        assertThat(product.getProductSpecs(), is(notNullValue()));
        assertThat(product.getImages(), is(notNullValue()));
        assertThat(product.getOffers(), is(notNullValue()));
        assertThat(product.getOffers().current(), is(notNullValue()));
        assertThat(product.getOffers().current().getPriceBands(), is(notNullValue()));
    }

    private ProductDto.Builder createProductDto() {

        return aProduct()
            .code("code")
            .name("name")
            .description("description")
            .vendorName("vendor")
            .stock(40000L)
            .termsAndConditions("OFFER valid for 1 week only")
            .categories(Collections.emptyList())
            .specifications(Collections.emptyList())
            .images(Collections.emptyList())
            .vatRate(ProductVatRateDto.aVatRateDto().code("").rate(new BigDecimal("1.00")).build())
            .offerStartDate(new DateTime().minusDays(1))
            .offerEndDate(new DateTime().plusDays(1))
            .priceBands(Collections.emptyList());
    }

    @Test
    public void shouldReturnNullIfGivenANullProduct() {

        ProductDto productDto = adapter.adapt((Product) null);

        assertThat(productDto, is(nullValue()));
    }

    @Test
    public void shouldCreateEmptyOffersIfNoOfferInProduct() {
        Product product = adapter.adapt(createProductDto().offerEndDate(null).offerStartDate(null).build());

        assertThat(product.getOffers().size(), is(0));
    }
}