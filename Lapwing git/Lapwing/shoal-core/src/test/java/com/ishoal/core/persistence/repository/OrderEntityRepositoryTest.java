package com.ishoal.core.persistence.repository;

import com.ishoal.core.config.ShoalCoreTestConfiguration;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.core.domain.Product;
import com.ishoal.core.domain.ProductCode;
import com.ishoal.core.products.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("/test.properties")
@ContextConfiguration(classes = { ShoalCoreTestConfiguration.class })
@Sql("/sql/clear-down-data.sql")
@Sql("/sql/user-test-data.sql")
@Sql("/sql/categories-test-data.sql")
@Sql("/sql/vendor-test-data.sql")
@Sql("/sql/vat-test-data.sql")
@Sql("/sql/product-test-data.sql")
@Sql("/sql/offer-test-data.sql")
@Sql("/sql/order-test-data.sql")
@Transactional
public class OrderEntityRepositoryTest {

    private static final ProductCode PRODUCT_CODE = ProductCode.from("HPELITE840");

    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderEntityRepository repository;

    @Test
    public void shouldFindCorrectNumberOfConfirmedOrdersForOffer() {
        assertThat(repository.findOrdersForOfferWithStatus(offerId(), OrderStatus.CONFIRMED), hasSize(3));
    }
    
    @Test
    public void shouldFindCorrectNumberOfRequestedOrdersForOffer() {
        assertThat(repository.findOrdersForOfferWithStatus(offerId(), OrderStatus.PROCESSING), hasSize(1));
    }

    private long offerId() {
        Product product = productService.getProduct(PRODUCT_CODE);
        return product.currentOffer().getId().asLong();
    }
}
