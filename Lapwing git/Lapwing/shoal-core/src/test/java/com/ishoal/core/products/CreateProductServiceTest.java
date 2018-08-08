package com.ishoal.core.products;

import com.ishoal.core.domain.Product;
import com.ishoal.core.repository.ProductRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.ishoal.core.domain.Product.aProduct;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CreateProductServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private ProductRepository productRepository;

    private CreateProductService createProductService;

    private ArgumentCaptor<Product> captor;

    @Before
    public void before() {

        captor = ArgumentCaptor.forClass(Product.class);
        when(productRepository.save(captor.capture())).thenAnswer(invocation -> captor.getValue());

        createProductService = new CreateProductService(productRepository);
    }

    @Test
    public void shouldCallProductRepository_whenProductCreated() {

        Product validProduct = buildFakeValidProduct();
        createProductService.createProduct(validProduct);

        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void shouldRejectAnInvalidProduct() {

        expectedException.expect(InvalidProductException.class);

        Product invalidProduct = buildFakeInvalidProduct();
        createProductService.createProduct(invalidProduct);
    }

    private Product buildFakeValidProduct() {

        return buildFakeValidProduct(aProduct());
    }

    private Product buildFakeValidProduct(Product.Builder builder) {

        Product validProduct = spy(builder.build());
        when(validProduct.isValidForSave()).thenReturn(true);
        return validProduct;
    }

    private Product buildFakeInvalidProduct() {

        Product invalidProduct = spy(aProduct().build());
        when(invalidProduct.isValidForSave()).thenReturn(false);
        return invalidProduct;
    }
}